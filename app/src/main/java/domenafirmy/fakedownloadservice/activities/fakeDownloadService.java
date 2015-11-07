package domenafirmy.fakedownloadservice.activities;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Random;

/**
 * Created by jawa on 07/11/2015.
 */
public class fakeDownloadService extends Service{

    private Random randomGenerator; //objekt generujacyliczby losowe

    @Override
    public void onCreate() {
        super.onCreate();
        randomGenerator = new Random();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static class DownloadAsyncTask extends AsyncTask<Long,Void,Void>{

        private Service service;


        public DownloadAsyncTask(Service service) {
            this.service = service;
        }

        @Override
        protected Void doInBackground(Long... params) {

            try {
                Thread.sleep(params[0]);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}
