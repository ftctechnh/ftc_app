// Simple autonomous program that drives bot forward until end of period
// or touch sensor is hit. If touched, backs up a bit and turns 90 degrees
// right and keeps going. Demonstrates obstacle avoidance and use of the
// REV Hub's built in IMU in place of a gyro. Also uses gamepad1 buttons to
// simulate touch sensor press and supports left as well as right turn.
//
// Also uses IMU to drive in a straight line when not avoiding an obstacle.

// from http://stemrobotics.cs.pdx.edu/node/7265
//  removed touch sensor code

package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

@Autonomous(name="Blue Left", group="Bacon Autonomous!")
//@Disabled
public class FinalPerfectBlueLeft extends LinearOpMode
{
  /* Declare all devices since hardware class isn't working */
    DcMotor                 frontLeftMotor;
    DcMotor                 backLeftMotor;
    DcMotor                 frontRightMotor;
    DcMotor                 backRightMotor;
    DcMotor                 verticalArmMotor;
    ColorSensor             colorSensor;
    Servo                   gemServo;
    BNO055IMU               imu;
  /* Set up and init all variables */
    Orientation             lastAngles = new Orientation();
    double globalAngle, power = .30, correction;
    double xPosUp = 0;
    double xPosDown = .55;

  /* Create a "timer" that begins once the OpMode begins */
    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() throws InterruptedException
    {
    /* Find all hardware in configuration */
        frontLeftMotor = hardwareMap.dcMotor.get("FL");
        backLeftMotor = hardwareMap.dcMotor.get("BL");
        frontRightMotor = hardwareMap.dcMotor.get("FR");
        backRightMotor = hardwareMap.dcMotor.get("BR");
        verticalArmMotor = hardwareMap.dcMotor.get("VAM");
        gemServo = hardwareMap.servo.get("gemservo");
        colorSensor = hardwareMap.colorSensor.get("colorsensor");

    /* Reverse the direction of the front right and back right motors */
        frontRightMotor.setDirection(DcMotor.Direction.REVERSE);
        backRightMotor.setDirection(DcMotor.Direction.REVERSE);

    /* When robot is off remove the brake on the wheel motors */
        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

    /* Set parameters for the gyro */
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();

        parameters.mode                = BNO055IMU.SensorMode.IMU;
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled      = false;

        // Retrieve and initialize the IMU. We expect the IMU to be attached to an I2C port
        // on a Core Device Interface Module, configured to be a sensor of type "AdaFruit IMU",
        // and named "imu".
        imu = hardwareMap.get(BNO055IMU.class, "imu");

    /* Initialize all motors and servoes to their starting positions */
        frontLeftMotor.setPower(0);
        frontRightMotor.setPower(0);
        backLeftMotor.setPower(0);
        backRightMotor.setPower(0);
        gemServo.setPosition(xPosUp);

    /* Initialize the gyro with the parameters above */
        imu.initialize(parameters);

    /* Tell driver the gyro is calibrating */
        telemetry.addData("I am...", "calibrating...");
        telemetry.update();

    /* Make sure the imu gyro is calibrated before continuing */
        while (!isStopRequested() && !imu.isGyroCalibrated())
        {
            sleep(50);
            idle();
        }

        telemetry.addLine("Um...waiting for start");
        telemetry.addData("Imu calibration?", imu.getCalibrationStatus().toString());
        telemetry.update();

        // wait for start button.
        waitForStart();

        /* Let the team know wassup */
        telemetry.addLine("Ayyy, I'm running");
        telemetry.update();

        /* Move the claw up so it doesn't dig into the ground coming off the balance board */
        moveclawbytime(1,.5,"Up");

        /* Put the servo color arm down */
        gemServo.setPosition(xPosDown);
        sleep(1500);

        /* Knock of the Red jewel */
        knockjewelRed();

        //////////////////* Begin the variance *\\\\\\\\\\\\\\\\\\\\\\\\\\\

        /////////////////* This is the Blue Left Case *\\\\\\\\\\\\\\\\\\\\\\\

        /* Drive forward into the triangle */
        movebytime(1.2,.5,"Forward");

        /* Wait a moment for the robot to stop moving */
        sleep(500);


        /////////* This should vary based on vuforia readings *\\\\\\\\\\\\\\\\\\\\\\
        /* Move forward slightly so the block is in the space */
        movebytime(.5, .3, "Right");
        /////////////////////////////////////////////////////////////////////////////


        /////////////////* End the variance *\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

        /* Move the claw down slightly */
        moveclawbytime(.5,.3,"Down");
    }

/***********************************************************************************************
 * These are all of the methods used in the Autonomous*
 ***********************************************************************************************/

/* This method moves the claw up or down for a certain amount of time either up or down */
    public void moveclawbytime(double time, double power, String direction) {
    /* reset the "timer" to 0 */
        runtime.reset();

    /* This switch case is determined by the String indicated above */
        switch (direction) {
            case "Up":
                verticalArmMotor.setPower(power);
                break;
            case "Down":
                verticalArmMotor.setPower(power);
                break;
        }

    /* If the timer hasn't reached the time that is indicated do nothing and keep the wheels powered */
        while (opModeIsActive() && runtime.seconds() < time) {
        }

    /* Once the while loop above finishes turn off claw motor */
        verticalArmMotor.setPower(0);
    }

/* This method moves the robot forward for time and power indicated*/
    public void movebytime (double time, double power, String direction) {
    /* reset the "timer" to 0 */
        runtime.reset();

    /* This switch case is determined by the String direction indicated above */

        switch (direction) {
            case "Forward":
                setWheelPower(power, -power, power, -power);
                break;
            case "Backward":
                setWheelPower(-power, power, -power, power);
                break;
            case "Right":
                setWheelPower(power, power, -power, -power);
                break;
            case "Left":
                setWheelPower(-power, -power, power, power);
                break;
        }
    /* If the timer hasn't reached the time that is indicated do nothing and keep the wheels powered */
        while (opModeIsActive() && runtime.seconds() < time) {

        }
    /* Once the while loop above finishes turn off the wheels */
        wheelsOff();
    }


/* This method simply sets all wheel motors to zero power */
    public void wheelsOff() {
        setWheelPower(0,0,0,0);
    }

/* This method powers each wheel to whichever power is desired */
    public void setWheelPower(double fl, double fr, double bl, double br) {

        /* Create power variables */
        double frontLeft;
        double frontRight;
        double backLeft;
        double backRight;

        double FrontLeftPower;
        double FrontRightPower;
        double BackLeftPower;
        double BackRightPower;

        FrontLeftPower = 0;
        FrontRightPower = 0;
        BackLeftPower = 0;
        BackRightPower = 0;

        /* Initialize the powers with the values input whenever this method is called */
        frontLeft   =   fl;
        frontRight  =   fr;
        backLeft    =   bl;
        backRight   =   br;

        /* set each wheel to the power indicated whenever this method is called */
        if ( FrontLeftPower != frontLeft) {
            frontLeftMotor.setPower(-fl);
            FrontLeftPower = frontLeft;
        }
        if ( FrontRightPower != frontRight) {
            frontRightMotor.setPower(fr);
            FrontRightPower = frontRight;
        }
        if ( BackLeftPower != backLeft) {
            backLeftMotor.setPower(-bl);
            BackLeftPower = backLeft;
        }
        if ( BackRightPower != backRight)
            backRightMotor.setPower(br);
            BackRightPower = backRight;
    }

/* This method is tells the color sensor to read color, then rotate to knock off the blue
jewel and then return the color sensor arm back up */
    public void knockjewelRed() {

        if (colorSensor.red() < colorSensor.blue()) {
            resetAngle();
            rotate(10, .3);
            wheelsOff();
            sleep(500);
            gemServo.setPosition(xPosUp);
            rotate(-10,.3);
        } else {
            resetAngle();
            rotate(-10, .3);
            wheelsOff();
            sleep(500);
            gemServo.setPosition(xPosUp);
            rotate(10,.3);
        }
    }

/* This method is tells the color sensor to read color, then rotate to knock off the red
jewel and then return the color sensor arm back up */
    public void knockjewelBlue(){

        if (colorSensor.red() > colorSensor.blue()) {
            resetAngle();
            rotate(10, .3);
            wheelsOff();
            sleep(500);
            gemServo.setPosition(xPosUp);
            rotate(-10,.3);
        } else {
            resetAngle();
            rotate(-10, .3);
            wheelsOff();
            sleep(500);
            gemServo.setPosition(xPosUp);
            rotate(10,.3);
        }

    }

