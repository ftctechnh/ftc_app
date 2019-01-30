package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

class EndEffector {
    static final double grabberLength = 7;
    static double wheelDiameter = 2;
    private static final double ticksPerRev = 1120;
    private static double inPerRev = Math.PI * wheelDiameter;
    static final double inPerTicks = inPerRev / ticksPerRev;
    static final double ticksPerIn = ticksPerRev / inPerRev;
    private static double degreesPerRev = 360;
    static final double degreesPerTicks = degreesPerRev / ticksPerRev;
    static final double ticksPerDegree = 1 / degreesPerTicks;
    static final double radiansPerTicks = 2*Math.PI / ticksPerRev;
    static final double ticksPerRadian = 1 / radiansPerTicks;
    static final double gearRatio = 125/15;

    DcMotor pivot;
    DcMotor contract;
    Servo pinch;
    Servo swing;
    Sensors sensors;
    Telemetry telemetry;

    double initialPivotAngle = 0;

    double length;

    EndEffector(HardwareMap hardwareMap, Telemetry telemetry, Sensors sensors)
    {
        pinch = hardwareMap.servo.get("pinch");
        swing = hardwareMap.servo.get("swing");
        this.sensors = sensors;
        this.telemetry = telemetry;
        pivot = hardwareMap.dcMotor.get("pivot");
        pivot.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        pivot.setDirection(DcMotorSimple.Direction.FORWARD);
        pivot.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        pivot.setPower(1);

        contract = hardwareMap.dcMotor.get("contract");
        contract.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        contract.setDirection(DcMotorSimple.Direction.FORWARD);
        contract.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        contract.setPower(1);
    }

    void open()
    {
        pinch.setPosition(.1);
    }
    void close()
    {
        pinch.setPosition(-.1);
    }

    boolean extend(double inchesPerSecond)
    {
        length += inchesPerSecond * Bogg.averageClockTime;
        contract.setTargetPosition((int)Math.round(length * inPerTicks));
        return  inchesPerSecond == 0;
    }

    void moveToLength(double length)
    {
        this.length = length;
        contract.setTargetPosition((int)Math.round(length * inPerTicks));
    }

    void moveToPosition(double x, double z)
    {
        double angle = Math.atan2(z,x);
        //subtract grabber
        z += grabberLength * Math.cos(angle);
        x += Math.signum(x) * grabberLength * Math.sin(angle);

        length = Math.hypot(x,z);
        angle = Math.atan2(z,x);

        moveToLength(length);
        pivot(angle);
    }

    void pivot(double angle)
    {
        pivot.setTargetPosition((int)(angle * ticksPerRadian * gearRatio));
    }

    void flipUp(double t)
    {
        close();
        if(t > .5)
        {
            double x = MyMath.closestToZero(sensors.getHighDistance() + 3, 48);
            double z = 33;
            moveToPosition(x, z);
            pickleUp();
        }
    }

    void flipDown(double t)
    {
        open();
        if(t > .5)
        {
            moveToPosition(-length, 0);
        }
        if(t > 1)
            pickleDown();
    }

    void pickleUp()
    {
        swing.setPosition(.5);
    }

    void pickleDown()
    {
        swing.setPosition(-.5);
    }

    
    double getRadius()
    {
        return Math.cos(getAngle()) * length;
    }

    double getAngle()
    {
        return pivot.getCurrentPosition() * radiansPerTicks / gearRatio - initialPivotAngle;
    }

    double getAngleInDegrees()
    {
        return pivot.getCurrentPosition() * degreesPerTicks / gearRatio;
    }
}
