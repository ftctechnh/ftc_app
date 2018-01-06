package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;


@Autonomous(name="Auto Red RelRec", group="Linear Auto")

public class RelRecSideRed extends AutoMaster {
    @Override
    public void runOpMode() {
        robot.init(hardwareMap);
        waitForStart();
        telemetry.addData("skatin fast,", "eatin' ass");
        VuforiaPlagiarism vu = new VuforiaPlagiarism();
        double box;
        VuforiaPlagiarism.type typee = VuforiaPlagiarism.type.ERROR;
        robot.init(hardwareMap);
        waitForStart();
        encode(15, 0.5, MoveType.LATERALLY);
        wait(500);
        robot.arm.setPosition(1);
        wait(750);
        if (robot.color.red() > 0) {
            encode(5, -0.25, MoveType.STRAIGHT);
        } else {
            encode(5, 0.25, MoveType.STRAIGHT);
        }
        robot.arm.setPosition(0);

        /*.getVuf(hardwareMap);
        if (typee == VuforiaPlagiarism.type.RIGHT) {
            box = 16.14;
        } else if (typee == VuforiaPlagiarism.type.CENTER) {
            box = 22.05;
        } else if (typee == VuforiaPlagiarism.type.LEFT) {
            box = 29.53;
        } else {
            box = 22.05;
        } */
        encode(25, -0.5, MoveType.LATERALLY);
        encode(15, 0.5, MoveType.STRAIGHT);
        wait(500);
        robot.gripper.setPower(-0.25);
        wait(1000);
        robot.gripper.setPower(0);
        encode(4, -0.25, MoveType.STRAIGHT);
    }
}