package org.firstinspires.ftc.teamcode.roverRuckus;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.BACK;

@TeleOp (name = "VuforiaCube", group = "test")
public class VuforiaCube extends LinearOpMode {
    //
    private static final String VUFORIA_KEY = "AbxR5+T/////AAAAGR1YlvU/6EDzrJvG5EfPnXSFutoBr1aCusr0K3pKqPuWTBQsUb0mv5irjoX2Xf/GFvAvHyw8v1GBYgHwE+hNTcNj05kw3juX+Ur4l3HNnp5SfXV/8fave0xB7yVYZ/LBDraNnYXiuT+D/5iGfQ99PVVao3LI4uGUOvL9+3vbPqtTXLowqFJX5uE7R/W4iLmNqHgTCSzWcm/J1CzwWuOPD252FDE9lutdDVRri17DBX0C/D4mt6BdI5CpxhG6ZR0tm6Zh2uvljnCK6N42V5x/kXd+UrBgyP43CBAACQqgP6MEvQylUD58U4PeTUWe9Q4o6Xrx9QEwlr8v+pmi9nevKnmE2CrPPwQePkDUqradHHnU";
    //
    private static final VuforiaLocalizer.CameraDirection CAMERA_CHOICE = BACK;
    //
    private OpenGLMatrix lastLocation = null;
    private boolean targetVisible = false;
    //
    VuforiaLocalizer vuforia;
    //
    public void runOpMode(){
        //
        VuforiaTrackables bloackTrackies = this.vuforia.loadTrackablesFromAsset("Block_OT");
        VuforiaTrackable blockTrackable = bloackTrackies.get(0);
        //
        telemetry.addData(">", "Press Play to start tracking");
        telemetry.update();
        waitForStartify();
        //
        bloackTrackies.activate();
        //
        while (opModeIsActive()){
            /*if (((VuforiaTrackableDefatListener)blockTrackable.getListener()).isVisible()) {
                //
                OpenGLMatrix robotLocationTransform = ((VuforiaTrackableDefaultListener)blockTrackable.getListener()).getUpdatedRobotLocation();
                if (robotLocationTransform != null) {
                    lastLocation = robotLocationTransform;
                }
                //

            }*/
            telemetry.addData("VuMark", "");
            telemetry.update();
        }
    }
    //
    public void waitForStartify(){
        waitForStart();
    }
}
