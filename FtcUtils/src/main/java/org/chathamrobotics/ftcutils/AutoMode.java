package org.chathamrobotics.ftcutils;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.Map;

/**
 * basic autonomous
 */
public abstract class AutoMode extends LinearOpMode {
    // State
    public OmniWheelDriver driver;

    /*
     * Whether the current team is red
     */
    public boolean isRedTeam;

    /*
     * Setup OpMode
     * @param {boolean} isRedTeam   Whether the current team is red
     */
    public AutoMode() { this.isRedTeam = true; }
    public AutoMode(boolean isRedTeam) {
        this.isRedTeam = isRedTeam;
    }

    /*
     * Initializes robot
     */
    public void initRobot() {
        driver = OmniWheelDriver.build(hardwareMap, telemetry);
    }

    /*
     * Called on start
     */
    abstract public void runRobot() throws StoppedException;

    /*
     * called on stop
     */
    public void stopRobot() {
        for (Map.Entry<String, DcMotor> entry : hardwareMap.dcMotor.entrySet()) {
            entry.getValue().setPower(0);
        }
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
        // For each motor
        for (Map.Entry<String, DcMotor> entry : hardwareMap.dcMotor.entrySet()) {
            telemetry.addData("Motor Power", entry.getKey() + "="
                    + entry.getValue().getController().getMotorPower(entry.getValue().getPortNumber()));
        }

        telemetry.update();
    }

    /*
     * Checks if opmode is still active and if it's not throws a StoppedException
     */
    public void checkForStop() throws StoppedException{
        if (! opModeIsActive()) throw new StoppedException();
    }
}
