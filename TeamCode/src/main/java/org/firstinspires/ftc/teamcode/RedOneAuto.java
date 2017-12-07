package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="Auto Red One", group="Linear Auto")

public class RedOneAuto extends AutoMaster {
    @Override
    public void runOpMode() {
        robot.init(hardwareMap);
        waitForStart();
        telemetry.addData("skatin fast,", "eatin' ass");
        encode(5, -0.5, MoveType.STRAIGHT);
        robot.arm.setPosition(1);
        wait(1000);
        if (robot.color.red() > 1) {
            encode(5, -0.25, MoveType.LATERALLY);
        } else {
            encode(5, 0.25, MoveType.LATERALLY);
        }
        robot.arm.setPosition(0);
        encode(15, 0.5, MoveType.STRAIGHT);
        encode(20, 0.5, MoveType.ROT);
        encode(40, 0.5, MoveType.STRAIGHT);
        wait(500);
        robot.gripper.setPower(-0.25);
        wait(1000);
        robot.gripper.setPower(0);
        encode(2, -0.25, MoveType.STRAIGHT);
    }
}