package ee.itcollege.android.schedule;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

public class EventListFragment extends ListFragment {
	public static ArrayList<Event> events = new ArrayList<Event>();


	public static String userID = "";
	// public int aasta;
	// public int kuu;
	// public int kuupaev;
	public static String showtext_current;
	public static String showtext_previous;
	public static String showtext_next;
	public static boolean onNextDateClicked = false;
	public static int dayOfWeek; // valitud kuupäeva nädalapäev. Nt 2012-05-18
									// -> 5 (ehk reede)
	public static Activity activity;
	public static Context context = ScheduleViewerActivity.context;
	public static boolean eventsEmpty = false;
	public int getWeekdayOfToday() {
		// tänane nädalapäev numbrites
		Calendar cal = Calendar.getInstance();
		int today = cal.get(Calendar.DAY_OF_WEEK);

		if (today == 0) {
			today = 7;
		} else
			today = today - 1;
		return today;
	}

	public static int getDayOfWeekFromDatetoString(String date) {

		// Tükeldan Date-tüüpi kuupäeva (yyyy-mm-dd) ära eraldi kolmeks
		// stringiks.
		String[] tokens = date.split("-");
		int yyyy = Integer.parseInt(tokens[0]);
		String mm = tokens[1];
		int dd = Integer.parseInt(tokens[2]);

		Calendar calendar = new GregorianCalendar(yyyy,
				Integer.parseInt(mm) - 1, dd);
		int day = calendar.get(Calendar.DAY_OF_WEEK);
		int dayParsed = day;
		
		if (Integer.toString(day).equals("0")) {
			dayParsed = 7;
		} else
			dayParsed = day - 1;
		if(dayParsed == 0) {
			dayParsed = 7;
		}

		Log.d("EventListFragment", "dayParsed: " + dayParsed);

		return dayParsed;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		eventsEmpty = false;
		
		int testday = getDayOfWeekFromDatetoString(showtext_current);
		// Tükeldan Date-tüüpi kuupäeva (yyyy-mm-dd) ära eraldi kolmeks
		// stringiks.
		dayOfWeek = testday;
		getSearchResults();
	} 

	public static void getPreviousDateSchedule() {
		String currentDate = showtext_current;
		String[] tokens = currentDate.split("-");
		int yyyy = Integer.parseInt(tokens[0]);
		String mm = tokens[1];
		int dd = Integer.parseInt(tokens[2]);

		Calendar cal = new GregorianCalendar(yyyy, Integer.parseInt(mm) - 1, dd);
		cal.add(Calendar.DATE, -1);
		// Make an SQL Date out of that
		java.sql.Date previousDate = new java.sql.Date(cal.getTimeInMillis());
		Log.d("EventListFragment", "getDate tulemus: " + previousDate);
		showtext_current = previousDate.toString();
	}

	public static void getNextDateSchedule() {
		Log.d("EventListFragment", "TextView showTextNoEvents: " +ScheduleViewerActivity.showTextNoEvents);
		String currentDate = showtext_current;
		String[] tokens = currentDate.split("-");
		int yyyy = Integer.parseInt(tokens[0]);
		String mm = tokens[1];
		int dd = Integer.parseInt(tokens[2]);

		Calendar cal = new GregorianCalendar(yyyy, Integer.parseInt(mm) - 1, dd);
		cal.add(Calendar.DATE, +1);
		// Make an SQL Date out of that
		java.sql.Date nextDate = new java.sql.Date(cal.getTimeInMillis());
		Log.d("EventListFragment", "getDate tulemus: " + nextDate);
		showtext_current = nextDate.toString();
	}

