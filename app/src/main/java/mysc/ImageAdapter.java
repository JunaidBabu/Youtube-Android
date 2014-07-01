package mysc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import in.junaidbabu.R;

/**
 * Created by babu on 30/6/14.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;

    // Keep all Images in array
    public Integer[] mThumbIds = {
            R.drawable.animations, R.drawable.art, R.drawable.comedy, R.drawable.education,
            R.drawable.everyday, R.drawable.fashion, R.drawable.hd, R.drawable.music,
            R.drawable.nature, R.drawable.science, R.drawable.sports, R.drawable.travel
    };
    public String[] mTitle = {
            "Animations", "Art", "Comedy", "Education",
            "Everyday Life", "Fashion", "HD", "Music",
            "Nature", "Science & Tech", "Sports", "Travel & Events"
    };
    // Constructor
    public ImageAdapter(Context c){
        mContext = c;
    }

    @Override
    public int getCount() {
        return mThumbIds.length;
    }

    @Override
    public Object getItem(int position) {
        return mThumbIds[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        ImageView imageView = new ImageView(mContext);
//        imageView.setImageResource(mThumbIds[position]);
//        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        imageView.setLayoutParams(new GridView.LayoutParams(70, 70));
//        // return imageView;


        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            grid = new View(mContext);
            grid = inflater.inflate(R.layout.list_single, null);
            TextView textView = (TextView) grid.findViewById(R.id.txt);
            ImageView imageView = (ImageView)grid.findViewById(R.id.img);
            textView.setText(mTitle[position]);
            imageView.setImageResource(mThumbIds[position]);
        } else {
            grid = (View) convertView;
        }
        return grid;
    }

}