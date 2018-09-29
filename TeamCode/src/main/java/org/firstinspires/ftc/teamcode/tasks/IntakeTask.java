package org.firstinspires.ftc.teamcode.tasks;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

public class IntakeTask extends TaskThread {

    private DcMotor lIntake, rIntake;
    double lpower = 0;
    double rpower = 0;
    int checkDelay = 250;
    ElapsedTime timer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);

    public IntakeTask(LinearOpMode opMode) {
        this.opMode = opMode;
        initialize();
    }

    @Override
    public void run() {
        timer.reset();

        while (opMode.opModeIsActive() && running) {
/*
            if (opMode.gamepad1.dpad_up || opMode.gamepad2.dpad_up) {
                setRollerPower(1);
            } else if (opMode.gamepad1.dpad_down || opMode.gamepad2.dpad_down) {

                setRollerPower(-1);
            } else{
                setRollerPower(0);
            }*/
            tankControl();


        }
    }
//
/*
    public void setRollerPower(double power) {
        rIntake.setPower(power);
        lIntake.setPower(power);
    }
*/

    public void tankControl() {
//        if (opMode.gamepad1.dpad_up || opMode.gamepad2.dpad_up) {
//            lpower += 0.05;
//            Utils.waitFor(checkDelay );
//
//        }
//        if (opMode.gamepad2.dpad_down || opMode.gamepad1.dpad_down) {
//            lpower += -0.05;
//            Utils.waitFor(checkDelay );
//
//        }
//        if (opMode.gamepad2.y) {
//            rpower += 0.05;
//            Utils.waitFor(checkDelay );
//
//        }
//        if (opMode.gamepad2.a) {
//            rpower += -0.05;
//            Utils.waitFor(checkDelay );
//
//        }

        lpower = opMode.gamepad2.left_stick_y;
        rpower = opMode.gamepad2.right_stick_y;
            if(opMode.gamepad2.x){
                rpower = -.7;
                lpower = -.7;
            }
            else if(opMode.gamepad2.y){
                rpower = -.15;
                lpower = -.15;
            }
            else if (opMode.gamepad2.b) {
                rpower = -.9;
                lpower = -.9;
            } else if (opMode.gamepad2.a) {
                rpower = -.17;
                lpower = -.17;
            }
//            if (opMode.gamepad2.b) {
//                rpower = 0;
//                lpower = 0;
//            }
        lIntake.setPower(lpower);
        rIntake.setPower(rpower);
        opMode.telemetry.addData("lpower: ", lpower);
        opMode.telemetry.addData("rpower: ", rpower);
//        opMode.telemetry.update();

    }

//    public double getPower() {
//        return power;
//    }

       /*( if (opMode.gamepad1.dpad_up || opMode.gamepad2.dpad_up) {
            lIntake.setPower(-.5);
            rIntake.setPower(-0.7);
        } else if (opMode.gamepad1.b || opMode.gamepad2.b) {
            lIntake.setPower(-.45);
            rIntake.setPower(-0.6);
        } else if (opMode.gamepad1.x || opMode.gamepad2.x) {
            lIntake.setPower(-.35);
            rIntake.setPower(-0.5);
        } else if (opMode.gamepad1.dpad_down || opMode.gamepad2.dpad_down) {
            lIntake.setPower(-.65);
            rIntake.setPower(-0.85);`*/
//        }
//    }

    @Override
    public void initialize() {
        lIntake = opMode.hardwareMap.dcMotor.get("lIntake");
        rIntake = opMode.hardwareMap.dcMotor.get("rIntake");
        lIntake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rIntake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        lIntake.setDirection(DcMotorSimple.Direction.FORWARD);
        rIntake.setDirection(DcMotorSimple.Direction.REVERSE);
        lIntake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rIntake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

    }
}
