package edu.usrobotics.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.LinkedBlockingQueue;

import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

/**
 * Created by Max on 9/11/2016.
 */
public class BluetoothReceiver implements Runnable, Receiver {

    LinkedBlockingQueue<String> receivedQueue = new LinkedBlockingQueue<> ();
    String connectionURL;

    public BluetoothReceiver(String connectionURL) {
        this.connectionURL = connectionURL;
    }

    public void connect () {
        try {
            StreamConnection streamConnection = (StreamConnection) Connector.open(connectionURL);

            //send string
/*            OutputStream outStream = streamConnection.openOutputStream();
            PrintWriter pWriter = new PrintWriter(new OutputStreamWriter(outStream));
            pWriter.write("Test String from Client\r\n");
            pWriter.flush();*/


            //read
            InputStream inStream = streamConnection.openInputStream();
            BufferedReader bReader2 = new BufferedReader(new InputStreamReader(inStream));
            String line;
            while ((line = bReader2.readLine()) != null) {
                receivedQueue.add(line);
                System.out.println("Received: "+line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        connect ();
    }

    @Override
    public boolean hasNext() {
        return receivedQueue.peek() != null;
    }

    @Override
    public String getNext() {
        return receivedQueue.poll();
    }
}
