package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import android.speech.tts.TextToSpeech;
import android.content.Context;
import android.app.Application;
import java.lang.reflect.Method;

import java.util.Locale;

/**
 * Created by Preston on 11/3/17.
 */

@Autonomous(name="AutonomousDriveTest", group="Testing")
public class AutonomousDriveTest extends LinearOpMode
{
    DriveEngine engine;
    Camera camera;
    double x = 0;
    double y = 0;
    DcMotor lift;
    TouchSensor touchBottom;

    Servo servo1;
    Servo servo2;

    private TextToSpeech myTTS;

    ElapsedTime timer = new ElapsedTime();

    RelicRecoveryVuMark mark;
    double counter = 0;

    void updateScreen()
    {
        telemetry.addData("Mark: ", mark);
        telemetry.addData("Counter: ", counter);
        telemetry.update();
        idle();
    }

    @Override
    public void runOpMode()
    {
        engine = new DriveEngine(hardwareMap);
        camera = new Camera(hardwareMap, telemetry);
        mark = RelicRecoveryVuMark.UNKNOWN;
        touchBottom = hardwareMap.touchSensor.get("touchBottom");

        servo2 = hardwareMap.servo.get("servo2");
        servo1 = hardwareMap.servo.get("servo1");

        lift = hardwareMap.dcMotor.get("lift");
        lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        myTTS =new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
            }
        });

        myTTS.setLanguage(Locale.US);
        myTTS.setSpeechRate(2.0f);
        waitForStart();


        close();

        double counter = 0;

        while (opModeIsActive())
        {
            while(mark == RelicRecoveryVuMark.UNKNOWN) {
                mark = camera.identify();
                if (mark != RelicRecoveryVuMark.UNKNOWN) {
                    switch (mark) {
                        case LEFT:
                            counter = userInput(2.13);
                            break;
                        case CENTER:
                            counter = userInput(1.82);
                            break;
                        case RIGHT:
                            counter = userInput(1.52);
                            break;
                        default:
                            counter = 0;
                    }
                    timer.reset();
                    break;
                }
                updateScreen();
            }
            double t1 = 0;
            if(timer.time() < counter){
                x = -.5;
                y = 0;
            }
            else if(timer.time() < counter + .5){
                x = 0;
                y = 0;
            }
            else if (t1 == 0){
                t1 = userInput(.79);
            }
            else if(timer.time() < counter+.5 + t1) {
                x = 0;
                y = -.5;
            }else if(timer.time() < counter+.5+ t1 + 4){
                x = 0;
                y = 0;
                open();
                if(!touchBottom.isPressed()){
                     lift.setPower(-1);
                }
                else{
                    lift.setPower(0);
                }
            }else if(timer.time() < counter+.5+ t1+4+ .6 ){
                x = 0;
                y = .5;
            }else{
                x = 0;
                y = 0;
                lift.setPower(0);
            }

            engine.drive(x, y);


        }

    }

    private void open()
    {
        servo2.setPosition(1);
        servo1.setPosition(1);
    }

    private void close()
    {
        servo2.setPosition(0.2);
        servo1.setPosition(0.2);
    }

    private double userInput(double hint){
        speak("Your hint is "+hint+" seconds.");
        return userInput();
    }
    private double userInput(){
        waitToSpeak("How many decimal places?");
        boolean a = false;
        boolean b = false;
        double count  = 0;
        double sum = 0;

        while (!gamepad1.b){
            if(!gamepad1.a){
                a = false;
            }
            else if(a == false && gamepad1.a){
                count += 1;
                speak(count + "");
                a = true;
            }
            else if(gamepad1.a && a)
                a = true;
            else
                a = false;
        }
        double decimalPlaces = count;
        count = 0;

        for(int k = 0; k < decimalPlaces; k++){
            while (gamepad1.b == false && b == false){
                if(!gamepad1.a){
                    a = false;
                }
                else if(a == false && gamepad1.a){
                    count ++;
                    speak(count + "");
                    if(k == 1)
                        waitToSpeak("tenths");
                    if(k == 2)
                        waitToSpeak("hundredths");
                    a = true;
                }
                else {
                    a = true;
                }
            }
            sum += count * Math.pow(10, -1 * k);
            while(b)

            b = gamepad1.b;
            count = 0;
        }
        return sum;
    }

    private void speak(String speech) {
        myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
    }
    private void waitToSpeak(String speech){myTTS.speak(speech, TextToSpeech.QUEUE_ADD, null);}
    /**
     *
     * @param x
     * @return inverse hyperbolic cosine of x
     */
    private double acosh(double x){
        return Math.log(x + Math.sqrt(x*x-1.0));
    }

    double vMax = 25.0; //inches per second
    double dragConstant = 44.7;

    /**
     * Time Bogg needs to go a distance at .5 power
     * @param distance
     * @return time
     */
    public double timeFromDistance(double distance){
        double t = vMax/dragConstant * acosh(Math.exp(dragConstant/(vMax*vMax)));
        return t;
    }

    public static Context getContext() {
        try {
            final Class<?> activityThreadClass =
                    Class.forName("android.app.ActivityThread");
            //find and load the main activity method
            final Method method = activityThreadClass.getMethod("currentApplication");
            return (Application) method.invoke(null, (Object[]) null);
        } catch (final java.lang.Throwable e) {
            // handle exception
            throw new IllegalArgumentException("No context could be retrieved!");
        }
    }
}

