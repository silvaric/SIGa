package org.isec.cub.siga.communication;

import android.os.AsyncTask;

/**
 * Created by silvaric on 03/06/14.
 */
public class RequestCategory extends AsyncTask<String, String, String> {

    @Override
    protected String doInBackground(String... uri) {

        String responseString = null;
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

        responseString = "Uncategorized";

        return responseString;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        //out.println("result : " + result);

        //Do anything with response..
    }
}