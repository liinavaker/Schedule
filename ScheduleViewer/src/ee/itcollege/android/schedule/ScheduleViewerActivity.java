package ee.itcollege.android.schedule;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class ScheduleViewerActivity extends FragmentActivity {


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.main);
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