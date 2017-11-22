package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 *Created by Pramodh on 10/27/2017.
 */
@TeleOp
public class Driver extends LinearOpMode {
    private DcMotor motor0;
    private DcMotor motor1;
    private DcMotor motor2;
    private Servo servo0;
    private Servo servo1;
    private Servo servo2;


//    VerticalLift lift = new VerticalLift(servo2);

    @Override
    public void runOpMode() {

        motor0 = hardwareMap.get(DcMotor.class, "motor0");
        motor1 = hardwareMap.get(DcMotor.class, "motor1");
        motor2 = hardwareMap.get(DcMotor.class, "motor2");
        servo0 = hardwareMap.get(Servo.class, "servo0");
        servo1 = hardwareMap.get(Servo.class, "servo1");
        servo2 = hardwareMap.get(Servo.class, "servo2");

        TankDriveTrain driveTrain = new TankDriveTrain(motor0, motor1);
        Grabber grabber = new Grabber(servo0, servo1);
        VerticalLiftMotor lift = new VerticalLiftMotor(motor2);

        double leftMotorPower = 0;
        double rightMotorPower = 0;

        //sends tests data to dc phone
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        //Wait for the game to start (driver presses PLAY)
        waitForStart();

        //run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            //driveTrain.dpad(this.gamepad1.dpad_up, this.gamepad1.dpad_down);
            driveTrain.move(this.gamepad1.right_stick_y, this.gamepad1.left_stick_y);
            grabber.Grab(this.gamepad1.right_trigger);
            lift.Lift(this.gamepad1.left_bumper, this.gamepad2.left_bumper, this.gamepad1.left_trigger, this.gamepad2.left_trigger);

            telemetry.addData("Left Bumper", this.gamepad1.left_bumper);
            telemetry.addData("Right Bumper", this.gamepad1.right_bumper);
            telemetry.addData("Left Trigger", this.gamepad1.left_trigger);
            telemetry.addData("Right Trigger", this.gamepad1.right_trigger);
            telemetry.addData("Left Stick Y", this.gamepad1.left_stick_y);
            telemetry.addData("Right Stick Y", this.gamepad1.right_stick_y);
            telemetry.update();
        }

//        grabber.reset();
    }

    private void autonomous() {



    }
}