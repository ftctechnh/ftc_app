package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Auto Blue RelRec", group="Linear Auto")

public class RelRecSideBlue extends AutoMaster {
    @Override
    public void runOpMode() {
        VuforiaPlagiarism vu = new VuforiaPlagiarism();
        VuforiaPlagiarism.type typee = VuforiaPlagiarism.type.ERROR;
        robot.init(hardwareMap);
        waitForStart();
        telemetry.addData("skatin fast,", "eatin' ass");
        wait(500);
        robot.arm.setPosition(1);
        wait(750);
        boolean i = false;
        if (robot.color.red() > 0) {
            encode(5, 0.25, MoveType.STRAIGHT);
        } else {
            encode(5, -0.25, MoveType.STRAIGHT);
            i = true;
        }
        robot.arm.setPosition(0);
        double box;
       /* typee = vu.getVuf(hardwareMap);
        if (typee == VuforiaPlagiarism.type.RIGHT) {
            box = 38.98;
        } else if (typee == VuforiaPlagiarism.type.CENTER) {
            box = 45.67;
        } else if (typee == VuforiaPlagiarism.type.LEFT) {
            box = 45.7;
        } else {
            box = 45.67;
        } */
        encode(28, -0.5, MoveType.STRAIGHT);
        encode(19, 0.5, MoveType.ROT);
        if (!i) {
            encode(15, 0.5, MoveType.LATERALLY);
        }
        encode(5, 0.5, MoveType.STRAIGHT);
        wait(500);
        robot.gripper.setPower(-0.25);
        wait(1000);
        robot.gripper.setPower(0);
        encode(4, -0.25, MoveType.STRAIGHT);
    }
}
