package org.isec.cub.siga.communication;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import org.isec.cub.siga.service.MyService;

import java.util.List;

/**
 * Created by silvaric on 06/06/14.
 */
public class ReqLocation {

    private static double[] responseString;

    private ReqLocation(MyService myService) {

        LocationManager lm = (LocationManager) myService.getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = lm.getProviders(true);

        /* Loop over the array backwards, and if you get an accurate location, then break out the loop*/
        Location l = null;

        for (int i=providers.size()-1; i>=0; i--) {
            l = lm.getLastKnownLocation(providers.get(i));
            if (l != null) break;
        }

        double[] gps = new double[2];
        if (l != null) {
            Log.w("LOCATION", "getLatitude : " + l.getLatitude());
            Log.w("LOCATION", "getLongitude : " + l.getLongitude());
            gps[0] = l.getLatitude();
            gps[1] = l.getLongitude();
        }
        responseString = gps;
    }

    public static double[] getLocation(MyService myService){
        new ReqLocation(myService);
        return responseString;
    }
}
