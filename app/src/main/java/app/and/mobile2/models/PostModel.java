package app.and.mobile2.models;

import com.google.gson.annotations.SerializedName;

public class PostModel {
    @SerializedName("thumbnail")
    private String thumbnail;
    @SerializedName("is_video")
    private boolean is_video;
    @SerializedName("subreddit_id")
    String subreddit_id;


    public PostModel(String thumbnail, boolean is_video, String subreddit_id) {
        this.thumbnail = thumbnail;
        this.is_video = is_video;
        this.subreddit_id = subreddit_id;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public boolean isIs_video() {
        return is_video;
    }

    public String getSubreddit_id() {
        return subreddit_id;
    }
}
