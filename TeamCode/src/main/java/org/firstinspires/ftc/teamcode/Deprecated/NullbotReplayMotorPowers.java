package org.firstinspires.ftc.teamcode.Deprecated;

import android.os.Environment;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.NullbotHardware;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

@Autonomous(name="Nullbot: Encoder Replay", group="Nullbot")
@Disabled
public class NullbotReplayMotorPowers extends LinearOpMode {

    NullbotHardware robot = new NullbotHardware();
    String f = "auto.txt";
    JSONArray commands;
    int index = 0;

    @Override
    public void runOpMode() {
        robot.init(hardwareMap, this, gamepad1, gamepad2);
        try {
            loadFile(f);
        } catch (JSONException e) {
            telemetry.log().add("JSON failed to be parsed");
            sleep(5000);
        } catch (IOException e) {
            telemetry.log().add("File failed to be read");
            sleep(5000);
        }
        waitForStart();

        /*while (opModeIsActive() && index < robot.secondsToTrack * robot.hz) {
            try {
                JSONObject command = commands.getJSONObject(index);
                robot.motorArr[0].setPower(command.getDouble("fLE"));
                robot.motorArr[1].setPower(command.getDouble("fRE"));
                robot.motorArr[2].setPower(command.getDouble("bLE"));
                robot.motorArr[3].setPower(command.getDouble("bRE"));
                index++;
                robot.waitForTick(1000 / robot.hz);
            } catch (JSONException e) {
                telemetry.log().add("JSON failed to be parsed");
                sleep(5000);
            }
        }*/
    }

    public void loadFile(String filename) throws IOException, JSONException {
        final File path =
                Environment.getExternalStoragePublicDirectory
                        (
                                Environment.DIRECTORY_DOCUMENTS + "/NullbotLogs/" + filename
                        );
        String text = new BufferedReader(new FileReader(path)).readLine();
        commands = new JSONArray(text);
    }
}
