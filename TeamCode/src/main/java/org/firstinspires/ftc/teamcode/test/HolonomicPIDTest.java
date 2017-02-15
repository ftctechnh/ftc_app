package org.firstinspires.ftc.teamcode.test;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.HolonomicRobot;

import java.util.Arrays;

/**
 * Created by 292486 on 1/4/2017.
 */

@Autonomous
public class HolonomicPIDTest extends LinearOpMode{

    private static void moveForwardWithControl(double speed, HolonomicRobot robot, PIDTest frontControl, PIDTest backControl) {
        ElapsedTime timer = new ElapsedTime(ElapsedTime.Resolution.SECONDS);

        frontControl.resetPID();
        backControl.resetPID();

        while(timer.time() < 7) {
            robot.frontLeft.setPower(-speed + frontControl.getCorrection());
            robot.frontRight.setPower(speed + frontControl.getCorrection());
            robot.backLeft.setPower(-speed + backControl.getCorrection());
            robot.backRight.setPower(speed + backControl.getCorrection());
        }
    }

    PIDTest frontController;
    PIDTest backController;
    HolonomicRobot robot;

    @Override
    public void runOpMode() throws InterruptedException {
        robot = new HolonomicRobot();
        robot.init(hardwareMap);

        frontController = new PIDTest(hardwareMap, robot.frontLeft, robot.frontRight);
        backController = new PIDTest(hardwareMap, robot.backLeft, robot.backRight);
        frontController.setMaxCorrection(0.25);
        backController.setMaxCorrection(0.25);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(hardwareMap.appContext);
        frontController.setTuning(Double.parseDouble(sharedPreferences.getString("mk_kp", "0.005")),
                Double.parseDouble(sharedPreferences.getString("mk_ki", "0.00005")),
                Double.parseDouble(sharedPreferences.getString("mk_kd", "0.001")), 0);

        moveForwardWithControl(.25, robot, frontController, backController);
        robot.stop();
    }

    /*
    public static void arcade(double y, double x, double l, double r, HardwareHolonomic robot) {
        double flSpeed = -y - x + l - r;
        double frSpeed = y - x + l - r;
        double blSpeed = -y + x + l - r;
        double brSpeed = y + x + l - r;

        Why are they moving in the wrong direction? Configuration
        double flSpeed = -y + x + l - r;
        double frSpeed = y + x + l - r;
        double blSpeed = -y - x + l - r;
        double brSpeed = y - x + l - r;

    double[] speeds = {flSpeed, frSpeed, blSpeed, brSpeed};
    Arrays.sort(speeds);
    if(speeds[3] > 1){
        for(double speed : speeds){
            speed /= speeds[3];
        }
    }

    move(flSpeed, frSpeed, blSpeed, brSpeed, robot);
     */

    /*
    move(-speed, speed, -speed, speed, robot);
     */

}
