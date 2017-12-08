package teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import static java.lang.Math.sqrt;

public class KiwiRobot {
    public DcMotor  Motor1 = null; // Back wheel
    public DcMotor  Motor2 = null; // Left front wheel
    public DcMotor  Motor3 = null; // Right front wheel
    
    public KiwiRobot() {
        this.Init();
    }
    
    private void Init() {
        this.Motor1 = hardwareMap.get(DcMotor.class, "motor1");
        this.Motor2 = hardwareMap.get(DcMotor.class, "motor2");
        this.Motor3 = hardwareMap.get(DcMotor.class, "motor3");
        
        this.Motor1.setDirection(DcMotor.Direction.FORWARD);
        this.Motor2.setDirection(DcMotor.Direction.FORWARD);
        this.Motor3.setDirection(DcMotor.Direction.FORWARD);
    }
}
