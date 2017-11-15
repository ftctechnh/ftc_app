package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by Eric on 11/9/2017.
 */

@Autonomous(name="SouthBlue ", group="Autonomisisisisis")
public class SouthRight extends LinearOpMode {
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
        //For kids //for ages 3-1020000000078605400000123
        waitForStartify();
        //
        //woo hoo
        //
        //Insert Code Here
        //12 went 9.5 inches, with a twist at the end?
        //
        jeffThePengwin.leftToPosition(5, .4);
        //
        //jeffThePengwin.forwardToPosition(12, .4);
        runtime.reset();
        waitify(15);
        jeffThePengwin.switcheroo();

        pengwinFin.moveFinDown();

        telemetry.addData("Color Blue Sensorify", pengwinFin.doesColorSensorSeeBlueJewel());
        telemetry.addData("Color Red Sensorify", !pengwinFin.doesColorSensorSeeBlueJewel());
        telemetry.addData("telemetry test", Math.random());
        telemetry.update();
        runtime.reset();
        while(runtime.seconds() < 60){

        }
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

    private void waitify(double time) {
        while (isCompletingTask(time)){
            getTelemetry(time);
        }
        //jeffThePengwin.powerInput = 0;

        //jeffThePengwin.bestowThePowerToAllMotors();
    }

    //
    //
    private void waitForStartify(){
        waitForStart();
    }
    //
    //
}