package org.firstinspires.ftc.team6417;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
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

@Autonomous(name = "Concept: NullOp", group = "Concept")
@Disabled
public class ConceptEventOp extends OpMode {

    private ElapsedTime runtime = new ElapsedTime();

    //vuforia initialization
    VuforiaLocalizer vuforia;

    VuforiaTrackables relicTrackables;
    VuforiaTrackable relicTemplate;

    @Override
    public void init() {

        telemetry.addData("Status", "Initialized");

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

        parameters.vuforiaLicenseKey = "AcWxpI//////AAABmSTUqOAeSU6ch+3XHmV8XvMbVbpRCaYwby/IOcqjvubbwTl3h6QgRRr5Sq8xDIqb44SiaO40EwykSMPj8lExZdBYpKxDIed6HDzF0bnyn8EOMvMwtw8y7qlfdoogz+XZ0QBLuYQJOtM6LoCNPVFikbunJRj72Pfty7C8WzzjaGmlZKjHWZamkhck6OvK6E1tlhRzwnPhFMDGfLFq/6clnV2RIo1CM8QJ0SJln+d28b4IjHp4FR6Ihl1PZz995tXGZE+cssZdTnIO3mX62f16wkSXVaQUxiPRIAJCGHExH1WkBge7d6r1pKLKmNOwwdrXLTP5WbOR2Mi5Qw9Lq6ATDBrseIVVnHXbgHbR9TaQXg8R";

        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.FRONT;
        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);

        relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate"); // can help in debugging; otherwise not necessary

    }

    @Override
    public void start() {

        runtime.reset();

        relicTrackables.activate();

    }

    @Override
    public void loop() {

        RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);
        if (vuMark != RelicRecoveryVuMark.UNKNOWN) {

            OpenGLMatrix pose = ((VuforiaTrackableDefaultListener) relicTemplate.getListener()).getPose();
            if (vuMark == RelicRecoveryVuMark.RIGHT)
                telemetry.addData("VuMark", "Right");
            else if (vuMark == RelicRecoveryVuMark.CENTER)
                telemetry.addData("VuMark", "Center");
            else if (vuMark == RelicRecoveryVuMark.LEFT)
                telemetry.addData("VuMark", "Left");
            else if (vuMark == RelicRecoveryVuMark.UNKNOWN)
                telemetry.addData("VuMark", "Unknown");

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
            }
        } else {
            telemetry.addData("VuMark", "not visible");
        }

        telemetry.update();
    }
}