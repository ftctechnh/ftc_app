
package org.usfirst.ftc.theintersect.code;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;


@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "Full Mission")
public class AutoFull extends LinearOpMode {
    static DcMotor rF, rB, lF, lB, flywheel1, flywheel2, sweeperLow;
    static GyroSensor gyro;
    static ColorSensor beaconColor,floorColor;
    static int conversionFactor = 50;
    static Servo sideWall;
    static CRServo buttonPusher;
    boolean red = false;
    boolean center = true;
    boolean timedOut = false;
    boolean getBeacons = true;
    double average;
    DriveTrain driveTrain;
    char alliance;
    String parkCenter;
    int state;
    int beaconAction = 0;
    int ambientBlue = 0;
    int ambientRed = 0;

    @Override
    public void runOpMode() throws InterruptedException {
        double speed = 0.2;
        int gyroRampMax = 20;
        int gyroRampMin = 3;
        int shootingPositionDistance = 15;
        int wallPositionDriveDistance = 50;


        initHardware();
        while(!gamepad1.start && !isStopRequested()) {
            if (gamepad1.x) {
                shootingPositionDistance -=1;
                Functions.waitFor(300);
            }
            if (gamepad1.y) {
                shootingPositionDistance +=1;
                Functions.waitFor(300);
            }
            if (gamepad1.a) {
                wallPositionDriveDistance -=1;
                Functions.waitFor(300);
            }
            if (gamepad1.b) {
                wallPositionDriveDistance +=1;
                Functions.waitFor(300);
            }
            if (gamepad1.left_bumper) {
                speed = speed - 0.1;
                Functions.waitFor(300);
            }
            if (gamepad1.right_bumper) {
                speed = speed + 0.1;
                Functions.waitFor(300);
            }
            if (gamepad1.back) {
                red = !red;
                Functions.waitFor(300);
            }
            if (gamepad1.dpad_down) {
                center = !center;
                Functions.waitFor(300);
            }
            if (gamepad1.dpad_up) {
                getBeacons = !getBeacons;
                Functions.waitFor(300);
            }

            alliance = (red)? 'r' :'b';
            parkCenter = (center) ? "center" : "corner";
            telemetry.addData("Alliance:", alliance);
            telemetry.addData("Get Beacons? ", getBeacons);
            telemetry.addData("Park on ", parkCenter);
            telemetry.addData("ambient Blue ", ambientBlue);
            telemetry.addData("ambient Red ", ambientRed);
            telemetry.addData("Shoot dist:", shootingPositionDistance);
            telemetry.addData("Wall dist", wallPositionDriveDistance);
            telemetry.addData("Rotation speed", speed);
            telemetry.update();
        }
//        driveTrain.SetGryrTurnParameters(gyroRampMax, gyroRampMin, 0.5);
        int heading = gyro.getHeading(); // set initial heading
        waitForStart();
        state = 0;// Todo:
        if (opModeIsActive() ) {
           // if(getBeacons)
            if(!getBeacons){
                Functions.waitFor(10000);
                if(red){
                    driveTrain.moveRight(1,25,3);
                }else{
                    driveTrain.moveLeft(1,25,3);
                }
            }
                    telemetry.addData("0: Drive to shoot ", heading);
                    // Drive backward ( heading = 0 )
                    double voltage = hardwareMap.voltageSensor.get("flywheel").getVoltage();
                    runFlywheel(((-0.155 * voltage) + 2.459 + .2));
                    sideWall.setPosition(Functions.sideWallDownPos);
                    driveTrain.moveBkwd(0.3, shootingPositionDistance, 3);
                    state = 1;
                    // @ shooting position, Shoot ball
                    telemetry.addLine("1: Shoot");
                    double voltage1 = hardwareMap.voltageSensor.get("flywheel").getVoltage();
                    double shootingPower = 730.09* Math.exp(-.582 * voltage1) + .2;
                    autoShoot( shootingPower, 4000, 750); // Todo:read voltage
                    state = 2;
                    telemetry.addData("voltage ", voltage1);
                    telemetry.addData("power", shootingPower);
                    telemetry.update();
                    telemetry.addLine("2: Turn ");
            if(getBeacons) {
                // Move towards wall for beacon - 1)Turn, 2)Drive, 3)Turn, 4)Align with wall
                // if (Red) Turn -45 Degree, else Turn 45 Degree
                heading = driveTrain.rotateGyroRamp(red ? -50 : 50, 0.3, 5, gyro, telemetry);
                state = 3;
                telemetry.addData("3: Drive to wall ", heading);
                // Drive backward 55? inch
                driveTrain.moveBkwd(0.7, wallPositionDriveDistance, 5);

                state = 4;
                telemetry.addLine("4: Rotate ");
                // Turn 45(red)/ 135(blue) ( Heading 0/180 )
                heading = driveTrain.rotateGyroRamp(((red) ? 50 : 130), .3, 5, gyro, telemetry);
                state = 6;
                telemetry.addData("6: Wall Alignment and move fwd ", heading);
                // align against wall
                driveTrain.moveRight(1, 500, 3);
                state = 7;
                telemetry.addLine("7: Seek beacon 1");
                // Drive backward(red)/forward(blue) till sensor found
                boolean foundBeacon;
                if (red) {
                    //foundBeacon = colorDriveBkwd(.25, 'r', 2);
                    driveTrain.moveFwdRight(-1,.8,25,3);
                    foundBeacon = colorDriveFwdAmbient(.25, 'r', 3);
                } else {
                    //foundBeacon = colorDriveFwd(.25, 'b', 2);
                    driveTrain.moveFwdRight(1,.8,25,3);
                    foundBeacon = colorDriveBkwdAmbient(.25, 'b', 3);
                }
                if (foundBeacon) {
                    pressBeacon(1500, 750);
                    beaconAction += 1;
                    if (red) {
                        //driveTrain.moveBkwd(1, 25, 3);
                        driveTrain.moveFwdRight(-1, 0.8, 25, 3);
                    } else {
                        //driveTrain.moveFwd(1, 25, 3);
                        driveTrain.moveFwdRight(1, 0.8, 25, 3);
                    }
                    //driveTrain.moveRight(1, 500, 2);//
                }
                state = 8;
                telemetry.addLine("8: Seek beacon 2");
                // try 2nd beacon
                if (red) {
                    foundBeacon = colorDriveBkwdAmbient(.25, 'r', 3);
                } else {
                    foundBeacon = colorDriveFwdAmbient(.25, 'b', 3);
                }
                if (foundBeacon) {
                    pressBeacon(1500, 750);
                    beaconAction += 2;
                }
                state = 9;
                if (center) {
                    telemetry.addLine("9: Turn towards center");
                    // Turn -45(red)/45(blue) ( Heading 315/225 )
                    heading = driveTrain.rotateGyroRamp((red ? -45 : 45), speed, 5, gyro, telemetry);
                    state = 10;
                } else {
                    telemetry.addLine("going to corner!!!");
                    driveTrain.moveLeft(1, 10, 5);
                }
                // Drive forward
                telemetry.addLine("10: To Parking");
                if (red) {
                    if (center) {
                        driveTrain.moveFwd(1, 60, 3);
                    }else {
                        driveTrain.moveFwd(1, 100, 3);
                    }

                } else {
                    if (red) {
                        driveTrain.moveBkwd(1, 60, 3);
                    }else {
                        driveTrain.moveBkwd(1, 100, 3);
                    }
                }
            }
            else{
                if(center){
                    Functions.waitFor(5000);
                    driveTrain.moveBkwd(1,45,5);
                    driveTrain.rotateCWGyro(-90,.4,3,gyro,telemetry);
                } else{
                    if(red){
                        driveTrain.moveRight(1, 60, 3);
                    }else{

                        driveTrain.moveLeft(1, 60, 3);
                    }
                    driveTrain.moveFwd(1,20,3);
                }
            }
            }
        }



