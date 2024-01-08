package com.route.ml_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.route.ml_app.Helper.AudioHelperActivity;
import com.route.ml_app.image.FaceDectectionActivity;
import com.route.ml_app.image.FlowerIdentificationActivity;
import com.route.ml_app.image.ImageClassificationActivity;
import com.route.ml_app.image.ObjectDetectionActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onGotoImageClassificationActivity(View view){
        //start the image classification activity
        Intent intent = new Intent(this, ImageClassificationActivity.class);
        startActivity(intent);
    }

    public void onGotoFlowerIdentificationActivity(View view){
        //start the flower identification activity
        Intent intent = new Intent(this, FlowerIdentificationActivity.class);
        startActivity(intent);
    }
    public void onGotoObjectDetectionActivity(View view){
        //start the object detection activity
        Intent intent = new Intent(this, ObjectDetectionActivity.class);
        startActivity(intent);
    }
    public void onGotoFaceDetectionActivity(View view){
        //start the Face detection activity
        Intent intent = new Intent(this, FaceDectectionActivity.class);
        startActivity(intent);
    }

    public void onGotoAudioClassification(View view){
        //start the Audio Helper activity
        Intent intent = new Intent(this, AudioHelperActivity.class);
        startActivity(intent);
    }
}