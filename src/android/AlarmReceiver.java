package de.appplant.cordova.plugin.background;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

  public static String ACTION_ALARM_START = "ACTION_ALARM_START";
  public static String ACTION_ALARM_RUN = "ACTION_ALARM_RUN";
  public static String ACTION_ALARM_STOP = "ACTION_ALARM_STOP";

  public static boolean isStop = true;

  private PendingIntent pendingIntent = null;

  @Override
  public void onReceive(Context context, Intent intent) {

    if (intent.getAction().equals(ACTION_ALARM_START)) {
      isStop = false;
      setAlarm(context, 5000);
    }

    if (intent.getAction().equals(ACTION_ALARM_RUN)) {
//      Toast.makeText(context, "定时唤醒", Toast.LENGTH_SHORT).show();
      if (isStop) {
        if (pendingIntent != null) {
          AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
          am.cancel(pendingIntent);
          pendingIntent = null;
        }
      } else {
        setAlarm(context, System.currentTimeMillis() + 5000);
      }
    }

    if (intent.getAction().equals(ACTION_ALARM_STOP)) {
      isStop = true;
      if (pendingIntent != null) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.cancel(pendingIntent);
        pendingIntent = null;
      }
    }
  }

  private void setAlarm(Context context, long time) {
    AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    Intent intent = new Intent(context, AlarmReceiver.class);
    intent.setAction(ACTION_ALARM_RUN);
    pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      AlarmManager.AlarmClockInfo info = new AlarmManager.AlarmClockInfo(time, pendingIntent);
      am.setAlarmClock(info, pendingIntent);
    }
  }
}
