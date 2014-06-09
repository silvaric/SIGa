package org.isec.cub.siga.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.Parse;

import org.isec.cub.siga.SobreActivity;
import org.isec.cub.siga.service.MyService;

import java.util.Calendar;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(org.isec.cub.siga.R.layout.activity_main);

        /**************************************
         *  SDK Parse.com                       *
         **************************************/

        Parse.initialize(this, "t2RlnC6F16lCXQFtL06ADqrMAos0X24u3Log9MCs", "rBj3NS9AESRPOmNTW7Q4J7XwP6J0ys6jeJVlLO7L");

        /*
        ParseObject testObject = new ParseObject("TestObject");
        testObject.put("foo", "TESTE");
        testObject.saveInBackground();
        */

        /*****************************************
         *  Process Background - ActivityManager *
         *****************************************/

        Calendar cal = Calendar.getInstance();

        Intent intent = new Intent(this, MyService.class);
        PendingIntent pintent = PendingIntent.getService(this, 0, intent, 0);

        //--- rodrigo.tavares - em principio deve bastar colocar a correr uma vez e o android mantem o serviço a correr com o "sticky"

        AlarmManager alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        // Start every 30 seconds
        alarm.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pintent );
//        setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 30*1000, pintent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(org.isec.cub.siga.R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == org.isec.cub.siga.R.id.action_settings2) {
            Intent intent = new Intent(this, SobreActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
