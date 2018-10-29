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
        mSeq.add(new AutoLib.MoveByEncoderStep(robot.fl, robot.bl, robot.fr, robot.br, 1.0f, 2000, true));
    }

    @Override
    public void start() {

    }

    @Override
    public void loop() {
        if(mSeq.loop()) requestOpModeStop();
    }
}
