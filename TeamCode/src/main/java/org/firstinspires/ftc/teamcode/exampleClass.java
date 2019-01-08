package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

class exampleClass {
    /**
     * These are variables.
     * The ones called "double" are numbers.
     * Single precision is 32 bits, double precision is 64 bits
     * In java, single precision numbers are called floats.
     *
     * Variables can be given initial values up here, but mostly we just give any
     * example object that is of the "exampleClass," that variable.
     * We can refer to that variable and get its value below.
     */
    static final double inPerTicks = 2 * Math.PI / 1120;
    private static double degreesPerRev = 360;
    static final double degreesPerTicks = degreesPerRev / 1120;
    static final double radiansPerTicks = 2*Math.PI / 1120;

    /**
     * Java is object oriented, so the variables can represent things, not just normal x and y variables.
     */
    DcMotor pivot;
    DcMotor contract;
    DcMotor spin;
    Servo balls;
    Servo blocks;

    double length;

    exampleClass(HardwareMap hardwareMap)
    {
        /**
         * The hardware map is set on the phones.
         * The phone looks at preset ports and finds the motors and servos.
         */
        spin = hardwareMap.dcMotor.get("spin");
        balls = hardwareMap.servo.get("balls");
        blocks = hardwareMap.servo.get("blocks");
        pivot = hardwareMap.dcMotor.get("pivot");
        contract = hardwareMap.dcMotor.get("contract");

        /**
         * We give the motors commands to reset their encoders,
         * set the direction, and tell them to either run at a speed or to a position.
         * We don't have to tell them speeds or positions yet,
         * but the motors know what mode they are set to.
         */

        spin.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        spin.setDirection(DcMotorSimple.Direction.FORWARD);
        spin.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        pivot.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        pivot.setDirection(DcMotorSimple.Direction.FORWARD);
        pivot.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        /**
         * You may wonder about the dots.
         * Each object has "methods," which do things.
         * Contract is an object, it's a DcMotor.
         * All DcMotors have a method called setMode.
         * It does exactly what you think.
         * You can look at all its methods by holding command and clicking setMode.
         */

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

    boolean raise(double raisePosition)
    {
//        pivot.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//        pivot.setTargetPosition(300);
//        return pivot.getCurrentPosition() > 250; //open before we reach the lander
        return true;
    }
    boolean lowerAllTheWay()
    {
//        pivot.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//        pivot.setTargetPosition(0);
//        return pivot.getCurrentPosition() < 10;
        return true;
    }

    void spin(double power)
    {
        //this.spin.setPower(power);
    }

    void extend(double l)
    {
//        length += l;
//        contract.setTargetPosition((int)Math.round(length * inPerTicks));
    }

    void moveToLength(double length)
    {
//        this.length = length;
//        contract.setTargetPosition((int)Math.round(length * inPerTicks));
    }

    void moveToLength()
    {
//        contract.setTargetPosition((int)Math.round(length * inPerTicks));
    }
    
    double getRadius()
    {
        //return Math.cos(getAngle()) * length;
        return 36;
    }

    double getAngle()
    {
        return 0;
//        return pivot.getCurrentPosition() * radiansPerTicks;
    }

    double getAngleInDegrees()
    {
        return 0;
//        return pivot.getCurrentPosition() * degreesPerTicks;
    }
}
