package org.firstinspires.ftc.teamcode.libraries.hardware;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Made by Scott 3.0 10/4/2020
 *  hardware = yes.
 */
public class BotHardware {
    //def not stealing Noah's idea of enums
    public enum Motor {
        frontRight("fr", false),
        backRight("br", false),
        frontLeft("fl", true),
        backLeft("bl", true);

        private final String name;
        private final boolean reverse;
        public DcMotorEx motor;

        Motor(String name, boolean reverse) {
            this.name = name;
            this.reverse = reverse;
        }

        void initMotor(OpMode mode) {
            try {
                this.motor = mode.hardwareMap.get(DcMotorEx.class, this.name);
                this.motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                this.motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                if (this.reverse)
                    this.motor.setDirection(DcMotorSimple.Direction.REVERSE);
            }
            catch (Exception e) {
                mode.telemetry.addData(this.name, "Failed to find motor");
            }
        }
    } //motor enum close brack
     /*public enum ServoE {
        //insert servos here
        //insert servo limit vals here

        private final String name;
        public Servo servo;
        private boolean reversed;

        ServoE(String name, boolean reversed){
            this.reversed = reversed;
            this.name = name;
        }

        ServoE(String name){ //shortcut so I don't have to type false every frickin time
            this(name, false);
        }

        void initServo(OpMode mode){
            try{
                this.servo = mode.hardwareMap.get(Servo.class, this.name);
            }
            catch(Exception e){
                mode.telemetry.addData(this.name, "Failed to find servo")
            }
        }
    } //servo enum close brack */
    //opmode pointer
    private final OpMode mode;

    public BotHardware(OpMode mode){
        this.mode = mode;
    }
    public void init(){
    //motor init
        for(int i = 0; i < Motor.values().length; i++){
            Motor.values()[i].initMotor(this.mode);
        }
    }

    public void start(){

    }

    public void setFrDrive(double power) { Motor.frontRight.motor.setPower(power); }

    public void setBrDrive(double power) {
        Motor.backRight.motor.setPower(power);
    }

    public void setFlDrive(double power) {
        Motor.frontLeft.motor.setPower(power);
    }

    public void setBlDrive(double power) {
        Motor.backLeft.motor.setPower(power);
    }
    //ORDER IS IMPORTANT
    //FR, BR, FL, BL
    //DON'T FUCK IT UP
    public void setAllDrive(double FRpower, double BRpower, double FLpower, double BLpower){
        Motor.frontRight.motor.setPower(FRpower);
        Motor.backRight.motor.setPower(BRpower);
        Motor.frontLeft.motor.setPower(FLpower);
        Motor.backLeft.motor.setPower(BLpower);
    }
    //init IMU here somewhere
    public void stopAll() {
        for(Motor motor : Motor.values()) motor.motor.setPower(0);
    }

    public DcMotorEx getMotor(String name) {
        return Motor.valueOf(name).motor;
    }
}
