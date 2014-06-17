package in.junaidbabu;

import mysc.GetUserReco;

/**
 * Created by babu on 13/6/14.
 */
public class VideoClass {
    String Vid;
    String Title;
    String thumb;
    String LongUrl=null;

    VideoClass(String vid, String title, String descr){
        this.Vid=vid;
        this.Title=title;
        this.thumb=descr;
    }

    public String getVid(){
        return this.Vid;
    }

    public String getTitle(){
        return this.Title;
    }

    public String getThumb(){
        return this.thumb;
    }

    public String getLongUrl(){
        return LongUrl;
    }
    public void setLongUrl(String url){
        this.LongUrl=url;
    }

    public void parseLongUrl(){
        GetUserReco.AsyncResult url = new GetUserReco.AsyncResult() {
            @Override
            public void gotResult(String s) {
                VideoClass.this.setLongUrl(s);
            }
        };
        new GetUserReco(url, MainVideoActivity.c.getString(R.string.serverip) + this.Vid);

    }
}
