package com.example.ddkatona.friction;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity implements SensorEventListener {

    Sensor accelerometer;
    SensorManager sm;
    TextView acceleration; //gyorsulás doboza
    TextView time; //csúszási idő doboza
    TextView curra;
    RelativeLayout rl;
    int n = 0;
    double sum = 0;
    boolean started = false;
    double D[] = new double[1000];
    int db = 0;
    boolean ended = false;
    long st = 0;
    boolean vm = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sm = (SensorManager)getSystemService(SENSOR_SERVICE);
        accelerometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sm.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        acceleration = (TextView) findViewById(R.id.accplain);
        time = (TextView) findViewById(R.id.time);
        curra = (TextView) findViewById(R.id.curra);
        rl = (RelativeLayout) findViewById(R.id.rl);

        Button srt = (Button) findViewById(R.id.start);


        // EZT CSINÁLJA A START GOMB MEGNYOMÁSÁRA
        srt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //lenullázzuk a tömböt
                for(int i = 0; i < db; i++){
                    D[i] = 0;
                }
                n = 0;
                sum = 0;
                db = 0;
                started = true; //elindult a mérés
                ended = false;
                vm = false;
                st = System.currentTimeMillis();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //EZT CSINÁLJA, HA MEGVÁLTOZIK A SZENZOR ÉRTÉKE
    @Override
    public void onSensorChanged(SensorEvent event) {

        double a = -event.values[1];
        String ac = "";

        if(a > 1){
            vm = true;
        }

        if(a < 0){
            ac = Double.toString(a).substring(0, 5);
            curra.setText("a =" + ac + "m/s^2");
        }
        else{
            ac = Double.toString(a).substring(0, 4);
            curra.setText("a = " + ac + "m/s^2");
        }

        if(a > 1 && started && !ended && vm) {
            D[n] = a;
            n++;
            sum += a;

            String mu = Double.toString(a/9.81).substring(0, 5);

            String t = Long.toString(System.currentTimeMillis() - st);
            time.setText(t + "ms");
            rl.setBackgroundColor(Color.rgb(100, 150, 100));
            acceleration.setTextColor(Color.rgb(255, 255, 255));
            acceleration.setText("µ = " + mu);
        }
        else{
            if(n != 0) {

                String s = "";
                String tmp = "";
                db = n;

                double avga = sum/n;
                String mu = Double.toString(avga/9.81).substring(0, 5);

                rl.setBackgroundColor(Color.rgb(255, 255, 255));
                acceleration.setTextColor(Color.rgb(0, 127, 255));
                acceleration.setText("µ = " + mu);

                ended = true;
                n = 0;
                sum = 0;
                vm = false;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
