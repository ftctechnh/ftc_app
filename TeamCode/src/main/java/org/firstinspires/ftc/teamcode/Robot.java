package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import static java.lang.Thread.sleep;

public class Robot {

    MecanumDrivetrain drivetrain;

    private HardwareMap hwMap =  null;
    OpMode opMode;
    private DcMotor lIntake, rIntake, lift, relic;
    private Servo hopper, gripper, jewelPivot, jewelArm, relicGripper, relicArm;

    private ColorSensor colorSensor;

    private ClosableVuforiaLocalizer vuforia;
    private ElapsedTime vutimer = new ElapsedTime();
    String vuforiaLicenseKey = "Ae3H91v/////AAAAGT+4TPU5r02VnQxesioVLr0qQzNtgdYskxP7aL6/" +
            "yt9VozCBUcQrSjwec5opfpOWEuc55kDXNNSRJjLAnjGPeaku9j4nOfe7tWxio/xj/uNdPX7fEHD0j5b" +
            "5M1OgX/bkWoUV6pUTAsKj4GaaAKIf76vnX36boqJ7BaMJNuhkYhoQJWdVqwFOC4veNcABzJRw4mQmfO" +
            "3dfPvNVjxDl8kgdBEQOZRi9kFDy9w3cTLatSGZne3IvyaYYd8uckzPnQb5Mgel3ORjar/84qO+GBmG2" +
            "vDhmiv+vkY4gbCtS0em5LM+7CIMuZa5vO9GmtqXyNsoCp9zpPlgZHc1OJ7javiI5jAzWEKCPjZcmLAkSs7k+amw";


    public void init(HardwareMap ahwMap, LinearOpMode linOp) {
        // Save reference to Hardware map
        hwMap = ahwMap;
        opMode = linOp;

        lIntake = hwMap.get(DcMotor.class, "LI");
        rIntake = hwMap.get(DcMotor.class, "RI");
        lift = hwMap.get(DcMotor.class, "lift");
        relic = hwMap.get(DcMotor.class, "relic");

        hopper = hwMap.get(Servo.class, "hopper");
        gripper = hwMap.get(Servo.class, "gripper");
        jewelPivot = hwMap.get(Servo.class, "jewelPivot");
        jewelArm = hwMap.get(Servo.class, "jewelArm");
        relicGripper = hwMap.get(Servo.class, "rGripper");
        relicArm = hwMap.get(Servo.class, "rArm");

        colorSensor = hwMap.get(ColorSensor.class, "sensor_color");

        hopper.setPosition(0.4);
        gripper.setPosition(0.65);
        jewelPivot.setPosition(0.5);
        jewelArm.setPosition(0.98);
        relicGripper.setPosition(0.6);
        relicArm.setPosition(1);

        drivetrain = new MecanumDrivetrain(this);

        drivetrain.init((ahwMap));
    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap, OpMode op) {
        // Save reference to Hardware map
        hwMap = ahwMap;
        opMode = op;

        lIntake = hwMap.get(DcMotor.class, "LI");
        rIntake = hwMap.get(DcMotor.class, "RI");
        lift = hwMap.get(DcMotor.class, "lift");
        relic = hwMap.get(DcMotor.class, "relic");

        hopper = hwMap.get(Servo.class, "hopper");
        gripper = hwMap.get(Servo.class, "gripper");
        jewelPivot = hwMap.get(Servo.class, "jewelPivot");
        jewelArm = hwMap.get(Servo.class, "jewelArm");
        relicGripper = hwMap.get(Servo.class, "rGripper");
        relicArm = hwMap.get(Servo.class, "rArm");

        hopper.setPosition(0.4);
        gripper.setPosition(0.65);
        jewelPivot.setPosition(0.5);
        jewelArm.setPosition(0.98);
        relicGripper.setPosition(0.6);
        relicArm.setPosition(1);

        drivetrain = new MecanumDrivetrain(this);

        drivetrain.init((ahwMap));
    }


    // Moves the drive train using the given x, y, and rotational velocities
    public void drive(double xVelocity, double yVelocity, double wVelocity){
        drivetrain.drive(xVelocity, yVelocity, wVelocity);
    }

    public void encoderDrive(double yDist, double maxSpeed){
        drivetrain.encoderDrive(yDist, maxSpeed);
    }

    public void gyroTurn(int wDist, String direction){
        drivetrain.gyroTurn(wDist, direction);
    }

    public void runIntake(double power){
        lIntake.setPower(-power);
        rIntake.setPower(power);
    }

    public void runLift(double power){
        lift.setPower(power);
    }

    public void runRelic(double power){
        relic.setPower(power);
    }

    public void setHopperPosition(double position){
        hopper.setPosition(position);
    }

    public void setGripperPosition(double position){
        gripper.setPosition(position);
    }

    public void setJewelPivotPosition(double position){
        jewelPivot.setPosition(position);
    }

    public void setJewelArmPosition(double position){
        jewelArm.setPosition(position);
    }

