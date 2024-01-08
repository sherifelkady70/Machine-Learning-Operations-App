package com.route.ml_app.image;

import android.graphics.Bitmap;
import android.media.FaceDetector;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetectorOptions;
import com.route.ml_app.Helper.BoxWithLabel;
import com.route.ml_app.Helper.HelperImageActivity;



import java.util.ArrayList;
import java.util.List;

public class FaceDectectionActivity extends HelperImageActivity {
    // Instead of importing, use fully qualified class name
    private com.google.mlkit.vision.face.FaceDetector faceDetector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FaceDetectorOptions highAccurateopts = new FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                .enableTracking()
                .build();


        faceDetector = FaceDetection.getClient(highAccurateopts);
    }

    @Override
    protected void runClassification(Bitmap bitmap) {
        Bitmap outPutBitmap = bitmap.copy(Bitmap.Config.ARGB_8888,true);
        InputImage inputImage = InputImage.fromBitmap(outPutBitmap,0);
        faceDetector.process(inputImage).addOnSuccessListener(new OnSuccessListener<List<Face>>() {
            @Override
            public void onSuccess(List<Face> faces) {
                if(faces.isEmpty()){
                    getOutputTextView().setText("No Face Detected");
                }else {
                    List<BoxWithLabel> boxs = new ArrayList<>();
                    for(Face face : faces){
                        BoxWithLabel boxWithLabel = new BoxWithLabel(face.getBoundingBox()
                                ,face.getTrackingId()+"");
                        boxs.add(boxWithLabel);
                    }
                    drawDetectionResult(boxs,bitmap);
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
