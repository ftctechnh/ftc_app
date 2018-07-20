package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IntegratingGyroscope;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import java.lang.Math;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
@Disabled
@TeleOp(name="Shoddier Tele OP", group="Linear Opmode")
public class ShoddierTeleOP extends LinearOpMode {

    // Declare OpMode members.
    ElapsedTime timer = new ElapsedTime();
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor ADrive;
    private DcMotor BDrive;
    private DcMotor CDrive;
    private IntegratingGyroscope gyro;
    private ModernRoboticsI2cGyro MRI2CGyro;
    private DcMotor Shoulder;
    private DcMotor Elbow;
    private Servo RF;
    private Servo LF;
    private Servo Wrist;
    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        ADrive = hardwareMap.get(DcMotor.class, "A");
        BDrive = hardwareMap.get(DcMotor.class, "B");
        CDrive = hardwareMap.get(DcMotor.class, "C");
        Shoulder = hardwareMap.get(DcMotor.class, "shoulder");
        Elbow = hardwareMap.get(DcMotor.class, "elbow");
        RF = hardwareMap.get(Servo.class, "right finger");
        LF = hardwareMap.get(Servo.class, "left finger");
        Wrist = hardwareMap.get(Servo.class, "wrist");
        MRI2CGyro = hardwareMap.get(ModernRoboticsI2cGyro.class, "gyro");
        gyro = (IntegratingGyroscope)MRI2CGyro;
        telemetry.log().add("Gyro Calibrating. Do Not Move!");
        MRI2CGyro.calibrate();
        // Wait until the gyro calibration is complete
        timer.reset();
        while (!isStopRequested() && MRI2CGyro.isCalibrating())  {
            telemetry.addData("calibrating", "%s", Math.round(timer.seconds())%2==0 ? "|.." : "..|");
            telemetry.update();
            sleep(50);
        }

        telemetry.log().clear(); telemetry.log().add("Gyro Calibrated. Press Start.");
        telemetry.clear(); telemetry.update();

        waitForStart();
        runtime.reset();
        telemetry.log().add("Press A & B to reset heading");
        // run until the end of the match (driver presses STOP)
        double temp;
        double APwr;
        double BPwr;
        double CPwr;
        double Fingeroffset = 0;
        double Wristoffset = 0;
        boolean Lb = false;
        boolean Lx = false;
        boolean LRbumper = false;
        boolean LLbumper = false;
        while (opModeIsActive()) {
            double drivey = -gamepad1.left_stick_y;
            double drivex = gamepad1.left_stick_x;
            double turn =  gamepad1.right_stick_x;
            double time = getRuntime();
            //drivebase powers
            APwr= drivex+drivey+turn;
            BPwr= drivex*cos((120)*Math.PI/180)+drivey*sin((120)*Math.PI/180)+turn;
            CPwr= drivex*cos((-120)*Math.PI/180)+drivey*sin((-120)*Math.PI/180)+turn;
            if(Math.max(Math.abs(APwr), Math.max(Math.abs(BPwr), Math.abs(CPwr)))>1) {
                temp = 1 / Math.max(Math.abs(APwr), Math.max(Math.abs(BPwr), Math.abs(CPwr)));
            }else{
                temp=1;
            }
            APwr = APwr*temp;
            BPwr = BPwr*temp;
            CPwr = CPwr*temp;
            // Send calculated power to wheels
            ADrive.setPower(Range.clip(APwr, -1, 1));
            BDrive.setPower(Range.clip(BPwr, -1, 1));
            CDrive.setPower(Range.clip(CPwr, -1, 1));
            //Shoddy arm code
            Elbow.setPower(.1*Range.clip(-1*gamepad2.left_stick_y,-1,1));
            Shoulder.setPower(.1*Range.clip(-1*gamepad2.right_stick_y, -1,1));
            boolean b = gamepad2.b;
            if(b && !Lb){
                Fingeroffset += .1;
            }
            Lb = b;
            boolean x = gamepad2.x;
            if(Lx && x){
                Fingeroffset -= .1;
            }
            Lx = x;
            boolean Rbumper = gamepad2.right_bumper;
            if(Rbumper && !LRbumper){
                Wristoffset += .1;
            }
            LRbumper = Rbumper;
            boolean Lbumper = gamepad2.left_bumper;
            if(Lbumper && !LLbumper){
                Wristoffset -= .1;
            }
            LLbumper = Lbumper;
            if(Math.abs(Fingeroffset)>.5){
                Fingeroffset = .5*Math.abs(Fingeroffset)/Fingeroffset;
            }
            if(Math.abs(Wristoffset)>.5){
                Wristoffset = .5*Math.abs(Wristoffset)/Wristoffset;
            }
            RF.setPosition(Range.clip(0.5 - Fingeroffset, 0,1));
            LF.setPosition(Range.clip(Fingeroffset + 0.5, 0,1));
            Wrist.setPosition(Range.clip(Wristoffset + 0.5, 0,1));
        }
    }
}