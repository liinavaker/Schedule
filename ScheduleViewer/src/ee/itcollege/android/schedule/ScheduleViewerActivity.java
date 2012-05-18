package ee.itcollege.android.schedule;


import java.sql.Date;
import java.util.Calendar;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

public class ScheduleViewerActivity extends FragmentActivity {
	


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.main);
	TextView currently_shown_schedule;
	
	int vahe = 0; //erinevus tänasest päevast
	
	currently_shown_schedule = (TextView) findViewById(R.id.currently_shown_schedule);
	String showtext_current = getDate(vahe).toString();
	currently_shown_schedule.setText(showtext_current);

	Button previousDate = (Button) findViewById(R.id.previousDate);
	String showtext_previous = getDate(-1).toString();
	previousDate.setText(showtext_previous);
	
	Button nextDate = (Button) findViewById(R.id.nextDate);
	String showtext_next = getDate(+1).toString();
	nextDate.setText(showtext_next);
		
    }
    
    public Date getDate(int vahe) {
    	// Get today as a Calendar  
		Calendar today = Calendar.getInstance();  
		// Subtract 1 day  
		today.add(Calendar.DATE, vahe);  
		// Make an SQL Date out of that  
		java.sql.Date date = new java.sql.Date(today.getTimeInMillis()); 
    	
		return date;
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