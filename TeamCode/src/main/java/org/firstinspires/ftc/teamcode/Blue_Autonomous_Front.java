package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

// Created by Swagster_Wagster on 9/29/17
//
//
// Last edit: 10/22/17 BY MRINAAL RAMACHANDRAN


@Autonomous(name = "Blue_Autonomous_Front",group="We Love Pi")
public class Blue_Autonomous_Front extends LinearOpMode {

    Autonomous_Functions af = new Autonomous_Functions();

    VuforiaLocalizer vuforia;

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

        af.init(hardwareMap);

        waitForStart();

        af.dropper.setPosition(.5);
        //sense color
        // if color red turn that way
        af.moveMotorWithEncoder(.2, 1300, Constants.spinLeft);
        // if color blue turn other way
        af.moveMotorWithEncoder(.2, 1300, Constants.spinLeft);

        relicTrackables.activate();

        while (opModeIsActive()) {

            RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);

            if (vuMark == RelicRecoveryVuMark.LEFT) {

                af.moveMotorWithEncoder(.2, 2600, Constants.backward);
                af.stopMotor();
                af.moveMotorWithEncoder(.2,1300, Constants.spinLeft);
                af.stopMotor();
                af.moveMotorWithEncoder(.2, 2600, Constants.right);
                af.stopMotor();
                af.moveMotorWithEncoder(.2, 2600, Constants.forward);
                af.stopMotor();
            }

            else if (vuMark == RelicRecoveryVuMark.CENTER) {

                af.moveMotorWithEncoder(.2, 2600, Constants.backward);
                af.stopMotor();
                af.moveMotorWithEncoder(.2,1300, Constants.spinLeft);
                af.stopMotor();
                af.moveMotorWithEncoder(.2, 2600, Constants.right);
                af.stopMotor();
                af.moveMotorWithEncoder(.2, 2600, Constants.forward);
                af.stopMotor();
            }

            else if (vuMark == RelicRecoveryVuMark.RIGHT) {

                af.moveMotorWithEncoder(.2, 2600, Constants.backward);
                af.stopMotor();
                af.moveMotorWithEncoder(.2,1300, Constants.spinLeft);
                af.stopMotor();
                af.moveMotorWithEncoder(.2, 2600, Constants.right);
                af.stopMotor();
                af.moveMotorWithEncoder(.2, 2600, Constants.forward);
                af.stopMotor();

            }

            else {
                telemetry.addData("VuMark", "not visible");
            }

            telemetry.update();
        }
    }
}