    /**
     * Resets the cumulative angle tracking to zero.
     */
    private void resetAngle()
    {
        lastAngles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        globalAngle = 0;
    }

    /**
     * Get current cumulative angle rotation from last reset.
     * @return Angle in degrees. + = left, - = right.
     */
    private double getAngle()
    {

        // We experimentally determined the Z axis is the axis we want to use for heading angle.
        // We have to process the angle because the imu works in euler angles so the Z axis is
        // returned as 0 to +180 or 0 to -180 rolling back to -179 or +179 when rotation passes
        // 180 degrees. We detect this transition and track the total cumulative angle of rotation.

        Orientation angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        double deltaAngle = angles.firstAngle - lastAngles.firstAngle;

        if (deltaAngle < -180)
            deltaAngle += 360;
        else if (deltaAngle > 180)
            deltaAngle -= 360;

        globalAngle += deltaAngle;

        lastAngles = angles;

        return globalAngle;
    }

    /**
     * See if we are moving in a straight line and if not return a power correction value.
     * @return Power adjustment, + is adjust left - is adjust right.
     */
    private double checkDirection()
    {
        // The gain value determines how sensitive the correction is to direction changes.
        // You will have to experiment with your robot to get small smooth direction changes
        // to stay on a straight line.
        double correction, angle, gain = .10;

        angle = getAngle();

        if (angle == 0)
            correction = 0;             // no adjustment.
        else
            correction = -angle;        // reverse sign of angle for correction.

        correction = correction * gain;

        return correction;
    }

