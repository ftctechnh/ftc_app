package org.firstinspires.ftc.teamcode.Experience;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Libs.Core_BoardExp;

@TeleOp(name = "Exp Gamepad Programme",group = "Exp")
public final class Exp_GamepadProgrammeMode extends Core_BoardExp {

    @Override
    public void runOpMode(){
        robot.init(hardwareMap);
        waitForStart();
        keyInt();
        int index = 0;
        do{
            blinkLED(10,0.5,LEDColorDisplay[index],LEDColorDisplay[index + 1]);
            idle();
            index += 2;

        }while (index < 6);
    }
}
