package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import java.lang.annotation.Target;


@TeleOp(name="BlockGrabber Linear OpMode", group="Linear Opmode")
//@Disabled
public class BlockGrabber extends LinearOpMode {

    // Declare OpMode members.
    private DcMotor GrabberMotor = null;
    private Servo s1, s2 = null;

    private int initial_angle = 0;
    private int final_angle = 45;


    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        GrabberMotor = hardwareMap.get(DcMotor.class, "Motor1");
        s1 = hardwareMap.get(Servo.class, "Servo1");
        s2 = hardwareMap.get(Servo.class, "Servo2");


        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        double initial_angle_value = Range.clip(initial_angle, 0,1 );
        double final_angle_value = Range.clip(final_angle, 0,1 );
        boolean pressed = false;

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        GrabberMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        GrabberMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        GrabberMotor.setTargetPosition(87);

        GrabberMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        while (GrabberMotor.isBusy()){
            GrabberMotor.setPower(-0.2);

        }
        GrabberMotor.setPower(0.0);
        s1.setPosition(initial_angle_value);
        s2.setPosition(initial_angle_value);

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            if (gamepad1.x && !pressed){
                s1.setPosition(final_angle_value);
                s2.setPosition(final_angle_value);

<<<<<<< HEAD
                GrabberMotor.setTargetPosition(87);
                GrabberMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                while (GrabberMotor.isBusy()){
                    GrabberMotor.setPower(0.2);

                }
                GrabberMotor.setPower(0.0);

=======
>>>>>>> 46e0b9270a53a9f184212290ff8b4f847ee3e333
                pressed = true;
            }
            if (gamepad1.x && pressed) {
                s1.setPosition(initial_angle_value);
                s2.setPosition(initial_angle_value);

                GrabberMotor.setTargetPosition(10);
                GrabberMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                while (GrabberMotor.isBusy()){
                    GrabberMotor.setPower(-0.2);

                }
                GrabberMotor.setPower(0.0);
                pressed = false;


            }



        }
    }
}
