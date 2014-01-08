package com.adm.geoadm;

import com.adm.geoadm.services.NotificationService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class InitializerService extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent initService = new Intent();
		initService.setAction("NotificationService");
		context.startService(initService);
	}

}
