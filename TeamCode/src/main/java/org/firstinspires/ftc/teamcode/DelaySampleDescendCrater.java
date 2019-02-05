package org.firstinspires.ftc.teamcode;

/**
 *  This is from the position closest to the crater.
 */
public abstract class DelaySampleDescendCrater extends StandardChassis {

    private boolean madeTheRun = false;
    private GoldStatus pos = GoldStatus.Unknown;

    protected DelaySampleDescendCrater(ChassisConfig config) {
        super(config);
    }

    /**
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        initMotors();
        initArm();
        initGyroscope();
        initTimeouts();
        initSampling();
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
        stopSampling();
    }

    /**
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop () {

        if (madeTheRun == false) {
            sleep(1000);

            descendFromLander();

            pos = loopSampling();

            if (pos == GoldStatus.Unknown) {
                sleep(3000);
                encoderDrive(10);
                encoderDrive(-10);
                if (pos == GoldStatus.Unknown) {
                    // take a guess; we have 33% chance of being correct
                    pos = GoldStatus.Center;
                }
            }

            if (pos == GoldStatus.Left) {
                encoderDrive(10);
                turnLeft(90);
                encoderDrive(10);
                turnRight(75);
                encoderDrive(30);
            } else if (pos == GoldStatus.Right) {
                encoderDrive(14);
                turnRight(90);
                encoderDrive(5);
                turnLeft(90);
                encoderDrive(25);
            } else {
                encoderDrive(30);
            }


            madeTheRun = true;
        }

        // Show the elapsed game time and wheel power.
        telemetry.addData("Status", "time: " + runtime.toString());
        telemetry.addData("Status", "madeTheRun=%b", madeTheRun);
    }
}