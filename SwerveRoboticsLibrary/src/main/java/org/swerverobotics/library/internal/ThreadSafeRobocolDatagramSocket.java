package org.swerverobotics.library.internal;

import com.qualcomm.robotcore.robocol.RobocolDatagram;
import com.qualcomm.robotcore.robocol.RobocolDatagramSocket;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;

/**
 * ThreadSafeRobocolDatagramSocket extends its base so as to synchronize concurrent
 * access so that the object's internals remain consistent. We wish we didn't have to
 * do this, but it seems we do.
 */
public class ThreadSafeRobocolDatagramSocket extends RobocolDatagramSocket
    {
    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    public ThreadSafeRobocolDatagramSocket()
        {
        }

    //----------------------------------------------------------------------------------------------
    // Operations
    //----------------------------------------------------------------------------------------------

    @Override synchronized public boolean isClosed()
        {
        return super.isClosed();
        }

    @Override synchronized public boolean isRunning()
        {
        return super.isRunning();
        }

    @Override synchronized public InetAddress getLocalAddress()
        {
        return super.getLocalAddress();
        }

    @Override synchronized public InetAddress getInetAddress()
        {
        return super.getInetAddress();
        }

    @Override synchronized public State getState()
        {
        return super.getState();
        }

    @Override synchronized public RobocolDatagram recv()
        {
        return super.recv();
        }

    @Override synchronized public void send(RobocolDatagram message)
        {
        super.send(message);
        }

    @Override synchronized public void close()
        {
        super.close();
        }

    @Override synchronized public void connect(InetAddress connectAddress) throws SocketException
        {
        super.connect(connectAddress);
        }

    @Override synchronized public void bind(InetSocketAddress bindAddress) throws SocketException
        {
        super.bind(bindAddress);
        }

    @Override synchronized public void listen(InetAddress destAddress) throws SocketException
        {
        super.listen(destAddress);
        }
    }
