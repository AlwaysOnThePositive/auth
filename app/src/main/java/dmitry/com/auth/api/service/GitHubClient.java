package dmitry.com.auth.api.service;

import java.util.List;
import java.util.Map;

import dmitry.com.auth.api.model.AccessToken;
import dmitry.com.auth.api.model.GitHubRepo;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface GitHubClient {
    @Headers("Accept: application/json")
    @POST("/login/oauth/access_token")
    @FormUrlEncoded
    Call<AccessToken> getAccessToken(
            @Field("client_id") String clientId,
            @Field("client_secret") String clientSecret,
            @Field("code") String code
    );

    // get repos
    @GET("/users/{user}/repos")
    Call<List<GitHubRepo>>reposForUser(@Path("user") String user, @HeaderMap Map<String, String>
            headers);


}
