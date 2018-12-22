package org.firstinspires.ftc.teamcode.core;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;


public class Functions {
    Orientation lastAngles = new Orientation();
    double globalAngle, power = .30, correction;
    Hardware Hw = new Hardware();

    //Move function using INCHES and POWER
    public void move(int distance, double power) {

        int target = distance * (int) (288 / (4 * Math.PI));
        Hw.backLeftDrive.setTargetPosition(target);
        Hw.backRightDrive.setTargetPosition(target);
        Hw.frontLeftDrive.setTargetPosition(target);
        Hw.frontRightDrive.setTargetPosition(target);

        Hw.backLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Hw.backRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Hw.frontLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Hw.frontRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        Hw.backLeftDrive.setPower(power);
        Hw.backRightDrive.setPower(power);
        Hw.frontLeftDrive.setPower(power);
        Hw.frontRightDrive.setPower(power);

        while (Hw.backLeftDrive.isBusy() && Hw.backRightDrive.isBusy()) {
            Hw.telemetry.addData("BackLeft", Hw.backLeftDrive.isBusy());
        }
        Hw.backLeftDrive.setPower(0);
        Hw.backRightDrive.setPower(0);
        Hw.frontLeftDrive.setPower(0);
        Hw.frontRightDrive.setPower(0);
    }

    public float getZAxis(){
        return Hw.gyro.getAngularOrientation(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).thirdAngle;
    }


    private void resetAngle()
    {
        lastAngles = Hw.gyro.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        globalAngle = 0;
    }

    private double getAngle()
    {

        Orientation angles = Hw.gyro.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

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
            correction = 0;             // no adjustment.
        else
            correction = -angle;        // reverse sign of angle for correction.

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

        Hw.backLeftDrive.setPower(leftPower);
        Hw.backRightDrive.setPower(rightPower);
        Hw.frontLeftDrive.setPower(leftPower);
        Hw.frontRightDrive.setPower(rightPower);

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
        Hw.backLeftDrive.setPower(0);
        Hw.backRightDrive.setPower(0);
        Hw.frontLeftDrive.setPower(0);
        Hw.frontRightDrive.setPower(0);

        // wait for rotation to stop.
        sleep(1000);

        // reset angle tracking on new heading.
        resetAngle();
    }
}


    public void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException ignore) {
        }
    }

    //PlaceMarker function that places the marker
    public void PlaceMarker() {
        Hw.markerServo.setPosition(-1);
        Hw.markerServo.setPosition(1);
    }

}