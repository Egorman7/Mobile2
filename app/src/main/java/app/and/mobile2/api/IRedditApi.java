package app.and.mobile2.api;

import retrofit2.Call;
import retrofit2.http.GET;

public interface IRedditApi {
    @GET("puppy.json")
    Call<String> getPuppies();
}
