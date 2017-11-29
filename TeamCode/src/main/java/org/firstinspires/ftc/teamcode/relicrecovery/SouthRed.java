package org.firstinspires.ftc.teamcode.relicrecovery;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;


/**
 * Created by Eric on 11/9/2017.
 */

@Autonomous(name="SouthRed", group="Autonomisisisisis")
public class SouthRed extends LinearOpMode {
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
        //
        waitForStartify();
        //
        pengwinArm.open();//close
        hurryUpAndWait(1);
        //
        jeffThePengwin.rightToPosition(5, .4);
        hurryUpAndWait(1);
        jeffThePengwin.switcheroo();
        //
        pengwinFin.moveFinDown();
        hurryUpAndWait(1);
        //
        if (pengwinFin.doesColorSensorSeeBlueJewel()){
            jeffThePengwin.turnLeftToPosition(4,0.4);
            hurryUpAndWait(1);
            pengwinFin.moveFinUp();
            hurryUpAndWait(1);
            jeffThePengwin.turnRightToPostion(4,0.4);
        }else{
            jeffThePengwin.turnRightToPostion(4,0.4);
            hurryUpAndWait(1);
            pengwinFin.moveFinUp();
            hurryUpAndWait(1);
            jeffThePengwin.turnLeftToPosition(4., 0.4);
        }
        hurryUpAndWait(2);
        gentlyPutTheMotorsToSleep();
        jeffThePengwin.forwardToPosition(27.5,0.4);
        hurryUpAndWait(5);
        //
        jeffThePengwin.leftToPosition(18,0.75);
        hurryUpAndWait(4);
        gentlyPutTheMotorsToSleep();
        //
        smartify();
        hurryUpAndWait(3);
        //
        pengwinArm.close();//open
        hurryUpAndWait(.5);
        //
        jeffThePengwin.forwardToPosition(1,.4);
        hurryUpAndWait(1);
        //
        pengwinArm.setUpPower(-0.4);
    }
    //
    //
    //
    private boolean isCompletingTask(double time) {
        runtime.reset();
        return opModeIsActive() &&
                (runtime.seconds() < time) &&
                (jeffThePengwin.isMoving());
    }

    private void hurryUpAndWait(double time){
        runtime.reset();
        while(runtime.seconds()<time && opModeIsActive()){
            //Do Nothing
        }
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
    private void gentlyPutTheMotorsToSleep() {
        jeffThePengwin.powerInput = 0;
        jeffThePengwin.bestowThePowerToAllMotors();
    }
    private void smartify(){//calibrate up position
        pengwinArm.upMotor.setPower(.4);//opposite of touchy
        while(jeffThePengwin.up.getState()){
            //TODO Wookie
        }
        pengwinArm.upMotor.setPower(0);//stop the motor
        pengwinArm.upPosition = pengwinArm.upMotor.getCurrentPosition();
    }
}