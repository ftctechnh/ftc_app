
package org.firstinspires.ftc.teamcode.components;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.AnalogInput;

@TeleOp(name = "potentiometer", group = "motor")
public class Potentiometer extends LinearOpMode{
    AnalogInput potentiometer;

    public void runOpMode(){
        potentiometer = hardwareMap.get(AnalogInput.class,"potentiometer");

        waitForStart();
        while(opModeIsActive()){
            telemetry.addData("current voltage", potentiometer.getVoltage());
            telemetry.addData("current port", potentiometer.getConnectionInfo());
            telemetry.update();
        }
    }
}
