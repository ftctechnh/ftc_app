package org.firstinspires.ftc.teamcode.MinerClue;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * Created by JL on 11/9/2017.
 */
@TeleOp(name = "Guido", group = "TeleOp")
public class guido extends LinearOpMode {
    private DcMotor rightMotor;
    private DcMotor leftMotor;
    double RPower = 0;
    double LPower = 0;
    double presition = 1;
    boolean keyedPrecition = false;
    public void runOpMode() {
        rightMotor = hardwareMap.get(DcMotor.class, "Rmotor");
        rightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        leftMotor = hardwareMap.get(DcMotor.class, "Lmotor");


        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        while (opModeIsActive()) {
            rightMotor.setPower(gamepad1.right_stick_y * presition);
            leftMotor.setPower(gamepad1.left_stick_y * presition);
            if(!keyedPrecition){
                if(gamepad1.left_trigger > 0 && gamepad1.right_trigger > 0){
                    presition = 1;
                    keyedPrecition = true;
                } else if(gamepad1.left_trigger > 0){
                    presition -= 0.3;
                    keyedPrecition = true;
                } else if(gamepad1.right_trigger > 0) {
                    presition += 0.3;
                    keyedPrecition = true;
                }
            }
            if(gamepad1.right_trigger == 0 && gamepad1.left_trigger == 0)
                keyedPrecition = false;
        }

    }
}
