package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Auto Blue One", group="Linear Auto")

public class BlueOneAuto extends AutoMaster {
    @Override
    public void runOpMode() {
        double box;
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
        robot.arm.setPosition(0);

        typee = vu.getVuf(hardwareMap);
        if (typee == VuforiaPlagiarism.type.RIGHT) {
            box = 38.98;
        } else if (typee == VuforiaPlagiarism.type.CENTER) {
            box = 45.67;
        } else if (typee == VuforiaPlagiarism.type.LEFT) {
            box = 45.7;
        } else {
            box = 45.67;
        }
        
    }
}
