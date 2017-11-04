package org.firstinspires.ftc.teamcode._Test._AutoLib;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode._Libs.AutoLib;

// an example of an autonomous OpMode derived from a common base class mode that does all the shared setup
// for a bunch of similar OpModes running on the same hardware --
// each derived OpMode just creates its own sequence and maybe also gets additional hardware that only it needs.

@Autonomous(name="Test: RedBallGyro from AutoOpModeBase", group ="Test")
//@Disabled
public class RedBallGyroAutoOpMode extends AutoOpModeBase {

    boolean bSetup = false;             // true when we're in "setup mode" where joysticks tweak parameters
    Servo bServoLeft, bServoRight;      // additional hardware used by this derived OpMode

    public void _init()
    {
        // example: get some additional hardware that the shared base doesn't need
        bServoLeft = mFactory.getServo("servoLeft");
        bServoRight = mFactory.getServo("servoRight");

        // add Steps to the sequence - use Gyro heading convention of positive degrees CW from initial heading
        float power = 0.5f;

        AutoLib.ConcurrentSequence cs1 = new AutoLib.ConcurrentSequence();
        cs1.add(new AutoLib.ServoStep(bServoLeft, 0.05));
        cs1.add(new AutoLib.ServoStep(bServoRight, 0.95));
        mSequence.add(cs1);

        mSequence.add(new AutoLib.LogTimeStep(this, "wait for 10 seconds", 10.0));

        mSequence.add(new AutoLib.AzimuthTimedDriveStep(this, 0, mCorrGyro, mPid, mMotors, power, 2.5f, false));
        mSequence.add(new AutoLib.AzimuthTimedDriveStep(this, -90, mCorrGyro, mPid, mMotors, power, 2.0f, false));

        AutoLib.ConcurrentSequence cs2 = new AutoLib.ConcurrentSequence();
        cs2.add(new AutoLib.ServoStep(bServoLeft, 0.95));
        cs2.add(new AutoLib.ServoStep(bServoRight, 0.05));
        mSequence.add(cs2);

        mSequence.add(new AutoLib.AzimuthTimedDriveStep(this, -90, mCorrGyro, mPid, mMotors, power, 2.2f, true));
    }

    public boolean _loop()
    {
        // example: this particular mode uses the gamepad to adjust PID parameters dynamically
        if (gamepad1.y)
             bSetup = true;     // enter "setup mode" using controller inputs to set Kp and Ki
        if (gamepad1.x)
            bSetup = false;     // exit "setup mode"

        if (bSetup) {           // if in "setup mode" ...
            // adjust PID parameters by joystick inputs
            Kp -= (gamepad1.left_stick_y * 0.0001f);
            Ki -= (gamepad1.right_stick_y * 0.0001f);
            // update the parameters of the PID used by all Steps in this test
            mPid.setK(Kp, Ki, Kd, KiCutoff);
            // log updated values to the operator's console
            telemetry.addData("Kp = ", Kp);
            telemetry.addData("Ki = ", Ki);
        }

        return bSetup;      // if we return true, then the base class loop() DOESN'T do its normal stuff (run the sequence)
    }
}
