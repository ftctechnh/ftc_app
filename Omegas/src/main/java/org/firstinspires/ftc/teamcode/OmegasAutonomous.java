/* Copyright (c) 2014, 2015 Qualcomm Technologies Inc

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Qualcomm Technologies Inc nor the names of its contributors
may be used to endorse or promote products derived from this software without
specific prior written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.util.ElapsedTime;

public abstract class OmegasAutonomous extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();
    HardwareOmegas Ω = new HardwareOmegas();

    // IPS Units
    static final double FORWARD_SPEED = 0.6;
    static final double TURN_SPEED = 0.5;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        Ω.init(hardwareMap);
        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            telemetry.addData("Left color sensor blue: ", Ω.leftColorSensor.blue());
            telemetry.addData("Left color sensor red: ", Ω.leftColorSensor.red());
            telemetry.addData("Right color sensor blue: ", Ω.rightColorSensor.blue());
            telemetry.addData("Right color sensor red: ", Ω.rightColorSensor.red());

            switch (getColor()) {
                case RED:
                    pushBeacon(Ω.leftColorSensor.red(),
                            Ω.rightColorSensor.red());
                    break;
                case BLUE:
                    pushBeacon(Ω.leftColorSensor.blue(),
                            Ω.rightColorSensor.blue());
                    break;
            }
        }
    }

    abstract Alliance getColor();

    // Test for a color.
    public void pushBeacon(int leftValue, int rightValue) {
        // Stop servo
        if (rightValue >= 10 && leftValue >= 10) {
            Ω.powerServo(Ω.leftBeaconator, 0.0f);
            Ω.powerServo(Ω.rightBeaconator, 0.0f);
        }

        // Press button
        if (leftValue >= 10 && rightValue <= 10) {
            Ω.powerServo(Ω.leftBeaconator, 1.0f);
            Ω.retractServo(Ω.rightBeaconator);
        } else if (leftValue <= 10 && rightValue >= 10) {
            Ω.powerServo(Ω.rightBeaconator, 1.0f);
            Ω.retractServo(Ω.leftBeaconator);
        }
    }
}
