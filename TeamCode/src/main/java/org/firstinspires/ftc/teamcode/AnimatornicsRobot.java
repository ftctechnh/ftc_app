package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class AnimatornicsRobot {

    private DcMotor lfMotor;
    private DcMotor lbMotor;
    private DcMotor rfMotor;
    private DcMotor rbMotor;

    private DcMotor liftMotor_1;
    private DcMotor liftMotor_2;
    private DcMotor slideMotor;

    private Servo leftServo;
    private Servo rightServo;

    private Telemetry telemetry;

    private boolean holdLift = false;
    private ElapsedTime runtime = new ElapsedTime();

    public AnimatornicsRobot(HardwareMap hardwareMap, Telemetry telemetry) {

        this.telemetry = telemetry;
        lfMotor = hardwareMap.get(DcMotor.class, "lfmotor");
        lbMotor = hardwareMap.get(DcMotor.class, "lbmotor");
        rfMotor = hardwareMap.get(DcMotor.class, "rfmotor");
        rbMotor = hardwareMap.get(DcMotor.class, "rbmotor");

        slideMotor = hardwareMap.get(DcMotor.class, "slide_motor");
        liftMotor_1 = hardwareMap.get(DcMotor.class, "lift_motor_1");
        liftMotor_2 = hardwareMap.get(DcMotor.class, "lift_motor_2");

        leftServo = hardwareMap.get(Servo.class, "leftServo");
        rightServo = hardwareMap.get(Servo.class, "rightServo");

        telemetry.addData("Status", "DC motor variables initialized");
    }

    public void manualDrive(LinearOpMode op) {

        double lfPower = op.gamepad1.left_stick_y - op.gamepad1.left_stick_x;
        double lbPower = op.gamepad1.left_stick_y + op.gamepad1.left_stick_x;
        double rfPower = -(op.gamepad1.right_stick_y + op.gamepad1.left_stick_x);
        double rbPower = -(op.gamepad1.right_stick_y - op.gamepad1.left_stick_x);

        lfPower = Range.clip(lfPower, -1, 1);
        lbPower = Range.clip(lbPower, -1, 1);
        rfPower = Range.clip(rfPower, -1, 1);
        rbPower = Range.clip(rbPower, -1, 1);

        lfMotor.setPower(lfPower);
        lbMotor.setPower(lbPower);
        rfMotor.setPower(rfPower);
        rbMotor.setPower(rbPower);

        telemetry.addData("Status", "Changed Wheels powers");

        if(op.gamepad1.y) {
            holdLift = true;
        }
        if(op.gamepad1.x) {
            holdLift = false;
        }

        if(!holdLift) {
            double liftPower = op.gamepad1.right_trigger - op.gamepad1.left_trigger;
            liftPower = liftPower; // full power is too fast.
            liftMotor_1.setPower(liftPower);
            liftMotor_2.setPower(-liftPower);
            telemetry.addData("Status", "liftPower: " + liftPower);
        } else {
            double liftPower = -0.25;
            liftMotor_1.setPower(liftPower);
            liftMotor_2.setPower(-liftPower);
            telemetry.addData("Status", "liftPower: " + liftPower);
        }

        double slidePower = op.gamepad2.left_stick_y+op.gamepad2.right_stick_y;
        slidePower = Range.clip(slidePower, -1, 1);
        slidePower = slidePower; // full power is too fast.
        slideMotor.setPower(slidePower);
        telemetry.addData("Status", "slidePower: " + slidePower);

        double leftServoPower = op.gamepad2.left_trigger;
        double rightServoPower = op.gamepad2.right_trigger;
        if(leftServoPower < 0.75) {
            leftServo.setPosition(1.0);
        } else {
            leftServo.setPosition(0.0);
        }

        if(rightServoPower > 0.75) {
            rightServo.setPosition(1.0);
        } else {
            rightServo.setPosition(0.0);
        }
    }

    public void moveLift(LinearOpMode op, double time, String direction, double liftPower) {

        liftMotor_1.setPower(liftPower);
        liftMotor_2.setPower(-liftPower);
        runtime.reset();
        while (op.opModeIsActive() && runtime.seconds() < time) {
            telemetry.addData("Path", "Lift:"+direction+": %2.5f S Elapsed", runtime.seconds());
            telemetry.update();
        }
        liftMotor_1.setPower(0.0);
        liftMotor_2.setPower(0.0);
    }

    public void moveRobot(LinearOpMode op, double time, String direction, double lfPower, double lbPower, double rfPower, double rbPower) {

        lfMotor.setPower(lfPower);
        lbMotor.setPower(lbPower);
        rfMotor.setPower(rfPower);
        rbMotor.setPower(rbPower);
        runtime.reset();
        while (op.opModeIsActive() && runtime.seconds() < time) {
            telemetry.addData("Path", "Robot:"+direction+": %2.5f S Elapsed", runtime.seconds());
            telemetry.update();
        }
        lfMotor.setPower(0.0);
        lbMotor.setPower(0.0);
        rfMotor.setPower(0.0);
        rbMotor.setPower(0.0);
    }

    public void moveSlide(LinearOpMode op, double time, String direction, double slidePower) {

        slideMotor.setPower(slidePower);
        runtime.reset();
        while (op.opModeIsActive() && runtime.seconds() < time) {
            telemetry.addData("Path", "Slide:"+direction+": %2.5f S Elapsed", runtime.seconds());
            telemetry.update();
        }
        slideMotor.setPower(0.0);
    }
}
