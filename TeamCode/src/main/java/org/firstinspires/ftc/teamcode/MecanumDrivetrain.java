package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;


class MecanumDrivetrain extends AbstractDrivetrain{

    private static final double WIDTH = 16;
    private static final double LENGTH = 5;
    private static final double MAX_SPEED = 1.0;

    private Robot robot;
    private ElapsedTime gyroTimer = new ElapsedTime();

    public MecanumDrivetrain(Robot r) {
        robot = r;
    }

    // Moves the drive train using the given x, y, and rotational velocities
    @Override
    public void drive(double xVelocity, double yVelocity, double wVelocity) {
        double speedScale = 1.0;

        double rfVelocity = yVelocity - xVelocity + wVelocity * (WIDTH / 2 + LENGTH / 2);
        double lfVelocity = yVelocity + xVelocity - wVelocity * (WIDTH / 2 + LENGTH / 2);
        double lbVelocity = yVelocity - xVelocity - wVelocity * (WIDTH / 2 + LENGTH / 2);
        double rbVelocity = yVelocity + xVelocity + wVelocity * (WIDTH / 2 + LENGTH / 2);

        double maxVelocity = findMax(lfVelocity, rfVelocity, lbVelocity, rbVelocity);

        if (maxVelocity > MAX_SPEED) {
            speedScale = MAX_SPEED / maxVelocity;
        }

        lfDriveM.setPower(lfVelocity * speedScale);
        rfDriveM.setPower(rfVelocity * speedScale);
        lbDriveM.setPower(lbVelocity * speedScale);
        rbDriveM.setPower(rbVelocity * speedScale);

    }

    @Override
    public void encoderDrive(double yDist, double maxSpeed) {
        int ENCODER_CPR = 1120; // Encoder counts per Revolution
        double gearRatio = 1.5; // [Gear Ratio]:1
        double circumference = 12.5; // Wheel circumference (in inches)
        double distanceToDrive = yDist / (circumference * gearRatio); // Number of rotations to drive
        double COUNTS = ENCODER_CPR * distanceToDrive; // Number of encoder counts to drive

        int rTarget = (rfDriveM.getCurrentPosition() + (int) COUNTS);
        int lTarget =(lfDriveM.getCurrentPosition() + (int) COUNTS);

        if (yDist > 0) {
            while (rfDriveM.getCurrentPosition() < rTarget - 5 &&
                    lfDriveM.getCurrentPosition() < lTarget - 5 ){
                drive(0, -maxSpeed, 0);
                robot.opMode.telemetry.addData("1. rPosition", rfDriveM.getCurrentPosition());
                robot.opMode.telemetry.addData("2. rTarget", rTarget);
                robot.opMode.updateTelemetry(robot.opMode.telemetry);
            }
            super.stop();
        } else if (yDist < 0) {
            while (rfDriveM.getCurrentPosition() > rTarget + 5 &&
                    lfDriveM.getCurrentPosition() > lTarget + 5) {
                drive(0, maxSpeed, 0);
                robot.opMode.telemetry.addData("1. rpPosition", rfDriveM.getCurrentPosition());
                robot.opMode.telemetry.addData("2. rTarget", rTarget);
                robot.opMode.updateTelemetry(robot.opMode.telemetry);
            }
            super.stop();
        }
    }

    @Override
    public void gyroTurn(int wDist, double maxSpeed){
        updateIMU();
        int targetHeading = wDist - (int)angles.firstAngle;

        targetHeading += targetHeading > 180 ? -360 : targetHeading < -180 ? 360 : 0;

        int[] headingList = {targetHeading - 2, targetHeading - 1, targetHeading,
                targetHeading + 1, targetHeading + 2};

        for(int i = 0; i < headingList.length; i++) {
            headingList[i] += headingList[i] < -180 ? 360 :
                    headingList[i] > 180 ? -360 : 0;
        }

        while(shouldKeepTurning2(headingList)){
            updateIMU();
            if(wDist < 0){
                maxSpeed = -1*maxSpeed;
            }
            drive(0,0, maxSpeed);
        }
    }


    private boolean shouldKeepTurning2(int[] listOfHeadings) {
        for(int heading : listOfHeadings) {
            if(heading == (int)-angles.firstAngle) {
                return false;
            }
        }

        return true;
    }

    private void updateIMU() {
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
    }

    // Helper function for power scaling in the drive method
    private double findMax(double... vals) {
        double max = Double.NEGATIVE_INFINITY;

        for (double d : vals) {
            if (d > max) max = d;
        }

        return max;
    }
}