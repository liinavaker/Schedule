package ee.itcollege.android.schedule;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ScheduleViewerActivity extends FragmentActivity {
	public static Context context;
	public static String showtext_current;
	public static TextView showTextNoEvents;
	public static Boolean firstTime = true;
	File sdcard = Environment.getExternalStorageDirectory();
	File dir = new File(sdcard.getAbsolutePath() + "/Schedule");
	private static final int REFRESH_CURRENT_DAY_MENU_ITEM = Menu.FIRST;
	private static final int REFRESH_ALL_MENU_ITEM = REFRESH_CURRENT_DAY_MENU_ITEM + 1;
	
	public static boolean refresh = false;
	public static boolean deleteAll = false; 
	public static String resultToShowEventsFromJSON = null;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		int vahe = 0; // erinevus tänasest päevast
		showtext_current = getDate(vahe).toString();
		
	//	gestureDetector = new GestureDetector(new SwipeGestureDetector());

		setContentView(R.layout.main);
		showTextNoEvents = (TextView) findViewById(R.id.no_events);
		TextView currently_shown_schedule;
		currently_shown_schedule = (TextView) findViewById(R.id.currently_shown_schedule);
		context = currently_shown_schedule.getContext();
		String estonianDate = parseDateIntoEstonian(showtext_current);
		currently_shown_schedule.setText(estonianDate);
		
	
		TextView weekday = (TextView) findViewById(R.id.weekday);
		String weekdayString = getWeekday(showtext_current);
		weekday.setText(weekdayString);
		
		showNoEvents();
		firstTime = false;
		
		LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
			      new IntentFilter("refresh_done"));
	}
	
	private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
		  @Override
		  public void onReceive(Context context, Intent intent) {
		    // Get extra data included in the Intent
		    String message = intent.getStringExtra("message");
		    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
		    Log.d("receiver", "Got message: " + message);
		  }
		};

		@Override
		protected void onDestroy() {
		  // Unregister since the activity is about to be closed.
		  LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
		  super.onDestroy();
		}
	
	public Date getDate(int vahe) {
		// Get today as a Calendar
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, vahe);
		// Make an SQL Date out of that
		java.sql.Date date = new java.sql.Date(cal.getTimeInMillis());
		return date;
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
	
	public void showNoEvents() {
		EventListFragment fragment = getFragment();
		if(fragment.eventsEmpty) {
			showTextNoEvents.setText("Pole ühtegi sündmust");
			showTextNoEvents.setVisibility(View.VISIBLE);
			Log.d("ScheduleViewActivity", "eventsEmtpy: true");
			
		} if (!fragment.eventsEmpty) {
			showTextNoEvents.setVisibility(View.GONE);
			Log.d("ScheduleViewActivity", "eventsEmtpy: false");
		}
		fragment.eventsEmpty = false;
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
		if (dayParsed == 0) {
			dayParsed = 7;
		}

		Log.d("EventListFragment", "dayParsed: " + dayParsed);

		return dayParsed;
	}
	
	public void sendMessage() {
		  Log.d("sender", "Broadcasting message");
		  Intent intent = new Intent("refresh_done");
		  // You can also include some extra data.
		  intent.putExtra("message", "Sündmused on uuendatud");
		  LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
		}
	
	
	public String getWeekday (String current_date) {
		int weekdayInNum = getDayOfWeekFromDatetoString(showtext_current);
		String[] weekdays ={"pühapäev","esmaspäev", "teisipäev", "kolmapäev", "neljapäev", "reede", "laupäev", "pühapäev"};
		Log.d("ScheduleViewerActivity", "weekdayInNum: "
				+ weekdayInNum);
		return weekdays[weekdayInNum];
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
		
		String yesterday_string = getDate(-1).toString(); 
		String today_string = getDate(0).toString();
		String tomorrow_string = getDate(+1).toString(); 
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
	
	private EventListFragment getFragment() {
		return (EventListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_event_list);
	}
	
	public void onNextDateClicked(View view) {
		EventListFragment fragment = this.getFragment();
		fragment.onNextDateClicked = true;
		fragment.getNextDateSchedule();
		fragment.onAttach(getParent());
				
		TextView currently_shown_schedule = (TextView) findViewById(R.id.currently_shown_schedule);
		String estonianDate = parseDateIntoEstonian(showtext_current);
		String date = showtext_current;
		currently_shown_schedule.setText(estonianDate);
		
		TextView weekday = (TextView) findViewById(R.id.weekday);
		String weekdayString = getWeekday(date);
		weekday.setText(weekdayString);

	}

	public void onPreviousDateClicked(View view) {
		EventListFragment fragment = this.getFragment();
		fragment.onNextDateClicked = false;
		fragment.getPreviousDateSchedule();
		fragment.onAttach(getParent());
		
		TextView currently_shown_schedule;
		currently_shown_schedule = (TextView) findViewById(R.id.currently_shown_schedule);
		String estonianDate = parseDateIntoEstonian(showtext_current);
		String date = showtext_current;
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
		EventListFragment fragment = getFragment();
		refresh = true;
		Log.d("ScheduleViewerActivity", "onOptionsItemSelected");
		switch (item.getItemId()) {
		case REFRESH_CURRENT_DAY_MENU_ITEM:
			Log.d("ScheduleViewerActivity", "case REFRESH_MENU_ITEM");
			deleteAll = false;
			fragment.getSearchResults();
			break;
		case REFRESH_ALL_MENU_ITEM:
			Log.d("ScheduleViewerActivity", "case REFRESH_ALL_MENU_ITEM");
			deleteAll = true;
			fragment.getSearchResults();
			break;
		}
		return super.onOptionsItemSelected(item);
	}		
}
	