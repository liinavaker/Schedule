package ee.itcollege.android.schedule;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

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

    private final ArrayList<Event> events = new ArrayList<Event>();

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

    @SuppressWarnings("nls")
    private void requestComplete(String result) {
	// events.clear();
	@SuppressWarnings("unused")
	HashMap<String, String> HashMapEvent = new HashMap<String, String>();
	try {
	    JSONObject json = new JSONObject(result);

	    @SuppressWarnings("unchecked")
	    Iterator<String> i = json.keys();
	    while (i.hasNext()) {
		String paev = i.next();

		// JSONis on tyhjade paevade puhul tyhi array [],
		// kui paev pole tyhi, siis on temaga seotud objekt {}
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
				Event event = new Event();
				String kellaaeg = k.next();
				JSONObject eventData = kellaajad
					.getJSONObject(kellaaeg);

				JSONObject atributes = eventData
					.getJSONObject("atributes");
				JSONObject description = eventData
					.getJSONObject("description");

				Log.d("requestComplete", "------------");

				String startDate = eventData
					.getString("startDate");
				event.setStartDate(startDate);
				Log.d("requestComplete", "startDate: "
					+ startDate);

				String endDate = eventData.getString("endDate");
				event.setEndDate(endDate);
				Log.d("requestComplete", "endDate: " + endDate);

				String weekday = atributes.getString("weekday");
				event.setWeekday(weekday);
				Log.d("requestComplete", "weekday: " + weekday);

				String subjectType = description
					.getString("T��p");
				event.setSubjectID(subjectType);
				Log.d("requestComplete", "subjectType: "
					+ subjectType);

				String location = eventData
					.getString("location");
				event.setLocation(location);
				Log.d("requestComplete", "location: "
					+ location);

				String lecturer = description
					.getString("�ppej�ud");
				event.setLecturer(lecturer);
				Log.d("requestComplete", "lecturer: "
					+ lecturer);
				String timePeriod = description
					.getString("Periood");
				event.setTimePeriod(timePeriod);
				Log.d("requestComplete", "timePeriod: "
					+ timePeriod);

				String frequency = description
					.getString("Sagedus");
				event.setFrequency(frequency);
				Log.d("requestComplete", "frequency: "
					+ frequency);

				String subject = eventData.getString("subject");
				event.setSubject(subject);
				Log.d("requestComplete", "subject: " + subject);
				Log.d("requestComplete", "------------");

				this.events.add(event);

				Log.d("requestComplete",
					"events Array suurus: "
						+ this.events.size());
			    }
			}
		    }
		}
	    }
	} catch (JSONException e) {
	    e.printStackTrace();
	}
    }

}
