package org.firstinspires.ftc.teamcode.opmodes.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.systems.IMUSystem;
import org.firstinspires.ftc.teamcode.systems.MecanumDriveSystem;

/**
 * Created by EvanCoulson on 10/11/17.
 */

public abstract class BaseAutonomousOpMode extends LinearOpMode
{
    // ConfigParser config;
    public MecanumDriveSystem driveSystem;
    public IMUSystem imuSystem;
    //public EyeSystem eye;

    public BaseAutonomousOpMode(String opModeName)
    {
        //config = new ConfigParser(opModeName + ".omc");
        telemetry.setMsTransmissionInterval(200);
    }

    protected void initSystems()
    {
        this.driveSystem = new MecanumDriveSystem(this);
        this.imuSystem = new IMUSystem(this);
        //this.eye = new EyeSystem(this);
    }
}
