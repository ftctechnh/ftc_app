package org.firstinspires.ftc.teamcode.libraries;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;

/*
 * Copyright (c) 2018 FTC team 4634 FROGbots
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

public class UDPid
{
    private int port;
    private double p, i, d;
    private Thread backgroundThread;

    public UDPid(int port)
    {
        this.port = port;
    }

    public UDPid()
    {
        this(8087);
    }

    public void beginListening()
    {
        backgroundThread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                listen();
            }
        });

        backgroundThread.start();
    }

    public void shutdown()
    {
        backgroundThread.interrupt();
    }

    public synchronized double getP()
    {
        return p;
    }

    public synchronized double getI()
    {
        return i;
    }

    public synchronized double getD()
    {
        return d;
    }

    private void listen()
    {
        try
        {
            DatagramSocket serverSocket = new DatagramSocket(port);
            byte[] packet = new byte[24];

            //System.out.printf("Listening on udp:%s:%d%n", InetAddress.getLocalHost().getHostAddress(), port);
            DatagramPacket receivePacket = new DatagramPacket(packet, packet.length);

            while (!Thread.currentThread().isInterrupted())
            {
                serverSocket.receive(receivePacket);

                byte[] pVal = new byte[8];
                byte[] iVal = new byte[8];
                byte[] dVal = new byte[8];

                System.arraycopy(packet, 0, pVal, 0, 8);
                System.arraycopy(packet, 8, iVal, 0, 8);
                System.arraycopy(packet, 16, dVal, 0, 8);

                p = toDouble(pVal);
                i = toDouble(iVal);
                d = toDouble(dVal);
            }

            serverSocket.close();
        }

        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private double toDouble(byte[] bytes)
    {
        return ByteBuffer.wrap(bytes).getDouble();
    }
}