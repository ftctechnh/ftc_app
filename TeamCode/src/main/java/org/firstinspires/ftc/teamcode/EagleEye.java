package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.vuforia.HINT;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.robotcore.internal.opengl.models.Teapot;

// Created by Swagster_Wagster on 9/30/17

// Last edit: 9/30/17 BY MRINAAL RAMACHANDRAN

// DEFAULT CODE FOR CAMERA SENSOR

@TeleOp(name = "EagleEye")
public class EagleEye extends LinearOpMode {

    String license_key = "AQBbCRX/////AAAAGevvCPApu0phjwrtg1hDMb5FkMizE8AaJCDJtXwc8y45ZcarRtpG2Edqj3dWfUCXGTucT4Ovr/Exh6ekwjyC6D+N59nTw2BdGx9VxVquX6vBsKl2acD9cfQOBkxSs6puRSAH/Pm3FMiP4AN/LXC+VedYtfIAE2UZmIF041Lr8sLs+wgephTto8kZ8ELxQZx98Q5T/RaLo3wkz5+jYksV5Pi8VRG0vFhk47fwa0gUrZDTFhq11og/bD9zZmLiFqH3tyHeMMSkYFVakMctRPNjKYPgi9iG4jlDjGbtq2a56QxTxzjBo/J/8K2PN2A6vnBw3tMc2E1KitBtPuKrD9h/5ACsIGWh2Zo5TiQC1YGTQr6F";

    @Override
    public void runOpMode() throws InterruptedException {

        VuforiaLocalizer.Parameters para = new VuforiaLocalizer.Parameters(R.id.cameraMonitorViewId);
        para.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        para.vuforiaLicenseKey = license_key;
        para.cameraMonitorFeedback = VuforiaLocalizer.Parameters.CameraMonitorFeedback.AXES;

        VuforiaLocalizer vuforia = ClassFactory.createVuforiaLocalizer(para);

        VuforiaTrackables image = vuforia.loadTrackablesFromAsset("RelicVuMark");

        image.get(0).setName("RelicRecovery");

        waitForStart();

        image.activate();

        while (opModeIsActive()) {

            for (VuforiaTrackable im : image) {

                im.getListener();

                OpenGLMatrix pose = ((VuforiaTrackableDefaultListener) im.getListener()).getPose();

                if (pose != null) {

                    VectorF translate = pose.getTranslation();

                    telemetry.addData(im.getName() + "translate", translate);

                    double degreeTurn = Math.toDegrees(Math.atan2(translate.get(1), translate.get(2)));

                    telemetry.addData(im.getName() + "Degrees", degreeTurn);

                }
            }
            telemetry.update();
        }
    }
}



