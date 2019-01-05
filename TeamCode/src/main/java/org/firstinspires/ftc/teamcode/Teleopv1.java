/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//import com.qualcomm.robotcore.util.*;
//import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//import com.qualcomm.robotcore.hardware.ColorSensor;
//import com.qualcomm.robotcore.hardware.DcMotor;
//import com.qualcomm.robotcore.hardware.I2cAddr;
//import com.qualcomm.robotcore.hardware.I2cDevice;
//import com.qualcomm.robotcore.hardware.Servo;
//import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * This OpMode uses the common Pushbot hardware class to define the devices on the robot.
 * All device access is managed through the HardwarePushbot class.
 * The code is structured as a LinearOpMode
 *
 * This particular OpMode executes a POV Game style Teleop for a PushBot
 * In this mode the left stick moves the robot FWD and back, the Right stick turns left and right.
 * It raises and lowers the claw using the Gampad Y and A buttons respectively.
 * It also opens and closes the claws slowly using the left and right Bumper buttons.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name="8535Teleop", group="Linear OpMode")
//@Disabled
public class Teleopv1 extends LinearOpMode {

    /* Declare OpMode members. */
//    HardwarePushbot robot           = new HardwarePushbot();   // Use a Pushbot's hardware
//                                                               // could also use HardwarePushbotMatrix class.
//    double          clawOffset      = 0;                       // Servo mid position
//    final double    CLAW_SPEED      = 0.02 ;                   // sets rate to move servo

    //@above default servo provided in sdk

    private boolean prodbot = false;
    //Last year we made two robots, a base model and the final result.
    // I think this boolean checked if we were using the base and then switched controls to just the four wheels.
    // May have to refer to this later.


    //Speed Factors for Fast/Slow Mode
    private double speedFactor = 1.0; //default full speed
    private ElapsedTime runtime = new ElapsedTime();
    private double currentLoopTime = 0.0;
    private double lastLoopTime = 0.0;


    //Driving Motors
    private DcMotor lf = null; //"lf" stands for left front and so on.
    private DcMotor rf = null;
    private DcMotor lb = null;
    private DcMotor rb = null;

    //other variables
    private static boolean JOYSTICK_SCALING = true;
    private static boolean tele = false; //show telemetry

    // We may use a color sensor for the item knockoff in Autonomous


    //Our software coach from last year helped us with this method that uses trigonometry to operate mecanum wheels
    private void mecanumMove(double lsx, double lsy, double rsx) {

        if (JOYSTICK_SCALING) {                 //I think Joystick_scaling is a boolean on whether the joystick is being moved
            lsy = Math.pow(lsy, 1.0);

            lsx = Math.pow(lsx, 1.0);
            rsx = Math.pow(rsx, 1.0);           //"ls" is left stick which was used for robo Mecanum movement
                                                //"rs" is right stick used for robot rotation
        }

        double r = Math.sqrt(lsy * lsy + lsx * lsx);            //This double uses the pythagoren theorem to find
                                                                // out the distance from the the joystick center
        double robotAngle = Math.atan2(-1 * lsy, lsx) - Math.PI / 4;
        double rightX = rsx;
        final double v1 = r * Math.cos(robotAngle) + rightX;    //Multiplies the scaling of the joystick to give different speeds based on joystick movement
        final double v2 = r * Math.sin(robotAngle) - rightX;
        final double v3 = r * Math.sin(robotAngle) + rightX;
        final double v4 = r * Math.cos(robotAngle) - rightX;

        lf.setPower(v1);
        rf.setPower(v2);
        lb.setPower(v3);
        rb.setPower(v4);
    }
    @Override
    public void runOpMode() throws InterruptedException{

        if (prodbot){
            telemetry.addData("Rover", "prodbot");
        } else {
            telemetry.addData("Rover", "Hal9000");
        }
//        double left;
//        double right;
//        double drive;
//        double turn;
//        double max;

        /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
        //robot.init(hardwareMap);

        //Above line is commented out because Hardware map is used for accessories such as attachment sensors/servos/motors

        // Send telemetry message to signify robot waiting;
        //telemetry.addData("Say", "Hello Driver");

        telemetry.addData("Status", "Initialized");                 //Telemetry is the messages displayed on phone
        telemetry.update();

        //initialize required driving motors
        lf = hardwareMap.get(DcMotor.class, "lf");
        rf = hardwareMap.get(DcMotor.class, "rf");
        lb = hardwareMap.get(DcMotor.class, "lb");
        rb = hardwareMap.get(DcMotor.class, "rb");

            lf.setDirection(DcMotor.Direction.REVERSE); //is REVERSE
            rf.setDirection(DcMotor.Direction.FORWARD); //is REVERSE
            lb.setDirection(DcMotor.Direction.REVERSE); //is FORWARD
            rb.setDirection(DcMotor.Direction.FORWARD); //is FORWARD

            // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();    //resets runtime()

        currentLoopTime = runtime.time();
        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            lastLoopTime = currentLoopTime;
            currentLoopTime = runtime.time();

            //gamepad1
            double forwardBack = gamepad1.left_stick_y;
            double leftRight = gamepad1.left_stick_x;
            double Rotate = gamepad1.right_stick_x;
            boolean slowMode = (gamepad1.left_trigger > 0.2);
//            boolean lowerBallArm = (gamepad1.dpad_down);
//            boolean raiseBallArm = (gamepad1.dpad_up);

            //gamepad2
//            double raiseLowerLift = gamepad2.left_stick_y;
//            double leftClamp = gamepad2.left_trigger;
//            double rightClamp = gamepad2.right_trigger;
//            boolean leftRelease = gamepad2.left_bumper;
//            boolean rightRelease = gamepad2.right_bumper;
//            boolean extendRelicArm = gamepad2.dpad_right;
//            boolean retractRelicArm = gamepad2.dpad_left;
//            boolean raiseRelicArm = gamepad2.dpad_up;
//            boolean lowerRelicArm = gamepad2.dpad_down;
//            boolean raiseRelic = gamepad2.y;//gamepad2.right_stick_y>0.2;
//            boolean lowerRelic = gamepad2.a;//gamepad2.right_stick_y<-0.2;


            if (slowMode) {
                speedFactor = 0.8;
            } else {
                speedFactor = 1.0;
            }

            double lsy = forwardBack * speedFactor;
            double lsx = leftRight * speedFactor;
            double rsx = Rotate;
            if (speedFactor < 1.0) {
                rsx *= 0.8;
            }
            mecanumMove(lsx, lsy, rsx);

            // Use gamepad buttons to move arm up (Y) and down (A)
            if (tele) telemetry.addData("Status", "Run Time: " + runtime.toString());
            if (tele) telemetry.update();

            // Pace this loop so jaw action is reasonable speed.
            sleep(50);
        }
    }
}
