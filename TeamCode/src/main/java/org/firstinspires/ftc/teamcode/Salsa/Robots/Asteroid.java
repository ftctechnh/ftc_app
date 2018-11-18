package org.firstinspires.ftc.teamcode.Salsa.Robots;

import org.firstinspires.ftc.teamcode.Salsa.Constants;
import org.firstinspires.ftc.teamcode.Salsa.Hardware.Robot;
import org.opencv.core.Mat;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


/**
 * Created by adityamavalankar on 11/5/18.
 */

public class Asteroid {

    public Robot robot = new Robot();
    public Constants constants;

    public void drive(double leftJoystick, double rightJoystick) {

        robot.leftBack.setPower(leftJoystick);
        robot.leftFront.setPower(leftJoystick);
        robot.rightFront.setPower(rightJoystick);
        robot.rightBack.setPower(rightJoystick);

    }

    public void mecanumDrive(boolean dpad_left, boolean dpad_right) {

        if (dpad_left) {
            robot.leftBack.setPower(-1);
            robot.leftFront.setPower(1);
            robot.rightFront.setPower(-1);
            robot.rightBack.setPower(1);
        }
        else if (dpad_right) {
            robot.leftBack.setPower(1);
            robot.leftFront.setPower(-1);
            robot.rightFront.setPower(1);
            robot.rightBack.setPower(-1);
        }
    }

    public void encoderDriveCM(double cm, double speed) {
        int timeSec = driveTimeCM(cm, speed);
        int timeSec_spaced = (int)(timeSec*1.7);

    }

    public int driveTimeCM(double cm, double speed) {
        double abs_speed = Math.abs(speed * constants.NEVEREST_40_RPM);
        double abs_distCM = Math.abs(cm);

        double circ = constants.WHEEL_CIRCUMFERENCE_CM;
        double dist_perMin = (abs_speed * circ);
        double timeMin = (abs_distCM/dist_perMin);
        double timeSec = (timeMin*60);

        return (int)timeSec;
    }

}
