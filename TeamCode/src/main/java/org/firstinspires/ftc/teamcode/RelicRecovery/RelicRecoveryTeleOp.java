package org.firstinspires.ftc.teamcode.RelicRecovery;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by jeppe on 22-10-2017.
 */
@TeleOp(name = "Relic Recovery",group = "TeleOp")
public class RelicRecoveryTeleOp extends RelicRecoveryTelemetry {

    @Override
    public void init()
    {

    }

    @Override
    public void loop()
    {
        rightPower = -gamepad1.right_stick_y;
        leftPower = -gamepad1.right_stick_y;
        liftPower = +gamepad1.right_trigger - gamepad1.left_trigger;
        right.setPower(rightPower);
        left.setPower(leftPower);
        lift.setPower(liftPower);
    }


}

