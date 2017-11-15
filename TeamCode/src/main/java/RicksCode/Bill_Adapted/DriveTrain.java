package RicksCode.Bill_Adapted;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;



public class DriveTrain {


    public enum SpeedSetting {FAST, SLOW}
    private SpeedSetting speedMode;
    public final double WHEEL_DIAMETER = 4.0;
    public final double GEAR_RATIO = 1.0;
    public final  double TICKS_REV = 1120;
    public final double     COUNTS_PER_INCH = (TICKS_REV * GEAR_RATIO) / (WHEEL_DIAMETER * 3.1415);

    //Iniatalize motors
    public DcMotor left_front  = null;
    public DcMotor right_front = null;
    public DcMotor right_rear  = null;
    public DcMotor left_rear   = null;


    public void init(HardwareMap hardwareMap) {
        left_front  = hardwareMap.get(DcMotor.class, "leftfront" );
        right_front = hardwareMap.get(DcMotor.class, "rightfront" );
        right_rear  = hardwareMap.get(DcMotor.class, "rightrear" );
        left_rear   = hardwareMap.get(DcMotor.class, "leftrear" );


//      Neverest Motors
//        left_front.setDirection(DcMotor.Direction.FORWARD);
//        left_rear.setDirection(DcMotor.Direction.FORWARD);
//        right_front.setDirection(DcMotor.Direction.REVERSE);
//        right_rear.setDirection(DcMotor.Direction.REVERSE);
//      Tetrix Motors
        left_front.setDirection(DcMotor.Direction.REVERSE);
        left_rear.setDirection(DcMotor.Direction.REVERSE);
        right_front.setDirection(DcMotor.Direction.FORWARD);
        right_rear.setDirection(DcMotor.Direction.FORWARD);

        // Set all motors to zero power
        left_front.setPower(0.0);
        right_front.setPower(0.0);
        right_rear.setPower(0.0);
        left_rear.setPower(0.0);

        runWithoutEncoders();

        speedMode = SpeedSetting.FAST;
    }

    public void runWithoutEncoders() {
        left_front.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        right_front.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        right_rear.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        left_rear.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void runUsingEncoders() {
        left_front.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        right_front.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        right_rear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        left_rear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void drive(double x, double y, double turn) {
        //speed change code
        double speedMultiplier = 1.0;
        switch (speedMode)
        {
            // lookup parameter for "fast mode"
            case FAST:
                 speedMultiplier = 1.0;
                break;
            case SLOW:
                // lookup slow speed parameter
                speedMultiplier = 0.3;
                break;
            default:
                speedMultiplier = 1.0;
        }


        //DRIVE CODE APPLYING MOTOR POWERS
        left_front.setPower(Range.clip (speedMultiplier*( y + x + turn), -1, 1));
        left_rear.setPower(Range.clip  (speedMultiplier*( y - x + turn), -1, 1));

        right_front.setPower(Range.clip(speedMultiplier*( y - x + turn), -1, 1));
        right_rear.setPower(Range.clip (speedMultiplier*( y + x + turn), -1, 1));

    }

    public void stop() {
        left_front.setPower(0.0);
        right_front.setPower(0.0);
        right_rear.setPower(0.0);
        left_rear.setPower(0.0);
    }
    
    //changes the speed of the robot
    public void setSpeedMode(SpeedSetting speed) {
        speedMode = speed;
    }
}
