package org.firstinspires.ftc.teamcode.sabbotage.relic.autonomous.steps;


import android.util.Log;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.teamcode.sabbotage.relic.autonomous.internal.AutonomousOp;
import org.firstinspires.ftc.teamcode.sabbotage.relic.robot.Robot;

public class Step_ReadVuMark implements AutonomousOp.StepInterface {

    private Robot robot;
    private boolean initDoneFlag;
    private VuforiaLocalizer vuforia;
    private VuforiaTrackable relicTemplate;

    // Constructor, called to create an instance of this class.
    public Step_ReadVuMark() {
    }


    @Override
    public String getLogKey() {
        return "Step_ReadVuMark";
    }


    private void initVuforia_runOnlyOnce() {
        if (initDoneFlag) {
            return;
        }
        initDoneFlag = true;

        HardwareMap hardwareMap = robot.getHardwareMap();
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        parameters.vuforiaLicenseKey = "Ae+VP7r/////AAAAGcWRUn9OeEh1st3FScOJjHMoqqrCaXtOASzaWFZSG7+3NM5OXqiV4+vrwsDpomiOtR15TGLxNXM5svP6p8n42RC5jSxy/JoOEjuW9uKygwnmqG8iCSUCkzugHXIr8Mm2Q9zMXAEHGJ+rsHOLPQjRuwBWWvU9hVZmCoqFDGKenN0GWyENvivGXwm5j4pBHO7hx8Vd6MSdP17OZKQhu2mB75kt9CNv/VmiExhyesVzlfaErgeos5fbNcwV8xzxeo+/5Bps6PnHDIAA1gOzSJ9xpFDSYu2UFj3P+WRd86EK31jAKbs/9OXdrPpr6zvlag4aGxQOt3sMkSg4X31qJNpDJCxNHfVIKT+G6m96xbfU0YqG";
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);

        VuforiaTrackables relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        this.relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate"); // can help in debugging; otherwise not necessary

        relicTrackables.activate();


    }


    @Override
    public void runStep() {

        initVuforia_runOnlyOnce();

    }

    @Override
    public boolean isStepDone() {


        if (hasFoundVuMark()) {

            logIt("Step is Done:");
            return true;
        }

        return false;
    }


    private void logIt(String methodName) {

        StringBuilder sb = new StringBuilder();
        sb.append(methodName);
        sb.append(" CurrentPosition:" + robot.motorDriveLeft.getCurrentPosition());
        sb.append(" Target:" + robot.motorDriveLeft.getTargetPosition());
        Log.i(getLogKey(), sb.toString());

    }

    private boolean hasFoundVuMark() {

        RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(this.relicTemplate);


        if (vuMark != RelicRecoveryVuMark.UNKNOWN) {
            Log.i(getLogKey(), "VuMark FOUND:" + vuMark);
            robot.setVuMark(vuMark);
            return true;
        }
        Log.i(getLogKey(), "VuMark not visible");
        return false;


    }

    @Override
    public boolean isAborted() {
        return false;
    }

    private String format(OpenGLMatrix transformationMatrix) {
        return (transformationMatrix != null) ? transformationMatrix.formatAsTransform() : "null";
    }

    @Override
    public void setRobot(Robot robot) {
        this.robot = robot;
    }


}
