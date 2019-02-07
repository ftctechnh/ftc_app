package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

class EndEffector {
    static final double grabberLength = 10;
    private static final double ticksPerRev = 1120;

    static double smallerWheelDiameter = .5;
    private static double smallerInPerRev = Math.PI * smallerWheelDiameter;
    static final double smallerInPerTicks = smallerInPerRev / ticksPerRev;
    static final double smallerTicksPerIn = ticksPerRev / smallerInPerRev;

    static double largerWheelDiameter = .75;
    private static double largerInPerRev = Math.PI * largerWheelDiameter;
    static final double largerInPerTicks = largerInPerRev / ticksPerRev;
    static final double largerTicksPerIn = ticksPerRev / largerInPerRev;

    private static double degreesPerRev = 360;
    static final double radiansPerTicks = 2 * Math.PI / ticksPerRev;
    static final double ticksPerRadian = 1 / radiansPerTicks;
    double AxleBoltX = 2.5;
    double AxleBoltZ = 8;
    double AxleBoltY = 5;
    double boltLength = 11.5;

    DcMotor pivot;
    DcMotor contract;
    Servo pinch;
    Servo swing;
    Sensors sensors;
    Telemetry telemetry;

    double initialStringLength = 5;
    double initialArmLength = 14;
    double armLength = initialArmLength;

    EndEffector(HardwareMap hardwareMap, Telemetry telemetry, Sensors sensors) {
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
        contract.setDirection(DcMotorSimple.Direction.REVERSE);
        contract.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        contract.setPower(1);
    }

    void open() {
        pinch.setPosition(.44);
    }

    void close() {
        pinch.setPosition(.4);
    }

    boolean extend(double inchesPerSecond) {
        armLength += inchesPerSecond * Bogg.averageClockTime;
        moveToLength(armLength);
        return inchesPerSecond == 0;
    }

    void moveToLength(double targetLength) {
        armLength = Math.min(targetLength, 28);
        contract.setTargetPosition((int) Math.round((targetLength - initialArmLength) * smallerInPerTicks));
    }

    void moveToPosition(double x, double z) {
        double angle = Math.atan2(z, x);
        //subtract grabber
//        z += grabberLength * Math.cos(angle);
//        x += Math.signum(x) * grabberLength * Math.sin(angle);
        //Simplify for now

        armLength = Math.hypot(x, z);
        angle = Math.atan2(z, x);

        moveToLength(armLength);
        pivot(angle);
    }

    void pivot(double angle) {
        double stringLength = .707107 * Math.sqrt(-368 * (-.625 * Math.cos(angle) + Math.sin(angle) - 1.23641));
        pivot.setTargetPosition((int) Math.round((stringLength - initialStringLength) * largerTicksPerIn));
    }

    void flipUp(double t) {
        close();
        if (t > 1) {
            double x = MyMath.closestToZero(sensors.getHighDistance() + 3, 30);
            double z = 33;
            moveToPosition(x, z);
            pickleUp();
        }
    }

    void flipDown(double t) {
        open();
        if (t > .5) {
            moveToPosition(armLength, 0);
        }
        if (t > 1)
            pickleDown();
    }

    void pickleUp() {
        swing.setPosition(.25);
    }

    void pickleDown() {
        swing.setPosition(.65);
    }

    
    double getRadius()
    {
        return Math.cos(getAngle()) * armLength;
    }

    double getAngle()
    {
        double stringLength = (initialStringLength + pivot.getCurrentPosition()) * largerInPerTicks;

        double s = Math.pow(stringLength, 2);
        double f = Math.pow(stringLength, 4);

        double w = Math.sqrt(-4 * (f - 455 * s + 4675.25));
        double numerator = .625 * (w - 3.2 *(s - 227.5));
        double denominator = w + 1.25 * (s - 227.5);
        return Math.atan2(numerator, denominator);
    }
}
