package com.example.moamen.multimodalinteraction;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.camera2.params.Face;
import com.google.android.gms.vision.face.FaceDetector;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Frame;


public class SimplePictureRequest extends AppCompatActivity {
    private final int RC_PICTURE_TAKEN = 1111;
    private final int RC_PERMISSIONS = 2222;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, RC_PERMISSIONS);
            finish();
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        // when the user clicks the button… {
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePicture, RC_PICTURE_TAKEN);

    }

    // this method gets called when you return from the camera application, with the picture included within
    // the data object
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Intent welcome = new Intent(this, WelcomeScreen.class);

        if (requestCode == RC_PICTURE_TAKEN && resultCode == RESULT_OK) {

            // the newly taken photo is now stored in a Bitmap object
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");

            // … handle the facial recognition etc. here

            FaceDetector detector = new FaceDetector.Builder(getApplicationContext())
                    .setTrackingEnabled(false)
                    .setProminentFaceOnly(true).build();

            // Copy and create the SafeFaceDetector class from the link on the next page
            SafeFaceDetector safeDetector = new SafeFaceDetector(detector);

            // Create a frame object from the bitmap and run face detection on the frame.
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            SparseArray<com.google.android.gms.vision.face.Face> faces = safeDetector.detect(frame);

            // Number of faces detected (there should be only one with .setProminentFaceOnly(true)
            Log.d("TAG", "faces detected: " + faces.size());
            // Get the first face in the faces array (you might have to add a check here that the array has any faces!)
            com.google.android.gms.vision.face.Face face = faces.get(0);
            Log.d("TAG", "faces detected: " + face);


            if (faces.size() == 1) {
                // release the objects for reuse
                safeDetector.release();
                bitmap.recycle();
                startActivity(welcome);
            }


            if (requestCode == RC_PERMISSIONS && resultCode == RESULT_OK) {
                // restart the activity if you arrive here from the permission dialog
                Intent reboot = new Intent(this, SimplePictureRequest.class);
                startActivity(reboot);
            }
        }
    }

}
