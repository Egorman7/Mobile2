package app.and.mobile2.models;

import com.google.gson.annotations.SerializedName;

public class PostModel {
    @SerializedName("thumbnail")
    private String thumbnail;
    @SerializedName("is_video")
    private boolean is_video;


    public PostModel(String thumbnail, boolean is_video) {
        this.thumbnail = thumbnail;
        this.is_video = is_video;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public boolean isIs_video() {
        return is_video;
    }
}
