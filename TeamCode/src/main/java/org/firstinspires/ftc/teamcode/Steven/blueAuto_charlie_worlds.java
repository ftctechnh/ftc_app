package org.firstinspires.ftc.teamcode.Steven;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;


import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Main.AutonomousGeneral_charlie;


/**
 * Created by adityamavalankar on 1/13/17.
 */
@Autonomous(name = "blueBeaconCharlieWorlds")
public class blueAuto_charlie_worlds extends AutonomousGeneral_charlie {



    //
    boolean second_beacon_press = false;
    String currentTeam = "blue";
    private ElapsedTime runtime = new ElapsedTime();
    public int gyro_corr_cnt =0;
    public int beacon_angle;
    public double pathhighspeed = 0.8;
    public boolean ball_first = false;
    //String currentColor = "blank";
    int initialHeading =361;
    public double[] timeProfile = new double[30];
    int profileindex = 0;

    double servoLeftPos = 0;
    double servoRightPos = 1;
    double initialPos = 0.5;

    @Override
    public void runOpMode() {

        initiate();
        idle();
        setMotorsModeToEncDrive();
        stopMotors();

        autoBeaconPresser.setPosition(initialPos);

        telemetry.addData("READY TO START", "");
        telemetry.update();


        waitForStart();
        runtime.reset();
        second_beacon_press = false;


        setMotorsModeToEncDrive();
        stopMotors();
        timeProfile[profileindex++] = runtime.milliseconds();

        encoderMecanumCrossDrive(1,150,150,5,2);
        encoderMecanumDrive(1,20,20,5,1);
        timeProfile[profileindex++] = runtime.milliseconds();
        servoBeaconPress();

        }
    public void servoBeaconPress(){
        boolean left_detected = false;
        boolean beacon_press_success = false;

        lineAlignStrafe();
        moveTowardWall();
        ColorSensorRead();//sets servo to correct position
       pressBeaconButton();
        autoBeaconPresser.setPosition(initialPos);
        if (second_beacon_press == false)
        {

            moveToNextBeacon();
        }
        else
        {
            setMotorsModeToRangeSensing();
            parkCenterVortex();
        }
        // below evaluate beacon press result and move to next step if it is success and handle failures if failures are seen
    }

    public void  moveToNextBeacon(){
        second_beacon_press = true;
        setMotorsModeToEncDrive();;
        encoderMecanumDrive(1,100,100,5,1);

        //if use strafing, add error correction so that it stays within a certain distance of the wall
        servoBeaconPress();

}
    public void ColorSensorRead(){
        readNewColorLeft();
        sleep(1000);
        //readNewColorRight();

        telemetry.addData("","READ DATA");
        telemetry.update();


        if (currentColorBeaconLeft.equals(currentTeam)){
            autoBeaconPresser.setPosition(servoLeftPos);

            //telemetry.addData("left color", currentColorBeaconLeft);
            telemetry.addData("left color", currentColorBeaconLeft);
            telemetry.addData("current team", currentTeam);
            telemetry.update();
        }
        else if(currentColorBeaconLeft.equals("red")){
            autoBeaconPresser.setPosition(servoRightPos);
            telemetry.addData("left color", currentColorBeaconLeft);
            telemetry.update();
        }
        //sleep(50000);

    }

    public void lineAlignStrafe() {
        //moveTowardWall();
        if(second_beacon_press){
            setMotorsToEnc(29, 29, 0.3);
        }
        else {
            setMotorsModeToColorSensing();
        }
        strafeRight(0.3);
        while(whiteLineDetectedBack() == false){


        }
        stopMotors();

        stopMotors();
        timeProfile[profileindex++] = runtime.milliseconds();


    }

    public void lineAlign() {

        if(second_beacon_press){
            setMotorsToEnc(29, 29, 0.3);
        }
        else {
            setMotorsModeToColorSensing();
        }
            telemetry.addData("DISTANCEDISTANCE:", rangeSensor.getDistance(DistanceUnit.CM));
            telemetry.update();
            if (rangeSensor.getDistance(DistanceUnit.CM) < 30) {
                crossDrive(1, 0.7);
            } else {
                crossDrive(2, 0.7);
            }
            double lineAligntime = runtime.milliseconds()+2500;
            boolean speedflag = false;
            while (whiteLineDetectedBack() == false) {
                if(runtime.milliseconds() > lineAligntime && !speedflag){
                    stopMotors();
                    setMotorsModeToColorSensing();
                    speedflag = true;
                    if (rangeSensor.getDistance(DistanceUnit.CM) < 30) {
                        crossDrive(1, 0.7);
                    } else {
                        crossDrive(2, 0.7);
                    }
                }
                if (rangeSensor.getDistance(DistanceUnit.CM) < 25) {
                    crossDrive(1, 0.7);
                } else if (rangeSensor.getDistance(DistanceUnit.CM) > 50) {
                    crossDrive(2, 0.7);
                }


            }

        stopMotors();
        timeProfile[profileindex++] = runtime.milliseconds();
       

    }



