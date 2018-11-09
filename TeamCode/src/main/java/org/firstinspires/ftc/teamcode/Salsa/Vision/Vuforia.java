package org.firstinspires.ftc.teamcode.Salsa.Vision;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;
import org.firstinspires.ftc.teamcode.Salsa.Constants;
import org.firstinspires.ftc.teamcode.Salsa.Hardware.Robot;

/**
 * Created by adityamavalankar on 11/4/18.
 */

public class Vuforia {

    public HardwareMap hwmap = null;
    public Constants constants = new Constants();
    public Robot robot = new Robot();
    public OpenGLMatrix lastLocation = null;
    public VuforiaLocalizer vuforia = null;
    public int cameraMonitorViewId = hwmap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hwmap.appContext.getPackageName());
    public VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
    public VuforiaTrackables relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
    public VuforiaTrackable relicTemplate = relicTrackables.get(0);
    public RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);



    public void enableVuforia(CameraOrientation camDirection, CameraUsed cam) {
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = constants.VUFORIA_KEY;


        if (cam == CameraUsed.WEBCAM) {
            parameters.cameraName = robot.webcamFront;
        }

        else if (cam == CameraUsed.PHONE) {
            if (camDirection == CameraOrientation.FRONT) {
                parameters.cameraDirection = CameraDirection.FRONT;
            } else if (camDirection == CameraOrientation.BACK) {
                parameters.cameraDirection = CameraDirection.BACK;
            } else {
                parameters.cameraDirection = CameraDirection.BACK;
            }
        }

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

    }

    public void disableVuforia() {
        relicTrackables.deactivate();
    }

    public void beginTracking() {
        relicTrackables.activate();
    }


    public RelicRecoveryVuMark VuMark() {

        RelicRecoveryVuMark vumark = vuMark;

        return vumark;
    }

}
