package org.isec.cub.siga.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by silvaric on 03/06/14.
 */
public class MyService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        ArrayList<String> appIgnoreList = new ArrayList<String>();

        Log.w("SERVICE", "[START] Service");

        //--- popula o array de apps a ignorar
        appIgnoreList.add("none");
        appIgnoreList.add("SIGa");
        appIgnoreList.add("Launcher");
        appIgnoreList.add("Discador");
        appIgnoreList.add("Settings");
        appIgnoreList.add("Configurações");

        //--- inicia o listener
        AppListener appListener = new AppListener(this, appIgnoreList);
        appListener.start();

        /**************************************
         *  Faz com que o serviço seja        *
         *  reiniciado sempre que seja 'morto'*
         **************************************/
        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        //TODO for communication return IBinder implementation
        return null;
    }
}