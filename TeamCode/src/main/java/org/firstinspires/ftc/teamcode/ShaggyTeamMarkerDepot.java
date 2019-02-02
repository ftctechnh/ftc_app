package org.firstinspires.ftc.teamcode;

/**
 *  This is from the position closest to the team depot.
 */
public abstract class ShaggyTeamMarkerDepot extends StandardChassis {

    private boolean madeTheRun = false;

    protected ShaggyTeamMarkerDepot(ChassisConfig config) {
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
                strafeLeft(100);
                encoderDrive(10);
                strafeRight(100);
                encoderDrive(1);
                dropFlag();
                sleep(3000);
                resetFlag();
            } else if (pos == GoldStatus.Right) {
                strafeRight(100);
                encoderDrive(10);
                strafeLeft(100);
                encoderDrive(1);
                dropFlag();
                sleep(3000);
                resetFlag();
            } else {
                encoderDrive(15);
                dropFlag();
                sleep(3000);
                resetFlag();
            }
            turnRight(125);
            encoderDrive(90, 90);

            madeTheRun = true;
            }

            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "time: " + runtime.toString());
            telemetry.addData("Status", "madeTheRun=%b", madeTheRun);
        }
    }
