import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class MyBot {
    
    public MyBot() {    }
    
    // defining motors 
    public DcMotor leftMotor;
    public DcMotor rightMotor;
    public Servo = arm;
    
    // default arm position
    public final static double ARM_HOME = 0.2;
    public final static double ARM_MAX_RANGE = 0.2;
    public final static double ARM_MIN_RANGE = 0.9;
    
    private ElapsedTime period  = new ElapsedTime();
    
    // assignment of the motors
    
    public void assign(HardwareMap hMap) {
        
        leftMotor = hMap.dcMotor.get("left");
        rightMotor = hMap.dcMotor.get("right");
        leftMotor.setDirection(DcMotor.Direction.REVERSE);
        arm = hMap.dcMotor.get("arm");
        
        leftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        
        leftMotor.setPower(0);
        rightMotor.setPower(0);
        arm.setPower(ARM_HOME);
        
    }
    
    public void waitForTick(long periodMs) {

        long  remaining = periodMs - (long)period.milliseconds();

        // sleep for the remaining portion of the regular cycle period.
        if (remaining > 0) {
            try {
                Thread.sleep(remaining);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Reset the cycle clock for the next pass.
        period.reset();
    }
}