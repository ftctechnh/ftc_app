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

import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.MatrixF;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

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

        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(R.id.cameraMonitorViewId);
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);

        VuforiaTrackables stonesAndChips = this.vuforia.loadTrackablesFromAsset("StonesAndChips");
        VuforiaTrackable stones = stonesAndChips.get(0);
        VuforiaTrackable chips = stonesAndChips.get(1);

        List<VuforiaTrackable> allTrackable = new ArrayList<>();
        allTrackable.addAll(stonesAndChips);
        allTrackable.get(0).setName("Stones");
        allTrackable.get(1).setName("Chips");

        OpenGLMatrix phoneLocationOnRobot = OpenGLMatrix.identityMatrix();

        ((VuforiaTrackableDefaultListener)stones.getListener()).setPhoneInformation(phoneLocationOnRobot,parameters.cameraDirection);
        ((VuforiaTrackableDefaultListener)chips.getListener()).setPhoneInformation(phoneLocationOnRobot,parameters.cameraDirection);

        telemetry.addData(">", "Press play to track");
        telemetry.update();
        waitForStart();

        stonesAndChips.activate();

        while (opModeIsActive()) {
            if(((VuforiaTrackableDefaultListener)stones.getListener()).isVisible()) {
                telemetry.addData(stones.getName(), ((VuforiaTrackableDefaultListener)stones.getListener()).isVisible() ? "Visible" : "Not Visible");

                OpenGLMatrix picLocation = ((VuforiaTrackableDefaultListener)stones.getListener()).getPose();

                if (picLocation != null) {
                    telemetry.addData("Pos", format(picLocation));
                    if(getX(picLocation) < 25 && getX(picLocation) > -25) {
                        telemetry.addData("Pos", "In bounds");
                        motorOne.setPower(0);

                    } else if(getX(picLocation) > 110 || getX(picLocation) < -110){
                        telemetry.addData("Pos", "Out of bounds");
                        motorRamp(.5, motorOne);
                        //TODO: Build the base that spins and tweak the motor behavior to match the physical device
                    }
                } else {
                    telemetry.addData("Pos", "Unknown");
                }
            } else {
                telemetry.addData(stones.getName(), "Not Visible");
            }
            telemetry.update();
            idle();
        }
    }

    public void motorRamp(double maxPower, DcMotor motor) {
        if (motor.getPower() == maxPower) {
            return;
        }
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
    public float getX(OpenGLMatrix position) {
        VectorF translation = position.getTranslation();

        return translation.get(0);
    }

    public String format(OpenGLMatrix position) {
        VectorF translation = position.getTranslation();

        //gets the x axis value they are stored such that x is the first value and the other values
        //move like a normal coordinate system (x,y,z) -> (0,1,2)
        float x = translation.get(0);

        return String.format(Locale.US, "%s, %f", translation.toString(), x);
    }
}


