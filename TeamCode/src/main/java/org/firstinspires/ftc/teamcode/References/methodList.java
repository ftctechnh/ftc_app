
package org.firstinspires.ftc.teamcode.References;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;

/**
 * Hi! ☺ This is a list of all methods used during the 2017-18 season ☺!
 */

public class methodList extends LinearOpMode {
    /* The methodClass is what we're referencing in Section 1. */
    methodClass robot = new methodClass();

    /* The relicClass is what references specific hardware pieces during the 2018-19 season in Section 2. */
    relicClass relicbot = new relicClass();

    /* Unfortunately, imu needs to called & initialized here, not in methodClass */
    BNO055IMU imu;
    Orientation lastAngles = new Orientation();
    double globalAngle;

    @Override
    public void runOpMode() throws InterruptedException {
    /* This is imu initialization */
        /* Set parameters for the gyro (imu)*/
        BNO055IMU.Parameters imuparameters = new BNO055IMU.Parameters();

        imuparameters.mode = BNO055IMU.SensorMode.IMU;
        imuparameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        imuparameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        imuparameters.loggingEnabled = false;

        // Retrieve and initialize the IMU. We expect the IMU to be attached to an I2C port
        // on a Core Device Interface Module, configured to be a sensor of type "AdaFruit IMU",
        // and named "imu".
        imu = hardwareMap.get(BNO055IMU.class, "imu");
    }

