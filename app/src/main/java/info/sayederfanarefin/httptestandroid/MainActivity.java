package info.sayederfanarefin.httptestandroid;

import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    TextView textV1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textV1 = (TextView) findViewById(R.id.gaa);
        try {
            //in the background the api from the neonsofts server will pull all the data of news
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        URL url = new URL("url");
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setReadTimeout(10000);
                        conn.setConnectTimeout(15000);
                        conn.setRequestProperty("USER-AGENT", "Mozilla/5.0");
                        conn.setRequestMethod("POST");
                        conn.setDoInput(true);
                        conn.setDoOutput(true);
                        //conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                        //conn.setRequestProperty("Accept", "application/x-www-form-urlencoded");
                        DataOutputStream dStream = new DataOutputStream(conn.getOutputStream());

                        String url_param = "params";
                        dStream.writeBytes(url_param);
                        dStream.flush();
                        dStream.close();

                        InputStream in;
                        if (conn.getResponseCode() == 200) {
                            in = conn.getInputStream();
                        } else {
                            /* error from server */
                            in = conn.getErrorStream();
                        }

                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        final StringBuilder result = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            result.append(line);
                        }

                        final String response_code = String.valueOf(conn.getResponseCode());

                        MainActivity.this.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                // your stuff to update the UI
                                textV1.setText("response code: "+ response_code +" \n\n " + result.toString());
                            }
                        });

                    } catch (final Exception exception) {
                        Log.v("===YOUR_APP_LOG_TAG", "I got an error "+exception.toString(), exception);
                    }
                    return null;
                }
            }.execute();

        } catch (Exception ex) {
        }

    }
}
