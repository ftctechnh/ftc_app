package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class OmniWheelDriveEngine extends DriveEngine {

    static double effectiveRobotRadius = 7.15;

    private double motorSpacing;

    static double ticksPerRev = 1120;
    static double inPerRev(){return Math.PI * effectiveWheelDiameter;}
    static double inPerTicks(){return inPerRev() / ticksPerRev;}

    OmniWheelDriveEngine() {}

    OmniWheelDriveEngine(HardwareMap hardwareMap, Telemetry telemetry, Sensors sensors, int numMotors)
    {
        super(telemetry, sensors);

        switch (numMotors) //order matters, we go counterclockwise
        {
            case 4:
                motors.add(hardwareMap.dcMotor.get("back"));
                motors.add(hardwareMap.dcMotor.get("right"));
                motors.add(hardwareMap.dcMotor.get("front"));
                motors.add(hardwareMap.dcMotor.get("left"));
                motorSpacing = Math.PI / 2;
                break;
            case 3:
                motors.add(hardwareMap.dcMotor.get("back"));
                motors.add(hardwareMap.dcMotor.get("right"));
                motors.add(hardwareMap.dcMotor.get("left"));
                motorSpacing = Math.PI * 2/3;
                break;
            case 2:
                motors.add(hardwareMap.dcMotor.get("right"));
                motors.add(hardwareMap.dcMotor.get("left"));
                motorSpacing = Math.PI;
                break;
            default:
                //starts at zero, motor0 is on x-axis.
                for (int i = 0; i < numMotors; i++) {
                    motors.add(hardwareMap.dcMotor.get("motor" + i));
                }
        }

        for(DcMotor motor: motors)
        {
            //We reset the encoders to 0
            motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            //We tell the motors to brake, not spin or float. They resist movement with friction.
            motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            //The drive engine motors should always move forward. Always!
            motor.setDirection(DcMotorSimple.Direction.FORWARD);
            //We run using velocity, not power.
            //The motors do this by checking the speed using change in position over time.
            //They use PID control to keep the speed constant.
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }


    @Override
    double[] processMotorPowersFromDriveValues(double[] driveValues){
        boolean op = driveValues[0] == 1;
        double x = driveValues[1];
        double y = driveValues[2];
        double spin = driveValues[3];

        double[] powers = new double[motors.size()];

        for (int i = 0; i < motors.size(); i++)  //Calculate motor powers
            powers[i] = x * Math.cos(i * motorSpacing)
                    +   y * Math.sin(i * motorSpacing)
                    +   spin;


        double max = MyMath.absoluteMax(powers);

        if(max > 1 || (op && Math.hypot(x,y) > .90))
            for (int i = 0; i < powers.length; i++)   // Adjust all motors to less than one
                powers[i] /= max;            // Or maximize a motor to one

        return powers;
    }


//    X = B—L/2—R/2
//    Y = R*√3/2—L*√3/2   These are the force and torque equations
//    S = B+R+L

//    B = X*2/3 + S/3
//    R = Y*√3/3 – X/3 + S/3  These are solved for the motor powers
//    L = -Y*√3/3 – X/3 + S/3



    // Say that the robot moves a distance of 1 in the direction of x.
    // Find the distance each motor moved, call it d.
    // Assign this as a coefficient to each motor.
    // Create a constant equal to the sum of the terms,
    // multiplying each coefficient by the distance each motor moved
    // Call this constant k.
    // Divide the equation by k to give 1 when x = 1.

//    B = X + S
//    R = Y*√3/2 – X/2 + S  These are the motor distances
//    L = -Y*√3/2 – X/2 + S

    //    X = (B—R/2—L/2) * 2/3
//    Y = (R*√3/2—L*√3/2) * 2/3  These are solved for the directional distances
//    S = (B+R+L) /3


    double xDist()
    {
        double smallSum = 0;
        for (int i = 0; i < motors.size(); i++) {
            smallSum += Math.abs(Math.cos(i * motorSpacing));
        }

        double sum = 0;
        for (int i = 0; i < motors.size(); i++) {
            sum += motors.get(i).getCurrentPosition() * Math.cos(i * motorSpacing);
        }

        double xDistance = sum / smallSum * inPerTicks();
        telemetry.addData("xDist", xDistance);

        return xDistance;
    }

    double yDist()
    {
        double smallSum = 0;
        for (int i = 0; i < motors.size(); i++) {
            smallSum += Math.abs(Math.sin(i * motorSpacing));
        }

        double sum = 0;
        for (int i = 0; i < motors.size(); i++) {
            sum += motors.get(i).getCurrentPosition() * Math.sin(i * motorSpacing);
        }

        double yDistance = sum / smallSum * inPerTicks();
        telemetry.addData("yDist", yDistance);


        return yDistance;
    }

    void orbit(double radius, double angle, double speed)
    {
        telemetry.addData("orbit radius", radius);
        radius /= effectiveRobotRadius;

        double[] powers = new double[motors.size()];

        for (int i = 0; i < powers.length; i++) {
            powers[i] = 1 + radius * Math.sin(angle + i * motorSpacing);
        }

        double max = MyMath.absoluteMax(powers);

        for (int i = 0; i < motors.size(); i++) {
            motors.get(i).setPower(powers[i] * speed / max);
        }
    }
}
