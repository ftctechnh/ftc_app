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
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import java.util.ArrayList;
import java.util.List;

/**
 * Demonstrates empty OpMode
 */
@Autonomous(name = "Concept: CompVisionTest", group = "Concept")
public class CompVisionTest extends LinearOpMode {

  public static final String TAG = "CompVisionTest";
  private VuforiaLocalizer vuforia;

  @Override
  public void runOpMode() throws InterruptedException {
    /*
      Create new localize parameters with back camera
     */
    VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(R.id.cameraMonitorViewId);
    parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
    parameters.vuforiaLicenseKey = "AfKfmjn/////AAAAGYbqotFKtkJ9irGUPXqP+XJYGrQvtyI3Vp+udm5R/m2uNOK7qPA6pzFujQYQQPVErgy52HDW1FIi0tUGLi0cYmHXHKVHKqGiPyTcNLKMaI/TRFxBrn04qsi7R1UdWlSJ7qUL3G0IhJ4o4v4oSH5a0F+af1SHmBxYDkY3q+iUh/sl3eC5egEKCWKq2lYmcbsZnbFgMtGH7sSyfNEu9ycWcKYMiufB5CuyQYgpJOue44CuaJj0Mz75No5JTTKr4RlP0zCzPgk6a7+q+PNBVJAl2jPIIrp+3aSFxFu4rD4dcKdae6izkWSLzABVA4JJAvX74p+fM9b0aEilzdnmbjRPC/65+/drIzzJCTvIrK2CQVEr";
    this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);

    //Load trackable objects
    VuforiaTrackables stonesAndChips = this.vuforia.loadTrackablesFromAsset("StonesAndChips");
    VuforiaTrackable stones = stonesAndChips.get(0);
    VuforiaTrackable chips = stonesAndChips.get(1);

    //Turn the data set into an easily parsed
    List<VuforiaTrackable> allTrackables = new ArrayList<>();
    allTrackables.addAll(stonesAndChips);

    //Set the phone location on the robot. Filled with an useless identity matrix ONLY HERE TO REMOVE NULLPOINTER ERROR
    OpenGLMatrix phoneLocationOnRobot = OpenGLMatrix.identityMatrix();

    //set locator parameters includes camera side and phone location on robot
    ((VuforiaTrackableDefaultListener)chips.getListener()).setPhoneInformation(phoneLocationOnRobot, parameters.cameraDirection);
    ((VuforiaTrackableDefaultListener)stones.getListener()).setPhoneInformation(phoneLocationOnRobot, parameters.cameraDirection);

    telemetry.addData(">", "Press play to start tracking");
    telemetry.update();
    waitForStart();

    //activate the locator
    stonesAndChips.activate();

    while (opModeIsActive()) {

      //Check to see if the picture is visible, if so send telemetry accordingly
      for (VuforiaTrackable target : allTrackables) {
        telemetry.addData(target.getName(), ((VuforiaTrackableDefaultListener)target.getListener()).isVisible() ? "Visible" : "Not Visible",
                ((VuforiaTrackableDefaultListener)target.getListener()).getPose());
      }

      telemetry.update();
      idle();
    }

  }
}
