package styleIn.tunarmahmudov.styleIn;;

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

public class FeedbackListAdapter extends ArrayAdapter<ParseObject> {

    private Context aContext;
    int aResource;


    public FeedbackListAdapter(Context context, int resource, ArrayList<ParseObject> objects) {
        super(context, resource, objects);
        aContext = context;
        aResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String hd = null;
        String service = null;
        String date = null;
        try {
            hd = getItem(position).getParseObject("reservedOffice").fetchIfNeeded().getString("name");
            service = getItem(position).fetchIfNeeded().getString("reservedService");
            date = getItem(position).fetchIfNeeded().getString("date");
        } catch (ParseException e) {
            e.printStackTrace();
        }


        LayoutInflater inflater = LayoutInflater.from(aContext);
        convertView = inflater.inflate(aResource, parent, false);

        TextView nameTextView = (TextView) convertView.findViewById(R.id.name);
        TextView serviceTextView = (TextView) convertView.findViewById(R.id.service);
        TextView dateTextView = (TextView) convertView.findViewById(R.id.date);

        nameTextView.setText(hd);
        serviceTextView.setText(service);
        dateTextView.setText(date);

        return convertView;
    }
}

