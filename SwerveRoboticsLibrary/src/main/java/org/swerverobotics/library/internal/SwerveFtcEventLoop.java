package org.swerverobotics.library.internal;

import android.content.Context;
import com.qualcomm.ftccommon.FtcEventLoop;
import com.qualcomm.ftccommon.FtcEventLoopHandler;
import com.qualcomm.ftccommon.UpdateUI;
import com.qualcomm.hardware.HardwareFactory;
import com.qualcomm.robotcore.eventloop.EventLoopManager;
import com.qualcomm.robotcore.eventloop.opmode.OpModeRegister;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.robocol.Command;

import java.util.concurrent.Semaphore;

/**
 * This class provides hooking services that we use to provide contextual 
 * information to opmode code.
 */
public class SwerveFtcEventLoop extends FtcEventLoop
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    FtcEventLoopHandler ftcEventLoopHandler;
    EventLoopManager    eventLoopManager;
    Semaphore semaphore = new Semaphore(0);

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    public SwerveFtcEventLoop(HardwareFactory hardwareFactory, OpModeRegister register, UpdateUI.Callback callback, Context robotControllerContext)
        {
        super(hardwareFactory, register, callback, robotControllerContext);
        this.ftcEventLoopHandler = MemberUtil.handlerOfFtcEventLoop(this);
        setEventLoopManager();
        }

    void setEventLoopManager()
        {
        if (null == this.eventLoopManager)
            this.eventLoopManager = MemberUtil.eventLoopManagerOfFtcEventLoopHandler(this.ftcEventLoopHandler);
        }

    //----------------------------------------------------------------------------------------------
    // Operations
    //----------------------------------------------------------------------------------------------

    public FtcEventLoopHandler getFtcEventLoopHandler()
        {
        return this.ftcEventLoopHandler;
        }
    public EventLoopManager getEventLoopManager()
        {
        return this.eventLoopManager;
        }

    @Override public void loop() throws RobotCoreException
    // Hook this so that we will have thread context set whenever we're running user
    // code on the loop() thread
        {
        SwerveThreadContext context = SwerveThreadContext.createIfNecessary();
        context.swerveFtcEventLoop = this;
        setEventLoopManager();
        //
        super.loop();
        }
    }
