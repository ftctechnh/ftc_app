package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.vuforia.HINT;
import com.vuforia.Vuforia;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

/**
 * Created by ethan on 2017-09-11.
 */
@TeleOp()

public class VuforiaTest extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException{
        //create vuforia params
        VuforiaLocalizer.Parameters params = new VuforiaLocalizer.Parameters(R.id.cameraMonitorViewId);

        //set param values
        params.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        params.vuforiaLicenseKey = "AbuDM6D/////AAAAGYYH2C9DTUzXt5vADIJYK4FhcJkx8VccnquSTg67efZ/JSLjC+qx1o+QV7idL5MYhjqswZglFenXN6KjsmP5lAUbShN6M0EvhpGdIoDx1f6zzUiiQts0GAswRyGwi4VJa+m8UMm8+ZJXi8k56X4gdn0KOr216AA0O0oqcMdIXc9A6q2KW5IlzvrTId8jZy8lLKrQStrJtUHtlqe5d2RT/gY7i+wIZz+aVTAvAdisMgCFsYWmK9IJdo3dPmrMVOFlhZvvgchEwYDKIJ6axGekXX8u2MAFl/hEuAJWpuoYa0/VzZ/JeM61dj6VpHsZaxC0BJMRG0ypQWWxbrJg0hjcjCdNhcMP6JSkNLU5AmTPDdC9";
        params.cameraMonitorFeedback = VuforiaLocalizer.Parameters.CameraMonitorFeedback.AXES;

        //create VuforiaLocalizer
        VuforiaLocalizer vuforia = ClassFactory.createVuforiaLocalizer(params);
        Vuforia.setHint(HINT.HINT_MAX_SIMULTANEOUS_IMAGE_TARGETS, 3);

        VuforiaTrackables cards = vuforia.loadTrackablesFromAsset("Cards");

        cards.get(0).setName("Three");
        cards.get(1).setName("Eight");
        cards.get(2).setName("Five");

        waitForStart();

        cards.activate();

        while (opModeIsActive()){
            for (VuforiaTrackable card : cards){
                OpenGLMatrix pose = ((VuforiaTrackableDefaultListener) card.getListener()).getPose();

                if (pose != null){
                    VectorF translation = pose.getTranslation();
                    telemetry.addData(card.getName() + "-Translation", translation);

                    double degreesToTurn = Math.toDegrees(Math.atan2(translation.get(1), translation.get(2)));
                    telemetry.addData(card.getName()+"-Degrees", degreesToTurn);

                }
            }
            telemetry.update();
        }
    }

}
