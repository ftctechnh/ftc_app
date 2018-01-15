package org.firstinspires.ftc.teamcode.Qualifier;

import android.app.ListFragment;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import static com.sun.tools.doclint.Entity.pi;


public class GlyphTrain {


//    public enum SpeedSetting {FAST, SLOW}

//    private SpeedSetting speedMode;
    public double pulleydiameter = 2.7;
    public final double TICKS_REV = 1120;
    public int lowerLiftLimit = 0;
    public int upperLiftLimit = (int)( (15/(Math.PI*pulleydiameter)) * TICKS_REV );   // was 4700
    public int liftIndex = 0;
//    public int[] liftPosition ={0,2100,4100};
    public int[] liftPosition ={0,(int)( (7/(Math.PI*pulleydiameter)) * TICKS_REV ),(int)( (14/(Math.PI*pulleydiameter)) * TICKS_REV )};
//     0,(int) (7 * TICKS_REV / (pulleydiameter*Math.PI)), (int)( 13 * TICKS_REV / (pulleydiameter*Math.PI) ) };


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
        // reset encoder to zero for lift (assume you have it down)
        lift_motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lift_motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lift_motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
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
        double clampRange = 0.4;
        if( moveto == "open"){
            leftlower.setPosition(0.45);
            rightlower.setPosition(0.50); //glyph open
        }
        else if(moveto == "close"){
            leftlower.setPosition(0.45 - clampRange);
            rightlower.setPosition(0.50 + clampRange);
        }
    }

    void liftGlyph(float height){
        // clamp glyph
        //glyphclamp("close");

        //height is encoder counts or inches, absolute or relative?
        // get the current encoder counts
        int startposition = lift_motor.getCurrentPosition();

        while(lift_motor.getCurrentPosition() < startposition + (height/(Math.PI*pulleydiameter))*TICKS_REV  &&
              lift_motor.getCurrentPosition() < upperLiftLimit  ){
            lift_motor.setPower(0.9);
        }

        lift_motor.setPower(0.0);

    }
    void liftGlyphIndex(int newindex){

        //height is encoder counts or ticks
        // going up
        if (Math.abs(lift_motor.getCurrentPosition()-liftPosition[newindex]) > 2200){
            if(liftPosition[newindex] > lift_motor.getCurrentPosition()){
                newindex = newindex -1;
            }else{
                newindex = newindex +1;
            }

        }
        if(liftPosition[newindex] > lift_motor.getCurrentPosition()){
            while(lift_motor.getCurrentPosition() < liftPosition[newindex] &&
                    lift_motor.getCurrentPosition() < upperLiftLimit  ){
                lift_motor.setPower(0.9);
            }
        } else{                           // going down
            while(lift_motor.getCurrentPosition() > liftPosition[newindex]  &&
                    lift_motor.getCurrentPosition() >lowerLiftLimit  ){
                lift_motor.setPower(-0.7);
            }

        }


        lift_motor.setPower(0.0);

    }

    void lowerGlyph(float height){
        // clamp glyph
        //glyphclamp("close");

        //height is encoder counts or inches, absolute or relative?
        // get the current encoder counts
        int startposition = lift_motor.getCurrentPosition();

        while(lift_motor.getCurrentPosition() > startposition - (height/(Math.PI*pulleydiameter))*TICKS_REV &&
                lift_motor.getCurrentPosition() > lowerLiftLimit  ){
            lift_motor.setPower(-0.7);
        }

        lift_motor.setPower(0.0);

    }


}
