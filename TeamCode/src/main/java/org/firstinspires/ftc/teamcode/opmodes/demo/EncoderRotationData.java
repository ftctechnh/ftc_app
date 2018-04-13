package org.firstinspires.ftc.teamcode.opmodes.demo;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.JsonWriter;
import android.widget.Toast;

import com.disnodeteam.dogecv.math.Line;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.libraries.AutoLib;
import org.firstinspires.ftc.teamcode.libraries.SensorLib;
import org.firstinspires.ftc.teamcode.libraries.hardware.BotHardware;
import org.firstinspires.ftc.teamcode.libraries.hardware.MatbotixUltra;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

/**
 * Created by Noah on 3/25/2018.
 */

@Autonomous(name="Ultra Encoder Stuff")
@Disabled
public class EncoderRotationData extends OpMode {
    private final int COUNTS = 750;
    private final float POWER = 0.4f;
    //parameters for gyro turning
    private float Kp5 = 10.0f;        // degree heading proportional term correction per degree of deviation
    private float Ki5 = 0.0f;         // ... integrator term
    private float Kd5 = 0;             // ... derivative term
    private float Ki5Cutoff = 0.0f;    // maximum angle error for which we update integrator

    private final SensorLib.PID motorPID = new SensorLib.PID(Kp5, Ki5, Kd5, Ki5Cutoff);

    private AutoLib.LinearSequence mSeq;
    private final BotHardware bot = new BotHardware(this);
    private MatbotixUltra sensor;

    private int startUltra;
    private int startCount;

    private JSONArray dataRay = new JSONArray();

    public void init() {
        bot.init();
        bot.start();

        sensor = new MatbotixUltra(hardwareMap.get(I2cDeviceSynch.class, "ultraback"), 100);
        sensor.initDevice();
        sensor.startDevice();
        sensor.getReading();

        buildSeq();
    }

    public void start() {
        startUltra = sensor.getReading();
        startCount = BotHardware.Motor.frontRight.motor.getCurrentPosition();
    }

    public void loop() {
        if(mSeq.loop()) buildSeq();
    }

    public void stop() {
        bot.stopAll();

        Writer output = null;
        try {
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/data.json");
            output = new BufferedWriter(new FileWriter(file));
            output.write(dataRay.toString());
            Toast.makeText(hardwareMap.appContext, "F ME IT WORKED", Toast.LENGTH_LONG).show();
        }
        catch(Exception e) {
            //oops
            e.printStackTrace();
        }
        finally {
            try {
                if (output != null) {
                    output.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void buildSeq() {
        int counts = COUNTS + (int)Math.round(Math.random() * 1000 - 500);

        mSeq = new AutoLib.LinearSequence();
        mSeq.add(new AutoLib.MoveByEncoderStep(bot.getMotorRay(), -POWER, counts, true));
        mSeq.add(new AutoLib.LogTimeStep(this, "WAIT", 1.0));
        mSeq.add(new RecordUltra());
        mSeq.add(new AutoLib.GyroTurnStep(this, 0, bot.getHeadingSensor(), bot.getMotorVelocityShimArray(), 90.0f, 360.0f, motorPID, 0.25f, 10, true));
        mSeq.add(new AutoLib.LogTimeStep(this, "WAIT", 1.0));
        mSeq.add(new AutoLib.MoveByEncoderStep(bot.getMotorRay(), POWER, counts, true));
        mSeq.add(new AutoLib.LogTimeStep(this, "WAIT", 1.0));
        mSeq.add(new RecordUltra());
        mSeq.add(new AutoLib.GyroTurnStep(this, 0, bot.getHeadingSensor(), bot.getMotorVelocityShimArray(), 90.0f, 360.0f, motorPID, 0.25f, 10, true));
        mSeq.add(new AutoLib.LogTimeStep(this, "WAIT", 1.0));
    }

    private class RecordUltra extends AutoLib.Step {
        public boolean loop() {
            int reading = sensor.getReading();
            int pos = BotHardware.Motor.frontRight.motor.getCurrentPosition();
            JSONObject data = new JSONObject();
            try {
                data.put("ultra", Math.abs(reading - startUltra));
                data.put("encode", Math.abs(pos - startCount));
            }
            catch (JSONException e) {
                throw new IllegalStateException(e.getMessage());
            }
            dataRay.put(data);
            startUltra = reading;
            startCount = pos;
            return true;
        }
    }

}
