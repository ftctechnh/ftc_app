//Goes into opponent's crater
package org.firstinspires.ftc.teamcode;

//import android.hardware.Sensor;
//import com.qualcomm.hardware.modernrobotics.ModernRoboticsAnalogOpticalDistanceSensor;
//import com.qualcomm.robotcore.eventloop.opmode.OpMode;
//import com.qualcomm.robotcore.hardware.IntegratingGyroscope;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;

import com.qualcomm.robotcore.util.ElapsedTime;

// below is the Annotation that registers this OpMode with the FtcRobotController app.
// @Autonomous classifies the OpMode as autonomous, name is the OpMode title and the
// optional group places the OpMode into the Exercises group.
// uncomment the @Disable annotation to remove the OpMode from the OpMode list.

@Autonomous(name="OldDepotAutonO", group="Competition Autonomous")
public class updatedDepotAutonO extends LinearOpMode
{
    DcMotor liftMotor;
    DcMotor frontLeft;
    DcMotor rearLeft;
    DcMotor frontRight;
    DcMotor rearRight;
    DcMotor armMotor;
    GyroSensor sensorGyro;
    ModernRoboticsI2cGyro mrGyro;

    private ElapsedTime runtime = new ElapsedTime();

    // called when init button is  pressed.
    @Override
    public void runOpMode() throws InterruptedException {
        //declare motors
        liftMotor = hardwareMap.dcMotor.get("liftMotor");
        frontLeft = hardwareMap.dcMotor.get("frontLeft");
        rearLeft = hardwareMap.dcMotor.get("rearLeft");
        frontRight = hardwareMap.dcMotor.get("frontRight");
        rearRight = hardwareMap.dcMotor.get("rearRight");
        armMotor = hardwareMap.dcMotor.get("armMotor");
        sensorGyro = hardwareMap.gyroSensor.get("gyro");

        //declare motor directions
        frontLeft.setDirection(DcMotor.Direction.FORWARD);
        rearLeft.setDirection(DcMotor.Direction.FORWARD);
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        rearRight.setDirection(DcMotor.Direction.REVERSE);
        armMotor.setDirection(DcMotor.Direction.FORWARD);
        liftMotor.setDirection(DcMotor.Direction.REVERSE);

        mrGyro = (ModernRoboticsI2cGyro) sensorGyro;

        double turnSpeed = 0.2;
        int zAccumulated;
        int target = 0;

        telemetry.addData("Mode", "waiting");
        telemetry.update();

        // wait for start button.

        waitForStart();

        telemetry.addData("Mode", "running");
        telemetry.update();

//-----------------------------Lift Up(lower robot) Start-------------------------------------------
        operateLift(1, 1, 5);
        //Wait 1 second
        sleep(500);
//-----------------------------Lift Up(lower robot) End---------------------------------------------


//-----------------------------Strafe off Lander Start----------------------------------------------
        rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rearLeft.setTargetPosition(-800);
        rearLeft.setPower(.5);
        rearRight.setTargetPosition(800);
        rearRight.setPower(.5);
        frontLeft.setTargetPosition(800);
        frontLeft.setPower(.5);
        frontRight.setTargetPosition(-800);
        frontRight.setPower(.5);
        while (rearLeft.isBusy() && opModeIsActive()) {
        }
        while (rearRight.isBusy() && opModeIsActive()) {
        }
        while (frontLeft.isBusy() && opModeIsActive()) {
        }
        while (frontRight.isBusy() && opModeIsActive()) {
        }
        rearLeft.setPower(0);
        rearRight.setPower(0);
        frontLeft.setPower(0);
        frontRight.setPower(0);
//-----------------------------Strafe off Lander End------------------------------------------------

//-----------------------------Straight off Lander Start--------------------------------------------
        rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rearLeft.setTargetPosition(500);
        rearLeft.setPower(.5);
        rearRight.setTargetPosition(500);
        rearRight.setPower(.5);
        frontLeft.setTargetPosition(500);
        frontLeft.setPower(.5);
        frontRight.setTargetPosition(500);
        frontRight.setPower(.5);
        while (rearLeft.isBusy() && opModeIsActive()) {
        }
        while (rearRight.isBusy() && opModeIsActive()) {
        }
        while (frontLeft.isBusy() && opModeIsActive()) {
        }
        while (frontRight.isBusy() && opModeIsActive()) {
        }
        rearLeft.setPower(0);
        rearRight.setPower(0);
        frontLeft.setPower(0);
        frontRight.setPower(0);
//-----------------------------Straight off Lander End----------------------------------------------

//-----------------------------Get in Position for Depot Start--------------------------------------
        rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rearLeft.setTargetPosition(800);
        rearLeft.setPower(.5);
        rearRight.setTargetPosition(-800);
        rearRight.setPower(.5);
        frontLeft.setTargetPosition(-800);
        frontLeft.setPower(.5);
        frontRight.setTargetPosition(800);
        frontRight.setPower(.5);
        while (rearLeft.isBusy() && opModeIsActive()) {
        }
        while (rearRight.isBusy() && opModeIsActive()) {
        }
        while (frontLeft.isBusy() && opModeIsActive()) {
        }
        while (frontRight.isBusy() && opModeIsActive()) {
        }
        rearLeft.setPower(0);
        rearRight.setPower(0);
        frontLeft.setPower(0);
        frontRight.setPower(0);
//-----------------------------Get in Position for Depot End----------------------------------------


//-----------------------------Straight to Depot Start----------------------------------------------
        rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rearLeft.setTargetPosition(3640);
        rearLeft.setPower(.9);
        rearRight.setTargetPosition(3640);
        rearRight.setPower(.9);
        frontLeft.setTargetPosition(3640);
        frontLeft.setPower(.9);
        frontRight.setTargetPosition(3640);
        frontRight.setPower(.9);
        while (rearLeft.isBusy() && opModeIsActive()) {
        }
        while (rearRight.isBusy() && opModeIsActive()) {
        }
        while (frontLeft.isBusy() && opModeIsActive()) {
        }
        while (frontRight.isBusy() && opModeIsActive()) {
        }
        rearLeft.setPower(0);
        rearRight.setPower(0);
        frontLeft.setPower(0);
        frontRight.setPower(0);
//-----------------------------Straight to Depot End------------------------------------------------


//-----------------------------Place Marker Start---------------------------------------------------
        armMotor.setPower(-.250);
        sleep(600); //wait
        armMotor.setPower(0);
        sleep(500); //wait
//-----------------------------Place Marker End-----------------------------------------------------

//-----------------------------Turn 90 Degrees Counterclockwise Start-------------------------------
        rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rearLeft.setTargetPosition(-1440);
        rearLeft.setPower(1);
        rearRight.setTargetPosition(1440);
        rearRight.setPower(1);
        frontLeft.setTargetPosition(-1440);
        frontLeft.setPower(1);
        frontRight.setTargetPosition(1440);
        frontRight.setPower(1);
        while (rearLeft.isBusy() && opModeIsActive()) {
        }
        while (rearRight.isBusy() && opModeIsActive()) {
        }
        while (frontLeft.isBusy() && opModeIsActive()) {
        }
        while (frontRight.isBusy() && opModeIsActive()) {
        }
        rearLeft.setPower(0);
        rearRight.setPower(0);
        frontLeft.setPower(0);
        frontRight.setPower(0);
//-----------------------------Turn 90 Degrees Counterclockwise End---------------------------------

//-----------------------------Forward Towards Wall Start-------------------------------------------
        rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rearLeft.setTargetPosition(1200);
        rearLeft.setPower(.7);
        rearRight.setTargetPosition(1200);
        rearRight.setPower(.7);
        frontLeft.setTargetPosition(1200);
        frontLeft.setPower(.7);
        frontRight.setTargetPosition(1200);
        frontRight.setPower(.7);
        while (rearLeft.isBusy() && opModeIsActive()) {
        }
        while (rearRight.isBusy() && opModeIsActive()) {
        }
        while (frontLeft.isBusy() && opModeIsActive()) {
        }
        while (frontRight.isBusy() && opModeIsActive()) {
        }
        rearLeft.setPower(0);
        rearRight.setPower(0);
        frontLeft.setPower(0);
        frontRight.setPower(0);
//-----------------------------Forward Towards Wall End---------------------------------------------

//-----------------------------Turn 45 Degrees Counterclockwise Start-------------------------------
        rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rearLeft.setTargetPosition(-640);
        rearLeft.setPower(1);
        rearRight.setTargetPosition(640);
        rearRight.setPower(1);
        frontLeft.setTargetPosition(-640);
        frontLeft.setPower(1);
        frontRight.setTargetPosition(640);
        frontRight.setPower(1);
        while (rearLeft.isBusy() && opModeIsActive()) {
        }
        while (rearRight.isBusy() && opModeIsActive()) {
        }
        while (frontLeft.isBusy() && opModeIsActive()) {
        }
        while (frontRight.isBusy() && opModeIsActive()) {
        }
        rearLeft.setPower(0);
        rearRight.setPower(0);
        frontLeft.setPower(0);
        frontRight.setPower(0);
//-----------------------------Turn 45 Degrees Counterclockwise End---------------------------------

//-----------------------------Strafe Towards Wall Start--------------------------------------------
        rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rearLeft.setTargetPosition(-700);
        rearLeft.setPower(.5);
        rearRight.setTargetPosition(700);
        rearRight.setPower(.5);
        frontLeft.setTargetPosition(700);
        frontLeft.setPower(.5);
        frontRight.setTargetPosition(-700);
        frontRight.setPower(.5);
        while (rearLeft.isBusy() && opModeIsActive()) {
        }
        while (rearRight.isBusy() && opModeIsActive()) {
        }
        while (frontLeft.isBusy() && opModeIsActive()) {
        }
        while (frontRight.isBusy() && opModeIsActive()) {
        }
        rearLeft.setPower(0);
        rearRight.setPower(0);
        frontLeft.setPower(0);
        frontRight.setPower(0);
//-----------------------------Strafe Towards Wall End----------------------------------------------

//-----------------------------Forward Towards Crater Start-----------------------------------------
        rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rearLeft.setTargetPosition(5250);
        rearLeft.setPower(1);
        rearRight.setTargetPosition(5250);
        rearRight.setPower(1);
        frontLeft.setTargetPosition(5250);
        frontLeft.setPower(1);
        frontRight.setTargetPosition(5250);
        frontRight.setPower(1);
        while (rearLeft.isBusy() && opModeIsActive()) {
        }
        while (rearRight.isBusy() && opModeIsActive()) {
        }
        while (frontLeft.isBusy() && opModeIsActive()) {
        }
        while (frontRight.isBusy() && opModeIsActive()) {
        }
        rearLeft.setPower(0);
        rearRight.setPower(0);
        frontLeft.setPower(0);
        frontRight.setPower(0);
//-----------------------------Forward Towards Crater End-------------------------------------------

//-----------------------------Lift Down Start------------------------------------------------------
        operateLift(0, 1, 5);
        //Wait 1 second
        sleep(500);
//-----------------------------Lift Down End--------------------------------------------------------
        /*
        //----------------------Strafe Block Start-------------------------------------
        rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rearLeft.setTargetPosition(-1440);
        rearLeft.setPower(1);
        rearRight.setTargetPosition(-1440);
        rearRight.setPower(1);
        frontLeft.setTargetPosition(-1440);
        frontLeft.setPower(1);
        frontRight.setTargetPosition(-1440);
        frontRight.setPower(1);
        while (rearLeft.isBusy() && opModeIsActive()) {
        }
        while (rearRight.isBusy() && opModeIsActive()) {
        }
        while (frontLeft.isBusy() && opModeIsActive()) {
        }
        while (frontRight.isBusy() && opModeIsActive()) {
        }
        rearLeft.setPower(0);
        rearRight.setPower(0);
        frontLeft.setPower(0);
        frontRight.setPower(0);
//-------------------Block End--------------------------------------------------
*/

    }

