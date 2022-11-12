package com.example.whatswether;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText  cityName;
    Button  btn;
    TextView rMain;
    TextView rdesciption;
    TextView rTemp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindComponents();

      btn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              String city=cityName.getText().toString().trim();
              DownloadTask task = new DownloadTask();
              task.execute("https://api.openweathermap.org/data/2.5/weather?q="+city+"&APPID=0852853b3628f9f0ef79308eacb461b4");
          }
      });

    }

    private void bindComponents() {
        cityName=findViewById(R.id.etCity);
        btn=findViewById(R.id.btn);
        rMain=findViewById(R.id.tvMain);
        rdesciption=findViewById(R.id.tvDesciption);
        rTemp=findViewById(R.id.tvTemp);

    }

    //-----------------------------------------------


    public class DownloadTask extends AsyncTask<String,Void,String>
    {

        @Override
        protected String doInBackground(String... urls) {

            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try{
                Log.d("I0","");
                url = new URL(urls[0]);
                Log.d("I1","");
                urlConnection = (HttpURLConnection) url.openConnection();
                Log.d("I2","");
                InputStream in = urlConnection.getInputStream();
                Log.d("I3","");
                InputStreamReader reader = new InputStreamReader(in);
                Log.d("I4","");
                int data=reader.read();
                Log.d("I5","");

                while(data!=-1)
                {
                    char curr = (char) data;
                    result += curr;
                    data = reader.read();
                }

                return   result;


            }catch (Exception e)
            {
                e.printStackTrace();
                return "Failed to Fetch JSON Data";
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i("M1 :",s);

            //Convert String into JSON

            try{

                JSONObject jsonObject = new JSONObject(s);

       //-------------------------------------------------------------------------------------------
                String WeatherInfo = jsonObject.getString("weather");


                JSONArray arr = new JSONArray(WeatherInfo);
                String main="",desp="";
                for(int i=0;i<arr.length();i++)
                {
                    JSONObject jsonPart = arr.getJSONObject(i);
                     main = jsonPart.getString("main") ;
                     desp = jsonPart.getString("description");
                }

                rMain.setText(main);
                rdesciption.setText(desp);
        //------------------------------------------------------------------


               //{"temp":295.47,"feels_like":295.04,"temp_min":295.47,"temp_max":295.47,"pressure":1015,"humidity":49,"sea_level":1015,"grnd_level":953}

                JSONObject jsonObject2 = jsonObject.getJSONObject("main");
                String temp = jsonObject2.getString("temp");
                Log.i("Temp",temp);
                int index=temp.indexOf(".");
                String sub = temp.substring(0,index);
                int tm=Integer.parseInt(sub)-272;
                rTemp.setText(String.valueOf(tm));



            }catch (Exception e)
            {
                e.printStackTrace();
            }


        }
    }




}