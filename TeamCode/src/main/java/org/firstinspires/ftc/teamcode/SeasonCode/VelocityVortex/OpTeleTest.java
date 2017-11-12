package org.firstinspires.ftc.teamcode.SeasonCode.VelocityVortex;


import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;


/**
 * <p>
 *      Here's main (beta) TeleOp OpMode. Testing realm, don't use in competition yet.
 * </p>
 *
 *
 * <p>
 *      While coding in this package, keep these units in mind: <br>
 *      1. Assume all angles are measured in degrees <br>
 *      2. Assume all distances are measured in centimeters <br>
 *      3. Assume all measurements of time are done in milliseconds <br>
 * </p>
 *
 *
 * That's all, folks!
 */
@TeleOp(name = "BetaBranch Test Realm")
@Disabled
@SuppressWarnings("all")
final class OpTeleTest extends LinearOpMode
{
    private Robot _robot = new Robot("Robot name");          // Robot we're working with

    private UtilToggle shootToggle = new UtilToggle();
    private boolean shoot = false;
    private Shooter.Direction direction = Shooter.Direction.FORWARD;
    private Shooter.Power power = Shooter.Power.HIGH;

    private UtilToggle driveSlowToggle = new UtilToggle();
    private UtilToggle driveReverseToggle = new UtilToggle();
    private UtilToggle driveGyroToggle = new UtilToggle();
    private boolean driveSlow = false;
    private boolean driveReverse = false;
    private boolean driveGyro = false;


    /**
     * Op mode loop, this is where the robot actually does things
     *
     * @throws InterruptedException
     */
    @Override
    public void runOpMode() throws InterruptedException
    {
        _robot.init(hardwareMap);
        greet();
        telemetry.update();
        waitForStart();

        telemetry.clear();

        while(opModeIsActive() && !isStopRequested())
        {
            // Attempt to power drivetrain, display message if failed
            if(!powerDriveTrain())
                telemetry.addData("Error" , "Attempt to power drivetrain has failed.");

            if(!powerHarvester())
                telemetry.addData("Error" , "Attempt to power harvester has failed.");

            if(!powerShooter())
                telemetry.addData("Error" , "Attempt to power shooter has failed");

            if(!powerLift())
                telemetry.addData("Error" , "Attempt to power lift has failed");

            if(!powerSpool())
                telemetry.addData("Error" , "Attempt to power spool has failed");






            if(_robot.drivetrain.frontColor.getColor() == SensorColor.Color.RED)
                telemetry.addData("Color" , "Red");

            else if(_robot.drivetrain.frontColor.getColor() == SensorColor.Color.BLUE)
                telemetry.addData("Color" , "Blue");

            else
                telemetry.addData("Color" , "Unknown");



            telemetry.addData("Range" , _robot.drivetrain.range.distance(DistanceUnit.CM));


            telemetry.addData("Gyro Thing" , _robot.drivetrain.gyro.heading());
            telemetry.addData("Gyro Drive" , driveGyro);

            telemetry.update();
        }
    }


    /**
     * Displays a greeting message with the robot's name.
     */
    private void greet()
    {
        telemetry.addData(">" , "Hello drivers");
        telemetry.addData(">" , "You will be controlling " + _robot.name() + "");
        telemetry.addLine();
    }


    /**
     * Powers the drivetrain based on controls defined internally
     *
     * @return True if powering was successful, false otherwise
     */
    private boolean powerDriveTrain()
    {
        boolean success;        // Tells whether powering the drivetrain was successful or not

        // Get basic directional movement from controllers
        _robot.drivePower.drive = -gamepad1.left_stick_y;
        _robot.drivePower.strafe = gamepad1.left_stick_x;
        _robot.drivePower.rotate = gamepad1.right_stick_x;

        if(driveSlowToggle.isPressed(gamepad1.a))
            driveSlow = !driveSlow;

        if(driveReverseToggle.isPressed(gamepad1.x))
            driveReverse = !driveReverse;

        if(driveGyroToggle.isPressed(gamepad1.right_bumper))
        {
            driveGyro = !driveGyro;

            if(_robot.drivePower.isReversed())
            {
                driveReverse = false;
            }
        }


        if(gamepad1.left_stick_button && gamepad1.right_stick_button)
            _robot.drivetrain.gyro.fastCalibrate();


        if(driveSlow)
            _robot.drivePower.slow();

        if(driveReverse)
            _robot.drivePower.reverse();

        if(driveGyro)
            success = _robot.drivetrain.gyroDrive(_robot.drivePower ,
                    Drivetrain.DrivetrainMode.SCALED);

        else
            success = _robot.drivetrain.drive(_robot.drivePower , Drivetrain.DrivetrainMode.SCALED);


        // "Snap turning": pressing the dpad will "snap" the robot to a certain orientation
        // Experimental, don't try this at home
        if(gamepad1.dpad_left)
            success = success && _robot.drivetrain.turnTo(180 , .2);

        else if(gamepad1.dpad_up)
            success = success && _robot.drivetrain.turnTo(90 , .2);

        else if(gamepad1.dpad_right)
            success = success && _robot.drivetrain.turnTo(0 , .2);

        else if(gamepad1.dpad_down)
            success = success && _robot.drivetrain.turnTo(270 , .2);


        return success;
    }


    /**
     * Powers the harvester based on controls defined internally
     *
     * @return True if powering was successful or not attempted, false otherwise
     */
    private boolean powerHarvester()
    {
        boolean success = true;     // Tells whether powering the harvester was successful or not

        if(gamepad1.right_trigger > 0)
            success = _robot.harvester.run(Harvester.Direction.FORWARD);

        else if(gamepad1.left_trigger > 0)
            success = _robot.harvester.run(Harvester.Direction.BACKWARD);

        else
            _robot.harvester.stop();

        return success;
    }


    /**
     * Powers the shooter based on controls defined internally
     *
     * @return True if powering was successful or not attempted, false otherwise
     */
    private boolean powerShooter()
    {
        boolean success;        // Tells whether powering the shooter was successful or not


        if(shootToggle.isPressed(gamepad2.b))
        {
            shoot = !shoot;

            if(gamepad2.right_trigger > 0)
                power = Shooter.Power.LOW;

            if(gamepad2.left_trigger > 0)
                direction = Shooter.Direction.BACKWARD;
        }


        if(shoot)
            success = _robot.shooter.run(power , direction);

        else
        {
            success = _robot.shooter.stop();

            power = Shooter.Power.HIGH;
            direction = Shooter.Direction.FORWARD;
        }

        return success;
    }


    /**
     * Powers the lift based on controls defined internally
     *
     * @return True if attempt to power lift succeeded or not attempted, false otherwise
     */
    private boolean powerLift()
    {
        boolean success;    // Tells whether powering the lift was successful or not

        success = _robot.lift.run(gamepad2.right_stick_y);

        return success;
    }


    /**
     * Powers the spool based on controls defined internally
     *
     * @return True if attempt to power spool succeeded or not attempted, false otherwise
     */
    private boolean powerSpool()
    {
        boolean success;    // Tells whether powering the spool was successful or not

        success = _robot.spool.run(gamepad2.left_stick_y);

        return success;
    }
}