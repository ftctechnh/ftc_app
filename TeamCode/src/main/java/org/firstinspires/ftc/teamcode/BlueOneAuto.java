package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Auto Blue One", group="Linear Auto")

public class BlueOneAuto extends AutoMaster {
    @Override
    public void runOpMode() {
        robot.init(hardwareMap);
        waitForStart();
        telemetry.addData("skatin fast,", "eatin' ass");
        encodeInd(0.25, MoveType.LATERALLY);
        while (robot.rangeSensor.getDistance(DistanceUnit.INCH) > 10.5) {}
        robot.setallDriveMotors(0);
        wait(1000);
        robot.arm.setPosition(1);
        if (robot.color.red() > 1) {
            encode(5, -0.5, MoveType.STRAIGHT);
        } else {
            encode(5, 0.5, MoveType.STRAIGHT);
        }
    }
}
