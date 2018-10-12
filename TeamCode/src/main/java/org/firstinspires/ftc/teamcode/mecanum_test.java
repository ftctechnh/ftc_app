package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.motors.NeveRest40Gearmotor;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import static com.qualcomm.robotcore.util.Range.scale;
import static java.lang.Math.abs;

/**
 * Created by thund on 9/30/2017.
 */

@TeleOp(name="mecanum_test",group="BBBot" )
@Disabled
public class mecanum_test extends LinearOpMode {
    HardwareSensorMap robot   = new HardwareSensorMap();   // Use a Pushbot's hardware
    DcMotor leftRear;
    DcMotor rightRear;
    DcMotor leftFront;
    DcMotor rightFront;

//    @Override
//    public void init() {
//        robot.init(hardwareMap);
//    }

    @Override
    public void runOpMode() throws InterruptedException {
        //
        //
        leftRear = hardwareMap.dcMotor.get("m0");
        rightRear = hardwareMap.dcMotor.get("m1");
        leftFront = hardwareMap.dcMotor.get("m2");
        rightFront = hardwareMap.dcMotor.get("m3");

        waitForStart();
        double drive;
        double turn;
        double left;
        double right;
        double max;

        double lrPower;
        double rrPower;
        double lfPower;
        double rfPower;

        /*
        while (true) {
            m1.setPower(wheelSpeed(0.5,Math.PI / 4,0.5,"motor_1"));
            m2.setPower(wheelSpeed(0.5,Math.PI / 4,0.5,"motor_2"));
        }
        */
        while (opModeIsActive()){
            //start: define turn
            drive = -gamepad1.left_stick_y; //turn power

            turn = gamepad1.right_stick_x; //turn direction

            left  = drive + turn;
            right = drive - turn;
            //End: define turn

            max = Math.max(abs(left), abs(right)); //
            if (max > 1.0)
            {
                left /= max;
                right /= max;
            }

            telemetry.addData("left",  "%.2f", left);
            telemetry.addData("right", "%.2f", right);
            telemetry.addData("drive", drive);
            telemetry.addData("turn", turn);
            telemetry.update();

            lrPower = -wheelSpeed(drive, (Math.PI / 2) * turn, turn, "LR");
            rrPower = -wheelSpeed(drive, (Math.PI / 2) * turn, turn, "RR");
            lfPower = -wheelSpeed(drive, (Math.PI / 2) * turn, turn, "LF");
            rfPower = -wheelSpeed(drive, (Math.PI / 2) * turn, turn, "RF");

            double r = Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y);
            double robotAngle = Math.atan2(gamepad1.left_stick_y, gamepad1.left_stick_x) - Math.PI / 4;
            double rightX = -gamepad1.right_stick_x;


            final double v1 = r * Math.cos(robotAngle) + rightX;
            final double v2 = r * Math.sin(robotAngle) - rightX;
            final double v3 = r * Math.sin(robotAngle) + rightX;
            final double v4 = r * Math.cos(robotAngle) - rightX;


            leftFront.setPower(v1);
            rightFront.setPower(v2);
            leftRear.setPower(v3);
            rightRear.setPower(v4);


            //telemetry.addData("LR",  "%.2f", lrPower);
            telemetry.addData("newLR",  "%.2f", v3);
            //telemetry.addData("RR",  "%.2f", rrPower);
            telemetry.addData("newRR",  "%.2f", v4);
            //telemetry.addData("LF",  "%.2f", lfPower);
            telemetry.addData("newLF",  "%.2f", v1);
            //telemetry.addData("RF",  "%.2f", rfPower);
            telemetry.addData("newRF",  "%.2f", v2);

/*
            leftRear.setPower(lrPower);
            rightRear.setPower(rrPower);
            leftFront.setPower(lfPower);
            rightFront.setPower(rfPower);
            */

        }

    }

    /**
     *
     * @param Vd = desired robot speed [-1, 1]
     * @param Td = desired robot angle [0, 2pi]
     * @pa  ram Vt = desired speed for changing direction [-1, 1]
     * @param wheel
     * @return
     */
    public double wheelSpeed(double Vd, double Td, double Vt, String wheel) {
        double V = 0;

        if (wheel == "LF" || wheel == "RR") {
            V = Vd * Math.sin(Td + (Math.PI / 4)) + Vt;
        }

        if (wheel == "RF" || wheel == "LR") {
            V = Vd * Math.cos(Td + (Math.PI / 4)) + Vt;
        }

        return V;
    }


}
