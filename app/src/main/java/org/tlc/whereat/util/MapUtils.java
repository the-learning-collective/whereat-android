package org.tlc.whereat.util;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.tlc.whereat.model.UserLocation;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MapUtils {

    public MapUtils(){}

    public static MarkerOptions parseMarker(UserLocation l){
        return new MarkerOptions().position(parseLatLon(l)).title(parseTime(l));
    }

    public static LatLng parseLatLon(UserLocation loc){
        return new LatLng(loc.getLatitude(), loc.getLongitude());
    }

    private static String parseTime(UserLocation loc){
        return TimeUtils.fullDate(loc.getTime());
    }

}
