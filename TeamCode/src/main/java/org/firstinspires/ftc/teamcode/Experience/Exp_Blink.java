package org.firstinspires.ftc.teamcode.Experience;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Libs.Core_BoardExp;
import org.firstinspires.ftc.teamcode.Libs.Enum_Libs;

@Autonomous(name = "Exp Blink LED",group = "Exp")
public final class Exp_Blink extends Core_BoardExp {

    @Override
    public void runOpMode(){
        robot.init(hardwareMap);
        waitForStart();
        while (opModeIsActive()){
            blinkLED(15,1, Enum_Libs.LEDColor.Blue, Enum_Libs.LEDColor.Red);
            Thread.currentThread().interrupt();
        }
    }
}
