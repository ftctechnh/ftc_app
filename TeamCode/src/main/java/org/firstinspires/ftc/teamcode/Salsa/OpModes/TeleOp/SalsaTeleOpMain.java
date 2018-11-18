package org.firstinspires.ftc.teamcode.Salsa.OpModes.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Salsa.OpModes.SalsaOpMode;

/**
 * Created by adityamavalankar on 11/17/18.
 */

@TeleOp(name = "Salsa TeleOp Main")
public class SalsaTeleOpMain extends SalsaOpMode {


    @Override
    public void init() {
        robot.initDrivetrain(hardwareMap);
        robot.initMotors(hardwareMap);
        robot.initServos(hardwareMap);
    }

    @Override
    public void loop() {

        drive();
        liftHanger();
    }

}
