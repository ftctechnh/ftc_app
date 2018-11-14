package org.firstinspires.ftc.teamcode.RoverRuckus.Deployers;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.RoverRuckus.BaseTeleOp;
import org.firstinspires.ftc.teamcode.RoverRuckus.Mappings.SoloMapping;

@TeleOp(name="Sparky Solo")
public class SoloTeleOp extends BaseTeleOp {
    @Override
    public void runOpMode() {
        this.controller = new SoloMapping(gamepad1, gamepad2);
        this.fieldCentric = false;
        super.runOpMode();
    }
}
