package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

/**
 ☺ Hi! This is the perfect teleop code for December 16, 2017! ☺
 */
@TeleOp(name = "♪ ♥ Drive Mode 3 (nonlinear w/ auto) ♥ ♪", group = "Our Teleop")
//@Disabled
public class FinalPerfectTeleopWithAutostuff extends LinearOpMode {

    /* Set up and init all variables */
    Orientation             lastAngles = new Orientation();
    double globalAngle;

    /* This says use MasterHardwareClass */
    MasterHardwareClass robot = new MasterHardwareClass();

    /*These values are used for the drive*/
    double frontLeft;
    double frontRight;
    double backLeft;
    double backRight;

    @Override
    public void runOpMode() {

        /* The init() method of the hardware class does all the work here*/
        robot.init(hardwareMap);

    /* Set parameters for the gyro (imu)*/
        BNO055IMU.Parameters imuparameters = new BNO055IMU.Parameters();

        imuparameters.mode                = BNO055IMU.SensorMode.IMU;
        imuparameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        imuparameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        imuparameters.loggingEnabled      = false;

    /* Initialize the gyro with the parameters above */
        robot.imu.initialize(imuparameters);

            /* Tell driver the gyro is calibrating */
        telemetry.addLine("I'm calibrating...");
        telemetry.update();

    /* Make sure the imu gyro is calibrated before continuing */
        while (!isStopRequested() && !robot.imu.isGyroCalibrated())
        {
            sleep(50);
            idle();
        }

         /* Calibrate the gyro */
        robot.gyro.calibrate();
        telemetry.addLine("♫ Gyro Calibrating ♫");
        telemetry.update();
        while (robot.gyro.isCalibrating()) {
            sleep(50);
            idle();
        }
        telemetry.addLine("!Gyro Calibrated!");
        telemetry.update();
        idle();


        // Wait for the start button
        telemetry.addLine("!☻ Ready to Run ☻!");
        telemetry.update();
        waitForStart();

        while (opModeIsActive()) {

            // Display the current value
            telemetry.addLine("Hi~♪");
            telemetry.addData("Claw Opening Controls", "X is Close, B is Open");
            telemetry.addData("Claw Moving Controls", "Use the D-Pad ↑ & ↓ buttons!");

            telemetry.addData("Position:", robot.imu.getPosition());
            telemetry.addData("Angular Orientation", robot.imu.getAngularOrientation());
            telemetry.addData("Gyro Value:", robot.gyro.getHeading());
            telemetry.addData("Current imu value:", getAngle());
            telemetry.update();

        /* Set the arm up */
//            if (robot.gemServo.getPosition() != robot.xPosUp) {
//                robot.gemServo.setPosition(robot.xPosUp);
//            }
            robot.gemServo.setPosition(robot.xPosUp);

        /* Servo Control */
            if (gamepad2.x) {
                if (robot.ClawPower != robot.clawClose) {
                    robot.clawServo.setPower(robot.clawClose);
                    robot.ClawPower = robot.clawClose;
                }
            }

            if (gamepad2.b) {
                if (robot.ClawPower != robot.clawOpen) {
                    robot.clawServo.setPower(robot.clawOpen);
                    robot.ClawPower = robot.clawOpen;
                }
            }

            if (!gamepad2.b && !gamepad2.x) {
                if(robot.ClawPower != robot.clawStill){
                    robot.clawServo.setPower(robot.clawStill);
                    robot.ClawPower = robot.clawStill;
                }
            }

        /* Vertical Arm Motor */
            if (gamepad2.dpad_up) {
                if (robot.VerticalArmPower != 1) {
                    robot.verticalArmMotor.setPower(1);
                    robot.VerticalArmPower = 1;
                }
            }
            else {
                if (robot.VerticalArmPower != 0) {
                    robot.verticalArmMotor.setPower(0);
                    robot.VerticalArmPower = 0;
                }

                if (gamepad2.dpad_down) {
                    if (robot.VerticalArmPower != -1) {
                        robot.verticalArmMotor.setPower(-1);
                        robot.VerticalArmPower = -1;
                    }
                } else {
                    if (robot.VerticalArmPower != 0) {
                        robot.verticalArmMotor.setPower(0);
                        robot.VerticalArmPower = 0;
                    }
                }
            }

        /* Imu smarts */
            if(gamepad1.dpad_up){
                rotate(0, 1);
            }

            if(gamepad1.dpad_down){
                rotate(90, 1);
            }

            if(gamepad1.dpad_left){
                rotate(180, 1);
            }

            if(gamepad1.dpad_right){
                rotate(-180, 1);
            }

//
//        /* Rotational Drive Control */
//            if (gamepad1.left_bumper && gamepad1.right_stick_x < 0 || gamepad1.right_stick_x > 0) {
//
//                double GRX = gamepad1.right_stick_x / robot.bumperSlowest;
//
//                final double v1 = +GRX;
//                final double v2 = -GRX;
//                final double v3 = +GRX;
//                final double v4 = -GRX;
//
//                frontLeft = v1;
//                frontRight = v2;
//                backLeft = v3;
//                backRight = v4;
//
//                setWheelPower(frontLeft, frontRight, backLeft, backRight);
//            } else {
//
//                double GRX = gamepad1.right_stick_x / robot.nobumper;
//
//                final double v1 = +GRX;
//                final double v2 = -GRX;
//                final double v3 = +GRX;
//                final double v4 = -GRX;
//
//                frontLeft = v1;
//                frontRight = v2;
//                backLeft = v3;
//                backRight = v4;
//
//                setWheelPower(frontLeft, frontRight, backLeft, backRight);
//            }

        /* Drive Control */
            if (gamepad1.left_bumper) {
                double GLY = -gamepad1.left_stick_y / robot.bumperSlowest;
                double GRX = gamepad1.right_stick_x / robot.bumperSlowest;
                double GLX = gamepad1.left_stick_x  / robot.bumperSlowest;

                final double v1 = GLY + GRX + GLX;
                final double v2 = GLY - GRX - GLX;
                final double v3 = GLY + GRX - GLX;
                final double v4 = GLY - GRX + GLX;

                frontLeft = v1;
                frontRight = v2;
                backLeft = v3;
                backRight = v4;

                setWheelPower(frontLeft, frontRight, backLeft, backRight);
            } else {
                    double GLY = -gamepad1.left_stick_y / robot.nobumper;
                    double GRX = gamepad1.right_stick_x / robot.nobumper;
                    double GLX =  gamepad1.left_stick_x / robot.nobumper;

                    final double v1 = GLY + GRX + GLX;
                    final double v2 = GLY - GRX - GLX;
                    final double v3 = GLY + GRX - GLX;
                    final double v4 = GLY - GRX + GLX;

                    frontLeft = v1;
                    frontRight = v2;
                    backLeft = v3;
                    backRight = v4;

                    setWheelPower(frontLeft, frontRight, backLeft, backRight);

                }
            }
        }

