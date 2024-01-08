package com.route.ml_app.Helper;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.route.ml_app.R;

public class AudioHelperActivity extends AppCompatActivity {
    protected Button startRecordingBtn;
    protected Button stopRecordingBtn;
    protected TextView txtOutput;
    protected TextView txtSpecs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_helper);

        initViews();
        stopRecordingBtn.setEnabled(false);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){ //Validation for SDK and Android Versions

            if(checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) //if permission not success create one
            {
                requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, 0); //ask user to access images , videsos , etc...
            }
        }

    }
    private void initViews(){
        startRecordingBtn=findViewById(R.id.startRecordBtn);
        stopRecordingBtn=findViewById(R.id.stopRecordBtn);
        txtOutput=findViewById(R.id.txtForOutput);
        txtSpecs=findViewById(R.id.txtForSpecs);
    }

    public void startRecording(View view){
        startRecordingBtn.setEnabled(false);
        stopRecordingBtn.setEnabled(true);
    }
    public void stopRecording(View view){
        startRecordingBtn.setEnabled(true);
        stopRecordingBtn.setEnabled(false);
    }
}