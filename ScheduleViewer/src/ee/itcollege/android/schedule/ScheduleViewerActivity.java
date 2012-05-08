package ee.itcollege.android.schedule;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

	
public class ScheduleViewerActivity extends Activity {
	
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		getUserTimetable("2224");
	}
	
	
	public String userID = "2224";
	private ArrayList<Event> events = new ArrayList<Event>();
	
	public void getUserTimetable(String userID) {
		Log.d("getUserTimetable", "UserID: " + userID);
		
		try {
			URL url = new URL(
					"https://itcollege.ois.ee/schedule?&format=json&student_id="
							+ URLEncoder.encode(userID));
			new GetUrlContents().execute(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	private void requestComplete(String result) {
		//events.clear();
		try {
			JSONArray results = new JSONArray(result);
			
			//SIIT EI SAA ENAM EDASI. 
			
			Log.d("requestComplete", "result: " + result);
			Log.d("requestComplete", "@requestComplete TRY");
			for (int i=0; i<results.length(); i++) {
				JSONObject timetableJson = (JSONObject) results.get(i);
				Event event = new Event();
				String test = results.getJSONObject(i).getString("startDate");
				Log.d("requestComplete", "@@test: " + test);
				//event.setStartDate(results.getJSONObject(i).getString("startDate"));
				
				String startDate = timetableJson.getString("startDate");
				//String endDate = timetableJson.getString("endDate");
				//String subject = timetableJson.getString("subject");
				//String location = timetableJson.getString("location");
				//String subjectID = timetableJson.getString("Ainekood");
				//String lecturer = timetableJson.getString("\u00d5ppej\u00f5ud");
				//String subjectType = timetableJson.getString("T\u00fc\u00fcp");
				//String weekday = timetableJson.getString("weekday");

				
				Log.d("requestComplete", "------------");
				Log.d("requestComplete", "startDate: " + startDate );
				//Log.d("requestComplete", "endDate: " + endDate );
				//Log.d("requestComplete", "subject: " + subject );
				//Log.d("requestComplete", "location: " + location);
				
				
				event.setStartDate(startDate);
				//event.setStartDate(endDate);
				//event.setStartDate(subject);
				//event.setStartDate(location);
				//event.setStartDate(subjectID);
				//event.setStartDate(lecturer);
				//event.setStartDate(subjectType);
				//event.setStartDate(weekday);
				events.add(event);
				
			}
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
				Log.d("GetUrlContents", "@GetUrlContents TRY");
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
			Log.d("onPostExecute", "@result: " + result);
			requestComplete(result);
		}
	}
	
	
	
}