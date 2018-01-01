package org.firstinspires.ftc.teamcode.seasons.relicrecovery;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.seasons.relicrecovery.mechanism.impl.GlyphLift;
import com.google.gson.Gson;

import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * This class is a simple testing program to test any code on.
 */
@TeleOp(name = "Test Op", group = "test")
public class RobotTeleOp extends LinearOpMode {
    private RelicRecoveryRobot robot;


    @Override
    public void runOpMode() throws InterruptedException {
        robot = new RelicRecoveryRobot(this);

        waitForStart();

        while (opModeIsActive()) {
                                    // PASTE YOUR CODE HERE

            Gson gson = new Gson();
            JSONParser parser = new JSONParser();

            try{
                Object obj = parser.parse(new FileReader("Testing.json"));
                JSONObject jsonObject = (JSONObject) obj;
                String name = (String) jsonObject.get("name");
                telemetry.addData("Data Output", name);
                JSONArray memberNames = (JSONArray) jsonObject.get("memberNames");
                String randomMemberName = (String) memberNames[rand.nextInt(9)];
                telemetry.addData("Name Output", randomMemberName);

            }catch(FileNotFoundException e){
                e.printStackTrace();
            }catch(IOException e){
                e.printStackTrace();
            }catch(ParseException e){
                e.printStackTrace();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
