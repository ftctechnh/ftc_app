package org.firstinspires.ftc.teamcode.RelicRecovery;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by jeppe on 25-09-2017.
 */
@TeleOp (name = "test",group = "TeleOp")
public class RelicRecoveryHardware extends OpMode
{
    // -----Hardware-----
    // ---Motors---
    protected DcMotor right;
    protected DcMotor left;
    protected DcMotor lift;
    
    // ---Servos---
    // normal
    protected Servo blockLatch;

    // continuous rotation
    protected CRServo relicClaw;

    // -----Variables-----
    double rightPower;
    double leftPower;
    double liftPower;

    double relicClawPosition;

    // -------Init-------
    @Override
    public void init() 
    {
        // ---Hardware Mapping---
        // ---DcMotor---
        right = hardwareMap.dcMotor.get("r");
        left = hardwareMap.dcMotor.get("l");
        lift = hardwareMap.dcMotor.get("lift");

        // ---Servos---
        // normal
        blockLatch = hardwareMap.servo.get("latch");
        // continuous rotation
        relicClaw = hardwareMap.crservo.get("claw");

        // -----set direction-----
        right.setDirection(DcMotorSimple.Direction.REVERSE);
        left.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    // -------loop-------
    @Override
    public void loop() {}
}
