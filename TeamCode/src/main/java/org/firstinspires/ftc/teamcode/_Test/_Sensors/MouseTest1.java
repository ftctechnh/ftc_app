package org.firstinspires.ftc.teamcode._Test._Sensors;


import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.input.InputManager;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.view.InputDevice;
import android.view.MotionEvent;
import android.view.View;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;

import java.util.HashMap;
import java.util.Iterator;


/**
 * A test example opmode to read and display mouse input
 * Created by phanau on 12/26/16.
 * see also simpler version in ftc_app-master-2 MouseListenTestOp
 */

// http://stackoverflow.com/questions/13280402/reading-raw-mouse-data-on-android
// this doesn't currently work - it always returns "no USB device found"
class MyUsbMouse {

    OpMode mOpMode;

    public float mMouseX, mMouseY;

    UsbManager usbManager;
    UsbDevice usbDevice;
    String TAG = "MyUsbMouse: ";

    MyUsbMouse(OpMode opmode)
    {
        mOpMode = opmode;
    }

    public void connect() {
        Context context = mOpMode.hardwareMap.appContext;

        this.usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        HashMap<String, UsbDevice> deviceList = usbManager.getDeviceList();

        // just get the first enumerated USB device
        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
        if (deviceIterator.hasNext()) {
            this.usbDevice = deviceIterator.next();
        }

        if (usbDevice == null) {
            mOpMode.telemetry.addData(TAG, "no USB device found");
            return;
        }

        // ask for permission

        final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
        final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {

            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (ACTION_USB_PERMISSION.equals(action)) {
                    synchronized (this) {
                        UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);

                        if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                            if (device != null) {
                                // call method to set up device communication
                                mOpMode.telemetry.addData(TAG, "permission granted. access mouse.");

                                // repeat in a different thread
                                transfer(device);
                            }
                        } else {
                            mOpMode.telemetry.addData(TAG, "permission denied for device " + device);
                        }
                    }
                } else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
                    UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (device != null) {
                        disconnect();
                    }
                }
            }
        };

        PendingIntent mPermissionIntent = PendingIntent.getBroadcast(context, 0, new Intent(ACTION_USB_PERMISSION), 0);
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        context.registerReceiver(mUsbReceiver, filter);

        usbManager.requestPermission(usbDevice, mPermissionIntent);
    }

    public boolean loop() {
        return false;
    }

    public void disconnect()
    {
        if (usbDevice != null) {
            UsbInterface usbInterface = usbDevice.getInterface(0);
            // usbInterface.releaseInterface();

            UsbDeviceConnection connection = this.usbManager.openDevice(usbDevice);
            if (connection != null)
                connection.close();
        }
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

        mMouseX = (int) bytes[1];
        mMouseY = (int) bytes[2];
    }

}


class MyMouseInputDevice {

    OpMode mOpMode;
    InputDevice mMouseDevice = null;

    public float mMouseX, mMouseY, mMouseRawX, mMouseRawY;

    MyMouseInputDevice(OpMode opmode)
    {
        mOpMode = opmode;
    }

    public void connect()
    {
        // get info about the Mouse device (not useful, but interesting)
        if(Build.VERSION.SDK_INT > 15) {
            Context context = mOpMode.hardwareMap.appContext;
            InputManager inptmgr = (InputManager)context.getSystemService(context.INPUT_SERVICE);
            int[] inputs = inptmgr.getInputDeviceIds();
            for(int i = 0;i<inputs.length;i++) {
                InputDevice device = inptmgr.getInputDevice(inputs[i]);
                String devicename = device.getName();
                if(devicename.toLowerCase().contains("mouse")) {
                    mOpMode.telemetry.addData("MyMouseInputDevice: ", device.toString());
                    mOpMode.telemetry.addData("MyMouseInputDevice: ", "  vendor: %d  product: %d", device.getVendorId(), device.getProductId());
                    mMouseDevice = device;
                }
            }
        }

        // install a GenericMotionListener on the screen of the FtcRobotControllerActivity --
        // requires modified FtcRobotControllerActivity.java that exposes static ref to itself as ftcRCact
        /*
        Activity ftcRCact = FtcRobotControllerActivity.ftcRCact;
        View view = ftcRCact.findViewById(com.qualcomm.ftcrobotcontroller.R.id.entire_screen);
        view.setOnGenericMotionListener(new View.OnGenericMotionListener() {
            @Override
            public boolean onGenericMotion(View v, MotionEvent event) {
                mMouseX = event.getX();
                mMouseY = event.getY();
                mMouseRawX = event.getRawX();
                mMouseRawY = event.getRawY();
                return false;
            }
        });
         */
    }

    public boolean loop() {
        return false;
    }

    public void disconnect()
    {
        // remove the GenericMotionListener from the Activity's View
        /*
        Activity ftcRCact = FtcRobotControllerActivity.ftcRCact;
        View view = ftcRCact.findViewById(com.qualcomm.ftcrobotcontroller.R.id.entire_screen);
        view.setOnGenericMotionListener(null);
         */
    }

}


@Autonomous(name="Test: Mouse Test 1", group ="Test")
//@Disabled
public class MouseTest1 extends OpMode {

    MyUsbMouse mMyUsbMouse;
    MyMouseInputDevice mMyMouseInputDevice;

    public MouseTest1() {
    }

    public void init() {
        // try to get raw USB device for mouse
        mMyUsbMouse = new MyUsbMouse(this);
        mMyUsbMouse.connect();

        // also try to get mouse as an input device
        mMyMouseInputDevice = new MyMouseInputDevice(this);
        mMyMouseInputDevice.connect();
    }

    public void loop() {
        mMyUsbMouse.loop();
        telemetry.addData("MyUsbMouse X Y: ", "%4.2f %4.2f", mMyUsbMouse.mMouseX, mMyUsbMouse.mMouseY);

        mMyMouseInputDevice.loop();
        telemetry.addData("MyMouseInputDevice X Y: ", "%4.2f %4.2f", mMyMouseInputDevice.mMouseX, mMyMouseInputDevice.mMouseY);
        telemetry.addData("MyMouseInputDevice raw X Y: ", "%4.2f %4.2f", mMyMouseInputDevice.mMouseRawX, mMyMouseInputDevice.mMouseRawY);
    }

    public void stop() {
        telemetry.addData("stop() called", "");

        mMyUsbMouse.disconnect();
        mMyMouseInputDevice.disconnect();
    }

}


/*
references:
http://stackoverflow.com/questions/26520105/usb-host-getdevicelist-return-empty?rq=1
http://stackoverflow.com/questions/13280402/reading-raw-mouse-data-on-android
http://stackoverflow.com/questions/19995876/connecting-a-usb-device-to-android?rq=1

https://developer.android.com/guide/topics/connectivity/usb/host.html
https://developer.android.com/reference/android/view/MotionEvent.html
*/