    public void turn(int target) throws InterruptedException {
        turnAbsolute(target + mrGyro.getIntegratedZValue());
    }


    public void turnAbsolute(int target) throws InterruptedException {
        int zAccumulated = mrGyro.getIntegratedZValue();
        double turnSpeed = .5;

        while (Math.abs(zAccumulated - target) > 3) {
            if (zAccumulated > target) {
                rearLeft.setPower(turnSpeed);
                frontLeft.setPower(turnSpeed);
                rearRight.setPower(-turnSpeed);
                frontRight.setPower(-turnSpeed);
            }
            if (zAccumulated < target) {
                rearLeft.setPower(-turnSpeed);
                frontLeft.setPower(-turnSpeed);
                rearRight.setPower(turnSpeed);
                frontRight.setPower(turnSpeed);
            }

            waitOneFullHardwareCycle();

            zAccumulated = mrGyro.getIntegratedZValue();
            telemetry.addData("1. accu", String.format("%03d", zAccumulated));
        }
        rearLeft.setPower(0);
        rearRight.setPower(0);
        frontLeft.setPower(0);
        frontRight.setPower(0);

        telemetry.addData("1. accu", String.format("%03d", zAccumulated));

        waitOneFullHardwareCycle();

    }


    public void operateLift(int position, double speed, int timeoutS){
        if (opModeIsActive()) {
            if(position == 0) {
                //Change This Number to Determine the Upper Position of the Lift
                liftMotor.setTargetPosition(liftMotor.getCurrentPosition() - 7700
                );
                liftMotor.setPower(-speed);
            }
            else {
                //Change This Number to Determine the Lower Position of the Lift
                liftMotor.setTargetPosition(liftMotor.getCurrentPosition() + 7700
                );
                liftMotor.setPower(-speed);
            }
            liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            runtime.reset();

            while (liftMotor.isBusy() && opModeIsActive() &&
                    (runtime.seconds() < timeoutS)) {
                // Display it for the driver.
                telemetry.addData("Lift",  "Running to %7d : %7d", liftMotor.getCurrentPosition(), liftMotor.getTargetPosition());
//                telemetry.addData("Path2",  "Running at %7d :%7d",
//                                            robot.leftDrive.getCurrentPosition(),
//                                            robot.rightDrive.getCurrentPosition());
                telemetry.update();
            }
            liftMotor.setPower(0);
            liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }

}