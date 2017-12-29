package org.firstinspires.ftc.teamcode.relicrecovery;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by Eric on 11/9/2017.
 */

@Autonomous(name="NorthRed", group="Autonomisisisisis")
public class NorthRed extends LinearOpMode {
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
        pengwinFin.fin.setPosition(1.0);
        //
        waitForStartify();
        //
        pengwinArm.open();//close
        runtime.reset();
        while(runtime.seconds()<1 && opModeIsActive()){
            //Do Nothing
        }
        //
        pengwinFin.moveFinUp();
        runtime.reset();
        while(runtime.seconds()<.5 && opModeIsActive()){
            //Do Nothing
        }
        //
        jeffThePengwin.rightToPosition(5, .4);
        runtime.reset();
        while(runtime.seconds()<1 && opModeIsActive() && jeffThePengwin.isMoving()){
            //Do Nothing
        }
        jeffThePengwin.switcheroo();
        //
        pengwinFin.moveFinDown();
        runtime.reset();
        while(runtime.seconds()<1 && opModeIsActive()){
            //Do Nothing
        }
        //
        if (pengwinFin.doesColorSensorSeeBlueJewel()){
            jeffThePengwin.turnLeftToPosition(3,0.4);
            runtime.reset();
            while(runtime.seconds()<1 && opModeIsActive()&& jeffThePengwin.isMoving()){
                //Do Nothing
            }
            pengwinFin.moveFinUp();
            runtime.reset();
            while(runtime.seconds()<1 && opModeIsActive()){
                //Do Nothing
            }
            jeffThePengwin.turnRightToPostion(3,0.4);
        }else{
            jeffThePengwin.turnRightToPostion(4,0.4);
            runtime.reset();
            while(runtime.seconds()<1 && opModeIsActive()&& jeffThePengwin.isMoving()){
                //Do Nothing
            }
            pengwinFin.moveFinUp();
            runtime.reset();
            while(runtime.seconds()<1 && opModeIsActive()){
                //Do Nothing
            }
            jeffThePengwin.turnLeftToPosition(4., 0.4);
        }
        runtime.reset();
        while(runtime.seconds()<2 && opModeIsActive()&& jeffThePengwin.isMoving()){
            //Do Nothing
        }
        gentlyPutTheMotorsToSleep();
        jeffThePengwin.forwardToPosition(28,0.4);
        runtime.reset();
        while(runtime.seconds()<5 && opModeIsActive()&& jeffThePengwin.isMoving()){
            //Do Nothing
        }
        //
        jeffThePengwin.turnRightToPostion(24, .4);
        runtime.reset();
        while(runtime.seconds()<4 && opModeIsActive()&& jeffThePengwin.isMoving()){
            //Do Nothing
        }
        gentlyPutTheMotorsToSleep();
        //
        jeffThePengwin.backToPosition(3, .4);
        runtime.reset();
        while(runtime.seconds()<1 && opModeIsActive()&& jeffThePengwin.isMoving()){
            //Do Nothing
        }
        //
        smartify();
        runtime.reset();
        while(runtime.seconds()<3 && opModeIsActive()){
            //Do Nothing
        }
        //
        pengwinArm.close();//open
        runtime.reset();
        while(runtime.seconds()<.5 && opModeIsActive()){
            //Do Nothing
        }
        //
        jeffThePengwin.forwardToPosition(1,.4);
        runtime.reset();
        while(runtime.seconds()<1 && opModeIsActive()&& jeffThePengwin.isMoving()){
            //Do Nothing
        }
        //
        jeffThePengwin.backToPosition(5, .4);
        runtime.reset();
        while(runtime.seconds()<1 && opModeIsActive()){
            //Do Nothing
        }
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