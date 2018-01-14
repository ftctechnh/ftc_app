package org.firstinspires.ftc.teamcode.TestOpModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.teamcode.GMR.Robot.Robot;
import org.firstinspires.ftc.teamcode.GMR.Robot.SubSystems.AllianceColor;

/**
 * Created by pston on 1/14/2018
 */

@TeleOp(name = "Range Column Test", group = "test")
public class testRangeMovement extends OpMode {

    private Robot robot;

    private boolean canMove = false;

    @Override
    public void init() {
        robot = new Robot(hardwareMap, telemetry);
    }

    @Override
    public void loop() {
        if (canMove) {
            canMove = !robot.columnMove(RelicRecoveryVuMark.CENTER, AllianceColor.RED, telemetry);
        } else {
            canMove = gamepad1.a;
        }
    }
}
