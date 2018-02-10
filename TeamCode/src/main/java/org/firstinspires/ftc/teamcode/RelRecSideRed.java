package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Auto Red RelRec", group="Linear Auto")
public class RelRecSideRed extends AutoMaster {
    @Override
    public void runOpMode() {
        VuforiaPlagiarism vu = new VuforiaPlagiarism();
        VuforiaPlagiarism.type typee = VuforiaPlagiarism.type.ERROR;
        double vufSpeed = 0;
        typee = vu.getVuf(hardwareMap);

        robot.init(hardwareMap);
        waitForStart();
        telemetry.addData("skatin fast,", "eatin' ass");

        //Knocks off the right ball
        robot.arm.setPosition(CSENSOR_ARM_DOWN);
        wait(750);
        if (robot.color.red() > 0) {
            encode(5, -0.25, MoveType.STRAIGHT);
            robot.arm.setPosition(CSENSOR_ARM_UP);
            encode(5, 0.25, MoveType.STRAIGHT);
        } else {
            encode(5, 0.25, MoveType.STRAIGHT);
            robot.arm.setPosition(CSENSOR_ARM_UP);
            encode(5, -0.25, MoveType.STRAIGHT);
        }
        robot.arm.setPosition(CSENSOR_ARM_UP);

        //Finds Vuforia
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
        encode(37, 0.5, MoveType.STRAIGHT);
        encode(19, 0.5, MoveType.ROT);
        if (vufSpeed != 0) {
            encode(VUF_DISTANCE, vufSpeed, MoveType.LATERALLY);
        }

        //Final approach
        encode(8, 0.5, MoveType.STRAIGHT);

        //Place block

        encode(7, -0.25, MoveType.STRAIGHT);
    }
}