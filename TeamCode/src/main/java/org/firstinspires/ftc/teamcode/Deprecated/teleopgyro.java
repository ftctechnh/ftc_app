package org.firstinspires.ftc.teamcode.Deprecated;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.AutoDrive;

/**
 * Created by BeehiveRobotics-3648 on 11/30/2017.
 */
@TeleOp(name="gyroteleop", group="linear OpMode")
@Disabled
public class teleopgyro extends OpMode {
    AutoDrive drive;
    public void init() {
        drive = new AutoDrive(hardwareMap.dcMotor.get("m1"), hardwareMap.dcMotor.get("m2"), hardwareMap.dcMotor.get("m3"), hardwareMap.dcMotor.get("m4"), hardwareMap, telemetry);
        drive.init();
    }
    public void loop() {
        telemetry.addData("Heading: ", drive.getHeading());
        telemetry.update();
    }
}
