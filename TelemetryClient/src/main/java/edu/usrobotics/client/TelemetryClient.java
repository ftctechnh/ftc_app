package edu.usrobotics.client;


import com.borsch.TelemetryCodec;
import com.borsch.TelemetryData;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.bluetooth.RemoteDevice;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class TelemetryClient {

    static CustomFrame logFrame;
    static MapFrame mapFrame;
    static String deviceName;
    static RemoteDevice device;
    static String connectionURL;
    static Receiver receiver;

    static float inch_mm = 25.4f;
    static Transform RobotSize = new Transform(18 * inch_mm, 18 * inch_mm, 18 * inch_mm);
    static float FTCFieldWidth_mm = (12*12 - 2) * inch_mm;   // the FTC field is ~11'10" center-to-center of the glass panels

    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Failed to set look and feel");
        }

        logFrame = new LogFrame();
        mapFrame = new MapFrame();

        logFrame.addWindowListener(new WindowAdapter() {
            public void windowActivated(WindowEvent e) {
                if (TelemetryClient.mapFrame != null) {
                    TelemetryClient.mapFrame.toFront();
                }
            }
        });

        while (deviceName == null || deviceName.length() < 1) {
            deviceName = getDeviceName();
            if (deviceName == null) {
                System.exit(0);
            }
        }

        // If input is a file path
        if (deviceName.indexOf(".txt") > 0) {
            fileInput();

        } else {
            bluetoothInput();
        }

        TelemetryData lastData = null;
        while (true) {
            if (receiver.hasNext()) {
                try {
                    lastData = processData(TelemetryCodec.Decode(receiver.getNext()), lastData);
                    if (lastData == null) {
                        break;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static TelemetryData processData(TelemetryData data, TelemetryData lastData) throws InterruptedException {
        Thread.sleep(lastData != null ? data.timestamp - lastData.timestamp : data.timestamp);

        System.out.println("Timestamp: "+data.timestamp);

        switch (data.type){

            case ROBOT_SPECS:
                RobotSize = new Transform(Float.parseFloat(data.data[0]), Float.parseFloat(data.data[1]), Float.parseFloat(data.data[2])).mutliplied(inch_mm);
                break;

            case UPDATE_MAP:
                mapFrame.updateTrackable(data.id, Float.parseFloat(data.data[0]), Float.parseFloat(data.data[2]), Float.parseFloat(data.data[4]));
                break;

            case STOP:
                return null;

            default:
                System.out.println("Unhandled telemetry DataType: " + data.type.name());
        }

        return data;
    }

    static void fileInput() {
        receiver = new FileReceiver(deviceName);
        Thread receiverThread = new Thread((Runnable) receiver);
        receiverThread.start();
    }

    static void bluetoothInput() {
        ServicesSearch ss = new ServicesSearch();


        while (device == null) {
            device = ss.getBluetoothDevice(deviceName);
            if (device == null) {
                Object[] options = {"Retry",
                        "Cancel"
                };

                int n = JOptionPane.showOptionDialog(logFrame,
                        "Could not find bluetooth device.",
                        "Device Not Found",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.ERROR_MESSAGE,
                        null,
                        options,
                        options[0]);

                if (n == 1) System.exit(0);
            }
        }

        connectionURL = ss.getBluetoothServiceURL(device, "446118f08b1e11e29e960800200c9a69");

        receiver = new BluetoothReceiver(connectionURL);
        Thread receiverThread = new Thread((Runnable) receiver);
        receiverThread.start();

    }

    static String getDeviceName() {
        return (String) JOptionPane.showInputDialog(
                logFrame,
                "Enter bluetooth device name:",
                "BT Device Name",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                "Yolometry"
        );
    }
}