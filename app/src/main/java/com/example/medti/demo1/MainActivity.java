package com.example.medti.demo1;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    String location;
    TextView textView;
    EditText editText1;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView1);
        Button button = (Button) findViewById(R.id.btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText1=(EditText)findViewById(R.id.sss);
                location = editText1.getText().toString();
                //Log.i("Location",editText1.getText().toString());
                final String url1= new String("https://api.openweathermap.org/data/2.5/weather?q="+location.toString()+"&APPID=a26b3c05bcd9eb7c2663b71871770b67");
                // Toast.makeText(getApplicationContext(),location,Toast.LENGTH_SHORT).show();
                new JSONtask().execute(url1);
               // Toast.makeText(getApplicationContext(),"Yoooo" ,Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class JSONtask extends AsyncTask<String, String, String> {

        private ProgressDialog progress;
        @Override
        protected String doInBackground(String... urls) {
            publishProgress();
            //Toast.makeText(getApplicationContext(),"Pehli line ",Toast.LENGTH_SHORT).show();
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                Log.i("Infoooooo",""+urls[0]);
                URL url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer  buffer = new StringBuffer();
                String line = "";
                int c=0;
                //Toast.makeText(getApplicationContext(),"Content le liya h",Toast.LENGTH_SHORT).show();
                while ((line=reader.readLine())!=null) {
                    buffer.append(line);
                }

                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            progress = ProgressDialog.show(MainActivity.this, "", "Wait...");
        }
        @Override
        protected void onPostExecute(String s) {
            JSONObject js;
            super.onPostExecute(s);
                if (progress != null){
                    progress.dismiss();
                }
                int location ,humidity,temp_min,temp_max;
                String name;
                progress = null;
                super.onPostExecute(s);
                try{
                    js= new JSONObject(s);
                    humidity=js.getJSONObject("main").getInt("humidity");
                    temp_max=js.getJSONObject("main").getInt("temp_max")-273;
                    name =js.getString("name");
                    AlertDialog.Builder alartdialog = new AlertDialog.Builder(MainActivity.this);

                    alartdialog.setTitle("Weather");
                    alartdialog.setIcon(R.drawable.temperature);
                    alartdialog.setMessage("Location Name: "+name+"\n"+"Humidty: "+humidity+"%"+"\n"+"Temperture: "+temp_max);
                    alartdialog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(MainActivity.this, "Done", Toast.LENGTH_SHORT).show();
                        }
                    });

                    AlertDialog a = alartdialog.create();
                    a.setCanceledOnTouchOutside(false);
                    a.show();
                }catch (JSONException  e){
                    Log.i("Errorr",""+e);
                }

        }
    }
}