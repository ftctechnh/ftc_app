package com.qualcomm.ftcrobotcontroller.opmodes;

public class Autobots_Simple extends PushBotTelemetrySensors {

    private int v_state = 0;

    public Autobots_Simple() {}

    @Override public void start()
    {
        super.start();
        reset_drive_encoders();
    }

    @Override public void loop()
    {
        switch(v_state)
        {
            case 0:
                if (have_drive_encoders_reset())
                {
                    drive_using_encoders(-1.0f, 1.0f, -2880, 2880);
                    v_state++;
                }
                break;
            case 1:
                if (a_ods_white_tape_detected())
                {
                    telemetry.addData("Tape Detected", "Stopping");
                    set_drive_power(0.0, 0.0);
                    v_state++;
                }
                else
                {
                    set_drive_power(1.0, 1.0);
                }
                break;
            default:
                break;
        }
        update_telemetry();
        telemetry.addData("11", "Drive State: " + v_state);
    }
}


/*

This is a stupid example of an autonomous so don't use it!

-Love Zach

package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

public class Autobots_Simple extends OpMode {

    DcMotor motorLeft;
    DcMotor motorRight;

    public void hardwareMap() {
        motorLeft = hardwareMap.dcMotor.get("motorLeft");
        motorRight = hardwareMap.dcMotor.get("motorRight");
        motorRight.setDirection(DcMotor.Direction.REVERSE);
    }

    public void drive(int x) {
        motorLeft.setPower(x);
        motorRight.setPower(x);
        motorLeft.setPower(x);
        motorRight.setPower(x);
        motorLeft.setPower(x);
        motorRight.setPower(x);
        motorLeft.setPower(x);
        motorRight.setPower(x);
        motorLeft.setPower(x);
        motorRight.setPower(x);
        motorLeft.setPower(x);
        motorRight.setPower(x);
        motorLeft.setPower(x);
        motorRight.setPower(x);
        motorLeft.setPower(x);
        motorRight.setPower(x);
        motorLeft.setPower(x);
        motorRight.setPower(x);
        motorLeft.setPower(x);
        motorRight.setPower(x);
        motorLeft.setPower(x);
        motorRight.setPower(x);
        motorLeft.setPower(x);
        motorRight.setPower(x);
        motorLeft.setPower(x);
        motorRight.setPower(x);
    }

    public void init() {
        hardwareMap();
        drive(1);
        drive(1);
        drive(1);
        drive(1);
        drive(1);
        drive(1);
        drive(0);
    }

    public void YOLO() {
        System.out.println("YOLO");
    }

    public void loop() {
        YOLO();
    }
}
*/

/*package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

public class Autobots_Simple extends OpMode {

    DcMotor motorRight1;
    DcMotor motorRight2;
    DcMotor motorLeft1;
    DcMotor motorLeft2;
    int count = 0;

    public void init() {

        motorRight1 = hardwareMap.dcMotor.get("motor_1_right");
        motorRight2 = hardwareMap.dcMotor.get("motor_2_right");
        motorLeft1 = hardwareMap.dcMotor.get("motor_1_left");
        motorLeft2 = hardwareMap.dcMotor.get("motor_2_left");
    }

    public void GoBot() {
        motorRight1.setPower(1);
        motorRight2.setPower(1);
        motorLeft1.setPower(1);
        motorLeft2.setPower(1);
    }

    public void StopBot() {
        motorRight1.setPower(0);
        motorRight2.setPower(0);
        motorLeft1.setPower(0);
        motorLeft2.setPower(0);
    }

    public void Autobots_Simple() {

        for (; count < 10; ++count) {
            GoBot();
        }
         if (count >= 9) {
             StopBot();
         }
    }

    public void loop(){
        //      DO NOTHING              :)
    }

}

*/














/*package com.qualcomm.ftcrobotcontroller.opmodes;

import android.os.SystemClock;

import com.qualcomm.robotcore.hardware.DcMotor;

public class Autobots_Simple extends PushBotTelemetry {

    private int v_state = 0;

    DcMotor motorRight1;
    DcMotor motorRight2;
    DcMotor motorLeft1;
    DcMotor motorLeft2;

    public Autobots_Simple() {}

    public void init() {

        motorRight1 = hardwareMap.dcMotor.get("motor_1_right");
        motorRight2 = hardwareMap.dcMotor.get("motor_2_right");

        motorLeft1 = hardwareMap.dcMotor.get("motor_1_left");
        motorLeft2 = hardwareMap.dcMotor.get("motor_2_left");


    }
/*        @Override
        public void start() {

            super.start();


            reset_drive_encoders();



        } */


    //public void loopForward() {
        //    motorLeft1.setPower(1);
          //  motorLeft2.setPower(1);
            //motorRight1.setPower(1);
         //   motorRight2.setPower(1);
       // }

       // public void loopStop() {
        //    motorLeft1.setPower(0);
    //        motorLeft2.setPower(0);
      //      motorRight1.setPower(0);
        //    motorRight2.setPower(0);
    //    }
