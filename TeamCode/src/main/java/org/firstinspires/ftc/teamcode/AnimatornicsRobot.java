package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class AnimatornicsRobot {

    private DcMotor lfMotor;
    private DcMotor lbMotor;
    private DcMotor rfMotor;
    private DcMotor rbMotor;

    private DcMotor collectMotor;
    private DcMotor flipMotor;
    private DcMotor liftMotor;
    private DcMotor collectSlideMotor;

    private Telemetry telemetry;

    private boolean holdLift = false;
    private ElapsedTime runtime = new ElapsedTime();

    public AnimatornicsRobot(HardwareMap hardwareMap, Telemetry telemetry) {

        this.telemetry = telemetry;
        lfMotor = hardwareMap.get(DcMotor.class, "lfmotor");
        lbMotor = hardwareMap.get(DcMotor.class, "lbmotor");
        rfMotor = hardwareMap.get(DcMotor.class, "rfmotor");
        rbMotor = hardwareMap.get(DcMotor.class, "rbmotor");

        collectMotor = hardwareMap.get(DcMotor.class, "collect_motor");
        collectSlideMotor = hardwareMap.get(DcMotor.class, "collect_slide_motor");
        flipMotor = hardwareMap.get(DcMotor.class, "flip_motor");
        liftMotor = hardwareMap.get(DcMotor.class, "lift_motor");

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
            double liftPower = op.gamepad1.left_trigger - op.gamepad1.right_trigger;
            liftMotor.setPower(liftPower);
            telemetry.addData("Status", "liftPower: " + liftPower);
        } else {
            double liftPower = 0.5;
            liftMotor.setPower(liftPower);
            telemetry.addData("Status", "liftPower: " + liftPower);
        }

        double collectPower = op.gamepad2.left_trigger - op.gamepad2.right_trigger;
        collectMotor.setPower(collectPower);
        telemetry.addData("Status", "collectPower: " + collectPower);

        double collectSlidePower = op.gamepad2.left_stick_y;
        collectSlideMotor.setPower(collectSlidePower);
        telemetry.addData("Status", "collectSlidePower_1: " + collectSlidePower);

        double flipPower = op.gamepad2.right_stick_y;
        flipMotor.setPower(flipPower);
        telemetry.addData("Status", "flipPower: " + flipPower);
    }

    public void moveLift(LinearOpMode op, double time, String direction, double liftPower) {

        liftMotor.setPower(liftPower);
        runtime.reset();
        while (op.opModeIsActive() && runtime.seconds() < time) {
            telemetry.addData("Path", "Lift:"+direction+": %2.5f S Elapsed", runtime.seconds());
            telemetry.update();
        }
        liftMotor.setPower(0.0);
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

    public void moveSlide(LinearOpMode op, double time, String direction, double collectSlidePower) {

        collectSlideMotor.setPower(collectSlidePower);
        runtime.reset();
        while (op.opModeIsActive() && runtime.seconds() < time) {
            telemetry.addData("Path", "Slide:"+direction+": %2.5f S Elapsed", runtime.seconds());
            telemetry.update();
        }
        collectSlideMotor.setPower(0.0);
    }
}
