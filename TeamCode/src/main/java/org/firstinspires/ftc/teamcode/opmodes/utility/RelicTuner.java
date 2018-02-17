package org.firstinspires.ftc.teamcode.opmodes.utility;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.opmodes.teleop.RelicTeleOp;


/**
 * Created by Derek on 2/9/2018.
 * <h1>Tuner Op mode</h1>
 * <h5>Tuner Op mode for the Relic Recovery season</h5>
 *
 * <p>
 *     The purpose of this Op mode is to help us adjust values critical to the manual
 *     and automatic control of the robot
 *
 *     @see org.firstinspires.ftc.teamcode.phone.PrefLoader
 *
 *     @see SharedPreferences
 *
 * </p>
 *

 */

@SuppressWarnings("WeakerAccess")
@TeleOp(name="RelicTuneOp",group = "Relic")
public class RelicTuner extends RelicTeleOp {

    SharedPreferences.Editor editor;
    private boolean editingMode = true,locked = false;
    private int editIndex = 0;
    private double value = 0;
    private double increment = 0.01;

    @Override
    public void init() {
        super.init();
        telemetry.clearAll();
        telemetry.addLine("Press right bumper to save changes and end");
        telemetry.addLine("Press Left bumper to switch modes to test");
        telemetry.addLine("X and B select variable to edit");
        telemetry.addLine("Dpad left and right increment the value");
        telemetry.addLine("Dpad up and down change the increment by an order of magnitude");
        telemetry.addLine("A to save and unlock!");
        telemetry.addLine("Be sure to test first!");
    }

    @SuppressLint("CommitPrefEdits") //taken care of in stop()
    @Override
    public void start() {
        super.start();
        telemetry.clearAll();
        editor = prefs.edit();
    }

    @Override
    public void loop() {

        if (wrapper1.RIGHT_BUMPER.isPressed()) {
            requestOpModeStop();
        }

        if (wrapper1.LEFT_BUMPER.isPressed()) {
            editingMode = !editingMode;
        }

        telemetry.addData("editing?",editingMode);
        telemetry.addData("Currently editing",tuneArr[editIndex]);
        telemetry.addData("Value",value);
        telemetry.addData("editIndex",editIndex);
        telemetry.addData("Increment", increment);

        if(editingMode) {
            if (!locked) {
                if (wrapper1.B.isPressed()) editIndex = (editIndex < tuneArr.length - 1) ? (editIndex + 1) : 0;
                if (wrapper1.X.isPressed()) editIndex = (editIndex > 0) ? (editIndex - 1) : tuneArr.length;
                value = Double.parseDouble(prefs.getString(tuneArr[editIndex],"1.337"));
            } else if (wrapper1.B.isPressed() || wrapper1.X.isPressed()) {
                telemetry.addLine("Cannot Change Index until current changes are applied!!!");
            }




            //value editing
            if (wrapper1.DPAD_RIGHT.isPressed()) {
                locked = true;
                value+=increment;
                editor.putString(tuneArr[editIndex],Double.toString(value));
            }

            if (wrapper1.DPAD_LEFT.isPressed()) {
                locked = true;
                value-= increment;
                editor.putString(tuneArr[editIndex],Double.toString(value));
            }

            //Increment
            if (wrapper1.DPAD_UP.isPressed()) {
                increment *= (increment == 10) ? 1 : 10;
            }

            if (wrapper1.DPAD_DOWN.isPressed()) {
                increment /= (increment == 0.01) ? 1 : 10;
            }

            //lock / unlock
            if (wrapper1.A.isPressed()) {
                if (locked) {
                    //NOT ASYNCHRONOUS
                    editor.commit();
                }
                locked = !locked;
            }

            update();
        } else {
            super.loop();
            telemetry.clear();
        }

    }

    @Override
    public void stop() {
        super.stop();
        editor.apply();
    }
}
