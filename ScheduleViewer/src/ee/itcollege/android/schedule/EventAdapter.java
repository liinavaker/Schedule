package ee.itcollege.android.schedule;

import java.util.List;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public 	class EventAdapter extends BaseAdapter {

	private Context context;
	private List<Event> events;
		
	public EventAdapter (Context context) {
		this.context = context;
	}
	
	public void setEvents(List <Event> events) {
		this.events = events;
		notifyDataSetChanged();
	}
	
	public int getCount() {
		return events.size();
	}

	public Object getItem(int position) {
		return events.get(position);
	}

	public long getItemId(int position) {
		return position;
	}
	
	public boolean isEnabled(int position) {
		return false;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View view = View.inflate(context, R.layout.event, null);
		TextView name = (TextView) view.findViewById(R.id.event_name);
		TextView date = (TextView) view.findViewById(R.id.event_date);
		TextView details = (TextView) view.findViewById(R.id.event_details);
		TextView location = (TextView) view.findViewById(R.id.event_location);
		
		Event event = events.get(position);
		name.setText(event.getSubject());
		date.setText(event.getDate());
		details.setText(event.getLecturer());
		location.setText(event.getLocation());
		
		return view;
	}
}
