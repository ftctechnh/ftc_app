package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Mechanisms.SparkyTheRobot;

@TeleOp(name="Sparky Arm Test", group="AAA")
@Config
public class SparkyArmTest extends LinearOpMode {
    public static double HEADING_INTERVAL = Math.PI / 4;
    public static double EXTEND_POWER = 0.8;

    SparkyTheRobot robot;

    @Override
    public void runOpMode() {

        robot = new SparkyTheRobot(this);
        robot.init(false);

        // TODO maybe tune these?
        robot.leftFlipper.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.rightFlipper.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        waitForStart();

        while (opModeIsActive()) {

        }
    }
}
