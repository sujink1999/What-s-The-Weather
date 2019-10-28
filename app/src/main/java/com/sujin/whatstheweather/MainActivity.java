package com.sujin.whatstheweather;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    EditText name;
    TextView output;
    Button getWeather;

    public void getTheWeather(View view)
    {

        DownloadTask task = new DownloadTask();

        task.execute("https://openweathermap.org/data/2.5/weather?q=" +name.getText().toString()+ "&appid=b6907d289e10d714a6e88b30761fae22");

        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(name.getWindowToken(),0);
    }


    public class DownloadTask extends AsyncTask<String, Void, String>
    {

        @Override
        protected String doInBackground(String... strings) {
            try {
                String result = "";
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream in = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while (data != -1)
                {
                    char character = (char) data;
                    if (character == '"')
                    {

                    }
                    result+=character;
                    data = reader.read();
                }
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Sorry no weather details available.", Toast.LENGTH_SHORT).show();
                output.setText("Fsiled");

                return null;
            }



        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String message = "";

            try {
                JSONObject json = new JSONObject(s);
                String weatherInfo = json.getString("weather");
                JSONArray arr = new JSONArray(weatherInfo);


                for(int i =0; i<arr.length();i++)
                {
                    JSONObject jsonPart = arr.getJSONObject(i);
                    String main = jsonPart.getString("main");
                    String desc = jsonPart.getString("description");

                    if(!main.equals("") && !desc.equals(""))
                    {
                        message+= main +": " +desc +"\r\n";

                    }else
                    {
                        Toast.makeText(MainActivity.this, "Sorry no weather details available.", Toast.LENGTH_SHORT).show();

                        output.setText("Failed2");
                    }



                }
                Log.i("message",message);
                if(!message.equals(""))
                {
                    output.setText(message);
                }else{
                    Toast.makeText(MainActivity.this, "Sorry no weather details available.", Toast.LENGTH_SHORT).show();
                    output.setText("");
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Sorry no weather details available.", Toast.LENGTH_SHORT).show();
                output.setText("");


            }



        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        name = (EditText) findViewById(R.id.editText);
        getWeather = (Button) findViewById(R.id.button);
        output = (TextView) findViewById(R.id.output);



    }
}
