/*
Copyright (c) 2016 Robert Atkinson

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Robert Atkinson nor the names of his contributors may be used to
endorse or promote products derived from this software without specific prior
written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESSFOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 * <p>
 * This particular OpMode just executes a basic Tank Drive Teleop for a PushBot
 * It includes all the skeletal structure that all linear OpModes contain.
 * <p>
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name = "Test Encoders", group = "Linear Opmode")
// @Autonomous(...) is the other common choice
//@Disabled
public class testEncoders extends LinearOpMode {

    DcMotor frontLeftMotor;
    DcMotor backLeftMotor;
    DcMotor frontRightMotor;
    DcMotor backRightMotor;
    DcMotor verticalArmMotor;
    public DcMotor P1Motor = null;
    public DcMotor P2Motor = null;

    /* Declare OpMode members. */
    private ElapsedTime runtime = new ElapsedTime();
    // DcMotor leftMotor = null;
    // DcMotor rightMotor = null;

    @Override
    public void runOpMode() {

            /* This runs the init in the hardware map! */
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        /* Find all hardware in configuration */
        frontLeftMotor = hardwareMap.dcMotor.get("FL");
        backLeftMotor = hardwareMap.dcMotor.get("BL");
        frontRightMotor = hardwareMap.dcMotor.get("FR");
        backRightMotor = hardwareMap.dcMotor.get("BR");
        verticalArmMotor = hardwareMap.dcMotor.get("VAM");
        P1Motor = hardwareMap.dcMotor.get("P1");
        P2Motor = hardwareMap.dcMotor.get("P2");

        verticalArmMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        sleep(100);
        verticalArmMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontLeftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            telemetry.addLine("B = FrontRight");
            telemetry.addLine("X = BackLeft");
            telemetry.addLine("Y = BackRight");
            telemetry.addLine("D-Pad = VerticalArm");

            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Front Left: ", frontLeftMotor.getCurrentPosition());
            telemetry.addData("Front Right: ", frontRightMotor.getCurrentPosition());
            telemetry.addData("Back Left: ", backLeftMotor.getCurrentPosition());
            telemetry.addData("Back Right: ", backRightMotor.getCurrentPosition());
            telemetry.addData("Vertical Arm:", verticalArmMotor.getCurrentPosition());
            telemetry.update();

            if (gamepad1.a) {
                frontLeftMotor.setPower(.2);
            } else {
                frontLeftMotor.setPower(0);
            }

            if (gamepad1.b) {
                frontRightMotor.setPower(.2);
            } else {
                frontRightMotor.setPower(0);
            }

            if (gamepad1.x) {
                backLeftMotor.setPower(.2);
            } else {
                backLeftMotor.setPower(0);
            }

            if (gamepad1.y) {
                backRightMotor.setPower(.2);
            } else {
                backRightMotor.setPower(0);
            }

            if (gamepad1.dpad_up){
                verticalArmMotor.setPower(.2);
            }

            if (gamepad1.dpad_down){
                verticalArmMotor.setPower(-.2);
            }

            if (!gamepad1.dpad_up && !gamepad1.dpad_down){
                verticalArmMotor.setPower(0);
            }

            if(gamepad1.left_bumper){
                P1Motor.setPower(1);
            }
            else{
                P1Motor.setPower(0);
            }

            if(gamepad1.right_bumper){
                P2Motor.setPower(1);
            }
            else{
                P2Motor.setPower(0);
            }

        }
    }
}