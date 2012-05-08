package ee.itcollege.android.schedule;


import android.app.Activity;
import android.os.Bundle;

	
public class ScheduleViewerActivity extends Activity {
	
	double ID = 2224;
	String url ="https://itcollege.ois.ee/schedule?&format=json&student_id=2224";
	
	
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);	
	}
}