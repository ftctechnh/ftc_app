package com.greengirls.chinaTeleOp;

import com.greengirls.RobotHardwareChina;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by G201956 on 10/4/2015.
 */
public class TeleOp extends RobotHardwareChina {

    /*
	 * Code to run when the op mode is initialized goes here
	 *
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#init()
	 */


    @Override
    public void init() {

        super.init();

    }
/**
 * Controller One
 *   left_stick_x moves the left wheels forward and backward
 *   right_stick_x moves the right wheels forward and backward
 *   right_trigger collects balls
 *   right_trigger spits out balls
 *
 * Controller Two
 *  Left bumper triggers the dino arm servos to open
 *  Right bumper triggers the dino arm servos to close
 *  Holding B triggers the ball channel servo to open
 *  Pressing Y raises the deflector arm
 *  Pressing A lowers the deflector arm
 *  Holding X shoots

 */

    @Override
    public void loop() {

        // Right wheels will be controlled by the right stick
        // Left wheels will be controlled by the left stick
        float rightWheels = gamepad1.right_stick_y;
        float leftWheels = gamepad1.left_stick_y;

        // clip the right/left values so that the values never exceed +/- 1
        rightWheels = Range.clip(rightWheels, -1, 1);
        leftWheels = Range.clip(leftWheels, -1, 1);

        // scale the joystick value to make it easier to control
        // the robot more precisely at slower speeds.
        rightWheels = (float)scaleInput(rightWheels);
        leftWheels =  (float)scaleInput(leftWheels);


        // write the values to the motors
        setRightMotors(rightWheels);
        setLeftMotors(leftWheels);


        //When dino arms are open
        if (gamepad2.left_bumper){
            openDinoArms();
        }

        //When dino arms are closed
        if (gamepad2.right_bumper){
            closeDinoArms();
        }

        //When ball channel is open
        if (gamepad2.b) {
            openBallChannel();
        } else {
        //When ball channel is closed
            closeBallChannel();
        }

        //When the deflector is raised
        if (gamepad2.y) {
            openDeflector();
        }
        else {
            stopDeflector();
        }

        //When deflector is lowered
        if (gamepad2.a) {
            closeDeflector();
        }
        else {
            stopDeflector();
        }

        //When ball is shooting
        if (gamepad2.x) {
           shootBalls();
        }
        else{
            stopShootBalls();
        }

        //When collector is collecting balls
        if (gamepad1.right_trigger>0){
            collectorForward();
        }
        else {
            stopCollector();
        }

        //When collector is spitting balls out
        if (gamepad1.left_trigger>0){
            collectorBackward();
        }
        else{
            stopCollector();
        }

        /*
		 * Send telemetry data back to driver station. Note that if we are using
		 * a legacy NXT-compatible motor controller, then the getPower() method
		 * will return a null value. The legacy NXT-compatible motor controllers
		 * are currently write only.
		 */

        telemetry.addData("Text", "*** Robot Data***");
//        telemetry.addData("arm", "arm:  " + String.format("%.2f", armPosition));
//        telemetry.addData("claw", "claw:  " + String.format("%.2f", clawPosition));
        telemetry.addData("left tgt pwr",  "left  pwr: " + String.format("%.2f", leftWheels));
        telemetry.addData("right tgt pwr", "right pwr: " + String.format("%.2f", rightWheels));
//		tlelmetry.addData("lfront", )

    }

    /*
	 * Code to run when the op mode is first disabled goes here
	 *
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()
	 */

    @Override
    public void stop() {

        // write the values to the motors
        setRightMotors(0);
        setLeftMotors(0);
        setCollectorMotor(0);
        setDeflectorMotor(0);
        setShooterMotor(0);

    }

    /*
     * This method scales the joystick input so for low joystick values, the
     * scaled value is less than linear.  This is to make it easier to drive
     * the robot more precisely at slower speeds.
     */

    double scaleInput(double dVal)  {
        double[] scaleArray = { 0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
                0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00 };

        // get the corresponding index for the scaleInput array.
        int index = (int) (dVal * 16.0);

        // index should be positive.
        if (index < 0) {
            index = -index;
        }

        // index cannot exceed size of array minus 1.
        if (index > 16) {
            index = 16;
        }

        // get value from the array.
        double dScale = 0.0;
        if (dVal < 0) {
            dScale = -scaleArray[index];
        } else {
            dScale = scaleArray[index];
        }

        // return scaled value.
        return dScale;
    }

}