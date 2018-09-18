package org.firstinspires.ftc.teamcode.Utilities.Logging;

import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;

public class AsyncLogWriter extends Thread {

    FileWriter fw;
    ArrayBlockingQueue<Object[]> queue;
    public boolean terminate;

    public AsyncLogWriter(FileWriter fw, ArrayBlockingQueue<Object[]> queue) {
        this.fw = fw;
        this.queue = queue;
        terminate = false;
    }

    public void run() {
        try {
            while (!terminate) {
                Object[] element = queue.take(); // Will quietly wait until element is available
                for (Object o : element) {
                    fw.write(o.toString() + ",");
                }
                fw.write("\n");
            }
        } catch (Exception e) {
            try { // In this case, we shut down poorly
                fw.write(e.toString());
            } catch (IOException IOE) {}
        }

        // Save file
        try {
            fw.close();
        } catch (IOException e) {}
    }

    public void terminate() {
        terminate = true;
    }
}
