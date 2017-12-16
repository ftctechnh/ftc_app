package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Autonomous(name="Auto Red One", group="Linear Auto")

public class RedOneAuto extends AutoMaster {
    @Override
    public void runOpMode() {
        VuforiaPlagiarism vu = new VuforiaPlagiarism();
        double box;
        VuforiaPlagiarism.type typee = VuforiaPlagiarism.type.ERROR;
        robot.init(hardwareMap);
        waitForStart();
        telemetry.addData("skatin fast,", "eatin' ass");
        encodeInd(0.25, MoveType.LATERALLY);
        while (robot.rangeSensor.getDistance(DistanceUnit.INCH) < 10.5) {}
        robot.setAllDriveMotors(0);
        wait(500);
        robot.arm.setPosition(1);
        wait(750);
        if (robot.color.red() > 1) {
            encode(5, -0.25, MoveType.STRAIGHT);
        } else {
            encode(5, 0.25, MoveType.STRAIGHT);
        }
        robot.arm.setPosition(0);
        encodeInd(-0.5, MoveType.LATERALLY);
        while (robot.rangeSensor.getDistance(DistanceUnit.INCH) < 20) {}
        robot.setAllDriveMotors(0);

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
        /*encodeInd(-0.25, MoveType.LATERALLY);
        while (robot.rangeSensor.getDistance(DistanceUnit.INCH) < 20) {}
        robot.setAllDriveMotors(0);*/
        encode(19, 0.25, MoveType.ROT);
        findBox(box, -1);
        encode(15, 0.5, MoveType.STRAIGHT);
        wait(500);
        robot.gripper.setPower(-0.25);
        wait(1000);
        robot.gripper.setPower(0);
        encode(2, -0.25, MoveType.STRAIGHT);
    }
}
