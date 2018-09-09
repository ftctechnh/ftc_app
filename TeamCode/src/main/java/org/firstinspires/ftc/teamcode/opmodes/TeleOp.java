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
package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.robotutil.Team;
import org.firstinspires.ftc.teamcode.tasks.BlockPushTask;
import org.firstinspires.ftc.teamcode.tasks.DriveTrainTask;
import org.firstinspires.ftc.teamcode.tasks.FlipTask;
import org.firstinspires.ftc.teamcode.tasks.IntakeTask;
import org.firstinspires.ftc.teamcode.tasks.SlideTask;

import static org.firstinspires.ftc.teamcode.robotutil.Team.BLUE;
import static org.firstinspires.ftc.teamcode.robotutil.Team.RED;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "Threaded Teleop")
public class TeleOp extends LinearOpMode {
    private DriveTrainTask driveTrainTask;

    private SlideTask slideTask;
    private IntakeTask intakeTask;
    private FlipTask flipTask;
    private BlockPushTask blockPushTask;
    Servo jewelArm;
    Servo cryptoArm;
    Team team = Team.BLUE;
    int leftSlidePos;
    int rightSlidePos;
    int slideTicksPerInch;
    int pos0 = 0;
    int pos1 = 6;
    int pos2 = 12;
    int pos3 = 18;
    int pos4 = 24;

    public void runOpMode() throws InterruptedException{
        initialize();
        telemetry.addData("Ready!", "");
        telemetry.update();
        waitForStart();
        long startTime = System.currentTimeMillis();

        driveTrainTask.start();
        slideTask.start();
        intakeTask.start();
        flipTask.start();;
        blockPushTask.start();
        while(opModeIsActive()) {
            telemetry.update();
           /* //Timer for 2 minute teleop period
            long elapsed = System.currentTimeMillis() - startTime;

            if (elapsed > 120 * 1000) {
                //Stop all tasks, the tasks will stop motors etc.
                driveTrainTask.running = false;
                slideTask.running = false;
                intakeTask.running = false;
                //Get out of the loop
                break;
            } else {
                int seconds = 120 - (int) (elapsed / 1000L);
                String timeString = (seconds / 60) + ":";
                if (seconds % 60 < 10) {
                    timeString += 0;
                }
                timeString += seconds % 60;
                telemetry.addData("Time elapsed", timeString);
            }
            telemetry.update();*/
        }
        stop();
    }
        public void initialize(){
            driveTrainTask = new DriveTrainTask(this);
            intakeTask = new IntakeTask(this);
            slideTask = new SlideTask(this);
            flipTask = new FlipTask(this);
            blockPushTask = new BlockPushTask(this);
            jewelArm = hardwareMap.servo.get("jewelArm");
            jewelArm.setDirection(Servo.Direction.REVERSE);
            jewelArm.setPosition(AutoFull.jewelArmUpPos);

            cryptoArm = hardwareMap.servo.get("cryptoArm");
//            decrease value to go more vertically\
            cryptoArm.setPosition(0.3);
        }

    public void setTeam() {
        boolean confirmed = false;
        while (!confirmed && !isStopRequested()) {
            if (gamepad1.x || gamepad2.x) {
                team = BLUE;
            } else if (gamepad1.b || gamepad2.b) {
                team = RED;
            }
            telemetry.addData("Team", team);
            if ((gamepad1.left_stick_button && gamepad1.right_stick_button) || (gamepad2.left_stick_button && gamepad2.right_stick_button)){
                telemetry.addData("Confirmed!", "");
                confirmed = true;
            }
            telemetry.update();
        }
    }

}
