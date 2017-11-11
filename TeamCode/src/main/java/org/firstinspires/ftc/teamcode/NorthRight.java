package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by Eric on 11/9/2017.
 */

@Autonomous(name="NorthRight", group="Autonomisisisisis")
public class NorthRight extends LinearOpMode {
    PengwinArm pengwinArm;
    PengwinFin pengwinFin;
    JeffThePengwin jeffThePengwin;
    private ElapsedTime runtime = new ElapsedTime();
    //

    //
    //Push
    @Override
    public void runOpMode() throws InterruptedException {
        jeffThePengwin = new JeffThePengwin(hardwareMap);
        pengwinArm = new PengwinArm(hardwareMap);
        pengwinFin = new PengwinFin(hardwareMap);
        //
        jeffThePengwin.startify();

        //For kids
        waitForStartify();
        //
        //woo hoo
        //
        //Insert Code Here
        jeffThePengwin.forwardToPosition(12, .4);
        runtime.reset();
        justWaitForSafety(15);
        jeffThePengwin.switcheroo();
    }
    //
    //
    //


    private boolean isCompletingTask(double time) {
        return opModeIsActive() &&
                (runtime.seconds() < time) &&
                (jeffThePengwin.isMoving());
    }

    private void getTelemetry(double time) {
        telemetry.addData("lbm position", jeffThePengwin.leftBackMotor.getCurrentPosition());
        telemetry.addData("lfm position", jeffThePengwin.leftFrontMotor.getCurrentPosition());
        telemetry.addData("rbm position", jeffThePengwin.rightBackMotor.getCurrentPosition());
        telemetry.addData("rfm position", jeffThePengwin.rightFrontMotor.getCurrentPosition());
        telemetry.addData("fin position", pengwinFin.fin.getPosition());
        telemetry.addData("Progress", runtime.seconds() / time + "%");
        telemetry.update();
    }

    //

    private void justWaitForSafety(double time) {
        while (isCompletingTask(time)){
            getTelemetry(time);
        }
    }

    //
    //
    private void waitForStartify(){
        waitForStart();
    }
    //
    //
}