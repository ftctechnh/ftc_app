package org.firstinspires.ftc.teamcode.seasons.relicrecovery;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Random;

/**
 * This class is a simple testing program to test any code on.
 */
@TeleOp(name = "Test Op", group = "test")
public class TestOP extends LinearOpMode {
    private RelicRecoveryRobot robot;


    @Override
    public void runOpMode() throws InterruptedException {

        waitForStart();

        while (opModeIsActive()) {
            // PASTE YOUR CODE HERE

            Gson gson = new Gson();
            Random rand = new Random();
            JsonParser parser = new JsonParser();

            Object obj = null;
            try {
                obj = parser.parse(new FileReader("Testing.json"));
                if(obj != null){
                    telemetry.addData("File has been:", "Parsed");
                } else {
                    telemetry.addData("File has been:", "Not Parsed");
                }
                telemetry.update();
                JSONObject jsonObject = (JSONObject) obj;
                String name = (String) jsonObject.get("name");
                String num = (String) jsonObject.get("team");

                telemetry.addData("Name", name);
                telemetry.addData("Team #", num);
                telemetry.update();

            } catch (FileNotFoundException e) {
                telemetry.addData("Error Report", e.getMessage());
            }catch (JSONException e) {
                telemetry.addData("Error Report", e.getMessage());
            }
        }
    }
}