    public void initHardware() {
        buttonPusher = hardwareMap.crservo.get("buttonPusher");
        rF = hardwareMap.dcMotor.get("rF");
        rB = hardwareMap.dcMotor.get("rB");
        lF = hardwareMap.dcMotor.get("lF");
        lB = hardwareMap.dcMotor.get("lB");
        lB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rF.setDirection(DcMotor.Direction.FORWARD);
        rB.setDirection(DcMotor.Direction.FORWARD);
        lB.setDirection(DcMotor.Direction.REVERSE);
        lF.setDirection(DcMotor.Direction.REVERSE);


        flywheel1 = hardwareMap.dcMotor.get("flywheel1");
        flywheel2 = hardwareMap.dcMotor.get("flywheel2");
        flywheel1.setDirection(DcMotorSimple.Direction.REVERSE);

        //beaconColor = hardwareMap.colorSensor.get("beaconColor");
        floorColor = hardwareMap.colorSensor.get("floorColor");
        beaconColor = hardwareMap.colorSensor.get("beaconColor");
        sweeperLow = hardwareMap.dcMotor.get("sweeperLow");
        sideWall = hardwareMap.servo.get("sideWall");
        sideWall.setPosition(Functions.sideWallUpPos);
        beaconColor.setI2cAddress(I2cAddr.create8bit(0x3c));
        gyro = hardwareMap.gyroSensor.get("gyro");
        driveTrain = new DriveTrain(lB, rB, lF, rF, this, gyro, floorColor);
        Servo lLift = hardwareMap.servo.get("lLift");
        Servo rLift = hardwareMap.servo.get("rLift");
        sideWall.setPosition(Functions.sideWallUpPos);
        rLift.setDirection(Servo.Direction.REVERSE);
        lLift.setPosition(Functions.liftUpPos);
        rLift.setPosition((Functions.liftUpPos));
        detectAmbientLight(beaconColor);
        calibrateGyro(telemetry);
    }
    public void cornerPark(){
        driveTrain.moveLeft(1, 10, 5000);
        if(alliance == 'r'){
            driveTrain.moveFwd(1, 90, 5000);

        }else{
            driveTrain.moveBkwd(1, 90, 5000);

        }
    }
    public void centerPark(){
        driveTrain.moveLeft(1, 62, 5000);
        if(alliance == 'r'){
            driveTrain.moveFwd(1, 50, 5000);

        }else{
            driveTrain.moveBkwd(1, 50, 5000);

        }
    }

