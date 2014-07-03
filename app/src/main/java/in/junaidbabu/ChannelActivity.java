package in.junaidbabu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import mysc.ImageAdapter;

public class ChannelActivity extends Activity{

    public static String channel;
    public String[] mTitle = {
            "Animations", "Art", "Comedy", "Education",
            "Everyday Life", "Fashion", "HD", "Music",
            "Nature", "Science & Tech", "Sports", "Travel & Events"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gridlayout);

        GridView gridView = (GridView) findViewById(R.id.grid_view);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                Intent i = new Intent(ChannelActivity.this, PlayerView.class);

                channel=mTitle[position].replaceAll("\\s+","");
                startActivity(i);
                // Sending image id to FullScreenActivity
                //Intent i = new Intent(getApplicationContext(), FullImageActivity.class);
                // passing array index
            //    i.putExtra("id", position);
             //   startActivity(i);
            }
        });
        // Instance of ImageAdapter Class
        gridView.setAdapter(new ImageAdapter(this));
//        ViewGroup group = (ViewGroup)findViewById(R.id.roottable);
//        setAllButtonListener(group);
    }

}
