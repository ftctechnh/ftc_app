package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Auto Blue Two", group="Linear Auto")

public class BlueTwoAuto extends AutoMaster {
    @Override
    public void runOpMode() {
        VuforiaPlagiarism vu = new VuforiaPlagiarism();
        VuforiaPlagiarism.type typee = VuforiaPlagiarism.type.ERROR;
        robot.init(hardwareMap);
        waitForStart();
        telemetry.addData("skatin fast,", "eatin' ass");
        encodeInd(0.25, MoveType.LATERALLY);
        while (robot.rangeSensor.getDistance(DistanceUnit.INCH) > 11.2) {}
        robot.setAllDriveMotors(0);
        wait(1000);
        robot.arm.setPosition(1);
        //TODO: Find out if it goes the right way
        if (robot.color.red() > 1) {
            encode(5, 0.5, MoveType.STRAIGHT);
        } else {
            encode(5, -0.5, MoveType.STRAIGHT);
        }
        robot.arm.setPosition(0);
        double box;
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
        findBox(box, -1);
        encode(32, 0.5, MoveType.ROT);
        encode(45, 0.5, MoveType.STRAIGHT);
        wait(500);
        robot.gripper.setPower(-0.25);
        wait(1000);
        robot.gripper.setPower(0);
        encode(2, -0.25, MoveType.STRAIGHT);
    }
}
