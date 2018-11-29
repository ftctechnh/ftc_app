package org.upacreekrobotics.eventloop;

import android.app.Activity;
import android.hardware.usb.UsbDevice;
import android.os.Build;

import com.qualcomm.ftccommon.CommandList;
import com.qualcomm.ftccommon.FtcEventLoopBase;
import com.qualcomm.ftccommon.ProgrammingModeController;
import com.qualcomm.ftccommon.UpdateUI;
import com.qualcomm.ftccommon.UsbModuleAttachmentHandler;
import com.qualcomm.ftccommon.configuration.FtcConfigurationActivity;
import com.qualcomm.ftccommon.configuration.USBScanManager;
import com.qualcomm.hardware.HardwareFactory;
import com.qualcomm.robotcore.eventloop.EventLoopManager;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpModeRegister;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.LynxModuleMetaList;
import com.qualcomm.robotcore.hardware.ScannedDevices;
import com.qualcomm.robotcore.hardware.configuration.Utility;
import com.qualcomm.robotcore.hardware.usb.RobotArmingStateNotifier;
import com.qualcomm.robotcore.hardware.usb.RobotUsbModule;
import com.qualcomm.robotcore.robocol.Command;
import com.qualcomm.robotcore.robocol.TelemetryMessage;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.SerialNumber;
import com.qualcomm.robotcore.util.ThreadPool;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.internal.camera.CameraManagerInternal;
import org.firstinspires.ftc.robotcore.internal.ftdi.FtDevice;
import org.firstinspires.ftc.robotcore.internal.ftdi.FtDeviceIOException;
import org.firstinspires.ftc.robotcore.internal.ftdi.FtDeviceManager;
import org.firstinspires.ftc.robotcore.internal.network.CallbackResult;
import org.firstinspires.ftc.robotcore.internal.opmode.OpModeManagerImpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Main event loop to control the robot
 * <p>
 * Modify this class with your own code, or create your own event loop by
 * implementing EventLoop.
 */
@SuppressWarnings("WeakerAccess")
public class OurEventLoop extends FtcEventLoopBase {

    //------------------------------------------------------------------------------------------------
    // State
    //------------------------------------------------------------------------------------------------

    protected final Utility utility;
    protected final OpModeManagerImpl opModeManager;
    protected UsbModuleAttachmentHandler usbModuleAttachmentHandler;
    protected final Map<String, Long> recentlyAttachedUsbDevices;  // serialNumber -> when to attach
    protected final AtomicReference<OpMode> opModeStopRequested;

    //OUR CODE
    private static Activity activity;
    private static EventLoopManager eventLoopManager;

    //------------------------------------------------------------------------------------------------
    // Construction
    //------------------------------------------------------------------------------------------------

    public OurEventLoop(HardwareFactory hardwareFactory, OpModeRegister userOpmodeRegister, UpdateUI.Callback callback, Activity activityContext, ProgrammingModeController programmingModeController) {
        super(hardwareFactory, userOpmodeRegister, callback, activityContext, programmingModeController);

        //OUR CODE
        activity = activityContext;

        this.opModeManager = createOpModeManager(activityContext);
        this.usbModuleAttachmentHandler = new DefaultUsbModuleAttachmentHandler();
        this.recentlyAttachedUsbDevices = new ConcurrentHashMap<String, Long>();
        this.opModeStopRequested = new AtomicReference<OpMode>();
        this.utility = new Utility(activityContext);
    }

    protected static OpModeManagerImpl createOpModeManager(Activity activityContext) {
        return new OpModeManagerImpl(activityContext, new HardwareMap(activityContext));
    }

    //OUR CODE
    public static OpModeManagerImpl createUserOpModeManager(){
        OpModeManagerImpl tempManager = new OpModeManagerImpl(activity, new HardwareMap(activity));
        tempManager.init(eventLoopManager);
        return tempManager;
    }

    //------------------------------------------------------------------------------------------------
    // Accessors
    //------------------------------------------------------------------------------------------------

    public OpModeManagerImpl getOpModeManager() {
        return opModeManager;
    }

    public UsbModuleAttachmentHandler getUsbModuleAttachmentHandler() {
        return this.usbModuleAttachmentHandler;
    }

