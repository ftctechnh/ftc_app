package org.firstinspires.ftc.team2993.structural;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

public class RobotHardware
{
    private HardwareMap map;

    public DcMotor fR, fL, bR, bL;
    public DcMotor armR, armL;
    public Servo   claw, sideArm;

    public Sensors color;

    public final double SERVO_OPEN = 0.1;
    public final double SERVO_CLOSED = 0.6;

    public RobotHardware(HardwareMap _map){
        map = _map;
    }

    public void init()
    {
        fR = map.get(DcMotor.class, "fR");
        fL = map.get(DcMotor.class, "fL");
        bR = map.get(DcMotor.class, "bR");
        bL = map.get(DcMotor.class, "bL");

        armR = map.get(DcMotor.class, "armL");
        armL = map.get(DcMotor.class, "armR");

        fR.setDirection(DcMotorSimple.Direction.FORWARD);
        bR.setDirection(DcMotorSimple.Direction.FORWARD);
        fL.setDirection(DcMotorSimple.Direction.REVERSE);
        bL.setDirection(DcMotorSimple.Direction.REVERSE);

        fR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        bR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        fL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        bL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        armR.setDirection(DcMotorSimple.Direction.FORWARD);
        armL.setDirection(DcMotorSimple.Direction.REVERSE);

        armR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        armL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        claw = map.get(Servo.class, "claw");
        sideArm = map.get(Servo.class, "sidearm");

        claw.scaleRange(.4, 1);

        color = new Sensors(map);
    }

    public void SetArm(double power)
    {
        power = Range.clip(power, -1d, 1d);
        armL.setPower(power);
        armR.setPower(power);
    }

    public void SetDrive(double powerLeft, double powerRight)
    {
        driveLeft(powerLeft);
        driveRight(powerRight);
    }

    public void driveLeft(double power)
    {
        power = Range.clip(power, -1d, 1d);
        fL.setPower(power);
        bL.setPower(power);
    }

    public void driveRight(double power)
    {
        power = Range.clip(power, -1d, 1d);

        fR.setPower(power);
        bR.setPower(power);
    }

    public void drive(double power)
    {
        driveLeft(power);
        driveRight(power);
    }

    public boolean isBusy(){
        return fL.isBusy() || fR.isBusy();
    }
}
