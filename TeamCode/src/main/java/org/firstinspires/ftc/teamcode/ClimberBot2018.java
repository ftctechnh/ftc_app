package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IntegratingGyroscope;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.RoboticsUtils.PID;

import static android.content.Context.*;
import static java.lang.Math.abs;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.toRadians;


/**
 * Created by jxfio on 1/21/2018.
 */
@TeleOp(name="Climber", group="Linear Opmode")
public class ClimberBot2018 extends LinearOpMode{

    // Declare OpMode members.
    //packageContext(org.firstinspires.ftc.teamcode, 0);
    ElapsedTime timer = new ElapsedTime();
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor left;
    private DcMotor back;
    private DcMotor right;
    private DcMotor wheelLift;
    private DcMotor swivel;
    private DcMotor arm;
    private DcMotor leftClimb;
    private DcMotor rightClimb;
    private Servo lift;
    private Servo leftFinger;
    private Servo rightFinger;
    private Servo backSwivel;
    private IntegratingGyroscope gyro;
    private ModernRoboticsI2cGyro MRI2CGyro;
    PID θPID = new PID(.5,0,0);//changed from .5, .0001, 0.1
    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        MRI2CGyro = hardwareMap.get(ModernRoboticsI2cGyro.class, "gyro");
        gyro = (IntegratingGyroscope)MRI2CGyro;
        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        left = hardwareMap.get(DcMotor.class, "B1");
        back = hardwareMap.get(DcMotor.class, "B3");
        right = hardwareMap.get(DcMotor.class, "B2");
        wheelLift = hardwareMap.get(DcMotor.class, "B0");
        swivel = hardwareMap.get(DcMotor.class,"R0");
        arm = hardwareMap.get(DcMotor.class, "R1");
        leftClimb = hardwareMap.get(DcMotor.class, "R2");
        rightClimb = hardwareMap.get(DcMotor.class, "R3");
        lift = hardwareMap.get(Servo.class,"S2");
        leftFinger = hardwareMap.get(Servo.class, "S3");
        rightFinger = hardwareMap.get(Servo.class, "S4");
        backSwivel = hardwareMap.get(Servo.class, "S5");
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
        double leftPwr;
        double backPwr;
        double rightPwr;
        double dt;
        double Desiredθ = 0;
        double TurnPWR;
        double PrevTime = time; //previous time value
        boolean curResetState = false;
        boolean lastResetState = false;
        boolean upRailMode = false;
        boolean onRail = false;
        boolean y = false;
        double pos;
        double prevpos=0;
        double prevderiv=0;
        double wheelLiftZero=0;
        final double wheelLiftZerotoGround=-20;
        PID wheelLiftPID = new PID(.01,0,0);//set all the ps to .01 instead of 1
        PID armPID = new PID(.01,0,0);//originally 1, ,00001,.1 (armswivel same)
        PID armSwivelPID = new PID(.01,0,0);
        boolean x=false;
        boolean b=false;
        boolean armSwivelZeroingState;
        boolean lastArmSwivelZeroingState=false;
        double armSwivelZeroState=0;
        boolean armZeroingState;
        boolean lastArmZeroingState=false;
        double armZeroState=0;
        boolean leftOpen=true;
        boolean rightOpen = true;
       // wheelLift.setPower(.01);//changed from 0.25 because it was slamming
       // sleep(1000); //changed from 1000 because it was pushing into robot for too long
       // wheelLiftZero = wheelLift.getCurrentPosition();
       // wheelLift.setPower(-.01);
        // commented all of this out because no matter what we do it would just slam on the robot and we want to drive first and foremost.

