package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

class EndEffector {
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
    Telemetry telemetry;

    double length;
    int pivotTicks = 0;

    EndEffector(HardwareMap hardwareMap, Telemetry telemetry)
    {
//        spin = hardwareMap.dcMotor.get("spin");
//        spin.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        spin.setDirection(DcMotorSimple.Direction.FORWARD);
//        spin.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        balls = hardwareMap.servo.get("balls");
//        blocks = hardwareMap.servo.get("blocks");
        pivot = hardwareMap.dcMotor.get("pivot");
        pivot.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        pivot.setDirection(DcMotorSimple.Direction.FORWARD);
        pivot.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        contract = hardwareMap.dcMotor.get("contract");
        contract.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        contract.setDirection(DcMotorSimple.Direction.FORWARD);
        contract.setMode(DcMotor.RunMode.RUN_TO_POSITION);
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

    boolean isUp()
    {
        pivot.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        return pivot.getCurrentPosition() > pivotTicks - 100; //open before we reach the lander

    }
    boolean isDown()
    {
        pivot.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        pivot.setTargetPosition(0);
        return pivot.getCurrentPosition() < 100;
    }

    void spin(double power)
    {
        //this.spin.setPower(power);
    }

    void extend(double l)
    {
        length += l;
        contract.setTargetPosition((int)Math.round(length * inPerTicks));
    }

    void moveToLength(double length)
    {
        this.length = length;
        contract.setTargetPosition((int)Math.round(length * inPerTicks));
    }

    void moveToPosition(double x, double z)
    {
        moveToLength(Math.hypot(x,z));
        double radians = Math.atan2(z,x);
        pivotTicks = (int) (radians * ticksPerRadian * gearRatio);
        pivot.setTargetPosition(pivotTicks);
    }

    void pivot(int position)
    {
        pivot.setTargetPosition((int)(position * ticksPerDegree * gearRatio));
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
