package ee.itcollege.android.schedule;

import android.app.Activity;
import android.os.Bundle;

/**
 * @author Heiki
 *
 */
public class ScheduleViewerActivity extends Activity {
    /**
     * Called when the activity is first created.
     * 
     * @param savedInstanceState
     * @param URI
     */
    public void onCreate(Bundle savedInstanceState, String URI) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.main);

	/* new FileManager()
		.DownloadFromUrl(
			"https://itcollege.ois.ee/et/schedule?&format=json&student_id=1679", "failinimi"); //$NON-NLS-1$
*/
    }

}