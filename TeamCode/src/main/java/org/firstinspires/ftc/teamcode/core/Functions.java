package org.firstinspires.ftc.teamcode.core;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import com.qualcomm.robotcore.hardware.HardwareMap;


public class Functions extends Hardware {
    Orientation lastAngles = new Orientation();
    double globalAngle, power = .30, correction;
    private LinearOpMode opmode;


    public Functions(Telemetry telemetry, HardwareMap hardwareMap, LinearOpMode linearOpMode)
    {
        super(telemetry, hardwareMap, linearOpMode);

        opmode = linearOpMode;
    }



    //Move function using INCHES and POWER
    public void move(int distance, double power) {

        int target = distance * (int) (288 / (4 * Math.PI));
        backLeftDrive.setTargetPosition(target);
        backRightDrive.setTargetPosition(target);
        frontLeftDrive.setTargetPosition(target);
        frontRightDrive.setTargetPosition(target);

        backLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        backLeftDrive.setPower(power);
        backRightDrive.setPower(power);
        frontLeftDrive.setPower(power);
        frontRightDrive.setPower(power);

        while (backLeftDrive.isBusy() && backRightDrive.isBusy()) {
            telemetry.addData("BackLeft", backLeftDrive.isBusy());
        }
        backLeftDrive.setPower(0);
        backRightDrive.setPower(0);
        frontLeftDrive.setPower(0);
        frontRightDrive.setPower(0);
    }

    public float getZAxis(){
        return gyro.getAngularOrientation(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).thirdAngle;
    }


    private void resetAngle()
    {
        lastAngles = gyro.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        globalAngle = 0;
    }

    private double getAngle()
    {

        Orientation angles = gyro.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        double deltaAngle = angles.firstAngle - lastAngles.firstAngle;

        if (deltaAngle < -180)
            deltaAngle += 360;
        else if (deltaAngle > 180)
            deltaAngle -= 360;

        globalAngle += deltaAngle;

        lastAngles = angles;

        return globalAngle;
    }

    private double checkDirection()
    {
        double correction, angle, gain = .10;

        angle = getAngle();

        if (angle == 0)
            correction = 0; // no adjustment.
        else
            correction = -angle; // reverse sign of angle for correction.

        correction = correction * gain;

        return correction;
    }

    //Turn function using DEGREES and POWER
    private void turn(int degrees, double power)
    {
        double  leftPower, rightPower;

        resetAngle();

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

        backLeftDrive.setPower(leftPower);
        backRightDrive.setPower(rightPower);
        frontLeftDrive.setPower(leftPower);
        frontRightDrive.setPower(rightPower);

        // rotate until turn is completed.
        if (degrees < 0)
        {
            // On right turn we have to get off zero first.
            while (opmode.opModeIsActive() && getAngle() == 0) {}

            while (opmode.opModeIsActive() && getAngle() > degrees) {}
        }
        else    // left turn.
            while (opmode.opModeIsActive() && getAngle() < degrees) {}

        // turn the motors off.
        backLeftDrive.setPower(0);
        backRightDrive.setPower(0);
        frontLeftDrive.setPower(0);
        frontRightDrive.setPower(0);

        // wait for rotation to stop.
        sleep(1000);

        // reset angle tracking on new heading.
        resetAngle();
    }


    public void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException ignore) {
        }
    }

    //PlaceMarker function that places the marker
    public void PlaceMarker() {
        markerServo.setPosition(-1);
        markerServo.setPosition(1);
    }

}