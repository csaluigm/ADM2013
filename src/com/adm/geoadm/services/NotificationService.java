package com.adm.geoadm.services;

import java.util.ArrayList;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.adm.geoadm.NuevoRecordatorio;
import com.adm.geoadm.R;
import com.adm.geoadm.db.Recordatorio;
import com.adm.geoadm.db.RecordatoriosDB;

/**
 * 
 */
public class NotificationService extends Service {
	
	public static final String LOG_TAG = "GeoADM.NotificationService";
	public static final long UPDATE_PERIOD = 5000;
	public static final float LOCATION_MIN_UPDATE = 5;
	
	NotificationManager nm;
	ArrayList<Integer> idsArray;


	@Override
	public IBinder onBind(Intent intent) {
		
		return null;
	}
	
	
	@Override
	public void onCreate() {
		nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		idsArray = new ArrayList<Integer>();
		super.onCreate();
	}
	
	@Override
	public void onDestroy() {
		for (Integer id : idsArray) 
			nm.cancel(id.intValue());
		super.onDestroy();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		//super.onStartCommand(intent, flags, startId);
		
		Log.d(LOG_TAG,"NotificationService started");
		
		LocationManager locManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				UPDATE_PERIOD,
				LOCATION_MIN_UPDATE,
				locationListener);
		
		Log.d(LOG_TAG,"Location Manager is registered");
		
		
		return START_STICKY;
	}
	
	/**
	 * Reminders which is nearby our location and must be notified to user
	 * @param loc Our Location. Must be passed by LocationManager
	 * @return Array of Target Reminders
	 */
	private ArrayList<Recordatorio> recordatoriosToNotify(Location loc) {
		ArrayList<Recordatorio> resRecordatorios = new ArrayList<Recordatorio>();
		RecordatoriosDB recsDB = new RecordatoriosDB(getApplicationContext());
		ArrayList<Recordatorio> activeRecordatorios = recsDB.listarRecordatoriosActivos();
		
		for (Recordatorio rec : activeRecordatorios) {
			float[] results = new float[3];
			Location.distanceBetween(loc.getLatitude(), loc.getLongitude(), rec.getLatitud(), rec.getLongitud(), results);
			double metters = distanceBetween(loc.getLatitude(), loc.getLongitude(), rec.getLatitud(), rec.getLongitud());
			Log.d(LOG_TAG,"Active recordatorio");
			Log.d(LOG_TAG,"My location{ latitude:" + String.valueOf(loc.getLatitude()) + ", longitud:" + String.valueOf(loc.getLongitude())+ "}");
			Log.d(LOG_TAG,rec.toString());
			Log.d(LOG_TAG,"Distance: " + String.valueOf(results[0]));
			Log.d(LOG_TAG,"Distance other: " + String.valueOf(metters));
			Location locB = new Location("POINT B");
			loc.setLatitude(rec.getLatitud());
			loc.setLongitude(rec.getLongitud());
			float distance = loc.distanceTo(locB);
			Log.d(LOG_TAG,"Distance distanceTo: " + String.valueOf(distance));
			if (results[0]<=rec.getRadius()) {
				Log.d(LOG_TAG,"Added to Active Recordatorio list");	
				resRecordatorios.add(rec);
			}
		}	
		recsDB.close();
		return resRecordatorios;
	}
	
	/**
	 * Calculates distance between two geographics points
	 * @param lat_a Latitude point A
	 * @param lng_a Longitude point A
	 * @param lat_b Latitude point B
	 * @param lng_b Longitude point B
	 * @return Meters between two points
	 */
	private double distanceBetween(double lat_a, double lng_a, double lat_b, double lng_b) {
		double pk = (float) (180/3.14169);

	    double a1 = lat_a / pk;
	    double a2 = lng_a / pk;
	    double b1 = lat_b / pk;
	    double b2 = lng_b / pk;

	    double t1 = Math.cos(a1)*Math.cos(a2)*Math.cos(b1)*Math.cos(b2);
	    double t2 = Math.cos(a1)*Math.sin(a2)*Math.cos(b1)*Math.sin(b2);
	    double t3 = Math.sin(a1)*Math.sin(b1);
	    double tt = Math.acos(t1 + t2 + t3);

	    return 6366000*tt;
	}
	
	/**
	 * Send a Notification in Android Notification Centre about Reminders
	 * @param rec Reminder to notify
	 */
	private void sendNotification(Recordatorio rec) {
		//Create notification
		Notification notif =  new Notification(
				R.drawable.ic_notification,
				rec.getNombre(),
				System.currentTimeMillis());

		//Create Pending Intent what start Activity associated to Notification
		Intent intent = new Intent(getApplicationContext(), NuevoRecordatorio.class);
		intent.putExtra(NuevoRecordatorio.KEY_NOTIFY_RECORDATORIO, rec.getId());
		PendingIntent pendInt = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
		notif.setLatestEventInfo(getApplicationContext(), rec.getNombre(), rec.getDescripcion(), pendInt);
		
		Log.d(LOG_TAG,"Sending notification for Recordatorio with name: " + rec.getNombre() + " and id: " + rec.getId() + " and address: " + rec.getDireccion());
		nm.notify(rec.getId(), notif);
		
		idsArray.add(Integer.valueOf(rec.getId()));
	}
	
	
	LocationListener locationListener = new LocationListener() {
		
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onLocationChanged(Location location) {
			Log.d(LOG_TAG,"Location changed is called");
			ArrayList<Recordatorio> recs = recordatoriosToNotify(location);
			for (Recordatorio r : recs) 
				sendNotification(r);
		}
	};
	
	
	
	
    
    
    /*private void sendNotification(String[] geofenceIds) {
    	// Create an explicit content Intent that starts the main Activity
        Intent notificationIntent =
                new Intent(getApplicationContext(),MainActivity.class);
        
        
        // Construct a task stack
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        // Adds the main Activity to the task stack as the parent
        stackBuilder.addParentStack(MainActivity.class);

        // Push the content Intent onto the stack
        stackBuilder.addNextIntent(notificationIntent);
        

        // Get a PendingIntent containing the entire back stack
        PendingIntent notificationPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        

        
        
        //Get Recordatorio Object based on Geofence Id
        RecordatoriosDB recordatorios = new RecordatoriosDB(getApplicationContext());
        Recordatorio[] listRecordatorio = new Recordatorio[geofenceIds.length];
        for (int i = 0; i<geofenceIds.length; i++)
        	listRecordatorio[i] = recordatorios.findByGeofenceId(geofenceIds[i]);
        	
        
        
        // Get an instance of the Notification manager
        NotificationManager mNotificationManager =
            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        
        // Set the notification contents
        for (Recordatorio rec : listRecordatorio) {
        	// Get a notification builder that's compatible with platform versions >= 4
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            
            //Set up notification
	        builder.setSmallIcon(R.drawable.ic_notification)
	               .setContentTitle(rec.getNombre())
	               .setContentText(rec.getDescripcion())
	               .setContentIntent(notificationPendingIntent);	        
	
	        // Issue the notification
	        mNotificationManager.notify(0, builder.build());
        }
    }*/
}
