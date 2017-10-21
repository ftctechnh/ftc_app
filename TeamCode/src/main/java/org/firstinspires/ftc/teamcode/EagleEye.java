package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;


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
        para.cameraMonitorFeedback = VuforiaLocalizer.Parameters.CameraMonitorFeedback.TEAPOT;

        VuforiaLocalizer vuforia = ClassFactory.createVuforiaLocalizer(para);

        VuforiaTrackables image = vuforia.loadTrackablesFromAsset("FTC_IMAGE_REG");

        telemetry.addData("size is ", image.size());
        image.get(0).setName("RIGHT");
        image.get(1).setName("CENTER");
        image.get(2).setName("LEFT");
        telemetry.addData("name is ", image.get(0).getName());
        telemetry.addData("name is ", image.get(1).getName());
        telemetry.addData("name is ", image.get(2).getName());

        waitForStart();

        image.activate();

        while (opModeIsActive()) {

            for (VuforiaTrackable im : image) {

                im.getListener();

                OpenGLMatrix pose = ((VuforiaTrackableDefaultListener) im.getListener()).getPose();

                if (pose != null) {

                    VectorF translate = pose.getTranslation();

                    //telemetry.addData(im.getName() + "translate", translate);

                    double degreeTurn = Math.toDegrees(Math.atan2(translate.get(1), translate.get(2)));

                    //telemetry.addData(im.getName() + "Degrees", degreeTurn);

                    if (im.getName() == "RIGHT") {

                        telemetry.addData("I AM THE RIGHT IMAGE", "YEET");
                        telemetry.update();
                    }

                    if (im.getName() == "CENTER") {

                        telemetry.addData("I AM THE CENTER IMAGE", "YEET");
                        telemetry.update();
                    }

                    if (im.getName() == "LEFT") {

                        telemetry.addData("I AM THE LEFT IMAGE", "YEET");
                        telemetry.update();
                    }

                }

            }
            telemetry.update();
        }
    }
}



