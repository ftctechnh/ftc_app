package com.walnutHillsEagles.WalnutLibrary;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Gamepad;

/**
 * Created by Yan Vologzhanin on 1/2/2016.
 */
public abstract class WalnutMotor {
    //Field
    private DcMotor motor;
    //Used for telemetry
    private String name;
    private boolean hasEncoders;
    public static class GamepadUpdater implements Runnable{
        public static boolean canProcess = false;
        public static double[] doubleValues = new double[12];
        public static boolean[] boolValues = new boolean[30];
        public Gamepad gamepad1Pointer;
        public Gamepad gamepad2Pointer;

        /*The array is initilized as follows:
        ----------------------------------------------
        -Analog
        0 - Gamepad 1, X axis, left stick (LEFTX1)
        1 - Gamepad 1, Y axis, left stick (LEFTY1)
        2 - Gamepad 1, X axis, right stick (RIGHTX1)
        3 - Gamepad 1, Y axis, right stick (RIGHTY1)
        4 - Gamepad 1, Left Trigger (LEFTZ1)
        5 - Gamepad 1, Right Trigger (RIGHTZ1)
        6 - Gamepad 2, X axis, left stick (LEFTX2)
        7 - Gamepad 2, Y axis, left stick (LEFTY2)
        8 - Gamepad 2, X axis, right stick (RIGHTX2)
        9 - Gamepad 2, Y axis, right stick (RIGHTY2)
        10 - Gamepad 2, Left Trigger (LEFTZ2)
        11 - Gamepad 2, Right Trigger (RIGHTZ2)
        -Digital
        0 - Gamepad 1, a button (A1)
        1 - Gamepad 1, b button (B1)
        2- Gamepad 1, x button (X1)
        3- Gamepad 1, y button (Y1)
        4 - Gamepad 1, back button (BACK1)
        5 - Gamepad 1, start button (START1)
        6 - Gamepad 1, guid button (GUIDE1)
        7 - Gamepad 1, dpad left (LEFT1)
        8 - Gamepad 1, dpad right (RIGHT1)
        9 - Gamepad 1, dpad down (DOWN1)
        10 - Gamepad 1, dpad up (UP1)
        11 - Gamepad 1, Left bumper (LBUMP1)
        12 - Gamepad 1, right bumper (RBUMP1)
        13 - Gamepad 1, left stick button (LSTICK1)
        14 - Gamepad 1, right stick button (RSTICK1)
        15 - Gamepad 2, a button (A2)
        16 - Gamepad 2, b button (B2)
        17- Gamepad 2, x button (X2)
        18- Gamepad 2, y button (Y2)
        19 - Gamepad 2, back button (BACK2)
        20- Gamepad 2, start button (START2)
        21- Gamepad 2, guid button (GUIDE2)
        22- Gamepad 2, dpad left (LEFT2)
        23- Gamepad 2, dpad right (RIGHT2)
        24- Gamepad 2, dpad down (DOWN2)
        25 - Gamepad 2, dpad up (UP2)
        26 - Gamepad 2, Left bumper (LBUMP2)
        27 - Gamepad 2, right bumper (RBUMP2)
        28 - Gamepad 2, left stick button (LSTICK2)
        29 - Gamepad 2, right stick button (RSTICK2)
        ---------------------------------------------*/
        private void setGamepads(Gamepad newGamepad1, Gamepad newGamepad2){
            gamepad1Pointer=newGamepad1;
            gamepad2Pointer=newGamepad2;
        }
        //Method that starts at thread and updates neccesary parts
        public void run(){
            canProcess = true;
            //Will loop and update values until told to stop
            while(canProcess){
                //Analog Values
                doubleValues[0] = gamepad1Pointer.left_stick_x;
                doubleValues[1] = gamepad1Pointer.left_stick_y;
                doubleValues[2] = gamepad1Pointer.right_stick_x;
                doubleValues[3] = gamepad1Pointer.right_stick_y;
                doubleValues[4] = gamepad1Pointer.left_trigger;
                doubleValues[5] = gamepad1Pointer.right_trigger;

                doubleValues[6] = gamepad2Pointer.left_stick_x;
                doubleValues[7] = gamepad2Pointer.left_stick_y;
                doubleValues[8] = gamepad2Pointer.right_stick_x;
                doubleValues[9] = gamepad2Pointer.right_stick_y;
                doubleValues[10] = gamepad2Pointer.left_trigger;
                doubleValues[11] = gamepad2Pointer.right_trigger;
                //Digital Values
                boolValues[0] = gamepad1Pointer.a;
                boolValues[1] = gamepad1Pointer.b;
                boolValues[2] = gamepad1Pointer.x;
                boolValues[3] = gamepad1Pointer.y;
                boolValues[4] = gamepad1Pointer.back;
                boolValues[5] = gamepad1Pointer.start;
                boolValues[6] = gamepad1Pointer.guide;
                boolValues[7] = gamepad1Pointer.dpad_left;
                boolValues[8] = gamepad1Pointer.dpad_right;
                boolValues[9] = gamepad1Pointer.dpad_down;
                boolValues[10] = gamepad1Pointer.dpad_up;
                boolValues[11] = gamepad1Pointer.left_bumper;
                boolValues[12] = gamepad1Pointer.right_bumper;
                boolValues[13] = gamepad1Pointer.left_stick_button;
                boolValues[14] = gamepad1Pointer.right_stick_button;

                boolValues[15] = gamepad2Pointer.a;
                boolValues[16] = gamepad2Pointer.b;
                boolValues[17] = gamepad2Pointer.x;
                boolValues[18] = gamepad2Pointer.y;
                boolValues[19] = gamepad2Pointer.back;
                boolValues[20] = gamepad2Pointer.start;
                boolValues[21] = gamepad2Pointer.guide;
                boolValues[22] = gamepad2Pointer.dpad_left;
                boolValues[23] = gamepad2Pointer.dpad_right;
                boolValues[24] = gamepad2Pointer.dpad_down;
                boolValues[25] = gamepad2Pointer.dpad_up;
                boolValues[26] = gamepad2Pointer.left_bumper;
                boolValues[27] = gamepad2Pointer.right_bumper;
                boolValues[28] = gamepad2Pointer.left_stick_button;
                boolValues[29] = gamepad2Pointer.right_stick_button;
            }
        }
        //Called by elsewhere in the program to terminate processing
        public static void stopProcessing(){
            canProcess = false;
        }
        public static void startProcessing(OpMode myOpmode){
            GamepadUpdater updater = new WalnutMotor.GamepadUpdater();
            Thread processor = new Thread(updater);
            updater.setGamepads(myOpmode.gamepad1, myOpmode.gamepad2);
            processor.start();
        }
    }
    //Constructor
    WalnutMotor(DcMotor myMotor, String myName, boolean encoderCheck){
        motor = myMotor;
        name = myName;
        if(encoderCheck){
            hasEncoders = true;
            motor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
            motor.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        }
        else{
            hasEncoders = false;
            motor.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        }

    }
    //Getters and Setters
    public DcMotor getMotor(){
        return motor;
    }
    public boolean checkEncoders(){
        return hasEncoders;
    }
    public String toString(){
        return this.name;
    }
    public void setName(String myName){
        name = myName;
    }
    //Methods
    public void stop(){
        if(hasEncoders){
            motor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
            motor.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        }
        motor.setPower(0);
    }

    public void power(double pow){
        if(pow>=-1.0&&pow <=1.0)
            motor.setPower(pow);
        else
            System.err.println("Invalid Power Value");
    }
    public abstract void operate();
}
