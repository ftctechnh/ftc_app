package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.GyroSensor;

import java.util.ArrayList;
import java.util.List;

//Baby Balancing Robot Iteration 1(BBBot I1) created by Eric on 8/29/2017.

@TeleOp (name="ChristineIsCrying",group="Christine" )
//TODO console Christine
public class ChristineIsCrying extends LinearOpMode{
    //aka are my encoders playing nice

    DcMotor dcMotor1;
    DcMotor dcMotor2;
    DcMotor dcMotor3;
    DcMotor dcMotor4;
    List<DcMotor> dcMotors = new ArrayList<>();
    double power = 0;

    @Override    public void runOpMode() throws InterruptedException {

        setupMotors();
        stopAndResetAllMotors();
        runAllMotorsWithoutEncoder();
        waitForStart();

        while (opModeIsActive()){

            power = gamepad1.left_stick_y;
            setPowerOnAllMotors(power);

            if(gamepad1.a){
                stopAllMotors();
                stopAndResetAllMotors();
            }
            if(gamepad1.b){
                readyAllMotorsToRunToPosition();
                runAllMotorsToPosition(200);
                setPowerOnAllMotors(1);
            }
            getTelemetryOnAllMotors();
            telemetry.update();
        }
    }

    private void setupMotors() {
        dcMotor1 = hardwareMap.dcMotor.get("dc1");
        dcMotor2 = hardwareMap.dcMotor.get("dc2");
        dcMotor3 = hardwareMap.dcMotor.get("dc3");
        dcMotor4 = hardwareMap.dcMotor.get("dc4");


        dcMotors = new ArrayList<DcMotor>();

        dcMotors.add(dcMotor1);
        dcMotors.add(dcMotor2);
        dcMotors.add(dcMotor3);
        dcMotors.add(dcMotor4);
    }

    private void setPowerOnAllMotors(double power){
        for (DcMotor dcMotor :
                dcMotors) {
          //  if(dcMotor.getDirection()== DcMotorSimple.Direction.FORWARD){
                dcMotor.setPower(power);
           // }else{
          //      dcMotor.setPower(-power);
          //  }
        }
    }

    private void stopAllMotors(){
        setPowerOnAllMotors(0);
    }

    private void setModeOnAllMotors(DcMotor.RunMode runMode){
        for (DcMotor dcMotor: dcMotors){
            dcMotor.setMode(runMode);
        }
    }

    private void stopAndResetAllMotors(){
        setModeOnAllMotors(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    private void readyAllMotorsToRunToPosition(){
        setModeOnAllMotors(DcMotor.RunMode.RUN_TO_POSITION);
    }

    private void runAllMotorsToPosition(int position){
        for (DcMotor dcMotor: dcMotors){
            dcMotor.setTargetPosition(position);
        }
    }
    private void runAllMotorsWithEncoder(){
        setModeOnAllMotors(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    private void runAllMotorsWithoutEncoder(){
        setModeOnAllMotors(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    private void getTelemetryOnAllMotors(){
        for(DcMotor dcMotor: dcMotors){
            telemetry.addData(dcMotor.getConnectionInfo() + "current position:", dcMotor.getCurrentPosition() );
            telemetry.addData("direction:",dcMotor.getDirection());
            telemetry.addData("mode: ", dcMotor.getMode());
        }
    }

}
