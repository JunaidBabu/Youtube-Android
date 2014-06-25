package mysc;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.List;

import in.junaidbabu.R;
import in.junaidbabu.VideoClass;

/**
 * Created by babu on 16/6/14.
 */
public class CustomList extends ArrayAdapter<VideoClass> {
    private final Context context;

    List<VideoClass> VC;

    public CustomList(Context context, List<VideoClass> vc) {
        super(context, R.layout.list_single, vc);
        this.context = context;
        this.VC = vc;
    }


    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View rowView= inflater.inflate(R.layout.list_single, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
        txtTitle.setText(VC.get(position).getTitle());
        new DownloadImageTask(imageView).execute(VC.get(position).getThumb());
        //imageView.setImageResource(imageId[position]);
        return rowView;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        private Bitmap bitmap;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }

//            URL url = null;
//            try {
//                url = new URL(urldisplay);
//                URLConnection connection = url.openConnection();
//                connection.setUseCaches(true);
//                Object response = connection.getContent();
//               // if (response instanceof Bitmap) {
//                    bitmap = BitmapFactory.decodeStream((InputStream) response);
//               // }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

            return bitmap;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}