package styleIn.tunarmahmudov.styleIn;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class HdListAdapter extends ArrayAdapter<ParseObject> {

    private Context aContext;
    int aResource;


    public HdListAdapter(Context context, int resource, ArrayList<ParseObject> objects) {
        super(context, resource, objects);
        aContext = context;
        aResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String hdName = null;
        double point = 0;
        ParseFile image = null;

        DecimalFormat decimalFormat = new DecimalFormat("#.##");

        try {
            hdName = getItem(position).fetchIfNeeded().getString("name");
            point = getItem(position).fetchIfNeeded().getDouble("point");
            image = getItem(position).fetchIfNeeded().getParseFile("image");

        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }


        LayoutInflater inflater = LayoutInflater.from(aContext);
        convertView = inflater.inflate(aResource, parent, false);

        TextView nameTextView = (TextView) convertView.findViewById(R.id.nameTextView);
        TextView pointTextView = (TextView) convertView.findViewById(R.id.pointTextview);
        final ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);

        nameTextView.setText(hdName);
        pointTextView.setText(String.valueOf(decimalFormat.format(point)));

        image.getDataInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] data, ParseException e) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                imageView.setImageBitmap(bitmap);
            }
        });

        return convertView;
    }
}

