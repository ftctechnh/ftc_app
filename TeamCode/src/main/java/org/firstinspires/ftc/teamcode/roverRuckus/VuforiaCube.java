package org.firstinspires.ftc.teamcode.roverRuckus;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;
import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.BACK;

@TeleOp (name = "VuforiaCube", group = "test")
public class VuforiaCube extends LinearOpMode {
    //
    private static final String VUFORIA_KEY = "AbxR5+T/////AAAAGR1YlvU/6EDzrJvG5EfPnXSFutoBr1aCusr0K3pKqPuWTBQsUb0mv5irjoX2Xf/GFvAvHyw8v1GBYgHwE+hNTcNj05kw3juX+Ur4l3HNnp5SfXV/8fave0xB7yVYZ/LBDraNnYXiuT+D/5iGfQ99PVVao3LI4uGUOvL9+3vbPqtTXLowqFJX5uE7R/W4iLmNqHgTCSzWcm/J1CzwWuOPD252FDE9lutdDVRri17DBX0C/D4mt6BdI5CpxhG6ZR0tm6Zh2uvljnCK6N42V5x/kXd+UrBgyP43CBAACQqgP6MEvQylUD58U4PeTUWe9Q4o6Xrx9QEwlr8v+pmi9nevKnmE2CrPPwQePkDUqradHHnU";
    //
    private static final VuforiaLocalizer.CameraDirection CAMERA_CHOICE = BACK;
    //
    private OpenGLMatrix lastLocation = null;
    public VectorF translation = null;
    private boolean targetVisible = false;
    public Orientation orientation = null;
    private static final float mmPerInch        = 25.4f;
    //
    VuforiaLocalizer vuforia;
    //
    public void runOpMode(){
        //
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        //
        telemetry.addData("Part", "1");
        telemetry.update();
        sleep(10000);
        //
        VuforiaTrackables bloackTrackies = this.vuforia.loadTrackablesFromAsset("block1");
        //
        telemetry.addData("Part", "2");
        telemetry.update();
        sleep(10000);
        //
        VuforiaTrackable blockTrackable = bloackTrackies.get(0);
        //
        telemetry.addData("Part", "3");
        telemetry.update();
        sleep(10000);
        //
        telemetry.addData(">", "Press Play to start tracking");
        telemetry.update();
        waitForStartify();
        //
        bloackTrackies.activate();
        //
        while (opModeIsActive()){
            if (((VuforiaTrackableDefaultListener)blockTrackable.getListener()).isVisible()) {
                //
                OpenGLMatrix robotLocationTransform = ((VuforiaTrackableDefaultListener)blockTrackable.getListener()).getUpdatedRobotLocation();
                if (robotLocationTransform != null) {
                    lastLocation = robotLocationTransform;
                    translation = lastLocation.getTranslation();
                    orientation = Orientation.getOrientation(lastLocation, EXTRINSIC, XYZ, DEGREES);
                }
                //
            }
            telemetry.addData("Cube?", lastLocation != null);
            telemetry.addData("Pos (in)", "{X, Y, Z} = %.1f, %.1f, %.1f",
                    translation.get(0) / mmPerInch, translation.get(1) / mmPerInch, translation.get(2) / mmPerInch);
            telemetry.addData("Rot (deg)", "{Roll, Pitch, Heading} = %.0f, %.0f, %.0f", orientation.firstAngle, orientation.secondAngle, orientation.thirdAngle);
            telemetry.update();
            //
        }
    }
    //
    public void waitForStartify(){
        waitForStart();
    }
}
