package com.imcgeek.testcamerax;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraX;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureConfig;
import androidx.camera.core.Preview;
import androidx.camera.core.PreviewConfig;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Rational;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private int REQUEST_CODE_PERMISSIONS = 101;
    private String [] REQUIRED_PERMISSIONS = new String [] {
            "android.permission.CAMERA",
            "android.permission.WRITE_EXTERNAL_STORAGE"
    };

    TextureView textureView;
    FloatingActionButton cameraButton;
    FloatingActionButton flashButton ;

    Preview preview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        textureView = (TextureView) findViewById(R.id.view_finder);
        cameraButton = (FloatingActionButton) findViewById(R.id.button_capture);
        flashButton = (FloatingActionButton) findViewById(R.id.button_flash);


        if(allPermissions()) {
            startCamera();
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }

    }

    private void startCamera() {
        CameraX.unbindAll();
        Rational aspectRatio = new Rational(textureView.getWidth(), textureView.getHeight());
        Size screen = new Size(textureView.getWidth(), textureView.getHeight());
        PreviewConfig pConfig = new PreviewConfig.Builder()
                .setTargetAspectRatio(aspectRatio)
                .setTargetResolution(screen)
                .build();
        preview = new Preview(pConfig);
        preview.setOnPreviewOutputUpdateListener(new Preview.OnPreviewOutputUpdateListener() {
            @Override
            public void onUpdated(Preview.PreviewOutput output) {
                ViewGroup parent = (ViewGroup) textureView.getParent();
                parent.removeView(textureView);
                parent.addView(textureView);

                textureView.setSurfaceTexture(output.getSurfaceTexture());

                updateTransform();
            }
        });


        ImageCaptureConfig imageCaptureConfig = new ImageCaptureConfig.Builder()
                .setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY)
                .setTargetRotation(getWindowManager().getDefaultDisplay().getRotation())
                .build();
        final ImageCapture imgCap = new ImageCapture(imageCaptureConfig);

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date fecha = new Date();
                SimpleDateFormat dt = new SimpleDateFormat("yyyyMMdd_HHmmss");

                String photoFileName = dt.format(fecha);
                File file = new File("/storage/emulated/0/DCIM/Camera/"+photoFileName+".jpg");
                imgCap.takePicture(file, new ImageCapture.OnImageSavedListener() {
                    @Override
                    public void onImageSaved(@NonNull File file) {
                        String msg = "Pic captured at " + file.getAbsolutePath();
                        Toast.makeText(getBaseContext(),msg,Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onError(@NonNull ImageCapture.UseCaseError useCaseError, @NonNull String message, @Nullable Throwable cause) {
                        String msg = "Pic capture failed "+message;
                        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();

                        if (cause != null) {
                            cause.printStackTrace();
                        }

                    }
                });
            }
        });

        flashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (preview.isTorchOn()) {
                    preview.enableTorch(false);
                    flashButton.setImageResource(R.drawable.ic_flash_off);
                } else {
                    preview.enableTorch(true);
                    flashButton.setImageResource(R.drawable.ic_flash_on);
                }
            }
        });

        CameraX.bindToLifecycle(this, preview, imgCap);
    }

    private void updateTransform() {
        Matrix mx = new Matrix();
        float w = textureView.getMeasuredWidth();
        float h = textureView.getMeasuredHeight();

        float cX = w / 2f;
        float cY = h / 2f;

        int rotationDgr ;
        int rotation = (int) textureView.getRotation();
        switch (rotation) {
            case Surface.ROTATION_0 :
                rotationDgr = 0;
                break;
            case Surface.ROTATION_90 :
                rotationDgr = 90;
                break;
            case Surface.ROTATION_180 :
                rotationDgr = 180;
                break;
            case Surface.ROTATION_270 :
                rotationDgr = 270;
                break;
            default:
                return;
        }
        mx.postRotate((float)rotationDgr, cX, cY);
        textureView.setTransform(mx);
    }

    private boolean allPermissions() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
}
