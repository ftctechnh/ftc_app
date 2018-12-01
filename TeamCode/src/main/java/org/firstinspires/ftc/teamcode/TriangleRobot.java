package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IntegratingGyroscope;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import static java.lang.Math.abs;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.toRadians;

/**
 * Created by jxfio on 1/21/2018.
 */
@TeleOp(name="tribot", group="Linear Opmode")
public class TriangleRobot extends LinearOpMode{
    // Declare OpMode members.
    ElapsedTime timer = new ElapsedTime();
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor ADrive;
    private DcMotor BDrive;
    private DcMotor CDrive;
    private IntegratingGyroscope gyro;
    private ModernRoboticsI2cGyro MRI2CGyro;
    private Servo F;
    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        MRI2CGyro = hardwareMap.get(ModernRoboticsI2cGyro.class, "gyro");
        gyro = (IntegratingGyroscope)MRI2CGyro;
        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        ADrive = hardwareMap.get(DcMotor.class, "A");
        BDrive = hardwareMap.get(DcMotor.class, "B");
        CDrive = hardwareMap.get(DcMotor.class, "C");
        MRI2CGyro = hardwareMap.get(ModernRoboticsI2cGyro.class, "gyro");
        gyro = (IntegratingGyroscope)MRI2CGyro;
        telemetry.log().add("Gyro Calibrating. Do Not Move!");
        MRI2CGyro.calibrate();
        // Wait until the gyro calibration is complete
        F = hardwareMap.get(Servo.class, "F");
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
        double dt;
        double Desiredθ = 0;
        double Fingeroffset = 0;
        double TurnPWR;
        double PrevTime = time; //previous time value
        boolean curResetState = false;
        boolean lastResetState = false;
        boolean b = false;
        boolean x = false;
        while (opModeIsActive()) {
            curResetState = (gamepad1.a && gamepad1.b);
            if (curResetState && !lastResetState) {
                MRI2CGyro.resetZAxisIntegrator();
            }
            lastResetState = curResetState;
            double θ = MRI2CGyro.getIntegratedZValue();
            double drivey = -gamepad1.left_stick_y; //should be neg here
            double drivex = -gamepad1.left_stick_x;
            //double turn =  gamepad1.right_stick_x;
            //negative sign because y goes in wrong direction
            double time = getRuntime();
            double driveθ = Math.atan2(drivex,drivey); //direction //took out neg
            double driveV = (sqrt(pow(drivey,2)+pow(drivex,2)));  //magnitude -pythagorean theorem
            driveV = Range.clip(driveV,-1,1);
            //integrating for angle
            dt = time - PrevTime;
            Desiredθ += (-gamepad1.right_stick_x * dt * 90); // 45 degrees per second
            PrevTime = time;
            //PIDθ
            TurnPWR = (θ - Desiredθ)/-100;
            TurnPWR = Range.clip(TurnPWR, -.75, .75);
            if(gamepad2.b && !b && Fingeroffset <= .4){
                Fingeroffset += .1;
            }
            if(gamepad2.x && !x && Fingeroffset >= .1 ){
                Fingeroffset -= .1;
            }
            b = gamepad2.b;
            x = gamepad2.x;
            //drivebase powers
            temp= 1 - TurnPWR;
            APwr= TurnPWR + temp * driveV*sin(driveθ); //power to send motor- proportional to the sign of the angle to drive at
            BPwr= TurnPWR + temp * driveV*sin(driveθ+toRadians(120));
            CPwr= TurnPWR + temp * driveV*sin(driveθ-toRadians(120));
            F.setPosition(Range.clip(0.5 - Fingeroffset, -1,1));
            // Send calculated power to wheels
            ADrive.setPower(Range.clip(APwr, -1, 1));  //range.clip concerns for ratios?- put in in case
            BDrive.setPower(Range.clip(BPwr, -1, 1));
            CDrive.setPower(Range.clip(CPwr, -1, 1));
            //Telemetry Data
            telemetry.addData("path0","driveθ:");
            telemetry.addData("path1","A Power:" + String.valueOf(APwr) + " B Power:" + String.valueOf(BPwr) + " C Power:" +String.valueOf(CPwr));
            telemetry.addData("path2","Integrated Z:" + String.valueOf(MRI2CGyro.getIntegratedZValue()) + " Desiredθ: " + String.valueOf(Desiredθ));
            telemetry.addData("path3","Time: "+ toString().valueOf(time));
            telemetry.update();// prints above stuff to phone

        }
    }
}
