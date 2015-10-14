package org.swerverobotics.library.internal;

import android.content.Context;
import com.qualcomm.ftccommon.FtcEventLoop;
import com.qualcomm.ftccommon.UpdateUI;
import com.qualcomm.hardware.HardwareFactory;
import com.qualcomm.robotcore.eventloop.EventLoopManager;
import com.qualcomm.robotcore.eventloop.opmode.OpModeRegister;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.robocol.Command;

import java.util.concurrent.Semaphore;

/**
 * This class is a nefarious hook to try to work around an initialization problem
 * that arises when registerOpModes takes too long. We would LOVE not to have to do
 * this, and will remove this as soon as the underlying bug is fixed.
 *
 * See http://ftcforum.usfirst.org/showthread.php?4967
 *
 * The essence of the bug is that we're being asked to process commands before we've been
 * initialized. So our work around is to simply block the latter on the former.
 */
public class SwerveFtcEventLoop extends FtcEventLoop
    {
    Semaphore semaphore = new Semaphore(0);

    public SwerveFtcEventLoop(HardwareFactory hardwareFactory, OpModeRegister register, UpdateUI.Callback callback, Context robotControllerContext)
        {
        super(hardwareFactory, register, callback, robotControllerContext);
        }

    @Override
    public void init(EventLoopManager eventLoopManager) throws RobotCoreException, InterruptedException
        {
        super.init(eventLoopManager);
        semaphore.release();
        }

    @Override public void processCommand(Command command)
        {
        try {
            semaphore.acquire();
            semaphore.release();
            }
        catch (InterruptedException e)
            {
            Util.handleCapturedInterrupt(e);
            }
        super.processCommand(command);
        }
    }
