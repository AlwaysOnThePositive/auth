package dmitry.com.auth.ui;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import dmitry.com.auth.R;
import dmitry.com.auth.api.model.AccessToken;
import dmitry.com.auth.api.service.GitHubClient;
import dmitry.com.auth.api.model.GitHubRepo;
import dmitry.com.auth.ui.adapter.GitHubRepoAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private String cliendId = "fcd97c7a329b55f7498d";
    private String clientSecret = "84e8d8db1caf8de9c62bc1c254bbedd65f402b41";
    private String redirectUri = "dmitry.com.auth://callback";

    private String myUrlGit = "https://github.com/login/oauth/authorize?client_id=" + cliendId +
            "&scope=repo&redirect_uri=" + redirectUri;

    private TextView textView;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        listView = findViewById(R.id.pagination_list);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(myUrlGit));
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        Uri uri = getIntent().getData();
        if (uri != null && uri.toString().startsWith(redirectUri)) {

            String code = uri.getQueryParameter("code");

            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl("https://github.com/")
                    .addConverterFactory(GsonConverterFactory.create());

            Retrofit retrofit = builder.build();

            GitHubClient client = retrofit.create(GitHubClient.class);
            Call<AccessToken> accessTokenCall = client.getAccessToken(
                    cliendId,
                    clientSecret,
                    code
            );

            accessTokenCall.enqueue(new Callback<AccessToken>() {
                @Override
                public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                    String token = response.body().getAccessToken();
                    textView.setText(token);
                    Toast.makeText(MainActivity.this, "yay", Toast.LENGTH_SHORT).show();

                    showRepos(token);
                }

                @Override
                public void onFailure(Call<AccessToken> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "no", Toast.LENGTH_SHORT).show();
                }
            });

//            Toast.makeText(this, uri.toString(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "huc-huc", Toast.LENGTH_LONG).show();
        }

        textView.setText(uri != null ? Objects.requireNonNull(uri).toString() : "кекккккккекеке");
    }

    private void showRepos(String token) {
        // for repo
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();

        GitHubClient client = retrofit.create(GitHubClient.class);

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "token " + token);
        Call<List<GitHubRepo>> call = client.reposForUser("AlwaysOnThePositive", headers);

        call.enqueue(new Callback<List<GitHubRepo>>() {
            @Override
            public void onResponse(Call<List<GitHubRepo>> call, Response<List<GitHubRepo>> response) {
                List<GitHubRepo> repos = response.body();

                listView.setAdapter(new GitHubRepoAdapter(MainActivity.this, repos));
            }

            @Override
            public void onFailure(Call<List<GitHubRepo>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "error :(", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

