package ee.itcollege.android.schedule;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

public class FrontpageActivity extends Activity {

	File sdcard = Environment.getExternalStorageDirectory();
	File dir = new File(sdcard.getAbsolutePath() + "/Schedule");
	String filename = "userID" + ".txt";
	File file = new File(dir, filename);

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.front);
		File sdcard = Environment.getExternalStorageDirectory();
		File dir = new File(sdcard.getAbsolutePath() + "/Schedule");

		EditText edittext = (EditText) findViewById(R.id.userID);

		CheckBox checkbox = (CheckBox) findViewById(R.id.rememberMe);

		if (dir.exists()) {
			try {
				String result = readFile();
				if (result != "") {
					edittext.setText(result);
					checkbox.setChecked(true);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else
			checkbox.setChecked(false);

	}

	public void writeToSDCard(String userID) {
		if (!dir.exists()) {
			dir.mkdirs();
		}

		if (file.exists()) {
			file.delete();
		}

		BufferedWriter br = null;

		try {
			br = new BufferedWriter(new FileWriter(file));
			br.write(userID);
			br.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != br)
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	private String readFile() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(file));
		return br.readLine();
	}

	public void onButtonClicked(View view) {
		EditText edittext = (EditText) findViewById(R.id.userID);
		String input = edittext.getText().toString();

		CheckBox checkbox = (CheckBox) findViewById(R.id.rememberMe);
		if (checkbox.isChecked()) {
			writeToSDCard(input);
		}
		if (!checkbox.isChecked() && file.exists()) {
			file.delete();
		}

		Intent intent = new Intent(this, ScheduleViewerActivity.class);
		EventListFragment.userID = input;
		startActivity(intent);
	}
}
