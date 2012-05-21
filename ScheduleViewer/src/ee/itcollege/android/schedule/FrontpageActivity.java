package ee.itcollege.android.schedule;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

public class FrontpageActivity extends Activity {
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.front);
		
	}
	
	//TODO: Kui nime pole valitud, siis nuppu vajutada ei saa!
	
	public void onButtonClicked (View view) {
		EditText edittext = (EditText) findViewById(R.id.userID);
		String input = edittext.getText().toString();
		
		
		Intent intent = new Intent(this, ScheduleViewerActivity.class);
		EventListFragment.userID = input;
	
	//	intent.putExtra(EventListFragment.userID, input);
		startActivity(intent);	
	}
}
