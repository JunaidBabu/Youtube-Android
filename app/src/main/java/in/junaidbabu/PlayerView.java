package in.junaidbabu;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import mysc.CustomVideoView;
import mysc.GetUserReco;

public class PlayerView extends FragmentActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    public static CustomVideoView mVideoView;
    public static TextView NotifText;
    public static String NextURL;
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_player_view);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        getActionBar().hide();

        final DrawerLayout drawer = (DrawerLayout)this.findViewById(R.id.drawer_layout);
        drawer.openDrawer(Gravity.LEFT);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.player_view, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";


        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_player_view, container, false);
            mVideoView = (CustomVideoView) rootView.findViewById(R.id.myplaysurface);

            //CustomMediaController controller=new CustomMediaController(getActivity());
            //mVideoView.setMediaController(controller);
            mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    NavigationDrawerFragment.startPlayback(NavigationDrawerFragment.VC, NavigationDrawerFragment.NowPlaying + 1);
                }
            });

            final SeekBar s = (SeekBar) rootView.findViewById(R.id.seekbar);
            NotifText = (TextView) rootView.findViewById(R.id.nextvideo);
            new Thread()
            {
                @Override
                public void run() {
                    while(true) {
                        try{
                        if (mVideoView.isPlaying()) {
                            s.setMax(mVideoView.getDuration());
                            s.setProgress(mVideoView.getCurrentPosition());
                        }
                    }catch (Exception e){}
                    }
                }
            }.start();

            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((PlayerView) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        final DrawerLayout mDrawerLayout = (DrawerLayout)this.findViewById(R.id.drawer_layout);

        switch (keyCode) {
            case 82: // Menu key
            {

                NavigationDrawerFragment.Selection=NavigationDrawerFragment.NowPlaying;
                NavigationDrawerFragment.mDrawerListView.setSelection(NavigationDrawerFragment.Selection);
                //drawer.openDrawer(Gravity.LEFT);
                if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                } else {
                    mDrawerLayout.openDrawer(Gravity.LEFT);
                }
                return true;
            }
            case 22: //right
            {
                mVideoView.seekTo(mVideoView.getCurrentPosition()+10000);
                return true;
            }
            case 21: //left
            {
                Toast.makeText(this, "Current selection " + NavigationDrawerFragment.Selection + "", Toast.LENGTH_LONG).show();

                mVideoView.seekTo(mVideoView.getCurrentPosition()-10000);
                //Toast.makeText(this, mVideoView.getCurrentPosition()+"", Toast.LENGTH_SHORT).show();

                return true;
            }
            case 23: //middle button
            {
                if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    NavigationDrawerFragment.startPlayback(NavigationDrawerFragment.VC, NavigationDrawerFragment.Selection);
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                } else {
                    if (mVideoView.isPlaying())
                        mVideoView.pause();
                    else
                        mVideoView.start();
                    return true;
                }

            }
            case 19: //up
            {
                if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    setSelection(NavigationDrawerFragment.Selection - 1);
                } else {
                    NavigationDrawerFragment.startPlayback(NavigationDrawerFragment.VC, NavigationDrawerFragment.NowPlaying-1);
                }

                //
                return true;
            }
            case 20: //down
            {
                if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    setSelection(NavigationDrawerFragment.Selection+1);

                } else {
                    NavigationDrawerFragment.startPlayback(NavigationDrawerFragment.VC, NavigationDrawerFragment.NowPlaying+1);
                }

                return true;
            }
            case 4: //back
            {
              //  startActivity(new Intent(this, ChannelActivity.class));
               // finish();
               // return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
    static int flag=0;
    public static void setSelection(int selec){
        if((NavigationDrawerFragment.Selection>selec && NavigationDrawerFragment.Selection>0) || (NavigationDrawerFragment.Selection<selec && NavigationDrawerFragment.Selection<NavigationDrawerFragment.VC.size()-1)){
            NavigationDrawerFragment.Selection=selec;
        }
       // NavigationDrawerFragment.mDrawerListView.smoothScrollToPositionFromTop(NavigationDrawerFragment.Selection, 200);
        NavigationDrawerFragment.mDrawerListView.setSelection(NavigationDrawerFragment.Selection);

        if(NavigationDrawerFragment.VC.size()-selec < 5){
            Toast.makeText(NavigationDrawerFragment.context, "Should load more now", Toast.LENGTH_SHORT).show();
            if(flag==0){
                flag=1;
            new GetUserReco(new GetUserReco.AsyncResult() {
                @Override
                public void gotResult(String s) {
                    //ListView lv = (ListView) findViewById(R.id.listView4);
                    // ListPop(mDrawerListView, s);
                    Log.w("s", s);
                    NavigationDrawerFragment.Listpopulate(s);
                    flag=0;
                }
            },  NextURL);
        }}
    }
}
