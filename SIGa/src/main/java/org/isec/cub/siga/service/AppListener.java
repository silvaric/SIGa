package org.isec.cub.siga.service;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.parse.ParseGeoPoint;

import org.isec.cub.siga.communication.Comms;
import org.isec.cub.siga.entity.AplicationEntity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * Created by rodrigo.tavares on 04-06-2014.
 */
public class AppListener extends Thread {

    private MyService myService;

    private Boolean keepRunning = true;

    private ArrayList<String> appIgnoreList;

    private ArrayList<AplicationEntity> appToSendList = null;

    //*** construtor ***
    public AppListener(MyService myService, ArrayList<String> ignoreList) {
        this.myService = myService;
        this.appIgnoreList = ignoreList;
    }

    //*** ciclo principal da aplicação ***
    public void run() {

        ActivityManager am;
        ActivityManager.RunningTaskInfo foregroundTaskInfo;
        PackageManager pm;
        String foregroundTaskPackageName;
        PackageInfo foregroundAppPackageInfo;

        AplicationEntity appEntity = null;

        String lastAppDetected = "none";
        String appName = "";

        Boolean ignoreApp = false;

        am = (ActivityManager) myService.getSystemService(Context.ACTIVITY_SERVICE);
        pm = myService.getPackageManager();

        while (keepRunning) {

            appName = "none";

            // The first in the list of RunningTasks is always the foreground task.
            foregroundTaskInfo = am.getRunningTasks(1).get(0);
            foregroundTaskPackageName = foregroundTaskInfo.topActivity.getPackageName();

            try {
                foregroundAppPackageInfo = pm.getPackageInfo(foregroundTaskPackageName, 0);
                appName = foregroundAppPackageInfo.applicationInfo.loadLabel(pm).toString();

            } catch (PackageManager.NameNotFoundException e) {
                //e.printStackTrace();
                Log.w("AppListener", "[DETECTED] Eeeee saiu coco");
            }

            //processName = pm.getApplicationLabel(pm.getApplicationInfo(info.processName, PackageManager.GET_META_DATA));
            //appName = processName.toString();

            //--- verifica se é uma aplicação que deve ignorar
            for (int x = 0; x < appIgnoreList.size(); x++) {
                if (appName.equals(appIgnoreList.get(x))) {

                    ignoreApp = true;
                    break;
                } else if (x == appIgnoreList.size() - 1) {
                    ignoreApp = false;
                }
            }

            //--- se a última app guardada no array, não for igual à app que se encontra a correr
            if (!appName.equals(lastAppDetected)) {

                if (appEntity != null) {

                    Log.w("AppListener", "[DETECTED] Add into array: " + lastAppDetected);

                    //--- adiciona a app ao array
                    this.addToAppToSendList(appEntity);

                    //--- coloca o objeto entidade da app a null
                    appEntity = null;
                }

                if (appEntity == null && !ignoreApp) {

                    Log.w("AppListener", "[DETECTED] Get Entity : " + appName);

                    //--- popula o objeto
                    appEntity = this.populateAppEntity(appName);

                    //--- assinala a última app que foi detetada
                    lastAppDetected = appName;
                }
            }

            //--- verifica constantemente se existem dados para enviar
            //--- ATENÇÃO: Aqui podemos controlar quantos dados queremos enviar de cada vez
            if (appToSendList != null && appToSendList.size() > 0) {

                Log.w("AppListener", "[SEND] is going to send");

                //--- envia todos os dados da lista e liberta-a
                this.dumpAppToSendList();
            }
        }
    }

    //*** preenche parâmetros do objeto entidade de uma app, e devolve esse objeto ***
    private AplicationEntity populateAppEntity(String appName) {

        AplicationEntity appEntity = new AplicationEntity();
        String categoria;
        ParseGeoPoint location;
        Timestamp timestamp;

        //--- recolhe a categoria da app
        categoria = Comms.getComms().getAppCategory(appName);

        //--- recolhe a localização da app
        location = Comms.getComms().getAppGPS(myService);

        //--- recolhe o timestamp
        Calendar calendar = Calendar.getInstance();
        timestamp = new Timestamp(calendar.getTime().getTime());

        //--- popula o objeto
        appEntity.setNome(appName);
        appEntity.setCategoria(categoria);
        appEntity.setLocation(location);
        appEntity.setTimestamp(timestamp);

        //--- imprime o objeto
        //appEntity.description();

        return appEntity;
    }

    //*** adiciona um objeto entidade de uma aplicação ao array 'appToSendList' ***
    private void addToAppToSendList(AplicationEntity appEntity) {

        Timestamp currentTimestamp = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());

        int duracao;

        //--- verifica se a lista já foi inicializada
        if (this.appToSendList == null) {

            this.appToSendList = new ArrayList<AplicationEntity>();

            //--- se o número de objetos no array for igual a 100, remove o mais antigo em preferência do mais recente
            //--- e força um GarbageColection para evitar males maiores
        } else if (this.appToSendList.size() == 100) {

            this.appToSendList.remove(0);
            System.gc();
        }

        //--- calcula a duração do uso da app pelo timestamp
        long diffInMs = currentTimestamp.getTime() - appEntity.getTimestamp().getTime();
        duracao = (int) TimeUnit.MILLISECONDS.toSeconds(diffInMs);

        //--- popula a duração
        appEntity.setDuracao(duracao);

        //--- guarda o objeto no array
        this.appToSendList.add(appEntity);
    }

    //*** tenta enviar todos os dados do array para o parse ***
    private void dumpAppToSendList() {

        //--- pega o connectivity manager
        ConnectivityManager cm =
                (ConnectivityManager) myService.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        //--- verifica se está ligado à internet
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (isConnected) {

            Log.w("AppListener", "[SEND] Running objects inside array ");

            /*ParseObject testObject = new ParseObject("TestObject");
            testObject.put("foo", "bar");
            testObject.saveInBackground();*/

            //--- corre o array de dados a enviar
            for (int i = 0; i < appToSendList.size(); i++) {

                Log.w("AppListener", "[SEND] Object array " + (i + 1) + "/" + appToSendList.size());
                //--- envia o objeto entidade
                Comms.getComms().sendData(appToSendList.get(i));
            }

            //--- liberta o array
            this.appToSendList = null;

            //--- força o GarbageColection
            System.gc();
        }
        //else
        //Log.w("LABEL", "Ia enviar, mas não há rede :(");
    }

    //*** pára a thread. não vai ser usado, mas pronto ***
    public void stopThread() {
        this.keepRunning = false;
    }
}