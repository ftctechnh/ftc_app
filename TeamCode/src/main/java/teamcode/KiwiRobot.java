package teamcode;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import static java.lang.Math.sqrt;

public class KiwiRobot {
    private double SQRT_3 = sqrt(3);
    private HardwareMap hardwareMap;

    public DcMotor  Motor1 = null; // Back wheel
    public DcMotor  Motor2 = null; // Left front wheel
    public DcMotor  Motor3 = null; // Right front wheel
    public Servo armServo = null;
    public Servo rightClampServo;
    public Servo jewelServo;
    public ColorSensor sensorColor;
    public DistanceSensor sensorDistance;
    
    public KiwiRobot(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
        this.init();
    }
    
    
    public void driveSpeed(double x, double y, double r) {
        double power1 =  x - r;
        double power2 = (((-.5) * x) - (SQRT_3/2) * y) - r;
        double power3 = (((-.5) * x) + (SQRT_3/2) * y) - r;

        this.Motor1.setPower(power1);
        this.Motor2.setPower(power2);
        this.Motor3.setPower(power3);
    }
    
    public void driveForward(double power, double distanceInches, boolean resetEncoders) {
        if (resetEncoders) {
            this.resetEncoders();
        }

        double f2 = 850/12;
        double f3 = 860/12;

        int y2_start = this.Motor2.getCurrentPosition();
        int y3_start = this.Motor3.getCurrentPosition();
        
        int y2 = (int)(f2 * -2 * distanceInches / SQRT_3) + y2_start;
        int y3 = (int)(f3 * 2 * distanceInches / SQRT_3) + y3_start;
        
        this.Motor2.setTargetPosition(y2);
        this.Motor3.setTargetPosition(y3);
        
        this.Motor2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        this.Motor3.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                
        this.Motor1.setPower(0);
        this.Motor2.setPower(power);
        this.Motor3.setPower(power);
        
        while (this.Motor2.isBusy() && this.Motor3.isBusy()) {
        }
        
        this.turnOffMotors();
        this.setRunWithEncoders();
    }
    
    public void turnDegrees(double power, double degrees) {
        this.resetEncoders();
        double f1 = 3681 / 360;
        double f2 = 3866 / 360;
        double f3 = 3832 / 360;
        int p1 = (int)(f1 * degrees);
        int p2 = (int)(f2 * degrees);
        int p3 = (int)(f3 * degrees);
        this.Motor1.setTargetPosition(p1);
        this.Motor2.setTargetPosition(p2);
        this.Motor3.setTargetPosition(p3);
        
        this.Motor1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        this.Motor2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        this.Motor3.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                
        this.Motor1.setPower(power);
        this.Motor2.setPower(power);
        this.Motor3.setPower(power);
        
        while (this.Motor1.isBusy() && this.Motor2.isBusy() && this.Motor3.isBusy()) {
        }
        
        this.turnOffMotors();
        this.setRunWithEncoders();
    }
    
    public void resetEncoders() {
        this.Motor1.setMode(DcMotor.RunMode.RESET_ENCODERS);
        this.Motor2.setMode(DcMotor.RunMode.RESET_ENCODERS);
        this.Motor3.setMode(DcMotor.RunMode.RESET_ENCODERS);
    }
    
    public void turnOffMotors() {
        this.Motor1.setPower(0);
        this.Motor2.setPower(0);
        this.Motor3.setPower(0);
    }
    
    private void setRunWithEncoders() {
        this.Motor1.setMode(DcMotor.RunMode.RUN_USING_ENCODERS);
        this.Motor2.setMode(DcMotor.RunMode.RUN_USING_ENCODERS);
        this.Motor3.setMode(DcMotor.RunMode.RUN_USING_ENCODERS);
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
        if(red >= 1.5 * blue)
        {
            return true;
        }
        return false;
    }

    private void init() {
        this.Motor1 = this.hardwareMap.get(DcMotor.class, "motor1");
        this.Motor2 = this.hardwareMap.get(DcMotor.class, "motor2");
        this.Motor3 = this.hardwareMap.get(DcMotor.class, "motor3");
        this.armServo = this.hardwareMap.get(Servo.class,"armServo");
        this.jewelServo = this.hardwareMap.get(Servo.class,"jewelServo");
        this.rightClampServo = this.hardwareMap.get(Servo.class,"rightClampServo");
        this.sensorColor = hardwareMap.get(ColorSensor.class, "colorDistanceSensor");
        this.sensorDistance = hardwareMap.get(DistanceSensor.class, "colorDistanceSensor");



        this.Motor1.setDirection(DcMotor.Direction.FORWARD);
        this.Motor2.setDirection(DcMotor.Direction.FORWARD);
        this.Motor3.setDirection(DcMotor.Direction.FORWARD);
        this.armServo.setDirection(Servo.Direction.FORWARD);
        this.rightClampServo.setDirection(Servo.Direction.REVERSE);
        this.jewelServo.setDirection(Servo.Direction.FORWARD);
    }
}