    /**
     * Rotate left or right the number of degrees. Does not support turning more than 180 degrees.
     * @param degrees Degrees to turn, + is left - is right
     */
    private void rotate(int degrees, double power)
    {
        double  leftPower, rightPower;

        // restart imu movement tracking.
        resetAngle();

        // getAngle() returns + when rotating counter clockwise (left) and - when rotating
        // clockwise (right).

        if (degrees < 0)
        {   // turn right.
            leftPower = -power;
            rightPower = power;
        }
        else if (degrees > 0)
        {   // turn left.
            leftPower = power;
            rightPower = -power;
        }
        else return;

        // set power to rotate.
        frontLeftMotor.setPower(leftPower);
        backLeftMotor.setPower(leftPower);

        frontRightMotor.setPower(rightPower);
        backRightMotor.setPower(rightPower);

        // rotate until turn is completed.
        if (degrees < 0)
        {
            // On right turn we have to get off zero first.
            while (opModeIsActive() && getAngle() == 0) {}

            while (opModeIsActive() && getAngle() > degrees) {}
        }
        else    // left turn.
            while (opModeIsActive() && getAngle() < degrees) {}

        // turn the motors off.
        frontLeftMotor.setPower(0);
        backLeftMotor.setPower(0);
        frontRightMotor.setPower(0);
        backRightMotor.setPower(0);

        // wait for rotation to stop.
        sleep(1000);

        // reset angle tracking on new heading.
        resetAngle();
    }
}