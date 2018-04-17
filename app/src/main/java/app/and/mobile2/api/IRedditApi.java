package app.and.mobile2.api;

import app.and.mobile2.models.RedditDataModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IRedditApi {
    @GET("r/puppy.json?raw_json=1/")
    Call<RedditDataModel> getPuppiesModel(@Query("after") String after, @Query("limit") int limit);
}
