package org.isec.cub.siga.communication;

import android.os.AsyncTask;
import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseObject;

import org.isec.cub.siga.entity.AplicationEntity;
import org.isec.cub.siga.service.MyService;

import java.util.concurrent.ExecutionException;

/**
 * Created by rodrigo.tavares on 04-06-2014.
 */

public class Comms {

    private static Comms comms;

    public static Comms getComms(){

        if(comms == null){
            comms = new Comms();
        }

        return comms;
    }

    public String getAppCategory(String appName){

        /* Devolve a categoria da aplicação se encontrada;
         * Devolve null se ocorreu algum erro;
         * Devolve uma categoria standard tipo "uncategorized" se não for encontrada a aplicação.
         * */

        String category="";
        String urlToRequest = "http://www.appbrain.com/search?q=" + appName.trim();

        AsyncTask<String, String, String> execute = new RequestCategory().execute(urlToRequest);

        try {
            category = execute.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        }

        //--- força aqui um GarbageCollect só pra evitar situações constrangedoras
        System.gc();

        return category;
    }

    public double[] getAppGPS(MyService myService){

        /* Devolve a posição do utilzador;
         * Devolve null se ocorreu algum erro;

         * */

        double[] location;
        final AsyncTask<MyService, Void, double[]> execute = new RequestLocationGPS().execute(myService);
        Log.w("LABEL", "2");
        try {
            location = execute.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        }

        //--- força aqui um GarbageCollect só pra evitar situações constrangedoras
        System.gc();

        return location;
    }

    public Boolean sendData(AplicationEntity appEnt){

        /* Devolve true se os dados foram enviados com sucesso;
         * Devolve false se ocorreu algum erro.
         * */

        ParseObject sendApplicationName = new ParseObject("sendApplicationName");

        sendApplicationName.put("AppName", appEnt.getNome());
        sendApplicationName.put("Category", appEnt.getCategoria());
        sendApplicationName.put("Duration", appEnt.getDuracao());
        //--- TODO: acrescentar os resto dos atributos (atenção ao atributo da posição e da duração)

        try {
            sendApplicationName.save();
            return true;
        } catch (ParseException e) {
            e.printStackTrace();
            Log.w("LABEL", "Falhou no envio, causa: " + e.getCause());
            return false;
        }
    }
}