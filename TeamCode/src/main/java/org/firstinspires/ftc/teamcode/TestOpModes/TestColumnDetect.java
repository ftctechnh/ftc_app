package org.firstinspires.ftc.teamcode.TestOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.GMR.Robot.Robot;
import org.firstinspires.ftc.teamcode.GMR.Robot.SubSystems.AllianceColor;

/**
 * Created by pston on 2/11/2018
 */
@Autonomous(name = "Test Column Detection", group = "test")
public class TestColumnDetect extends OpMode {

    private Robot robot;

    private boolean hasFinished = false;

    @Override
    public void init() {
        robot = new Robot(hardwareMap, telemetry, false);
    }

    @Override
    public void loop() {
        if (!hasFinished) {
            hasFinished = robot.columnDrive(AllianceColor.BLUE, telemetry, 3);
        } else {
            robot.driveTrain.stop();
        }
    }
}
