package RicksCode.Bill_VV;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by jmgu3 on 11/7/2016.
 */
public class ParameterFileHandler {

    // all data here
    public Integer waitTime;
   // public Double shooterSpeed;
    public Double shooterWait;
    public Double shooterForwardTime;
    public Double shooterForwardAfterShoot;
    public Double driveSpeed;
    public Double whiteColor;
    public Double driveBackTime;
    // variables used during the configuration process
    private String configFileName = "AutonInfo.txt";

    public void initializeValues() {
        waitTime = 0;
        shooterWait = 0.0;
        shooterForwardTime = 0.0;
        shooterForwardAfterShoot = 0.0;
        driveSpeed = .5;
        whiteColor = .5;
        driveBackTime = 750.0;



    }

    public void readDataFromFile(Context context) {
        // setup initial configuration parameters here
        initializeValues();

        // read configuration data from file
        try {
            InputStream inputStream = context.openFileInput(configFileName);

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                // read data here
                waitTime = Integer.valueOf(bufferedReader.readLine());
                shooterWait = Double.valueOf(bufferedReader.readLine());
                shooterForwardTime = Double.valueOf(bufferedReader.readLine());
                shooterForwardAfterShoot = Double.valueOf(bufferedReader.readLine());
                driveSpeed = Double.valueOf(bufferedReader.readLine());
                whiteColor = Double.valueOf(bufferedReader.readLine());
                driveBackTime = Double.valueOf(bufferedReader.readLine());
                //shooterSpeed = Double.valueOf(bufferedReader.readLine());

                inputStream.close();
            }
        } catch (Exception e) {
            // values here, for first time or in case there's a problem reading.
            initializeValues();
        }

    }

    public boolean writeDataToFile(Context context) {
        // may want to write configuration parameters to a file here if they are needed for teleop too!
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(configFileName, Context.MODE_PRIVATE));

            // write data here, as a string on its own line. "\n" puts a new line at the end of the write, like hitting "enter"

            outputStreamWriter.write(Integer.toString(waitTime) + "\n");
            outputStreamWriter.write(Double.toString(shooterWait) + "\n");
            outputStreamWriter.write(Double.toString(shooterForwardTime) + "\n");
            outputStreamWriter.write(Double.toString(shooterForwardAfterShoot) + "\n");
            outputStreamWriter.write(Double.toString(driveSpeed) + "\n");
            outputStreamWriter.write(Double.toString(whiteColor) + "\n");
            outputStreamWriter.write(Double.toString(driveBackTime) + "\n");
            outputStreamWriter.close();
            return true;
        } catch (IOException e) {
            return false;
        }

    }

}