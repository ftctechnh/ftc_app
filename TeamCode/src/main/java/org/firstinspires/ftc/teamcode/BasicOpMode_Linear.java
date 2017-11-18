package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IntegratingGyroscope;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import Jama.Matrix;
import java.lang.Math;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

@TeleOp(name="Drivebase", group="Linear Opmode")
public class BasicOpMode_Linear extends LinearOpMode {

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
		boolean curResState = false;
		boolean LastResState = false;
		double temp;
        double previoustime = getRuntime();
        double theta = 0;
        double θpreverr = 0;
        double θkp = .02;
        double θki = .0000001;
        double θkd = .00001;
		double APwr;
		double BPwr;
		double CPwr;
		double Fingeroffset = 0;
		double Wristoffset = 0;
		boolean b = false;
		boolean x = false;
        boolean Rbumper = false;
        boolean Lbumper = true;
		while (opModeIsActive()) {
			curResState = gamepad1.a && gamepad1.b;
			if(LastResState && !curResState){
				MRI2CGyro.resetZAxisIntegrator();
			}
			LastResState = curResState;
			double angle = MRI2CGyro.getIntegratedZValue();
            double drivey = -gamepad1.left_stick_y;
			double drivex = gamepad1.left_stick_x;
            double turn =  gamepad1.right_stick_x;
            double time = getRuntime();
            //drivebase powers
			theta += .01*.5*(time-previoustime)*turn;
            double θerr = angle-theta;
            double θderiv = (θerr-θpreverr)/(time-previoustime);
            double θinteg = .5*(time-previoustime)*(θerr+θpreverr);
            double θmotor = θkp*θerr+θki*θinteg+θkd*θderiv;
			APwr= drivex*cos((angle)*Math.PI/180)+drivey*sin((angle)*Math.PI/180)+θmotor;
			BPwr= drivex*cos((120+angle)*Math.PI/180)+drivey*sin((120+angle)*Math.PI/180)+θmotor;
			CPwr= drivex*cos((angle-120)*Math.PI/180)+drivey*sin((angle-120)*Math.PI/180)+θmotor;
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
            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Data", "A (%.2f), B (%.2f), C (%.2f) angle (%.2f)", APwr, BPwr, CPwr, angle);
			telemetry.addData("MEH too lazy to name this", "temp (%.2f) drvey (%.2f) drivey, drivex (%.2f)", temp, drivey, drivex);
            telemetry.update();
			//Shoddy last minute arm code
			Elbow.setPower(.1*Range.clip(-1*gamepad2.left_stick_y,-1,1));
            Shoulder.setPower(.1*Range.clip(-1*gamepad2.right_stick_y, -1,1));
			if(gamepad2.b && !b){
				Fingeroffset += .1;
			}
			if(gamepad2.x && !x){
				Fingeroffset -= .1;
			}
            if(gamepad2.right_bumper && !Rbumper){
                Wristoffset += .1;
            }
            if(gamepad2.left_bumper && !Lbumper){
                Wristoffset -= .1;
            }
			RF.setPosition(Range.clip(0.5 - Fingeroffset, 0,1));
			LF.setPosition(Range.clip(Fingeroffset + 0.5, 0,1));
			Wrist.setPosition(Range.clip(Wristoffset + 0.5, 0,1));
			x = gamepad2.x;
			b = gamepad2.b;
            Rbumper = gamepad2.right_bumper;
            Lbumper = gamepad2.left_bumper;
        }
    }
}
