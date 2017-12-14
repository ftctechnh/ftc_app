package org.firstinspires.ftc.teamcode.SeasonCode.VelocityVortex;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;


/**
 * <p>
 *      Simple autonomous that shoots two balls
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
@Autonomous(name = "Simple 2 Shot")
@Disabled
@SuppressWarnings("all")
final class OpAuto2Shot extends UtilAuto
{
    private Robot _robot = null;            // The robot we're working with

    @Override
    public void runOpMode() throws InterruptedException
    {
        _robot.init(hardwareMap);
        waitForStart();

        sleep(startDelay);

        _robot.shooter.timedRun(Shooter.Power.HIGH , Shooter.Direction.FORWARD , 7_000);
        _robot.harvester.timedRun(Harvester.Direction.FORWARD , 1 , 500);
        _robot.harvester.stop();

        sleep(3_000);

        _robot.harvester.timedRun(Harvester.Direction.FORWARD , 1 , 5_000);
        _robot.shooter.stop();
        _robot.harvester.stop();
    }
}