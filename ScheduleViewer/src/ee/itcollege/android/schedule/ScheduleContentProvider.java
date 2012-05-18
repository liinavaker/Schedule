package ee.itcollege.android.schedule;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public class ScheduleContentProvider extends ContentProvider {
	
	public static final Uri CONTENT_URI = Uri
	.parse("content://ee.itcollege.schedule/events");
	
	private static final String TABLE_NAME = "events";
	
	// tabeli veerud
	public static final String ID = "_id";
	public static final String subject = "subject";
	public static final String location = "location";
	public static final String date = "date";
	public static final String startDate = "startDate";
	public static final String endDate = "endDate";
	public static final String lecturer = "lecturer";
	public static final String subjectType = "subjectType";
	public static final String weekday = "weekday";
	

	@Override
	public int delete(Uri arg0, String arg1, String[] arg2) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType(Uri arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri arg0, ContentValues arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Cursor query(Uri arg0, String[] arg1, String arg2, String[] arg3,
			String arg4) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
		// TODO Auto-generated method stub
		return 0;
	}

}
