package org.firstinspires.ftc.teamcode.opmodes.test;

import android.content.Context;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import static org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark.UNKNOWN;
import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.BACK;
import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.Parameters;


/**
 * Created by Derek on 12/7/2017.
 */

@TeleOp(name="VuMarkTest")
public class TestVuMark extends OpMode {


    private OpenGLMatrix lastLocation = null;
    private Context appContext;
    private VuforiaLocalizer vuforia;
    private Parameters parameters;
    private int cameraId;
    private VuforiaTrackable template;

    @Override
    public void init() {
        appContext = hardwareMap.appContext;
        cameraId = appContext.getResources().getIdentifier("cameraMonitorViewId", "id", appContext.getPackageName());
        parameters = new Parameters(cameraId);
        parameters.vuforiaLicenseKey = "AbiBqmz/////AAAAGUn62yKAi0hClNIjdOpIi4w3rYNs9YZqxBchLDdT72TDaBXOG1tW43YVVoO4ksL2nrwpqhH8jgf+4utsxCPEkmu1/YSiBYV29PTLMQEAid3KY6kAArYdMybcfcJlwxRU16OSQGEU/zdINDGcubBRyYokjfcWEFzSsl1YfeCWdldio5Vh5aMNgfcylUINBUtU7Ts5xtlxoV5+k1nYzXdBZT5m7Qic+wo6meRj+ITuVr/O+yOjHOBGLk+2mcrKspu2YfdrCpqVUMKv8I2+zuLxTqyVohezStZGrEOhXBavIQAMXO/hu3LotQFrzgw9icKcR4D4awtdCysTsUcdF+qGWr0Au59341gtmsqqySrgzfTc.3.3";
        parameters.cameraDirection = BACK;
        vuforia = ClassFactory.createVuforiaLocalizer(parameters);
        VuforiaTrackables relics = vuforia.loadTrackablesFromAsset("RelicVuMark");
        template = relics.get(0);
        template.setName("RelicVuMarkTemplate");

        //start looking for the pictographs
        relics.activate();
    }

    @Override
    public void loop() {
        RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(template);

        if (!vuMark.equals(UNKNOWN)) {

            telemetry.addData("Vumark: ", vuMark.name());
        }
    }
}
