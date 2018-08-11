package org.firstinspires.ftc.teamcode.Year_2018_19.TeleOp;

import com.qualcomm.ftccommon.SoundPlayer;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Year_2018_19.Robot.TileRunnerRobotHardware;
import org.firstinspires.ftc.teamcode.Year_2018_19.SongPlayer;

@TeleOp(name="TileRunnerTeleOp", group="TeleOpMode")

public class TileRunnerTeleOp extends OpMode
{
    //Creates robot hardware.
    TileRunnerRobotHardware robot = new TileRunnerRobotHardware();

    //Robot initiates hardware and components.
    public void init()
    {
        robot.init(hardwareMap);
        SongPlayer.playR2D2Sound(this.hardwareMap.appContext);
        telemetry.addData("Status", "Robot has initiated!");
        telemetry.update();
    }

    //Robot starts.
    public void start()
    {
        SongPlayer.playBB8Sound(this.hardwareMap.appContext);
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
            SongPlayer.playMusic(this.hardwareMap.appContext);
        }
        else if (gamepad1.y) {
            SongPlayer.stopMusic();
        }
    }

    //Robot ends.
    public void stop()
    {
        SongPlayer.stopMusic();
        telemetry.addData("Status", "Robot has stopped!");
        telemetry.update();
    }
}