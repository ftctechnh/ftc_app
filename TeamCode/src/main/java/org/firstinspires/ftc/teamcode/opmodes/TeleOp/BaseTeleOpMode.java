package org.firstinspires.ftc.teamcode.opmodes.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Config.ConfigParser;
import org.firstinspires.ftc.teamcode.Hardware.controller.Controller;

/**
 * Created by EvanCoulson on 10/11/17.
 */

public abstract class BaseTeleOpMode extends OpMode
{
    protected final ConfigParser config;
    protected Controller controller1;
    protected Controller controller2;
    //protected MecanumDriveSystem driveSystem;
    //protected Logger logger;


    public BaseTeleOpMode(String opModeName)
    {
        //this.logger = new Logger(this, opModeName);
        //logger.setLoggingServices(LoggingService.FILE);
        this.config = new ConfigParser(opModeName + ".omc");
    }

    public void initBaseSystems()
    {
        this.controller1 = new Controller(gamepad1);
        this.controller2 = new Controller(gamepad2);
        //this.driveSystem = new MecanumDriveSystem(this);

        initButtons();
    }

    public abstract void initButtons();
}
