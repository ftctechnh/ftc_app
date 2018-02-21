package org.firstinspires.ftc.teamcode.opmodes.demo;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.qualcomm.robotcore.eventloop.opmode.*;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.libraries.interfaces.PointHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

/**
 * Created by Noah on 4/9/2017.
 * Opmode which should take optical mouse input and draw its path on a canvas
 * to see how accurate an optical mouse tracker actually is
 */

@Autonomous(name = "Optical Mouse Path Drawing Demo", group = "Test")
@Disabled
public class OpticalMouseCanvasFun extends OpMode implements PointHandler{

    private final RelativeLayout layout = (RelativeLayout)((Activity)hardwareMap.appContext).findViewById(com.qualcomm.ftcrobotcontroller.R.id.CheapCamera);
    private LineView lineView;
    private UsbManager usbManager;
    private UsbDevice usbDevice;
    private UsbEndpoint endpoint;
    private UsbDeviceConnection usbDeviceConnection;
    private final String TAG = "Optical Mouse Line";

    private LinkedList<Double> pts = new LinkedList<>();
    private double lastTime = 0;

    private final PointHandler pointer = this;

    @Override
    public void init(){
        telemetry.addData("Canvas test!", "Go!");
        telemetry.update();

        //usb stuff
        connect(hardwareMap.appContext);

        //canvas stuff
        Runnable doCanvasSetup = new Runnable() {
            @Override
            public void run() {
                Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                paint.setColor(Color.RED);
                lineView = new LineView(hardwareMap.appContext, pointer, paint);

                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                layout.addView(lineView, params);
            }
        };

        layout.getHandler().post(doCanvasSetup);

        //start out at 0,0
        pts.add(0.0);
        pts.add(0.0);
    }

    @Override
    public void init_loop(){

    }

    @Override
    public void start(){

    }

    @Override
    public void loop(){
        int TIMEOUT = 10;
        byte[] bytes = new byte[endpoint.getMaxPacketSize()];

        usbDeviceConnection.bulkTransfer(endpoint, bytes, bytes.length, TIMEOUT);

        // depending on mouse firmware and vendor the information you're looking for may
        // be in a different order or position. For some logitech devices the following
        // is true:

        int x = (int) bytes[1];
        int y = (int) bytes[2];
        int scrollwheel = (int) bytes[3];

        telemetry.addData(TAG, "X: " + x + " Y: " + y + " S: " + scrollwheel);

        if((x | y) > 0){
            //integrate position to velocity
            if(lastTime == 0)lastTime = getRuntime();
            double deltaT = (getRuntime() - lastTime) / 1000.0;
            //and add it to the array
            pts.add(pts.get(pts.size() - 2) + deltaT * x);
            pts.add(pts.get(pts.size() - 2) + deltaT * y);


        }

        telemetry.addData("Point", "x: " + pts.get(pts.size() - 1) + " y: " + pts.get(pts.size() - 2));

        if(pts.size() > 5000){
            pts.removeLast();
            pts.removeLast();
        }
    }

    @Override
    public void stop(){
        Runnable hideChart = new Runnable() {
            @Override
            public void run() {
                layout.removeView(lineView);
            }
        };

        layout.getHandler().post(hideChart);
    }

    private void connect(Context context) {
        this.usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        HashMap<String, UsbDevice> deviceList = usbManager.getDeviceList();

        // just get the first enumerated USB device
        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();

        RobotLog.v(TAG, deviceIterator.toString());

        if (deviceIterator.hasNext()) {
            this.usbDevice = deviceIterator.next();
        }

        if (usbDevice == null) {
            RobotLog.v(TAG, "no USB device found");
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
                                RobotLog.v(TAG, "permission granted. access mouse.");

                                // repeat in a different thread
                                transfer(device);
                            }
                        }
                        else {
                            RobotLog.e(TAG, "permission denied for device " + device);
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
        boolean forceClaim = true;

        // just grab the first endpoint
        UsbInterface intf = device.getInterface(0);
        endpoint = intf.getEndpoint(0);
        usbDeviceConnection = this.usbManager.openDevice(device);
        usbDeviceConnection.claimInterface(intf, forceClaim);
    }

    @Override
    public float[] getPoints() {
        float[] ray = new float[pts.size()];
        for(int i = 0; i < pts.size(); i++){
            ray[i] = pts.get(i).floatValue();
        }
        return ray;
    }

    //draws on continous line given an array of points and a color
    private static class LineView extends View {
        Paint mPaint;
        PointHandler mPts;

        public LineView(Context context, PointHandler pts, Paint paint){
            super(context);

            mPts = pts;
            mPaint = paint;
        }

        @Override
        public void onDraw(Canvas canvas){
            this.setBackgroundColor(Color.WHITE);

            float[] pts = mPts.getPoints();
            for(int i = 0; i < pts.length-4; i+=2){
                //draw single line at offset i
                canvas.drawLines(pts, i, 4, mPaint);
            }
        }
    }
}
