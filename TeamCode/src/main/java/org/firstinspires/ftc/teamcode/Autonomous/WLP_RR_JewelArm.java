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


import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * This file provides necessary functionality for We Love Pi Team's 2017 Relic
 * Recovery Robot's jewel arm used for koncking the jewel belong to the opposing
 * team.
 * It uses servo motor to move the jewele arm and REV roboocis color and distance
 * sensor to detect color of the jewel.
 */

public class WLP_RR_JewelArm {

    // constants
    static final double     ARM_DOWN    = 0.75;    // ARM Down Servo position
    static final double     ARM_UP    = 0.0;    // ARM Resting Servo position


    // Global variables to be initialized in init function
    private Telemetry telemetry = null;
    private HardwareMap hardwareMap = null;


    //  Declare Jewel Arm private components
    private Servo arm = null;

    private boolean isInitialized = false;

    // Use default Constructors
    WLP_RR_JewelArm() {
        // Nothing needs to be done here

    }


    // Code to run ONCE when the driver hits INIT
    public void init(Telemetry telemetry, HardwareMap hardwareMap) {


        // Initialize hardware devices passed from the parent
        this.telemetry = telemetry;
        this.hardwareMap = hardwareMap;


        // Initialize jewel arm servo. Note that the strings used here as parameters
        // to 'get' must correspond to the name assigned to the servo during the robot
        // configuration step (using the FTC Robot Controller app on the phone).

        arm = hardwareMap.get(Servo.class, "jewel_servo");

        // set servo range
        arm.scaleRange(ARM_UP, ARM_DOWN);

        // RESET the servo position
        arm.setPosition(ARM_UP);

        isInitialized = true;
        telemetry.addData("Jewel Arm", "Servo Postion %f", arm.getPosition());
        telemetry.addData("Jewel Arm", "Initialization succeeded");
    }

    // Lowers the jewel arm by using the servor motor
    public void lowerArm() {
        double delta = 0.2;
        double pos = arm.getPosition();

        telemetry.addData("lowerArm", "Position before %.2f", pos);
        while (pos < ARM_DOWN) {
            pos += delta;
            arm.setPosition(pos);
            sleep(50);
        }
        telemetry.addData("lowerArm", "Position after %.2f", arm.getPosition());
    }

    // Pulls up the jewel arm to its original position
    public void raiseArm()  {

        double delta = 0.2;
        double pos = arm.getPosition();

        telemetry.addData("raiseArm", "Position before %.2f", pos);

        while (pos > ARM_UP) {
            pos -= delta;
            arm.setPosition(pos);
            sleep(50);
        }
        telemetry.addData("raiseArm", "Position after %.2f", arm.getPosition());

    }

    public final void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
