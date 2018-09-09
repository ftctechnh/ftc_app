package org.firstinspires.ftc.teamcode.Year_2018_19.Robot;

import android.content.Context;
import android.media.MediaPlayer;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.R;

import java.io.IOException;

public class TileRunnerRobot
{
    //The hardware components.
    public DcMotor frontLeftDrive;
    public DcMotor frontRightDrive;
    public DcMotor backLeftDrive;
    public DcMotor backRightDrive;
    public Servo flagServo;
    private MediaPlayer mediaPlayer = null;

    //Set up commands here.
    public void init(HardwareMap hwMap)    //Hardware and components initiates.
    {
        frontLeftDrive = hwMap.get(DcMotor.class, "frontLeftDrive");
        frontRightDrive = hwMap.get(DcMotor.class, "frontRightDrive");
        backLeftDrive = hwMap.get(DcMotor.class, "backLeftDrive");
        backRightDrive = hwMap.get(DcMotor.class, "backRightDrive");
        flagServo = hwMap.get(Servo.class, "flagServo");

        frontRightDrive.setDirection(DcMotor.Direction.REVERSE);
        backRightDrive.setDirection(DcMotor.Direction.REVERSE);
    }

    public void safetyStop() //Safely stops all motors and other running components.
    {
        frontLeftDrive.setPower(0);
        frontRightDrive.setPower(0);
        backLeftDrive.setPower(0);
        backRightDrive.setPower(0);
        stopMusic();
    }

    public void playMusic(Context context) {
        mediaPlayer = MediaPlayer.create(context, R.raw.music);
        mediaPlayer.seekTo(0);
        mediaPlayer.start();
    }

    public void stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    public void playBB8Sound(Context context) {
        mediaPlayer = MediaPlayer.create(context, R.raw.bb8);
        mediaPlayer.seekTo(0);
        mediaPlayer.start();
    }

    public void playR2D2Sound (Context context) {
        mediaPlayer = MediaPlayer.create(context, R.raw.r2d2);
        mediaPlayer.seekTo(0);
        mediaPlayer.start();
    }
}