    public void setUsbModuleAttachmentHandler(UsbModuleAttachmentHandler handler) {
        this.usbModuleAttachmentHandler = handler;
    }

    //------------------------------------------------------------------------------------------------
    // Core Event Loop
    //------------------------------------------------------------------------------------------------

    /**
     * Init method
     * <p>
     * This code will run when the robot first starts up. Place any initialization code in this
     * method.
     * <p>
     * It is important to save a copy of the event loop manager from this method, as that is how
     * you'll get access to the gamepad.
     * <p>
     * If an Exception is thrown then the event loop manager will not start the robot.
     * <p>
     * Caller synchronizes: called on RobotSetupRunnable.run() thread
     */
    @Override
    public void init(EventLoopManager eventLoopManager) throws RobotCoreException, InterruptedException {
        RobotLog.ii(TAG, "======= INIT START =======");
        super.init(eventLoopManager);

        //OUR CODE
        this.eventLoopManager = eventLoopManager;

        opModeManager.init(eventLoopManager);
        registeredOpModes.registerAllOpModes(userOpmodeRegister);
        sendUIState();

        ftcEventLoopHandler.init(eventLoopManager);
        HardwareMap hardwareMap = ftcEventLoopHandler.getHardwareMap();

        opModeManager.setHardwareMap(hardwareMap);
        hardwareMap.logDevices();

        RobotLog.ii(TAG, "======= INIT FINISH =======");
    }

    /**
     * Loop method, this will be called repeatedly while the robot is running.
     * <p>
     *
     * @see com.qualcomm.robotcore.eventloop.EventLoop#loop()
     * <p>
     * Caller synchronizes: called on EventLoopRunnable.run() thread.
     */
    @Override
    public void loop() throws RobotCoreException {

        // Atomically capture the OpMode to stop, if any
        OpMode opModeToStop = opModeStopRequested.getAndSet(null);
        if (opModeToStop != null) {
            processOpModeStopRequest(opModeToStop);
        }

        checkForChangedOpModes();

        ftcEventLoopHandler.displayGamePadInfo(opModeManager.getActiveOpModeName());
        Gamepad gamepads[] = ftcEventLoopHandler.getGamepads();

        opModeManager.runActiveOpMode(gamepads);
    }

    @Override
    public void refreshUserTelemetry(TelemetryMessage telemetry, double sInterval) {
        ftcEventLoopHandler.refreshUserTelemetry(telemetry, sInterval);
    }

    /**
     * Teardown method
     * <p>
     * This method will be called when the robot is being shut down. This method should stop the robot. There will be no more changes to write
     * to the hardware after this method is called.
     * <p>
     * If an exception is thrown, then the event loop manager will attempt to shut down the robot
     * without the benefit of this method.
     * <p>
     *
     * @see com.qualcomm.robotcore.eventloop.EventLoop#teardown()
     * <p>
     * Caller synchronizes: called on EventLoopRunnable.run() thread.
     */
    @Override
    public void teardown() throws RobotCoreException, InterruptedException {
        RobotLog.ii(TAG, "======= TEARDOWN =======");

        super.teardown();

        opModeManager.stopActiveOpMode();
        opModeManager.teardown();

        ftcEventLoopHandler.close();

        RobotLog.ii(TAG, "======= TEARDOWN COMPLETE =======");
    }

    /**
     * If the driver station sends over a command, it will be routed to this method. You can choose
     * what to do with this command, or you can just ignore it completely.
     * <p>
     * Called on RecvRunnable.run() thread. Method is thread safe, non-interfering
     */
    @Override
    public CallbackResult processCommand(Command command) throws InterruptedException, RobotCoreException {
        ftcEventLoopHandler.sendBatteryInfo();

        CallbackResult result = super.processCommand(command);
        if (!result.stopDispatch()) {
            CallbackResult localResult = CallbackResult.HANDLED;

            String name = command.getName();
            String extra = command.getExtra();

            if (name.equals(CommandList.CMD_INIT_OP_MODE)) {
                handleCommandInitOpMode(extra);
            } else if (name.equals(CommandList.CMD_RUN_OP_MODE)) {
                handleCommandRunOpMode(extra);
            } else if (name.equals(CommandList.CMD_SCAN)) {
                handleCommandScan(extra);
            } else if (name.equals(CommandList.CMD_DISCOVER_LYNX_MODULES)) {
                handleCommandDiscoverLynxModules(extra);
            } else if (name.equals(CommandList.CMD_SET_MATCH_NUMBER)) {
                handleCommandSetMatchNumber(extra);
            } else {
                localResult = CallbackResult.NOT_HANDLED;
            }
            if (localResult == CallbackResult.HANDLED) {
                result = localResult;
            }
        }
        return result;
    }

