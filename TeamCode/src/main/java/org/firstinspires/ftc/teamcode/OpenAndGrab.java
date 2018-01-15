
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;


@TeleOp

public class OpenAndGrab extends LinearOpMode {

    private DcMotor m1,m2;

    boolean pressed = false;


    @Override
    public void runOpMode() {

        m1 = hardwareMap.get(DcMotor.class,"Motor1");
        m2 = hardwareMap.get(DcMotor.class,"Motor2");

        m2.setDirection(DcMotor.Direction.REVERSE);

        m1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        m1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        m2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        m2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        m1.setTargetPosition(0);
        m2.setTargetPosition(0);

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        // Wait for the game to start (driver presses PLAY)
        waitForStart();



        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {


            telemetry.addData("Status:","Running");
            telemetry.update();


            if (gamepad1.a && !pressed) {
                m1.setTargetPosition(160);
                m2.setTargetPosition(160);


                while (m1.isBusy() && m2.isBusy()){
                    m1.setPower(0.3);
                    m2.setPower(0.3);
                }

                m1.setPower(0);
                m2.setPower(0);

                pressed = true;
            }
            else if (gamepad1.a && pressed){
                m1.setTargetPosition(0);
                m2.setTargetPosition(0);


                while (m1.isBusy() && m2.isBusy()){
                    m1.setPower(-0.3);
                    m2.setPower(-0.3);
                }

                m1.setPower(0);
                m2.setPower(0);

                pressed = false;
            }




        }
    }
}