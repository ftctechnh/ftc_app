package org.firstinspires.ftc.teamcode.framework;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

public abstract class Match extends LinearOpMode {

    public ElapsedTime runTime;

    //Setup OpMode instance to allow other classes to access hardwareMap and Telemetry
    private static OpMode opmode = null;
    public static OpMode getOpModeInstance(){
        return opmode;
    }

    public Match(){
        //setup telemetry and hardwareMap
        opmode = this;
        runTime = new ElapsedTime();
    }

    @Override
    public void runOpMode(){
        autonInit();

        while(opModeIsActive() && !isStopRequested() && !isStarted()) autonInitLoop();

        runTime.reset();
        if(opModeIsActive() && !isStopRequested()) autonRun();

        autonStop();

        if(opModeIsActive() && !isStopRequested()) teleopInit();

        while(opModeIsActive() && !isStopRequested()) teleopInitLoop();

        while (opModeIsActive() && !isStopRequested()) teleopLoop();

        teleopStop();
    }

    public abstract void autonInit();

    public void autonInitLoop(){

    }

    public abstract void autonRun();

    public void autonStop(){

    }

    public abstract void teleopInit();

    public void teleopInitLoop(){

    }

    public abstract void teleopLoop();

    public void teleopStop(){

    }
}
