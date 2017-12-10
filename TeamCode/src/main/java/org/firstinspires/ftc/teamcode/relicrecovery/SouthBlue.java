package org.firstinspires.ftc.teamcode.relicrecovery;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.relicrecovery.JeffThePengwin;
import org.firstinspires.ftc.teamcode.relicrecovery.PengwinArm;
import org.firstinspires.ftc.teamcode.relicrecovery.PengwinFin;

/**
 * Created by Eric on 11/9/2017.
 */

@Autonomous(name="SouthBlue ", group="Autonomisisisisis")
public class SouthBlue extends LinearOpMode {
    PengwinArm pengwinArm;
    PengwinFin pengwinFin;
    JeffThePengwin jeffThePengwin;
    private ElapsedTime runtime = new ElapsedTime();
    //
    //
    //Push
    @Override
    public void runOpMode() throws InterruptedException{
        jeffThePengwin = new JeffThePengwin(hardwareMap);
        pengwinArm = new PengwinArm(hardwareMap);
        pengwinFin = new PengwinFin(hardwareMap);
        //
        jeffThePengwin.startify();
        pengwinFin.moveFinUp();
        // pengwinArm.upMotor.setPower(-0.45);
        //For kids //for ages 3-1020000000078605400000123
        waitForStart();
        //
        pengwinArm.open();//close
        jeffThePengwin.rightToPosition(2.5, .4);
        //
        //jeffThePengwin.forwardToPosition(12, .4);
        runtime.reset();
        while (runtime.seconds()<1 && opModeIsActive()){
            //Do Nothing
        }
        pengwinFin.moveFinDown();
        runtime.reset();
        while(runtime.seconds()<1 && opModeIsActive()){
            //doNothing
        }
        //YAY
        //WE LOVE DOING NOTHING
        if (!pengwinFin.doesColorSensorSeeBlueJewel()){
            jeffThePengwin.turnLeftToPosition(2,0.4);
            runtime.reset();
            while (runtime.seconds()<2 && opModeIsActive()){
                //Do nothing
            }
            pengwinFin.moveFinUp();
            runtime.reset();
            while (runtime.seconds()<1 && opModeIsActive()){
                //Do nothing
            }
            jeffThePengwin.turnRightToPostion(5,0.4);
        }
        else {
            jeffThePengwin.turnRightToPostion(5,0.4);
            runtime.reset();
            while (runtime.seconds()<2 && opModeIsActive()){
                //Do nothing
            }
            pengwinFin.moveFinUp();
            runtime.reset();
            while (runtime.seconds()<1 && opModeIsActive()){
                //Do nothing
            }
            jeffThePengwin.turnLeftToPosition(5,0.4);
        }
        runtime.reset();
        while (runtime.seconds()<2 && opModeIsActive()){
            //Do nothing
        }
        gentlyPutTheMotorsToSleep();
        jeffThePengwin.backToPosition(25, 0.4);
        runtime.reset();
        while (runtime.seconds()<6 && opModeIsActive()){
            //Do nothing
        }
        jeffThePengwin.turnRightToPostion(40,1);
        gentlyPutTheMotorsToSleep();
        jeffThePengwin.rightToPosition(10, 0.4);
        runtime.reset();
        while (runtime.seconds()<2 && opModeIsActive()){
            //Do nothing
        }
        gentlyPutTheMotorsToSleep();

        //
        runtime.reset();
        smartify();
        runtime.reset();

        pengwinArm.close();//open
        runtime.reset();
        while(runtime.seconds()<.5 && opModeIsActive()){
            //Do Nothing
        }
        //
        jeffThePengwin.forwardToPosition(1,.4);
        runtime.reset();
        while(runtime.seconds()<1 && opModeIsActive()){
            //Do Nothing
        }
    }
    //
    //
    //


    private void gentlyPutTheMotorsToSleep() {
        jeffThePengwin.powerInput = 0;
        jeffThePengwin.bestowThePowerToAllMotors();
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
    //
    private void getTelemetry(double time) {
        telemetry.addData("lbm position", jeffThePengwin.leftBackMotor.getCurrentPosition());
        telemetry.addData("lfm position", jeffThePengwin.leftFrontMotor.getCurrentPosition());
        telemetry.addData("rbm position", jeffThePengwin.rightBackMotor.getCurrentPosition());
        telemetry.addData("rfm position", jeffThePengwin.rightFrontMotor.getCurrentPosition());
        telemetry.addData("fin position", pengwinFin.fin.getPosition());
        telemetry.addData("Progress", runtime.seconds() / time + "%");
        telemetry.addData("Color Blue Sensorify", pengwinFin.colorSensor.blue());
        telemetry.addData("Color Red Sensorify", pengwinFin.colorSensor.red());
        telemetry.addData("telemetry test", Math.random());
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
    private void smartify(){//calibrate up position
        pengwinArm.upMotor.setPower(.4);//opposite of touchy
        while(jeffThePengwin.up.getState()){
            //TODO Wookie
        }
        pengwinArm.upMotor.setPower(0);//stop the motor
        pengwinArm.upPosition = pengwinArm.upMotor.getCurrentPosition();
    }
}