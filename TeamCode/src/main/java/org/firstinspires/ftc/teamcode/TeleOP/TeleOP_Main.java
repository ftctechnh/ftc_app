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
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.HardwareRobot;

import static org.firstinspires.ftc.teamcode.HardwareRobot.COLOR_POS_DOWN;
import static org.firstinspires.ftc.teamcode.HardwareRobot.GRAB_POS_DOWN;
import static org.firstinspires.ftc.teamcode.HardwareRobot.GRAB_POS_UP;
import static org.firstinspires.ftc.teamcode.HardwareRobot.RELIC_POS_PIVOT_INIT;
import static org.firstinspires.ftc.teamcode.HardwareRobot.RELIC_POS_PIVOT_UP;
import static org.firstinspires.ftc.teamcode.HardwareRobot.START_POS_DOWN;
import static org.firstinspires.ftc.teamcode.HardwareRobot.START_POS_UP;
import static org.firstinspires.ftc.teamcode.HardwareRobot.RELIC_POS_CLAW_INIT;
import static org.firstinspires.ftc.teamcode.HardwareRobot.RELIC_POS_CLAW_HAND;

@TeleOp(name = "MAIN", group = "Servo")
//@Disabled
public class TeleOP_Main extends LinearOpMode {

    /// VARIABILE

    private ElapsedTime runtime = new ElapsedTime();
    static final double INCREMENT = 0.01;
    static final double DRIVEPOW = 0.5;
    static final double LIFTPOW = 1.0;
    //static final double BRATPOW = 0.5;
    static final double     DRIVE_SPEED             = 0.2;
    static final double     TURN_SPEED = 0.15;
    static final double     COUNTS_PER_MOTOR_REV    = 1120 ;    // ANDYMARK_TICKS_PER_REV
    static final double     DRIVE_GEAR_REDUCTION    = 1.0 ;     // This is < 1.0 if geared UP ???????? or 2.0
    static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference ????????
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double     COUNTS_PER_CM = COUNTS_PER_INCH / 2.54;
    static final double     COUNTER_PER_CM_RETREAT = COUNTS_PER_INCH / 2.54;
    static final double     COUNTS_PER_CM_EXTEND = COUNTS_PER_INCH / 2.54;

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

            double FRONT_LEFT_POWER;
            double FRONT_RIGHT_POWER;
            double BACK_LEFT_POWER;
            double BACK_RIGHT_POWER;
            /**GAMEPAD1**/
            double x = gamepad1.right_stick_x;  /// stanga-dreapta (LFT STICK)
            double y = -gamepad1.left_stick_y; /// fata-spate (LFET STICK)
            double z; /// stanga-dreapta (RIGHT STICK)
            if (gamepad1.left_trigger > 0)
                z = gamepad1.left_trigger;//era cu -
            else if (gamepad1.right_trigger > 0)
                z = -gamepad1.right_trigger;//era cu +
            else z = 0;

            /**PRINDERE CUBURI**/

            if (gamepad1.b && moved >= 0) {position_up = GRAB_POS_UP; position_down = GRAB_POS_DOWN;   moved = -1; sleep(400);}
            else if (gamepad1.a  && moved < 0) {position_up = START_POS_UP; position_down = START_POS_DOWN;    moved = 0;  sleep(400);}
            robot.Up_Hand.setPosition(position_up);
            robot.Down_Hand.setPosition(position_down);
            idle();

            /**LIFT**/
            double putereLift;
            if (gamepad1.x || gamepad2.x)
                putereLift = LIFTPOW;
            else if (gamepad1.y || gamepad2.y)
                putereLift = -LIFTPOW;
            else putereLift = 0;
            robot.Lift.setPower(putereLift);
            telemetry.update();

            /**GAMEPAD2**/
            double x2 = gamepad2.right_stick_x;  /// stanga-dreapta (LFT STICK)
            double y2 = -gamepad2.left_stick_y; /// fata-spate (LFET STICK)
            double z2; /// stanga-dreapta (RIGHT STICK)

                /** PRINDERE CUBURI **/
            if (gamepad2.a && moved_down >= 0) {position_down = GRAB_POS_DOWN;  moved_down = -1;    sleep(400);}
            else if (gamepad2.a && moved_down < 0) {position_down = START_POS_DOWN;   moved_down = 0; sleep(400);}
            if (gamepad2.b && moved >= 0) {position_up = GRAB_POS_UP;  moved = -1;    sleep(400);}
            else if (gamepad2.b && moved < 0) {position_up = START_POS_UP;   moved = 0; sleep(400);}
            robot.Up_Hand.setPosition(position_up);
            robot.Down_Hand.setPosition(position_down);
            idle();

