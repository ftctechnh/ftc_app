package org.firstinspires.ftc.teamcode.GeneralCode.PreviousSeasonOpModes.VelocityVortexOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Disabled
@Autonomous(name = "Sensor Test", group = "Sensor")
public class Sensor_Test extends LinearOpMode {
    public void runOpMode() {
        SupersHardwareMap robot = new SupersHardwareMap(true);
        robot.init(hardwareMap);
        while(1==1 && opModeIsActive()) {
            robot.updateGyro();
            telemetry.addData("Color sensor blue:", robot.color.blue());
            telemetry.addData("Color sensor red:", robot.color.red());
            telemetry.addData("ODS Reading:", robot.ods.getLightDetected());
            telemetry.addData("ODS2 Reading:", robot.ods2.getLightDetected());
            telemetry.addData("Gyro heading:", robot.heading);
            telemetry.update();
        }
    }
}