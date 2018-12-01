package org.firstinspires.ftc.teamcode.RoverRuckus.Deployers;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RoverRuckus.BaseTeleOp;
import org.firstinspires.ftc.teamcode.RoverRuckus.Mappings.ComputerMapping;
import org.firstinspires.ftc.teamcode.RoverRuckus.Mappings.SoloMapping;

@TeleOp(name="Sparky Terminal", group="zzz")
public class ComputerTeleOp extends BaseTeleOp {
    @Override
    public void runOpMode() {
        this.controller = new ComputerMapping(gamepad1, gamepad2);
        this.fieldCentric = false;
        super.runOpMode();
    }
}
