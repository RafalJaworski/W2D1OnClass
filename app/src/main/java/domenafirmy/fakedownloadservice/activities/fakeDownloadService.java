package domenafirmy.fakedownloadservice.activities;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class fakeDownloadService extends Service{

    //urzywamy listy bo bedziemy mieli przy okazji kolejnosc
    private List<DownloadAsyncTask> runningTask;
    //pula watkow
    private ExecutorService threadPool;

    private Random randomGenerator; //objekt generujacyliczby losowe

    private ServiceBinder currentBinder;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("Service", "onCreate");
        randomGenerator = new Random();
        runningTask = new ArrayList<>();
        //tworzymy pule watkow skladajacych sie zawsze z 2 watkow
        threadPool = Executors.newFixedThreadPool(2);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("Service", "onStartCommand");
        //jesli binder instnieje to powiadom go o zmianie
        if(null != currentBinder){
            currentBinder.notifyObserver();
        }
        DownloadAsyncTask newTask = new DownloadAsyncTask(this,startId);
        //dodajemy nowe zadanie do listy
        runningTask.add(newTask);
        newTask.executeOnExecutor(threadPool,randomGenerator.nextInt(10)*1000L);

        return Service.START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        Log.d("Service", "onDestroy");
        super.onDestroy();
        for(DownloadAsyncTask i: runningTask ) {
            i.cancel(true);
            i.service = null;
        }
        //zamknieci puli watkow
        threadPool.shutdown();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if(null == currentBinder){
            currentBinder = new ServiceBinder(this);
            return new ServiceBinder(this);
        }
        //null - nie pozwalamy innym sie podlaczyc
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        //czyscimy bierzacy binder poniewaz klient sie rozlaczyl
        currentBinder = null;
        //false zeby nie mogla byc wywolana metoda rebind
        return false;
    }

    public static class DownloadAsyncTask extends AsyncTask<Long,Void,Void>{
        //w Service nie ma listy runningTask wiec typujemy na nasz servis
        private fakeDownloadService service;
        private int startId;

        public DownloadAsyncTask(fakeDownloadService service,int startId) {
            this.service = service;
            this.startId = startId;
        }

        @Override
        protected Void doInBackground(Long... params) {
            Log.d("AsyncTask","doInBackground"+startId+"thread="+Thread.currentThread().getId());
            try {
                Thread.sleep(params[0]);
            } catch (InterruptedException e) {
                Log.d("AsyncTask","InterruptedException");
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            Log.d("AsyncTask","onPostExecute"+startId);
            super.onPostExecute(aVoid);
            service.runningTask.remove(this);
            //stopself moze przyjmowac int startId
            service.stopSelf(startId);
        }
    }

    public static class ServiceBinder extends Binder{
        private fakeDownloadService service;
        private BinderToActivityConnection observer;

        public ServiceBinder(fakeDownloadService service) {
            this.service = service;
        }

        public int getPendingTasksCount(){
            return this.service.runningTask.size();
        }

        public void setObserver(BinderToActivityConnection observer) {
            this.observer = observer;
        }

        public void notifyObserver()
        {
            if(null != observer){
                observer.onTasksRefresh();
            }
        }
    }

    public interface BinderToActivityConnection
    {
        public void onTasksRefresh();
    }
}
