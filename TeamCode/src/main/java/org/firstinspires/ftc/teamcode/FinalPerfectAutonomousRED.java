/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

/**
 ☺ Hi! This is the perfect teleop code for December 16, 2017! ☺
 */
@Autonomous(name = "♪ ♥ Perfect Autonomous with Color ♥  ♪", group = "Concept")
//@Disabled
public class FinalPerfectAutonomousRED extends LinearOpMode {

    /* this says use ArmHardwareClass */
    MasterHardwareClassRIGHTNOW robot = new MasterHardwareClassRIGHTNOW();

    /* Create a "timer" that begins once the OpMode begins */
    private ElapsedTime runtime = new ElapsedTime();

    /* Variables for gyro */
    Orientation lastAngles = new Orientation();
    double globalAngle, power = .30, correction;

    @Override
    public void runOpMode() {
        robot.init(hardwareMap);

        // hsvValues is an array that will hold the hue, saturation, and value information.
        // values is a reference to the hsvValues array.

        // sometimes it helps to multiply the raw RGB values with a scale factor
        // to amplify/attentuate the measured values.
        final double SCALE_FACTOR = 255;
        float hsvValues[] = {0F, 0F, 0F};
        // values is a reference to the hsvValues array.
        final float values[] = hsvValues;
        int relativeLayoutId = hardwareMap.appContext.getResources().getIdentifier("RelativeLayout", "id", hardwareMap.appContext.getPackageName());
        final View relativeLayout = ((Activity) hardwareMap.appContext).findViewById(relativeLayoutId);

        /* Parameters for the gyro */
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();

        parameters.mode                = BNO055IMU.SensorMode.IMU;
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled      = false;


        /* The init() method of the hardware class does all the work here*/
        robot.init(hardwareMap);


        robot.imu.initialize(parameters);

        telemetry.addData("Mode", "calibrating...");
        telemetry.update();


        // make sure the imu gyro is calibrated before continuing.
        while (!isStopRequested() && !robot.imu.isGyroCalibrated())
        {
            sleep(50);
            idle();
        }

        telemetry.addData("Mode", "waiting for start");
        telemetry.addData("imu calib status", robot.imu.getCalibrationStatus().toString());
        telemetry.update();

        // wait for start button.

        waitForStart();

        telemetry.addData("Mode", "running");
        telemetry.update();

        sleep(1000);

        // drive until end of period.


        // Wait for the start button
        telemetry.addLine("!☻ Ready to Drive Autonomous ☻!");
        telemetry.update();
        waitForStart();

        while (opModeIsActive()) {

            // Use gyro to drive in a straight line.
            correction = checkDirection();

            robot.gemServo.setPosition(robot.xPosDown);
            // send the info back to driver station using telemetry function.
//            telemetry.addData("RedR  ", robot.sensorColorRight.red());
//            // change the background color to match the color detected by the RGB sensor.
//            // pass a reference to the hue, saturation, and value array as an argument
//            // to the HSVToColor method.
//            relativeLayout.post(new Runnable() {
//                public void run() {
//                    relativeLayout.setBackgroundColor(Color.HSVToColor(0xff, values));
//                }
//            });
//
//            telemetry.update();
//
//            /*xPos  =  gamepad1.right_stick_x;
//            robot.gemServo.setPosition(xPos);*/
//            // send the info back to driver station using telemetry function.
//            telemetry.addData("RedL  ", robot.sensorColorRight.red());
//
//            // change the background color to match the color detected by the RGB sensor.
//            // pass a reference to the hue, saturation, and value array as an argument
//            // to the HSVToColor method.
//            relativeLayout.post(new Runnable() {
//                public void run() {
//                    relativeLayout.setBackgroundColor(Color.HSVToColor(0xff, values));
//                }
//            });
//            relativeLayout.post(new Runnable() {
//                public void run() {
//                    relativeLayout.setBackgroundColor(Color.WHITE);
//                }
//            });

            findColor();
            telemetry.addData("1 imu heading", lastAngles.firstAngle);
            telemetry.addData("2 global heading", globalAngle);
            telemetry.addData("3 correction", correction);
            telemetry.update();


        }
    }

    /***********************************************************************************************
     * These are all of the methods used in the Autonomous *
     ***********************************************************************************************/


/* This method moves the robot forward for time and power indicated*/
    public void movebytime (double time, double power, String direction) {
    /* reset the "timer" to 0 */
        runtime.reset();
    /* This runs the wheel power so it moves forward, the powers for the left wheels
    are inversed so that it runs properly on the robot
     */

        switch (direction) {
            case "Forward":
                setWheelPower(-power, power, -power, power);
                break;
            case "Backward":
                setWheelPower(power, power, -power, -power);
                break;
            case "Right":
                setWheelPower(-power, -power, power, power);
                break;
            case "Left":
                setWheelPower(power, power, -power, -power);
                break;
        }
    /* If the timer hasn't reached the time that is indicated do nothing and keep the wheels powered */
        while (opModeIsActive() && runtime.seconds() < time) {

        }
    /* Once the while loop above finishes turn off the wheels */
        wheelsOff();
    }


    /* This method simply sets all motor to zero power*/
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
    //eyy
    public void findColor()
    {
        if ((robot.sensorColorRight.red()) < robot.sensorColorRight.blue())
        {
            rotate(90,.5);
            wheelsOff();
        }
        else
            rotate(-90,.5);
            wheelsOff();
    }
}