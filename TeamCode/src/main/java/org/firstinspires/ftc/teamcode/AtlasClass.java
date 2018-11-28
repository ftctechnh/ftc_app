package org.firstinspires.ftc.teamcode;

//import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

//import org.firstinspires.ftc.robotcore.external.Telemetry;


public class AtlasClass {
    DcMotor flipM, lbDrive, rfDrive, lfDrive, rbDrive, liftM, extendM, collectM;
    Servo deliveryS;

    AtlasClass(HardwareMap hardwareMap) throws InterruptedException {
        rbDrive = hardwareMap.dcMotor.get("rbDriveM");
        rbDrive.setPower(0);
        rbDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        lbDrive = hardwareMap.dcMotor.get("lbDriveM");
        lbDrive.setPower(0);
        lbDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        rfDrive = hardwareMap.dcMotor.get("rfDriveM");
        rfDrive.setPower(0);
        rfDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        lfDrive = hardwareMap.dcMotor.get("lfDriveM");
        lfDrive.setPower(0);
        lfDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        flipM = hardwareMap.dcMotor.get("flipM");
        flipM.setPower(0);
        flipM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        liftM = hardwareMap.dcMotor.get("liftM");
        liftM.setPower(0);
        liftM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        extendM = hardwareMap.dcMotor.get("extendM");
        extendM.setPower(0);
        extendM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        collectM = hardwareMap.dcMotor.get("collectM");
        collectM.setPower(0);
        collectM.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        deliveryS = hardwareMap.servo.get("deliveryS");
        deliveryS.setPosition(0);


    }


}