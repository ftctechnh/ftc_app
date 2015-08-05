package org.swerverobotics.library.thunking;

import com.qualcomm.robotcore.hardware.I2cController;
import com.qualcomm.robotcore.util.SerialNumber;

import java.util.concurrent.locks.Lock;

/**
 * Another in our story..
 */
public class ThunkedI2cController implements I2cController
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    public I2cController target;          // can only talk to him on the loop thread

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    private ThunkedI2cController(I2cController target)
        {
        if (target == null) throw new NullPointerException("null " + this.getClass().getSimpleName() + " target");
        this.target = target;
        }

    static public ThunkedI2cController create(I2cController target)
        {
        return target instanceof ThunkedI2cController ? (ThunkedI2cController)target : new ThunkedI2cController(target);
        }

    //----------------------------------------------------------------------------------------------
    // I2cController
    //----------------------------------------------------------------------------------------------

    @Override public synchronized void close()
        {
        (new NonwaitingThunk()
        {
        @Override protected void actionOnLoopThread()
            {
            target.close();
            }
        }).doWriteOperation();
        }

    @Override public synchronized int getVersion()
        {
        return (new ResultableThunk<Integer>()
        {
        @Override protected void actionOnLoopThread()
            {
            this.result = target.getVersion();
            }
        }).doReadOperation();
        }

    @Override public synchronized String getDeviceName()
        {
        return (new ResultableThunk<String>()
        {
        @Override protected void actionOnLoopThread()
            {
            this.result = target.getDeviceName();
            }
        }).doReadOperation();
        }

    @Override synchronized public SerialNumber getSerialNumber()
        {
        return (new ResultableThunk<SerialNumber>()
        {
        @Override protected void actionOnLoopThread()
            {
            this.result = target.getSerialNumber();
            }
        }).doReadOperation();
        }

    @Override synchronized public void enableI2cReadMode(final int physicalPort, final int i2cAddress, final int memAddress, final int length)
        {
        (new NonwaitingThunk()
        {
        @Override protected void actionOnLoopThread()
            {
            target.enableI2cReadMode(physicalPort, i2cAddress, memAddress, length);
            }
        }).doWriteOperation();
        }

    @Override synchronized public void enableI2cWriteMode(final int physicalPort, final int i2cAddress, final int memAddress, final int length)
        {
        (new NonwaitingThunk()
        {
        @Override protected void actionOnLoopThread()
            {
            target.enableI2cWriteMode(physicalPort, i2cAddress, memAddress, length);
            }
        }).doWriteOperation();
        }

    @Override synchronized public Lock getI2cReadCacheLock(final int physicalPort)
        {
        return (new ResultableThunk<Lock>()
        {
        @Override protected void actionOnLoopThread()
            {
            this.result = target.getI2cReadCacheLock(physicalPort);
            }
        }).doReadOperation();
        }

    @Override synchronized public Lock getI2cWriteCacheLock(final int physicalPort)
        {
        return (new ResultableThunk<Lock>()
        {
        @Override protected void actionOnLoopThread()
            {
            this.result = target.getI2cWriteCacheLock(physicalPort);
            }
        }).doReadOperation();
        }

    @Override synchronized public byte[] getI2cReadCache(final int physicalPort)
        {
        return (new ResultableThunk<byte[]>()
        {
        @Override protected void actionOnLoopThread()
            {
            this.result = target.getI2cReadCache(physicalPort);
            }
        }).doReadOperation();
        }

    @Override synchronized public byte[] getI2cWriteCache(final int physicalPort)
        {
        return (new ResultableThunk<byte[]>()
        {
        @Override protected void actionOnLoopThread()
            {
            this.result = target.getI2cWriteCache(physicalPort);
            }
        }).doReadOperation();
        }

    @Override synchronized public void setI2cPortActionFlag(final int physicalPort)
        {
        (new NonwaitingThunk()
        {
        @Override protected void actionOnLoopThread()
            {
            target.setI2cPortActionFlag(physicalPort);
            }
        }).doWriteOperation();
        }

    @Override synchronized public boolean isI2cPortActionFlagSet(final int physicalPort)
        {
        return (new ResultableThunk<Boolean>()
        {
        @Override protected void actionOnLoopThread()
            {
            this.result = target.isI2cPortActionFlagSet(physicalPort);
            }
        }).doReadOperation();
        }

    @Override synchronized public void readI2cCacheFromModule(final int physicalPort)
        {
        (new NonwaitingThunk()
        {
        @Override protected void actionOnLoopThread()
            {
            target.readI2cCacheFromModule(physicalPort);
            }
        }).doWriteOperation();
        }

    @Override synchronized public void writeI2cCacheToModule(final int physicalPort)
        {
        (new NonwaitingThunk()
        {
        @Override protected void actionOnLoopThread()
            {
            target.writeI2cCacheToModule(physicalPort);
            }
        }).doWriteOperation();
        }

    @Override synchronized public void writeI2cPortFlagOnlyToModule(final int physicalPort)
        {
        (new NonwaitingThunk()
        {
        @Override protected void actionOnLoopThread()
            {
            target.writeI2cPortFlagOnlyToModule(physicalPort);
            }
        }).doWriteOperation();
        }

    @Override synchronized public boolean isI2cPortInReadMode(final int physicalPort)
        {
        return (new ResultableThunk<Boolean>()
        {
        @Override protected void actionOnLoopThread()
            {
            this.result = target.isI2cPortInReadMode(physicalPort);
            }
        }).doReadOperation();
        }

    @Override synchronized public boolean isI2cPortInWriteMode(final int physicalPort)
        {
        return (new ResultableThunk<Boolean>()
        {
        @Override protected void actionOnLoopThread()
            {
            this.result = target.isI2cPortInWriteMode(physicalPort);
            }
        }).doReadOperation();
        }

    @Override synchronized public boolean isI2cPortReady(final int physicalPort)
        {
        return (new ResultableThunk<Boolean>()
        {
        @Override protected void actionOnLoopThread()
            {
            this.result = target.isI2cPortReady(physicalPort);
            }
        }).doReadOperation();
        }

    @Override synchronized public void registerForI2cPortReadyCallback(final I2cController.I2cPortReadyCallback callback, final int physicalPort)
        {
        (new NonwaitingThunk()
        {
        @Override protected void actionOnLoopThread()
            {
            target.registerForI2cPortReadyCallback(callback, physicalPort);
            }
        }).doWriteOperation();
        }

    @Override synchronized public  void deregisterForPortReadyCallback(final int physicalPort)
        {
        (new NonwaitingThunk()
        {
        @Override protected void actionOnLoopThread()
            {
            target.deregisterForPortReadyCallback(physicalPort);
            }
        }).doWriteOperation();
        }

    }
