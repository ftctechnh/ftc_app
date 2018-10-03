package org.firstinspires.ftc.teamcode.opmodes.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.config.ConfigParser;
import org.firstinspires.ftc.teamcode.robot.systems.ClawSystemNoMergeConflictPlease;
import org.firstinspires.ftc.teamcode.robot.systems.ElevatorSystem;
import org.firstinspires.ftc.teamcode.robot.systems.Eye;
import org.firstinspires.ftc.teamcode.robot.systems.IMUSystem;
import org.firstinspires.ftc.teamcode.robot.systems.MecanumDriveSystem;
import org.firstinspires.ftc.teamcode.robot.systems.ParallelLiftSystem;
import org.firstinspires.ftc.teamcode.robot.systems.PixySystem;

/**
 * Created by EvanCoulson on 10/11/17.
 */

public abstract class BaseAutonomousOpMode extends LinearOpMode
{
    public ConfigParser config;
    public MecanumDriveSystem driveSystem;
    public IMUSystem imuSystem;
    public Eye eye;
    public ElevatorSystem elevator;
    public PixySystem pixySystem;
    public ClawSystemNoMergeConflictPlease claw;
    public ParallelLiftSystem parallelLiftSystem;

    public BaseAutonomousOpMode(String opModeName)
    {
        config = new ConfigParser(opModeName + ".omc");
        telemetry.setMsTransmissionInterval(200);
    }

    protected void initSystems()
    {
        this.driveSystem = new MecanumDriveSystem(this);
        this.imuSystem = new IMUSystem(this);
        this.eye = new Eye(this);
        this.elevator = new ElevatorSystem(this);
        this.claw = new ClawSystemNoMergeConflictPlease(this);
        this.parallelLiftSystem = new ParallelLiftSystem(this);
        this.pixySystem = new PixySystem(this, 1);
    }
}
