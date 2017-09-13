package org.firstinspires.ftc.rick;

import android.content.Context;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;




public class FtcConfig {
//

    private enum ConfigStep {
        TEST_GAMEPAD1,
        TEST_GAMEPAD2,
        COLOR,
        DELAY,
        AUTON_TYPE,
        READY;
        private static ConfigStep[] vals = values();
        public ConfigStep next() { return vals[(this.ordinal()+1) % vals.length];}
        public ConfigStep prev() { return vals[(this.ordinal()-1+vals.length) % vals.length];}
    }

    public class Param {
        boolean teamIsBlue;
        int delayInSec;
        AutonType autonType;
    }

    public enum AutonType {
        GO_FOR_BEACON,
        GO_FOR_MOUNTAIN;
        private static AutonType[] vals = values();
        public AutonType next() { return vals[(this.ordinal()+1) % vals.length];}
        public AutonType prev() { return vals[(this.ordinal()-1+vals.length) % vals.length];}
    }

    // the parameters that need to be setup during configuration
    boolean gamepad1IsOK, gamepad2IsOK;

    public Param param;

    // variables used during the configuration process
    ConfigStep configStepState, currConfigStepCheck;
    boolean back1, a1, b1, b2, x1, y1, start1;
    boolean lastBack1, lastA1, lastB1, lastB2, lastX1, lastY1, lastStart1;  //debounce
    private String configFileName="FtcRobotConfig.txt";

    public FtcConfig() {
        param = new Param();
    }

    public void init(Context context, LinearOpMode opMode) {
        // setup initial configuration parameters here
        gamepad1IsOK=false;
        gamepad2IsOK=false;
        //context=hardwareMap.appContext;

        // read configuration data from file
        try {
            InputStream inputStream = context.openFileInput(configFileName);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                param.teamIsBlue = Boolean.valueOf(bufferedReader.readLine());
                param.delayInSec = Integer.valueOf(bufferedReader.readLine());
                String autonTypeString = bufferedReader.readLine();
                for (AutonType a : AutonType.values()) {
                    if (a.name().equals(autonTypeString)) {
                        param.autonType = a;
                    }
                }

                inputStream.close();
            }
        }
        catch (Exception e) {
            opMode.telemetry.addData("Exception", "Error reading config file: " + e.toString());
            // can't read from file, so initialize to reasonable values
            param.teamIsBlue=true;
            param.delayInSec=0;
            param.autonType= AutonType.GO_FOR_BEACON;
        }