            if (gamepad2.left_trigger > 0)
                z2 = gamepad2.left_trigger;//era cu -
            else if (gamepad2.right_trigger > 0)
                z2 = -gamepad2.right_trigger;//era cu +
            else z2 = 0;

            /**PIVOT**/
            /*if (gamepad1.dpad_left)///URCA
            {
                robot.Pivot_Relic.setPosition(RELIC_POS_PIVOT_UP);
                moved_pivot = RELIC_POS_PIVOT_UP;
            }
            if (gamepad1.dpad_right) {///COBOARA
                /*
                if (moved_pivot !=RELIC_POS_PIVOT_UP)
                {
                    moved_pivot = RELIC_POS_PIVOT_INIT;
                    robot.Pivot_Relic.setPosition(moved_pivot);
                }
                else
                if (moved_pivot != RELIC_POS_PIVOT_INIT)
                {
                    while (moved_pivot > RELIC_POS_PIVOT_INIT && opModeIsActive()) {
                        moved_pivot -= 0.005;//era 0.01
                        robot.Pivot_Relic.setPosition(moved_pivot);
                    }
                }
            }*/


            if (gamepad2.dpad_left)///URCA
            {
                if( moved_pivot > 0)
                    moved_pivot-=0.005;
                robot.Pivot_Relic.setPosition(moved_pivot);
            }
            if (gamepad2.dpad_right)
            {
                ///COBOARA
                    if( moved_pivot < 1)
                        moved_pivot+=0.005
                                ;
                    robot.Pivot_Relic.setPosition(moved_pivot);
            }

            /**CLAW**/
            if ((gamepad2.left_bumper || gamepad1.left_bumper) && moved_claw >= 0) {position_claw = RELIC_POS_CLAW_HAND; moved_claw--;   sleep(400);}
            else if ((gamepad2.left_bumper || gamepad1.left_bumper) && moved_claw < 0) {position_claw = RELIC_POS_CLAW_INIT; moved_claw++;   sleep(400);}
            robot.Claw_Relic.setPosition(position_claw);

            //DECLARARE PUTERE
            if (x != 0 || y != 0 || z != 0)
            {
                FRONT_LEFT_POWER     = - x - DRIVEPOW * y + DRIVEPOW * z;
                FRONT_RIGHT_POWER    = - x + DRIVEPOW * y + DRIVEPOW * z;
                BACK_LEFT_POWER      = + x - DRIVEPOW * y + DRIVEPOW * z;
                BACK_RIGHT_POWER     = + x + DRIVEPOW * y + DRIVEPOW * z;
            }
            else
            {
                FRONT_LEFT_POWER     = DRIVEPOW * (- x2 - y2 + z2);
                FRONT_RIGHT_POWER    = DRIVEPOW * (- x2 + y2 + z2);
                BACK_LEFT_POWER      = DRIVEPOW * (+ x2 - y2 + z2);
                BACK_RIGHT_POWER     = DRIVEPOW * (+ x2 + y2 + z2);
            }
            robot.FrontLeftMotor.setPower(FRONT_LEFT_POWER);
            robot.FrontRightMotor.setPower(FRONT_RIGHT_POWER);
            robot.BackRightMotor.setPower(BACK_RIGHT_POWER);
            robot.BackLeftMotor.setPower(BACK_LEFT_POWER);

            /** BRAT RELICVA **/
            if (gamepad1.dpad_up)
            {
                if (!gamepad2.dpad_up)
                    robot.Brat.setPower(1);
                else
                    robot.Brat.setPower(0);
                if (!gamepad2.dpad_down)
                    robot.BratRetreat.setPower(0.18);
                else
                    robot.BratRetreat.setPower(0);
            }
            else if (gamepad1.dpad_down)
            {
                if (!gamepad2.dpad_up)
                    robot.Brat.setPower(-1);
                else
                    robot.Brat.setPower(0);
                if (!gamepad2.dpad_down)
                    robot.BratRetreat.setPower(-0.3);
                else
                    robot.BratRetreat.setPower(0);
            }
            else
            {
                robot.Brat.setPower(0);
                robot.BratRetreat.setPower(0);
            }


            telemetry.addData(">", "Done");
            telemetry.update();
        }
    }
}
