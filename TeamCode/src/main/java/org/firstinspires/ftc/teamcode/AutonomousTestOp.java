package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by 111 on 9/29/2016.
 */
@TeleOp(name = "AutonomousTestOp", group = "Autonomous")
public class AutonomousTestOp extends LinearOpMode
{
    private OmniDriveBot robot = new OmniDriveBot();

    public void runOpMode()
    {
        robot.init(hardwareMap);
        waitForStart();
        robot.spin(-180);
        telemetry.addData("fR Encoder", robot.getfR().getCurrentPosition());
        telemetry.addData("fL Encoder", robot.getfL().getCurrentPosition());
        telemetry.update();
        sleep(2000);
        robot.spin(180);
        telemetry.addData("fR Encoder", robot.getfR().getCurrentPosition());
        telemetry.addData("fL Encoder", robot.getfL().getCurrentPosition());
        telemetry.update();
        sleep(3000);
    }
}

