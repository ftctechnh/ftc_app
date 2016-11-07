package org.firstinspires.ftc.team9853;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.team9853.OmniWheelDriver;
import org.firstinspires.ftc.team9853.StoppedException;

import java.util.Map;

/**
 * backup autonomous
 */
public class AutoModeCharge extends LinearOpMode {
    private OmniWheelDriver driver;

    static final long waitTime = 10000;
    static final long driveTime = 3000;

    /*
     * team
     * the current team.
     */
    public boolean isRedTeam;

    /*
     * Setup new autonomous mode.
     */
    public AutoModeCharge(String team) {
        this.isRedTeam = team.toLowerCase().equals("red");
    }

    /*
     * Initializes robots
     */
    public void initRobot() {
        driver = new OmniWheelDriver(
                hardwareMap.dcMotor.get("FrontLeft"),
                hardwareMap.dcMotor.get("FrontRight"),
                hardwareMap.dcMotor.get("BackLeft"),
                hardwareMap.dcMotor.get("BackRight"),
                telemetry
        );
    }

    /*
     * called on start
     */
    public void runRobot() throws StoppedException {
        for(long endTime = System.currentTimeMillis() + waitTime; System.currentTimeMillis() < endTime;) {
            statusCheck();
            // Do nothing
        }

        for(long endTime = System.currentTimeMillis() + driveTime; System.currentTimeMillis() < endTime;) {
            statusCheck();
            driver.move(isRedTeam ? -1 : 1, 1, 0, .7);
        }
    }

    /*
     * called on stop
     */
    public void stopRobot() {
        for (Map.Entry<String, DcMotor> entry : hardwareMap.dcMotor.entrySet()) {
            entry.getValue().setPower(0);
        }
    }


    /*
         * Runs opmode. Duh!
         */
    @Override
    public void runOpMode() throws InterruptedException {

        initRobot();

        // Wait for start call
        waitForStart();

        try {
            runRobot();
        }
        catch (StoppedException error) {
            //Just continue to robot stop
        }
        finally {
            stopRobot();
        }
    }

    /*
     * periodically checks for stop and updates telemetry
     */
    private void statusCheck() throws StoppedException {
        telemetryUpdate();
        checkForStop();
    }

    /*
     * Updates telemetry readings
     */
    private void telemetryUpdate() {
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
    private void checkForStop() throws StoppedException {
        if(! opModeIsActive()) throw new StoppedException();
    }
}