        // setup initial toggle memory states for buttons used
        lastBack1=false; lastA1=false; lastB2=false; lastY1=false; lastStart1=false;
        configStepState = ConfigStep.TEST_GAMEPAD1;
    }

    public void init_loop(Context context, LinearOpMode opMode) {
        // read the gamepad state
        a1 = opMode.gamepad1.a;
        b2 = opMode.gamepad2.b;
        y1 = opMode.gamepad1.y;
        x1 = opMode.gamepad1.x;
        b1 = opMode.gamepad1.b;

        back1 = opMode.gamepad1.y || opMode.gamepad1.left_bumper || opMode.gamepad1.right_bumper;
        start1 = opMode.gamepad1.a;

        opMode.telemetry.clear();  // Rick maybe this should be clearall?

        currConfigStepCheck = ConfigStep.TEST_GAMEPAD1;
        // message to driver about state of this config parameter
        if (configStepState.ordinal() >= currConfigStepCheck.ordinal()) {
            if (!gamepad1IsOK) {
                opMode.telemetry.addData("C" + currConfigStepCheck.ordinal(), "GAMEPAD1 NOT VERIFIED!!!");
            }
        }
        // configure this parameter
        if (configStepState == currConfigStepCheck) {
            if (!gamepad1IsOK) {
                opMode.telemetry.addData("C" + currConfigStepCheck.ordinal() + "A", "Push A on Gamepad 1");
            }
            if (a1) {
                gamepad1IsOK = true;
            }
        }

        currConfigStepCheck = ConfigStep.TEST_GAMEPAD2;
        // message to driver about state of this config parameter
        if (configStepState.ordinal() >= currConfigStepCheck.ordinal()) {
            if (!gamepad2IsOK) {
                opMode.telemetry.addData("C" + currConfigStepCheck.ordinal(), "GAMEPAD2 NOT VERIFIED!!!");
            }
        }
        // configure this parameter
        if (configStepState == currConfigStepCheck) {
            if (!gamepad2IsOK) {
                opMode.telemetry.addData("C" + currConfigStepCheck.ordinal() + "A", "Push B on Gamepad 2");
            }
            if (b2) {
                gamepad2IsOK = true;
            }
        }


        currConfigStepCheck = ConfigStep.COLOR;
        // message to driver about state of this config parameter
        if (configStepState.ordinal() >= currConfigStepCheck.ordinal()) {
            if (param.teamIsBlue) {
                opMode.telemetry.addData("C" + currConfigStepCheck.ordinal(), "Color: Red");
            } else {
                opMode.telemetry.addData("C" + currConfigStepCheck.ordinal(), "Color: Blue");
            }
        }
        // configure this parameter
        if (configStepState == currConfigStepCheck) {
            opMode.telemetry.addData("C" + currConfigStepCheck.ordinal() + "A", "Push B for Red, X for Blue");
            if (opMode.gamepad1.x) {
                param.teamIsBlue = false;
            }
            if (opMode.gamepad1.b) {
                param.teamIsBlue = true;
            }
      }

        currConfigStepCheck = ConfigStep.DELAY;
        // message to driver about state of this config parameter
        if (configStepState.ordinal() >= currConfigStepCheck.ordinal()) {
            opMode.telemetry.addData("C" + currConfigStepCheck.ordinal(), "Delay: " + param.delayInSec + " sec");
        }
        // configure this parameter
        if (configStepState == currConfigStepCheck) {
            opMode.telemetry.addData("C" + configStepState.ordinal() + "A", "Push X for -, Y for +");
            if (b1 && !lastB1) {
                param.delayInSec++;
            }
            if (x1 && !lastX1) {
                param.delayInSec--;
                if (param.delayInSec < 0) {
                    param.delayInSec = 0;
                }
            }
        }

        currConfigStepCheck = ConfigStep.AUTON_TYPE;
        // message to driver about state of this config parameter
        if (configStepState.ordinal() >= currConfigStepCheck.ordinal()) {
            opMode.telemetry.addData("C" + currConfigStepCheck.ordinal(), "Auton: " + param.autonType.name());
        }
        // configure this parameter
        if (configStepState == currConfigStepCheck) {
            opMode.telemetry.addData("C" + configStepState.ordinal() + "A", "Push Y for +, A for -");
            if (b1 && !lastB1) {
                param.autonType = param.autonType.next();
            }
            if (x1 && !lastX1) {
                param.autonType = param.autonType.prev();
            }
        }

        currConfigStepCheck = ConfigStep.READY;
        // message to driver about state of this config parameter
        if (configStepState.ordinal() >= currConfigStepCheck.ordinal() ) {
            opMode.telemetry.addData("C" + currConfigStepCheck.ordinal(), "READY TO GO!");

            // may want to write configuration parameters to a file here if they are needed for teleop too!
            try {
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(configFileName, Context.MODE_PRIVATE));

                // write each configuration parameter as a string on its own line
                outputStreamWriter.write(Boolean.toString(param.teamIsBlue)+"\n");
                outputStreamWriter.write(Integer.toString(param.delayInSec)+"\n");
                outputStreamWriter.write(param.autonType.name()+"\n");

                outputStreamWriter.close();
            }
            catch (IOException e) {
                opMode.telemetry.addData("Exception", "Configuration file write failed: " + e.toString());
            }

        }

        if (configStepState != ConfigStep.READY) {
            opMode.telemetry.addData("D" + configStepState.ordinal(), "Push Start for next option");
        }
        opMode.telemetry.addData("E" + configStepState.ordinal(), "Push Back or a Bumper to go back");

        if (start1 && !lastStart1 && (configStepState.ordinal() < ConfigStep.READY.ordinal())) {
            configStepState = configStepState.next();
        }

        if (back1 && !lastBack1 && (configStepState.ordinal() >0) ) {
            configStepState = configStepState.prev();
        }

        // update toggle memory for next call
        lastBack1=back1;
        lastA1=a1;
        lastB1=b1;
        lastB2=b2;
        lastX1=x1;
        lastY1=y1;
        lastStart1=start1;
    }

}
