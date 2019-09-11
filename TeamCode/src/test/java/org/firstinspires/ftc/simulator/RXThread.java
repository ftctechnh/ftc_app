package org.firstinspires.ftc.simulator;

import com.google.gson.Gson;

import org.firstinspires.ftc.teamcode.common.math.Pose;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class RXThread extends Thread {
    private final static int PACKETSIZE = 100;
    private final static int JAVA_SERVER_PORT = 4445;
    protected DatagramSocket rxsocket;
    private boolean terminate;

    volatile Pose pose;

    @Override
    public void start() {
        try {
            rxsocket = new DatagramSocket(JAVA_SERVER_PORT);
            rxsocket.setSoTimeout(250);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        this.terminate = false;
        this.pose = null;
        super.start();
    }

    @Override
    public void run() {
        while(!terminate) {
            byte buf[] = new byte[PACKETSIZE];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            try {
                rxsocket.receive(packet);
                String data = new String(packet.getData(), 0, packet.getLength());
                pose = new Gson().fromJson(data, Pose.class);
            } catch(IOException e) {}
        }
        rxsocket.close();
    }

    public void kill() {
        this.terminate = true;
    }

    public boolean hasPose() {
        return this.pose == null;
    }
}
