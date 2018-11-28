package org.upacreekrobotics.dashboard;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Data {

    Socket socket;
    PrintWriter writer;
    BufferedReader reader;

    private static boolean connected = false;

    public Data() {
        connect();
    }

    public void write(Message message) {
        write(message.getMessage());
    }

    public void write(String text) {
        try {
            writer.println(text);
        } catch (NullPointerException e) {
            connect();
        }
    }

    public Message read() {
        String line = null;
        try {
            line = reader.readLine();
        } catch (IOException e) {
            connect();
        } catch (NullPointerException e) {
            connect();
        }
        if (line != null) {
            String[] text = line.split("~", 2);
            return new Message(MessageType.valueOf(text[0]), text[1]);
        }
        return null;
    }

    public boolean isConnected() {
        try {
            writer.println(new Message(MessageType.HAND_SHAKE, "HI").getMessage());
            return !writer.checkError();
        } catch (NullPointerException e) {
        }
        return false;
    }

    private void connect() {
        socket = null;
        connected = false;
        while (!connected) {
            try {
                connected = false;
                ServerSocket serv = new ServerSocket(19231);
                socket = serv.accept();
                writer = new PrintWriter(socket.getOutputStream(), true);
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                connected = true;
            } catch (IOException e) {
            } catch (NullPointerException e) {
            }
        }
    }
}
