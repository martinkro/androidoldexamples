package com.personal.tools.permissionold;


import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
//import android.support.v4.content.PermissionChecker;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener{
    public static final String LOG_TAG = "permissionold";
    private static final int CAMERA_REQUEST = 1888;  
    private Camera camera;
    private int cameraId = 0;
    private boolean safeToTakePicture = false;
    
    ImageView mImagePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        findViewById(R.id.testStorage).setOnClickListener(this);
        findViewById(R.id.testCamera).setOnClickListener(this);
        mImagePhoto = (ImageView)findViewById(R.id.imagePhoto);
        
     // do we have a camera?
        if (!getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Toast.makeText(this, "No camera on this device", Toast.LENGTH_LONG)
                    .show();
        } else {
            cameraId = findFrontFacingCamera();
            if (cameraId < 0) {
                Toast.makeText(this, "No front facing camera found.",
                        Toast.LENGTH_LONG).show();
            } else {
                camera = Camera.open(cameraId);
            }
        }
        
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    private void testExternalStorage(){
        getTopActivity();
        File dataDir = Environment.getDataDirectory();
        File externalStorageDir = Environment.getExternalStorageDirectory();
        Log.d(LOG_TAG,"dataDir:" + dataDir.getAbsolutePath());
        Log.d(LOG_TAG,"externalStorageDir:" + externalStorageDir.getAbsolutePath());
        File externalTestDir = getExternalFilesDir("test");
        Log.d(LOG_TAG,"externalTestDir:" + externalTestDir.getAbsolutePath());
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
        String date = dateFormat.format(new Date());
        String photoFile = "Test_" + date + ".txt";

        String filename =externalTestDir.getPath() + File.separator + photoFile;

        File pictureFile = new File(filename);
        
        //PermissionChecker.checkSelfPermission(MainActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE);
        //PermissionChecker

        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            fos.write(filename.getBytes());
            fos.close();
            Toast.makeText(this, "New Text saved:" + photoFile,
                    Toast.LENGTH_LONG).show();
        } catch (Exception error) {
            Log.d(LOG_TAG, "File" + filename + " not saved: "
                    + error.getMessage());
            Toast.makeText(this, "Text could not be saved.",
                    Toast.LENGTH_LONG).show();
        }
    }
    
    private void testCamera(){
        //testCameraFront();
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);  
        startActivityForResult(cameraIntent, CAMERA_REQUEST);  
    }
    
    private void testCameraFront(){
        if (safeToTakePicture) { 
            camera.takePicture(null, null, new PhotoHandler(getApplicationContext())); 
            safeToTakePicture = false;
        }
        //camera.startPreview();
        //camera.takePicture(null, null,new PhotoHandler(getApplicationContext()));
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
        if (requestCode == CAMERA_REQUEST) {  
         Bitmap photo = (Bitmap) data.getExtras().get("data");  
         mImagePhoto.setImageBitmap(photo);
        }  
    }
    
    private int findFrontFacingCamera() {
        int cameraId = -1;
        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            CameraInfo info = new CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
                Log.d(LOG_TAG, "Camera found");
                cameraId = i;
                break;
            }
        }
        return cameraId;
    }
    
    @Override
    protected void onPause() {
        if (camera != null) {
            camera.release();
            camera = null;
        }
        super.onPause();
    }
    
    public void onClick(View v){
        switch(v.getId()){
        case R.id.testStorage:
            testExternalStorage();
            break;
        case R.id.testCamera:
            testCamera();
            break;
        default:
            break;
        }
    }
    
    private void getTopActivity(){
        ActivityManager activityMgr = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> apps = activityMgr.getRunningAppProcesses();
        for(RunningAppProcessInfo app:apps){
            
        }
        
        List<RunningTaskInfo> tasks = activityMgr.getRunningTasks(2);
        for(RunningTaskInfo task:tasks){
            Log.d(LOG_TAG,"topActivity:" + task.topActivity);
        }
    }

}
