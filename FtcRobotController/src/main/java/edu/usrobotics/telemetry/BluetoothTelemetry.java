package edu.usrobotics.telemetry;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Created by Maxim Borsch on 9/8/2016.
 */
public class BluetoothTelemetry extends Thread implements ITelemetryWriter {
    private BluetoothServerSocket serverSocket;
    private final String NAME = "Yolometry";
    private static UUID MY_UUID = UUID.fromString("446118f0-8b1e-11e2-9e96-0800200c9a69");//66
    private ArrayList<BluetoothSocket> sockets = new ArrayList<BluetoothSocket>();

    private static List<byte[]> outputStreamQueue = Collections.synchronizedList(new ArrayList<byte[]>());
    public static boolean isSupported = false;


    public void init() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            System.err.println("BluetoothTelemetry: Bluetooth adapter not found!");
            return;
        }
        if (!mBluetoothAdapter.isEnabled()) {
            System.err.println("BluetoothTelemetry: Bluetooth not enabled!");
            return;
        }


        BluetoothServerSocket tmp = null;
        try {
            tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
        } catch (IOException e) {
            e.printStackTrace();
        }
        serverSocket = tmp;
        isSupported = true;
    }

    @Override
    public boolean write(String line) {
        outputStreamQueue.add(line.getBytes());
        return true;
    }

    public void run() {
        if (serverSocket == null) {
            System.err.println ("BluetoothTelemetry will not start.");
            return;
        }

        BluetoothSocket socket;
        while (true) {
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                break;
            }

            if (socket != null) {
                socketConnected(socket);

                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }

            //SEND UPDATE TO ALL SOCKETS
            synchronized(outputStreamQueue) {
                if (!outputStreamQueue.isEmpty() && !sockets.isEmpty()) {
                    byte[] nextPacket = outputStreamQueue.remove (0);

                    try {
                        OutputStream stream;
                        for (BluetoothSocket client : sockets) {
                            if (client.isConnected()) {
                                stream = client.getOutputStream();
                                stream.write(nextPacket);
                                stream.flush();

                            } else { sockets.remove(client); }
                        }
                    } catch (IOException e) {
                        System.out.println("Failed to send update bytes to a client");
                        e.printStackTrace();
                    }
                }
            }

        }
    }

    void socketConnected (BluetoothSocket socket) {
        sockets.add (socket);
        System.out.println("BT Device Connected: " + socket.getRemoteDevice().getName());
    }

    public void cancel() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}