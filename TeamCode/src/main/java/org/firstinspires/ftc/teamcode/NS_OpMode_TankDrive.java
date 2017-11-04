package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by Nithilan on 10/29/2017.
 * Tank Drive opmode for Golden Gear robot.
 */
@TeleOp(name = "NS: Tank Drive", group = "Training")
public class NS_OpMode_TankDrive extends OpMode {
    NS_Robot_GoldenGears GGRobot = null;

    @Override
    public void init() {
        GGRobot = new NS_Robot_GoldenGears(hardwareMap);
        telemetry.addData("Status", "Robot Initalized");
    }

    @Override
    public void start(){
        GGRobot.Reset();
        telemetry.addData("Status", "Robot Started");
    }

    @Override
    public void loop() {
        GGRobot.DriveRobot(-gamepad1.left_stick_y, -gamepad1.right_stick_y);

        GGRobot.RotateArm(-gamepad2.left_stick_y);

        double tolerance = 0.15;
        if (-gamepad2.right_stick_y > tolerance){
            GGRobot.ActuateClaw(true);
        }
        else if (-gamepad2.right_stick_y < -tolerance){
            GGRobot.ActuateClaw(false);
        }
    }

    @Override
    public void stop(){
        GGRobot.Reset();
        telemetry.addData("Status", "This Is AWESOME!");
    }
}