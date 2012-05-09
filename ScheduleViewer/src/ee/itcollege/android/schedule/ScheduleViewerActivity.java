package ee.itcollege.android.schedule;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import android.widget.EditText;
import android.content.Context;
/**
 * @author Heiki
 * 
 */
public class ScheduleViewerActivity extends Activity {

    /**
     * Called when the activity is first created.
     * 
     * @param savedInstanceState
     * @param URI
     */
    public void onCreate(Bundle savedInstanceState, String URI) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.main);
	new FileManager()
		.DownloadFromUrl(
			"https://itcollege.ois.ee/et/schedule?&format=json&student_id=1679", "failinimi"); //$NON-NLS-1$ //$NON-NLS-2$

    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.main);
	getUserTimetable(ScheduleViewerActivity.userID);
    }

    /**
     * main differential
     */
    static String userID = "2224"; //$NON-NLS-1$

   
    
    @SuppressWarnings("unused")
    private ArrayList<Event> events = new ArrayList<Event>();

    /**
     * @author Liina
     * @param userID
          */
    @SuppressWarnings({ "hiding", "nls" })
    public void getUserTimetable(String userID) {
	Log.d("getUserTimetable", "UserID: " + userID);

	try {
	    URL url = new URL(
		    "https://itcollege.ois.ee/schedule?&format=json&student_id=" //$NON-NLS-1$
			    + URLEncoder.encode(userID));
	    new GetUrlContents().execute(url);
	} catch (MalformedURLException e) {
	    e.printStackTrace();
	}
    }

    @SuppressWarnings("static-method")
    private void requestComplete(String result) {
	// events.clear();
	try {
	    JSONObject json = new JSONObject(result);

	    @SuppressWarnings("unchecked")
	    Iterator<String> i = json.keys();
	    while (i.hasNext()) {
		String paev = i.next();

		// JSONis on tyhjade p2evade puhul tyhi array [],
		// kui p2ev pole tyhi, siis on temaga seotud objekt {}
		// Meid huvitab ainult see case, kus on objekt ehk JSONObject
		JSONObject kuupaevad = json.optJSONObject(paev);
		// Kui polnud JSONObject, siis saime nulli
		if (null != kuupaevad) {
		    @SuppressWarnings("unchecked")
		    Iterator<String> j = kuupaevad.keys();
		    while (j.hasNext()) {
			String kuupaev = j.next();

			JSONArray kattuvused = kuupaevad.getJSONArray(kuupaev);
			for (int n = 0; n < kattuvused.length(); n++) {
			    JSONObject kellaajad = (JSONObject) kattuvused
				    .get(n);

			    @SuppressWarnings("unchecked")
			    Iterator<String> k = kellaajad.keys();
			    while (k.hasNext()) {
				String kellaaeg = k.next();

				JSONObject eventData = kellaajad
					.getJSONObject(kellaaeg);

				Log.d("requestComplete", //$NON-NLS-1$
					"data: " + eventData.toString()); //$NON-NLS-1$
			    }
			}
		    }
		}
	    }

	    // Event event = new Event();
	    // event.setStartDate(results.getJSONObject(i).getString("startDate"));

	    // String endDate = timetableJson.getString("endDate");
	    // String subject = timetableJson.getString("subject");
	    // String location = timetableJson.getString("location");
	    // String subjectID = timetableJson.getString("Ainekood");
	    // String lecturer =
	    // timetableJson.getString("\u00d5ppej\u00f5ud");
	    // String subjectType =
	    // timetableJson.getString("T\u00fc\u00fcp");
	    // String weekday = timetableJson.getString("weekday");

	    Log.d("requestComplete", "------------"); //$NON-NLS-1$ //$NON-NLS-2$

	    // Log.d("requestComplete", "endDate: " + endDate );
	    // Log.d("requestComplete", "subject: " + subject );
	    // Log.d("requestComplete", "location: " + location);

	    // event.setStartDate(endDate);
	    // event.setStartDate(subject);
	    // event.setStartDate(location);
	    // event.setStartDate(subjectID);
	    // event.setStartDate(lecturer);
	    // event.setStartDate(subjectType);
	    // event.setStartDate(weekday);
	    // events.add(event);

	} catch (JSONException e) {
	    e.printStackTrace();
	}
    }

    private class GetUrlContents extends AsyncTask<URL, Void, String> {

	@Override
	protected String doInBackground(URL... params) {
	    StringBuilder sb = new StringBuilder();

	    try {

		HttpURLConnection connection = (HttpURLConnection) params[0]
			.openConnection();
		BufferedReader reader = new BufferedReader(
			new InputStreamReader(connection.getInputStream()));

		String line;
		Log.d("GetUrlContents", "@GetUrlContents TRY"); //$NON-NLS-1$ //$NON-NLS-2$
		while ((line = reader.readLine()) != null) {
		    sb.append(line);
		}
		reader.close();
		connection.disconnect();

	    } catch (IOException e) {
		e.printStackTrace();
	    }
	    return sb.toString();
	}

	@Override
	protected void onPostExecute(String result) {
	    super.onPostExecute(result);
	    Log.d("onPostExecute", "@result: " + result); //$NON-NLS-1$ //$NON-NLS-2$
	    requestComplete(result);
	}
    }

}
