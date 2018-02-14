package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * ☺ Hi! This is the perfect teleop code for December 16, 2017! ☺
 */
@TeleOp(name = "Testing New Bot", group = "Our Teleop")
//@Disabled
public class testNewbot extends LinearOpMode {

    DcMotor                 slowMotor;
    DcMotor                 fastMotor;
    DcMotor                 verticalArmMotor;
    Servo                   trayServo;

    /*These values are used for the drive*/
    double verticalMax = 5900;
    double verticalMin = 300;
    double slowPower = .5;
    double fastPower = 1;
    double trayOut = 0;
    double trayIn = 1;
    boolean up;

    @Override
    public void runOpMode() {

//        /* Initialize the vertical arm encoder */
        verticalArmMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        robot.clawMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        sleep(100);
        verticalArmMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        slowMotor = hardwareMap.dcMotor.get("SM");
        fastMotor = hardwareMap.dcMotor.get("FM");
        verticalArmMotor = hardwareMap.dcMotor.get("VAM");
        trayServo = hardwareMap.servo.get("TS");

        // Wait for the start button
        telemetry.addLine("!☻ Ready to Run ☻!");
        telemetry.update();
        waitForStart();

        while (opModeIsActive()) {

            // Display the current value
            telemetry.addLine("Hi~♪");
            telemetry.addData("Wheel Control", "Hold X to start, release to stop");
            telemetry.addData("Tray Moving Controls", "Use the D-Pad ↑ & ↓ buttons!");
            telemetry.addData("Tray Flipping Controls", "Hold A for out, release for in");
            telemetry.update();

        /* Vertical Arm Motor */
            if (gamepad1.dpad_up && verticalArmMotor.getCurrentPosition() < verticalMax) {
                    verticalArmMotor.setPower(1);
                    up = true;
                }
             else {
                if (gamepad1.dpad_up && verticalArmMotor.getCurrentPosition() >= verticalMax) {
                    verticalArmMotor.setPower(0);
                    up = true;
                } else {
                    if (!gamepad1.dpad_up && !gamepad1.dpad_down) {
                        verticalArmMotor.setPower(0);
                        up = false;
                    }
                }
            }

            if (!up) {
                if (gamepad1.dpad_down && verticalArmMotor.getCurrentPosition() > verticalMin) {
                    verticalArmMotor.setPower(-1);
                    up = false;
                    }
                } else {
                    if (gamepad1.dpad_down && verticalArmMotor.getCurrentPosition() <= verticalMin) {
                        verticalArmMotor.setPower(0);
                        up = false;
                    } else {
                        if (!gamepad1.dpad_up && !gamepad1.dpad_down) {
                            verticalArmMotor.setPower(0);
                            up = false;
                        }
                    }
                }
            }

        /* Servo Control */
            if (gamepad1.x) {
                slowMotor.setPower(slowPower);
                fastMotor.setPower(fastPower);
            }
            else {
                slowMotor.setPower(0);
                fastMotor.setPower(0);
            }

            if(gamepad1.a){
            trayServo.setPosition(trayOut);
            }
            else{
                trayServo.setPosition(trayIn);
            }

            }
        }

