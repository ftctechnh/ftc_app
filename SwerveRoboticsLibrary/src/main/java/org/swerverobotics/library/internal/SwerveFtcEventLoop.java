package org.swerverobotics.library.internal;

import android.content.Context;
import com.qualcomm.ftccommon.FtcEventLoop;
import com.qualcomm.ftccommon.FtcEventLoopHandler;
import com.qualcomm.ftccommon.UpdateUI;
import com.qualcomm.hardware.HardwareFactory;
import com.qualcomm.robotcore.eventloop.EventLoopManager;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManager;
import com.qualcomm.robotcore.eventloop.opmode.OpModeRegister;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.robocol.Command;
import com.qualcomm.robotcore.util.RobotLog;

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

    protected SwerveOpModeManager swerveOpModeManager;
    protected EventLoopManager    eventLoopManager;

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    public SwerveFtcEventLoop(HardwareFactory hardwareFactory, OpModeRegister register, UpdateUI.Callback callback, Context robotControllerContext)
        {
        super(hardwareFactory, register, callback, robotControllerContext);
        setEventLoopManager();
        }

    
    protected OpModeManager createOpModeManager()
        {
        this.swerveOpModeManager = new SwerveOpModeManager(new HardwareMap(this.robotControllerContext));
        return this.swerveOpModeManager;
        }

    void setEventLoopManager()
        {
        if (null == this.eventLoopManager)
            this.eventLoopManager = this.ftcEventLoopHandler.getEventLoopManager();
        }

    //----------------------------------------------------------------------------------------------
    // Operations
    //----------------------------------------------------------------------------------------------

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

    @Override
    public synchronized void teardown() throws RobotCoreException
        {
        // Do our shutdown first so that the system 'teardown' log messages are
        // really at the end.
        this.swerveOpModeManager.onRobotShutdown();
        super.teardown();
        }
    }