    public void setRelicGripperPosition(double position){
        relicGripper.setPosition(position);
    }

    public void setRelicArmPosition(double position){
        relicArm.setPosition(position);
    }

    public void bumpJewel(String alliance){

        jewelArm.setPosition(0.8);
        try{
            sleep(300);
        }catch(InterruptedException e){
            opMode.telemetry.addLine("I broke from sleep. :( (bumpJewel function)");
        }

        jewelPivot.setPosition(0.7);
        try{
            sleep(300);
        }catch(InterruptedException e){
            opMode.telemetry.addLine("I broke from sleep. :( (bumpJewel function)");
        }
        jewelArm.setPosition(0.32);
        try{
            sleep(500);
        }catch(InterruptedException e){
            opMode.telemetry.addLine("I broke from sleep. :( (bumpJewel function)");
        }
        jewelPivot.setPosition(0.6);
        try{
            sleep(300);
        }catch(InterruptedException e){
            opMode.telemetry.addLine("I broke from sleep. :( (bumpJewel function)");
        }
        jewelArm.setPosition(0.27);


        float[] hsvValues = new float[3];
        final float values[] = hsvValues;

        colorSensor.enableLed(true);
        opMode.telemetry.addLine("raw Android color: ")
                .addData("a", "%02x", colorSensor.alpha())
                .addData("r", "%02x", colorSensor.red())
                .addData("g", "%02x", colorSensor.green())
                .addData("b", "%02x", colorSensor.blue());
        opMode.telemetry.update();

        try{
            sleep(500);
        }catch(InterruptedException e){
            opMode.telemetry.addLine("I broke from sleep. :( (bumpJewel function)");
            opMode.telemetry.update();
        }


        if(alliance.equals("blue")){
            if(colorSensor.red() > colorSensor.blue()){
                jewelPivot.setPosition(0.8);
            }
            else if(colorSensor.red() < colorSensor.blue()){
                jewelPivot.setPosition(0.4);
            }
        }
        else if(alliance.equals("red")){
            if(colorSensor.red() > colorSensor.blue()){
                jewelPivot.setPosition(0.4);
            }
            else if(colorSensor.red() < colorSensor.blue()){
                jewelPivot.setPosition(0.8);
            }
        }

        try{
            sleep(500);
        }catch(InterruptedException e){
            opMode.telemetry.addLine("I broke from sleep. :( (bumpJewel function)");
            opMode.telemetry.update();
        }

        jewelArm.setPosition(0.8);
        jewelPivot.setPosition(0.5);


    }


    public String activateVuforia () {
        int cameraMonitorViewId = hwMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hwMap.appContext.getPackageName()); // Get the camera!
        VuforiaLocalizer.Parameters parameters_Vuf = new VuforiaLocalizer.Parameters(cameraMonitorViewId);  // Prepare the parameters
        parameters_Vuf.vuforiaLicenseKey = vuforiaLicenseKey;
        parameters_Vuf.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;  // Look through the inward facing camera
        this.vuforia = new ClosableVuforiaLocalizer(parameters_Vuf);             // Apply Parameters

        VuforiaTrackables Cryptokey = this.vuforia.loadTrackablesFromAsset("RelicVuMark");  // Create VuMarks from Pictograph
        VuforiaTrackable Targets = Cryptokey.get(0);
        Targets.setName("Targets");

        Cryptokey.activate();
        opMode.telemetry.addData("Searching for Key", "...");
        opMode.telemetry.update();
        String vufkey = decryptKey(Targets);
        opMode.telemetry.addData("Key Found", vufkey);
        opMode.telemetry.update();

        this.vuforia.close();
        return vufkey;
    }


    private String decryptKey(VuforiaTrackable cryptokeys) {

        String key = "";
        vutimer.reset();
        RelicRecoveryVuMark vuMark;
        while (((LinearOpMode) opMode).isStarted()) {
            vuMark = RelicRecoveryVuMark.from(cryptokeys);

            if (vuMark == RelicRecoveryVuMark.LEFT) {       //Store which cryptokey is found and
                key = "KeyLeft";            //update the telemetry accordingly.
                opMode.telemetry.addData("Spotted Key", "Left!");
                opMode.telemetry.update();
            } else if (vuMark == RelicRecoveryVuMark.CENTER) {
                key = "KeyCenter";
                opMode.telemetry.addData("Spotted Key", "Center!");
                opMode.telemetry.update();
            } else if (vuMark == RelicRecoveryVuMark.RIGHT) {
                key = "KeyRight";
                opMode.telemetry.addData("Spotted Key", "Right!");
                opMode.telemetry.update();
            } else {
                key = "KeyUnknown";
                opMode.telemetry.addData("Spotted Key", "Unknown");
                opMode.telemetry.update();
            }
            if(((LinearOpMode) opMode).isStopRequested() || key != "KeyUnknown" || vutimer.time() > 3) {
                break;           //Stop this code if a stop is requested, or if the VuMark has been
            }                    //found, or if more than 3 seconds have passed.
        }
        return key;
    }





}
