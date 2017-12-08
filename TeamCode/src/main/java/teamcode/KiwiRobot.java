package teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import static java.lang.Math.sqrt;

public class KiwiRobot {
    private double SQRT_3 = sqrt(3);
    
    public DcMotor  Motor1 = null; // Back wheel
    public DcMotor  Motor2 = null; // Left front wheel
    public DcMotor  Motor3 = null; // Right front wheel
    
    public KiwiRobot() {
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
    
    public void driveForward(double power, double distance, boolean resetEncoders = false) {
        if (resetEncoders) {
            this.resetEncoders()
        }
        
        double y2 = -2 * power / SQRT_3;
        double y3 = 2 * power / SQRT_3;
        
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
        double f1 = 3681 / 360;
        double f2 = 3866 / 360;
        double f3 = 3832 / 360;
        double p1 = f1 * degrees;
        double p2 = f2 * degrees;
        double p3 = f3 * degrees;
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
    
    private void init() {
        this.Motor1 = hardwareMap.get(DcMotor.class, "motor1");
        this.Motor2 = hardwareMap.get(DcMotor.class, "motor2");
        this.Motor3 = hardwareMap.get(DcMotor.class, "motor3");
        
        this.Motor1 = hardwareMap.get(DcMotor.class, "motor1");
        this.Motor2 = hardwareMap.get(DcMotor.class, "motor2");
        this.Motor3 = hardwareMap.get(DcMotor.class, "motor3");
        
        this.Motor1.setDirection(DcMotor.Direction.FORWARD);
        this.Motor2.setDirection(DcMotor.Direction.FORWARD);
        this.Motor3.setDirection(DcMotor.Direction.FORWARD);
    }
}
