package org.swerverobotics.library.internal;

import android.app.Activity;

import com.qualcomm.robotcore.eventloop.opmode.OpModeManager;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerImpl;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.RobotLog;

public class SwerveOpModeManager extends OpModeManagerImpl
    {
    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    public SwerveOpModeManager(Activity activity, HardwareMap map)
        {
        super(activity, map);
        }

    //----------------------------------------------------------------------------------------------
    // Operations
    //----------------------------------------------------------------------------------------------

    @Override
    protected void callActiveOpModeStop()
        {
        RobotLog.v("Swerve: OpModeManager: %s: stop ...", activeOpModeName);
        super.callActiveOpModeStop();
        if (this.activeOpMode != OpModeManagerImpl.DEFAULT_OP_MODE)
            {
            RobotStateTransitionNotifier.getInstance().onUserOpModeStop();
            }
        RobotLog.v("Swerve: OpModeManager: %s: ... stop complete", activeOpModeName);
        }

    @Override
    protected void callActiveOpModeInit()
        {
        RobotLog.v("Swerve: OpModeManager: %s: init...", activeOpModeName);
        super.callActiveOpModeInit();
        RobotLog.v("Swerve: OpModeManager: %s: ... init complete", activeOpModeName);
        }

    @Override
    protected void callActiveOpModeStart()
        {
        RobotLog.v("Swerve: OpModeManager: %s: start...", activeOpModeName);
        super.callActiveOpModeStart();
        RobotLog.v("Swerve: OpModeManager: %s: ... start complete", activeOpModeName);
        }

    public void onRobotShutdown()
        {
        RobotLog.v("Swerve: OpModeManager: onRobotShutdown ...");
        // We need to shutdown the opmode if we really need to. But if we don't, don't bother,
        // since that will add additional log messages when FtcEventLoop.teardown() itself calls
        // stopActiveOpMode().
        if (this.activeOpMode != OpModeManagerImpl.DEFAULT_OP_MODE)
            {
            this.stopActiveOpMode();
            }
        RobotStateTransitionNotifier.getInstance().onRobotShutdown();
        RobotLog.v("Swerve: OpModeManager: ... onRobotShutdown complete");
        }
    }
