package com.route.ml_app.image;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.label.ImageLabel;
import com.google.mlkit.vision.label.ImageLabeler;
import com.google.mlkit.vision.label.ImageLabeling;
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions;
import com.route.ml_app.Helper.HelperImageActivity;

import java.util.List;

public class ImageClassificationActivity extends HelperImageActivity {
    private ImageLabeler imageLabeler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        imageLabeler = ImageLabeling.getClient(new ImageLabelerOptions
                .Builder()
                .setConfidenceThreshold(0.5f)
                .build()); //now building the image labeler to pass it the bitmap



    }

    @Override
    protected void runClassification(Bitmap bitmap) {
        InputImage inputImage1 = InputImage.fromBitmap(bitmap,0);
        imageLabeler.process(inputImage1).addOnSuccessListener(new OnSuccessListener<List<ImageLabel>>() {
            @Override
            public void onSuccess(List<ImageLabel> imageLabels) {
                StringBuilder stringBuilder = new StringBuilder();
                if(imageLabels.size() > 0) {
                    for (ImageLabel label : imageLabels) {
                        stringBuilder.append(label.getText())
                                .append(" : ")
                                .append(label.getConfidence())
                                .append("\n");
                    }
                    getOutputTextView().setText(stringBuilder.toString());
                }else {
                    getOutputTextView().setText("Could not Classify");
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
