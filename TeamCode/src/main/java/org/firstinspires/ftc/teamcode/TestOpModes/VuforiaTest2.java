package org.firstinspires.ftc.teamcode.TestOpModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.teamcode.GMR.Robot.Robot;
import org.firstinspires.ftc.teamcode.GMR.Robot.SubSystems.AllianceColor;

/**
 * Created by FTC 4316 on 1/14/2018
 */

@TeleOp(name = "Vuforia Test", group = "test")
public class VuforiaTest2 extends OpMode {
    private OpenGLMatrix lastLocation = null;
    private VuforiaLocalizer vuforia;

    private VuforiaTrackables relicTrackables;
    private VuforiaTrackable relicTemplate;
    private VuforiaLocalizer.Parameters parameters;

    private Robot robot;

    @Override
    public void init() {
        robot = new Robot(hardwareMap, telemetry, true);

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
    }

    @Override
    public void loop() {
        telemetry.addData("Pictograph:", robot.vision.detectPictograph());
        telemetry.addData("Column number (Red):", robot.vision.keyColumnDetect(AllianceColor.RED));
        telemetry.update();
    }
}
