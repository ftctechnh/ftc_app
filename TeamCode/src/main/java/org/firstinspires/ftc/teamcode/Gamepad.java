/*
Copyright 2019 FIRST Tech Challenge Team 15791

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
associated documentation files (the "Software"), to deal in the Software without restriction,
including without limitation the rights to use, copy, modify, merge, publish, distribute,
sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial
portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import java.lang.annotation.Target;
import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.hardware.Blinker;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a PushBot
 * It includes all the skeletal structure that all linear OpModes contain.
 *
 * Remove a @Disabled the on the next line or two (if present) to add this opmode to the Driver Station OpMode list,
 * or add a @Disabled annotation to prevent this OpMode from being added to the Driver Station
 */
@TeleOp

public class Gamepad extends LinearOpMode {
    private Gyroscope imu;
    private DcMotor motor_1;
    private DcMotor motor_2;
    private DcMotor motor_3;
    private DcMotor motor_4;
    private DcMotor motor_5;
    private Servo servo_1;
    private Servo servo_2;
    private Blinker expansion_Hub_2;


    @Override
    public void runOpMode() {
        imu = hardwareMap.get(Gyroscope.class, "imu");
        motor_1 = hardwareMap.get(DcMotor.class, "motor 1");
        motor_2 = hardwareMap.get(DcMotor.class, "motor 2");
        motor_3 = hardwareMap.get(DcMotor.class, "motor 3");
        motor_4 = hardwareMap.get(DcMotor.class, "motor 4");
        motor_5 = hardwareMap.get(DcMotor.class, "motor 5");
        servo_1 = hardwareMap.servo.get("servo 1");
        servo_2 = hardwareMap.servo.get("servo 2");
        expansion_Hub_2 = hardwareMap.get(Blinker.class, "Expansion Hub 2");

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        double tgtPower = 0;
        double tgtPower2 = 0;
        double tgtPower3 = 0;
        double tgtPower4 = 0;
        boolean servoPower = false;

        servoPower = this.gamepad2.a;
        servo_1.setPosition(0);
        servo_2.setPosition(1);

        while (opModeIsActive()) {
            tgtPower = -this.gamepad1.left_stick_y;
            motor_1.setPower(tgtPower);
            motor_2.setPower(-tgtPower);
            motor_3.setPower(tgtPower);
            motor_4.setPower(-tgtPower);

            tgtPower2 = -this.gamepad1.left_stick_x;
            motor_1.setPower(tgtPower2);
            motor_2.setPower(tgtPower2);
            motor_3.setPower(-tgtPower2);
            motor_4.setPower(-tgtPower2);

            tgtPower3 = -this.gamepad1.right_stick_x;
            motor_1.setPower(tgtPower3);
            motor_2.setPower(tgtPower3);
            motor_3.setPower(tgtPower3);
            motor_4.setPower(tgtPower3);

            tgtPower4 = -this.gamepad2.left_stick_y;
            motor_5.setPower(tgtPower4);



            if(gamepad2.a == true)
            {
                servo_1.setPosition(0.5);
                servo_2.setPosition(0.5);
            }
            else
            {
                servo_1.setPosition(0);
                servo_2.setPosition(1);

            }

            telemetry.addData("Target Power", tgtPower);
            telemetry.addData("Motor Power", motor_1.getPower());
            telemetry.addData("Status", "Running");
            telemetry.update();

        }
    }
}
