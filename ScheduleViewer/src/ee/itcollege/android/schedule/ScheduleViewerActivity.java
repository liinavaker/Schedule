package ee.itcollege.android.schedule;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class ScheduleViewerActivity extends FragmentActivity {
	public static Context context;
	public String showtext_current;

	// public String showtext_previous = EventListFragment.showtext_previous;
	// public String showtext_next = EventListFragment.showtext_next;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		int vahe = 0; // erinevus tänasest päevast
		showtext_current = EventListFragment.getDate(vahe).toString();
		EventListFragment.showtext_current = showtext_current;
		Log.d("ScheduleViewerActivity", "TODAYshowtext_current: "
				+ showtext_current);

		setContentView(R.layout.main);

		TextView currently_shown_schedule;
		currently_shown_schedule = (TextView) findViewById(R.id.currently_shown_schedule);
		context = currently_shown_schedule.getContext();
		EventListFragment.context = context;
		String estonianDate = parseDateIntoEstonian(showtext_current);
		currently_shown_schedule.setText(estonianDate);
	}
	
	public String parseDateIntoEstonian(String date) {
		String today = checkIfToday(date);
		String[] tokens = date.split("-");
		int yyyy = Integer.parseInt(tokens[0]);
		int mm = Integer.parseInt(tokens[1]);
		int dd = Integer.parseInt(tokens[2]);
		String[] kuud = { "jaan.", "veebr.", "märts", "aprill", "mai", "juuni", "juuli", "august", "sept.", "okt.", "nov.", "dets."};
		String kuu ="";
		for(int i = 0; i < kuud.length; i++) {
			if (mm-1 == i) {
				kuu = kuud[i];
			}
		}
		String[] daytokens = Integer.toString(dd).split("");
		String dayWithoutZero = "";
		if(daytokens[0] == "0"){
			dayWithoutZero = daytokens[1];
		} else {
			dayWithoutZero = Integer.toString(dd);
		}	
		String estonianDate = "";
		if(today != ""){
			estonianDate = today; 
		} else {
			estonianDate = dayWithoutZero + ". " + kuu + " " + Integer.toString(yyyy); 	
		}
		
		return estonianDate;
	}
	
	public String checkIfToday(String date){
		String expectedPattern = "yyyy-MM-dd";
	    SimpleDateFormat formatter = new SimpleDateFormat(expectedPattern);
		Date datecurrentlyshown = null;
		try {
			datecurrentlyshown = formatter.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String yesterday_string = EventListFragment.getDate(-1).toString(); 
		String today_string = EventListFragment.getDate(0).toString();
		String tomorrow_string = EventListFragment.getDate(+1).toString(); 
		String ifclose = "";
		Date yesterday = null;
		try {
			yesterday = formatter.parse(yesterday_string);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Date today = null;
		try {
			today = formatter.parse(today_string);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Date tomorrow = null;
		try {
			tomorrow = formatter.parse(tomorrow_string);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Log.d("ScheduleViewerActivity", "datecurrentlyshown: " +datecurrentlyshown);
		Log.d("ScheduleViewerActivity", "yesterday: " +yesterday);
		Log.d("ScheduleViewerActivity", "today: " +today);
		Log.d("ScheduleViewerActivity", "tomorrow: " +tomorrow);
		if(datecurrentlyshown.equals(yesterday)){
			ifclose = "Eile";
		} if(datecurrentlyshown.equals(today)) {
			ifclose = "Täna";
		} if(datecurrentlyshown.equals(tomorrow)) {
			ifclose = "Homme";
		} 
		Log.d("ScheduleViewerActivity", "ifclose: " +ifclose);
		return ifclose;
	}
	
	
	public void onNextDateClicked(View view) {
		EventListFragment.onNextDateClicked = true;
		EventListFragment fragment = (EventListFragment) getSupportFragmentManager()
				.findFragmentById(R.id.fragment_event_list);
		EventListFragment.getNextDateSchedule();
		fragment.onAttach(getParent());

		TextView currently_shown_schedule;
		currently_shown_schedule = (TextView) findViewById(R.id.currently_shown_schedule);
		String estonianDate = parseDateIntoEstonian(EventListFragment.showtext_current);
		currently_shown_schedule.setText(estonianDate);
	}

	public void onPreviousDateClicked(View view) {
		EventListFragment.onNextDateClicked = false;
		EventListFragment fragment = (EventListFragment) getSupportFragmentManager()
				.findFragmentById(R.id.fragment_event_list);
		EventListFragment.getPreviousDateSchedule();
		fragment.onAttach(getParent());

		TextView currently_shown_schedule;
		currently_shown_schedule = (TextView) findViewById(R.id.currently_shown_schedule);
		String estonianDate = parseDateIntoEstonian(EventListFragment.showtext_current);
		currently_shown_schedule.setText(estonianDate);
	}

	/*
	 * static String meetodMidaTahadValjaKutsuda(String
	 * numericalStudentIDstring) { Object studentID = numericalStudentIDstring;
	 * String ID = studentID.toString(); // TODO: add try parse in case user //
	 * enters something else instead on // numerical ID String URI =
	 * ("https://itcollege.ois.ee/et/schedule?&format=json&student_id=" + ID);
	 * //$NON-NLS-1$ // creating hardwired JSON data URL. not nice. return URI;
	 * // TODO Auto-generated method stub
	 * 
	 * }
	 */
}