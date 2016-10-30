package org.firstinspires.ftc.teamcode;

//------------------------------------------------------------------------------
//
// PushBotManual
//

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="TeleOp", group="Manual")

public class RobotTeleOp extends RobotTelemetry {

    private boolean manual = true;

    enum AUTO_STATE {
        FIND_BLUE_LINE,
        FOLLOW_BLUE_LINE,
        PREP_BEACON,
        PUSH_BEACON,
        END
    }

    AUTO_STATE current_state = AUTO_STATE.FIND_BLUE_LINE;

    boolean firstLaunch = false; //A variable used for initializing variables in different areas of the OpMode.
    boolean pressed;
    boolean bPress;
    long startTime;
    long endTime;
    double SLOW_SPEED = 0.07;
    double QUICK_SPEED = 0.10;
    int COLOR_THRESHOLD = 5;
    int BEACON_DISTANCE = 5;
    double OPT_DISTANCE = 2.8;

    float SPEED_SCALE = 2.5F;


    @Override public void init() {
        super.init();
        beaconPosition(0.5);

    }


    private boolean toggle = true;

    @Override
    public void start(){
        // arm_3_position(0.4D);
    }

    @Override
    public void stop() {
        //arm_1_position(0.26D);
        //arm_2_position(0.54D);
    }
    @Override
    public void loop() {
        //region Old Crap
        //
        // GAMEPAD 1
        // Manage the drive wheel motors.
        //
        //Manage the arm servos.
        //GAMEPAD 2
        //arm_1_position(get_arm_1_position() + gamepad2.left_stick_ y);
        //arm_2_position(get_arm_2_position() + gamepad2.right_stick_y);


        //if (gamepad2.x){ Nah.
        //    ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 100);
        //    toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 200);
        //
        //    arm_home_position();
        //}

        /*Raw Controls
        double deadzone = .05;
        if (Math.abs(gamepad2.left_stick_y - prev_left_y) > deadzone) {
            arm_1_position(-gamepad2.left_stick_y);
            prev_left_y = gamepad2.left_stick_y;
        }
        if (Math.abs(gamepad2.right_stick_y - prev_right_y) > deadzone) {
            arm_2_position(-gamepad2.right_stick_y);
            prev_right_y = gamepad2.right_stick_y;
        }
        */
        //endregion

        if (gamepad1.x){
            if (!bPress){
                current_state = AUTO_STATE.FIND_BLUE_LINE;
                if (manual){
                    manual = false;
                }else{
                    manual = true;
                }

                bPress = true;
            }
        } else {
            bPress = false;
        }

        telemetry.addData("Manual", manual);
        telemetry.addData("State", current_state.toString());
        telemetry.addData("BServo", getBeaconPosition());

        if (manual){
            float l_left_drive_power = scale_motor_power(-gamepad1.left_stick_y) / SPEED_SCALE;
            float l_right_drive_power = scale_motor_power(-gamepad1.right_stick_y) / SPEED_SCALE;

            if (gamepad1.dpad_up || gamepad1.dpad_left || gamepad1.dpad_right || gamepad1.dpad_down){
                DPADPower();
            }else{
                set_drive_power(l_left_drive_power, l_right_drive_power);
            }

            if (gamepad2.left_bumper){
                beaconPosition(getBeaconPosition() - 0.05);
            }
            if (gamepad2.right_bumper){
                beaconPosition(getBeaconPosition() + 0.05);
            }

        }
        else
        {
            switch (current_state) {
                case FIND_BLUE_LINE:
                    set_drive_power(SLOW_SPEED, SLOW_SPEED);

                    if (getLineFollowState(RobotAutoBeacons.VV_LINE_COLOR.BLUE, COLOR_THRESHOLD) != RobotAutoBeacons.ROBOT_LINE_FOLLOW_STATE.NONE){
                        current_state = AUTO_STATE.FOLLOW_BLUE_LINE;
                    }
                    break;

                case FOLLOW_BLUE_LINE:
                    if (rangeSensor.cmUltrasonic() >= BEACON_DISTANCE || rangeSensor.cmOptical() >= OPT_DISTANCE || rangeSensor.cmOptical() == 0){
                        switch (getLineFollowState(RobotAutoBeacons.VV_LINE_COLOR.BLUE, COLOR_THRESHOLD)){
                            case LEFT:
                                set_drive_power(QUICK_SPEED, 0);
                                break;
                            case RIGHT:
                                set_drive_power(0, QUICK_SPEED);
                                break;
                            case BOTH:
                                set_drive_power(SLOW_SPEED, SLOW_SPEED);
                                break;
                        }
                    }else{
                        current_state = AUTO_STATE.PREP_BEACON;
                    }
                    break;

                case PREP_BEACON:
                    if (getBeaconColor() == RobotAutoBeacons.VV_BEACON_COLOR.BLUE) {
                        prepareForBeacon(true);
                    }

                    current_state = AUTO_STATE.PUSH_BEACON;
                    break;

                case PUSH_BEACON:
                    if (firstLaunch){
                        //Set times.
                        startTime = System.currentTimeMillis();
                        endTime = startTime + 250;
                        firstLaunch = false;
                    }

                    if (pressed){
                        //Back up for roughly 1/4 a second.
                        if (System.currentTimeMillis() >= endTime){
                            stopdrive();
                            pressed = false;
                            firstLaunch = true;
                            current_state = AUTO_STATE.END;
                        }else{
                            set_drive_power(-SLOW_SPEED, -SLOW_SPEED);
                        }
                    }else{
                        //Go forward for roughly 1/4 a second.
                        if (System.currentTimeMillis() >= endTime){
                            stopdrive();
                            pressed = true;
                            firstLaunch = true;
                        }else{
                            set_drive_power(SLOW_SPEED, SLOW_SPEED);
                        }
                    }
                    break;

                case END:
                    manual = true;
                    break;
            }
        }
    }

    void DPADPower() {
        if (gamepad1.dpad_up){
            set_drive_power(1, 1);
        }
        if (gamepad1.dpad_left){
            set_drive_power(-1, 1);
        }
        if (gamepad1.dpad_right){
            set_drive_power(1, -1);
        }
        if (gamepad1.dpad_down){
            set_drive_power(-1, -1);
        }
    }
}
