package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.hardware.DcMotorController;

/**
 * Created by Lapeer FTC Robotics (#7138 & #8935) on 9/2/2015.
 * Use this class to interface with HiTechnic Motor Controllers to properly read encoders using the RUN_USING_ENCODERS run mode.
 */
public class HiTechnicMotorController {
    private boolean debug = false;
    private boolean motor1EncoderActive = false;
    private boolean motor2EncoderActive = false;
    private boolean resetMotor1Encoder = false;
    private boolean resetMotor2Encoder = false;
    private int motor1Encoder = 0;
    private int motor2Encoder = 0;
    private double motor1Power = 0.0;
    private double motor2Power = 0.0;
    /* private */ int state = 0;
    private int stateCnt = 0;
    private int nextState = 0;
    private DcMotorController motorController = null;

    /**
     * Constructor - create this class with a reference to a HiTechnic Motor Controller.
     * @param motorController
     */
    public HiTechnicMotorController(DcMotorController motorController) {
        this.motorController = motorController;
    }

    /**
     * Call the process() method from the OpMode loop() method.
     */
    public void process() {

        nextState = state;
        switch(state){
            case 0: // reset the encoders
                if (debug)
                    DbgLog.msg("Case 0 - set reset encoder");
                if (motor1EncoderActive)
                {
                    motorController.setMotorChannelMode(1,DcMotorController.RunMode.RESET_ENCODERS);
                    this.resetMotor1Encoder = false;
                }
                if (motor2EncoderActive) {
                    motorController.setMotorChannelMode(2, DcMotorController.RunMode.RESET_ENCODERS);
                    this.resetMotor1Encoder = false;
                }
                motorController.setMotorControllerDeviceMode(DcMotorController.DeviceMode.READ_ONLY);
                nextState = 1;
                break;
            case 1: // check for reset
                if (debug)
                    DbgLog.msg("Case 1a - device mode: "+motorController.getMotorControllerDeviceMode ());
                if (motorController.getMotorControllerDeviceMode() == DcMotorController.DeviceMode.READ_ONLY) {

                    boolean motor1EncoderReset = false;
                    boolean motor2EncoderReset = false;

                    if (motor1EncoderActive) {
                        if (debug)
                            DbgLog.msg("Case 1b - check motor1 runmode: "+motorController.getMotorChannelMode(1).toString( )+" = RESET_ENCODERS");
                        if (motorController.getMotorChannelMode(1) == DcMotorController.RunMode.RESET_ENCODERS)
                            motor1EncoderReset = true;
                    }
                    else {
                        motor1EncoderReset = true;
                    }

                    if (motor2EncoderActive) {
                        if (debug)
                            DbgLog.msg("Case 1b - check motor2 runmode: "+motorController.getMotorChannelMode(2).toString( )+" = RESET_ENCODERS");
                        if (motorController.getMotorChannelMode(2) == DcMotorController.RunMode.RESET_ENCODERS)
                            motor2EncoderReset = true;
                    }
                    else {
                        motor2EncoderReset = true;
                    }

                    if (motor1EncoderReset && motor2EncoderReset) {
                        motorController.setMotorControllerDeviceMode(DcMotorController.DeviceMode.WRITE_ONLY);
                        nextState = 2;
                    }
                }
                break;
            case 2: // switch to RUN_USING_ENCODERS run mode
                if (debug)
                    DbgLog.msg("Case 2a - device mode: "+motorController.getMotorControllerDeviceMode ());
                if (motorController.getMotorControllerDeviceMode() == DcMotorController.DeviceMode.WRITE_ONLY) {

                    if (motor1EncoderActive) {
                        if (debug)
                            DbgLog.msg("Case 2b - set motor 1 run using encoder");
                        motorController.setMotorChannelMode(1,DcMotorController.RunMode.RUN_USING_ENCODERS);
                    }

                    if (motor2EncoderActive) {
                        if (debug)
                            DbgLog.msg("Case 2b - set motor 2 run using encoder");
                        motorController.setMotorChannelMode(2,DcMotorController.RunMode.RUN_USING_ENCODERS);
                    }

                    motorController.setMotorControllerDeviceMode(DcMotorController.DeviceMode.READ_ONLY);
                    nextState = 3;
                }
                break;
            case 3: // check run mode
                if (debug)
                    DbgLog.msg("Case 3a - device mode: "+motorController.getMotorControllerDeviceMode ());
                if (motorController.getMotorControllerDeviceMode() == DcMotorController.DeviceMode.READ_ONLY) {

                    boolean motor1RunUsingEncoders = false;
                    boolean motor2RunUsingEncoders = false;

                    if (motor1EncoderActive) {
                        if (debug)
                            DbgLog.msg("Case 3b - check motor1 runmode: "+motorController.getMotorChannelMode(1).toString( )+" = RUN_USING_ENCODERS");
                        if (motorController.getMotorChannelMode(1) == DcMotorController.RunMode.RUN_USING_ENCODERS)
                            motor1RunUsingEncoders = true;
                    }
                    else {
                        motor1RunUsingEncoders = true;
                    }

                    if (motor2EncoderActive) {
                        if (debug)
                            DbgLog.msg("Case 3b - check motor2 runmode: "+motorController.getMotorChannelMode(2).toString( )+" = RUN_USING_ENCODERS");
                        if (motorController.getMotorChannelMode(2) == DcMotorController.RunMode.RUN_USING_ENCODERS)
                            motor2RunUsingEncoders = true;
                    }
                    else {
                        motor2RunUsingEncoders = true;
                    }

                    if (motor1RunUsingEncoders && motor2RunUsingEncoders) {
                        motorController.setMotorControllerDeviceMode(DcMotorController.DeviceMode.WRITE_ONLY);
                        nextState = 4;
                    }
                }
                break;
            case 4: // write motor power
                if (debug)
                    DbgLog.msg("Case 4a - device mode: "+motorController.getMotorControllerDeviceMode ());
                if (motorController.getMotorControllerDeviceMode() == DcMotorController.DeviceMode.WRITE_ONLY) {

                    if (debug) {
                        DbgLog.msg("Case 4b - set motor 1 power: "+motor1Power);
                        DbgLog.msg("Case 4b - set motor 2 power: " +motor2Power);
                    }

                    motorController.setMotorPower(1,motor1Power);
                    motorController.setMotorPower(2,motor2Power);

                    motorController.setMotorControllerDeviceMode(DcMotorController.DeviceMode.READ_ONLY);
                    nextState = 5;
                }
                break;
            case 5: // read the encoder position
                if (debug)
                    DbgLog.msg("Case 5a - device mode: "+motorController.getMotorControllerDeviceMode ());
                if (motorController.getMotorControllerDeviceMode() == DcMotorController.DeviceMode.READ_ONLY) {

                    boolean motor1EncoderRead = false;
                    boolean motor2EncoderRead = false;

                    if (motor1EncoderActive) {
                        int pos = motorController.getMotorCurrentPosition(1);
                        if (debug)
                            DbgLog.msg("Case 5b - motor1 pos: "+pos);
                        if (pos != 0) {
                            motor1Encoder = pos;
                            motor1EncoderRead = true;
                        }
                    }
                    else {
                        motor1EncoderRead = true;
                    }

                    if (motor2EncoderActive) {
                        int pos = motorController.getMotorCurrentPosition(2);
                        if (debug)
                            DbgLog.msg("Case 5b - motor2 pos: "+pos);
                        if (pos != 0) {
                            motor2Encoder = pos;
                            motor2EncoderRead = true;
                        }
                    }
                    else {
                        motor2EncoderRead = true;
                    }

                    if ((motor1EncoderRead && motor2EncoderRead) || (stateCnt > 10)) {
                        motorController.setMotorControllerDeviceMode(DcMotorController.DeviceMode.WRITE_ONLY);
                        nextState = 4;
                    }
                }
                break;
        }

        if(nextState != state){
            state = nextState;
            stateCnt = 0;
        }
        else {
            stateCnt++;
        }



    }

