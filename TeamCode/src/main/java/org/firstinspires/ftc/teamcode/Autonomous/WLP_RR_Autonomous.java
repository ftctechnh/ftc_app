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

package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;


/**
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This is the We Love Pi Relic Recovery autonomous mode main entry point. The We Love Pi autonomous
 * mode includes a Jewel Arm and a Mecanum Wheel Drivetrain steered with a Modern Robotics gyro sensor.
 *
 *
 */

@Autonomous(name= "WLP_RR_Autonomous", group = "We Love PI")

public class WLP_RR_Autonomous extends LinearOpMode {

    static final double     SPEED_STRAIGHT = 0.4;
    static final double     SPEED_KNOCK = 0.2;
    static final double     SPEED_TURN = 0.3;

    static final double     BR_JEWEL_TO_CRYPTO_0    = -92 ;    // Go at 0 degree 92 CM from Jewel to Crypto Box
    static final double     BR_JEWEL_TO_CRYPTO_90   = 50 ;    // Go at 90 degree 61 CM from Jewel to Crypto Box
    static final double     JEWEL_KNOCK_DISTANCE = 8;          // Forward and backward distance to knock the jewel

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private WLP_RR_JewelArm arm = new WLP_RR_JewelArm();
    private WLP_RR_DriveByGyro drivetrain = new WLP_RR_DriveByGyro();
    private WLP_RevColorSensor colorSensor = new WLP_RevColorSensor();

    private WLP_RevColorSensor.ColorName teamColor = WLP_RevColorSensor.ColorName.BLUE;


    @Override
    public void runOpMode() {

        double distance_at_0 = BR_JEWEL_TO_CRYPTO_0;
        double distance_at_90 = BR_JEWEL_TO_CRYPTO_90;
        double jewelKnockDistance = JEWEL_KNOCK_DISTANCE;

        telemetry.addData("Status", "Initializing");
        telemetry.update();

        arm.init(telemetry, hardwareMap);
        colorSensor.init(telemetry, hardwareMap);
        drivetrain.init(telemetry, hardwareMap, this);


        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // Lower the ARM
        arm.lowerArm();

        sleep(100);

        // Knock the Jewel
        if (teamColor != colorSensor.getColor()) {
            jewelKnockDistance = -1.0*JEWEL_KNOCK_DISTANCE;
        }

        drivetrain.moveStraight(SPEED_KNOCK, jewelKnockDistance);
        distance_at_0 += jewelKnockDistance;

        // Raise the arm
        arm.raiseArm();

        // Move straight to crypto box
        telemetry.addData("WLP_RR_Autonomous", "Starting to drive forward");
        drivetrain.moveStraight(SPEED_STRAIGHT, distance_at_0);


        telemetry.addData("WLP_RR_Autonomous", "Turning right");
        drivetrain.gyroTurn(SPEED_TURN, -90);

        telemetry.addData("WLP_RR_Autonomous", "Starting to drive forward");
        drivetrain.moveStraight(SPEED_STRAIGHT, distance_at_90);

        // Show the elapsed game time and wheel power.
        telemetry.addData("WLP_RR_Autonomous", "Completed in (secs)" + runtime.toString());
        telemetry.update();
    }

}
