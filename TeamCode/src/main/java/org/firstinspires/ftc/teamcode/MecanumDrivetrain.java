package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
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
    private LinearOpMode linOp;

    public MecanumDrivetrain(Robot r) {
        robot = r;
        linOp = (LinearOpMode)robot.opMode;
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
                    lfDriveM.getCurrentPosition() < lTarget - 5 && linOp.opModeIsActive()){
                drive(0, -maxSpeed, 0);
                robot.opMode.telemetry.addData("1. rPosition", rfDriveM.getCurrentPosition());
                robot.opMode.telemetry.addData("2. rTarget", rTarget);
                robot.opMode.updateTelemetry(robot.opMode.telemetry);
            }
            super.stop();
        } else if (yDist < 0) {
            while (rfDriveM.getCurrentPosition() > rTarget + 5 &&
                    lfDriveM.getCurrentPosition() > lTarget + 5 && linOp.opModeIsActive()) {
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
        double kp = 0.002;

        // define and normalize target heading
        int targetHeading = wDist - (int)angles.firstAngle;
        targetHeading += targetHeading > 180 ? -360 : targetHeading < -180 ? 360 : 0;

        // define and normalize current heading
        int currentHeading = (int)angles.firstAngle;
        currentHeading  += currentHeading < -180 ? 360 : currentHeading > 180 ? -360 : 0;

        // initialize heading list
        int[] headingList = {currentHeading, currentHeading, currentHeading,
                currentHeading, currentHeading};

        int error = targetHeading - currentHeading;
        error += error > 180 ? -360 : error < -180 ? 360 : 0;

        double power;


        while(shouldKeepTurning2(headingList, targetHeading) && linOp.opModeIsActive()){
            updateIMU(); // update heading

            // update and normalize current heading
            currentHeading = (int)angles.firstAngle;
            currentHeading  += currentHeading < -180 ? 360 : currentHeading > 180 ? -360 : 0;

            error = targetHeading - currentHeading;
            error += error > 180 ? -360 : error < -180 ? 360 : 0;

            power = error*kp;

            power += error > 0 ? 0.15 : error < 0 ? -0.15 : 0;

            updateHeadingList(headingList, currentHeading);// update heading list

            robot.opMode.telemetry.addData("Angle: ", currentHeading);
            robot.opMode.telemetry.addData("Target:", targetHeading);
            robot.opMode.telemetry.update();

            drive(0,0, power);
        }
    }

    private void updateHeadingList(int[] headingList, int currentHeading){
        for (int i = 0; i < headingList.length-1; i++) {
            headingList[i] = headingList[i+1];
        }
        headingList[headingList.length-1] = currentHeading;
    }

    private boolean shouldKeepTurning2(int[] listOfHeadings, int targetHeading) {
        for(int heading : listOfHeadings) {
            if(heading != targetHeading) {
                return true;
            }
        }
        return false;
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