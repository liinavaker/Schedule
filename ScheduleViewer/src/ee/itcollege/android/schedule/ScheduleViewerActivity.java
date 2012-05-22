package ee.itcollege.android.schedule;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class ScheduleViewerActivity extends FragmentActivity {
	public static Context context;
	public String showtext_current;
	public static TextView showTextNoEvents;
	public static Boolean firstTime = true;
	File sdcard = Environment.getExternalStorageDirectory();
	File dir = new File(sdcard.getAbsolutePath() + "/Schedule");
	private static final int REFRESH_CURRENT_DAY_MENU_ITEM = Menu.FIRST;
	private static final int REFRESH_ALL_MENU_ITEM = REFRESH_CURRENT_DAY_MENU_ITEM + 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		int vahe = 0; // erinevus tänasest päevast
		showtext_current = EventListFragment.getDate(vahe).toString();
		EventListFragment.showtext_current = showtext_current;
		
	//	gestureDetector = new GestureDetector(new SwipeGestureDetector());

		setContentView(R.layout.main);
		showTextNoEvents = (TextView) findViewById(R.id.no_events);
		TextView currently_shown_schedule;
		currently_shown_schedule = (TextView) findViewById(R.id.currently_shown_schedule);
		context = currently_shown_schedule.getContext();
		EventListFragment.context = context;
		String estonianDate = parseDateIntoEstonian(showtext_current);
		currently_shown_schedule.setText(estonianDate);
		
		TextView weekday = (TextView) findViewById(R.id.weekday);
		String weekdayString = getWeekday(showtext_current);
		weekday.setText(weekdayString);
		
		showNoEvents();
		firstTime = false;
	}

	// TODO: onLeftSwipe ja onRightSwipe näitavad vastavalt eelmise ja järgmine päeva tunniplaani. 
/*	  private void onLeftSwipe() {
		  Log.e("ScheduleViewerActivity", "onLeftSwipe");
		  onPreviousDateClicked(null);
		  }
	  private void onRightSwipe() {
		  Log.e("ScheduleViewerActivity", "onRightSwipe");
		  onNextDateClicked(null);
		  }
	  
	  private class SwipeGestureDetector 
	          extends SimpleOnGestureListener {
	    // Swipe properties, you can change it to make the swipe 
	    // longer or shorter and speed
	    private static final int SWIPE_MIN_DISTANCE = 120;
	    private static final int SWIPE_MAX_OFF_PATH = 200;
	    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

	    @Override
	    public boolean onFling(MotionEvent e1, MotionEvent e2,
	                         float velocityX, float velocityY) {
	      try {
	        float diffAbs = Math.abs(e1.getY() - e2.getY());
	        float diff = e1.getX() - e2.getX();

	        if (diffAbs > SWIPE_MAX_OFF_PATH)
	          return false;
	        
	        // Left swipe
	        if (diff > SWIPE_MIN_DISTANCE
	        && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
	           ScheduleViewerActivity.this.onLeftSwipe();

	        // Right swipe
	        } else if (-diff > SWIPE_MIN_DISTANCE
	        && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
	        	ScheduleViewerActivity.this.onRightSwipe();
	        }
	      } catch (Exception e) {
	        Log.e("ScheduleViewerActivity", "Error on gestures");
	      }
	      return false;
	    }
	  }
*/	
	
	  public static void showNoEvents() {
		if(EventListFragment.eventsEmpty) {
			showTextNoEvents.setText("Pole ühtegi sündmust");
			showTextNoEvents.setVisibility(View.VISIBLE);
		} if (!EventListFragment.eventsEmpty) {
			showTextNoEvents.setVisibility(View.GONE);
		}
	}
	
	public String getWeekday (String current_date) {
		int weekdayInNum = EventListFragment.getDayOfWeekFromDatetoString(EventListFragment.showtext_current);
		String[] weekdays ={"pühapäev","esmaspäev", "teisipäev", "kolmapäev", "neljapäev", "reede", "laupäev", "pühapäev"};
		String weekday = "";
		Log.d("ScheduleViewerActivity", "weekdayInNum: "
				+ weekdayInNum);
		for(int i = 0; i < weekdays.length; i++) {
			if(weekdayInNum == i){
				weekday = weekdays[i];
			}
		}
		Log.d("ScheduleViewerActivity", "weekday: "
				+ weekday);
		return weekday;
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
			e.printStackTrace();
		}
		Date today = null;
		try {
			today = formatter.parse(today_string);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Date tomorrow = null;
		try {
			tomorrow = formatter.parse(tomorrow_string);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		if(datecurrentlyshown.equals(yesterday)){
			ifclose = "eile";
		} if(datecurrentlyshown.equals(today)) {
			ifclose = "täna";
		} if(datecurrentlyshown.equals(tomorrow)) {
			ifclose = "homme";
		} 

		return ifclose;
	}
	
	public void onNextDateClicked(View view) {
		EventListFragment.onNextDateClicked = true;
		EventListFragment fragment = (EventListFragment) getSupportFragmentManager()
				.findFragmentById(R.id.fragment_event_list);
		EventListFragment.getNextDateSchedule();
		fragment.onAttach(getParent());
				
		TextView currently_shown_schedule = (TextView) findViewById(R.id.currently_shown_schedule);
		String estonianDate = parseDateIntoEstonian(EventListFragment.showtext_current);
		String date = EventListFragment.showtext_current;
		currently_shown_schedule.setText(estonianDate);
		
		TextView weekday = (TextView) findViewById(R.id.weekday);
		String weekdayString = getWeekday(date);
		weekday.setText(weekdayString);

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
		String date = EventListFragment.showtext_current;
		currently_shown_schedule.setText(estonianDate);		
		
		TextView weekday = (TextView) findViewById(R.id.weekday);
		String weekdayString = getWeekday(date);
		weekday.setText(weekdayString);
	}
	
	public boolean onCreateOptionsMenu(Menu menu){
		
		menu.add(0, REFRESH_CURRENT_DAY_MENU_ITEM, 0, "Värskenda käesoleva päeva sündmuseid");
		menu.add(0, REFRESH_ALL_MENU_ITEM, 0, "Värskenda kõiki sündmuseid");
		
		return super.onCreateOptionsMenu(menu);
		}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.d("ScheduleViewerActivity", "onOptionsItemSelected");
		switch (item.getItemId()) {
		case REFRESH_CURRENT_DAY_MENU_ITEM:
			Log.d("ScheduleViewerActivity", "case REFRESH_MENU_ITEM");
			break;
		case REFRESH_ALL_MENU_ITEM:
			Log.d("ScheduleViewerActivity", "case REFRESH_ALL_MENU_ITEM");
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/*
	public boolean onOptionsItemSelected (MenuItem item) {
		File[] filelist = dir.listFiles();
		Log.d("ScheduleViewerActivity", "onOptionsItemSelected");
		//userID + "_" + showtext_current + ".json";
		
		for(int i = 0; i < filelist.length; i++) {
			if(filelist[i].toString().startsWith(EventListFragment.userID)) {
				Log.d("ScheduleViewerActivity", "Starts with " + EventListFragment.userID + " - " + filelist[i].toString());
			}		
		}
	
		switch (item.getItemId()) {
		case R.id.refresh:
			
			break;
		}
		return super.onOptionsItemSelected(item);
		
	}
	*/
}