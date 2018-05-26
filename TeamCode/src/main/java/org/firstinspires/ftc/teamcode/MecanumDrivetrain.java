package org.firstinspires.ftc.teamcode;

class MecanumDrivetrain extends AbstractDrivetrain {

    private static final double WIDTH = 8;
    private static final double LENGTH = 8.5;
    private static final double MAX_SPEED = 1.0;

    public MecanumDrivetrain(){
    }

    // Moves the drive train using the given x, y, and rotational velocities
    public void drive(double xVelocity, double yVelocity, double wVelocity){
        double speedScale = 1.0;

        double lfVelocity = yVelocity - xVelocity + wVelocity*(WIDTH+LENGTH);
        double rfVelocity = yVelocity + xVelocity - wVelocity*(WIDTH+LENGTH);
        double lbVelocity = yVelocity - xVelocity - wVelocity*(WIDTH+LENGTH);
        double rbVelocity = yVelocity + xVelocity + wVelocity*(WIDTH+LENGTH);

        double maxVelocity = findMax(lfVelocity,rfVelocity, lbVelocity, rbVelocity);

        if(maxVelocity > MAX_SPEED){
            speedScale = MAX_SPEED/maxVelocity;
        }

        lfDriveM.setPower(lfVelocity*speedScale);
        rfDriveM.setPower(rfVelocity*speedScale);
        lbDriveM.setPower(lbVelocity*speedScale);
        rbDriveM.setPower(rbVelocity*speedScale);

    }

    public void encoderDrive(double xDist, double yDist, double wDist, double maxSpeed){
        // TODO: Implement
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