package org.firstinspires.ftc.team2981.structural;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

/**
 * Created by 200462069 on 10/14/2017.
 */

public class Vision {

    OpenGLMatrix lastLocation = null;
    VuforiaLocalizer vuforia;
    VuforiaTrackables trackables;
    VuforiaTrackable relic;
    VuforiaLocalizer.Parameters parameters;

    public Vision(HardwareMap hardwareMap){
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        //parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

        parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = "AaDxUz3/////AAAAGSJYyeGWDU3Im6rx85GbYy5gq8Raja7mkDAmDCUs6BvNAusEF4omrCaZUFeEG7gyW/Sq1exxlBmowJ4IY2ICrleyyxb1XJaFw0IsYhuBzESI/duL9SW2gVXcULoqBd7q4wniSHWZNNlkMYiuSbaW6z7299VJzV0QEi0HugnY+5PhZHUts9CU+lGIukrkAIDWP5bXOEmERBRpl4XKWIviWeCGHiVQVwAjeBEPnX1fsqRf+178gAoXEXDanp9cHriUGyU4a0vqhvJyb2LoQG5NrNLoFGUMU45pTWdjjY8TuVv9sfYSVwcboP2vzFeh8TVBbQRJrNrdWiRbw35nn+JSrZY6ulR5ZSDTq7l1apzxTy/s\n";
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;

        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);

        trackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        relic = trackables.get(0);
        relic.setName("relicVuMarkTemplate");
    }

    public void start(){
        trackables.activate();
    }

    public void stop(){
        trackables.deactivate();
    }

    public RelicRecoveryVuMark track(){
        return RelicRecoveryVuMark.from(relic);
    }
}