        while (opModeIsActive()) {
                //Gyro reset
            curResetState = (gamepad1.a && gamepad1.b);
            if (curResetState && !lastResetState) {
                MRI2CGyro.resetZAxisIntegrator();
            }
            lastResetState = curResetState;
            armZeroingState = (gamepad2.a && gamepad2.b);
            if (armZeroingState&&!lastArmZeroingState){
                armZeroState=arm.getCurrentPosition();
            }
            lastArmZeroingState =armZeroingState;
            armSwivelZeroingState = (gamepad2.y && gamepad2.a);
            if (armSwivelZeroingState&&!lastArmSwivelZeroingState){
                armSwivelZeroState=arm.getCurrentPosition();
            }
            lastArmSwivelZeroingState =armSwivelZeroingState;
            if(gamepad1.y&&! y){
                upRailMode=true;
            }
            y = gamepad1.y;
            if(gamepad2.y){
                lift.setPosition(0);
            }else if(gamepad2.a){
                lift.setPosition(1);
            }else{
                lift.setPosition(.5);
            }
            if(gamepad2.x&&!x){
                leftOpen=!leftOpen;
            }
            x=gamepad2.x;
            if(gamepad2.b&&!b){
                rightOpen=!rightOpen;
            }
            b=gamepad2.b;
            if(leftOpen){
                leftFinger.setPosition(1);
            }else{
                leftFinger.setPosition(.25);
            }
            if(rightOpen){
                rightFinger.setPosition(0);
            }else{
                rightFinger.setPosition(.75);
            }
            while (upRailMode){
                double dp;
                dt = time - PrevTime;
                PrevTime = time;
                time = getRuntime();
                if(gamepad1.y&&! y){
                    upRailMode=false;
                }
                armPID.iteratePID(arm.getCurrentPosition()-armZeroState+70,dt);
                armSwivelPID.iteratePID(swivel.getCurrentPosition()-armSwivelZeroState,dt);
                arm.setPower(Range.clip(armPID.getPID(),-1,1));
                swivel.setPower(Range.clip(armSwivelPID.getPID(),-1,1));
                y = gamepad1.y;
                backSwivel.setPosition(1);
                leftClimb.setPower(-1);
                rightClimb.setPower(1);
                pos = rightClimb.getCurrentPosition();
                dp = pos-prevpos;
                prevpos = pos;
                if(Math.abs(dp/dt-prevderiv)>10){
                    onRail = true;
                }

                while (onRail){
                    dt = time - PrevTime;
                    PrevTime = time;
                    time = getRuntime();
                    wheelLiftPID.iteratePID(wheelLift.getCurrentPosition()- wheelLiftZero,dt);
                    wheelLift.setPower(Range.clip(wheelLiftPID.getPID(),-1,1));
                    leftClimb.setPower(Range.clip(-gamepad1.left_stick_y,-1,1));
                    rightClimb.setPower(Range.clip(gamepad1.left_stick_y,-1,1));
                    if(Math.abs((pos-prevpos)/dt-prevderiv)>10){
                        onRail = false;
                    }
                    wheelLiftPID.iteratePID(wheelLift.getCurrentPosition()-wheelLiftZero,dt);
                    armPID.iteratePID(arm.getCurrentPosition()-armZeroState+70,dt);
                    armSwivelPID.iteratePID(swivel.getCurrentPosition()-armSwivelZeroState,dt);
                    arm.setPower(Range.clip(armPID.getPID(),-1,1));
                    swivel.setPower(Range.clip(armSwivelPID.getPID(),-1,1));
                    wheelLift.setPower(-Range.clip(wheelLiftPID.getPID(),-1,1));
                }
                wheelLiftPID.iteratePID(wheelLift.getCurrentPosition()-wheelLiftZero+wheelLiftZerotoGround,dt);
                wheelLift.setPower(-Range.clip(wheelLiftPID.getPID(),-1,1));
                prevderiv=(pos-prevpos)/dt;
                back.setPower(Range.clip(gamepad1.left_stick_y,-1,1));
            }
            double θ = MRI2CGyro.getIntegratedZValue();
            double drivey = -gamepad1.left_stick_y; //should be neg here
            double drivex = -gamepad1.left_stick_x;
                //double turn =  gamepad1.right_stick_x;
                //negative sign because y goes in wrong direction
            double time = getRuntime();
            double driveθ = Math.atan2(drivex,drivey); //direction //took out neg
            double driveV = sqrt(pow(drivey,2)+pow(drivex,2));  //magnitude -pythagorean theorem
            driveV = Range.clip(driveV,-1,1);
                //integrating for angle
            dt = time - PrevTime;
            Desiredθ += (-gamepad1.right_stick_x * dt * 90); // 45 degrees per second
            PrevTime = time;
                //PID
            wheelLiftPID.iteratePID(wheelLift.getCurrentPosition()-wheelLiftZero+wheelLiftZerotoGround,dt);
            wheelLift.setPower(Range.clip(-wheelLiftPID.getPID(),-1,1));
            θPID.iteratePID(θ-Desiredθ,dt);
            TurnPWR = θPID.getPID();
                //TurnPWR = (θ - Desiredθ)/-100;
            TurnPWR = Range.clip(TurnPWR, -.75, .75);
                //drivebase powers
            temp= 1 - TurnPWR;
            leftPwr= TurnPWR + temp * driveV*sin(driveθ+toRadians(60)); //power to send motor- proportional to the sign of the angle to drive at
            backPwr= TurnPWR + temp * driveV*sin(driveθ+toRadians(180));
            rightPwr= TurnPWR + temp * driveV*sin(driveθ-toRadians(60));
                // Send calculated power to motors
            swivel.setPower(Range.clip(gamepad2.left_stick_x,-1,1));
            arm.setPower(Range.clip(gamepad2.left_stick_y,-1,1));
            backSwivel.setPosition(1/2);
            left.setPower(Range.clip(leftPwr, -1, 1));  //range.clip concerns for ratios?- put in in case
            back.setPower(Range.clip(backPwr, -1, 1));
            right.setPower(Range.clip(rightPwr, -1, 1));
                //Telemetry Data
            /*telemetry.addData("path0","driveθ:");
            telemetry.addData("path1","A Power:" + String.valueOf(leftPwr) + " B Power:" + String.valueOf(backPwr) + " C Power:" +String.valueOf(rightPwr));
            telemetry.addData("path2","Integrated Z:" + String.valueOf(MRI2CGyro.getIntegratedZValue()) + " Desiredθ: " + String.valueOf(Desiredθ));
            telemetry.addData("path3","Time: "+ toString().valueOf(time));
            telemetry.update();// prints above stuff to phone*/
        }
    }
}
