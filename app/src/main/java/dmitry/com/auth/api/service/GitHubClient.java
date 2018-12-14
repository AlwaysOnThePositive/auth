package dmitry.com.auth.api.service;

import java.util.List;

import dmitry.com.auth.api.model.AccessToken;
import dmitry.com.auth.api.model.GitHubRepo;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
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

    // query
    @GET("/users/{user}/repos")
    Call<List<GitHubRepo>>reposForUser(@Path("user") String user);

}
