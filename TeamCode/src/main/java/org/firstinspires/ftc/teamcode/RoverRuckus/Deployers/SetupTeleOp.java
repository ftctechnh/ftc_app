package org.firstinspires.ftc.teamcode.RoverRuckus.Deployers;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RoverRuckus.BaseTeleOp;
import org.firstinspires.ftc.teamcode.RoverRuckus.Mappings.ComputerMapping;
import org.firstinspires.ftc.teamcode.RoverRuckus.Mappings.SetupMapping;

@TeleOp(name="Sparky Setup")
public class SetupTeleOp extends BaseTeleOp {
    @Override
    public void runOpMode() {
        this.controller = new SetupMapping(gamepad1, gamepad2);
        this.fieldCentric = false;
        super.runOpMode();
    }
}
