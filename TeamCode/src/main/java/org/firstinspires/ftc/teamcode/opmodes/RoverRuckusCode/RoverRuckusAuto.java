package org.firstinspires.ftc.teamcode.opmodes.RoverRuckusCode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.libraries.AutoLib;
import org.firstinspires.ftc.teamcode.libraries.hardware.RoverRuckusRatbotHardware;

@Autonomous(name = "AutoTest")
public class RoverRuckusAuto extends OpMode{
    RoverRuckusRatbotHardware robot =  new RoverRuckusRatbotHardware();
    private AutoLib.Sequence mSeq = new AutoLib.LinearSequence();

    public void init() {
        robot.init(hardwareMap);
        mSeq.add(new AutoLib.MoveByEncoderStep(robot.fl, robot.fr, 1.0f, 6000, true));
        //mSeq.add(new AutoLib.TurnByEncoderStep(robot.fr, robot.fl, 1.0f, -1.0f, 2000, -2000, true));
    }

    @Override
    public void start() {

    }

    @Override
    public void loop() {
        if(mSeq.loop()) requestOpModeStop();
    }
}
