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
    // State
    //----------------------------------------------------------------------------------------------

    final Object readLock = new Object();
    final Object openCloseLock = new Object();

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    public ThreadSafeRobocolDatagramSocket()
        {
        }

    //----------------------------------------------------------------------------------------------
    // Operations
    //----------------------------------------------------------------------------------------------

    @Override public void close()
        {
        synchronized (this.openCloseLock)
            {
            super.close();
            }
        }

    @Override public void bind(InetSocketAddress bindAddress) throws SocketException
        {
        synchronized (this.openCloseLock)
            {
            super.bind(bindAddress);
            }
        }

    @Override public boolean isClosed()
        {
        return super.isClosed();
        }

    @Override public boolean isRunning()
        {
        return super.isRunning();
        }

    @Override public InetAddress getLocalAddress()
        {
        return super.getLocalAddress();
        }

    @Override public InetAddress getInetAddress()
        {
        return super.getInetAddress();
        }

    @Override public State getState()
        {
        return super.getState();
        }

    @Override public RobocolDatagram recv()
        {
        synchronized (this.readLock)
            {
            return super.recv();
            }
        }

    @Override public void listen(InetAddress destAddress) throws SocketException
        {
        super.listen(destAddress);
        }

    @Override public void send(RobocolDatagram message)
        {
        super.send(message);
        }

    @Override public void connect(InetAddress connectAddress) throws SocketException
        {
        super.connect(connectAddress);
        }


    }
