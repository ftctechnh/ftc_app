package teamcode;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.vuforia.HINT;
import com.vuforia.Vuforia;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import static java.lang.Math.sqrt;

public class KiwiRobot {
    private final boolean TELEMETRY_ON = true;

    private double SQRT_3 = sqrt(3);
    private HardwareMap hardwareMap;
    private Telemetry telemetry;
    private VuforiaLocalizer vuforia;
    private VuforiaTrackables relicTrackables;
    private VuforiaTrackable trackable;

    public DcMotor motor1 = null; // Back wheel
    public DcMotor motor2 = null; // Left front wheel
    public DcMotor motor3 = null; // Right front wheel
    public Servo armServo = null;
    public Servo rightClampServo;
    public Servo jewelServo;
    public ColorSensor sensorColor;
    public DistanceSensor sensorDistance;
    public DigitalChannel digitalTouch;
    
    public KiwiRobot(HardwareMap hardwareMap, Telemetry telemetry) {
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;
        this.init();
    }

    public void driveSpeed(double x, double y, double r) {
        double power1 =  x - r;
        double power2 = (((-.5) * x) - (SQRT_3/2) * y) - r;
        double power3 = (((-.5) * x) + (SQRT_3/2) * y) - r;

        this.motor1.setPower(power1);
        this.motor2.setPower(power2);
        this.motor3.setPower(power3);
    }
    
    public void driveForward(double power, double distanceInches, boolean resetEncoders) {
        if (resetEncoders) {
            this.resetEncoders();
        }

        double f2 = 1020/12; // 850/12;
        double f3 = 1020/12; // 860/12;

        int y2_start = this.motor2.getCurrentPosition();
        int y3_start = this.motor3.getCurrentPosition();
        
        int y2 = (int)((f2 * distanceInches) * (-2 / SQRT_3)) + y2_start;
        int y3 = (int)((f3 * distanceInches) * (2 / SQRT_3)) + y3_start;
        
        this.motor2.setTargetPosition(y2);
        this.motor3.setTargetPosition(y3);
        
        this.motor2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        this.motor3.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                
        this.motor1.setPower(0);
        this.motor2.setPower(power);
        this.motor3.setPower(power);
        
        while (this.motor2.isBusy() && this.motor3.isBusy()) {
        }
        
        this.turnOffMotors();
        this.setRunWithEncoders();
    }

    public void driveLateral(double power, double distanceInches, boolean resetEncoders) {
        if (resetEncoders) {
            this.resetEncoders();
        }

        double f1 = 1200/12;
        double f2 = 420/12;
        double f3 = 420/12;

        int y1_start = this.motor1.getCurrentPosition();
        int y2_start = this.motor2.getCurrentPosition();
        int y3_start = this.motor3.getCurrentPosition();

        int y1 = (int)((f1 * distanceInches) * (1) + y1_start);
        int y2 = (int)(f2 * distanceInches) * (-2) + y2_start;
        int y3 = (int)(f3 * distanceInches) * (-2) + y3_start;

        this.motor1.setTargetPosition(y1);
        this.motor2.setTargetPosition(y2);
        this.motor3.setTargetPosition(y3);

        this.motor1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        this.motor2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        this.motor3.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        this.motor1.setPower(power * 2);
        this.motor2.setPower(power);
        this.motor3.setPower(power);

        while (this.motor1.isBusy() && this.motor2.isBusy() && this.motor3.isBusy()) {
        }

        this.turnOffMotors();
        this.setRunWithEncoders();
    }
    
