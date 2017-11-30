package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Created by BeehiveRobotics-3648 on 11/30/2017.
 */
@TeleOp(name="gyroteleop", group="linear OpMode")
public class teleopgyro extends OpMode {
    AutoDrive drive;
    public void init() {
        drive=new AutoDrive(hardwareMap.dcMotor.get("m1"), hardwareMap.dcMotor.get("m2"), hardwareMap.dcMotor.get("m3"), hardwareMap.dcMotor.get("m4"), hardwareMap.gyroSensor.get("g1"), telemetry);
        drive.init();
    }
    public void loop() {
        telemetry.addData("Heading: ", drive.getHeading());
        telemetry.update();
    }
}
