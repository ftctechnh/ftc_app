package edu.usrobotics.client;


import javax.bluetooth.RemoteDevice;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class TelemetryClient  {

    static CustomFrame logFrame;
    static String deviceName;
    static RemoteDevice device;
    static String connectionURL;
    static BluetoothReceiver receiver;

    public static void main (String[] args) {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Failed to set look and feel");
        }

        logFrame = new CustomFrame();

        while (deviceName == null || deviceName.length() < 1) {
            deviceName = getDeviceName ();
            if (deviceName == null) {
                System.exit(0);
            }
        }


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
    }

    static String getDeviceName () {
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