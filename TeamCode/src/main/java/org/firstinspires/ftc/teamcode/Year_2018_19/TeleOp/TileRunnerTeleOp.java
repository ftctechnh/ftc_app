package org.firstinspires.ftc.teamcode.Year_2018_19.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.R;
import org.firstinspires.ftc.teamcode.Year_2018_19.Robot.TileRunnerRobotHardware;
import org.firstinspires.ftc.teamcode.Year_2018_19.SongPlayer;

import android.content.Context;
import android.media.MediaPlayer;

import java.io.IOException;
import java.util.Timer;

@TeleOp(name="TileRunnerTeleOp", group="TeleOpMode")

public class TileRunnerTeleOp extends OpMode
{
    //Creates robot hardware.
    TileRunnerRobotHardware robot = new TileRunnerRobotHardware();
    private boolean pressedLast;

    //Robot initiates hardware and components.
    public void init()
    {
        robot.init(hardwareMap);
        this.pressedLast = false;
        telemetry.addData("Status", "Robot has initiated!");
        telemetry.update();
    }

    //Robot starts.
    public void start()
    {
        telemetry.addData("Status", "Robot has started!");
        telemetry.update();
    }

    //Called repeatedly after robot starts.
    public void loop()
    {
        robot.topLeftMotor.setPower(-gamepad1.left_stick_y); //Controls top left motor
        robot.bottomLeftMotor.setPower(-gamepad1.left_stick_y); //Controls bottom left motor
        robot.topRightMotor.setPower(-gamepad1.right_stick_y); //Controls top right motor
        robot.bottomRightMotor.setPower(-gamepad1.right_stick_y); //Controls bottom right motor

        if (gamepad1.a || gamepad2.a)
        {
            robot.flagServo.setPosition(0.6);
        }
        else if (gamepad1.b || gamepad2.b)
        {
            robot.flagServo.setPosition(0.3);
        }
        if (this.gamepad1.x) {
            SongPlayer.start(this.hardwareMap.appContext);
        }
        else if (gamepad1.y) {
            SongPlayer.stop();
        }
    }

    //Robot ends.
    public void stop()
    {
        SongPlayer.stop();
        telemetry.addData("Status", "Robot has stopped!");
        telemetry.update();
    }

    public void FlagWave() throws InterruptedException
    {
        Timer timer = new Timer();
        
        Thread.sleep(1000);
        robot.flagServo.setPosition(0.7);
        Thread.sleep(1000);
        robot.flagServo.setPosition(0.3);
    }
}

/*public class SoundOpMode extends OpMode {
    private boolean pressedLast;
    public void init() {
        this.pressedLast = false;
    }
    public void loop() {
        if (this.gamepad1.a && !this.pressedLast) {
            SongPlayer.start(this.hardwareMap.appContext);
            this.pressedLast = true;
        }
        else if (!this.gamepad1.a && this.pressedLast) {
            SongPlayer.stop();
            this.pressedLast = false;
        }
    }
    public void stop() {
        SongPlayer.stop();
    }
}*/