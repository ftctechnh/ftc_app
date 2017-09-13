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
package org.firstinspires.ftc.teamcode;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

/**
 * This file provides basic Telop driving for a Pushbot robot.
 * The code is structured as an Iterative OpMode
 *
 * This OpMode uses the common Pushbot hardware class to define the devices on the robot.
 * All device access is managed through the HardwarePushbot class.
 *
 * This particular OpMode executes a basic Tank Drive Teleop for a PushBot
 * It raises and lowers the claw using the Gampad Y and A buttons respectively.
 * It also opens and closes the claws slowly using the left and right Bumper buttons.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name="TeleopTest", group="Pushbot")
public class TeleopTest extends OpMode{

    /* Declare OpMode members. */
    TestTeleopHardware robot       = new TestTeleopHardware(); // use the class created to define a Pushbot's hardware
    float hsvValues[] = {0F,0F,0F};

    VuforiaLocalizer vuforia;
    VuforiaTrackable relicTemplate;

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);
        robot.color.enableLed(true);

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

        parameters.vuforiaLicenseKey = "ARBXEdT/////AAAAGY2Q5MtVQEHQmwvm8X0bY7J/9Zf721jAIWZEpecl4azd6rvsEoabTso95SFlfWxAZxCd/AhM733sVVxYWDr+6Nwe02C5aEukRUwDpoP/ONoq2cBBwiQ/GuXiFDB7rClbUW0YPmmhVqMdJrV3yX5HJNW3aLD0vHx6udXXMoECsnlmQkBXy5TLjrNoKA4K7+YojSCjuWmnIRw3LcKYmijaXvcfpeW7MHX5j5SsZ3MDT6F84lNeE/S4EOSTX4Ii65IGYjkfMH4PQ0iCaV2wOPNP8u8vUEH+wvweovKcPiS4LHMF3v+w8C9ksn+gpeJU+aq3tDJ+hgsQXykgthAWiyyzB+DYp3ciP3RPOEGojX1Pu/RO";

        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.FRONT;
        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);
        VuforiaTrackables relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate"); // can help in debugging; otherwise not necessary
        relicTrackables.activate();

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Say", "Hello Driver");    //
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        double left;
        double right;
        //int servoVal;
       // LynxI2cColorRangeSensor range = (LynxI2cColorRangeSensor) robot.color;

        Color.RGBToHSV(robot.color.red() * 8, robot.color.green() * 8, robot.color.blue() * 8, hsvValues);


        // Run wheels in tank mode (note: The joystick goes negative when pushed forwards, so negate it)
        left = -gamepad1.left_stick_y;
        right = -gamepad1.right_stick_y;
        robot.Motor1.setPower(left);
        robot.Motor2.setPower(-right);

//        if(gamepad1.right_bumper = true) {
//            robot.servo1.setPosition(255);
//            servoVal = 255;
//        }else{
//            robot.servo1.setPosition(0);
//            servoVal = 0;
//        }

        if (gamepad1.b){
            robot.servo1.setPosition(.5);
        }else if(gamepad1.x){
            robot.servo1.setPosition(0);
        };

        //robot.servo1.setPosition((right/2)+.5);

        RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);
        if (vuMark != RelicRecoveryVuMark.UNKNOWN) {

                /* Found an instance of the template. In the actual game, you will probably
                 * loop until this condition occurs, then move on to act accordingly depending
                 * on which VuMark was visible. */
            telemetry.addData("VuMark", "%s visible", vuMark);
        } else {
            telemetry.addData("Vumark ", "Not Found");
        }

        // ALL THE FREAKING TELEMETRY IN OUR TELEOP THERES A LOT
        telemetry.addData("left",  "%.2f", left);
        telemetry.addData("right", "%.2f", right);
        telemetry.addLine()
                 .addData("Clear", robot.color.alpha())
                 .addData("Red  ", robot.color.red())
                 .addData("Green", robot.color.green())
                 .addData("Blue ", robot.color.blue())
                 .addData("Hue", hsvValues[0]);

        //telemetry.addLine()
          //      .addData("distance", "%.3f", range.getDistance(DistanceUnit.CM));

        telemetry.addLine()
                .addData("servo value", robot.servo1.getPosition());
               // .addData("seroSetVal", servoVal)

        telemetry.addLine()
                .addData("encoder count motor one", robot.Motor1.getCurrentPosition())
                .addData("encoder count motor two", robot.Motor2.getCurrentPosition());

        telemetry.addLine()
                .addData("range", robot.MRrange.getDistance(DistanceUnit.CM));

        telemetry.addLine()
                .addData("heading", robot.heading);
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }

}
