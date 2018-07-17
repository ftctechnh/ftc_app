package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name = "legacy_auto", group = "LinearOpMode")

public class CRI_Auto extends LinearOpMode {

    private Robot robot = new Robot();

    @Override
    public void runOpMode() {
        robot.init(hardwareMap, this);

        waitForStart();

        robot.gyroTurn(90, 0.3);
        sleep(5000);

    }
}
