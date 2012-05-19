package ee.itcollege.android.schedule;


import java.sql.Date;
import java.util.Calendar;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

public class ScheduleViewerActivity extends FragmentActivity {
	public static Context context;
	public String showtext_current;
//	public String showtext_previous = EventListFragment.showtext_previous;
//	public String showtext_next = EventListFragment.showtext_next;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	
	int vahe = 0; //erinevus tänasest päevast
	showtext_current = EventListFragment.getDate(vahe).toString();
	EventListFragment.showtext_current = showtext_current;
	Log.d("ScheduleViewerActivity", "TODAYshowtext_current: " +showtext_current);
	
	setContentView(R.layout.main);
	
	TextView currently_shown_schedule;
	currently_shown_schedule = (TextView) findViewById(R.id.currently_shown_schedule);
	context = currently_shown_schedule.getContext();
	EventListFragment.context = context;
	currently_shown_schedule.setText(showtext_current);

    }
    
    public void onNextDateClicked(View view) {
    	EventListFragment fragment = (EventListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_event_list);
    	EventListFragment.getNextDateSchedule();
    	fragment.onAttach(getParent());
    	
    	TextView currently_shown_schedule;
    	currently_shown_schedule = (TextView) findViewById(R.id.currently_shown_schedule);
    	currently_shown_schedule.setText(EventListFragment.showtext_current);
    }

    public void onPreviousDateClicked(View view) {
    	EventListFragment fragment = (EventListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_event_list);
    	EventListFragment.getPreviousDateSchedule();
    	fragment.onAttach(getParent());
    	
    	TextView currently_shown_schedule;
    	currently_shown_schedule = (TextView) findViewById(R.id.currently_shown_schedule);
    	currently_shown_schedule.setText(EventListFragment.showtext_current);
    }

    
 /*   
    static String meetodMidaTahadValjaKutsuda(String numericalStudentIDstring) {
	Object studentID = numericalStudentIDstring;
	String ID = studentID.toString(); // TODO: add try parse in case user
					  // enters something else instead on
					  // numerical ID
	String URI = ("https://itcollege.ois.ee/et/schedule?&format=json&student_id=" + ID); //$NON-NLS-1$
	// creating hardwired JSON data URL. not nice.
	return URI;
	// TODO Auto-generated method stub

    }
 */   
}