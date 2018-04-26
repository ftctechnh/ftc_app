package org.firstinspires.ftc.teamcode.opmodes.old2017;

/**
 * Created by Robotics on 3/7/2017.
 */

import com.qualcomm.robotcore.eventloop.opmode.*;

import org.firstinspires.ftc.teamcode.libraries.hardware.BotHardwareOld;
import org.firstinspires.ftc.teamcode.libraries.SensorLib;
import org.firstinspires.ftc.teamcode.libraries.AutoLib;

@Autonomous(name="Block Auto (Shoot Balls, Defense)", group="Main")
@Disabled
public class BlockAuto extends OpMode {

    BotHardwareOld robot = new BotHardwareOld();

    AutoLib.Sequence mShoot;

    boolean bDone = false;

    boolean redColor;

    OpMode modePointer;

    // parameters of the PID controller for this sequence's first part
    float Kp = 0.05f;        // degree heading proportional term correction per degree of deviation
    //float Ki = 0.02f;         // ... integrator term
    float Ki = 0.00f;
    float Kd = 0;             // ... derivative term
    float KiCutoff = 3.0f;    // maximum angle error for which we update integrator

    SensorLib.PID gPid = new SensorLib.PID(Kp, Ki, Kd, KiCutoff);

    public BlockAuto(OpMode mode, boolean isRed){
        modePointer = mode;
        redColor = isRed;
    }

    @Override
    public void init(){
        //init hardware objects
        final boolean debug = false;
        robot = new BotHardwareOld();
        robot.init(modePointer, debug, true);
        robot.setMaxSpeedAll(2500);

        mShoot = new AutoLib.LinearSequence();

        mShoot.add(new AutoLib.LogTimeStep(modePointer, "Wait", 10.5));
        mShoot.add(new AutoLib.MoveByEncoderStep(robot.getMotorArray(), 0.4f, 1450, true));
        mShoot.add(new AutoLib.LogTimeStep(modePointer, "YAY", 0.5));
        mShoot.add(new AutoLib.EncoderMotorStep(robot.launcherMotor, 1.0f,  1500, true, modePointer));
        mShoot.add(new AutoLib.TimedServoStep(robot.ballServo, 0.6f, 0.8, false));
        mShoot.add(new AutoLib.EncoderMotorStep(robot.launcherMotor, 1.0f, 1500, true, modePointer));

        int direction;
        if(redColor) direction = 25;
        else direction = -25;

        //mShoot.add(new AutoLib.GyroTurnStep(modePointer, direction, robot.getNavXHeadingSensor(), robot.getMotorArray(), 0.4f, 3.0f, true));
        mShoot.add(new AutoLib.MoveByEncoderStep(robot.getMotorArray(), 0.5f, 2000, true));
    }

    @Override
    public void init_loop(){
        modePointer.telemetry.addData("NavX Connected", robot.navX.isConnected());
        modePointer.telemetry.addData("NavX Ready", robot.startNavX());
    }

    @Override
    public void start(){
        robot.navX.zeroYaw();
    }

    @Override
    public void loop(){
        // until we're done, keep looping through the current Step(s)
        if (!bDone)
            bDone = mShoot.loop();       // returns true when we're done
        else
            modePointer.telemetry.addData("sequence finished", "");
    }

    @Override
    public void stop(){
        super.stop();
    }

}
