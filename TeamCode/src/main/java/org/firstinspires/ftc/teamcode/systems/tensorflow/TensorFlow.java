package org.firstinspires.ftc.teamcode.systems.tensorflow;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.systems.base.System;

import java.util.ArrayList;
import java.util.List;

public class TensorFlow extends System
{
    /**
     * {@link #vuforia} is the variable we will use to store our instance of the Vuforia
     * localization engine.
     */
    private VuforiaLocalizer vuforia;

    /**
     * {@link #tensorFlowObjectDetector} is the variable we will use to store our instance of the Tensor Flow Object
     * Detection engine.
     */
    private TFObjectDetector tensorFlowObjectDetector;
    private static final String VUFORIA_KEY = "AfIW5rj/////AAAAGaDrYjvjtkibrSYzQTjEFjJb+NGdODG1LJE2IVqxl0wdLW+9JZ3nIyQF2Hef7GlSLQxR/6SQ3pkFudWmzU48zdcBEYJ+HCwOH3vKFK8gJjuzrcc7nis7JrU+IMTONPctq+JTavtRk+LBhM5bxiFJhEO7CFnDqDDEFc5f720179XJOvZZA0nuCvIqwSslb+ybEVo/G8BDwH1FjGOaH/CxWaXGxVmGd4zISFBsMyrwopDI2T0pHdqvRBQ795QCuJFQjGQUtk9UU3hw/E8Z+oSC36CSWZPdpH3XkKtvSb9teM5xgomeEJ17MdV+XwTYL0iB/aRXZiXRczAtjrcederMUrNqqS0o7XvYS3eW1ViHfynl";

    public TensorFlow(OpMode opMode) {
        super(opMode, "TensorFlow");
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minimumConfidence = 0.95;
        tensorFlowObjectDetector = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
    }

    public void loadModelFromAsset(String assetFile, String label) {
        tensorFlowObjectDetector.loadModelFromAsset(assetFile, label);
    }

    public void activate() {
        if (tensorFlowObjectDetector != null) {
            tensorFlowObjectDetector.activate();
        }
    }

    public List<Recognition> getUpdatedRecognitions() {
        List<Recognition> recognitionList = tensorFlowObjectDetector.getUpdatedRecognitions();
        if (recognitionList == null) {
            return new ArrayList<Recognition>();
        } else {
            return recognitionList;
        }
    }

    public void shutDown() {
        if (tensorFlowObjectDetector != null) {
            tensorFlowObjectDetector.shutdown();
        }
    }
}
