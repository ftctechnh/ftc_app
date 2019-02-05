package org.firstinspires.ftc.teamcode;

/**
 * This is from the position closest to the crater.
 */
public abstract class ShaggyTeamMarkerCrater extends StandardChassis {

    private boolean madeTheRun = false;

    protected ShaggyTeamMarkerCrater(ChassisConfig config) {
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

                GoldStatus pos = loopSampling();
                if (pos == GoldStatus.Unknown) {
                    encoderDrive(10);
                    encoderDrive(-10);
                    if (pos == GoldStatus.Unknown) {
                        // take a guess; we have 33% chance of being correct
                        pos = GoldStatus.Center;
                    }
                }

                // TODO: remove strsfing, use turning.
                encoderDrive(15);
                if (pos == GoldStatus.Left) {
                    turnLeft(90);
                    encoderDrive(100);
                    turnRight(90);
                    encoderDrive(10);
                    turnRight(90);
                    encoderDrive(100);
                    encoderDrive(-100);

                } else if (pos == GoldStatus.Right) {
                    turnRight(90);
                    encoderDrive(100);
                    turnLeft(90);
                    encoderDrive(10);
                    turnLeft(90);
                    encoderDrive(100);
                    encoderDrive(-100);

                } else {
                    encoderDrive(15);
                    encoderDrive(-15);

                }
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
