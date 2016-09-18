package edu.usrobotics.client;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Max on 9/17/2016.
 */
public class FileReceiver implements Receiver, Runnable {

    LinkedBlockingQueue<String> receivedQueue = new LinkedBlockingQueue<>();
    String path;

    public FileReceiver (String path) {
        this.path = path;
    }


    @Override
    public void run() {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                receivedQueue.offer(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
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
