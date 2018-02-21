package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Probably doesn't need its own program.", group="Linear Auto")

public class VufTest extends AutoMaster {
    @Override
    public void runOpMode() {
        robot.init(hardwareMap);
        waitForStart();
        telemetry.addData("skatin fast,", "eatin' ass");
        VuforiaPlagiarism vu = new VuforiaPlagiarism();
        VuforiaPlagiarism.type typee = VuforiaPlagiarism.type.ERROR;
        typee = vu.getVuf(hardwareMap);
            if (typee == VuforiaPlagiarism.type.RIGHT) {
                while (opModeIsActive()) {
                    telemetry.addData("You did it: ", typee);
                    telemetry.update();
                }
            } else {
                while (opModeIsActive()) {
                    telemetry.addData("You failed: ", typee);
                    telemetry.update();
                }
            }
    }
}

