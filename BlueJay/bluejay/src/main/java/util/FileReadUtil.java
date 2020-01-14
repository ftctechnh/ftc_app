package util;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FileReadUtil {
	public static String readFromSD(){
		File sdcard = Environment.getExternalStorageDirectory();

		//Get the text file
		File file= new File(sdcard,"Vuforia Key.txt");

		//Read text from file
		StringBuilder text = new StringBuilder();

		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;

			while ((line = br.readLine()) != null) {
				text.append(line);
				text.append('\n');
			}
			br.close();
		}
		catch (IOException e) {
		}

		return text.toString();
	}

}
