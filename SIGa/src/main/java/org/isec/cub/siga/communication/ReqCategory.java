package org.isec.cub.siga.communication;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;

/**
 * Created by silvaric on 06/06/14.
 */
public class ReqCategory {

    private static String responseString;

    private ReqCategory(String urlToRequest) {

        responseString = null;
        /*HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response;
        String responseString = null;
        try {
            response = httpclient.execute(new HttpGet(uri[0]));
            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                responseString = out.toString();
            } else{
                //Closes the connection.
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (ClientProtocolException e) {
            out.println("1e : " + e.getMessage());
        } catch (IOException e) {
            out.println("2e : " + e.getMessage());
        }*/

        HttpClient Client = new DefaultHttpClient();
        String Content;
        String Error = null;

        try {
            HttpGet httpget = new HttpGet(urlToRequest);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            responseString = Client.execute(httpget, responseHandler);
        } catch (ClientProtocolException e) {
            System.out.println("1e : " + e.getMessage());

        } catch (IOException e) {
            System.out.println("2e : " + e.getMessage());

        }

        if (responseString == null) {
            responseString = "Uncategorized";
        }
    }

    public static String getCategory(String urlToRequest){
        new ReqCategory(urlToRequest);
        return responseString;
    }
}
