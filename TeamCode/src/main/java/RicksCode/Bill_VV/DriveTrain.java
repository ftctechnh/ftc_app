package RicksCode.Bill_VV;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

//import static RicksCode.Bill_VV.DriveTrain.Color.BLUE;
//import static RicksCode.Bill_VV.DriveTrain.Color.GREEN;
//import static RicksCode.Bill_VV.DriveTrain.Color.RED;
//import static RicksCode.Bill_VV.DriveTrain.Color.YELLOW;
//import static RicksCode.Bill_VV.DriveTrain.CornerColor.BLUEYELLOW;
//import static RicksCode.Bill_VV.DriveTrain.CornerColor.GREENBLUE;
//import static RicksCode.Bill_VV.DriveTrain.CornerColor.REDGREEN;
//import static RicksCode.Bill_VV.DriveTrain.CornerColor.YELLOWRED;
//import static RicksCode.Bill_VV.DriveTrain.Corner.LEFTFRONT;
//import static RicksCode.Bill_VV.DriveTrain.Corner.RIGHTFRONT;
//import static RicksCode.Bill_VV.DriveTrain.Corner.RIGHTREAR;
//import static RicksCode.Bill_VV.DriveTrain.Corner.LEFTREAR;


public class DriveTrain {

    //public enum Color {YELLOW, RED, GREEN, BLUE}
    //public enum CornerColor {BLUEYELLOW, YELLOWRED, REDGREEN, GREENBLUE}
    //public enum Corner {LEFTFRONT, RIGHTFRONT, RIGHTREAR, LEFTREAR}
    public enum SpeedSetting {FAST, SLOW}

    //private Color frontColor;
    private SpeedSetting speedMode;
    
   //public DcMotor[] motorByColor;
//    public DcMotor[] motorByCorner;

    //Iniatalize motors
    public DcMotor left_front  = null;
    public DcMotor right_front = null;
    public DcMotor right_rear  = null;
    public DcMotor left_rear   = null;

    /* Constructor */
    public DriveTrain() {
    //    motorByColor = new DcMotor[4];
 //       motorByCorner = new DcMotor[4];
//        motor_leftfront = new DcMotor;
//        motor_rightfront = new motor_rightfront;
//        motor_rightrear = new motor_rightrear;
//        motor_leftrear = new motor_leftrear;
    }

    public void init(HardwareMap hardwareMap) {
        left_front  = hardwareMap.get(DcMotor.class, "left_front" );
        right_front = hardwareMap.get(DcMotor.class, "right_front" );
        right_rear  = hardwareMap.get(DcMotor.class, "right_rear" );
        left_rear   = hardwareMap.get(DcMotor.class, "left_rear" );

//        motor_leftfront  = hardwareMap.dcMotor.get("left_front" );
//        motor_rightfront = hardwareMap.dcMotor.get("right_front");
//        motor_rightrear  = hardwareMap.dcMotor.get("right_rear" );
//        motor_leftrear   = hardwareMap.dcMotor.get("left_rear"  );
        // motorByColor[YELLOWRED.ordinal()] = hardwareMap.dcMotor.get("left_rear");
        //motorByColor[REDGREEN.ordinal()] = hardwareMap.dcMotor.get("right_front");
        //motorByColor[GREENBLUE.ordinal()] = hardwareMap.dcMotor.get("right_rear");

        left_front.setDirection(DcMotor.Direction.REVERSE);   // Set left to REVERSE if using AndyMark motors
        right_front.setDirection(DcMotor.Direction.FORWARD);  // Set right to FORWARD if using AndyMark motors
        right_rear.setDirection(DcMotor.Direction.FORWARD);
        left_rear.setDirection(DcMotor.Direction.REVERSE);

        // Set all motors to zero power
        left_front.setPower(0.0);
        right_front.setPower(0.0);
        right_rear.setPower(0.0);
        left_rear.setPower(0.0);

        runWithoutEncoders();

        speedMode = SpeedSetting.FAST;
    }

    public void runWithoutEncoders() {
//        for (int i=0; i<4; i++) {
//            motorByCorner[i].setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//        }
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

//        for (int i=0; i<4; i++) {
//            motorByCorner[i].setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        }
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
        left_front.setPower(Range.clip (speedMultiplier*(x + y + turn), -1, 1));
        right_rear.setPower(Range.clip (speedMultiplier*(-x - y + turn), -1, 1));

        right_front.setPower(Range.clip(speedMultiplier*(x - y + turn), -1, 1));
        left_rear.setPower(Range.clip  (speedMultiplier*(-x + y + turn), -1, 1));

//        motorByCorner[LEFTFRONT.ordinal()].setPower(Range.clip(speedMultiplier*(x + y + turn), -1, 1));
//        motorByCorner[RIGHTREAR.ordinal()].setPower(Range.clip(speedMultiplier*(-x - y + turn), -1, 1));
//
//        motorByCorner[RIGHTFRONT.ordinal()].setPower(Range.clip(speedMultiplier*(x - y + turn), -1, 1));
//        motorByCorner[LEFTREAR.ordinal()].setPower(Range.clip(speedMultiplier*(-x + y + turn), -1, 1));
//

        
       /*Original Code
            motorByDirection[FRONTLEFT.ordinal()].setPower(Range.clip(x + y + turn, -1, 1));
            motorByDirection[BACKRIGHT.ordinal()].setPower(Range.clip(-x - y + turn, -1, 1));

            motorByDirection[FRONTRIGHT.ordinal()].setPower(Range.clip(x - y + turn, -1, 1));
            motorByDirection[BACKLEFT.ordinal()].setPower(Range.clip(-x + y + turn, -1, 1));
       */
        
    }

    public void stop() {
        drive(0,0,0);
    }

//    //changes the front to the selected color and update motors
//    public void setFront(Color color) {
//        frontColor = color;
//        for (int i = 0; i < 4; i++)
//            motorByDirection[i] = motorByColor[(i + frontColor.ordinal()) % 4];
//    }
    
    //changes the speed of the robot
    public void setSpeedMode(SpeedSetting speed) {
        speedMode = speed;
    }

//    public void setBack(Color color) {
//        switch (color)
//        {
//            case YELLOW:
//                setFront(GREEN);
//                break;
//            case RED:
//                setFront(BLUE);
//                break;
//            case GREEN:
//                setFront(YELLOW);
//                break;
//            case BLUE:
//                setFront(RED);
//        }
//    }

//    public Color getFront() {
//        return frontColor;
//    }

//    public int getCurrentPositionByColor(CornerColor wheel )
//    {
//        return motorByColor[wheel.ordinal()].getCurrentPosition();
//    }

}
