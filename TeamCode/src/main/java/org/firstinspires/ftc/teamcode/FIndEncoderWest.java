
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

@TeleOp
public class FIndEncoderWest extends LinearOpMode {

    DcMotor grabber,lift;
    @Override
    public void runOpMode() {

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        
        lift = hardwareMap.get(DcMotor.class,"Lift_Motor");
        grabber = hardwareMap.get(DcMotor.class,"Grabber_Motor");

        grabber.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        grabber.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        grabber.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        
        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            double liftPower = Range.clip(gamepad1.left_stick_y,-0.2,0.2);
            double grabPower = Range.clip(gamepad1.right_stick_y,-0.2,0.2);

            lift.setPower(liftPower);
            grabber.setPower(grabPower);

            int lEn = lift.getCurrentPosition();
            int gEn = grabber.getCurrentPosition();

            String data = "Lifting:" + lEn + "Grabber:" + gEn;
            telemetry.addData("Motors",data);
            telemetry.update();

        }
    }
}
