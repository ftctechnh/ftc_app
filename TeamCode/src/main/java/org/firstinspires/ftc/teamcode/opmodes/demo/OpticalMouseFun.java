package org.firstinspires.ftc.teamcode.opmodes.demo;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.*;

import com.qualcomm.robotcore.eventloop.opmode.*;
import com.qualcomm.robotcore.util.RobotLog;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Noah on 3/19/2017.
 * This opmode should be a testbed for a usb optical mouse library
 * by the time I read this again however there is a high likleyhood it will be something entirely different
 * I never update my comments
 *
 * code stolen from: http://stackoverflow.com/questions/13280402/reading-raw-mouse-data-on-android
 */

//@Autonomous(name="USB Mouse Fun", group ="Test")

public class OpticalMouseFun extends OpMode {
    UsbManager usbManager;
    UsbDevice usbDevice;

    private static final String usbTAG = "Mouse Fun";
    private Context context;

    @OpModeRegistrar
    public static void register(Context huh, OpModeManager manage){
        OpModeMeta meta = new OpModeMeta("USB Mouse Fun", OpModeMeta.Flavor.AUTONOMOUS, "Test");
        manage.register(meta, OpticalMouseFun.class);
    }

    private void connect() {
        this.usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        HashMap<String, UsbDevice> deviceList = usbManager.getDeviceList();

        // just get the first enumerated USB device
        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();

        RobotLog.v(usbTAG, deviceIterator.toString());

        if (deviceIterator.hasNext()) {
            this.usbDevice = deviceIterator.next();
        }

        if (usbDevice == null) {
            RobotLog.v(usbTAG, "no USB device found");
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
                                RobotLog.v(usbTAG, "permission granted. access mouse.");

                                // repeat in a different thread
                                transfer(device);
                            }
                        }
                        else {
                            RobotLog.e(usbTAG, "permission denied for device " + device);
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
        int scrollwheel = (int) bytes[3];

        RobotLog.v(usbTAG, "X: " + x + " Y: " + y + " S: " + scrollwheel);

        // call a listener, process your data ...
    }

    @Override
    public void init(){
        connect();
    }

    @Override
    public void init_loop(){

    }

    @Override
    public void start(){

    }

    @Override
    public void loop(){

    }

    @Override
    public void stop(){

    }
}
