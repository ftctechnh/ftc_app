package org.firstinspires.ftc.teamcode;

/* *
 * Created by ftcrobotics on 11/19/17.
 * Concept by Howard
 * First Coding by Jeffrey and Alexis
 */

import com.qualcomm.robotcore.util.Range;

@SuppressWarnings("WeakerAccess")
class KPAutoCommon{
    KPAutoCommon (){
       // constructor to bring class into compliance
    }
    static final int CL = 1;
    static final int UC = -1;
    static long stageTimer=0;

    static long lastSensor;
    static long lastServo;
    static long lastNav;
    static long lastTelemetry;
    // states for the NAV switch statement
    static int autoState = 0;

    static Clamps workClamp(double clampAction, long duration, long clampLimit)
    {
        Clamps myClamp = new Clamps();

        myClamp.status = false;
        if (clampAction == CL)
        {
            myClamp.leftClamp = Clamps.LEFT_CLAMPED;
            myClamp.rightClamp = Clamps.RIGHT_CLAMPED;
        }
        else if (clampAction == UC)
        {
            myClamp.leftClamp = Clamps.LEFT_UNCLAMPED;
            myClamp.rightClamp = Clamps.RIGHT_UNCLAMPED;
        }
        if (duration > clampLimit) {
            myClamp.status = true;
        }
        return myClamp;
    }

    /* @Override */

    static void autoTasks(RobotKP robot, Structures aSet)
    {
        /*
         * Initialize the drive system variables.
         * The init() method of the hardware class does all the work here
         */
        Drive2 myDrive = new Drive2();
        BotMotors dPwr = new BotMotors();
        Clamps myClamps = new Clamps();
        CrabVec mVector = new CrabVec();

        long currentTime = System.currentTimeMillis();

        float leftDriveCmd = 0;
        float rightDriveCmd = 0;
        float leftRearCmd = 0;
        float rightRearCmd = 0;
        float riserCmd = 0;
        int currentHeading = 0;
        int startHeading = 0;

        myClamps.leftClamp = Clamps.LEFT_UNCLAMPED;
        myClamps.rightClamp = Clamps.RIGHT_UNCLAMPED;

        //Loop For Timing System
        /* ***************************************************
         *                SENSORS
         *        INPUTS: Raw Sensor Values
         *       OUTPUTS: parameters containing sensor values*
         ****************************************************/
        if (currentTime - lastSensor > RobotKP.MINOR_FRAME) {
            lastSensor = currentTime;
            currentHeading = robot.gyro.getHeading();
        }

        /* **************************************************
         *                NAV
         ****************************************************/
        if (currentTime - lastNav > RobotKP.MINOR_FRAME)
        {
            lastNav = currentTime;
            boolean stageComplete = (stageTimer > aSet.dur[autoState]);
            stageTimer += RobotKP.MINOR_FRAME;

            switch ( aSet.mode[autoState] )
            {
                case Structures.CLM:  // operate the clamp
                    myClamps =
                      workClamp(aSet.clamp[autoState],stageTimer, aSet.dur[autoState]);
                    riserCmd = myClamps.liftMotor;
                    stageComplete |= myClamps.status;
                    break;
                case Structures.LFT:
                    mVector.currT = currentTime;
                    break;
                case Structures.MOV:   // move the bot, drive it somewhere
                    mVector.currT = currentTime;
                    mVector.maxT = aSet.dur[autoState];
                    mVector.destH = aSet.heading[autoState];
                    mVector.currH = currentHeading;
                    mVector.forward = aSet.vFwd[autoState];
                    mVector.yaw = aSet.vCrab[autoState];
                    mVector.pwr = aSet.vPwr[autoState];
                    mVector.stageDuration = stageTimer;
                    dPwr = myDrive.autonomousCrab(mVector);
                    stageComplete |= (dPwr.status == 0);
                    break;
                case Structures.WT:
                    break;
                default:
                    break;
            }
            if (stageComplete)
            {
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
        }  // END NAVIGATION

        /* **************************************************
         *   Servo and Motor OUTPUTS
         ****************************************************/
        if (currentTime - lastServo > RobotKP.MINOR_FRAME)
        {
            lastServo = currentTime;

            robot.leftClamp.setPosition(myClamps.leftClamp);
            robot.rightClamp.setPosition(myClamps.rightClamp);

            robot.leftDrive.setPower(leftDriveCmd);
            robot.rightDrive.setPower(rightDriveCmd);
            robot.leftRear.setPower(leftRearCmd);
            robot.rightRear.setPower(rightRearCmd);

            robot.liftMotor.setPower(riserCmd);
        }

            /* ***************************************************
             *                TELEMETRY
             *       Inputs:  telemetry structure
             *       Outputs: command telemetry output to phone
             ****************************************************/
        if (currentTime - lastTelemetry > RobotKP.TELEMETRY_PERIOD)
        {
            lastTelemetry = currentTime;
        }
        //SAFE EXIT OF RUN OPMODE, stop motors, leave servos????
        robot.leftDrive.setPower(0);
        robot.rightDrive.setPower(0);
        robot.leftRear.setPower(0);
        robot.rightRear.setPower(0);
        robot.liftMotor.setPower(0);
        //addData("Path", "Complete")
        //telemetry.update()
    }

}