    public void turnDegrees(double power, double degrees) {
        this.resetEncoders();
        double f1 = 3970 / 360;//3681
        double f2 = 3970 / 360;//3866
        double f3 = 3970 / 360;//3832
        int p1 = (int)(f1 * degrees);
        int p2 = (int)(f2 * degrees);
        int p3 = (int)(f3 * degrees);
        this.motor1.setTargetPosition(p1);
        this.motor2.setTargetPosition(p2);
        this.motor3.setTargetPosition(p3);
        
        this.motor1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        this.motor2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        this.motor3.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                
        this.motor1.setPower(power);
        this.motor2.setPower(power);
        this.motor3.setPower(power);
        
        while (this.motor1.isBusy() && this.motor2.isBusy() && this.motor3.isBusy()) {
        }
        
        this.turnOffMotors();
        this.setRunWithEncoders();
    }
    
    public void resetEncoders() {
        this.motor1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.motor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.motor3.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
    
    public void turnOffMotors() {
        this.motor1.setPower(0);
        this.motor2.setPower(0);
        this.motor3.setPower(0);
    }
    
    private void setRunWithEncoders() {
        this.motor1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.motor2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.motor3.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void liftArm(double currentPosition) {
        double armLiftChange = .0015;
        double newPosition = currentPosition - armLiftChange;
        armServo.setPosition(newPosition);
    }

    private  void lowerArm(double currentPosition) {
        double armLiftChange = .0015;
        double newPosition = currentPosition + armLiftChange;
        armServo.setPosition(newPosition);
    }

    public void closeClampPosition() {
        this.rightClampServo.setPosition(0);
    }

    public void openClampPosition() {
        this.rightClampServo.setPosition(1);
    }

    public void closeClamp(double currentPosition) {
        double newPosition = currentPosition + .0005;
        rightClampServo.setPosition(newPosition);
    }

    public void openClamp(double currentPosition) {
        double newPosition = currentPosition - .0005;
        rightClampServo.setPosition(newPosition);
    }

    //detects if the side the color sensor is facing is red
    public Boolean isJewelRed()
    {
        double differenceFactor = 1.5;
        double red = sensorColor.red();
        double blue = sensorColor.blue();
        if(red >= blue)
        {
            return true;
        }
        return false;
    }

    public int getColumn() {
        int columntoPlaceBlock = -1;
        RelicRecoveryVuMark vumarkImageTracker = RelicRecoveryVuMark.from(this.trackable);

        if(vumarkImageTracker != RelicRecoveryVuMark.UNKNOWN)
        {
            if(vumarkImageTracker == RelicRecoveryVuMark.LEFT)
            {
                columntoPlaceBlock = 1;
            }else if(vumarkImageTracker == RelicRecoveryVuMark.CENTER)
            {
                columntoPlaceBlock = 2;
            }else if(vumarkImageTracker == RelicRecoveryVuMark.RIGHT)
            {
                columntoPlaceBlock = 3;
            }

            /*
            OpenGLMatrix pose = ((VuforiaTrackableDefaultListener) trackable.getListener()).getPose();

            if (pose != null) {
                VectorF translation = pose.getTranslation();
                telemetry.addData(trackable.getName() + "-Translation", translation);

                double degreesToTurn = Math.toDegrees(Math.atan2(translation.get(1), translation.get(2)));
                telemetry.addData(trackable.getName() + "-degrees", degreesToTurn);

                //find out which array value is x, y, and z
                float zero = translation.get(0);
                float one = translation.get(1);
                float two = translation.get(2);
                telemetry.addData(trackable.getName() + "x", zero);
                telemetry.addData(trackable.getName() + "y", one);
                telemetry.addData(trackable.getName() + "z", two);
            }
            */
        }

        return columntoPlaceBlock;
    }

    public int detectVuMark() {
        return -1;
    }

    public boolean isGlyphGrabbed() {
        return !digitalTouch.getState();
    }

    public void writeLog(String msg){
        if (TELEMETRY_ON) {
            telemetry.addData("Status", msg);
        }
    }

    public void updateLog() {
        if (TELEMETRY_ON) {
            telemetry.update();
        }
    }

    public void logEnconders() {
        telemetry.addData("back wheel encoder values", this.motor1.getCurrentPosition());
        telemetry.addData("left wheel encoder values", this.motor2.getCurrentPosition());
        telemetry.addData("right wheel encoder values", this.motor3.getCurrentPosition());
    }

    private void initVuforia() {
        int cameraMonitorViewId = this.hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

        parameters.vuforiaLicenseKey = "AY5Y5vL/////AAAAGX2e2w6Jc0ethIktT/zzRE1khe+fR9Mt2fiD8nQZ5KNecPTwAiKX5OZSAAZD/AeeaQbXrhx/NUL0ItyuFDzn5tzYDrVFnhryOQMyuK6RZsw0qG60IbzEffXP+ppGpWRvx/Owr+hJJpNcrIo6otnFFZ79vGiQQiDohkAAsHNIXymC8/xgHDk0XXhtU+UYA8yyhzIFOVNgwBRmYmNhomE/wmShZK69EOLfpfRVvjwE8dj2vlhwTChJ1r/4GUyXB7yZ092c19r345QEx511Nhl+Oo3PSolBWO2hn43uRZ5IB4e+cvR/O6KMV25ylM1toRR98TM06NmmGlbR3+19NBA9Ej7T2aOvCf3dSa0ZTpT+haT7";
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        parameters.cameraMonitorFeedback = parameters.cameraMonitorFeedback.AXES;

        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);

        Vuforia.setHint(HINT.HINT_MAX_SIMULTANEOUS_IMAGE_TARGETS, 1);

        this.relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        this.trackable = relicTrackables.get(0);
        this.trackable.setName("RelicRecovery");

        /** Start tracking the data sets we care about. */
        this.relicTrackables.activate();
    }

    private void initMotors() {
        this.motor1 = this.hardwareMap.get(DcMotor.class, "motor1");
        this.motor2 = this.hardwareMap.get(DcMotor.class, "motor2");
        this.motor3 = this.hardwareMap.get(DcMotor.class, "motor3");

        this.motor1.setDirection(DcMotor.Direction.FORWARD);
        this.motor2.setDirection(DcMotor.Direction.FORWARD);
        this.motor3.setDirection(DcMotor.Direction.FORWARD);
    }

    private void initServos() {
        this.armServo = this.hardwareMap.get(Servo.class,"armServo");
        this.jewelServo = this.hardwareMap.get(Servo.class,"jewelServo");
        this.rightClampServo = this.hardwareMap.get(Servo.class,"rightClampServo");

        this.armServo.setDirection(Servo.Direction.FORWARD);
        this.jewelServo.setDirection(Servo.Direction.FORWARD);
        this.rightClampServo.setDirection(Servo.Direction.REVERSE);
    }

    private void initColorDistanceSensor() {
        this.sensorColor = hardwareMap.get(ColorSensor.class, "colorDistanceSensor");
        this.sensorDistance = hardwareMap.get(DistanceSensor.class, "colorDistanceSensor");
    }

    private void initTouchSensor() {
        // get a reference to our digitalTouch object.
        this.digitalTouch = hardwareMap.get(DigitalChannel.class, "revTouchSensor");

        // set the digital channel to input.
        this.digitalTouch.setMode(DigitalChannel.Mode.INPUT);
    }

    private void init() {
        int capacity = this.telemetry.log().getCapacity();
        this.writeLog("log capacity = " + capacity);
        this.writeLog("start init");
        this.initVuforia();
        this.initMotors();
        this.initServos();
        this.initColorDistanceSensor();
        this.initTouchSensor();
        this.writeLog("end init");
    }

    public boolean grabGlyph() {
        closeClaw();
        boolean grabbed = isGlyphGrabbed();
        if (!grabbed) {
            openClaw();
        }
        return grabbed;
    }

    public final void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void openClaw()
    {
        rightClampServo.setPosition(0);
        sleep(450);
    }

    public void closeClaw()
    {
        rightClampServo.setPosition(1);
        sleep(450);
    }


}
