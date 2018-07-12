package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;


class MecanumDrivetrain extends AbstractDrivetrain{

    private static final double WIDTH = 16;
    private static final double LENGTH = 5;
    private static final double MAX_SPEED = 1.0;

    private Robot robot;

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
    public void encoderDrive(double xDist, double yDist, double wDist, double maxSpeed) {
        // TODO: Implement
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



    // Helper function for power scaling in the drive method
    private double findMax(double... vals) {
        double max = Double.NEGATIVE_INFINITY;

        for (double d : vals) {
            if (d > max) max = d;
        }

        return max;
    }
}