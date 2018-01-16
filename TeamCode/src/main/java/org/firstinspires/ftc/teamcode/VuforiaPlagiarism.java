package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.vuforia.CameraDevice;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

public class VuforiaPlagiarism {
    public static final String TAG = "Vuforia VuMark Sample";
    Hardware750 robot = new Hardware750();
    VuforiaLocalizer vuforia;

    public enum type {
        LEFT, CENTER, RIGHT, ERROR
    }
    
    public type getVuf(HardwareMap hardwareMap) {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        
        parameters.vuforiaLicenseKey = Hardware750.VUF_LIC;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);
        
        VuforiaTrackables relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        VuforiaTrackable relicTemplate = relicTrackables.get(0);
        
        relicTemplate.setName("relicVuMarkTemplate");
        relicTrackables.activate();
        
        boolean bool = true;
        String string = "error";
        CameraDevice.getInstance().setFlashTorchMode(true);
        long time = System.currentTimeMillis();
        
        while ((bool) && (System.currentTimeMillis() < (time + 2000))) {
            RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);
            
            if (vuMark != RelicRecoveryVuMark.UNKNOWN) {
                if (vuMark == vuMark.CENTER) {
                    bool = false;
                    string = "CENTER";
                } else if (vuMark == vuMark.LEFT) {
                    bool = false;
                    string = "LEFT";
                } else if (vuMark == vuMark.RIGHT) {
                    bool = false;
                    string = "RIGHT";
                }
                
                //TODO: Find out if removing this will break anything
                OpenGLMatrix pose = ((VuforiaTrackableDefaultListener) relicTemplate.getListener()).getPose();
                if (pose != null) {
                    VectorF trans = pose.getTranslation();
                    Orientation rot = Orientation.getOrientation(pose, AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);
                }
            }
        } //End of while loop
        CameraDevice.getInstance().setFlashTorchMode(false);
        if (string.equals("LEFT")) {
            return type.LEFT;
        } else if (string.equals("RIGHT")) {
            return type.RIGHT;
        } else if (string.equals("CENTER")) {
            return type.CENTER;
        } else {
            return type.ERROR;
        }
    }
}
