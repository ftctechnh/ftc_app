package org.firstinspires.ftc.teamcode.Experience;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;

@Autonomous(name = "Color",group = "Exp")
public class ColorSensor_A extends LinearOpMode {

    private ColorSensor colorSensor_A = null;
    private ColorSensor colorSensor_B = null;
    @Override
    public void runOpMode(){
        colorSensor_A = hardwareMap.colorSensor.get("C1");
        colorSensor_B = hardwareMap.colorSensor.get("C2");
        waitForStart();
        boolean isA = true;
        while (opModeIsActive()) {
            if (isA) {
                telemetry.addData("C1 Red:", colorSensor_A.red());
                telemetry.addData("C1 Green:", colorSensor_A.green());
                telemetry.addData("C1 Blue:", colorSensor_A.blue());
                isA = false;
                sleep(3000);
            } else {
                telemetry.addData("C2 Red:", colorSensor_B.red());
                telemetry.addData("C2 Green:", colorSensor_B.green());
                telemetry.addData("C2 Blue:", colorSensor_B.blue());
                isA = true;
                sleep(3000);
            }
            telemetry.update();
        }
    }
}
