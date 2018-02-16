package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IntegratingGyroscope;
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
        boolean curResetState = false;
        boolean lastResetState = false;
        while (opModeIsActive()) {
            curResetState = (gamepad1.a && gamepad1.b);
            if (curResetState && !lastResetState) {
                MRI2CGyro.resetZAxisIntegrator();
            }
            lastResetState = curResetState;
            double θ = MRI2CGyro.getIntegratedZValue();
            double drivey = -gamepad1.left_stick_y;
            double drivex = gamepad1.left_stick_x;
            //double turn =  gamepad1.right_stick_x;
            double time = getRuntime();
            double driveθ = Math.atan(drivex/drivey);
            double driveV = (abs(drivey)/drivey)*sqrt(pow(drivey,2)+pow(drivey,2));
            Range.clip(driveV,-1,1);
            //drivebase powers
            APwr= driveV*sin(driveθ);
            BPwr= driveV*sin(driveθ+toRadians(120));
            CPwr= driveV*sin(driveθ-toRadians(120));
            // Send calculated power to wheels
            ADrive.setPower(Range.clip(APwr, -1, 1));
            BDrive.setPower(Range.clip(BPwr, -1, 1));
            CDrive.setPower(Range.clip(CPwr, -1, 1));
            //Telemetry Data
            telemetry.addData("path1","A Power:" + String.valueOf(APwr) + " B Power:" + String.valueOf(BPwr) + " C Power:" +String.valueOf(CPwr));
            telemetry.addData("path2","Integrated Z:" + String.valueOf(MRI2CGyro.getIntegratedZValue()));
        }
    }
}
