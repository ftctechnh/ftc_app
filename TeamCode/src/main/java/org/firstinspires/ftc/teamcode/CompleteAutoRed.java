package org.firstinspires.ftc.teamcode;
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

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 *                              RED SIDE
 * This autonomous op mode will drive forward then turn a bit to face the center
 * vortex. It will shoot the particles preloaded. Then it will drive forward, 
 * since the cap ball is directly under the center vortex, and parks (Moving
 * the cap ball).
 * 
 * Servo "trigger" needs testing to obtain appropriate values to use.
 * 
 *          LETS DO THIS
 */

@Autonomous(name="Golden Eagles: CompleteAutoRed", group="Pushbot")
//@Disabled
public class CompleteAutoRed extends LinearOpMode {

    /* Declare OpMode members. */
    HardwareK9bot robot = new HardwareK9bot(); // Use a Pushbot's hardware
    private ElapsedTime runtime = new ElapsedTime();
    
    static final double SLOW_FORWARD_SPEED = 0.2;
    static final double FORWARD_SPEED = 0.5;
    static final double FULL_POWER = 1;
    static final double NO_POWER = 0;
    static final double MIN_TRIGGER_POSITION = 0.2;
    static final double MAX_TRIGGER_POSITION = 0.4;
    

    @Override
    public void runOpMode() {

        /*
         * Initialize the drive system variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);

        // Send telemetry message to signify robot waiting
        telemetry.addData("Status", "Ready to rumble!");
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        
        
        // Step 1: Go forward a bit, then rotate in place
        goToPosition();
        
        // Step 2: Shoot one particle
        shootParticle();
        
        // Step 3: Shoot another particle if there is (ANOTHER ONE)
        shootParticle();
        
        // Step 4: Park in center vortex, moving the cap ball as well
        parkInCenter();


        // Send telemetry message to signify robot completion
        telemetry.addData("Status", "Relaxin'.");
        telemetry.update();
        
    }
    
    public void goToPosition() {
        
        // Reset timer for this method
        runtime.reset();

        // Go forward a bit (1 sec)
        robot.leftMotor.setPower(SLOW_FORWARD_SPEED);
        robot.rightMotor.setPower(SLOW_FORWARD_SPEED);
        while (opModeIsActive() && (runtime.seconds() < 1.0)) {
            telemetry.addData("Status", "Running gotToPosition() method (1).");
            telemetry.update();
        }
        
        // Rotate right in place (1 sec)
        robot.leftMotor.setPower(-SLOW_FORWARD_SPEED);
        robot.rightMotor.setPower(SLOW_FORWARD_SPEED);
        while (opModeIsActive() && (runtime.seconds() < 2.0)) {
            telemetry.addData("Status", "Running gotToPosition() method (2).");
            telemetry.update();
        }
        
        // Go forward a bit again (1 sec)
        robot.leftMotor.setPower(SLOW_FORWARD_SPEED);
        robot.rightMotor.setPower(SLOW_FORWARD_SPEED);
        while (opModeIsActive() && (runtime.seconds() < 3.0)) {
            telemetry.addData("Status", "Running gotToPosition() method (3).");
            telemetry.update();
        }
        
        // Stop motors
        robot.leftMotor.setPower(NO_POWER);
        robot.rightMotor.setPower(NO_POWER);
        
    }
    
    public void shootParticle() {
        
        // Reset timer for this method
        runtime.reset();

        // Load a particle in (2.5 sec TOTAL)
        
        // (1) 1.5 sec
        robot.trigger.setPosition(MAX_TRIGGER_POSITION);
        while (opModeIsActive() && (runtime.seconds() < 1.5)) {
            telemetry.addData("Status", "Running shootParticle() method (1).");
            telemetry.update();
        }
        // (2) 1 sec
        robot.trigger.setPosition(MIN_TRIGGER_POSITION);
        while (opModeIsActive() && (runtime.seconds() < 2.5)) {
            telemetry.addData("Status", "Running shootParticle() method (2).");
            telemetry.update();
        }
        
        // Shoot the particle (2.5 sec TOTAL)
        
        // (1) 1.5 sec
        robot.leftShooter.setPower(FULL_POWER);
        robot.rightShooter.setPower(FULL_POWER);
        while (opModeIsActive() && (runtime.seconds() < 3.5)) {
            telemetry.addData("Status", "Running shootParticle() method (3).");
            telemetry.update();
        }
        // (2) 1 sec
        robot.trigger.setPosition(MAX_TRIGGER_POSITION);
        while (opModeIsActive() && (runtime.seconds() < 3.5)) {
            telemetry.addData("Status", "Running shootParticle() method (4).");
            telemetry.update();
        }
        
        robot.leftShooter.setPower(NO_POWER);
        robot.rightShooter.setPower(NO_POWER);
        robot.trigger.setPosition(MIN_TRIGGER_POSITION)
        
    }
    
    public void parkInCenter() {
        
        // Reset timer for this method
        runtime.reset();
        
        // Go forward to center vortex (1 sec)
        robot.leftMotor.setPower(FORWARD_SPEED);
        robot.rightMotor.setPower(FORWARD_SPEED);
        while (opModeIsActive() && (runtime.seconds() < 1.0)) {
            telemetry.addData("Status", "Running parkInCenter() method.");
            telemetry.update();
        }
        
        // Stop motors
        robot.leftMotor.setPower(NO_POWER);
        robot.rightMotor.setPower(NO_POWER);
        
    }
    
}
