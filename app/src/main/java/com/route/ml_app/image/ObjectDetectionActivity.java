package com.route.ml_app.image;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.objects.DetectedObject;
import com.google.mlkit.vision.objects.ObjectDetection;
import com.google.mlkit.vision.objects.ObjectDetector;
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions;
import com.route.ml_app.Helper.BoxWithLabel;
import com.route.ml_app.Helper.HelperImageActivity;

import java.util.ArrayList;
import java.util.List;

public class ObjectDetectionActivity extends HelperImageActivity {
    ObjectDetector objectDetector;//define object Detector
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ObjectDetectorOptions options =  //initialize object detector
                new ObjectDetectorOptions.Builder()
                        .setDetectorMode(ObjectDetectorOptions.SINGLE_IMAGE_MODE)
                        .enableMultipleObjects()
                        .enableClassification()
                        .build();

        objectDetector = ObjectDetection.getClient(options);
    }

    @Override
    protected void runClassification(Bitmap bitmap) {
        InputImage inputImage = InputImage.fromBitmap(bitmap,0);
        objectDetector.process(inputImage).addOnSuccessListener(new OnSuccessListener<List<DetectedObject>>() {
            @Override
            public void onSuccess(List<DetectedObject> detectedObjects) {
                StringBuilder builder = new StringBuilder();
                List<BoxWithLabel> boxs = new ArrayList<>();
                if (!detectedObjects.isEmpty()) {
                    for (DetectedObject object : detectedObjects) {
                        if (!object.getLabels().isEmpty()) {
                            String label = object.getLabels().get(0).getText();
                            builder.append(label).append(" : ")
                                    .append(object.getLabels().get(0).getConfidence())
                                    .append("\n");
                            boxs.add(new BoxWithLabel(object.getBoundingBox(),label));
                            Log.d("DetectionObject", "detected objects : " + label);
                        }
                    }
                    getOutputTextView().setText(builder.toString());
                    drawDetectionResult(boxs,bitmap);

                } else {
                    builder.append("unKnown").append("\n");
                    getOutputTextView().setText(builder.toString());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });

    }
}
