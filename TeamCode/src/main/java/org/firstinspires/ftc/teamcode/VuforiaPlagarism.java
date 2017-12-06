package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcontroller.external.samples.ConceptVuforiaNavigation;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuMarkInstanceId;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

@Autonomous(name="Vuforia", group ="Concept")
public class VuforiaPlagarism extends LinearOpMode {

    public static final String TAG = "Vuforia VuMark Sample";

    Hardware750 robot = new Hardware750();

    OpenGLMatrix lastLocation = null;

    VuforiaLocalizer vuforia;

    public enum type {
        LEFT, CENTER, RIGHT, ERROR
    }

    public type getVuf(HardwareMap hardwareMap) {

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

        parameters.vuforiaLicenseKey = "AbQfkoj/////AAAAGURTD1LwoUjKk6qgxygb/6QTHah6F5/HMfF99SDO7C7wnhjBctp6i+bm/mX4El1OTHR8wW0gGjoM4qNsfM3cgFiMDHE4/IBhgpc2siB6nwrgEVZbo3PwJ0xImdXvTSEfWn8Fc6g+svSUFb97VAyjVAEsOvMC+sSqpjIKEQLoCdbCpLRmnX+9socxkX5qix9OVb0xREGbTtddp2fwtLleMXMHxUwhsTc3q7vqD5LDK7Q8GxOaV9jyB6/3Y3T65qaWOGjlGo39Ts394+WTp4hqwqvuu0Gkztlk2e6IeJbN9sN1+8xb2XQllnrHeBhIXxaoES1MRkyjMHliwQxbRJv8kwPeY9q/AsOA/dUy1x87iZLp";

        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);


        VuforiaTrackables relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        VuforiaTrackable relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate"); // can help in debugging; otherwise not necessary

        telemetry.addData(">", "Press Play to start");
        telemetry.update();
        waitForStart();

        relicTrackables.activate();

        boolean bool = true;
        String string = "error";
        while (bool) {

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

                telemetry.addData("VuMark", "%s visible", vuMark);

                OpenGLMatrix pose = ((VuforiaTrackableDefaultListener) relicTemplate.getListener()).getPose();
                telemetry.addData("Pose", format(pose));

                if (pose != null) {
                    VectorF trans = pose.getTranslation();
                    Orientation rot = Orientation.getOrientation(pose, AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);

                    // Extract the X, Y, and Z components of the offset of the target relative to the robot
                    double tX = trans.get(0);
                    double tY = trans.get(1);
                    double tZ = trans.get(2);

                    // Extract the rotational components of the target relative to the robot
                    double rX = rot.firstAngle;
                    double rY = rot.secondAngle;
                    double rZ = rot.thirdAngle;

                    telemetry.addData("tX", tX);
                    telemetry.addData("tY", tY);
                    telemetry.addData("tZ", tZ);
                    telemetry.addData("rX", rX);
                    telemetry.addData("rY", rY);
                    telemetry.addData("rZ", rZ);

                }
            } else {
                telemetry.addData("VuMark", "not visible");
            }
        }
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
        String format (OpenGLMatrix transformationMatrix){
            return (transformationMatrix != null) ? transformationMatrix.formatAsTransform() : "null";
        }

    public void runOpMode() {

    }
}
