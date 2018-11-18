package org.firstinspires.ftc.teamcode.Salsa.OpModes.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Salsa.Methods.SalsaOpMode;

/**
 * Created by adityamavalankar on 11/4/18.
 */

@TeleOp(name = "Mecanum Drive", group = "Avocado")
public class MecanumDrive extends SalsaOpMode {


    @Override
    public void init(){
        robot.initDrivetrain(hardwareMap);
    }

    @Override
    public void loop() {
        drive();
        mecanumDrive();
    }
}
