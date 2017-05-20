package org.firstinspires.ftc.teamcode.Experience;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Libs.Core_BoardExp;
import org.firstinspires.ftc.teamcode.Libs.Enum_Libs;

@Autonomous(name = "Exp Light Sensor",group = "Exp")
public final class Exp_LightSensor extends Core_BoardExp {

    @Override
    public void runOpMode(){
        robot.init(hardwareMap);
        waitForStart();
        while (opModeIsActive()){
            if(isWhite(3)){
                blinkLED(10,0.2, Enum_Libs.LEDColor.Purple, Enum_Libs.LEDColor.Cyan);
            }else {
                blinkLED(10,0.2, Enum_Libs.LEDColor.Green, Enum_Libs.LEDColor.Red);
            }
        }
    }
}
