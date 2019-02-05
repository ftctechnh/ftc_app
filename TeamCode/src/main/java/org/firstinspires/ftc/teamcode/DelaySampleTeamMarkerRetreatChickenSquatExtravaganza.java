package org.firstinspires.ftc.teamcode;


public abstract class DelaySampleTeamMarkerRetreatChickenSquatExtravaganza extends StandardChassis {

    private boolean madeTheRun = false;

    protected DelaySampleTeamMarkerRetreatChickenSquatExtravaganza(ChassisConfig config) {
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
            sleep(1000);
            descendFromLander();

            //When gold is detected on the side of the screen it is on, strafe left, right or stay depending on where it is. Then, move forward into the crater.\
            GoldStatus pos = loopSampling();
            if (pos == GoldStatus.Unknown) {
                encoderDrive(10);
                encoderDrive(-10);
                if (pos == GoldStatus.Unknown) {
                    // take a guess; we have 33% chance of being correct
                    pos = GoldStatus.Center;
                }
            }

            // we will always have a valid pos here.
            encoderDrive(15);
            if (pos == GoldStatus.Left) {
                turnLeft(90);
                encoderDrive(10);
                turnRight(75);
                encoderDrive(20);
                turnRight(90);
                dropFlag();
                sleep(3000);
                resetFlag();
                turnLeft(90);
                encoderDrive(-30);
            } else if (pos == GoldStatus.Right) {
                turnRight(90);
                encoderDrive(10);
                turnLeft(75);
                encoderDrive(20);
                turnLeft(90);
                dropFlag();
                sleep(3000);
                resetFlag();
                turnRight(90);
                encoderDrive(-30);
            } else {
                encoderDrive(30);
                dropFlag();
                sleep(3000);
                resetFlag();
                encoderDrive(-30);
            }

            madeTheRun = true;
        }

        // Show the elapsed game time and wheel power.
        telemetry.addData("Status", "time: " + runtime.toString());
        telemetry.addData("Status", "madeTheRun=%b", madeTheRun);
    }
}