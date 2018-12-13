package dmitry.com.auth;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private String cliendId = "57147c10cab5c9b26181";
    private String clientSecret = "141f753271d296d20560c2bc75b467ab00e621e9";
    private String redirectUri = "githubcom://callback";

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github" +
                ".com/login/oauth/authorize" + "?client_id=" + cliendId +
                "&scope=repo&redirect_uri=" + redirectUri));
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Uri uri = getIntent().getData();
        if (uri != null ) {
            Toast.makeText(this, "yay", Toast.LENGTH_SHORT).show();
        }
    }
}

//& uri.toString().startsWith(redirectUri)