    /**
     * @see FtcConfigurationActivity#doUSBScanAndUpdateUI()
     */
    protected void handleCommandScan(String extra) throws RobotCoreException, InterruptedException {
        RobotLog.vv(FtcConfigurationActivity.TAG, "handling command SCAN");

        final USBScanManager usbScanManager = startUsbScanMangerIfNecessary();

        // Start a scan and wait for it to complete, but if a scan is already in progress, then just wait for that one to finish
        final ThreadPool.SingletonResult<ScannedDevices> future = usbScanManager.startDeviceScanIfNecessary();

        // Actually carry out the scan in a worker thread so that we don't hold up the receive loop for
        // half-second or so that carrying out the scan will take.
        ThreadPool.getDefault().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    ScannedDevices scannedDevices = future.await();
                    if (scannedDevices == null) scannedDevices = new ScannedDevices();

                    // Package up the raw scanned device info and send that back to the DS
                    String data = usbScanManager.packageCommandResponse(scannedDevices);
                    RobotLog.vv(FtcConfigurationActivity.TAG, "handleCommandScan data='%s'", data);
                    networkConnectionHandler.sendCommand(new Command(CommandList.CMD_SCAN_RESP, data));

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
    }

    protected void handleCommandDiscoverLynxModules(String extra) throws RobotCoreException, InterruptedException {
        RobotLog.vv(FtcConfigurationActivity.TAG, "handling command DiscoverLynxModules");
        final SerialNumber serialNumber = SerialNumber.fromString(extra);

        final USBScanManager usbScanManager = startUsbScanMangerIfNecessary();

        // Start a scan and wait for it to complete, but if a scan is already in progress, then just wait for that one to finish
        final ThreadPool.SingletonResult<LynxModuleMetaList> future = this.usbScanManager.startLynxModuleEnumerationIfNecessary(serialNumber);

        // Actually carry out the scan in a worker thread so that we don't hold up the receive loop for
        // full second or more that carrying out the discovery will take.
        ThreadPool.getDefault().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    LynxModuleMetaList lynxModules = future.await();
                    if (lynxModules == null) lynxModules = new LynxModuleMetaList(serialNumber);

                    // Package up the raw module list and send that back to the DS
                    String data = usbScanManager.packageCommandResponse(lynxModules);
                    RobotLog.vv(FtcConfigurationActivity.TAG, "DiscoverLynxModules data='%s'", data);
                    networkConnectionHandler.sendCommand(new Command(CommandList.CMD_DISCOVER_LYNX_MODULES_RESP, data));

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
    }

    /**
     * The driver station is requesting our opmode list/ UI state. Build up an appropriately-delimited
     * list and send it back to them. Also take this opportunity to forcibly refresh their error/warning
     * state: the opmode list is only infrequently requested (so sending another datagram isn't
     * a traffic burden) and it's requested just after a driver station reconnects after a disconnect
     * (so doing the refresh now is probably an opportune thing to do).
     */
    protected void sendUIState() {

        super.sendUIState();

        EventLoopManager manager = ftcEventLoopHandler.getEventLoopManager();
        if (manager != null)
            manager.refreshSystemTelemetryNow(); // null check is paranoia, need isn't verified
    }

    /*
     * handleCommandSetMatchNumber
     *
     * Cache the match number in the opMode manager.
     */
    protected void handleCommandSetMatchNumber(String extra) {
        try {
            opModeManager.setMatchNumber(Integer.parseInt(extra));
        } catch (NumberFormatException e) {
            RobotLog.logStackTrace(e);
        }
    }