    /***********************************************************************************************
     * Sect 1. These methods could be pretty useful no matter what the game is *
     ***********************************************************************************************/
    /* This resets the encoders on the wheels to RUN_WITHOUT_ENCODER */
    public void runWithoutEncoder() {
        robot.frontLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.frontRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.backLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.backRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        sleep(100);
        robot.frontLeftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.frontRightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.backLeftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.backRightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    /* This resets the encoders back to ZERO while still running with RUN_TO_POSITION */
    public void resetRunToPosition() {
        robot.frontLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.frontRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.backLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.backRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        sleep(100);
        robot.frontLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.frontRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.backLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.backRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    /* This tells the wheels to run to a position for a certain number of ticks, at a certain power
  and in a certain direction */
    public void runToPositionWheels(int ticks, double power, String direction) {
        switch (direction) {
            case "Forward":
               robot.frontRightMotor.setTargetPosition(ticks);
               robot.frontLeftMotor.setTargetPosition(ticks);
               robot.backRightMotor.setTargetPosition(ticks);
               robot.backLeftMotor.setTargetPosition(ticks);
                break;
            case "Backward":
               robot.frontRightMotor.setTargetPosition(-ticks);
               robot.frontLeftMotor.setTargetPosition(-ticks);
               robot.backRightMotor.setTargetPosition(-ticks);
               robot.backLeftMotor.setTargetPosition(-ticks);
                break;
            case "Right":
               robot.frontRightMotor.setTargetPosition(-ticks);
               robot.frontLeftMotor.setTargetPosition(ticks);
               robot.backRightMotor.setTargetPosition(ticks);
               robot.backLeftMotor.setTargetPosition(-ticks);
                break;
            case "Left":
               robot.frontRightMotor.setTargetPosition(ticks);
               robot.frontLeftMotor.setTargetPosition(-ticks);
               robot.backRightMotor.setTargetPosition(-ticks);
               robot.backLeftMotor.setTargetPosition(ticks);
                break;
        }

        switch (direction) {
            case "Forward":
                setWheelPower(-power, power, -power, power);
                break;
            case "Backward":
                setWheelPower(power, -power, power, -power);
                break;
            case "Right":
                setWheelPower(-power, -power, power, power);
                break;
            case "Left":
                setWheelPower(power, power, -power, -power);
                break;
        }
    }
    
    /* This method moves the robot forward for time and power indicated*/
    public void movebytime(long time, double power, String direction) {
    /* This switch case is determined by the String direction indicated above */
        switch (direction) {
            case "Forward":
                setWheelPower(-power, power, -power, power);
                break;
            case "Backward":
                setWheelPower(power, -power, power, -power);
                break;
            case "Right":
                setWheelPower(-power, -power, power, power);
                break;
            case "Left":
                setWheelPower(power, power, -power, -power);
                break;
        }

    /* Sleep */
        sleep(time);

  /* Once the while loop above finishes turn off the wheels */
        wheelsOff();
    }

    /* This method simply sets all wheel motors to zero power */
    public void wheelsOff() {
        setWheelPower(0, 0, 0, 0);
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
        frontLeft = fl;
        frontRight = fr;
        backLeft = bl;
        backRight = br;

        /* set each wheel to the power indicated whenever this method is called */
        if (FrontLeftPower != frontLeft) {
           robot.frontLeftMotor.setPower(-fl);
            FrontLeftPower = frontLeft;
        }
        if (FrontRightPower != frontRight) {
           robot.frontRightMotor.setPower(fr);
            FrontRightPower = frontRight;
        }
        if (BackLeftPower != backLeft) {
           robot.backLeftMotor.setPower(-bl);
            BackLeftPower = backLeft;
        }
        if (BackRightPower != backRight)
           robot.backRightMotor.setPower(br);
        BackRightPower = backRight;
    }

    /* Resets the cumulative angle tracking to zero.*/
    private void resetAngle() {
        lastAngles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        globalAngle = 0;
    }

    /* Get current cumulative angle rotation from last reset. @return Angle in degrees. + = left, - = right. */
    private double getAngle() {

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

    /* Rotate left or right the number of degrees.  Degrees to turn, + is left - is right, no more than 180 */
    private void rotate(int degrees, double power) {
        double leftPower, rightPower;

        // restart imu movement tracking.
        resetAngle();

        // getAngle() returns + when rotating counter clockwise (left) and - when rotating
        // clockwise (right).

        if (degrees < 0) {   // turn right.
            leftPower = power;
            rightPower = -power;
        } else if (degrees > 0) {   // turn left.
            leftPower = -power;
            rightPower = power;
        } else return;

        // set power to rotate.
       robot.frontLeftMotor.setPower(leftPower);
       robot.backLeftMotor.setPower(leftPower);

       robot.frontRightMotor.setPower(rightPower);
       robot.backRightMotor.setPower(rightPower);

        // rotate until turn is completed.
        if (degrees < 0) {
            // On right turn we have to get off zero first.
            while (opModeIsActive() && getAngle() == 0) {
            }

            while (opModeIsActive() && getAngle() > degrees) {
            }
        } else    // left turn.
            while (opModeIsActive() && getAngle() < degrees) {
            }

        // turn the motors off.
       robot.frontLeftMotor.setPower(0);
       robot.backLeftMotor.setPower(0);
       robot.frontRightMotor.setPower(0);
       robot.backRightMotor.setPower(0);

        // wait for rotation to stop.
        sleep(1000);

        // reset angle tracking on new heading.
        resetAngle();
    }

    /***********************************************************************************************
     * Sect 2. 2017-18 Specific Game Methods *
     ***********************************************************************************************/

    /* This moves the robot so it's ready to turn 45 towards the cryptobox based on
    the vuforia key reading*/
    public void keyAlignment(String teamColorPosition, RelicRecoveryVuMark vuMark) {
        runWithoutEncoder();
        switch (teamColorPosition) {
            case "BlueRight":
                vumarkSwitch(vuMark, 85, 50, 40, .5);
                break;
            case "BlueLeft":
                vumarkSwitch(vuMark, -85, -60, -50, .5);
                break;
            case "RedRight":
                vumarkSwitch(vuMark, -85, -60, -50, .5);
                break;
            case "RedLeft":
                vumarkSwitch(vuMark, -40, -50, -85, .5);
                break;

        }
    }

    /* This method is the switch case for vuMark alignment */
    public void vumarkSwitch(RelicRecoveryVuMark vuMark, int leftRotate, int centerRotate, int rightRotate, double power) {
        switch (vuMark) {
            case LEFT:
                rotate(leftRotate, power);
                sleep(500);
                resetRunToPosition();
                runToPositionWheels(400, .5, "Backward");
                sleep(1500);
                /* Place Glyph */
                placeGlyphOut();
                resetRunToPosition();
                runToPositionWheels(200, .5, "Forward");
                sleep(1500);
                resetRunToPosition();
                runToPositionWheels(500, .5, "Backward");
                sleep(1500);
                resetRunToPosition();
                runToPositionWheels(300, .5, "Forward");
                sleep(1500);
                placeTrayIn();
                sleep(100);
                break;
            case CENTER:
                rotate(centerRotate, power);
                sleep(500);
                resetRunToPosition();
                runToPositionWheels(400, .5, "Backward");
                sleep(1500);
                placeGlyphOut();
                runToPositionWheels(200, .5, "Forward");
                sleep(1500);
                resetRunToPosition();
                runToPositionWheels(500, .5, "Backward");
                sleep(1500);
                resetRunToPosition();
                runToPositionWheels(300, .5, "Forward");
                sleep(1500);
                placeTrayIn();
                sleep(100);
                break;
            case UNKNOWN:
                rotate(centerRotate, power);
                sleep(2000);
                resetRunToPosition();
                runToPositionWheels(400, .5, "Backward");
                sleep(2000);
                /* Place Glyph */
                placeGlyphOut();
                runToPositionWheels(200, .5, "Forward");
                sleep(1500);
                resetRunToPosition();
                runToPositionWheels(500, .5, "Backward");
                sleep(1500);
                resetRunToPosition();
                runToPositionWheels(300, .5, "Forward");
                sleep(1500);
                placeTrayIn();
                sleep(100);
                break;
            case RIGHT:
                rotate(rightRotate, power);
                sleep(2000);
                runToPositionWheels(400, .5, "Backward");
                sleep(2000);
                /* Place Glyph */
                placeGlyphOut();
                runToPositionWheels(200, .5, "Forward");
                sleep(1000);
                resetRunToPosition();
                runToPositionWheels(500, .5, "Backward");
                sleep(1000);
                resetRunToPosition();
                runToPositionWheels(300, .5, "Forward");
                sleep(1000);
                placeTrayIn();
                sleep(100);
                break;
        }

    }

    /* This flips out */
    public void placeGlyphOut() {
        relicbot.trayMotor.setTargetPosition(relicbot.trayOut);
        relicbot.trayMotor.setPower(-.2);
        sleep(2000);
//        trayMotor.setTargetPosition(trayIn);
//        trayMotor.setPower(.2);
//        sleep(2000);
    }

    /* This flips in */
    public void placeTrayIn() {
        relicbot.trayMotor.setTargetPosition(relicbot.trayIn);
        relicbot.trayMotor.setPower(.2);
        sleep(2000);

    }

    /* This method moves the tray to position */
    public void setTrayPosition(String position) {
        switch (position) {
            case "Out":
                relicbot.trayMotor.setTargetPosition(relicbot.trayOut);
                relicbot.trayMotor.setPower(-.4);
                break;
            case "In":
                relicbot.trayMotor.setTargetPosition(relicbot.trayIn);
                relicbot.trayMotor.setPower(.4);
                break;
        }
    }

    /* This method is tells the color sensor to read color, then rotate to knock off the blue
    jewel and then return the color sensor arm back up */
    public void knockjewelRed() {

        if (relicbot.colorSensor.red() < relicbot.colorSensor.blue()) {
            resetAngle();
            rotate(10, .3);
            wheelsOff();
            sleep(200);
            relicbot.gemServo.setPosition(relicbot.xPosUp);
            rotate(-10, .3);
        } else {
            resetAngle();
            rotate(-10, .3);
            wheelsOff();
            sleep(200);
            relicbot.gemServo.setPosition(relicbot.xPosUp);
            rotate(10, .3);
        }
    }

    /* This method is tells the color sensor to read color, then rotate to knock off the red
    jewel and then return the color sensor arm back up */
    public void knockjewelBlue() {

        if (relicbot.colorSensor.red() > relicbot.colorSensor.blue()) {
            resetAngle();
            rotate(10, .3);
            wheelsOff();
            sleep(200);
            relicbot.gemServo.setPosition(relicbot.xPosUp);
            rotate(-10, .3);
        } else {
            resetAngle();
            rotate(-10, .3);
            wheelsOff();
            sleep(200);
            relicbot.gemServo.setPosition(relicbot.xPosUp);
            rotate(10, .3);
        }
    }
}