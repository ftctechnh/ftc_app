package org.firstinspires.ftc.teamcode;

import android.annotation.TargetApi;
import android.os.Build;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.ArrayList;

public class DriveEngine {
    DcMotor back;
    DcMotor right;
    DcMotor left;

    static double wheelDiameter = 5;
    static double robotRadius = 8;

    private static final double ticksPerRev = 1120;
    private static double inPerRev = Math.PI * wheelDiameter;
    static final double inPerTicks = inPerRev /ticksPerRev;

    double theta;
    double xOut, yOut;
    Telemetry telemetry;

    DriveEngine(HardwareMap hardwareMap, Telemetry telemetry) {
        this.telemetry = telemetry;

        back  = hardwareMap.dcMotor.get("back");
        right = hardwareMap.dcMotor.get("right");
        left  = hardwareMap.dcMotor.get("left");
        theta = 0;

        back.setMode (DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        left.setMode (DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        back.setMode (DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        right.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        left.setMode (DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        back.setDirection (DcMotor.Direction.FORWARD);
        right.setDirection(DcMotor.Direction.FORWARD);
        left.setDirection (DcMotor.Direction.FORWARD);

    }

    void drive(double x, double y) {
        double xprime = x * Math.cos(theta) - y * Math.sin(theta);
        double yprime = x * Math.sin(theta) + y * Math.cos(theta);

        xOut = x = xprime;
        yOut = y = yprime;

        telemetry.addData("driveE x", xOut);
        telemetry.addData("driveE y", yOut);

        back.setPower(x);
        right.setPower( (-x/2) + ( (y*Math.sqrt(3)) / 2 ) );
        left.setPower ( (-x/2) + ( (-y*Math.sqrt(3)) / 2 ) );
    }


    void drive(double x, double y, boolean op) {
        double xprime = x * Math.cos(theta) - y * Math.sin(theta);
        double yprime = x * Math.sin(theta) + y * Math.cos(theta);

        x = xprime;
        y = yprime;

        double backPower = x;
        double rightPower = (-x/2) + y * (Math.sqrt(3)/2) ;
        double leftPower  = (-x/2) - y * (Math.sqrt(3)/2) ;

        if(op && Math.sqrt(x*x + y*y) > .90)
        {
            double max = Math.max(Math.max(backPower,rightPower),leftPower);
            backPower  *= 1 / max;
            rightPower *= 1 / max;
            leftPower  *= 1 / max;
        }

        xOut = backPower;
        yOut = (rightPower - leftPower)/2;

        telemetry.addData("driveE x", xOut);
        telemetry.addData("driveE y", yOut);

        back.setPower(backPower);
        right.setPower(rightPower);
        left.setPower (leftPower);
    }

    void driveAtAngle(double theta)
    {
        this.theta = theta;
    }

    void rotate(double spin) {
        telemetry.addData("driveE rotate", spin);
        back.setPower(spin);
        right.setPower(spin);
        left.setPower(spin);
    }

    void driveCurvy(double x, double y, double spin)
    {
        telemetry.addData("driveE x", xOut);
        telemetry.addData("driveE y", yOut);
        telemetry.addData("driveE rotate", spin);
        double root3 = Math.sqrt(3);
        back.setPower ( (x                + spin) /2);
        right.setPower( (-x/2 + y*root3/2 + spin) /2);
        left.setPower ( (-x/2 - y*root3/2 + spin) /2);
    }

    void resetDistances()
    {
        back.setMode (DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        left.setMode (DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        back.setMode (DcMotor.RunMode.RUN_USING_ENCODER);
        right.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        left.setMode (DcMotor.RunMode.RUN_USING_ENCODER);
    }

    private ArrayList<Boolean> checkpoint = new ArrayList<>();
    boolean moveOnPath(String commandKey, double[] ... args)
    {
        addCommands(args.length, commandKey);

        int c = 0;
        for(boolean b: checkpoint)
        {
            if(b == true)
                c++;
        }
        telemetry.addData("checkpoint count", c);

        if(c == checkpoint.size())
            return true;
        switch (args[c].length)
        {
            case 1:
                double targetAngle = args[c][0];
                double currentAngle = spinAngle();
                telemetry.addData("current angle", spinAngle());

                if(Math.abs(currentAngle) < Math.abs(targetAngle)) {
                    rotate(Math.signum(targetAngle) * .15);
                }
                else{
                    checkpoint.set(c, true);
                    resetDistances();
                }
                break;
            case 2:
                double[] point = args[c];
                double x = xDist();
                double y = yDist();
                double deltaX = point[0] - x;
                double deltaY = point[1] - y;
                double r = Math.hypot(deltaX, deltaY);
                telemetry.addData("targetX", point[0]);
                telemetry.addData("targetY", point[1]);
                telemetry.addData("radius", r);

                if(r > 4) {
                    drive(deltaX / r * .2, deltaY / r * .2);
                }
                else if(r > .5) {
                    drive(deltaX / r * .1, deltaY / r * .1);
                }
                else if(r <= .5){
                    checkpoint.set(c, true);
                    resetDistances();
                }
                break;
        }
        return false;
    }

    private ArrayList<String> keyList;
    private void addCommands(int n, String commandKey)
    {
        if(keyList.contains(commandKey))
            return;
        keyList.add(commandKey);
        for (int i = 0; i < n; i++)
            checkpoint.add(false);
        resetDistances();
    }

    double xDist()
    {
        return Math.abs(back.getCurrentPosition() * DriveEngine.inPerTicks);
    }

    double yDist()
    {
        return Math.abs(right.getCurrentPosition() - left.getCurrentPosition()) /2 * DriveEngine.inPerTicks;
    }

    /**
     *
     * @return angle in radians
     */
    double spinAngle()
    {
        double averagePosition = (back.getCurrentPosition() + left.getCurrentPosition() + right.getCurrentPosition()) / 3;
        return Math.abs(averagePosition * DriveEngine.inPerTicks) /robotRadius; //TODO: Find radius
    }
}