    /***
     *     Gyro calibration routing
     *     @param telemetry - telemetry printing handle
     *
      */

    private void calibrateGyro(Telemetry telemetry){
        gyro.calibrate();
        // make sure the gyro is calibrated before continuing
        while (gyro.isCalibrating() && !isStopRequested())  {
            Functions.waitFor(50);
        }
        telemetry.addLine("Robot Ready.");
        telemetry.update();
    }

    /***
     *     Shoot all balls out and extend pusher??
     *     @param speed - flywheel speed
     *     @param flywheelTime
     *     @param pusherTime - extend pusher out for pusherTime
     */

    private void autoShoot(double speed, int flywheelTime, int pusherTime) {
      //  runFlywheel(speed);
     //   Functions.waitFor(1000);
        sweeperOn(-.7);
        extendPusher(pusherTime);
        sideWall.setPosition(Functions.sideWallDownPos);
        Functions.waitFor(flywheelTime - pusherTime - 1000);
        runFlywheel(0);
        sweeperOff();
    }
    private boolean colorDriveBkwdDifferential(double speed ,char color, int timeoutS){
        timedOut = false;
        boolean success = false;
        long endTime = System.currentTimeMillis() + (timeoutS * 1000);
        driveTrain.moveBkwd(speed);
        if(color == 'b') {
            while (System.currentTimeMillis() < endTime && opModeIsActive()) {
                if((beaconColor.blue()-beaconColor.red()) > Functions.differentialThreshold) {
                    success = true;
                    break;
                }
            }
        } else if(color == 'r') {
            while (System.currentTimeMillis() < endTime && opModeIsActive()) {
                if(beaconColor.red() - beaconColor.blue()> Functions.differentialThreshold) {
                    success = true;
                    break;
                }
            }
        }
        driveTrain.stopAll();
        telemetry.clear();
        telemetry.addData("Blue", beaconColor.blue());
        telemetry.addData("Red", beaconColor.red());
        telemetry.update();
        return success;
    }
    private boolean colorDriveBkwd(double speed ,char color, int timeoutS){
        timedOut = false;
        boolean success = false;
        long endTime = System.currentTimeMillis() + (timeoutS * 1000);
        driveTrain.moveBkwd(speed);
        if(color == 'b') {
            while (System.currentTimeMillis() < endTime && opModeIsActive()) {
                if(detectBlue(beaconColor) && beaconColor.blue()>beaconColor.red()) {
                    success = true;
                    break;
                }
            }
        } else if(color == 'r') {
            while (System.currentTimeMillis() < endTime && opModeIsActive()) {
                if(detectRed(beaconColor) && beaconColor.red()>beaconColor.blue()) {
                    success = true;
                    break;
                }
            }
        }
        driveTrain.stopAll();
        telemetry.clear();
        telemetry.addData("Blue", beaconColor.blue());
        telemetry.addData("Red", beaconColor.red());
        telemetry.update();
        return success;
    }
    private boolean colorDriveFwd(double speed,char color, int timeoutS){
        timedOut = false;
        boolean success = false;
        long endTime = System.currentTimeMillis() + (timeoutS * 1000);
        driveTrain.moveFwd(speed);
        if(color == 'b') {
            while (System.currentTimeMillis() < endTime && opModeIsActive()) {
                if(detectBlue(beaconColor) && beaconColor.blue()>beaconColor.red()) {
                    success = true;
                    break;
                }
            }
        } else if(color == 'r') {
            while (System.currentTimeMillis() < endTime && opModeIsActive()) {
                if(detectRed(beaconColor) && beaconColor.red()>beaconColor.blue()) {
                    success = true;
                    break;
                }
            }
        }
        driveTrain.stopAll();
        telemetry.clear();
        telemetry.addData("Blue", beaconColor.blue());
        telemetry.addData("Red", beaconColor.red());
        telemetry.update();
        return success;
    }




