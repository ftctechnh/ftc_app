/*
Copyright (c) 2016 Robert Atkinson

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Robert Atkinson nor the names of his contributors may be used to
endorse or promote products derived from this software without specific prior
written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESSFOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package org.firstinspires.ftc.teamcode;

import android.os.Environment;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


@Autonomous(name="Nullbot: Encoder Replay", group="Nullbot")
public class NullbotReplayMotorPowers extends LinearOpMode {

    /* Declare OpMode members. */
    NullbotHardware robot = new NullbotHardware();   // Use a Pushbot's hardware
    String f = "Nullbot-log-Oct 3, 2015 2:43:47 AM.txt";
    JSONArray commands;
    int index = 0;

    @Override
    public void runOpMode() {
        robot.init(hardwareMap, this, true);
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

        while (opModeIsActive() && index < robot.secondsToTrack * robot.hz) {
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
        }
    }

    public void loadFile(String filename) throws IOException, JSONException {
        final File path =
                Environment.getExternalStoragePublicDirectory
                        (
                                Environment.DIRECTORY_DOCUMENTS + "/NullbotLogs/"
                        );
        String text = new BufferedReader(new FileReader(path)).readLine();
        commands = new JSONArray(text);
    }
}
