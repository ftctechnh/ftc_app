package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.libraries.AutoLib;
import org.firstinspires.ftc.teamcode.libraries.VuforiaBallLib;
import org.firstinspires.ftc.teamcode.opmodes.hardware.BotHardware;

/**
 * Created by Noah on 11/18/2017.
 */

@Autonomous(name="Red Drive")
public class RedDriveAuto extends OpMode {
    BotHardware bot = new BotHardware(this);
    private AutoLib.Sequence mSeq = new AutoLib.LinearSequence();

    protected boolean isRed = true;

    @Override
    public void init() {
        bot.init();

        //drive backwards
        mSeq.add(new AutoLib.MoveByTimeStep(bot.getMotorRay(), -0.2f, 1.0f, true));
        //turn back
        mSeq.add(new AutoLib.GyroTurnStep(this, isRed ? 90 : -90, bot.getHeadingSensor(), bot.getMotorRay(), 0.2f, 3.0f, true));
        //dump block
        mSeq.add(new AutoLib.TimedServoStep(bot.getStick(), BotHardware.ServoE.garyDown, 0.5, false));
    }

    @Override
    public void start() {

    }

    @Override
    public void loop() {
        if(mSeq.loop()) requestOpModeStop();
    }

    @Override
    public void stop() {

    }
}
