package org.firstinspires.ftc.teamcode.seasons.relicrecovery;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Random;

/**
 * This class is a simple testing program to test any code on.
 */
@TeleOp(name = "JSON Op", group = "test")
public class JsonOp extends LinearOpMode {
    private RelicRecoveryRobot robot;

    @Override
    public void runOpMode() throws InterruptedException {

        waitForStart();
        telemetry.addData("Status", "TestOp Running");

        while (opModeIsActive()) {
            // PASTE YOUR CODE HERE
            telemetry.update();
            JsonParser parser = new JsonParser();

            Object obj;
            try {
                //get file in folder
                obj = parser.parse(new FileReader("/storage/emulated/0/FIRST/Testing.json"));
                telemetry.update();
                JsonObject jsonObject = (JsonObject) obj;
                String name = jsonObject.get("name").toString();
                String num = jsonObject.get("team").toString();

                telemetry.addData("Name", name);
                telemetry.addData("Team #", num);
                telemetry.update();

            } catch (FileNotFoundException e) {
                telemetry.addData("Error Report", e.getMessage());
                telemetry.addData("Exception", "FileNotFound");
                telemetry.addData("Cause", e.getCause());
                telemetry.addData("e.toString()", e.toString());
            }catch (ClassCastException e) {
                telemetry.addData("Error Report", e.getMessage());
                telemetry.addData("Exception", "ClassCast");
                telemetry.addData("Cause", e.getCause());
                telemetry.addData("e.toString()", e.toString());
            }
        }
    }
}