//package org.firstinspires.ftc.teamcode;
//
//import com.qualcomm.robotcore.hardware.DcMotor;
//import com.qualcomm.robotcore.hardware.HardwareMap;
//import com.qualcomm.robotcore.hardware.Servo;
//import com.qualcomm.robotcore.util.ElapsedTime;
//
////This class defines all the specific hardware for a the BACONbot robot.
//
//public class HolonomicHardwareClass {
//    /* Public OpMode members. */
//    public DcMotor frontLeftMotor = null;
//    public DcMotor frontRightMotor = null;
//    public DcMotor backLeftMotor = null;
//    public DcMotor backRightMotor = null;
////    public DcMotor verticalArmMotor = null;
////    public Servo   clawServo = null;
//
//
//    /* Give place holder values for the motors and the grabber servo */
//    double FrontLeftPower = 0;
//    double FrontRightPower = 0;
//    double BackRightPower = 0;
//    double BackLeftPower = 0;
//
//    /* local OpMode members. */
//    HardwareMap hwMap = null;
//    private ElapsedTime period = new ElapsedTime();
//
//    /* Constructor */
//    public HolonomicHardwareClass() {
//
//    }
//
//    /* Initialize standard Hardware interfaces */
//    public void init(HardwareMap ahwMap) {
//        // Save reference to Hardware map
//        hwMap = ahwMap;
//
//        // Define and Initialize Hardware
//
//        // Define and Initialize Hardware
//        frontLeftMotor = hwMap.dcMotor.get("FL");
//        frontRightMotor = hwMap.dcMotor.get("FR");
//        backLeftMotor = hwMap.dcMotor.get("BL");
//        backRightMotor = hwMap.dcMotor.get("BR");
////        verticalArmMotor = hwMap.dcMotor.get("VAM");
////        clawServo =  hwMap.servo.get("CS");
//
//        // Set all hardware to default position
//        // Set all hardware to default position
//        frontLeftMotor.setPower(0);
//        frontRightMotor.setPower(0);
//        backLeftMotor.setPower(0);
//        backRightMotor.setPower(0);
////        verticalArmMotor.setPower(0);
////        clawServo.setPosition(0);
//
//
//        // Set proper encoder state for all motor
//        frontLeftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//        frontRightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//        backLeftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//        backRightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//
//
////        // Set proper encoder state for all motor
////        frontLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
////        frontRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
////        backLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
////        backRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//
//    }
//
//}