    protected void handleCommandInitOpMode(String extra) {
        String newOpMode = ftcEventLoopHandler.getOpMode(extra);
        opModeManager.initActiveOpMode(newOpMode);
    }

    protected void handleCommandRunOpMode(String extra) {
        // Make sure we're in the opmode that the DS thinks we are
        String newOpMode = ftcEventLoopHandler.getOpMode(extra);
        if (!opModeManager.getActiveOpModeName().equals(newOpMode)) {
            opModeManager.initActiveOpMode(newOpMode);
        }
        opModeManager.startActiveOpMode();
    }

    @Override
    public void requestOpModeStop(OpMode opModeToStopIfActive) {
        // Note that this may be called from any thread, including an OpMode's loop() thread.
        opModeStopRequested.set(opModeToStopIfActive);
    }

    private void processOpModeStopRequest(OpMode opModeToStop) {
        // Called on the event loop thread. If the currently active OpMode is the one indicated,
        // we are to stop it and set up the default.

        if (opModeToStop != null && this.opModeManager.getActiveOpMode() == opModeToStop) {

            RobotLog.ii(TAG, "auto-stopping OpMode '%s'", this.opModeManager.getActiveOpModeName());

            // Call stop on the currently active OpMode and init the default one
            this.opModeManager.stopActiveOpMode();
        }
    }


    //------------------------------------------------------------------------------------------------
    // Usb connection management
    //------------------------------------------------------------------------------------------------

    /**
     * Deal with the fact that a UsbDevice has recently attached to the system
     *
     * @param usbDevice
     */
    @Override
    public void onUsbDeviceAttached(UsbDevice usbDevice) {
        // Find out who it is.
        SerialNumber serialNumber = getSerialNumberOfUsbDevice(usbDevice);

        // Remember whoever it was for later
        if (serialNumber != null) {
            pendUsbDeviceAttachment(serialNumber, 0, TimeUnit.MILLISECONDS);
        } else {
            // We don't actually understand under what conditions we'll be unable to open an
            // FT_Device for which we're actually receiving a change notification: who else
            // would have it open (for example)? We'd like to do something more, but don't
            // have an idea of what that would look like.
            //
            // 2018.06.01: It is suspected that the serial number being returned as null was
            // a consequence of a race between USB attachment notifications here and in FtDeviceManager.
            RobotLog.ee(TAG, "ignoring: unable get serial number of attached UsbDevice vendor=0x%04x, product=0x%04x device=0x%04x name=%s",
                    usbDevice.getVendorId(), usbDevice.getProductId(), usbDevice.getDeviceId(), usbDevice.getDeviceName());
        }
    }

    protected SerialNumber getSerialNumberOfUsbDevice(UsbDevice usbDevice) {
        SerialNumber serialNumber = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            serialNumber = SerialNumber.fromStringOrNull(usbDevice.getSerialNumber());
        }

        if (serialNumber == null) {
            // Don't need this branch any more, but left in for now to preserve code paths. Remove after further testing.
            FtDevice ftDevice = null;
            try {
                FtDeviceManager manager = FtDeviceManager.getInstance(this.activityContext); // note: we're not supposed to close this
                ftDevice = manager.openByUsbDevice(this.activityContext, usbDevice);
                if (ftDevice != null) {
                    serialNumber = SerialNumber.fromStringOrNull(ftDevice.getDeviceInfo().serialNumber);
                }
            } catch (RuntimeException | FtDeviceIOException e) {  // RuntimeException is paranoia
                // ignored
            } finally {
                if (ftDevice != null)
                    ftDevice.close();
            }
        }

        if (serialNumber == null) { // non FTDI devices on KitKat, or devices that simply lack a serial number
            try {
                CameraManagerInternal cameraManagerInternal = (CameraManagerInternal) ClassFactory.getInstance().getCameraManager();
                serialNumber = cameraManagerInternal.getRealOrVendorProductSerialNumber(usbDevice);
            } catch (RuntimeException e) {
                // ignore
            }
        }

