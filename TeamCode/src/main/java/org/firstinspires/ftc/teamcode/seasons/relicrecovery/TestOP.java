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
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            JSONObject jsonObject = (JSONObject) obj;
            try {
                String name = (String) jsonObject.get("name");

                //print name in test-op
                JSONArray memberNames = (JSONArray) jsonObject.get("memberNames");
                String randomMemberName = (String) memberNames.get(rand.nextInt(9));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
/*
E/EventLoopManager: java.lang.NullPointerException
E/EventLoopManager:     at org.firstinspires.ftc.teamcode.seasons.relicrecovery.TestOP.runOpMode(TestOP.java:44)
 */