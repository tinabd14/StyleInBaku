package styleIn.tunarmahmudov.styleIn;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ServicesAdapter extends ArrayAdapter<String> {

    private Context aContext;
    int aResource;
    int size;


    public ServicesAdapter(Context context, int resource, ArrayList<String> objects) {
        super(context, resource, objects);
        aContext = context;
        aResource = resource;
        size = objects.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String serviceName = "";
        String price = "";

        if(position < size / 2)
        {
            serviceName = getItem(position);
            price = getItem((size / 2) + position);
        }

        LayoutInflater inflater = LayoutInflater.from(aContext);
        convertView = inflater.inflate(aResource, parent, false);

        TextView serviceNameTextView = (TextView) convertView.findViewById(R.id.serviceNameTextView);
        TextView priceTextView = (TextView) convertView.findViewById(R.id.priceTextView);

        serviceNameTextView.setText(serviceName);
        priceTextView.setText(price);

        return convertView;
    }
}
