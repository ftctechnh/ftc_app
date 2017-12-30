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

    /**
     * This is a model class to hold North red
     * @see java.lang.Object
     * @author Eric and Nora
     */
    public NorthRed(){

    }
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
        /**
         * the arm closed to grab the glyph
         * @see java.lang.Object
         * @author Eric and Nora
         */
        //
        pengwinArm.open();//close
        runtime.reset();
        while(runtime.seconds()<1 && opModeIsActive()){
            //Do Nothing
        }
        /**
         * keep the fin up while closing
         * @see java.lang.Object
         * @author Eric and Nora
         */
        //
        pengwinFin.moveFinUp();
        runtime.reset();
        while(runtime.seconds()<.5 && opModeIsActive()){
            //Do Nothing
        }
        /**
         * move the robot to get close to the jewel
         * @see java.lang.Object
         * @author Eric and Nora
         */
        //
        jeffThePengwin.rightToPosition(5, .4);
        runtime.reset();
        while(runtime.seconds()<1 && opModeIsActive() && jeffThePengwin.isMoving()){
            //Do Nothing
        }
        jeffThePengwin.switcheroo();
        /**
         * moves the fin down to hit the ball
         * @see java.lang.Object
         * @author Eric and Nora
         */
        //
        pengwinFin.moveFinDown();
        runtime.reset();
        while(runtime.seconds()<1 && opModeIsActive()){
            //Do Nothing
        }

        /**
         *dedcide whether the ball is blue or red and hit it
         * @see java.lang.Object
         * @author Eric and Nora
         */
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

            /**
             *move the fin back up for easy access
             * @see java.lang.Object
             * @author Eric and Nora
             */
            //
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

        /**
         *stop the motors and then go forward
         * @see java.lang.Object
         * @author Eric and Nora
         */
        //
        gentlyPutTheMotorsToSleep();
        jeffThePengwin.forwardToPosition(28,0.4);
        runtime.reset();
        while(runtime.seconds()<5 && opModeIsActive()&& jeffThePengwin.isMoving()){
            //Do Nothing
        }

        /**
         *turn right to line up with the glyph box
         * @see java.lang.Object
         * @author Eric and Nora
         */
        //
        jeffThePengwin.turnRightToPostion(24, .4);
        runtime.reset();
        while(runtime.seconds()<4 && opModeIsActive()&& jeffThePengwin.isMoving()){
            //Do Nothing
        }
        gentlyPutTheMotorsToSleep();

        /**
         *move the robot back to place the block
         * @see java.lang.Object
         * @author Eric and Nora
         */
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
        /**
         *open the arm to let the gylph go
         * @see java.lang.Object
         * @author Eric and Nora
         */
        //
        pengwinArm.close();//open
        runtime.reset();
        while(runtime.seconds()<.5 && opModeIsActive()){
            //Do Nothing
        }

        /**
         *move torwards the glyph case to push the block back in
         * @see java.lang.Object
         * @author Eric and Nora
         */
        //
        jeffThePengwin.forwardToPosition(1,.4);
        runtime.reset();
        while(runtime.seconds()<1 && opModeIsActive()&& jeffThePengwin.isMoving()){
            //Do Nothing
        }

        /**
         *move back after pushing in back
         * @see java.lang.Object
         * @author Eric and Nora
         */
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
    //do the telemetrys
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