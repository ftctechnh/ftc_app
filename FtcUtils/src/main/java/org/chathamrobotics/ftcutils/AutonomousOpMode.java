package org.chathamrobotics.ftcutils;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.lasarobotics.vision.opmode.LinearVisionOpMode;

import java.util.Map;

/**
 * basic autonomous
 */
public abstract class AutonomousOpMode extends LinearOpMode {
    // State
    public OmniWheelDriver driver;

    /*
     * Whether the current team is red
     */
    public boolean isRedTeam;



    /*
     * Initializes robot
     */
    public void initRobot() {
        driver = OmniWheelDriver.build(this);
    }

    /*
     * Called on start
     */
    abstract public void runRobot() throws StoppedException;

    /*
     * called on stop
     */
    public void stopRobot() {
        OpModeTools.stop(this);
    }

    /*
     * Runs OpMode. Duh!
     */
    @Override
    public void runOpMode() throws InterruptedException {
        initRobot();

        // Wait for start call
        waitForStart();

        debug();

        try {
            runRobot();
        }
        catch (StoppedException error) {
            //Just continue to robot stop
        }
        finally {
            debug();
            stopRobot();
        }
    }

    /*
     * periodically checks for stop and updates telemetry
     */
    public void statusCheck() throws StoppedException {
        debug();
        checkForStop();
    }

    /*
     * Updates telemetry readings
     */
    public void debug() {
        OpModeTools.debug(this);
    }

    /*
     * Checks if opmode is still active and if it's not throws a StoppedException
     */
    public void checkForStop() throws StoppedException{
        if (! opModeIsActive()) throw new StoppedException();
    }
}
