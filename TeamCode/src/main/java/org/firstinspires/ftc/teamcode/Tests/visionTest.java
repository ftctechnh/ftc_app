package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
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

/**
 * Created by Stephen Ogden on 9/10/17.
 * FTC 6128 | 7935
 * FRC 1595
 */

@TeleOp(name="visionTest", group ="Test")
@Disabled
// With flashlight, can detect .5 meters away
// Without phone flashlight, 1.1 meters away
public class visionTest extends LinearOpMode{
    public static final String TAG = "Vuforia VuMark Sample";
    OpenGLMatrix lastLocation = null;
    private VuforiaLocalizer vuforia;
    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initializing...");
        telemetry.update();
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        parameters.vuforiaLicenseKey = "AUgZTU3/////AAAAGaQ5yTo6EkZqvsH9Iel0EktQjXWAZUz3q3FPq22sUTrmsYCccs/mjYiflQBH2u7lofbTxe4BxTca9o2EOnNwA8dLGa/yL3cUgDGjeRfXuwZUCpIG6OEKhiPU5ntOpT2Nr5uVkT3vs2uRr7J6G7YoaGHLw2i1wGncRaw37rZyO03QRh0ZatdKIiK1ItuvJkP3qfUJwQwcpROwa+ZdDNQDbpU6WTL+kPZpnkgR8oLcu+Na1lWrbJ2ZTYG8eUjoIGowbVVGJgORHJazy6/7MbYH268h9ZC4vZ12ItyDK/GlPRTeQWdcZRlWfzAAFwNrjmdjWv9hMuOMoWxo2Y2Rw1Fwii4ohLyRmcQa/wAWY+AOEL14";
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);
        VuforiaTrackables relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        VuforiaTrackable relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate");
        telemetry.addData("Status", "Done! Press play to begin auto");
        telemetry.update();
        waitForStart();
        relicTrackables.activate();
        while (opModeIsActive()) {
            CameraDevice.getInstance().setFlashTorchMode(false);
            RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);
            if (vuMark != RelicRecoveryVuMark.UNKNOWN) {
                telemetry.addData("Status", "%s visible", vuMark);
                OpenGLMatrix pose = ((VuforiaTrackableDefaultListener)relicTemplate.getListener()).getPose();
                telemetry.addData("Pose", format(pose));
                if (pose != null) {
                    VectorF trans = pose.getTranslation();
                    Orientation rot = Orientation.getOrientation(pose, AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);
                    double tX = trans.get(0);
                    double tY = trans.get(1);
                    double tZ = trans.get(2);
                    double rX = rot.firstAngle;
                    double rY = rot.secondAngle;
                    double rZ = rot.thirdAngle;
                }
            } else {
                telemetry.addData("Status", "Target not visible!");
            }
            telemetry.update();
        }
        CameraDevice.getInstance().setFlashTorchMode(false);
    }
    private String format(OpenGLMatrix transformationMatrix) {
        return (transformationMatrix != null) ? transformationMatrix.formatAsTransform() : "null";
    }
}
