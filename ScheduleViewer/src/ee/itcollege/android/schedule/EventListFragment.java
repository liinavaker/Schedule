package ee.itcollege.android.schedule;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v4.app.ListFragment;
import android.util.Log;

public class EventListFragment extends ListFragment {
	private ArrayList<Event> events = new ArrayList<Event>();

	public static String userID = "1679";
	// public int aasta;
	// public int kuu;
	// public int kuupaev;
	public int paev1;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		
		
		// Mis kuupäev oli eile? formaat yyyy-mm-dd
		// Get today as a Calendar  
		Calendar today = Calendar.getInstance();  
		// Subtract 1 day  
		today.add(Calendar.DATE, 0);  
		// Make an SQL Date out of that  
		java.sql.Date yesterday = new java.sql.Date(today.getTimeInMillis()); 
		
		Log.d("EventListFragment", "yesterday: " +yesterday);
		
		
		// Mis päev täna on?
		Calendar cal = Calendar.getInstance();
		// aasta = cal.get(Calendar.YEAR);
		// kuu = cal.get(Calendar.MONTH);
		// kuupaev = cal.get(Calendar.DATE);

		paev1 = cal.get(Calendar.DAY_OF_WEEK);
		
	//	// teisendan ümber formaadiks, kus nädal algab esmaspäevaga
		if (paev1 == 1) {
			paev1 = 7;
		} else 
			paev1 = paev1 -1;

		// Log.d("EventListFragment", "year: " + aasta);
		// Log.d("EventListFragment", "kuu: " + kuu);
		// Log.d("EventListFragment", "kuupaev: " + kuupaev);

		getSearchResults(userID);
	}

	public void getSearchResults(String userID) {
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
		Log.d("EventListFragment", "requestComplete()");
		// events.clear();
		try {
			JSONObject json = new JSONObject(result);

			@SuppressWarnings("unchecked")
			Iterator<String> i = json.keys();
			while (i.hasNext()) {
				String paev = i.next();

				// JSONis on tühjade päevade puhul tühi array [],
				// kui päev pole tühi, siis on temaga seotud objekt {}
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
								JSONObject atributes = eventData
										.getJSONObject("atributes");
								JSONObject description = eventData
										.getJSONObject("description");


								String weekday = atributes.getString("weekday");
								
								if (weekday.equals(Integer.toString(paev1))) {
									Event event = new Event();
									event.setWeekday(weekday);
									
									Log.d("requestComplete", "------------");
									Log.d("requestComplete", "weekday: " + weekday);
										
									String startDate = eventData
											.getString("startDate");
									event.setStartDate(startDate);
									Log.d("requestComplete", "startDate: "
											+ startDate);

									String endDate = eventData.getString("endDate");
									event.setEndDate(endDate);
									Log.d("requestComplete", "endDate: " + endDate);

									String Date = description.getString("Aeg");
									event.setDate(Date);
									Log.d("requestComplete", "Date: " + Date);

									String subjectType = description
											.getString("Tüüp");
									event.setSubjectType(subjectType);
									Log.d("requestComplete", "subjectType: "
											+ subjectType);

									String location = eventData
											.getString("location");
									event.setLocation(location);
									Log.d("requestComplete", "location: "
											+ location);

									String lecturer = description
											.getString("Õppejõud");
									event.setLecturer(lecturer);
									Log.d("requestComplete", "lecturer: "
											+ lecturer);

									// String timePeriod = description
									// .getString("Periood");
									// event.setTimePeriod(timePeriod);
									// Log.d("requestComplete", "timePeriod: "
									// + timePeriod);

									String frequency = description
											.getString("Sagedus");
									event.setFrequency(frequency);
									Log.d("requestComplete", "frequency: "
											+ frequency);

									String subject = eventData.getString("subject");
									event.setSubject(subject);
									Log.d("requestComplete", "subject: " + subject);

									Log.d("requestComplete", "------------");

									events.add(event);

									// kontrollin, kas events array-sse on
									// lisandunud uus event.
									Log.d("requestComplete",
											"events Array suurus: " + events.size());

								}
								
															}
						}
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		EventAdapter adapter = new EventAdapter(getActivity());
		adapter.setEvents(events);
		setListAdapter(adapter);
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
