package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.libraries.AutoLib;
import org.firstinspires.ftc.teamcode.libraries.hardware.BotHardware;

/**
 * Created by Noah on 12/2/2017.
 */

@Autonomous(name="Everything's Broken Drive", group="test")
//@Disabled
public class SimpleDriveAuto extends OpMode {
    private BotHardware bot = new BotHardware(this);
    private AutoLib.Sequence mSeq = new AutoLib.LinearSequence();

    public void init() {
        bot.init();
        mSeq.add(new AutoLib.MoveByEncoderStep(bot.getMotorVelocityShimArray(), 270.0f, 1600, true));
    }

    @Override
    public void start() {
        //hmmm
    }

    @Override
    public void loop() {
        if(mSeq.loop()) requestOpModeStop();
    }
}
