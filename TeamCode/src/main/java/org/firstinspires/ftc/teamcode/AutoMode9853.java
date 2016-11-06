package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.lasarobotics.vision.opmode.LinearVisionOpMode;

import java.util.Map;

/**
 * Autonoumous program for 9853
 */
public class AutoMode9853 extends LinearVisionOpMode {
    private OmniWheelDriver driver;

    static double startPoisitonOne = 6.00;
    static double startPoisitonTwo = 8.00;
    static double beaconPoisitonOne = 5.00;
    static double beaconPoisitonTwo = 7.00;
    static double fieldLength = 12.00;

    /*
     * team
     * the current team.
     */
    public boolean isRedTeam;

    /*
     * Start position
     */
    public double startPosition;

    /*
     * Beacon Position
     */
    public double beaconPosition;

    /*
     * Setup new autonomous mode.
     */
    public AutoMode9853(String team) {this(team, startPoisitonOne, beaconPoisitonOne);}
    public AutoMode9853(String team, double startPosition, double beaconPosition) {
        this.isRedTeam = team.toLowerCase().equals("red");
        this.beaconPosition = beaconPosition;
        this.startPosition = startPosition;
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
    public void runRobot() throws StoppedException{

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
