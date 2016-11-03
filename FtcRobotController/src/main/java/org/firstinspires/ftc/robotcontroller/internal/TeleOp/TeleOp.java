package org.firstinspires.ftc.robotcontroller.internal.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.robotcontroller.internal.Devices.DriveSystem;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.robotcontroller.internal.Devices.FlyWheelMechanic;
import org.firstinspires.ftc.robotcontroller.internal.Devices.SweeperMechanic;
import org.firstinspires.ftc.robotcontroller.internal.Devices.TrapDoorMechanic;


/**
 * Created by abnaveed on 10/13/2016.
 */


public class TeleOp extends OpMode
{
    public DriveSystem drive;

    public FlyWheelMechanic flywheel;

    public SweeperMechanic sweeper;

    public TrapDoorMechanic trapdoor;

    // TeleOp
    public DriveSystem driveSystem;
    public DcMotor leftMotor = null;
    public DcMotor rightMotor = null;
    public FlyWheelMechanic flymotor;
    private static final float deadBand = .05f;

    @Override
    public void init()
    {
        driveSystem = new DriveSystem(hardwareMap);
        flywheel = new FlyWheelMechanic(hardwareMap);
        sweeper = new SweeperMechanic(hardwareMap);
        trapdoor = new TrapDoorMechanic(hardwareMap);
    }

    @Override
    public void loop()
    {
        // Getting joystick values
        double leftJoystick = gamepad1.left_stick_y;
        double rightJoystick = gamepad1.right_stick_y;

        // Uses deadBand to prevent robot from constantly moving.
        if(leftJoystick > deadBand)
        {
            driveSystem.setLeft(leftJoystick);
        }
        else
        {
            driveSystem.setLeft(0);
        }

        if(rightJoystick > deadBand)
        {
            driveSystem.setRight(rightJoystick);
        }
        else
        {
            driveSystem.setRight(0);
        }


        /// flywheel
        boolean sweeper = gamepad1.right_bumper;
        if(sweeper){
            SweeperMechanic.setPower(100.0);
        }
        else {
            SweeperMechanic.setPower(0.0);
        }
    }
}
