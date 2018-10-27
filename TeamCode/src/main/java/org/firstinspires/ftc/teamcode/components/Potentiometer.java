
package org.firstinspires.ftc.teamcode.components;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DigitalChannel;

@TeleOp(name = "Sensor test", group = "motor")
public class Potentiometer extends LinearOpMode{
    AnalogInput potentiometer;
    //DigitalChannel limitSwitch;

    public void runOpMode(){
        potentiometer = hardwareMap.get(AnalogInput.class,"potentiometer");
      //  limitSwitch = hardwareMap.get(DigitalChannel.class, "limitSwitch");
        telemetry.addData("waiting for start", "yes");
        waitForStart();
        while(opModeIsActive()){
        //    telemetry.addData("Limit switch state: ", limitSwitch.getState());
            telemetry.addData("Poten current voltage: ", potentiometer.getVoltage());
            telemetry.addData("Poten current port: ", potentiometer.getConnectionInfo());
            telemetry.update();
        }
    }
}
