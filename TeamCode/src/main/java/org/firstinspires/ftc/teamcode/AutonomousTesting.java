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

    BotHardware bot;

    @Override
    public void init(){
        bot = new BotHardware();

        bot.init(this, false);

    }

    @Override
    public void start(){
        //Vuf.start();
    }

    @Override
    public void loop() {

        telemetry.addData("Color Sensor Left Red", bot.leftSensor.red());
        telemetry.addData("Color Sensor Left Blue", bot.leftSensor.blue());
        telemetry.addData("Color Sensor Right Red", bot.rightSensor.red());
        telemetry.addData("Color Sensor Right Blue", bot.rightSensor.blue());

        // until we're done, keep looping through the current Step(s)
        //if (!bDone)
        //    bDone = mSequence.loop();       // returns true when we're done
        //else
        //    telemetry.addData("First sequence finished", "");

        //Vuf.loop(true);
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

