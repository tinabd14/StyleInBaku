package styleIn.tunarmahmudov.styleIn;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.ArrayList;

public class ScheduleListAdapter extends ArrayAdapter<ParseObject> {

    private Context aContext;
    int aResource;


    public ScheduleListAdapter(Context context, int resource, ArrayList<ParseObject> objects) {
        super(context, resource, objects);
        aContext = context;
        aResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String customer = null;
        String service = null;
        String date = null;
        String hour = null;
        String status = null;

        try {
            customer = getItem(position).getParseObject("reservedCustomer").fetchIfNeeded().getString("username");
            service = getItem(position).fetchIfNeeded().getString("reservedService");
            hour = getItem(position).fetchIfNeeded().getString("hour");
            date = getItem(position).fetchIfNeeded().getString("date");
            status = getItem(position).fetchIfNeeded().getString("status");
        } catch (ParseException e) {
            e.printStackTrace();
        }


        LayoutInflater inflater = LayoutInflater.from(aContext);
        convertView = inflater.inflate(aResource, parent, false);

        TextView nameTextView = (TextView) convertView.findViewById(R.id.name2);
        TextView serviceTextView = (TextView) convertView.findViewById(R.id.service2);
        TextView hourTextView = (TextView) convertView.findViewById(R.id.hour2);
        TextView dateTextView = (TextView) convertView.findViewById(R.id.date2);
        TextView statusTextView = (TextView) convertView.findViewById(R.id.status2);

        nameTextView.setText(customer);
        serviceTextView.setText(service);
        hourTextView.setText(hour);
        dateTextView.setText(date);
        statusTextView.setText(status);

        return convertView;
    }
}

