package org.firstinspires.ftc.teamcode;

/**
 *  This is from the position closest to the crater.
 */
public abstract class SampleDescendMarkerCrater extends StandardChassis {

    private boolean madeTheRun = false;
    private GoldStatus pos = GoldStatus.Unknown;

    protected SampleDescendMarkerCrater(ChassisConfig config) {
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

            descendFromLander();

            pos = loopSampling();

            GoldStatus pos = sampleProbe();


            if (pos == GoldStatus.Left) {
                encoderDrive(10);
                turnLeft(90);
                encoderDrive(10);
                turnRight(75);
                turnLeft(68);
                encoderDrive(24, 24);
                turnLeft(62);
                encoderDrive(64, 64);

                dropFlag();
                sleep(3000);
                resetFlag();

                turnRight(165);
                encoderDrive(103 , 110);

            } else if (pos == GoldStatus.Right) {
                encoderDrive(14);
                turnRight(90);
                encoderDrive(5);
                turnLeft(90);
                turnLeft(68);
                encoderDrive(24, 24);
                turnLeft(62);
                encoderDrive(64, 64);

                dropFlag();
                sleep(3000);
                resetFlag();

                turnRight(165);
                encoderDrive(103 , 110);

            } else {
                turnLeft(68);
                encoderDrive(24, 24);
                turnLeft(62);
                encoderDrive(64, 64);

                dropFlag();
                sleep(3000);
                resetFlag();

                turnRight(165);
                encoderDrive(103 , 110);

            }


            madeTheRun = true;
        }

        // Show the elapsed game time and wheel power.
        telemetry.addData("Status", "time: " + runtime.toString());
        telemetry.addData("Status", "madeTheRun=%b", madeTheRun);
    }
}