package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Auto Blue NonRelRec", group="Linear Auto")
public class NonRelRecSideBlue extends AutoMaster {
    @Override
    public void runOpMode() {
        VuforiaPlagiarism vu = new VuforiaPlagiarism();
        VuforiaPlagiarism.type typee = VuforiaPlagiarism.type.ERROR;
        double vufSpeed = 0;

        robot.init(hardwareMap);
        waitForStart();
        telemetry.addData("skatin fast,", "eatin' ass");

        //Knocks off the right ball
        robot.arm.setPosition(1);
        wait(750);
        if (robot.color.red() > 0) {
            encode(5, 0.25, MoveType.STRAIGHT);
            robot.arm.setPosition(0);
            wait(1000);
            encode(5, -0.1, MoveType.STRAIGHT);
        } else {
            encode(5, -0.25, MoveType.STRAIGHT);
            robot.arm.setPosition(0);
            wait(1000);
            encode(5, 0.1, MoveType.STRAIGHT);
        }

        //Finds Vuforia
        typee = vu.getVuf(hardwareMap);
        if (typee == VuforiaPlagiarism.type.RIGHT) {
            vufSpeed = 0.5;
        } else if (typee == VuforiaPlagiarism.type.CENTER) {
            vufSpeed = 0;
        } else if (typee == VuforiaPlagiarism.type.LEFT) {
            vufSpeed = -0.5;
        } else {
            vufSpeed = 0;
        }

        //Lines up the proper box
        encode(23.5, -0.5, MoveType.STRAIGHT);
        encode(57, 0.5, MoveType.ROT);
        encode(23, 0.5, MoveType.STRAIGHT);
        encode(19, -0.5, MoveType.ROT);
        if (vufSpeed != 0) {
            encode(VUF_DISTANCE, vufSpeed, MoveType.LATERALLY);
        }

        //Final approach
        encode(15, 0.5, MoveType.STRAIGHT); // Ryan is dumb for making me do this for no reason
        wait(500);
        robot.gripper.setPower(-0.25);
        wait(1000);
        robot.gripper.setPower(0);
        encode(4, -0.25, MoveType.STRAIGHT);
    }
}
