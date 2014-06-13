package in.junaidbabu;

/**
 * Created by babu on 13/6/14.
 */
public class VideoClass {
    String Vid;
    String Title;
    String Des;

    VideoClass(String vid, String title, String descr){
        this.Vid=vid;
        this.Title=title;
        this.Des=descr;
    }

    public String getVid(){
        return this.Vid;
    }

    public String getTitle(){
        return this.Title;
    }
}
