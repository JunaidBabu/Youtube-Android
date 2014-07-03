package in.junaidbabu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class ChannelActivity extends Activity implements View.OnClickListener{

    public static String channel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel);

        ViewGroup group = (ViewGroup)findViewById(R.id.roottable);
        setAllButtonListener(group);
    }

    public void setAllButtonListener(ViewGroup viewGroup) {
        View v;
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            v = viewGroup.getChildAt(i);
            if (v instanceof ViewGroup) {
                setAllButtonListener((ViewGroup) v);
            } else if (v instanceof Button) {

                v.setOnClickListener(this);
                Drawable focus = (v.getBackground());

                StateListDrawable states = new StateListDrawable();
                states.addState(new int[] {android.R.attr.state_pressed},
                        focus);
               focus.setAlpha(100);
                states.addState(new int[] {android.R.attr.state_focused},
                        focus);
                states.addState(new int[] { },
                        v.getBackground());
                v.setBackground(states);
                v.setPadding(0, 110, 0, 0);
                ((Button) v).setTextColor(Color.WHITE);


            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.channel, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        Button b = (Button)view;
        Toast.makeText(this, b.getText(), Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, PlayerView.class);

        channel=b.getText().toString().replaceAll("\\s+","");
        startActivity(i);
    }
}