/*
        @Override
        public void loop()

        {


            motorLeft1.setPower(0);
            motorLeft2.setPower(0);
            motorRight1.setPower(0);
            motorRight2.setPower(0);

            SystemClock.sleep(2);

            motorLeft1.setPower(1);
            motorLeft2.setPower(1);
            motorRight1.setPower(1);
            motorRight2.setPower(1);

*/
/*
            switch (v_state) {

                case 0:

                    reset_drive_encoders();

                    v_state++;

                    break;

                case 1:

                    run_using_encoders();

                    set_drive_power(1.0f, 1.0f);

                    motorRight1.setPower(1.0f);
                    motorRight2.setPower(1.0f);
                    motorLeft1.setPower(1.0f);
                    motorLeft2.setPower(1.0f);

                    if (have_drive_encoders_reached(-2880, 2880)) {

                        reset_drive_encoders();

                        set_drive_power(0.0f, 0.0f);

                        motorRight1.setPower(0.0f);
                        motorRight2.setPower(0.0f);
                        motorLeft1.setPower(0.0f);
                        motorLeft2.setPower(0.0f);

                        v_state++;
                    }
                    break;

                case 2:
                    if (have_drive_encoders_reset()) {
                        v_state++;
                    }
                    break;

                case 3:
                    run_using_encoders();

                    set_drive_power(1.0f, -1.0f);

                    motorRight1.setPower(-1.0f);
                    motorRight2.setPower(-1.0f);
                    motorLeft1.setPower(1.0f);
                    motorLeft2.setPower(1.0f);

                    if (have_drive_encoders_reached(-2880, 2880)) {

                        reset_drive_encoders();

                        set_drive_power(0.0f, 0.0f);

                        motorRight1.setPower(0.0f);
                        motorRight2.setPower(0.0f);
                        motorLeft1.setPower(0.0f);
                        motorLeft2.setPower(0.0f);

                        v_state++;
                    }
                    break;

                case 4:
                    if (have_drive_encoders_reset()) {

                        v_state++;
                    }
                    break;

                case 5:
                    run_using_encoders();

                    set_drive_power(-1.0f, 1.0f);

                    motorRight1.setPower(1.0f);
                    motorRight2.setPower(1.0f);
                    motorLeft1.setPower(1.0f);
                    motorLeft2.setPower(1.0f);



                    if (have_drive_encoders_reached(-2880, 2880)) {

                        reset_drive_encoders();

                        set_drive_power(0.0f, 0.0f);

                        motorRight1.setPower(0.0f);
                        motorRight2.setPower(0.0f);
                        motorLeft1.setPower(0.0f);
                        motorLeft2.setPower(0.0f);

                        v_state++;
                    }
                    break;

                case 6:
                    if (have_drive_encoders_reset()) {
                        v_state++;
                    }
                    break;

                default:

                    break;
            }


            update_telemetry();
            telemetry.addData("18", "State: " + v_state);


        }

    private void time(int i) {
    }


}


package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

public class Autobots_Simple extends OpMode {

    DcMotor rightMotor;
    DcMotor leftMotor;

    final static int ENCODER_CPR = 1440;
    final static double GEAR_RATIO = 2;
    final static int WHEEL_DIAMETER = 4;
    final static int DISTANCE = 24;

    final static double CIRCUMFERENCE = Math.PI * WHEEL_DIAMETER;
    final static double ROTATIONS = DISTANCE / CIRCUMFERENCE;
    final static double COUNTS = ENCODER_CPR * ROTATIONS * GEAR_RATIO;

    @Override
    public void init() {
        leftMotor = hardwareMap.dcMotor.get("left_drive");
        rightMotor = hardwareMap.dcMotor.get("right_drive");

        rightMotor.setDirection(DcMotor.Direction.REVERSE);

        leftMotor.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        rightMotor.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
    }

    @Override
    public void start() {
        leftMotor.setTargetPosition((int) COUNTS);
        rightMotor.setTargetPosition((int) COUNTS);

        leftMotor.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);
        rightMotor.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);

        leftMotor.setPower(0.5);
        rightMotor.setPower(0.5);
    }

    @Override
    public void loop() {
        telemetry.addData("Motor Target", COUNTS);
        telemetry.addData("Left Position", leftMotor.getCurrentPosition());
        telemetry.addData("Right Position", rightMotor.getCurrentPosition());
    }


*/
