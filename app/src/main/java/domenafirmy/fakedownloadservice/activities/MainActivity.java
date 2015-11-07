package domenafirmy.fakedownloadservice.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.ButterKnife;
import butterknife.OnClick;
import domenafirmy.fakedownloadservice.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
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
