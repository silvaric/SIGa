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

    private ReqCategory(String appName) throws IOException, XPatherException {

        String encodingAppName = new String(appName.getBytes("UTF-8"), "UTF-8");

        String urlToRequest = "http://www.appbrain.com/search?q=" + encodingAppName;

        responseString = null;

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
            if(!responseString.equals(appName)){
                Log.w("CATEGORY", "[SUCCESS-PARSE] category : " + responseString);
            }else{
                responseString = null;
            }
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
