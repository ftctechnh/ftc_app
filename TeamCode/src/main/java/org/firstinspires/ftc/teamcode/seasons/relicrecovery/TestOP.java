package org.firstinspires.ftc.teamcode.seasons.relicrecovery;

import com.google.gson.JsonParser;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.seasons.relicrecovery.mechanism.impl.GlyphLift;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.BufferedReader;
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
        robot = new RelicRecoveryRobot(this);

        waitForStart();

        while (opModeIsActive()) {
                                    // PASTE YOUR CODE HERE
/*
            son gson = new Gson();
            Random rand = new Random();
            JsonParser parser = new JsonParser();

            Object obj = parser.parse(new FileReader("Testing.json"));
            JSONObject jsonObject = (JSONObject) obj;
            String name = (String) jsonObject.get("name");
            //print name in test-op
            JSONArray memberNames = (JSONArray) jsonObject.get("memberNames");
            String randomMemberName = (String) memberNames.get(rand.nextInt(9));

    public JsonTesting() throws FileNotFoundException, JSONException {
            } **/
        }
    }
}
