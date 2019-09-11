package org.firstinspires.ftc.simulator;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.firstinspires.ftc.teamcode.common.math.Pose;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

class TXHandler {
    private final static String PYTHON_SERVER_HOST = "localhost";
    private final static int PYTHON_SERVER_PORT = 4446;

    protected DatagramSocket txsocket;
    protected InetAddress txhost;
    protected DatagramSocket rxhost;

    Gson gson;
    double framerate;
    int index;

    public TXHandler(double framerate) throws IOException {
        txhost = InetAddress.getByName(PYTHON_SERVER_HOST);
        txsocket = new DatagramSocket();
        gson = new Gson();
        index = 0;
        this.framerate = framerate;
    }

    public void sendMessage(Pose p) throws IOException {
        JsonObject obj = gson.toJsonTree(p).getAsJsonObject();
        obj.addProperty("index", index);
        obj.addProperty("fps", framerate);
        String jsonStr = gson.toJson(obj);
        System.out.println(jsonStr);
        byte[] data = gson.toJson(obj).getBytes();
        DatagramPacket packet = new DatagramPacket(data, data.length, txhost, PYTHON_SERVER_PORT);
        txsocket.send(packet);
        index += 1;
    }
}