	public static Date getDate(int vahe) {
		// Get today as a Calendar
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, vahe);
		// Make an SQL Date out of that
		java.sql.Date date = new java.sql.Date(cal.getTimeInMillis());
		Log.d("EventListFragment", "getDate tulemus: " + date);
		return date;
	}

	private boolean fileExists() {
		Log.d("EventListFragment", "fileExists()");
		File sdcard = Environment.getExternalStorageDirectory();
		File dir = new File(sdcard.getAbsolutePath() + "/Schedule");
		Log.d("EventListFragment", "dir: " +dir);
		
		File file = new File(dir, filename());
		Log.d("EventListFragment", "file: " +file);
		Log.d("EventListFragment", "fileExists: " +file.exists());
		return file.exists();
	}

	private String filename() {
		return userID + "_" + showtext_current + ".json";
	}

	private void writeToSDCard(String result) throws IOException {
		if ((null == result) || (0 == result.length())) {
			return; // will not write empty file
		}
		
		Log.d("EventListFragment", "writeToSDCard: result: " + result);
		File sdcard = Environment.getExternalStorageDirectory();
		File dir = new File(sdcard.getAbsolutePath() + "/Schedule");
		dir.mkdirs();

		// Get the text file
		File file = new File(dir, filename());
		BufferedWriter br = null;
		try {
			br = new BufferedWriter(new FileWriter(file));
			br.write(result);
			br.flush();
		} finally {
			if (null != br)
				br.close();
		}
	}

	private String readFile() throws IOException {
		File sdcard = Environment.getExternalStorageDirectory();
		File dir = new File(sdcard.getAbsolutePath() + "/Schedule");
		File file = new File(dir, filename());
		BufferedReader br = new BufferedReader(new FileReader(file));
		return br.readLine();
	}

	public void getSearchResults() {
		if (!fileExists()) {
			downloadFile();
		} else {
			try {
				String result = readFile();
				showEventsFromJSON(result);
			} catch(Exception e) {
			//	 showAlert("Error", e.getMessage());
			}
		}
	}

	public static void showAlert(String title, String message) {
		AlertDialog dialog = new AlertDialog.Builder(context).create();
		dialog.setTitle(title);
		dialog.setMessage(message);
		dialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				return;
			}
		});
		dialog.show();
	}

	private void downloadFile() {
		Log.d("EventListFragment", "downloadFile");
		Log.d("EventListFragment", "UserID: " + userID);
		try {
			URL url = new URL(
					"https://itcollege.ois.ee/schedule?&format=json&student_id="
							+ URLEncoder.encode(userID) + "&date="
							+ URLEncoder.encode(showtext_current));
			new GetUrlContents().execute(url);
			Log.d("getUserTimetable", "url: " + url);
		} catch (Exception e) {
	//		showAlert("Error", e.getMessage());
		}
	}
	
	public static JSONObject parseJSON(String result) {
		JSONObject json = null;
		if(result.length() == 0){
			Log.d("EventListFragment", "parseJSON: result.length() == 0 ");			
			return json;
		}
			
		try {
			Log.d("EventListFragment", "parseJSON: TRY");
			json = new JSONObject(result);
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
								Log.d("EventListFragment", "weekday: "
										+ weekday);
								Log.d("EventListFragment", "dayOfWeek: "
										+ dayOfWeek);
								if (weekday.equals(Integer.toString(dayOfWeek))) {
									Event event = new Event();
									event.setWeekday(weekday);

									Log.d("requestComplete", "------------");
									Log.d("requestComplete", "weekday: "
											+ weekday);

									String startDate = eventData
											.getString("startDate");
									event.setStartDate(startDate);
									Log.d("requestComplete", "startDate: "
											+ startDate);

									String endDate = eventData
											.getString("endDate");
									event.setEndDate(endDate);
									Log.d("requestComplete", "endDate: "
											+ endDate);

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

									String subject = eventData
											.getString("subject");
									event.setSubject(subject);
									Log.d("requestComplete", "subject: "
											+ subject);

									Log.d("requestComplete", "------------");

									events.add(event);

									// kontrollin, kas events array-sse on
									// lisandunud uus event.
									Log.d("requestComplete",
											"events Array suurus: "
													+ events.size());

								}
							} 
						}
					}
				}
			}
			if(events.size() == 0) {
				eventsEmpty = true;
				Log.d("EventListFragment", "FirstTime: " +ScheduleViewerActivity.firstTime);
				if(ScheduleViewerActivity.firstTime == false) {
				ScheduleViewerActivity.showNoEvents();
				Log.d("EventListFragment", "IF SEES ||FirstTime: " +ScheduleViewerActivity.firstTime);
				}
			} if (events.size() != 0) {
				Log.d("EventListFragment", "FIRST events.size() != 0");
				eventsEmpty = false;
				if(ScheduleViewerActivity.firstTime == false) { 
				Log.d("EventListFragment", "IF SEES ||FirstTime: " +ScheduleViewerActivity.firstTime);
				ScheduleViewerActivity.showNoEvents();
				}
			}
			
		} catch (JSONException e) {
//			showAlert("Error", e.getMessage());
		}
		return json;
	}

	private void requestComplete(String result) {
		Log.d("EventListFragment", "requestComplete()");
		try {
			writeToSDCard(result);
		} catch (IOException e) {
//			showAlert("Error", e.getMessage());
		}
		showEventsFromJSON(result);
	}

	private void showEventsFromJSON(String result) {
		Log.d("EventListFragment", "Test: showEventsFromJSON");
		Log.d("EventListFragment", "Array events suurus: " +events.size());
		events.clear();
		parseJSON(result);
		EventAdapter adapter = new EventAdapter(getActivity());
		compareEvents();
		adapter.setEvents(events);
		setListAdapter(adapter);
	}

	public static void compareEvents() {
		Collections.sort(events, new Comparator<Event>() {
			public int compare(Event s1, Event s2) {
				return s1.getStartDate().compareToIgnoreCase(s2.getStartDate());
			}
		});		
	}

	private class GetUrlContents extends AsyncTask<URL, Void, String> {
		
		@Override
		protected String doInBackground(URL... params) {
			Log.d("EventListFragment", "GetUrlContents");
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
//				showAlert("Error", e.getMessage());
			}
			return sb.toString();
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if(result.length() == 0) {
				showAlert("Ühenduse viga", "Tunniplaani kuvamine ebaõnnestus. Palun kontrolli seadme Interneti ühendust.");
				if(onNextDateClicked) {
				getPreviousDateSchedule();
				} else {
					getNextDateSchedule();
				}
					
			}
			Log.d("EventListFragment", "onPostExecute @result: " + result);
			requestComplete(result);
		}
	}
}
