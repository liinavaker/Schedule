package ee.itcollege.android.schedule;


import android.app.Activity;

import android.os.Bundle;

public class ScheduleViewerActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		
		new FileManager().DownloadFromUrl("https://itcollege.ois.ee/et/schedule/ical?Submit=&student_id=2224", "failinimi");
		
		
	}
	


}