
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.util.Range;


@TeleOp

public class POV extends LinearOpMode {

    private DcMotor m1, m2;
    private Servo arm;
    //m3,m4
    private double rightPower = 0;
    private double leftPower = 0;


    @Override
    public void runOpMode() {

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        // Wait for the game to start (driver presses PLAY)
        m1 = hardwareMap.get(DcMotor.class, "Motor1");
        m2 = hardwareMap.get(DcMotor.class, "Motor2");

        arm = hardwareMap.get(Servo.class, "Servo1");



        //m3 = hardwareMap.get(DcMotor.class,"Motor3");
        //m4 = hardwareMap.get(DcMotor.class,"Motor4");

        waitForStart();


        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            telemetry.addData("Status", "Running");
            telemetry.update();

            rightPower = Range.clip(gamepad1.left_stick_y - gamepad1.left_stick_x,-0.5,0.5);
            leftPower = Range.clip(gamepad1.left_stick_y + gamepad1.left_stick_x,-0.5,0.5);

            arm.setPosition(gamepad2.right_stick_y);


            double turn = gamepad1.right_stick_x;

            if (turn > 0){
                m1.setPower(0.5);
                m2.setPower(-0.5);
                //m3.setPower(0.5);
                //m4.setPower(-0.5);
            }
            else if(turn < 0){
                m1.setPower(-0.5);
                m2.setPower(0.5);
                //m3.setPower(-0.5);
               // m4.setPower(0.5);
            }
            else{
                m1.setPower(-rightPower);
                m2.setPower(-rightPower);
               // m3.setPower(leftPower);
                //m4.setPower(leftPower);
            }


        }
    }
}
