package org.firstinspires.ftc.teamcode.opmodes.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.config.ConfigParser;
import org.firstinspires.ftc.teamcode.hardware.controller.Controller;
import org.firstinspires.ftc.teamcode.logger.Logger;
import org.firstinspires.ftc.teamcode.logger.LoggingService;
import org.firstinspires.ftc.teamcode.robot.systems.ClawSystemNoMergeConflictPlease;
import org.firstinspires.ftc.teamcode.robot.systems.ElevatorSystem;
import org.firstinspires.ftc.teamcode.robot.systems.MecanumDriveSystem;
import org.firstinspires.ftc.teamcode.robot.systems.ParallelLiftSystem;

/**
 * Created by EvanCoulson on 10/11/17.
 */

public abstract class BaseTeleOpMode extends OpMode
{
    protected final ConfigParser config;
    protected Controller controller1;
    protected Controller controller2;
    protected MecanumDriveSystem driveSystem;
    protected ParallelLiftSystem liftSystem;
    protected ClawSystemNoMergeConflictPlease claw;
    protected ElevatorSystem elevator;
    protected Logger logger;


    public BaseTeleOpMode(String opModeName)
    {
        this.logger = new Logger(this, opModeName);
        logger.setLoggingServices(LoggingService.FILE);
        this.config = new ConfigParser(opModeName + ".omc");
    }

    public void initBaseSystems()
    {
        this.controller1 = new Controller(gamepad1);
        this.controller2 = new Controller(gamepad2);
        this.driveSystem = new MecanumDriveSystem(this);

        this.liftSystem = new ParallelLiftSystem(this);
        this.claw = new ClawSystemNoMergeConflictPlease(this);
        this.elevator = new ElevatorSystem(this);
        initButtons();
    }

    public abstract void initButtons();
}
