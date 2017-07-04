package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;


@Autonomous(name="Nullbot: Follow line", group="Nullbot")
public class NullbotFollowLine extends LinearOpMode {

    NullbotHardware robot = new NullbotHardware();

    @Override
    public void runOpMode() {
        robot.init(hardwareMap, this, false);


        waitForStart();
    }
}
