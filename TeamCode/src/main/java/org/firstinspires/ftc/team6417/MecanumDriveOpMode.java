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


package org.firstinspires.ftc.team6417;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;


/**
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 * <p>
 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all linear OpModes contain.
 * <p>
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name = "Mecanum Opmode", group = "Linear Opmode")
//@Disabled
public class MecanumDriveOpMode extends LinearOpMode {

    enum Direction {
        FORWARD, BACKWARD, LEFT, RIGHT;
    }

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    Hardware6417 robot = new Hardware6417();

    boolean hasNudged = false;

    @Override
    public void runOpMode() {

        robot.init(hardwareMap);
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        // Wait for the game to start (driver presses PLAY)

        waitForStart();
        runtime.reset();

        double forward, strafe, rotate, lift, armSpeed;

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            forward = -gamepad1.left_stick_y;
            strafe = gamepad1.left_stick_x;
            rotate = gamepad1.right_stick_x;
            lift = -gamepad2.left_stick_y;

            setDriveSpeeds(forward, strafe, rotate);

            // code to raise and lower arm
            armSpeed = gamepad2.left_stick_y;
            robot.armMotor.setPower(armSpeed);

            // this section rotates the grabber claw on a servo so that it is always
            // aligned perpendicular to the ground. This helps us keep the skystone
            // at a 90 degree angle so we can stack it on top of the building

            if(gamepad2.left_stick_y != 0){
                if(lift > 0){
                    robot.alignServo.setDirection(Servo.Direction.FORWARD);
                    if(robot.alignServo.getPosition() < 0.23) {
                        robot.alignServo.setPosition(robot.alignServo.getPosition() + (lift / 1900));
                    }
                }
                else{
                    robot.alignServo.setDirection(Servo.Direction.REVERSE);
                    robot.alignServo.setPosition(robot.alignServo.getPosition() - (lift / 1900));
                }

            }

            // extend and retract the arm's extrusions
            double extendSpeed = gamepad2.right_stick_y;
            robot.extendMotor.setPower(-extendSpeed);

            // latch and unlatch the grabber claw onto the skystone
            if(gamepad2.left_trigger > 0)
                robot.grabServo.setPosition(0);
            else if(gamepad2.right_trigger > 0)
                robot.grabServo.setPosition(0.5);


            // nudging allows us to move a small distance more precisely
            // than we can with the gamepad sticks
            if(gamepad1.dpad_up || gamepad2.dpad_up){
                nudgeRobot(Direction.FORWARD);
            }
            else if(gamepad1.dpad_left || gamepad2.dpad_left){
                nudgeRobot(Direction.LEFT);
            }
            else if(gamepad1.dpad_down || gamepad2.dpad_down){
                nudgeRobot(Direction.BACKWARD);
            }
            else if(gamepad1.dpad_right || gamepad2.dpad_right){
                nudgeRobot(Direction.RIGHT);
            }

            // latch and unlatch onto the building platform
            if(gamepad2.y){
                robot.leftGrab.setPosition(0);
                robot.rightGrab.setPosition(0);
            }
            else if(gamepad2.x){
                robot.leftGrab.setPosition(0.75);
                robot.rightGrab.setPosition(0.75);
            }

        }
    }

    // moves robot in direction controlled by top gamepad button for a moment
    private void setDriveSpeeds(double forward, double strafe, double rotate) {

        double frontLeftSpeed = forward + strafe + rotate;
        double frontRightSpeed = forward - strafe - rotate;
        double backLeftSpeed = forward - strafe + rotate;
        double backRightSpeed = forward + strafe - rotate;

        double largest = 1.0;
        largest = Math.max(largest, Math.abs(frontLeftSpeed));
        largest = Math.max(largest, Math.abs(frontRightSpeed));
        largest = Math.max(largest, Math.abs(backLeftSpeed));
        largest = Math.max(largest, Math.abs(backRightSpeed));


        robot.leftFront.setPower(frontLeftSpeed / largest);
        robot.rightFront.setPower(frontRightSpeed / largest);
        robot.leftBack.setPower(backLeftSpeed / largest);
        robot.rightBack.setPower(backRightSpeed / largest);
    }

    // nudges robot based on direction passed in
    // directions will be dealt with in runOpMode
    private void nudgeRobot(Direction dir) {

        switch(dir) {
            case FORWARD:
                setDriveSpeeds(0.2, 0, 0);
                break;
            case BACKWARD:
                setDriveSpeeds(-0.2, 0, 0);
                break;
            case LEFT:
                setDriveSpeeds(0, 0, 0.2);
                break;
            case RIGHT:
                setDriveSpeeds(0, 0, -0.2);
                break;
        }

        sleep(10);
        setDriveSpeeds(0, 0, 0);

    }
}