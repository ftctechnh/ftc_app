
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;



@TeleOp
//@Disabled
public class BlockGrabber extends LinearOpMode {

    private DcMotor grabber;
    private Servo s1,s2;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        grabber = hardwareMap.get(DcMotor.class, "Motor1");
        s1 = hardwareMap.get(Servo.class, "Servo1");
        s2 = hardwareMap.get(Servo.class, "Servo2");

        grabber.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        grabber.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        grabber.setTargetPosition(87);
        grabber.setMode(DcMotor.RunMode.RUN_TO_POSITION);



        boolean pressed = false;
        double currentAngle = 0;
        double interval = 0.05;
        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        while (grabber.isBusy()){
            grabber.setPower(0.2);
        }

        grabber.setPower(0);

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            if ((gamepad1.x) && (!pressed)){
                while (currentAngle < 0.5){
                    s1.setPosition(currentAngle);
                    s2.setPosition(currentAngle);
                    currentAngle += interval;

                }
                pressed = true;
            }
            else if((gamepad1.x) && (pressed)){
                while (currentAngle > 0.05) {
                    s1.setPosition(currentAngle);
                    s2.setPosition(currentAngle);
                    currentAngle -= interval;
                }
                pressed = false;
            }



        }
    }
}
