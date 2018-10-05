package org.firstinspires.ftc.teamcode;

/* *
 * Created by ftcrobotics on 11/19/17.
 * Concept by Howard
 * First Coding by Jeffrey and Alexis
 */


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@Autonomous(name="Generic Autonomous", group="Pushbot")
@SuppressWarnings("WeakerAccess")
public class KPRedLeftAutonomous extends LinearOpMode {
    /* Declare OpMode members. */
    RobotKP robot   = new RobotKP();   // Use a Pushbot's hardware

    static final int CL = 1;
    static final int UC = -1;

    // states for the NAV switch statement
    static final int CLM = 0;
    static final int MOV = 2;
    static final int LFT = 3;
    static final int WAIT = 4;

    int autoState = 0;

    Clamps workClamp(double clampAction, long duration, long clampLimit)
    {
        Clamps myClamp = new Clamps();

        myClamp.status = false;
        if (clampAction == CL)
        {
            myClamp.leftClamp = Clamps.LEFT_CLAMPED;
            myClamp.rightClamp = Clamps.RIGHT_CLAMPED;
        }
        else
        {
            myClamp.leftClamp = Clamps.LEFT_UNCLAMPED;
            myClamp.rightClamp = Clamps.RIGHT_UNCLAMPED;
        }
        if (duration > clampLimit) {
            myClamp.status = true;
        }
        return myClamp;
    }

    @Override

