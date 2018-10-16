package org.firstinspires.ftc.teamcode.opmodes.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Config.ConfigParser;
import org.firstinspires.ftc.teamcode.systems.IMUSystem;
import org.firstinspires.ftc.teamcode.systems.MecanumDriveSystem;

/**
 * Created by EvanCoulson on 10/11/17.
 */

public abstract class BaseAutonomousOpMode extends LinearOpMode
{
    //public ConfigParser config;
    public MecanumDriveSystem driveSystem;
    public IMUSystem imuSystem;
    //public Eye eye;

    public BaseAutonomousOpMode(String opModeName)
    {
        //config = new ConfigParser(opModeName + ".omc");
        telemetry.setMsTransmissionInterval(200);
    }

    protected void initSystems()
    {
        telem("about to start drivesystem");
        this.driveSystem = new MecanumDriveSystem(this);
        telem("started drivesystem about 2 start imyou");
        this.imuSystem = new IMUSystem(this);
        //this.eye = new Eye(this);
    }

    private void telem(String message) {
        telemetry.addLine(message);
        telemetry.update();
        sleep(2000);
    }
}
