package org.firstinspires.ftc.teamcode.TestOpModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

/**
 * Created by pston on 1/7/2018
 */

public class VuforiaTest extends OpMode {

    OpenGLMatrix lastLocation = null;
    VuforiaLocalizer vuforia;

    VuforiaTrackable relicTemplate;
    VuforiaTrackables relicTrackables;

    @Override
    public void init() {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

        parameters.vuforiaLicenseKey = "AUkgO0T/////AAAAGaYeMjdF+Us8tdP9fJcRhP9239Bwgzo0STjrR4II0s58wT/ja6GlSAQi/ptpHERhBhdNq8MMmlxC6bjyebsGnr/26IxYKhFFdC67Q7HE0jhDrsrEfxfJMFnsk2zSdt5ofwm2Z1xNhdBg2kfFCzdodI7aHFEdUQ6fddoTioTSPu9zzU9XqBr7Ra+5mTaIwp10heZmlXIjWfu8220ef/tZQ8QSmDX1GSqRLBjUJspesff8Nv9pkQAK3Nvp8YFHKJoFNkSV7QJW7mi/liHYq6DxYqhWk977WYGwzhHA003HNV4OUWhTLJGiPsiFhAlcJVbnVMn6ldnsSauT4unjXA9VBIzaYtSJc29UJYmWyin3MxPz";

        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);

        relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate");
    }

    @Override
    public void loop() {
        RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);
        telemetry.addData("Column:", vuMark);
    }
}
