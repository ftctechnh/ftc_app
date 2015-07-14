package org.swerverobotics.library;

import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.*;

public class ThunkingLegacyModule implements LegacyModule
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    LegacyModule target;   // can only talk to him on the loop thread

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    private ThunkingLegacyModule(LegacyModule target)
        {
        this.target = target;
        }

    static public ThunkingLegacyModule Create(LegacyModule target)
        {
        return target instanceof ThunkingLegacyModule ? (ThunkingLegacyModule)target : new ThunkingLegacyModule(target);
        }

    //----------------------------------------------------------------------------------------------
    // LegacyModule interface
    //----------------------------------------------------------------------------------------------

    @Override public void enableNxtI2cReadMode(int physicalPort, int i2cAddress, int memAddress, int memLength)
        {
        class Thunk extends SynchronousOpMode.WaitableAction
            {
            int physicalPort;
            int i2cAddress;
            int memAddress;
            int memLength;
            @Override public void actionOnLoopThread()
                {
                target.enableNxtI2cReadMode(physicalPort, i2cAddress, memAddress, memLength);
                }
            }
        Thunk thunk = new Thunk();
        thunk.physicalPort = physicalPort;
        thunk.i2cAddress = i2cAddress;
        thunk.memAddress = memAddress;
        thunk.memLength = memLength;
        thunk.dispatch();
        }

    @Override public void enableNxtI2cWriteMode(int physicalPort, int i2cAddress, int memAddress, byte[] initialValues)
        {
        class Thunk extends SynchronousOpMode.WaitableAction
            {
            int physicalPort;
            int i2cAddress;
            int memAddress;
            byte[] initialValues;
            @Override public void actionOnLoopThread()
                {
                target.enableNxtI2cWriteMode(physicalPort, i2cAddress, memAddress, initialValues);
                }
            }
        Thunk thunk = new Thunk();
        thunk.physicalPort = physicalPort;
        thunk.i2cAddress = i2cAddress;
        thunk.memAddress = memAddress;
        thunk.initialValues = initialValues;
        thunk.dispatch();
        }

    @Override public void enableAnalogReadMode(int physicalPort, int i2cAddress)
        {
        class Thunk extends SynchronousOpMode.WaitableAction
            {
            int physicalPort;
            int i2cAddress;
            @Override public void actionOnLoopThread()
                {
                target.enableAnalogReadMode(physicalPort, i2cAddress);
                }
            }
        Thunk thunk = new Thunk();
        thunk.physicalPort = physicalPort;
        thunk.i2cAddress = i2cAddress;
        thunk.dispatch();
        }

    @Override public void enable9v(int physicalPort, boolean enable)
        {
        class Thunk extends SynchronousOpMode.WaitableAction
            {
            int physicalPort;
            boolean enable;
            @Override public void actionOnLoopThread()
                {
                target.enable9v(physicalPort, enable);
                }
            }
        Thunk thunk = new Thunk();
        thunk.physicalPort = physicalPort;
        thunk.enable = enable;
        thunk.dispatch();
        }

    @Override public void setDigitalLine(int physicalPort, int line, boolean set)
        {
        class Thunk extends SynchronousOpMode.WaitableAction
            {
            int physicalPort;
            int line;
            boolean set;
            @Override public void actionOnLoopThread()
                {
                target.setDigitalLine(physicalPort, line, set);
                }
            }
        Thunk thunk = new Thunk();
        thunk.physicalPort = physicalPort;
        thunk.line = line;
        thunk.set = set;
        thunk.dispatch();
        }

    @Override public byte[] readLegacyModuleCache(int physicalPort)
        {
        class Thunk extends SynchronousOpMode.ResultableAction<byte[]>
            {
            int physicalPort;
            @Override public void actionOnLoopThread()
                {
                this.result = target.readLegacyModuleCache(physicalPort);
                }
            }
        Thunk thunk = new Thunk();
        thunk.physicalPort = physicalPort;
        thunk.dispatch();
        return thunk.result;
        }

    @Override public void writeLegacyModuleCache(int physicalPort, byte[] data)
        {
        class Thunk extends SynchronousOpMode.WaitableAction
            {
            int physicalPort;
            byte[] data;
            @Override public void actionOnLoopThread()
                {
                target.writeLegacyModuleCache(physicalPort, data);
                }
            }
        Thunk thunk = new Thunk();
        thunk.physicalPort = physicalPort;
        thunk.data = data;
        thunk.dispatch();
        }

    @Override public byte[] readAnalog(int physicalPort)
        {
        class Thunk extends SynchronousOpMode.ResultableAction<byte[]>
            {
            int physicalPort;
            @Override public void actionOnLoopThread()
                {
                this.result = target.readAnalog(physicalPort);
                }
            }
        Thunk thunk = new Thunk();
        thunk.physicalPort = physicalPort;
        thunk.dispatch();
        return thunk.result;
        }

    @Override public boolean isPortReady(int physicalPort)
        {
        class Thunk extends SynchronousOpMode.ResultableAction<Boolean>
            {
            int physicalPort;
            @Override public void actionOnLoopThread()
                {
                this.result = target.isPortReady(physicalPort);
                }
            }
        Thunk thunk = new Thunk();
        thunk.physicalPort = physicalPort;
        thunk.dispatch();
        return thunk.result;
        }

    @Override public void close()
        {
        class Thunk extends SynchronousOpMode.WaitableAction
            {
            @Override public void actionOnLoopThread()
                {
                target.close();
                }
            }
        Thunk thunk = new Thunk();
        thunk.dispatch();
        }
    }
