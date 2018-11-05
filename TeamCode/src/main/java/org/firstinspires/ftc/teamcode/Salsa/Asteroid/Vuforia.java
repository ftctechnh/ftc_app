package org.firstinspires.ftc.teamcode.Salsa.Asteroid;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.Const;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

/**
 * Created by adityamavalankar on 11/4/18.
 */

public class Vuforia {

    public HardwareMap hwmap = null;
    Hardware hardware = new Hardware();
    Constants constants = new Constants();
    OpenGLMatrix lastLocation = null;
    VuforiaLocalizer vuforia = null;
    int cameraMonitorViewId = hwmap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hwmap.appContext.getPackageName());
    VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
    VuforiaTrackables relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
    VuforiaTrackable relicTemplate = relicTrackables.get(0);
    RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);



    public void enableVuforia() {
        parameters.vuforiaLicenseKey = constants.VUFORIA_KEY;
        relicTemplate.setName("relicVuMarkTemplate");
        vuforia = ClassFactory.getInstance().createVuforia(parameters);
    }

    public void disableVuforia() {
        relicTrackables.deactivate();
    }

    public void beginTracking() {
        relicTrackables.activate();
    }

    public void setCameraDirection(Direction cameraDirecrtion){
        if (cameraDirecrtion == Direction.FRONT) {
            parameters.cameraDirection = VuforiaLocalizer.CameraDirection.FRONT;
        }
        else if (cameraDirecrtion == Direction.BACK) {
            parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        }
    }

    public void setCamera(Camera camera){
        if (camera == Camera.WEBCAM) {
            parameters.cameraName = hardware.webcamFront;
        }
        else {
            parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        }
    }

    public RelicRecoveryVuMark VuMark() {

        RelicRecoveryVuMark vumark = vuMark;

        return vumark;
    }

}

enum Direction {
    FRONT,
    BACK;
}

enum Camera {
    WEBCAM,
    PHONE;
}
