package org.firstinspires.ftc.teamcode.ftc2016to2017season.Aditya;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by adityamavalankar on 1/12/17.
 */

@TeleOp(name="Proportional Line Follower")
@Disabled
public class proportionalStraightLineFollowOp extends OpMode {

    DcMotor leftWheelMotorFront, leftWheelMotorBack, rightWheelMotorFront, rightWheelMotorBack;
    double leftPower, rightPower, correction;
    double PERFECT_COLOR_VALUE = 0.11;
    OpticalDistanceSensor lightSensor;

    @Override
    public void init() {
        leftWheelMotorFront = hardwareMap.dcMotor.get("leftWheelMotorFront");
        leftWheelMotorBack = hardwareMap.dcMotor.get("leftWheelMotorBack");
        rightWheelMotorFront = hardwareMap.dcMotor.get("rightWheelMotorFront");
        rightWheelMotorBack = hardwareMap.dcMotor.get("rightWheelMotorBack");
        //-----------------//
        lightSensor = hardwareMap.opticalDistanceSensor.get("lightSensor");

        rightWheelMotorBack.setDirection(DcMotorSimple.Direction.REVERSE);
        rightWheelMotorFront.setDirection(DcMotorSimple.Direction.REVERSE);


    }

    @Override
    public void init_loop() {

        callibratePerfectColor(15);

    }

    @Override
    public void start() {

    }

    @Override
    public void loop() {
        sensorReadDrive();
//        readLightSensor();

    }

    @Override
    public void stop() {
    }

    public void readOpticalColor() {

        correction = (PERFECT_COLOR_VALUE - lightSensor.getLightDetected());
    }

    public void callibratePerfectColor(int timesToAverage) {

        double aPERFECT_COLOR_VALUE[] = new double[timesToAverage];

        for (int x = 0; x < timesToAverage; x++) {

            aPERFECT_COLOR_VALUE[x] = lightSensor.getLightDetected();
        }

        for (double i : aPERFECT_COLOR_VALUE) {

            PERFECT_COLOR_VALUE += i;
        }

        PERFECT_COLOR_VALUE = PERFECT_COLOR_VALUE / timesToAverage;

        telemetry.addData("the perfect color is...", PERFECT_COLOR_VALUE);
        telemetry.update();

    }

    public void sensorReadDrive(){


            // Sets the powers so they are no less than .075 and apply to correction
            if (correction <= 0) {
                leftPower = .15 - correction;
                rightPower = .15;

                leftWheelMotorFront.setPower(leftPower);
                rightWheelMotorFront.setPower(rightPower);
                leftWheelMotorBack.setPower(leftPower);
                rightWheelMotorBack.setPower(rightPower);
                readOpticalColor();

            } else {

                leftPower = .15;
                rightPower = .15 + correction;

                leftWheelMotorFront.setPower(leftPower);
                rightWheelMotorFront.setPower(rightPower);
                leftWheelMotorBack.setPower(leftPower);
                rightWheelMotorBack.setPower(rightPower);
                readOpticalColor();

            }

            telemetry.addData("current light value", lightSensor.getLightDetected());
            telemetry.update();

    }

    public void readLightSensor(){

        telemetry.addData("current light value", lightSensor.getLightDetected());
        telemetry.update();

    }

}