    /***********************************************************************************************
     * These are all of the methods used in the Teleop*
     ***********************************************************************************************/

    /* This method powers each wheel to whichever power is desired */
    public void setWheelPower(double fl, double fr, double bl, double br) {

        /* Create power variables */
        double frontLeft;
        double frontRight;
        double backLeft;
        double backRight;

        /* Initialize the powers with the values input whenever this method is called */
        frontLeft   =   fl;
        frontRight  =   fr;
        backLeft    =   bl;
        backRight   =   br;

        /* set each wheel to the power indicated whenever this method is called */
        if (robot.FrontLeftPower != frontLeft) {
            robot.frontLeftMotor.setPower(fl);
            robot.FrontLeftPower = frontLeft;
        }
        if (robot.FrontRightPower != frontRight) {
            robot.frontRightMotor.setPower(fr);
            robot.FrontRightPower = frontRight;
        }
        if (robot.BackLeftPower != backLeft) {
            robot.backLeftMotor.setPower(bl);
            robot.BackLeftPower = backLeft;
        }
        if (robot.BackRightPower != backRight)
            robot.backRightMotor.setPower(br);
            robot.BackRightPower = backRight;
    }

    /**
     * Resets the cumulative angle tracking to zero.
     */
    private void resetAngle()
    {
        lastAngles = robot.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
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

        Orientation angles = robot.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        double deltaAngle = angles.firstAngle - lastAngles.firstAngle;

        if (deltaAngle < -180)
            deltaAngle += 360;
        else if (deltaAngle > 180)
            deltaAngle -= 360;

        globalAngle = deltaAngle;

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
        robot.frontLeftMotor.setPower(leftPower);
        robot.backLeftMotor.setPower(leftPower);

        robot.frontRightMotor.setPower(rightPower);
        robot.backRightMotor.setPower(rightPower);

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
        robot.frontLeftMotor.setPower(0);
        robot.backLeftMotor.setPower(0);
        robot.frontRightMotor.setPower(0);
        robot.backRightMotor.setPower(0);

        // wait for rotation to stop.
        sleep(1000);

        // reset angle tracking on new heading.
        resetAngle();
    }
}