    private boolean colorDriveBkwdAmbient(double speed ,char color, int timeoutS){
        timedOut = false;
        boolean success = false;
        long endTime = System.currentTimeMillis() + (timeoutS * 1000);
        driveTrain.moveBkwd(speed);
        if(color == 'b') {
            while (System.currentTimeMillis() < endTime && opModeIsActive()) {
                if(detectBeaconAmbient( color, beaconColor) && beaconColor.blue()>beaconColor.red()) {
                    success = true;
                    break;
                }
            }
        } else if(color == 'r') {
            while (System.currentTimeMillis() < endTime && opModeIsActive()) {
                if(detectBeaconAmbient(color, beaconColor) && beaconColor.red()>beaconColor.blue()) {
                    success = true;
                    break;
                }
            }
        }
        driveTrain.stopAll();
        telemetry.clear();
        telemetry.addData("Blue", beaconColor.blue());
        telemetry.addData("Red", beaconColor.red());
        telemetry.addData("Ambient Blue", ambientBlue);
        telemetry.addData("Ambient Red", ambientRed);
        telemetry.update();
        return success;
    }
    private boolean colorDriveFwdAmbient(double speed,char color, int timeoutS){
        timedOut = false;
        boolean success = false;
        long endTime = System.currentTimeMillis() + (timeoutS * 1000);
        driveTrain.moveFwd(speed);
        if(color == 'b') {
            while (System.currentTimeMillis() < endTime && opModeIsActive()) {
                if(detectBeaconAmbient(color, beaconColor) && beaconColor.blue()>beaconColor.red()) {
                    success = true;
                    break;
                }
            }
        } else if(color == 'r') {
            while (System.currentTimeMillis() < endTime && opModeIsActive()) {
                if(detectBeaconAmbient(color, beaconColor) && beaconColor.red()>beaconColor.blue()) {
                    success = true;
                    break;
                }
            }
        }
        driveTrain.stopAll();
        telemetry.clear();
        telemetry.addData("Blue", beaconColor.blue());
        telemetry.addData("Red", beaconColor.red());
        telemetry.addData("Ambient Blue", ambientBlue);
        telemetry.addData("Ambient Red", ambientRed);
        telemetry.update();
        return success;
    }





    public void extendPusher(int time){
        buttonPusher.setPower(1);
        Functions.waitFor(time);
        buttonPusher.setPower(0);

    }
    public void retractPusher(int time){
        buttonPusher.setPower(-1);
        Functions.waitFor(time);
        buttonPusher.setPower(0);

    }

    public void runFlywheel(double power){
        flywheel2.setPower(power);
        flywheel1.setPower(power);
    }

    public void wallAlign(){
        driveTrain.strafeRight(.6);
        Functions.waitFor(1000);
        driveTrain.stopAll();
    }
    public void wallGTFO(){
        driveTrain.strafeLeft(0.5);
        Functions.waitFor(500);
        driveTrain.stopAll();
    }
    public void pressBeacon(int extendTime, int retractTime){
        extendPusher(extendTime);
        retractPusher(retractTime);
    }
    public void moveBeacon(){
        boolean beaconRed = detectRed(beaconColor);
        if (beaconRed){
            telemetry.addLine("blue detected");
            telemetry.addData("red", beaconColor.red());
            telemetry.addData("blue", beaconColor.blue());
            telemetry.update();
            wallGTFO();
            Functions.waitFor(200);
            rB.setPower(.3);
            lF.setPower(.3);
            rF.setPower(.3);
            lB.setPower(.3);
            Functions.waitFor(400);
            driveTrain.stopAll();
        } else{
            telemetry.addLine("red detected");
            telemetry.addData("red", beaconColor.red());
            telemetry.addData("blue", beaconColor.blue());
            telemetry.update();
            Functions.waitFor(1000);
            /*driveTrain.strafeLeft(0.5);
            Functions.waitFor(300);*/
            /*driveTrain.moveBkwd(0.3);
            Functions.waitFor(300);
            driveTrain.stopAll();*/
        }
    }
    public void resetEncoders() {
        lF.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lF.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        lB.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lB.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rF.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rF.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rB.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rB.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }
    public void firstBeacon(){
        if (red) {
            driveTrain.strafeLeft(0.3, 18, 5);
            driveTrain.moveBkwd(0.3,6, 5);
            driveTrain.moveFwd(0.2);
            while (!detectRed(beaconColor) && opModeIsActive()){
            }
            driveTrain.stopAll();
            buttonPusher.setPower(0.6);
            driveTrain.strafeRight(0.2,3,5);
        }
        else {
            driveTrain.strafeRight(0.3, 18,5);
            driveTrain.moveBkwd(0.3,6,5);
            driveTrain.moveFwd(0.2);
            while (!detectBlue(beaconColor) && opModeIsActive()){
            }
            driveTrain.stopAll();
            buttonPusher.setPower(0.6);
            driveTrain.strafeRight(0.2,3,3);
        }
    }
    public static void waitFor(int mill) {
        try {
            Thread.sleep(mill);
        } catch (Exception e) {
            Thread.currentThread().interrupt();
        }
    }

