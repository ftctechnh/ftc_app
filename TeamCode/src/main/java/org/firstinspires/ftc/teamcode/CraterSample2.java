package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;


/** DRIVETRAIN CONFIGURATION
 *      front
 *      C   D
 * left       right
 *      B   A
 *      back
 */



@Autonomous(name = "CraterSample2", group = "Auto")
public class CraterSample2 extends LinearOpMode {

    private DcMotor aDrive = null;
    private DcMotor bDrive = null;
    private DcMotor cDrive = null;
    private DcMotor dDrive = null;
    private DcMotor aMech = null;
    private DcMotor bMech = null;
    private DcMotor cMech = null;
    private DcMotor dMech = null;

    private double forward = 0;
    private double turn = 0;


    private String[] titles = new String[] {"armpower", "armtime", "armdist", "turn0power", "turn0time", "forward0power", "forward0time", "turn1power", "turn1time"} ;
    private double[] values = new double[] {    0.3,           2,      1000,        0.3,          3,             0.3,             5,            0.3,           7  };
    private Tuner tuner;

    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {
        /////////////////////////////////////////////////////////// INIT


        aDrive = hardwareMap.get(DcMotor.class, "1-0");
        aDrive.setDirection(DcMotorSimple.Direction.FORWARD);
        bDrive = hardwareMap.get(DcMotor.class, "1-1");
        bDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        cDrive = hardwareMap.get(DcMotor.class, "1-2");
        cDrive.setDirection(DcMotorSimple.Direction.FORWARD);
        dDrive = hardwareMap.get(DcMotor.class, "1-3");
        dDrive.setDirection(DcMotorSimple.Direction.REVERSE);

        aMech = hardwareMap.get(DcMotor.class, "2-0");
        bMech = hardwareMap.get(DcMotor.class, "2-1");
        cMech = hardwareMap.get(DcMotor.class, "2-2");
        dMech = hardwareMap.get(DcMotor.class, "2-3");

        tuner = new Tuner(titles, values, gamepad1, telemetry);

        telemetry.update();
        /////////////////////////////////////////////////////////// END INIT

        while (!opModeIsActive()){
            tuner.tune();
        }

        telemetry.addData("armpos",cMech.getCurrentPosition());
        telemetry.update();


        waitForStart();
        runtime.reset();

        telemetry.addData("armpos",cMech.getCurrentPosition());
        telemetry.update();

        int target = cMech.getCurrentPosition() + 21000;
        while(cMech.getCurrentPosition() < target){
            cMech.setPower(0.5);
            telemetry.addData("armpos",cMech.getCurrentPosition());
            telemetry.update();
        }
        cMech.setPower(0);

        runtime.reset();
        while(runtime.seconds()<0.5){
            turn = 0.5;
            forward=0;
            drive(forward,turn);
        }
        pause();






    }




    private Boolean timedAction(double start, double end){
        return (runtime.seconds() > start && runtime.seconds() < end);
    }

    private void drive(double forward, double turn){
        aDrive.setPower(forward + turn);
        bDrive.setPower(forward - turn);
        cDrive.setPower(forward - turn);
        dDrive.setPower(forward + turn);
    }

    private void pause(){
        aDrive.setPower(0);
        bDrive.setPower(0);
        cDrive.setPower(0);
        dDrive.setPower(0);
        aMech.setPower(0);
        bMech.setPower(0);
        cMech.setPower(0);
        dMech.setPower(0);
    }


}
