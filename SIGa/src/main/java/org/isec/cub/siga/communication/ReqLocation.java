package org.isec.cub.siga.communication;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import com.parse.ParseGeoPoint;

import org.isec.cub.siga.service.MyService;

import java.util.List;

/**
 * Created by silvaric on 06/06/14.
 */
public class ReqLocation {

    private static ParseGeoPoint responseString;

    private ReqLocation(MyService myService) {

        LocationManager lm = (LocationManager) myService.getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = lm.getProviders(true);

        /* Loop over the array backwards, and if you get an accurate location, then break out the loop*/
        Location l = null;

        for (int i=providers.size()-1; i>=0; i--) {
            l = lm.getLastKnownLocation(providers.get(i));
            if (l != null) break;
        }

        if (l != null) {
            responseString = new ParseGeoPoint(l.getLatitude(),l.getLongitude());
            Log.w("LOCATION", "[SUCCESS-PARSE] getLatitude : " + responseString.getLatitude());
            Log.w("LOCATION", "[SUCCESS-PARSE] getLongitude : " + responseString.getLongitude());
        }else{
            responseString = new ParseGeoPoint();
            Log.w("LOCATION", "[FAILURE-PARSE] location");
        }
    }

    public static ParseGeoPoint getLocation(MyService myService){
        new ReqLocation(myService);
        return responseString;
    }
}
