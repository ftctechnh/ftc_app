package org.firstinspires.ftc.teamcode;

/**
 * This is from the position farthest from the team depot
 */
public abstract class TeamMarkerRun2 extends StandardChassis {

    private boolean madeTheRun = false;

    protected TeamMarkerRun2(ChassisConfig config) {
        super(config);
    }

    /**
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        initMotors();
        initGyroscope();
        initTimeouts();
    }

    /**
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop () {
    }

    /**
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start () {
        // Reset the game timer.
        runtime.reset();
    }

    /**
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop () {
    }

    /**
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop () {

        if (madeTheRun == false) {

            // forward 35 inches, turn 90degrees, forward 40 inches
            encoderDrive(15, 15);
            turnLeft(68);
            encoderDrive(24, 24);
            turnLeft(62);
            encoderDrive(64, 64);

            dropFlag();
            sleep(3000);
            resetFlag();

            turnRight(165);
            encoderDrive(103 , 110);


            madeTheRun = true;
        }

        // Show the elapsed game time and wheel power.
        telemetry.addData("Status", "time: " + runtime.toString());
        telemetry.addData("Status", "madeTheRun=%b", madeTheRun);
    }
}
