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

package org.firstinspires.ftc.teamcode.TeleOP;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.HardwareRobot;

import static org.firstinspires.ftc.teamcode.HardwareRobot.GRAB_POS_DOWN;
import static org.firstinspires.ftc.teamcode.HardwareRobot.GRAB_POS_UP;
import static org.firstinspires.ftc.teamcode.HardwareRobot.RELIC_POS_CLAW_HAND;
import static org.firstinspires.ftc.teamcode.HardwareRobot.RELIC_POS_CLAW_INIT;
import static org.firstinspires.ftc.teamcode.HardwareRobot.START_POS_DOWN;
import static org.firstinspires.ftc.teamcode.HardwareRobot.START_POS_UP;

@TeleOp(name = "TEST_2xControllere", group = "Servo")
//@Disabled
public class TeleOP_2xControllere extends LinearOpMode {

    /// VARIABILE

    private ElapsedTime runtime = new ElapsedTime();
    static final double INCREMENT = 0.01;
    static final double DRIVEPOW = 0.6;//era 0.2//0.9
    static final double LIFTPOW = 0.7;
    static final double BRATPOW = 0.5;

    HardwareRobot robot = new HardwareRobot();

    double position_up = START_POS_UP;  //position_up = START_POS_UP;
    double position_down = START_POS_DOWN;  //position_down = START_POS_DOWN;
    double position_claw = RELIC_POS_CLAW_INIT;
    //double position_pivot = RELIC_POS_PIVOT_INIT;
    double cnt = 0;

    @Override
    public void runOpMode() {

        int moved = 0;
        int moved_down = 0;
        int moved_claw = 0;
        double moved_pivot = 0;

        robot.init(hardwareMap);
        idle();
        waitForStart();

        while (opModeIsActive()) {

            // DECLARARE VARIABILE
            double x = gamepad1.right_stick_x;  /// stanga-dreapta (LFT STICK)
            double y = -gamepad1.left_stick_y; /// fata-spate (LFET STICK)
            double z; /// stanga-dreapta (RIGHT STICK)

            if (gamepad1.left_trigger > 0)
                z = gamepad1.left_trigger;//era cu -
            else if (gamepad1.right_trigger > 0)
                z = -gamepad1.right_trigger;//era cu +
            else z = 0;

            double x2 = gamepad2.right_stick_x;  /// stanga-dreapta (LFT STICK)
            double y2 = -gamepad2.left_stick_y; /// fata-spate (LFET STICK)
            double z2; /// stanga-dreapta (RIGHT STICK)

            if (gamepad2.left_trigger > 0)
                z2 = gamepad2.left_trigger;//era cu -
            else if (gamepad2.right_trigger > 0)
                z2 = -gamepad2.right_trigger;//era cu +
            else z2 = 0;

            //DECLARARE PUTERE
            double FRONT_LEFT_POWER = -x - y + z - x2 - y2 + z2;
            double FRONT_RIGHT_POWER = -x + y + z - x2 + y2 + z2;
            double BACK_LEFT_POWER = +x - y + z + x2 - y2 + z2;
            double BACK_RIGHT_POWER = +x + y + z + x2 + y2 + z2;

            /// MOTOARE ///

            robot.FrontLeftMotor.setPower(DRIVEPOW * FRONT_LEFT_POWER);
            robot.FrontRightMotor.setPower(DRIVEPOW * FRONT_RIGHT_POWER);
            robot.BackRightMotor.setPower(DRIVEPOW * BACK_RIGHT_POWER);
            robot.BackLeftMotor.setPower(DRIVEPOW * BACK_LEFT_POWER);
            /// SERVO ///

            //CUB SUS
            if (gamepad1.b && moved >= 0) {
                position_up = GRAB_POS_UP;
                moved = -1;
                ///sleep(400);
            } else if (gamepad1.b && moved < 0) {
                position_up = START_POS_UP;
                moved = 0;
                ///sleep(400);
            }
            ///CUB JOS
            if (gamepad1.a && moved_down >= 0) {
                position_down = GRAB_POS_DOWN;
                moved_down = -1;
                ///sleep(400);
            } else if (gamepad1.a && moved_down < 0) {
                position_down = 0.8;//START_POS_DOWN;
                moved_down = 0;
                ///sleep(400);
            }
            robot.Up_Hand.setPosition(position_up);
            robot.Down_Hand.setPosition(position_down);
            idle();

            /** PIVOT **/
            if (gamepad2.dpad_right) {
                robot.Pivot_Relic.setPosition(1);
                moved_pivot = 1;
            }
            if (gamepad2.dpad_left) {
                if (moved_pivot == 0) {
                    moved_pivot = 0.5;
                    robot.Pivot_Relic.setPosition(moved_pivot);
                } else if (moved_pivot == 1) {
                    while (moved_pivot > 0.5 && opModeIsActive()) {
                        moved_pivot -= 0.005;//era 0.01
                        robot.Pivot_Relic.setPosition(moved_pivot);
                    }
                }
            }

            /** CLAW **/
            if (gamepad2.left_bumper && moved_claw >= 0) {
                position_claw = RELIC_POS_CLAW_HAND;
                moved_claw--;
                sleep(400);
            } else if (gamepad2.left_bumper && moved_claw < 0) {
                position_claw = RELIC_POS_CLAW_INIT;
                moved_claw++;
                sleep(400);
            }
            robot.Claw_Relic.setPosition(position_claw);
            /** BRAT RELICVA **/
            if (gamepad2.dpad_up) {
                while (gamepad2.dpad_up && opModeIsActive())
                    robot.Brat.setPower(BRATPOW);
            } else if (gamepad2.dpad_down) {
                while (gamepad2.dpad_down && opModeIsActive())
                    robot.Brat.setPower(-BRATPOW);
            } else {
                robot.Brat.setPower(0);
            }
            /**if (gamepad1.right_bumper){
                    while (gamepad1.right_bumper && opModeIsActive())
                        robot.BratRetreat.setPower(0.2);
                }**/
                /** LIFT MODIFICA **/
                if (gamepad2.y) {
                   // while (gamepad2.y && opModeIsActive())
                        robot.Lift.setPower(LIFTPOW);
                } else if (gamepad2.x) {
                    //while (gamepad2.x && opModeIsActive())
                        robot.Lift.setPower(-LIFTPOW);
                } else {
                    robot.Lift.setPower(0);
                }
                telemetry.update();
            }
            telemetry.addData(">", "Done");
            telemetry.update();
        }
    }
