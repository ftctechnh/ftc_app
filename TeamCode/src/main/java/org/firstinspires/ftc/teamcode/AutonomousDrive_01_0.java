package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by Preston on 11/3/17.
 */

@Autonomous(name="AutonomousDrive_01_0", group="Testing")
public class AutonomousDrive_01_0 extends LinearOpMode
{
    DriveEngine engine;
    double x = 0;
    double y = 0;
    DcMotor lift = hardwareMap.dcMotor.get("lift");
    Servo glyph = hardwareMap.servo.get("glyph");
    double open = 0.7;
    double close = 0.05;
    ElapsedTime timer = new ElapsedTime();

    double t1 = 4.5, t2 = 0, t3 = 0, t4 = 0;
    double time1 = t1, time2 = time1 + t2, time3 = time2 + t3, time4 = time3 + t4;

    @Override
    public void runOpMode()
    {
        engine = new DriveEngine(hardwareMap);
        lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        waitForStart();

        while (opModeIsActive())
        {
            double time = timer.time();
            if(time < time1){
                x = 0;
                y = 1;
            }
            else if(time < time2){
                x = 0;
                y = 0;
                lift.setPower(-.5);
            }
            else if(time < time3){
            }
            else if(time < time4){
                x = 1;
                y = 0;
            }
            else{
                x = 0;
                y = 0;
            }

            engine.drive(x,y);


            telemetry.addData("leftx: ", gamepad1.left_stick_x);
            telemetry.addData("lefty: ", gamepad1.left_stick_y);
            telemetry.addData("rightx: ", gamepad1.right_stick_x);
            telemetry.addData("righty: ", gamepad1.right_stick_y);
            telemetry.addData("x: ", x);
            telemetry.addData("y: ", y);
            telemetry.update();
            idle();
        }

    }
}

