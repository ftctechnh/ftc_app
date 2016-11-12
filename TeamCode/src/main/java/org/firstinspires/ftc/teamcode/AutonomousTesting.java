package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.*;
import com.qualcomm.robotcore.hardware.DcMotor;


/**
 * simple example of using a Step that makes a bot with "squirrely wheels" drive along a given course
 * Created by phanau on 10/31/16.
 */


// simple example sequence that tests either of gyro-based AzimuthCountedDriveStep or AzimuthTimedDriveStep to drive along a square path
@Autonomous(name="Testing Servo Code", group="Test")
//@Disabled
public class AutonomousTesting extends OpMode {

    AutoLib.Sequence mSequence;             // the root of the sequence tree
    boolean bDone;                          // true when the programmed sequence is done
    DcMotor mMotors[];                      // motors, some of which can be null: assumed order is fr, br, fl, bl

    //VuforiaLib_FTC2016 Vuf;

    BotHardware robot = new BotHardware();

    //some constants to make navigating the field easier
    static final double mmToEncode = 1; //TODO: Find this value
    static final double inchToMm = 25.4;
    static final double footToMm = inchToMm * 12;
    static final double squareToMm = footToMm * 2;

    // create an autonomous sequence with the steps to drive
    static final float power = 0.5f;
    static final float error = 5.0f;       // get us within 10 degrees for this test
    static final float targetZ = 6*25.4f;

    final boolean debug = true;

    @Override
    public void init() {
        robot.init(this, debug);

        //Vuf = new VuforiaLib_FTC2016();
        //Vuf.init(this, null);     // pass it this OpMode (so it can do telemetry output) and use its license key for now

        // create the root Sequence for this autonomous OpMode
        mSequence = new AutoLib.LinearSequence();

        mSequence.add(new AutoLib.TimedServoStep(robot.leftServo, 0.5, 5.0, true));

        // start out not-done
        bDone = false;
    }

    @Override
    public void start(){
        //Vuf.start();
    }

    @Override
    public void loop() {

        // until we're done, keep looping through the current Step(s)
        if (!bDone)
            bDone = mSequence.loop();       // returns true when we're done
        else
            telemetry.addData("First sequence finished", "");

        //Vuf.loop(true);
    }

    @Override
    public void stop() {
        super.stop();
        //Vuf.stop();
    }
}