    public static double convertGamepad(float y) {
        int m;
        if (y < 0) {
            m = 1;
        } else {
            m = -1;
        }
        return m * (1 - Math.sqrt(1 - (y * y)));
    }


    public void sweeperOn(double power){
        sweeperLow.setPower(power);
    }
    public void sweeperOff(){
        sweeperLow.setPower(0);
    }


    public void flywheelOn(double power){
        for(double i = 0; i < power*10; i++){
           flywheel1.setPower(i/10);
           flywheel2.setPower(i/10);
            Functions.waitFor(100);
        }
    }
    public void flywheelOff(double power){
        for(double i = power*10; i > 0; i--){
            flywheel1.setPower(i/10);
            flywheel2.setPower(i/10);
            Functions.waitFor(100);
        }
    }



    public void debugColor(ColorSensor colorsensor, Telemetry telemetry) {
        while (System.currentTimeMillis()
                < System.currentTimeMillis() + 10000000000L && opModeIsActive()) {
            telemetry.addData("Red", colorsensor.red());
            telemetry.addData("Green", colorsensor.green());
            telemetry.addData("Blue", colorsensor.blue());
            telemetry.update();
            Functions.waitFor(50);
        }
    }

    public boolean detectWhite(ColorSensor colorsensor) {
        int red = colorsensor.red();
        int green = colorsensor.green();
        int blue = colorsensor.blue();
        average = (red + green + blue) / 3;
        return (average > 17);
    }


    public boolean detectBlue(ColorSensor colorsensor) {
        int blue = colorsensor.blue();
        return blue > Functions.blueThreshold;
    }
    public void detectAmbientLight(ColorSensor colorsensor){
        ambientBlue = colorsensor.blue();
        ambientRed = colorsensor.red();
    }
    public boolean detectBeaconAmbient(char color, ColorSensor colorsensor){
        if(color == 'b')
            return colorsensor.blue() - ambientBlue > Functions.blueAmbientThreshold;
        else if(color == 'r')
            return colorsensor.red() - ambientRed > Functions.redAmbientThreshold;
        else return false;
    }

    public boolean detectRed(ColorSensor colorsensor) {
        int red = colorsensor.red();
        int blue = colorsensor.blue();
        return red>Functions.redThreshold;
    }
    public void stopAtWhite(double power, long timeout, Telemetry telemetry) {
        driveTrain.moveFwd(power);
        long endTime = System.currentTimeMillis() + (timeout * 1000);
        while (System.currentTimeMillis() < endTime) {
            if (detectWhite(beaconColor)) {
                break;
            }
        }
        driveTrain.stopAll();
        waitFor(5000);
    }
    public void options(){
        telemetry.addData("Team", "Blue");
        telemetry.update();
        boolean confirmed = false;
        red = false;
        while(!confirmed){
            if (gamepad1.a){
                red = true;
                telemetry.addData("Team", red ? "Red": "Blue");
            }
            if (gamepad1.b){
                red = false;
                telemetry.addData("Team", red ? "Red": "Blue");
            }
            telemetry.update();

            if (gamepad1.left_stick_button && gamepad1.right_stick_button){
                telemetry.addData("Team", red ? "Red" : "Blue");
                telemetry.addData("Confirmed!", "");
                telemetry.update();
                confirmed = true;
            }

        }
    }

    private void haltUntilPressStart() {
        while (!gamepad1.start  && !isStopRequested()) {
            Functions.waitFor(300);
        }
    }
}