    /**
     * Set debug = true to log debug statements.
     * @param debug
     */
    public void setDebugLog(boolean debug) {
        this.debug = debug;
    }

    /**
     * Call on initialization if encoder is attached, prior to calling process() in loop() method.
     */
    public void resetMotor1Encoder() {
        this.resetMotor1Encoder = true;
        this.motor1EncoderActive = true;
    }

    /**
     * Call on initialization if encoder is attached, prior to calling process() in loop() method.
     */
    public void resetMotor2Encoder() {
        this.resetMotor2Encoder = true;
        this.motor2EncoderActive = true;
    }

    /**
     * Set motor 1 power
     * @param pwr
     */
    public void setMotor1Power(double pwr) {
        this.motor1Power = pwr;
    }

    /**
     * Set motor 2 power
     * @param pwr
     */
    public void setMotor2Power(double pwr) {
        this.motor2Power = pwr;
    }

    /**
     * Get motor 1 encoder value
     * @return
     */
    public int getMotor1Encoder() {
        return this.motor1Encoder;
    }

    /**
     * Get motor 2 encoder value
     * @return
     */
    public int getMotor2Encoder() {
        return this.motor2Encoder;
    }

    /**
     * Get motor 1 power
     * @return
     */
    public double getMotor1Power() {
        return this.motor1Power;
    }

    /**
     * Get motor 2 power
     * @return
     */
    public double getMotor2Power() {
        return this.motor2Power;
    }

}
