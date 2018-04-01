package org.firstinspires.ftc.teamcode.Year_2018_19;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.teamcode.Year_2018_19.RobotHardware;

@TeleOp(name="BeltDriveTeleOp", group="TeleOpMode")

public class BeltDriveTeleOp extends OpMode
{
    //Creates robot hardware.
    RobotHardware myRobot = new RobotHardware();

    //Robot initiates hardware and components.
    public void init()
    {
        myRobot.init(hardwareMap);
        telemetry.addData("Status", "Robot has initiated!");
        telemetry.update();
    }

    //Robot starts.
    public void start()
    {
        telemetry.addData("Status", "Robot has started!");
        telemetry.update();
    }

    //Called repeatedly after robot starts.
    public void loop()
    {

    }

    //Robot ends.
    public void stop()
    {
        telemetry.addData("Status", "Robot has stopped!");
        telemetry.update();
    }
}