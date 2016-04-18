package com.greengirls;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.IrSeekerSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.ServoController;

/**
 * Created by Dell User on 10/15/2015.
 */
public class RobotHardwareChina extends OpMode {

    //Set Max and Min values of dino arms
    protected final static double DINO_ARM_MIN_RANGE  = 0.20;
    protected final static double DINO_ARM_MAX_RANGE  = 0.90;

    //Set Max and Min values of ball channel
    protected final static double BALL_CHANNEL_MIN_RANGE  = 0.20;
    protected final static double BALL_CHANNEL_MAX_RANGE  = 0.90;

    //define servos
    private Servo dinoArm1;
    private Servo dinoArm2;
    private Servo ballChannel;
    private ServoController servoController;

    //define Motors and MotorControllers
    private DcMotorController rightMotorController;
    private DcMotor rightFrontMotor;
    private DcMotorController leftMotorController;
    private DcMotor leftFrontMotor;
    private DcMotor rightBackMotor;
    private DcMotor leftBackMotor;
    private DcMotorController collectorShooterMotorController;
    private DcMotor collectorMotor;
    private DcMotor shooterMotor;
    private DcMotorController deflectorMotorController;
    private DcMotor deflectorMotor;

    //define sensors
    //legacy module
    private IrSeekerSensor irSensorRight;
    private IrSeekerSensor irSensorLeft;


    @Override public void init(){


        //Map hardware for Right motor controller
        rightMotorController = hardwareMap.dcMotorController.get("right_drive_controller");
        rightFrontMotor = hardwareMap.dcMotor.get("rfront");
        rightBackMotor = hardwareMap.dcMotor.get("rback");

        //Map hardware for Left motor controller
        leftMotorController = hardwareMap.dcMotorController.get("left_drive_controller");
        leftFrontMotor = hardwareMap.dcMotor.get("lfront");
        leftBackMotor = hardwareMap.dcMotor.get("lback");

        //Reversing motors of wheels
        rightBackMotor.setDirection(DcMotor.Direction.REVERSE);
        leftFrontMotor.setDirection(DcMotor.Direction.REVERSE);

        //Map hardware for collector shooter motor controller
        collectorShooterMotorController = hardwareMap.dcMotorController.get("collector_shooter_controller");
        collectorMotor = hardwareMap.dcMotor.get("collector");
        shooterMotor = hardwareMap.dcMotor.get("shooter");

        //Map hardware for deflector motor controller
        deflectorMotorController = hardwareMap.dcMotorController.get("deflector_controller");
        deflectorMotor = hardwareMap.dcMotor.get("deflector");

        //Map hardware for servos
        servoController = hardwareMap.servoController.get("servo_controller");

        dinoArm1 = hardwareMap.servo.get("dinoright");
        dinoArm2 = hardwareMap.servo.get("dinoleft");
        ballChannel = hardwareMap.servo.get("ballchannel");

        //Map hardware sensors
        //legacy module
        irSensorRight = hardwareMap.irSeekerSensor.get("irright");
        irSensorLeft = hardwareMap.irSeekerSensor.get("irleft");


        openDinoArms();
        closeBallChannel();
    }

    //get the power for both right motors
    public double getRightMotors(){
        return rightFrontMotor.getPower();
    }

    //set get power for both left motors
    public double getLeftMotors(){
        return leftFrontMotor.getPower();
    }

    //set power to right motors
    public void setRightMotors(double power){
        rightFrontMotor.setPower(power);
        rightBackMotor.setPower(power);
    }

    //set power to left motors
    public void setLeftMotors(double power){
        leftFrontMotor.setPower(power);
        leftBackMotor.setPower(power);
    }

    //get the power to collector motor
    public double getCollectorMotor(){
        return collectorMotor.getPower();
    }

    //set the power to collector motor
    public void setCollectorMotor(double power) {
        collectorMotor.setPower(power);
    }

    //get the power to shooter motor
    public double getShooterMotor(){
        return shooterMotor.getPower();
    }

    //set the power to shooter motor
    public void setShooterMotor(double power){
        shooterMotor.setPower(power);
    }

    //get the power to deflector motor
    public double getDeflectorMotor(){
        return deflectorMotor.getPower();
    }

    //get the power to deflector motor
    public void setDeflectorMotor(double power){
        deflectorMotor.setPower(power);
    }

    public boolean getIrReading() {
        return irSensorRight.signalDetected();

    }
    /**
     * We did this for mapping out the buttons
     * also we could use it in more than one place
     */

    //put dinoArms into open position
    public void openDinoArms(){
        dinoArm1.setPosition(DINO_ARM_MIN_RANGE);
        dinoArm2.setPosition(DINO_ARM_MAX_RANGE);
    }

    //put dinoArms into close position
    public void closeDinoArms(){
        dinoArm1.setPosition(DINO_ARM_MAX_RANGE);
        dinoArm2.setPosition(DINO_ARM_MIN_RANGE);
    }

    //put ball channel into open position
    public void openBallChannel() {
        ballChannel.setPosition(BALL_CHANNEL_MIN_RANGE);
    }

    //put ballChannel into close position
    public void closeBallChannel() {
        ballChannel.setPosition(BALL_CHANNEL_MAX_RANGE);
    }

    //run deflector to open position
    public void openDeflector() {
        //deflectorMotor.setPower(0.1);
        deflectorMotor.setTargetPosition(45);
    }

    //reverse to close position
    public void closeDeflector(){
        //deflectorMotor.setPower(-0.1);
        deflectorMotor.setTargetPosition(0);
    }

    //stop deflector motor
    public void stopDeflector() {
        deflectorMotor.setPower(0);
    }

    //run shooter motor
    public void shootBalls() {
        shooterMotor.setPower(0.1);
    }

    //stop the shooter motor
    public void stopShootBalls(){
        shooterMotor.setPower(0);
    }

    //run collector motor
    public void collectorForward(){
        collectorMotor.setPower(0.1);
    }

    //reverse collector motor
    public void collectorBackward(){
        collectorMotor.setPower(-0.1);
    }

    //stop the collector motor
    public void stopCollector(){
        collectorMotor.setPower(0);
    }

    //the set up of encoders
    // had to use math.round to convert a double to an int
    public void runWithEncoders() {
        DcMotorController.RunMode l_mode =
                rightMotorController.getMotorChannelMode
                        (((int) Math.round(getRightMotors())));
        if (l_mode == DcMotorController.RunMode.RESET_ENCODERS)
        {
            rightMotorController.setMotorChannelMode
                    ( ((int) Math.round(getRightMotors())), DcMotorController.RunMode.RUN_USING_ENCODERS);
        }
    }

    public void resetEncoders() {
        //
        // Reset the motor encoders on the drive wheels.
        //
        rightMotorController.setMotorChannelMode
                (((int) Math.round(getRightMotors())), DcMotorController.RunMode.RESET_ENCODERS);
    }

    public boolean encoderCountReached(double rightCount)
    {
        //
        // Assume failure.
        //
        boolean l_status = false;

        //
        // Have the encoders reached the specified values?
        //
        // TODO Implement stall code using these variables.
        //
        if ((Math.abs (rightFrontMotor.getCurrentPosition ()) > rightCount))
        {
            //
            // Set the status to a positive indication.
            //
            l_status = true;
        }

        //
        // Return the status.
        //
        return l_status;

}



    @Override public void loop() {

    }
}
