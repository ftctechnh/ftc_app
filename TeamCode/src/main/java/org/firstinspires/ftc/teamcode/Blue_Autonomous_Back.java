package org.firstinspires.ftc.teamcode;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.*;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.Exceptions;
import com.qualcomm.robotcore.hardware.ColorSensor;

// Created by Swagster_Wagster on 9/29/17
//
//
// Last edit: 10/22/17 BY MRINAAL RAMACHANDRAN


@Autonomous(name = "Blue_Autonomous_Back",group="We Love Pi")
public class Blue_Autonomous_Back extends LinearOpMode {

    Autonomous_Functions af = new Autonomous_Functions();

    VuforiaLocalizer vuforia;

    float hsvValues[] = {0F,0F,0F};

    // bPrevState and bCurrState represent the previous and current state of the button.
    boolean bPrevState = false;
    boolean bCurrState = false;

    // bLedOn represents the state of the LED.
    boolean bLedOn = true;

    public String senseJewelColor () {

        String color = "NOTHING";


        Color.RGBToHSV(af.colorSensor.red() * 8, af.colorSensor.green() * 8, af.colorSensor.blue() * 8, hsvValues);

        int red = af.colorSensor.red();
        int blue = af.colorSensor.blue();
        int green = af.colorSensor.green();

        telemetry.addData("blue value is", blue);
        telemetry.addData("red value is ", red);
        telemetry.addData("green value is ", green);
        telemetry.update();
        opModeIsActive();


        // sense color blue
        if (blue >=2 && blue > red) {
            opModeIsActive();
            telemetry.addData("i am blue", af.colorSensor.blue());
            telemetry.addData("red value is ", af.colorSensor.red());
            telemetry.update();
            opModeIsActive();

            color = "BLUE";
        }

        // sense color red
        if (red >=2  && red>blue ) {

            opModeIsActive();
            telemetry.addData("i am red", af.colorSensor.red());
            telemetry.addData("blue value is ", af.colorSensor.blue());
            telemetry.update();
            opModeIsActive();

            color = "RED";
        }

        // Return the color
        return color;
    }

    public void knockOffJewel () {

        af.dropper.setPosition(0);
        telemetry.addData("Position down ", "yeet");
        telemetry.update();
        opModeIsActive();

        af.mysleep(500);

        af.colorSensor.enableLed(true);

        af.jewelColor = senseJewelColor();

        while(af.jewelColor=="NOTHING")
        {
            af.jewelColor = senseJewelColor();
            telemetry.addData("sensing color", "yeet");
            telemetry.update();
            opModeIsActive();
        }
        //sense color

        // if color red turn that way
        if (af.jewelColor == "RED") {

            af.moveMotorWithEncoder(.05, 2000, Constants.spinRight);
            af.stopMotor(1000);
            af.dropper.setPosition(1);
            af. stopMotor(1000);
            af.moveMotorWithEncoder(.03, 2000, Constants.spinLeft);
            af.stopMotor(1000);
        }

        // if color blue turn other way
        else if (af.jewelColor == "BLUE") {
            af.moveMotorWithEncoder(.05, 2000, Constants.spinLeft);
            af.stopMotor(1000);
            af.dropper.setPosition(1);
            af.stopMotor(1000);
            af.moveMotorWithEncoder(.03, 2000, Constants.spinRight);
            af.stopMotor(1000);
        }

    }


    @Override
    public void runOpMode() {

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

        parameters.vuforiaLicenseKey = "AQBbCRX/////AAAAGevvCPApu0phjwrtg1hDMb5FkMizE8AaJCDJtXwc8y45ZcarRtpG2Edqj3dWfUCXGTucT4Ovr/Exh6ekwjyC6D+N59nTw2BdGx9VxVquX6vBsKl2acD9cfQOBkxSs6puRSAH/Pm3FMiP4AN/LXC+VedYtfIAE2UZmIF041Lr8sLs+wgephTto8kZ8ELxQZx98Q5T/RaLo3wkz5+jYksV5Pi8VRG0vFhk47fwa0gUrZDTFhq11og/bD9zZmLiFqH3tyHeMMSkYFVakMctRPNjKYPgi9iG4jlDjGbtq2a56QxTxzjBo/J/8K2PN2A6vnBw3tMc2E1KitBtPuKrD9h/5ACsIGWh2Zo5TiQC1YGTQr6F";

        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        parameters.cameraMonitorFeedback = VuforiaLocalizer.Parameters.CameraMonitorFeedback.NONE;
        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);

        VuforiaTrackables relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        VuforiaTrackable relicTemplate = relicTrackables.get(0);

        waitForStart();

        // Moved the init hardware below wait for start. Dropper motor doesnt go 0-1 above wait for start
        af.init(hardwareMap);

        relicTrackables.activate();

        boolean found=false;
        while (opModeIsActive()) {

            // IMAGE REG MIGHT NOT WORK DUE TO LIGHTING. BEFORE FREAKING OUT, GO TO A LOCATION WITH BETTER LIGHTING AND TEST
            if(!found) {

                RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);
                if (vuMark == RelicRecoveryVuMark.LEFT) {

                    telemetry.addData("vuMark", vuMark);
                    telemetry.update();

                    found=true;

                    knockOffJewel();

                } else if (vuMark == RelicRecoveryVuMark.CENTER) {

                    telemetry.addData("vuMark", vuMark);
                    telemetry.update();
                    found=true;

                    knockOffJewel();

                } else if (vuMark == RelicRecoveryVuMark.RIGHT) {

                    telemetry.addData("vuMark", vuMark);
                    telemetry.update();
                    found=true;

                    knockOffJewel();


                } else {
                    telemetry.addData("VuMark", "not visible");

                }
            }
            telemetry.update();

        }
    }
}




