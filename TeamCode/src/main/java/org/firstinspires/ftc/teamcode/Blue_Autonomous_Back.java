package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

// Created by Swagster_Wagster on 9/29/17
//
//
// Last edit: 10/22/17 BY MRINAAL RAMACHANDRAN


@Autonomous(name = "Blue_Autonomous_Back",group="We Love Pi")
public class Blue_Autonomous_Back extends LinearOpMode {

    Autonomous_Functions af = new Autonomous_Functions();

    VuforiaLocalizer vuforia;

    public void doDropper () {

        af.dropper.setPosition(0);
        af.stopMotor(1000);
        //sense color
        // if color red turn that way
        af.moveMotorWithEncoder(.05, 500, Constants.spinLeft);
        af.stopMotor(1000);
        // if color blue turn other way
        af.dropper.setPosition(1);
        af.stopMotor(1000);
        af.moveMotorWithEncoder(.05, 500, Constants.spinRight);
        af.stopMotor(1000);

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

        //STEP ONE KNOCK OFF BALL

        telemetry.addData("vumark", "before activate");
        telemetry.update();
        relicTrackables.activate();

        telemetry.addData("vumark", "after activate");
        telemetry.update();
        boolean found=false;
        while (opModeIsActive()) {

            // IMAGE REG MIGHT NOT WORK DUE TO LIGHTING. BEFORE FREAKING OUT, GO TO A LOCATION WITH BETTER LIGHTING AND TEST
            if(!found) {
                RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);
                if (vuMark == RelicRecoveryVuMark.LEFT) {

                    telemetry.addData("vuMark", vuMark);
                    telemetry.update();
                    found=true;
                    doDropper();
                    telemetry.addData("autonomous_left", "moving backward");
                    telemetry.update();
                    af.moveMotorWithEncoder(.05, 3000, Constants.left);
                    af.stopMotor();
                /*
                af.moveMotorWithEncoder(.2, 1300, Constants.spinLeft);
                af.stopMotor();
                af.moveMotorWithEncoder(.2, 2600, Constants.right);
                af.stopMotor();
                af.moveMotorWithEncoder(.2, 2600, Constants.forward);

                */

                } else if (vuMark == RelicRecoveryVuMark.CENTER) {

                    telemetry.addData("vuMark", vuMark);
                    telemetry.update();
                    found=true;
                    doDropper();
                    telemetry.addData("autonomous_left", "moving backward");
                    telemetry.update();
                    af.moveMotorWithEncoder(.05, 3000, Constants.left);
                    af.stopMotor();
                /*
                af.moveMotorWithEncoder(.2, 1300, Constants.spinLeft);
                af.stopMotor();
                af.moveMotorWithEncoder(.2, 2600, Constants.right);
                af.stopMotor();
                af.moveMotorWithEncoder(.2, 2600, Constants.forward);

                */
                } else if (vuMark == RelicRecoveryVuMark.RIGHT) {

                    telemetry.addData("vuMark", vuMark);
                    telemetry.update();
                    found=true;
                    doDropper();
                    telemetry.addData("autonomous_left", "moving backward");
                    telemetry.update();
                    af.moveMotorWithEncoder(.05, 3000, Constants.left);
                    af.stopMotor();
                /*
                af.moveMotorWithEncoder(.2, 1300, Constants.spinLeft);
                af.stopMotor();
                af.moveMotorWithEncoder(.2, 2600, Constants.right);
                af.stopMotor();
                af.moveMotorWithEncoder(.2, 2600, Constants.forward);

                */
                } else {
                    telemetry.addData("VuMark", "not visible");

                }
            }
            telemetry.update();

        }
    }
}




