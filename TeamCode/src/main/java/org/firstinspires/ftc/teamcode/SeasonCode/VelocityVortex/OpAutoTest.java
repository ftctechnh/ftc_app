package org.firstinspires.ftc.teamcode.SeasonCode.VelocityVortex;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;


/**
 * <p>
 *      The test autonomous. Please don't use this
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
@Autonomous(name = "Test auto, DON\'T USE")
@Disabled
@SuppressWarnings("all")
final class OpAutoTest extends UtilAuto
{
    private Robot _robot = new Robot("Ducky");

    @Override
    public void runOpMode() throws InterruptedException
    {
        _robot.init(hardwareMap);
        _robot.drivetrain.gyro.calibrate();

        _robot.drivetrain.lineColor.enableLED(true);

        super.preRunInit();

        waitForStart();

        _robot.drivetrain.useAntiRotate = true;


        initAlign();

        alignWithBeacon();

        pushButton();

        toNextBeacon();

        pushButton();

        finalAlign();
    }


    /**
     * Performs the initial alignment by moving toward the center and doing a 180
     */
    private void initAlign()
    {
        if(teamColor == SensorColor.Color.BLUE)
        {
            _robot.drivetrain.driveForTime(90 , 1.0 , 1_000);
            sleep(250);
            _robot.drivetrain.driveForTime(0 , 1.0 , 1_000);
            sleep(250);
            _robot.drivetrain.driveForTime(90 , 1.0 , 1_000);
            sleep(250);
            _robot.drivetrain.stop();
            sleep(250);
            _robot.drivetrain.driveToDistance(20 , 0 , 1);
            _robot.drivetrain.stop();
            sleep(250);
            _robot.drivetrain.turnTo(90 , .04);
            _robot.drivetrain.stop();
        }
        else
        {
            _robot.drivetrain.driveForTime(270 , 1.0 , 2_000);
            _robot.drivetrain.stop();
            sleep(250);
            _robot.drivetrain.driveToDistance(20 , 0 , 1);
            _robot.drivetrain.stop();
            sleep(250);
            _robot.drivetrain.turnTo(90 , .04);
            _robot.drivetrain.stop();
        }
    }


    /**
     * Pushes the correct button based on the color passed in
     */
    private void pushButton()
    {
        if(teamColor == SensorColor.Color.BLUE)
        {
            telemetry.addData("front color" , _robot.drivetrain.frontColor.getColor().asString());
            telemetry.update();

            if(_robot.drivetrain.frontColor.getColor() == teamColor)
                _robot.drivetrain.driveToColor(90 , .75 , teamColor , _robot.drivetrain.backColor);

            else
                _robot.drivetrain.driveToColor(270 , .75 , teamColor , _robot.drivetrain.frontColor);

            _robot.drivetrain.stop();

            sleep(250);

            _robot.drivetrain.driveForTime(0 , .2 , 1_000);
            _robot.drivetrain.stop();

            _robot.drivetrain.driveForTime(180 , .2 , 1_000);
            _robot.drivetrain.stop();

            _robot.drivetrain.turnTo(90 , .07);
            _robot.drivetrain.stop();
        }
        else
        {
            if(_robot.drivetrain.frontColor.getColor() == teamColor)
                _robot.drivetrain.driveToColor(90 , .75 , teamColor , _robot.drivetrain.backColor);

            else
                _robot.drivetrain.driveToColor(270 , .75 , teamColor , _robot.drivetrain.frontColor);

            _robot.drivetrain.stop();

            sleep(250);

            _robot.drivetrain.driveForTime(0 , .2 , 1_000);
            _robot.drivetrain.stop();

            _robot.drivetrain.driveForTime(180 , .2 , 1_000);
            _robot.drivetrain.stop();

            _robot.drivetrain.turnTo(90 , .07);
            _robot.drivetrain.stop();
        }
    }


    /**
     * Positions the robot toward the back color of the beacon
     */
    private void alignWithBeacon()
    {
        if(teamColor == SensorColor.Color.BLUE)
        {
            _robot.drivetrain.driveToDistance(15 , 0 , .2);
            _robot.drivetrain.stop();

            _robot.drivetrain.turnTo(90 , .1);
            _robot.drivetrain.stop();

            _robot.drivetrain.driveToLine(270 , .2);

            _robot.drivetrain.stop();

            _robot.drivetrain.turnTo(90 , .07);

            _robot.drivetrain.driveToDistance(15 , 0 , .2);
            _robot.drivetrain.stop();

            _robot.drivetrain.turnTo(90 , .1);
            _robot.drivetrain.stop();

            _robot.drivetrain.driveToLight(90 , .07 , _robot.drivetrain.frontColor);
        }
        else
        {
            _robot.drivetrain.driveToDistance(15 , 0 , .2);
            _robot.drivetrain.stop();

            _robot.drivetrain.turnTo(90 , .1);
            _robot.drivetrain.stop();

            _robot.drivetrain.driveToLine(90 , .2);

            _robot.drivetrain.stop();

            _robot.drivetrain.turnTo(90 , .07);

            _robot.drivetrain.driveToDistance(12 , 0 , .2);
            _robot.drivetrain.stop();

            _robot.drivetrain.turnTo(90 , .1);
            _robot.drivetrain.stop();

            _robot.drivetrain.driveToLine(270 , .05);
        }
    }


    /**
     * Travels to the next beacon
     */
    private void toNextBeacon()
    {
        if(teamColor == SensorColor.Color.BLUE)
        {
            _robot.drivetrain.driveForTime(90 , 1 , 1_000);
            _robot.drivetrain.stop();
            sleep(250);

            _robot.drivetrain.driveToDistance(20 , 0 , .2);
            _robot.drivetrain.stop();
            sleep(250);

            _robot.drivetrain.turnTo(90 , .04);
            _robot.drivetrain.stop();
            sleep(250);

            _robot.drivetrain.driveToLine(90 , .2);
            _robot.drivetrain.stop();
            sleep(250);

            _robot.drivetrain.turnTo(90 , .07);

            _robot.drivetrain.driveToDistance(12 , 0 , .2);
            _robot.drivetrain.turnTo(90 , .07);

            _robot.drivetrain.driveToLine(90 , .12 , 1_500);
            _robot.drivetrain.driveToLine(270 , .2 , 1_500);
        }
        else
        {
            _robot.drivetrain.driveForTime(270 , 1 , 1_000);
            _robot.drivetrain.stop();
            sleep(250);

            _robot.drivetrain.driveToDistance(20 , 0 , .2);
            _robot.drivetrain.stop();
            sleep(250);

            _robot.drivetrain.turnTo(90 , .04);
            _robot.drivetrain.stop();
            sleep(250);

            _robot.drivetrain.driveToLine(270 , .2);
            _robot.drivetrain.stop();
            sleep(250);

            _robot.drivetrain.turnTo(90 , .07);

            _robot.drivetrain.driveToDistance(15 , 0 , .2);
            _robot.drivetrain.turnTo(90 , .07);
        }
    }


    private void finalAlign()
    {
        if(teamColor == SensorColor.Color.BLUE)
        {
            _robot.drivetrain.turnTo(90 , .2);
            _robot.drivetrain.stop();
        }
        else
        {
            _robot.drivetrain.turnTo(270 , .2);
            _robot.drivetrain.stop();
        }
    }
}