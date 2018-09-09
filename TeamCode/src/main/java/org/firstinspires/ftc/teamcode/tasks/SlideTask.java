package org.firstinspires.ftc.teamcode.tasks;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by Howard on 10/15/16.
 */
public class SlideTask extends TaskThread {

    private DcMotor lSlide , rSlide;
    int lZeroPosition, rZeroPosition;
    int inchPos;
    int encodertoInchConversion = 190;
    int pos0 = 0;
    int pos3 = 13   ;
    int pos4 = 19;

    TouchSensor lTouch, rTouch;
    ElapsedTime timer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);

    public SlideTask(LinearOpMode opMode) {
        this.opMode = opMode;
        initialize();
    }

    @Override
    public void run() {
        timer.reset();
        while (opMode.opModeIsActive()/* && running*/) {
            long start  = System.currentTimeMillis();
            int debounce = 200;
            if (opMode.gamepad1.left_bumper || opMode.gamepad2.left_bumper) {
                opMode.telemetry.addLine("down");
                setSlidePower(-.8);
            } else if (opMode.gamepad1.right_bumper || opMode.gamepad2.right_bumper) {
                setSlidePower(.8);
                opMode.telemetry.addLine("up");

            } else{
                setSlidePower(0);
                opMode.telemetry.addLine("idle");

            }
//            opMode.telemetry.update();

//            if(opMode.gamepad1.a || opMode.gamepad2.a){
//                setSlidePosition(pos0,3);
//                zeroSlides();
//            }
//            if(opMode.gamepad1.x || opMode.gamepad2.x){
//                setSlidePosition(pos3,3);
//            }
//            if(opMode.gamepad1.y || opMode.gamepad2.y){
//                setSlidePosition(pos4,3);
//            }

        }
    }

    public void setSlidePower(double power) {
        rSlide.setPower(power);
        lSlide.setPower(power);
    }
     public void zeroSlides(){
       /* while(!lTouch.isPressed() && !rTouch.isPressed() && running){
            if(!rTouch.isPressed()){
                rSlide.setPower(-1);
            }
            if(!lTouch.isPressed()){
                lSlide.setPower(-1);
            }
        }*/
        resetSlideEncoders();
        lZeroPosition = lSlide.getCurrentPosition();
        rZeroPosition = rSlide.getCurrentPosition();
        opMode.telemetry.addData("lSlideZero", lZeroPosition);
        opMode.telemetry.addData("rSlideZero", rZeroPosition);
//        opMode.telemetry.update();


     }


     public void resetSlideEncoders(){
         lSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
         lSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
         rSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
         rSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
     }

     public double encoderToInch(int encoderPos) {
        return (encoderPos / encodertoInchConversion);
     }

    public int inchToEncoder(int inchPos) {
        return (inchPos*encodertoInchConversion);
    }

    public void setSlidePosition(int posInch, int timeoutS){
         boolean up = true;
//        lSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        rSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        int newLTarget = inchToEncoder(posInch);
        int newRTarget = inchToEncoder(posInch);
        // Determine new target position, and pass to motor controller

        lSlide.setTargetPosition(newLTarget);
        rSlide.setTargetPosition(newRTarget);
        // Turn On RUN_TO_POSITION

        lSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // reset the timeout time and start motion.
        timer.reset();

        if(lSlide.getCurrentPosition()<= newLTarget && rSlide.getCurrentPosition()<= newRTarget) {
            setSlidePower(1);
            up = true;
        } else{
            setSlidePower(-1);
            up = false;
        }

        // keep looping while we are still active, and there is time left, and both motors are running.
        while (opMode.opModeIsActive() && (timer.time()< timeoutS*1000) && running) {
            opMode.telemetry.addData("l target ", newLTarget);
            opMode.telemetry.addData("r target ", newRTarget);
            opMode.telemetry.addData("lpos ", lSlide.getCurrentPosition());
            opMode.telemetry.addData("rpos ", rSlide.getCurrentPosition());
//            opMode.telemetry.update();
            /*
            if(!lSlide.isBusy()){
                    lSlide.setPower(0);
                }
            if(!rSlide.isBusy()){
                rSlide.setPower(0);
            }*/
            /*if(up){
                if(rSlide.getCurrentPosition() >= newRTarget){
                    rSlide.setPower(0);
                }
                if(lSlide.getCurrentPosition() >= newRTarget){
                    lSlide.setPower(0);
                }
            } else{
                if(rSlide.getCurrentPosition() <= newRTarget){
                    rSlide.setPower(0);
                }
                if(lSlide.getCurrentPosition() <= newRTarget){
                    lSlide.setPower(0);
                }
            }*/
        }
        setSlidePower(0);
        // Stop all motion
        opMode.telemetry.addData("l target ", newLTarget);
        opMode.telemetry.addData("r target ", newRTarget);
        opMode.telemetry.addData("lpos ", lSlide.getCurrentPosition());
        opMode.telemetry.addData("rpos ", rSlide.getCurrentPosition());
        opMode.telemetry.addLine("done ");
//        opMode.telemetry.update();

        // Turn off RUN_TO_POSITION
        rSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }
    @Override
    public void initialize() {
        lSlide = opMode.hardwareMap.dcMotor.get("lSlide");
        rSlide = opMode.hardwareMap.dcMotor.get("rSlide");
        lSlide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rSlide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        lSlide.setDirection(DcMotorSimple.Direction.FORWARD);
        rSlide.setDirection(DcMotorSimple.Direction.REVERSE);
        lSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        //lTouch = opMode.hardwareMap.touchSensor.get("lTouch");
        //rTouch = opMode.hardwareMap.touchSensor.get("rTouch");
//        zeroSlides();
    }
}
