package org.firstinspires.ftc.teamcode.opmode.autonomous;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.HolonomicRobot;
import org.firstinspires.ftc.teamcode.R;

import java.util.prefs.Preferences;

/**
 * Created by 292486 on 11/15/2016.
 */

@Autonomous(name = "Simple Autonomous", group = "autonomous")
public class SimpleAutonomous extends LinearOpMode {

    HolonomicRobot robot = new HolonomicRobot();
    private long delayTime, forwardTimeShoot, forwardTimeCap;
    private int alliance;   //0 - blue | 1 - red
    private String allianceColor;
    private boolean shootBalls;


    @Override
    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(hardwareMap.appContext);
        alliance = Integer.parseInt(sharedPreferences.getString("mk_color", "0"));
        allianceColor = alliance==0 ? "Blue" : "Red";
        delayTime = Long.parseLong(sharedPreferences.getString("mk_delayTime", "5000"));
        forwardTimeCap = Long.parseLong(sharedPreferences.getString("mk_ballTime", "2500"));
        shootBalls = sharedPreferences.getBoolean("mk_shootBalls", false);
        forwardTimeShoot = Long.parseLong(sharedPreferences.getString("mk_shootTime", "1000"));
        telemetry.addData("Settings: ", "%s D%d B%d Shoot%b %d", allianceColor, delayTime, forwardTimeCap, shootBalls, forwardTimeShoot);
        telemetry.update();

        checkSettings();

        waitForStart();

        sleep(delayTime);


        if(shootBalls) {
            robot.move(0.4, -0.5, 0.4, -0.5);
            sleep(forwardTimeShoot);
            robot.stop();

            //robot.intake.setPower(-0.5);
            //sleep(1000);
            robot.intake.setPower(0);

            //Shoot();
            robot.shooterRed.setPower(.7);
            robot.shooterBlue.setPower(.7);
            sleep(1500);
            robot.shooterRed.setPower(0);
            robot.shooterBlue.setPower(0);
            robot.intake.setPower(-0.5);
            sleep(5000);
            robot.intake.setPower(0);
            robot.shooterRed.setPower(.7);
            robot.shooterBlue.setPower(.7);
            sleep(1500);
            robot.shooterRed.setPower(0);
            robot.shooterBlue.setPower(0);

        }

        //HitCap();
        robot.move(0.4, -0.5, 0.4, -0.5);
        sleep(forwardTimeCap);
        if(alliance==0) //Blue [Change spin direction]
        {
            robot.move(-0.5, -0.5, -0.5, -0.5);
        } else {
            robot.move(0.5, 0.5, 0.5, 0.5);
        }
        sleep(1000);
        robot.stop();
    }

    private void checkSettings()
    {
        telemetry.addData("ALLIANCE: ", allianceColor);
        telemetry.addData("SHOOT BALLS: ", shootBalls);

        if(delayTime < 5000)
        {
            telemetry.addData("!!!DELAY TIME: ", delayTime + "!!!");
        } else {
            telemetry.addData("DELAY TIME: ", delayTime);
        }
        if(forwardTimeCap < 2000)
        {
            telemetry.addData("!!!CAP TIME: ", forwardTimeCap + "!!!");
        } else {
            telemetry.addData("CAP TIME: ", forwardTimeCap);

        }
        if(forwardTimeShoot < 1000)
        {
            telemetry.addData("!!!SHOOT TIME: ", forwardTimeShoot + "!!!");
        } else {
            telemetry.addData("SHOOT TIME: ", forwardTimeShoot);
        }

        telemetry.update();
        /*
        new AlertDialog.Builder(hardwareMap.appContext).setTitle("Delay Time Short").setMessage("Are you sure? Shoot Time: " + forwardTimeShoot)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
                */
    }
}

