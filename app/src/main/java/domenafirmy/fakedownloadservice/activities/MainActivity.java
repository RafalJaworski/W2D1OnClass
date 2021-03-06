package domenafirmy.fakedownloadservice.activities;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import domenafirmy.fakedownloadservice.R;

public class MainActivity extends AppCompatActivity implements fakeDownloadService.BinderToActivityConnection {

    @Bind(R.id.state)
    protected TextView state;

    protected fakeDownloadService.ServiceBinder binder;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (fakeDownloadService.ServiceBinder) service;
            Log.d("Main", "onServiceConnected");
            //odczytanie bierzacej liczby zadan
            state.setText(Integer.toString(binder.getPendingTasksCount()));
            //ustawia klase obserwujaca zmiany w binder
            binder.setObserver(MainActivity.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            binder = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Log.d("Main", "onCreate");
        //podlaczanie do servisu
        final Intent bindIntent = new Intent(this,fakeDownloadService.class);
        bindService(bindIntent,serviceConnection , Service.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }

    @OnClick(R.id.download_start)
    public void onStartClick()
    {
        Intent startIntent = new Intent(this,fakeDownloadService.class);
        startService(startIntent);
    }

    @OnClick(R.id.download_stop)
    public void onStopClick()
    {
        Intent startIntent = new Intent(this,fakeDownloadService.class);
       stopService(startIntent);
    }

    @Override
    public void onTasksRefresh() {
        state.setText(Integer.toString(binder.getPendingTasksCount()));
    }
}
