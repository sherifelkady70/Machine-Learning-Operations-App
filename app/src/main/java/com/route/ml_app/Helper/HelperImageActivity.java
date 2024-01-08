package com.route.ml_app.Helper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.label.ImageLabel;
import com.google.mlkit.vision.label.ImageLabeler;
import com.google.mlkit.vision.label.ImageLabeling;
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions;
import com.route.ml_app.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class HelperImageActivity extends AppCompatActivity {
    ImageView inputImage;
    TextView outputTextView;
    int REQUEST_PICK_CODE=1000;
    int REQUEST_CAPTURE_IMAGE=1001;
    private File photoFile;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helper_image);

        inputImage = findViewById(R.id.imageViewPickImage);
        outputTextView = findViewById(R.id.textView);


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){ //Validation for SDK and Android Versions

            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) //if permission not success create one
            {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0); //ask user to access images , videsos , etc...
            }
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data); //the result returned from user

        if( resultCode == RESULT_OK ){ //check if user selected that image
            if( requestCode == REQUEST_PICK_CODE ){
                Uri uri = data.getData(); //uri for specific image
                Bitmap bitmap = loadFromUri(uri); //load the image and store it in Bitmap's object
                inputImage.setImageBitmap(bitmap); //display image in view
                runClassification(bitmap); //make classifications by InputImage using ImageLabeler
            } else if (requestCode==REQUEST_CAPTURE_IMAGE) {
                Bitmap bitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                inputImage.setImageBitmap(bitmap);
                runClassification(bitmap);
            }
        }
    }

    public Bitmap loadFromUri(Uri uri){ //method to load image bu uri and return it in Bitmap's object
        Bitmap bitmap = null;

        try{
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.O_MR1){
                ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(),uri);
                bitmap=ImageDecoder.decodeBitmap(source);
            }else {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
            }
        }catch (IOException e){
            e.printStackTrace();
        }

        return bitmap;
    }

    public void onPickImage(View view){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,REQUEST_PICK_CODE);
    }
    public void onPickPhoto(View view){
        //create file to share with camera
        photoFile = createPhotoFile();
        Uri fileUri = FileProvider.getUriForFile(this,"com.iago.fileprovider",photoFile); //Error here

        //make a intent
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,fileUri);
        //startActivityForResult
        startActivityForResult(intent,REQUEST_CAPTURE_IMAGE);
        Log.d("onPickPhoto", "onPickPhoto: " + photoFile);
    }

    public File createPhotoFile(){
        File photoFileDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "ML_IMAGE_HELPER");
        if(!photoFileDir.exists()){
            photoFileDir.mkdirs();
            //to work must the parent directory is exist
        }
        String name = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
         File file = new File(photoFileDir.getPath() + File.separator + name);
        return file;
    }


    protected void runClassification(Bitmap bitmap){

    }

    protected TextView getOutputTextView(){
        return outputTextView;
    }
    protected ImageView getInputImage(){
        return inputImage;
    }

    protected void drawDetectionResult(List<BoxWithLabel> boxes , Bitmap bitmap){
        Bitmap outPutBitmap = bitmap.copy(Bitmap.Config.ARGB_8888,true);
        Canvas canvas = new Canvas(outPutBitmap);
        Paint penRect = new Paint();
        penRect.setColor(Color.RED);
        penRect.setStyle(Paint.Style.STROKE);
        penRect.setStrokeWidth(8f);

        Paint penLabel = new Paint();
        penLabel.setColor(Color.YELLOW);
        penLabel.setStyle(Paint.Style.FILL_AND_STROKE);
        penLabel.setTextSize(90f);

        for(BoxWithLabel boxWithLabel : boxes){
            canvas.drawRect(boxWithLabel.rect,penRect);

            Rect labelSize = new Rect(0,0,0,0);
            penLabel.getTextBounds(boxWithLabel.label, 0,boxWithLabel.label.length(),labelSize);
            float fontSize = penLabel.getTextSize()*boxWithLabel.rect.width()/labelSize.width();
            if(fontSize<penLabel.getTextSize()){
                penLabel.setTextSize(fontSize);
            }

            canvas.drawText(boxWithLabel.label,boxWithLabel.rect.left,
                    boxWithLabel.rect.top+labelSize.height(),penLabel);
        }
        getInputImage().setImageBitmap(outPutBitmap);
    }

}