package domenafirmy.fakedownloadservice.activities;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by jawa on 07/11/2015.
 */
public class fakeDownloadService extends Service{

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_REDELIVER_INTENT;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static class DownloadAsyncTask extends AsyncTask<Long,Void,Void>{


        @Override
        protected Void doInBackground(Long... params) {

            try {
                Thread.sleep(params[0]);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
