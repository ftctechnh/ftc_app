package org.firstinspires.ftc.teamcode.Qualifier;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import static com.sun.tools.doclint.Entity.pi;


public class GlyphTrain {


//    public enum SpeedSetting {FAST, SLOW}

//    private SpeedSetting speedMode;
public double pulleydiameter = 1;
    public final double TICKS_REV = 1120;

    //Iniatalize motors
    public DcMotor left_glyph  = null;
    public DcMotor right_glyph = null;
    public DcMotor lift_motor  = null;
    public Servo   leftlower   = null;
    public Servo   rightlower  = null;


    public void init(HardwareMap hardwareMap) {
        left_glyph  = hardwareMap.get(DcMotor.class, "left_glyph");
        right_glyph = hardwareMap.get(DcMotor.class, "right_glyph");

        lift_motor = hardwareMap.get(DcMotor.class, "lift_motor");

        rightlower = hardwareMap.get(Servo.class, "right_lower");
        leftlower  = hardwareMap.get(Servo.class, "left_lower");


//      Neverest Motors
        left_glyph.setDirection(DcMotor.Direction.REVERSE);
        right_glyph.setDirection(DcMotor.Direction.FORWARD);

        // not sure if run using encoders is good here?
        left_glyph.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        right_glyph.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Set all motors to zero power
        stopGlyphMotors();


    }



    public void startGlyphMotors(double glyphpower) {
        //speed change code
//        double drive_direction = atan(toRadians(y/x));
//        double speedMultiplier;
//        switch (speedMode) {
//            // lookup parameter for "fast mode"
//            case FAST:
//                speedMultiplier = 1.0;
//                break;
//            case SLOW:
//                // lookup slow speed parameter
//                speedMultiplier = 0.3;
//                break;
//            default:
//                speedMultiplier = 1.0;
//        }


            left_glyph.setPower(glyphpower);
            right_glyph.setPower(glyphpower-.1);
    }

    public void stopGlyphMotors() {
        left_glyph.setPower(0.0);
        right_glyph.setPower(0.0);
    }
    void glyphclamp(String moveto){
        if( moveto == "open"){
            leftlower.setPosition(0.50);
            rightlower.setPosition(0.50); //glyph open
        }
        else if(moveto == "close"){
            leftlower.setPosition(0.27);
            rightlower.setPosition(0.65); //glyph open
        }
    }

    void liftGlyph(float height){
        // clamp glyph
        //glyphclamp("close");

        //height is encoder counts or inches, absolute or relative?
        // get the current encoder counts
        int startposition = lift_motor.getCurrentPosition();

        while(lift_motor.getCurrentPosition() < startposition + (height/(Math.PI*pulleydiameter))*TICKS_REV ){
            lift_motor.setPower(0.7);
        }

        lift_motor.setPower(0.0);

    }



}