    public void runOpMode() {
        /*
         * Initialize the drive system variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);
        Drive2 myDrive = new Drive2();
        BotMotors dPwr = new BotMotors();
        Clamps myClamps = new Clamps();
        CrabVec mVector = new CrabVec();

        telemetry.addData("Status", "Resetting Encoders");    //
        telemetry.update();

        long currentTime = System.currentTimeMillis();
        long lastSensor = currentTime;
        long lastServo = currentTime + 10;
        long lastNav = currentTime + 15;
        long lastTelemetry = currentTime + 17;
        long stageTimer=0;
        float leftDriveCmd = 0;
        float rightDriveCmd = 0;
        float leftRearCmd = 0;
        float rightRearCmd = 0;
        float riserCmd = 0;
        int currentHeading = 0;
        int startHeading = 0;
        dPwr.leftFront = 0;

        myClamps.leftClamp = Clamps.LEFT_UNCLAMPED;
        myClamps.rightClamp = Clamps.RIGHT_UNCLAMPED;

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        ElapsedTime runtime = new ElapsedTime();
        //A Timing System By Katherine Jeffrey,and Alexis
       // Wait for the game to start (driver presses PLAY)

        int[] thisCase =   {CLM,  LFT,  MOV,  MOV,  CLM,  MOV, WAIT};
        int[] clampArray = { CL,    0,    0,    0,   UC,    0,    0};
        int[] newHeading = { 0,     0,    0,    0,    0,    0,    0};
        float[] crabX =    { 0,     0,    0,    0,    0,    0,     0};
        float[] straightY ={ 0,     0,    0,    0,    0,    0 ,    0};
        long[] stateDur =  {500, 2000, 3000, 1000, 1000,  2000,  500};
        float[] pwrSet =   { 0,    25,    0,   30,    0,    25,    0};

        waitForStart();
        runtime.reset();
  /* ***********************************************************************************************
   *****************************       OpMode    CODE          ************************************
   ************************************************************************************************/
        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            currentTime = System.currentTimeMillis();

            //Loop For Timing System
            /* ***************************************************
             *                SENSORS
             *        INPUTS: Raw Sensor Values
             *       OUTPUTS: parameters containing sensor values*
             ****************************************************/
            if (currentTime - lastSensor > RobotKP.MINOR_FRAME) {
                lastSensor = currentTime;
                currentHeading = robot.gyro.getHeading();
                // no sensors at this time.  If we add some, change this comment.
            }


            /* **************************************************
             *                NAV
             *      Inputs:  Gamepad positions
             *               Sensor Values (as needed)
             *      Outputs: Servo and Motor position commands
             ****************************************************/
            if (currentTime - lastNav > RobotKP.MINOR_FRAME) {
                lastNav = currentTime;
                boolean stageComplete = false;
                stageTimer += RobotKP.MINOR_FRAME;

                switch ( thisCase[autoState] ) {
                    case CLM:  // operate the clamp
                        myClamps =
                          workClamp(clampArray[autoState],stageTimer, RobotKP.CLAMP_TIME);
                        riserCmd = myClamps.liftMotor;
                        stageComplete = myClamps.status;
                        break;
                    case MOV:   // move the bot, drive it somewhere
                        mVector.currT = currentTime;
                        mVector.maxT = stateDur[autoState];
                        mVector.destH = newHeading[autoState];
                        mVector.currH = currentHeading;
                        mVector.forward = straightY[autoState];
                        mVector.yaw = crabX[autoState];
                        mVector.pwr = pwrSet[autoState];
                        dPwr = myDrive.autonomousCrab(mVector);
                        stageComplete = (dPwr.status == 0);
                        break;
                    case WAIT:
                        stageComplete = (stageTimer > stateDur[autoState]);
                        break;
                    default:
                        break;
                }
                if (stageComplete) {
                    riserCmd = 0;
                    mVector.startH = startHeading;
                    stageTimer= 0;
                    autoState++;
                }

                // motor commands: Clipped & clamped.
                leftDriveCmd  = Range.clip(dPwr.leftFront, Drive2.MOTORMIN, Drive2.MOTORMAX);
                rightDriveCmd = Range.clip(dPwr.rightFront, Drive2.MOTORMIN, Drive2.MOTORMAX);
                leftRearCmd   = Range.clip(dPwr.leftRear, Drive2.MOTORMIN, Drive2.MOTORMAX);
                rightRearCmd  = Range.clip(dPwr.rightRear, Drive2.MOTORMIN, Drive2.MOTORMAX);

                riserCmd      = Range.clip(riserCmd,  Drive2.MOTORMIN, Drive2.MOTORMAX);
            }
            // END NAVIGATION
        /*   ^^^^^^^^^^^^^^^^  THIS SECTION IS MAPPING INPUTS TO OUTPUTS   ^^^^^^^^^^^^^^^
         *  ------------------------------------------------------------------------------
         *           ALL OF THE STUFF BELOW HERE IS WRITING OUTPUTS
        */

            /* **************************************************
             *                SERVO OUTPUT
             *                Inputs: leftClamp position command
             *                        rightClamp position command *
             *                Outputs: Physical write to servo interface.
             ****************************************************/
            if (currentTime - lastServo > RobotKP.MINOR_FRAME) {
                lastServo = currentTime;

                // Move both servos to new position.
                robot.leftClamp.setPosition(myClamps.leftClamp);
                robot.rightClamp.setPosition(myClamps.rightClamp);

                robot.leftDrive.setPower(leftDriveCmd);
                robot.rightDrive.setPower(rightDriveCmd);
                robot.leftRear.setPower(leftRearCmd);
                robot.rightRear.setPower(rightRearCmd);

                /* Lifter Motor Power   */
                robot.liftMotor.setPower(riserCmd);
            }

            /* ***************************************************
             *                TELEMETRY
             *       Inputs:  telemetry structure
             *       Outputs: command telemetry output to phone
             ****************************************************/
            if (currentTime - lastTelemetry > RobotKP.TELEMETRY_PERIOD) {
                lastTelemetry = currentTime;
                telemetry.addData("Switch State ", autoState);
                telemetry.addData("Switch Timer ", stageTimer );
                telemetry.update();
            }
        }
        //SAFE EXIT OF RUN OPMODE, stop motors, leave servos????
        robot.leftDrive.setPower(0);
        robot.rightDrive.setPower(0);
        robot.leftRear.setPower(0);
        robot.rightRear.setPower(0);
        robot.liftMotor.setPower(0);
        telemetry.addData("Path", "Complete");
        telemetry.update();
    }
}
