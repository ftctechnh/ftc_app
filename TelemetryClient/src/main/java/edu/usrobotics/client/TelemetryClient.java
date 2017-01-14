package edu.usrobotics.client;


import com.borsch.TelemetryCodec;
import com.borsch.TelemetryData;
import edu.usrobotics.client.ui.LogWindow;
import edu.usrobotics.client.ui.StartupWindow;

import javax.bluetooth.RemoteDevice;
import javax.swing.*;
import javax.swing.text.Style;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TelemetryClient {

    public static StartupWindow startupWindow;
    private static boolean ignoreFocusEvent;
    public static LogWindow logWindow;
    public static MapFrame mapFrame;
    public static TelemetryFrame telemetryFrame;

    /**
     * Current source of telemetry input (Bluetooth server, file path, or HTTP server)
     */
    public static String inputURI;
    public static RemoteDevice device;
    public static String connectionURL;
    public static Receiver receiver;

    public static float inch_mm = 25.4f;
    public static Transform RobotSize = new Transform(18 * inch_mm, 18 * inch_mm, 18 * inch_mm);
    public static float FTCFieldWidth_mm = (12*12 - 2) * inch_mm;   // the FTC field is ~11'10" center-to-center of the glass panels

    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            log("Failed to set look and feel");
        }

        startupWindow = new StartupWindow();
        logWindow = new LogWindow();
        mapFrame = new MapFrame();
        telemetryFrame = new TelemetryFrame();


        startupWindow.addWindowListener(new WindowAdapter() {
            public void windowActivated(WindowEvent e) {
                if (!e.getWindow().equals(startupWindow)) {
                    return;
                } else if (ignoreFocusEvent) {
                    ignoreFocusEvent = false;
                    return;
                }

                mapFrame.toFront();
                telemetryFrame.toFront();
                logWindow.toFront();
                if (startupWindow.isVisible()) {
                    ignoreFocusEvent = true;
                    startupWindow.toFront();
                }
            }
        });
    }

    public static void startTelemetry (String inputPath) {
        if (inputPath == null || inputPath.length() < 1) {
            startupWindow.invalidServer();

            return;
        }

        inputURI = inputPath;

        // If input is a file path
        if (inputURI.indexOf(".txt") > 0) {
            log ("Playback from file: "+ inputURI);
            fileInput();
            legacy ();

        } else {
            log ("Connecting to BT: "+ inputURI);
            bluetoothInput();
        }
    }

    private static void legacy () {
        TelemetryData lastData = null;
        while (true) {
            if (receiver.hasNext()) {
                try {
                    lastData = processData(TelemetryCodec.Decode(receiver.getNext()), lastData);
                    if (lastData == null) { // Null only on STOP code
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

        switch (data.type){

            case ROBOT_SPECS:
                RobotSize = new Transform(Float.parseFloat(data.data[0]), Float.parseFloat(data.data[1]), Float.parseFloat(data.data[2])).scaled(inch_mm);
                break;

            case UPDATE_DEVICE:
                telemetryFrame.updateData(data.data.length > 1 ? data.data[1] : null, data.id, data.data[0]);
                break;

            case UPDATE_MAP:
                mapFrame.updateTrackable(data.id, Float.parseFloat(data.data[0]), Float.parseFloat(data.data[1]), Float.parseFloat(data.data[2]), Float.parseFloat(data.data[3]), Float.parseFloat(data.data[4]), Float.parseFloat(data.data[5]));
                break;

            case LOG:
                log(data.data[0], LogWindow.LOG_STYLE);
                break;

            case WARN:
                log(data.data[0], LogWindow.WARN_STYLE);
                break;

            case ERROR:
                log(data.data[0], LogWindow.ERROR_STYLE);
                break;

            case STOP:
                return null;

            default:
                log("Unhandled telemetry DataType: " + data.type.name(), LogWindow.WARN_STYLE);
        }

        return data;
    }

    public static void log (String msg, Style style) {
        System.out.println (msg);

        if (logWindow != null) {
            logWindow.log(msg, style);
        }
    }

    public static void log (String msg) {
        log(msg, LogWindow.LOG_STYLE);
    }

    static void fileInput() {
        receiver = new FileReceiver(inputURI);

        log("Created FileReceiver for path '" + inputURI + "', starting read thread...");

        Thread receiverThread = new Thread((Runnable) receiver);
        receiverThread.start();
    }

    static void bluetoothInput() {
        ServicesSearch ss = new ServicesSearch();


        while (device == null) {
            device = ss.getBluetoothDevice(inputURI);
            if (device == null) {
                Object[] options = {"Retry",
                        "Cancel"
                };

                int n = JOptionPane.showOptionDialog(startupWindow,
                        "Could not find bluetooth device.",
                        "Device Not Found",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.ERROR_MESSAGE,
                        null,
                        options,
                        options[0]);

                if (n == 1) {
                    return;
                }
            }
        }

        connectionURL = ss.getBluetoothServiceURL(device, "446118f08b1e11e29e960800200c9a69");

        log ("Acquired BT Service URL: " + connectionURL, LogWindow.LOG_STYLE);

        receiver = new BluetoothReceiver(connectionURL);
        Thread receiverThread = new Thread((Runnable) receiver);
        receiverThread.start();
        log ("BT Connected", LogWindow.LOG_STYLE);

    }

    private static String getInputURI() {
        return (String) JOptionPane.showInputDialog(
                startupWindow,
                "Enter bluetooth device name:",
                "BT Device Name",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                "10237 On-board BT"
        );
    }
}