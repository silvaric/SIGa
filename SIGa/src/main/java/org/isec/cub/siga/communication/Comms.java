package org.isec.cub.siga.communication;

import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

import org.isec.cub.siga.entity.AplicationEntity;
import org.isec.cub.siga.service.MyService;

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
        Log.w("CATEGORY", "urlToRequest :" + urlToRequest);
        category = ReqCategory.getCategory(urlToRequest);

        /*try {
            AsyncTask<String, Void, String> execute = new RequestCategory().execute(urlToRequest);
            category = execute.get();

        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        }*/

        //--- força aqui um GarbageCollect só pra evitar situações constrangedoras
        System.gc();

        return category;
    }

    public ParseGeoPoint getAppGPS(MyService myService){

        /* Devolve a posição do utilzador;
         * Devolve null se ocorreu algum erro;

         * */

        ParseGeoPoint location;

        location = ReqLocation.getLocation(myService);

        /*AsyncTask<MyService, String, double[]> execute = new RequestLocationGPS().execute(myService);

        try {
            location = execute.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        }*/

        //--- força aqui um GarbageCollect só pra evitar situações constrangedoras
        System.gc();

        return location;
    }

    public Boolean sendData(AplicationEntity appEnt){

        /* Devolve true se os dados foram enviados com sucesso;
         * Devolve false se ocorreu algum erro.
         * */

        //
        //ParseObject sendApplicationName = new ParseObject("sendDistributonSIGa");
        ParseObject sendApplicationName = new ParseObject("sendDevelopmentSIGa");

        sendApplicationName.put("AppName", appEnt.getNome());
        sendApplicationName.put("Category", appEnt.getCategoria());
        sendApplicationName.put("Duration", appEnt.getDuracao());
        sendApplicationName.put("Location", appEnt.getLocation());

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