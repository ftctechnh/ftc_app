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
    private static double degreesPerRev = 360;
    static final double degreesPerTicks = degreesPerRev / ticksPerRev;
    static final double ticksPerDegree = 1 / degreesPerTicks;
    static final double radiansPerTicks = 2*Math.PI / ticksPerRev;
    static final double ticksPerRadian = 1 / radiansPerTicks;
    static final double gearRatio = 125/15;

    DcMotor pivot;
    DcMotor contract;
    DcMotor spin;
    Servo balls;
    Servo blocks;
    Sensors sensors;
    Telemetry telemetry;

    double length;
    int pivotTicks = 0;

    EndEffector(HardwareMap hardwareMap, Telemetry telemetry, Sensors sensors)
    {
//        spin = hardwareMap.dcMotor.get("spin");
//        spin.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        spin.setDirection(DcMotorSimple.Direction.FORWARD);
//        spin.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        balls = hardwareMap.servo.get("balls");
//        blocks = hardwareMap.servo.get("blocks");
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
//        balls.setPosition(.5);
//        blocks.setPosition(.5);
    }
    void close()
    {
//        balls.setPosition(0);
//        blocks.setPosition(0);
    }

    void extend(double inchesPerSecond)
    {
        length += inchesPerSecond * Bogg.averageClockTime;
        contract.setTargetPosition((int)Math.round(length * inPerTicks));
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
        z += Math.abs(grabberLength * Math.cos(angle));
        x -= Math.abs(grabberLength * Math.sin(angle));

        length = Math.hypot(x,z);
        angle = Math.atan2(z,x);

        moveToLength(length);
        pivotTicks = (int) (angle * ticksPerRadian * gearRatio);
        pivot.setTargetPosition(pivotTicks);
    }

    void pivot(int position)
    {
        pivot.setTargetPosition((int)(position * ticksPerDegree * gearRatio));
    }

    void flipUp(double t)
    {
        close();
        if(t > .5)
        {
            double x = Math.min(sensors.getHighDistance() + 3, 48);
            double z = 33;
            moveToPosition(x, z);
            pickleUp();
        }
    }

    void flipDown(double t)
    {
        if(t > .5)
        {
            open();
            moveToPosition(length, 0);
        }
        if(t > 1)
            pickleDown();
    }

    void pickleUp()
    {

    }

    void pickleDown()
    {

    }

    
    double getRadius()
    {
        return Math.cos(getAngle()) * length;
    }

    double getAngle()
    {
        return pivot.getCurrentPosition() * radiansPerTicks / gearRatio;
    }

    double getAngleInDegrees()
    {
        return pivot.getCurrentPosition() * degreesPerTicks / gearRatio;
    }
}
