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

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;




@TeleOp
//@Disabled
public class EncoderRecorder  extends LinearOpMode{

    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor m1, m2;

    @Override
    public void runOpMode(){
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        m1 = hardwareMap.get(DcMotor.class, "Motor1");
        m2 = hardwareMap.get(DcMotor.class, "Motor2");

        m2.setDirection(DcMotor.Direction.REVERSE);

        m1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        m2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        m1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        m2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        int[][][] positions = new int[20][2][2];

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            if (gamepad1.a){
                positions = record();
            }

            if (gamepad1.x){
                run(positions);
            }

        }
    }

    public int[][][] record(){
        int[][][] pos = new int[20][2][2];

        int i = 0;
        boolean moving = false;

        while (gamepad1.a == false) {
            double rightPower = Range.clip(gamepad1.left_stick_y - gamepad1.right_stick_x, -0.5, 0.5);
            double leftPower = Range.clip(gamepad1.left_stick_y + gamepad1.right_stick_y, -0.5, 0.5);

            if ((rightPower != 0) || (leftPower != 0)){
                m1.setPower(rightPower);
                m2.setPower(leftPower);
                moving = true;
                pos[i][0][1] = (rightPower > 0)? 1:-1;
                pos[i][1][1] = (leftPower > 0) ? 1:-1;
            }
            else{
                m1.setPower(0);
                m2.setPower(0);

                if (moving){
                    pos[i][0][0] = m1.getCurrentPosition();
                    pos[i][1][0] = m2.getCurrentPosition();
                    m1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    m2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    moving = false;
                }
            }


        }
        return pos;
    }

    public void run(int[][][] pos){
        for (int[][] i : pos){
            m1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            m1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            int posR = i[0][0];
            int posL = i[1][0];
            double  velR = Range.clip(i[0][1],-0.5,0.5);
            double  velL = Range.clip(i[1][1],-0.5,0.5);

            m1.setTargetPosition(posR);
            m2.setTargetPosition(posL);

            m1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            m2.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            m1.setPower(velR);
            m2.setPower(velL);

            while (m1.isBusy() && m2.isBusy()){
                telemetry.addData("Satus","Moving");
            }

            m1.setPower(0);
            m2.setPower(0);
        }

    }


}
