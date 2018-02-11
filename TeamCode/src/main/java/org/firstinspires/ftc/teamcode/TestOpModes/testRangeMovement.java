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

    private boolean canMove1 = false;
    private boolean canMove2 = false;

    @Override
    public void init() {
        robot = new Robot(hardwareMap, telemetry, false);
    }

    @Override
    public void loop() {
        if (canMove1) {
            canMove1 = !robot.columnMove(RelicRecoveryVuMark.CENTER, AllianceColor.RED, telemetry);
        } else {
            canMove1 = gamepad1.a;
        }

        if (canMove2) {
            canMove2 = !robot.columnDrive(AllianceColor.RED, telemetry, 2);
        } else {
            canMove2 = gamepad1.b;
        }
    }
}
