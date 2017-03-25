package org.firstinspires.ftc.teamcode.driverchoices;

import android.media.MediaPlayer;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.ImprovedOpModeBase;
import org.firstinspires.ftc.teamcode.debugging.ConsoleManager;

//Add the teleop to the op mode register.
@TeleOp(name="TankBot Drive", group="TankBot Group")

public class TankBotDriveAround extends ImprovedOpModeBase
{
    /*** CONFIGURE ALL ROBOT ELEMENTS HERE ***/
    //Drive motors (they are lists because it helps when we add on new motors.
    private DcMotor leftMotor, rightMotor;
    private Servo turret;
    private double motorCorrectionFactor = -0.1;

    // Called on initialization (once)
    protected void initializeHardware() throws InterruptedException
    {
        //Make sure that the robot components are found and initialized correctly.
        //This all happens during init()
        /*************************** DRIVING MOTORS ***************************/
        leftMotor = initialize(DcMotor.class, "Left Motor");
        rightMotor = initialize(DcMotor.class, "Right Motor");
        leftMotor.setDirection (DcMotorSimple.Direction.REVERSE);

        turret = initialize(Servo.class, "Turret");
    }

    private void setRightPower(double power)
    {
        rightMotor.setPower (power * (1 - motorCorrectionFactor));
    }

    private void setLeftPower(double power)
    {
        leftMotor.setPower (power * (1 + motorCorrectionFactor));
    }

    //All teleop controls are here.
    protected void driverStationSaysGO() throws InterruptedException
    {
        //Normal mode variables
        double leftPower, rightPower;
        boolean backwards = false;
        long lastTimeBackTogglePressed = System.currentTimeMillis(),
                lastTimeMusicTogglePressed = System.currentTimeMillis ();
        double currentTurretPosition = 0.5;

        //Keep looping while opmode is active (waiting a hardware cycle after all of this is completed, just like loop())
        while (opModeIsActive())
        {
            /**************************** CONTROLLER #1 ********************************/
            /************** Direction Toggle **************/
            if (!backwards)
            { // Driving forward
                leftPower = gamepad1.left_stick_y;
                rightPower = gamepad1.right_stick_y;
            } else { // Driving backward
                leftPower = -gamepad1.right_stick_y;
                rightPower = -gamepad1.left_stick_y;
            }

            /************** Motor Speed Control **************/
            rightPower = Range.clip(rightPower, -1, 1);
            leftPower = Range.clip(leftPower, -1, 1);

            // Write the values to the motors.  Scale the robot in order to run the robot more effectively at slower speeds.
            setLeftPower (scaleInput(leftPower));
            setLeftPower (scaleInput(rightPower));

            //Toggle direction
            if (gamepad1.a && (System.currentTimeMillis() - lastTimeBackTogglePressed) > 500)
            {
                backwards = !backwards;
                lastTimeBackTogglePressed = System.currentTimeMillis();
            }

            if (gamepad1.y && (System.currentTimeMillis () - lastTimeMusicTogglePressed) > 500)
            {
                if (mediaPlayer != null)
                    StopPlayingAudio ();
                else
                    PlayAudio (DownloadedSongs.IMPERIAL_MARCH);
            }

            /************** Turret Position **************/
            if (turret != null)
            {
                if (gamepad1.right_trigger > 0.5)
                    currentTurretPosition -= 0.005;
                else if (gamepad1.left_trigger > 0.5)
                    currentTurretPosition += 0.005;

                currentTurretPosition = Range.clip(currentTurretPosition, 0, 1);
                turret.setPosition(currentTurretPosition);
            }

            /************** Data Output **************/
            ConsoleManager.outputConstantDataToDrivers(new String[] {
                    "Right power = " + rightPower,
                    "Music playing = " + (mediaPlayer != null)
            });
            /******************** END OF LOOP ********************/

            idle();

        }
    }

    /*
     * This method scales the joystick input so for low joystick values, the
     * scaled value is less than linear.  This is to make it easier to drive
     * the robot more precisely at slower speeds.
     */

    double scaleInput(double dVal)
    {
        double[] scaleArray = {0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
                0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00};

        // get the corresponding index for the scaleInput array.
        int index = (int) (dVal * 16.0);
        if (index < 0) {
            index = -index;
        } else if (index > 16) {
            index = 16;
        }

        double dScale = 0.0;
        if (dVal < 0) {
            dScale = -scaleArray[index];
        } else {
            dScale = scaleArray[index];
        }

        return dScale;
    }

    /************ MEDIA PLAYER STUFF!!!!! ************/
    protected enum DownloadedSongs
    {
        IMPERIAL_MARCH
    }
    private MediaPlayer mediaPlayer = null;
    protected void PlayAudio(DownloadedSongs choice)
    {
        try
        {
            int selectedSong = com.qualcomm.ftcrobotcontroller.R.raw.imperialmarch;
            switch (choice)
            {
                case IMPERIAL_MARCH:
                    selectedSong = com.qualcomm.ftcrobotcontroller.R.raw.imperialmarch;
                    break;
            }
            mediaPlayer = MediaPlayer.create(hardwareMap.appContext, selectedSong);
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
            {
                public void onCompletion(MediaPlayer mediaPlayer1)
                {
                    mediaPlayer1.release();
                }
            });

            ConsoleManager.outputNewLineToDrivers ("Playing " + choice.toString());

            sleep (1000); //Give the MediaPlayer some time to initialize, and register that a song is being played.
        }
        catch (Exception e)
        {
            ConsoleManager.outputNewLineToDrivers ("Error while attempting to play music.");
            return;
        }
    }

    //Used to make the media player stop playing audio, and also to prevent excess memory allocation from being taken up.
    protected void StopPlayingAudio()
    {
        if (mediaPlayer != null)
        {
            if (mediaPlayer.isPlaying())
                mediaPlayer.stop(); //stop playing
            mediaPlayer.release(); //prevent resource allocation
            mediaPlayer = null; //nullify the reference.
        }
    }

    protected void driverStationSaysSTOP()
    {
        StopPlayingAudio ();
        leftMotor.setPower(0);
        rightMotor.setPower(0);
    }

}