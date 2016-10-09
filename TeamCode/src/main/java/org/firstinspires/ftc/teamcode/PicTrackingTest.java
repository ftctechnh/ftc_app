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

import android.hardware.Camera;
import android.hardware.Camera.Parameters;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a PushBot
 * It includes all the skeletal structure that all linear OpModes contain.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@Autonomous(name="Concept: PicTracking Test", group="Concept")
public class PicTrackingTest extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
    public static final String TAG = "PicTrackingTest";
    VuforiaLocalizer vuforia;



    @Override
    public void runOpMode() throws InterruptedException {


        DcMotor motorOne =  hardwareMap.dcMotor.get("MotorOne");
        motorOne.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //create a new set of parameters with the back camera selected. Then create an instance of
        //the tracker class
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(R.id.cameraMonitorViewId);
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);

        //Setup the targets. We are getting them from a file located in the assets folder
        VuforiaTrackables stonesAndChips = this.vuforia.loadTrackablesFromAsset("StonesAndChips");
        VuforiaTrackable stones = stonesAndChips.get(0);
        VuforiaTrackable chips = stonesAndChips.get(1);

        //Create a list of the targets that is easy to iterate through
        List<VuforiaTrackable> allTrackable = new ArrayList<>();
        allTrackable.addAll(stonesAndChips);
        allTrackable.get(0).setName("Stones");
        allTrackable.get(1).setName("Chips");

        //Tell the engine where the phone is on the robot. In this example we do not care so we
        //just feed it an identity matrix.
        OpenGLMatrix phoneLocationOnRobot = OpenGLMatrix.identityMatrix();

        //Tell the tracker the phone parameters we set earlier, and tell it where the phone is
        //located.
        ((VuforiaTrackableDefaultListener)stones.getListener()).setPhoneInformation(phoneLocationOnRobot,parameters.cameraDirection);
        ((VuforiaTrackableDefaultListener)chips.getListener()).setPhoneInformation(phoneLocationOnRobot,parameters.cameraDirection);

        //wait for the driver to press the start button
        telemetry.addData(">", "Press play to track");
        telemetry.update();
        waitForStart();

        //Activate the tracker.
        stonesAndChips.activate();

        boolean motorOn = false;

        //Main Program Loop
        while (opModeIsActive()) {
            if (((VuforiaTrackableDefaultListener)stones.getListener()).isVisible()) {
                telemetry.addData(stones.getName(), "Visible");

                //Get the position matrix of the image from the tracker.
                OpenGLMatrix picLocation = ((VuforiaTrackableDefaultListener)stones.getListener()).getPose();

                if (picLocation != null) {
                    telemetry.addData("Pos", format(picLocation));
                    telemetry.addData("Pos Avg:", getXAvg(picLocation));

                    if (getXAvg(picLocation) > 60 /*&& !motorOn*/) {
                        telemetry.addData("Motor", "motor ON");
                        motorOne.setPower(-0.03);
                        motorOn = true;
                    } else if (getXAvg(picLocation) < -60 /*&& !motorOn*/) {
                        telemetry.addData("Motor", "motor ON");
                        motorOne.setPower(0.03);
                        motorOn = true;
                    }
                    if (getXAvg(picLocation) < 25 && getXAvg(picLocation) > -25) {
                        telemetry.addData("Motor", "Skipped");
                        motorOne.setPower(0);
                        motorOn = false;
                    }
                    telemetry.addData("Motor Stat", motorOn);

                } else {
                    telemetry.addData("Pos", "Unknown");
                }
            } else {
                telemetry.addData(stones.getName(), "Not Visible");
                motorOne.setPower(0);
            }
            telemetry.update();
            idle();
        }
    }

    public void motorRamp(double maxPower, DcMotor motor) {
        if(maxPower < 0) {
            motor.setDirection(DcMotorSimple.Direction.REVERSE);
            maxPower = Math.abs(maxPower);
        }else  {
            motor.setDirection(DcMotorSimple.Direction.FORWARD);
        }
        double power = 0;
        runtime.reset();
        while (power < maxPower) {
            power = (0.002*runtime.milliseconds());
            motor.setPower(power);
        }
    }

    public double getXAvg(OpenGLMatrix position) {
        //gets the average of 5 readings of the X position. This usually shifts the trend back and
        //lowers the chance of a major deviation in the readings

        double avg = 0;
        for(int i = 0; i < 4; i++) {
            avg += getX(position);
        }
        return (avg/5d);
    }

    public float getX(OpenGLMatrix position) {
        //gets an X value from the position on the phones coordinate system

        VectorF translation = position.getTranslation();
        return translation.get(0);
    }

    public String format(OpenGLMatrix position) {
        //gets the x axis value they are stored such that x is the first value and the other values
        //move like a normal coordinate system (x,y,z) -> (0,1,2)

        VectorF translation = position.getTranslation();
        float x = translation.get(0);
        return String.format(Locale.US, "%s, %f", translation.toString(), x);
    }
}


