package domenafirmy.fakedownloadservice.activities;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import domenafirmy.fakedownloadservice.R;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.state)
    protected TextView state;

    protected fakeDownloadService.ServiceBinder binder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //podlaczanie do servisu
        final Intent bindIntent = new Intent(this,fakeDownloadService.class);
        bindService(bindIntent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                binder = (fakeDownloadService.ServiceBinder) service;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                binder = null;
            }
        }, Service.BIND_AUTO_CREATE);
    }

    @OnClick(R.id.download_start)
    public void onStartClick()
    {
        Intent startIntent = new Intent(this,fakeDownloadService.class);
        this.startService(startIntent);
    }

    @OnClick(R.id.download_stop)
    public void onStopClick()
    {
        Intent startIntent = new Intent(this,fakeDownloadService.class);
        this.stopService(startIntent);
    }
}
