package org.firstinspires.ftc.teamcode.Salsa.Robots;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Salsa.Constants;
import org.firstinspires.ftc.teamcode.Salsa.Hardware.Robot;
import org.opencv.core.Mat;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;


/**
 * Created by adityamavalankar on 11/5/18.
 */

public class Asteroid {

    public Robot robot = new Robot();
    public Constants constants;
    private LinearOpMode linearOpMode;
    private OpMode opMode;
    private ElapsedTime runtime = new ElapsedTime();
    private Telemetry telemetry;


    public void drive() {

        drive(opMode.gamepad1.left_stick_y, opMode.gamepad1.right_stick_y);
    }

    public void drive(double leftJoystick, double rightJoystick) {

        robot.leftBack.setPower(leftJoystick);
        robot.leftFront.setPower(leftJoystick);
        robot.rightFront.setPower(rightJoystick);
        robot.rightBack.setPower(rightJoystick);

    }

    public void liftHanger(double g2_rightJoystick) {

     robot.liftSlides.setPower(g2_rightJoystick * constants.LIFT_SLIDES_REVERSE_CONSTANT);

    }

    public void liftHanger() {

        liftHanger(opMode.gamepad2.right_stick_y);
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

    public void setTargetPosition(int target) {
        robot.leftFront.setTargetPosition(robot.leftFront.getCurrentPosition() + target);
        robot.rightFront.setTargetPosition(robot.rightFront.getCurrentPosition() + target);
        robot.leftBack.setTargetPosition(robot.leftBack.getCurrentPosition() + target);
        robot.rightBack.setTargetPosition(robot.rightBack.getCurrentPosition() + target);
    }

    public void setPower(double power) {
        robot.leftFront.setPower(power);
        robot.leftBack.setPower(power);
        robot.rightFront.setPower(power);
        robot.rightBack.setPower(power);

    }

    public void encoderDriveCM(double cm, double speed) {
        int timeSec = driveTimeCM(cm, speed);
        int timeSec_spaced = (int)(timeSec*constants.ENC_DRIVE_TIME_MULTIPLIER);

        int distanceEnc = (int)(constants.TICKS_PER_CM * cm);

        setTargetPosition(distanceEnc);
        setMotorRunMode(DcMotor.RunMode.RUN_TO_POSITION);

        setPower(Math.abs(speed));
        runtime.reset();

        while(linearOpMode.opModeIsActive() && (runtime.seconds() < timeSec_spaced) && robot.leftFront.isBusy()
                && robot.leftBack.isBusy() && robot.rightFront.isBusy() && robot.rightBack.isBusy()) {
            telemetry.addLine("Robot in Encoder Drive");
            telemetry.addData("Target Distance (cm)", cm);
            telemetry.update();
        }

        setPower(0);

        setMotorRunMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }

    public void setMotorRunMode(DcMotor.RunMode runmode) {
        robot.leftFront.setMode(runmode);
        robot.rightFront.setMode(runmode);
        robot.leftBack.setMode(runmode);
        robot.rightBack.setMode(runmode);
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
