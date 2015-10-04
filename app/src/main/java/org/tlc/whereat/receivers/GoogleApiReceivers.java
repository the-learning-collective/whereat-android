package org.tlc.whereat.receivers;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import org.tlc.whereat.R;
import org.tlc.whereat.fragments.LocServicesAlertFragment;
import org.tlc.whereat.fragments.PlayServicesAlertFragment;
import org.tlc.whereat.pubsub.Dispatcher;
import org.tlc.whereat.pubsub.LocationPublisher;
import org.tlc.whereat.util.PopToast;

public class GoogleApiReceivers extends Receiver {

    //FIELDS

    public static final String TAG = GoogleApiReceivers.class.getSimpleName();

    protected final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000; //TODO: move to resources
    protected final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000; //TODO: move to resources

    protected BroadcastReceiver mApiClientDisconnected = apiClientDisconnectedReceiver();
    protected BroadcastReceiver mLocationServicesDisabledReceiver = locationServicesDisabledReceiver();
    protected BroadcastReceiver mPlayServicesDisabledReceiver = playServicesDisabledReceiver();

    protected LocServicesAlertFragment mLocServicesAlert;
    protected PlayServicesAlertFragment mPlayServicesAlert;

    // CONSTRUCTOR

    public GoogleApiReceivers(Context ctx, LocalBroadcastManager lbm){
        mCtx = ctx;
        mLbm = lbm;
        mLocServicesAlert = new LocServicesAlertFragment();
        mPlayServicesAlert = new PlayServicesAlertFragment();
    }

    // PUBLIC METHODS

    public void register(){
        Dispatcher.register(mLbm, mApiClientDisconnected, LocationPublisher.ACTION_GOOGLE_API_CLIENT_DISCONNECTED);
        Dispatcher.register(mLbm, mLocationServicesDisabledReceiver, LocationPublisher.ACTION_LOCATION_SERVICES_DISABLED);
        Dispatcher.register(mLbm, mPlayServicesDisabledReceiver, LocationPublisher.ACTION_PLAY_SERVICES_DISABLED);
    }

    public void unregister(){
        mLbm.unregisterReceiver(mApiClientDisconnected);
        mLbm.unregisterReceiver(mLocationServicesDisabledReceiver);
        mLbm.unregisterReceiver(mPlayServicesDisabledReceiver);
    }

    // RECEIVERS

    protected BroadcastReceiver apiClientDisconnectedReceiver(){
        return new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent i) {
                ConnectionResult cr = i.getExtras().getParcelable(LocationPublisher.ACTION_GOOGLE_API_CLIENT_DISCONNECTED);
                fixApiConnection(cr);
            }
        };
    }

    protected BroadcastReceiver locationServicesDisabledReceiver(){
        return new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                fixLocationServices();
            }
        };
    }

    protected BroadcastReceiver playServicesDisabledReceiver(){
        return new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                fixPlayServices();
            }
        };
    }

    // FIXERS

    protected void fixApiConnection(ConnectionResult cr){
        if (cr.hasResolution() && mCtx instanceof Activity) {
            try {
                cr.startResolutionForResult((Activity) mCtx, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            PopToast.briefly(mCtx, mCtx.getString(R.string.goog_loc_api_disconnected_toast));
            Log.i(TAG, "Google location API connection failed with code " + cr.getErrorCode());
        }
    }

    protected void fixLocationServices(){
        mLocServicesAlert.show(
            ((Activity) mCtx).getFragmentManager(),
            mCtx.getString(R.string.goog_loc_services_alert_tag));
    }

    protected void fixPlayServices(){
        int code = playAvailable(mCtx);
        mPlayServicesAlert.setCode(code);

        if (recoverable(playAvailable(mCtx))) {
            mPlayServicesAlert.show(
                ((Activity) mCtx).getFragmentManager(),
                mCtx.getString(R.string.goog_play_services_alert_tag));
            //GooglePlayServicesUtil.getErrorDialog(code, (Activity) mCtx, PLAY_SERVICES_RESOLUTION_REQUEST).show();
        }
        else {
            PopToast.briefly(mCtx, mCtx.getString(R.string.goog_play_services_unavailable_toast));
        }
    }

    protected int playAvailable(Context ctx){
        return GooglePlayServicesUtil.isGooglePlayServicesAvailable(mCtx);
    }

    protected boolean recoverable(int code){
        return GooglePlayServicesUtil.isUserRecoverableError(code);
    }
}
