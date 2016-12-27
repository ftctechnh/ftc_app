package org.firstinspires.ftc.teamcode;

import android.content.*;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;

import com.qualcomm.robotcore.eventloop.opmode.*;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;

import java.util.HashMap;
import java.util.Iterator;


/**
 * simple example of using a Step that makes a bot with "squirrely wheels" drive along a given course
 * Created by phanau on 10/31/16.
 */


// simple example sequence that tests either of gyro-based AzimuthCountedDriveStep or AzimuthTimedDriveStep to drive along a square path
@Autonomous(name="Testing Color Code", group="Test")
//@Disabled
public class AutonomousTesting extends OpMode {

    AutoLib.Sequence mSequence;             // the root of the sequence tree
    boolean bDone;                          // true when the programmed sequence is done
    DcMotor mMotors[];                      // motors, some of which can be null: assumed order is fr, br, fl, bl

    //VuforiaLib_FTC2016 Vuf;

    BotHardware robot = new BotHardware();

    //some constants to make navigating the field easier
    static final double mmToEncode = 1; //TODO: Find this value
    static final double inchToMm = 25.4;
    static final double footToMm = inchToMm * 12;
    static final double squareToMm = footToMm * 2;

    // create an autonomous sequence with the steps to drive
    static final float power = 0.5f;
    static final float error = 5.0f;       // get us within 10 degrees for this test
    static final float targetZ = 6*25.4f;

    final boolean debug = false;

    final static String TAG = "MouseUSBMess";

    final Context mahContext = FtcRobotControllerActivity.getAppContext();

    @Override
    public void init() {

        UsbManager mManager = (UsbManager) mahContext.getSystemService(Context.USB_SERVICE);
        HashMap<String, UsbDevice> deviceList = mManager.getDeviceList();
        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();

        while (deviceIterator.hasNext())
        {
            UsbDevice device = deviceIterator.next();
            RobotLog.vv(TAG,"Model: " + device.getDeviceName());
            RobotLog.vv(TAG,"ID: " + device.getDeviceId());
            RobotLog.vv(TAG,"Class: " + device.getDeviceClass());
            RobotLog.vv(TAG,"Protocol: " + device.getDeviceProtocol());
            RobotLog.vv(TAG,"Vendor ID " + device.getVendorId());
            RobotLog.vv(TAG,"Product ID: " + device.getProductId());
            RobotLog.vv(TAG,"Interface count: " + device.getInterfaceCount());
            RobotLog.vv(TAG,"---------------------------------------");
            // Get interface details
            for (int index = 0; index < device.getInterfaceCount(); index++)
            {
                UsbInterface mUsbInterface = device.getInterface(index);
                RobotLog.vv(TAG,"  *****     *****");
                RobotLog.vv(TAG,"  Interface index: " + index);
                RobotLog.vv(TAG,"  Interface ID: " + mUsbInterface.getId());
                RobotLog.vv(TAG,"  Inteface class: " + mUsbInterface.getInterfaceClass());
                RobotLog.vv(TAG,"  Interface protocol: " + mUsbInterface.getInterfaceProtocol());
                RobotLog.vv(TAG,"  Endpoint count: " + mUsbInterface.getEndpointCount());
                // Get endpoint details
                for (int epi = 0; epi < mUsbInterface.getEndpointCount(); epi++)
                {
                    UsbEndpoint mEndpoint = mUsbInterface.getEndpoint(epi);
                    RobotLog.vv(TAG,"    ++++   ++++   ++++");
                    RobotLog.vv(TAG,"    Endpoint index: " + epi);
                    RobotLog.vv(TAG,"    Attributes: " + mEndpoint.getAttributes());
                    RobotLog.vv(TAG,"    Direction: " + mEndpoint.getDirection());
                    RobotLog.vv(TAG,"    Number: " + mEndpoint.getEndpointNumber());
                    RobotLog.vv(TAG,"    Interval: " + mEndpoint.getInterval());
                    RobotLog.vv(TAG,"    Packet size: " + mEndpoint.getMaxPacketSize());
                    RobotLog.vv(TAG,"    Type: " + mEndpoint.getType());
                }
            }
        }
        RobotLog.vv(TAG," No more devices connected.");

        //robot.init(this, debug);

        //Vuf = new VuforiaLib_FTC2016();
        //Vuf.init(this, null);     // pass it this OpMode (so it can do telemetry output) and use its license key for now

        // start out not-done
        bDone = false;

        //robot.startNavX();
    }

    @Override
    public void start(){
        //Vuf.start();
    }

    @Override
    public void loop() {

        // until we're done, keep looping through the current Step(s)
        //if (!bDone)
        //    bDone = mSequence.loop();       // returns true when we're done
        //else
        //    telemetry.addData("First sequence finished", "");

        //Vuf.loop(true);
        telemetry.addData("Heading", robot.distSensor.getUltrasonicLevel());

        //telemetry.addData("Red R", robot.rightSensor.red());
        //telemetry.addData("Blue R", robot.rightSensor.blue());
        //telemetry.addData("Green R", robot.rightSensor.green());
    }

    @Override
    public void stop() {
        super.stop();
        //Vuf.stop();
    }
}

//deal with this sometime

/*
UsbManager usbManager;
UsbDevice usbDevice;

private void connect() {
    this.usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
    HashMap<String, UsbDevice> deviceList = usbManager.getDeviceList();

    // just get the first enumerated USB device
    Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
    if (deviceIterator.hasNext()) {
        this.usbDevice = deviceIterator.next();
    }

    if (usbDevice == null) {
        Log.w(TAG, "no USB device found");
        return;
    }

    // ask for permission

    final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice device = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);

                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if(device != null){
                            // call method to set up device communication
                            Log.i(TAG, "permission granted. access mouse.");

                            // repeat in a different thread
                            transfer(device);
                        }
                    }
                    else {
                        Log.d(TAG, "permission denied for device " + device);
                    }
                }
            } else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
                UsbDevice device = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                if (device != null) {
                    // TODO:
                    // call your method that cleans up and closes communication with the device
                    // usbInterface.releaseInterface();
                    // usbDeviceConnection.close();
                }
            }
        }
    };

    PendingIntent mPermissionIntent = PendingIntent.getBroadcast(context, 0, new Intent(ACTION_USB_PERMISSION), 0);
    IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
    context.registerReceiver(mUsbReceiver, filter);

    usbManager.requestPermission(usbDevice, mPermissionIntent);
}

private void transfer(UsbDevice device) {

    int TIMEOUT = 0;
    boolean forceClaim = true;

    // just grab the first endpoint
    UsbInterface intf = device.getInterface(0);
    UsbEndpoint endpoint = intf.getEndpoint(0);
    UsbDeviceConnection connection = this.usbManager.openDevice(device);
    connection.claimInterface(intf, forceClaim);

    byte[] bytes = new byte[endpoint.getMaxPacketSize()];

    connection.bulkTransfer(endpoint, bytes, bytes.length, TIMEOUT);

    // depending on mouse firmware and vendor the information you're looking for may
    // be in a different order or position. For some logitech devices the following
    // is true:

    int x = (int) bytes[1];
    int y = (int) bytes[2];
    int scrollwheel = (int) bytes[3]

    // call a listener, process your data ...
}
 */

