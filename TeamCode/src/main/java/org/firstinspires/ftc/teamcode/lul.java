package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.modules.GamepadV2;
import org.firstinspires.ftc.teamcode.modules.MecanumDrive;
import org.firstinspires.ftc.teamcode.modules.PIDController;

import java.util.Timer;

@TeleOp(name = "lul")
public class lul extends OpMode {
    private GamepadV2 pad1 = new GamepadV2();
    private DcMotor frontLeft, frontRight, backLeft, backRight;
    private DcMotor slide;
    private DcMotor flipper;
    public static final int PID_DELAY_MS = 10;
    public static final int ENCODER_TICKS_PER_SECOND = 32000;
    boolean pidOn;
    PIDController controllerFL = new PIDController(0, PID_DELAY_MS), controllerFR = new PIDController(0, PID_DELAY_MS), controllerBL = new PIDController(0, PID_DELAY_MS), controllerBR = new PIDController(0, PID_DELAY_MS);
    int lastPositionFL = 0, lastPositionFR = 0, lastPositionBL = 0, lastPositionBR = 0;

    public void init() {
        frontLeft = hardwareMap.dcMotor.get("frontLeft");
        frontRight = hardwareMap.dcMotor.get("frontRight");
        backLeft = hardwareMap.dcMotor.get("backLeft");
        backRight = hardwareMap.dcMotor.get("backRight");

        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        slide = hardwareMap.dcMotor.get("slide");
        flipper = hardwareMap.dcMotor.get("flipper");
    }

    public void loop() {
        pad1.update(gamepad1);

        MecanumDrive.loop(frontLeft, frontRight, backLeft, backRight, pad1);

        slide.setPower(pad1.triggers_exponential(1.00));
        if(pad1.left_bumper) {
            flipper.setPower(0.05);
        }
        else if(pad1.right_bumper) {
            flipper.setPower(-0.80);
        }
        else {
            flipper.setPower(0.00);
        }

        new Thread(){
            @Override
            public void run() {
                while(true) {
                    if(pidOn) {
                        int ticksPerSecond = (frontLeft.getCurrentPosition() - lastPositionFL) / controllerFL.getDelay();
                        lastPositionFL = frontLeft.getCurrentPosition();
                        controllerFL.setPIDParam(PIDController.PID_SETPOINT, ENCODER_TICKS_PER_SECOND * (float) frontLeft.getPower());
                        int pidAdjust = controllerFL.doPID(ticksPerSecond);
                        frontLeft.setPower(pidAdjust / ENCODER_TICKS_PER_SECOND);
                    }
                }
            }
        }.start();
        new Thread(){
            @Override
            public void run() {
                while(true) {
                    if(pidOn) {
                        int ticksPerSecond = (frontRight.getCurrentPosition() - lastPositionFR) / controllerFR.getDelay();
                        lastPositionFR = frontRight.getCurrentPosition();
                        controllerFR.setPIDParam(PIDController.PID_SETPOINT, ENCODER_TICKS_PER_SECOND * (float) frontRight.getPower());
                        int pidAdjust = controllerFR.doPID(ticksPerSecond);
                        frontRight.setPower(pidAdjust / ENCODER_TICKS_PER_SECOND);
                    }
                }
            }
        }.start();
        new Thread(){
            @Override
            public void run() {
                while(true) {
                    if(pidOn) {
                        int ticksPerSecond = (backLeft.getCurrentPosition() - lastPositionBL) / controllerBL.getDelay();
                        lastPositionBL = backLeft.getCurrentPosition();
                        controllerBL.setPIDParam(PIDController.PID_SETPOINT, ENCODER_TICKS_PER_SECOND * (float) backLeft.getPower());
                        int pidAdjust = controllerBL.doPID(ticksPerSecond);
                        backLeft.setPower(pidAdjust / ENCODER_TICKS_PER_SECOND);
                    }
                }
            }
        }.start();
        new Thread(){
            @Override
            public void run() {
                while(true) {
                    if(pidOn) {
                        int ticksPerSecond = (backRight.getCurrentPosition() - lastPositionBR) / controllerBR.getDelay();
                        lastPositionBR = backRight.getCurrentPosition();
                        controllerBR.setPIDParam(PIDController.PID_SETPOINT, ENCODER_TICKS_PER_SECOND * (float) backRight.getPower());
                        int pidAdjust = controllerBR.doPID(ticksPerSecond);
                        backRight.setPower(pidAdjust / ENCODER_TICKS_PER_SECOND);
                    }
                }
            }
        }.start();
    }
}