        return serialNumber;
    }

    @Override
    public void pendUsbDeviceAttachment(SerialNumber serialNumber, long time, TimeUnit unit) {
        long nsDeadline = time == 0L ? 0L : System.nanoTime() + unit.toNanos(time);
        this.recentlyAttachedUsbDevices.put(serialNumber.getString(), nsDeadline);
    }

    /**
     * Process any usb devices that might have recently attached.
     * Called on the event loop thread.
     */
    @Override
    public void processedRecentlyAttachedUsbDevices() throws RobotCoreException, InterruptedException {

        // Snarf a copy of the set of serial numbers
        Set<String> serialNumbersToProcess = new HashSet<String>();
        long now = System.nanoTime();

        for (Map.Entry<String, Long> pair : this.recentlyAttachedUsbDevices.entrySet()) {
            if (pair.getValue() <= now) {
                serialNumbersToProcess.add(pair.getKey());
                this.recentlyAttachedUsbDevices.remove(pair.getKey());
            }
        }

        // If we have a handler, and there's something to handle, then handle it
        if (this.usbModuleAttachmentHandler != null && !serialNumbersToProcess.isEmpty()) {

            // Find all the UsbModules in the current hardware map
            List<RobotUsbModule> modules = this.ftcEventLoopHandler.getHardwareMap().getAll(RobotUsbModule.class);

            // For each serial number, find the module with that serial number and ask the handler to deal with it
            for (String serialNumberString : new ArrayList<>(serialNumbersToProcess)) {
                SerialNumber serialNumberAttached = SerialNumber.fromString(serialNumberString);
                boolean found = false;
                for (RobotUsbModule module : modules) {
                    if (serialNumberAttached.matches(module.getSerialNumber())) {
                        if (module.getArmingState() != RobotArmingStateNotifier.ARMINGSTATE.ARMED) {
                            serialNumbersToProcess.remove(serialNumberString);
                            handleUsbModuleAttach(module);
                            found = true;
                            break;
                        }
                    }
                }
                if (!found) {
                    RobotLog.vv(TAG, "processedRecentlyAttachedUsbDevices(): %s not in hwmap; ignoring", serialNumberAttached);
                }
            }
        }
    }

    @Override
    public void handleUsbModuleDetach(RobotUsbModule module) throws RobotCoreException, InterruptedException {
        // Called on the event loop thread
        UsbModuleAttachmentHandler handler = this.usbModuleAttachmentHandler;
        if (handler != null)
            handler.handleUsbModuleDetach(module);
    }

    public void handleUsbModuleAttach(RobotUsbModule module) throws RobotCoreException, InterruptedException {
        // Called on the event loop thread
        UsbModuleAttachmentHandler handler = this.usbModuleAttachmentHandler;
        if (handler != null)
            handler.handleUsbModuleAttach(module);
    }

    public class DefaultUsbModuleAttachmentHandler implements UsbModuleAttachmentHandler {

        @Override
        public void handleUsbModuleAttach(RobotUsbModule module) throws RobotCoreException, InterruptedException {

            String id = nameOfUsbModule(module);

            RobotLog.ii(TAG, "vv===== MODULE ATTACH: disarm %s=====vv", id);
            module.disarm();

            RobotLog.ii(TAG, "======= MODULE ATTACH: arm or pretend %s=======", id);
            module.armOrPretend();

            RobotLog.ii(TAG, "^^===== MODULE ATTACH: complete %s=====^^", id);
        }

        @Override
        public void handleUsbModuleDetach(RobotUsbModule module) throws RobotCoreException, InterruptedException {
            // This provides the default policy for dealing with hardware modules that have experienced
            // abnormal termination because they, e.g., had their USB cable disconnected.

            String id = nameOfUsbModule(module);

            RobotLog.ii(TAG, "vv===== MODULE DETACH RECOVERY: disarm %s=====vv", id);

            // First thing we do is disarm the module. That will put it in a nice clean state.
            module.disarm();

            RobotLog.ii(TAG, "======= MODULE DETACH RECOVERY: pretend %s=======", id);

            // Next, to make it appear more normal and functional to its clients, we make it pretend
            module.pretend();

            RobotLog.ii(TAG, "^^===== MODULE DETACH RECOVERY: complete %s=====^^", id);
        }

        String nameOfUsbModule(RobotUsbModule module) {
            return HardwareFactory.getDeviceDisplayName(activityContext, module.getSerialNumber());
        }
    }
}