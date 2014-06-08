package org.isec.cub.siga.communication;

import android.util.Log;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by silvaric on 06/06/14.
 */
public class ReqCategory {

    private static String responseString;

    private ReqCategory(String urlToRequest) throws IOException, XPatherException {

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

        /*
        HttpClient Client = new DefaultHttpClient();
        String Content;
        String Error = null;

        try {
            HttpGet httpget = new HttpGet(urlToRequest);

            HttpParams params = httpget.getParams();
            params.setParameter(ClientPNames.HANDLE_REDIRECTS, Boolean.FALSE);
            httpget.setParams(params);

            HttpResponse httpResp = Client.execute(httpget);
            int code = httpResp.getStatusLine().getStatusCode();
            Log.w("CATEGORY", "HTTPGet code : " + code);

            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            responseString = Client.execute(httpget, responseHandler);
        } catch (ClientProtocolException e) {
            System.out.println("1e : " + e.getMessage());

        } catch (IOException e) {
            System.out.println("2e : " + e.getMessage());

        }

*/
        // config cleaner properties
        HtmlCleaner htmlCleaner = new HtmlCleaner();
        CleanerProperties props = htmlCleaner.getProperties();
        props.setAllowHtmlInsideAttributes(false);
        props.setAllowMultiWordAttributes(true);
        props.setRecognizeUnicodeChars(true);
        props.setOmitComments(true);

        // create URL object
        URL url = new URL(urlToRequest);

        // userAgent
        String USER_AGENT = "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:23.0) Gecko/20100101 Firefox/23.0";

        HttpURLConnection lConn = (HttpURLConnection) url.openConnection();
        lConn.setRequestProperty("User-Agent", USER_AGENT);
        lConn.connect();

        // get HTML page root node
        //TagNode root = htmlCleaner.clean(url);
        TagNode root = htmlCleaner.clean( lConn.getInputStream() );

        // query XPath
        Object[] statsNode = root.evaluateXPath("//div[@style='display: inline-block;']");
        // process data if found any node
        if(statsNode.length > 0) {
            // I already know there's only one node, so pick index at 0.
            TagNode resultNode = (TagNode)statsNode[0];
            // get text data from HTML node
            responseString = resultNode.getText().toString();
            Log.w("CATEGORY", "[SUCCESS-PARSE] category : " + responseString);
        }

        if (responseString == null) {
            responseString = "Uncategorized";
            Log.w("CATEGORY", "[FAILURE-PARSE] category : " + responseString);
        }
    }

    public static String getCategory(String urlToRequest){
        try {
            new ReqCategory(urlToRequest);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (XPatherException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseString;
    }
}