    public void moveTowardWall(){
        setMotorsModeToGyroSensing();
        straightDrive(-0.8);
        telemetry.addData("Distance", rangeSensor.getDistance(DistanceUnit.CM));
        telemetry.update();
        sleep(1000);
        while (rangeSensor.getDistance(DistanceUnit.CM) > 12) {

        }
        stopMotors();
        idle();
        setMotorsModeToGyroSensing();

        straightDrive(0.8);
        while (rangeSensor.getDistance(DistanceUnit.CM) < 10) {

        }
        stopMotors();
        idle();
        telemetry.addData("Distance",rangeSensor.getDistance(DistanceUnit.CM));
        telemetry.update();
    }

    public void printColorsSeen(){
        telemetry.addData("left color", currentColorBeaconLeft);
        telemetry.addData("right color", currentColorBeaconRight);
        telemetry.update();
    }

    public void parkCenterVortex()
    {
        setMotorsModeToEncDrive();
        stopMotors();
        timeProfile[profileindex++] = runtime.milliseconds();//21406.18
        if (second_beacon_press)
        {
           {

                setMotorsModeToEncDrive();
                encoderMecanumCrossDrive(pathhighspeed, 90, 90, 5, 4);

                encoderMecanumDrive(pathhighspeed,56,-56,5,0);
             //   gyroCorrection((beacon_angle + 135));
                encoderShoot(0.8);
                intake_motor.setPower(1);
                sleep(1400);
                intake_motor.setPower(0);
                encoderShoot(0.8);

                encoderMecanumDrive(pathhighspeed,-50,-50,5,0);
                encoderMecanumDrive(pathhighspeed, 10, 10, .2, 0);
                encoderMecanumDrive(pathhighspeed, -30, -30, .2, 0);
                timeProfile[profileindex++] = runtime.milliseconds();//32730.99
            }
        }
        else
        {

        }
        for(int i = 0; i < profileindex; i++){
            //telemetry.addData(""+i,timeProfile[i]);
        }
        telemetry.update();
        sleep(30000);
    }


    public void pressBeaconButton()
    {
        setMotorsModeToEncDrive();
        stopMotors();
        double distFromWall = rangeSensor.getDistance(DistanceUnit.CM)+4;
       // telemetry.addData("",distFromWall);
        telemetry.addData("distance",distFromWall);
        telemetry.update();

        encoderMecanumDrive(0.6, -distFromWall, -distFromWall, 5,0);
//
        sleep(500);
//
        encoderMecanumDrive(0.6, distFromWall, distFromWall, 5,0);
        //setMotorsModeToRangeSensing();
       // moveTowardWall();

//        encoderDrive(.3, -distFromWall, -distFromWall, 1);
//        sleep(500);
//        encoderDrive(.3, distFromWall, distFromWall, 1);
    }
    public void gyroCorrection(int angle){

        ;
        angle = angle%360;
        setMotorsModeToRangeSensing();
        telemetry.addData("gyroCorrection starting", gyro.getHeading());
        telemetry.addData("gyroCorrection cnt",gyro_corr_cnt);
        telemetry.update();
        int gyro_read = gyro.getHeading();
        if((gyro_read >(((angle-5)+360)%360)) && (gyro_read < ((angle+5)%360)))
        {
            return;
        }
        if((angle+180)%360<180){
            if (gyro_read>angle || gyro_read<((angle+180)%360)){//make sure the angle is correct
                turnLeft(0.7);
                while(gyro_read>angle || gyro_read<((angle+180)%360)){
                    gyro_read = gyro.getHeading();
                    idle();
                }
            }
            else{
                turnRight(0.7);
                while(gyro_read<angle && gyro_read>((angle+180)%360)){
                    gyro_read = gyro.getHeading();
                    idle();
                }
            }
        }
        else{
            if (gyro_read>angle && gyro_read<((angle+180)%360)){//make sure the angle is correct
                turnLeft(0.7);
                while(gyro_read>angle && gyro_read<((angle+180)%360)){
                    gyro_read = gyro.getHeading();
                }
            }
            else{
                turnRight(0.7);
                while(gyro_read<angle || gyro_read>((angle+180)%360)){
                    gyro_read = gyro.getHeading();
                    //idle();
                }
            }
        }

        /*gyro_corr_cnt++;
        telemetry.addData("gyroCorrection done", gyro.getHeading());
        telemetry.addData("gyroCorrection cnt",gyro_corr_cnt);
        telemetry.update();*/
        stopMotors();

    }

    public void minimizeError(){
        front_left_motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        front_right_motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        back_right_motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        back_left_motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        encoderMecanumDrive(0.3,5,5,4,0);
        front_left_motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        front_right_motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        back_right_motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        back_left_motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        //stopMotors();
    }



}
//-------------------------------------------------------------------//


