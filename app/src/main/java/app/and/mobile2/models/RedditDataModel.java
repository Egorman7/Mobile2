package app.and.mobile2.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RedditDataModel {
    @SerializedName("data")
    Data data;

    public RedditDataModel(Data data) {
        this.data = data;
    }

    public Data getData() {
        return data;
    }

    public class Data{
        @SerializedName("children")
        ArrayList<Children> children;
        @SerializedName("after")
        String after;

        public Data(ArrayList<Children> children, String after) {
            this.children = children;
            this.after = after;
        }

        public String getAfter() {
            return after;
        }

        public ArrayList<Children> getChildren() {
            return children;
        }

        public class Children{
            @SerializedName("data")
            PostModel data;

            public Children(PostModel data) {
                this.data = data;
            }

            public PostModel getData() {
                return data;
            }
        }
    }
}
