package app.and.mobile2.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RedditApi {
    private static Retrofit retrofit;
    private static IRedditApi instance;
    private static final String BASE_URL = "https://www.reddit.com/";
    public static IRedditApi getInstance(){
        if(instance==null){
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()).build();
            instance = retrofit.create(IRedditApi.class);
        }
        return instance;
    }
}
