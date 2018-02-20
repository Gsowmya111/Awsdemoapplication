package com.example.sowmyaram.awsdemoapplication;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.bumptech.glide.Glide;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by sowmyaram on 8/16/2017.
 */

public class Demo extends AppCompatActivity {
    AmazonS3Client s3;
    BasicAWSCredentials credentials;
    TransferUtility transferUtility;
    TransferObserver transferObserver;
   // File uploadToS3 = new File("/storage/emulated/0/Samsung/Image/1024x600_01.jpg");
    //file which is created will be located in this location
   File uploadToS3 = new File("/data/data/com.example.sowmyaram.awsdemoapplication/files/mytextfile.txt/");
  //  File downloadFromS3 = new File("/storage/emulated/0/Samsung/Image/bell_off.png");
    String bucket = "kaapiwalaa8051";
    String key = "AKIAJPJJ3MAUSI6TQAMA";
    String secret = "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";
    ProgressBar pb;
    TextView _status,filetext;
    Button download;
    ImageView img_downl;
    String rawdata;
    static final int READ_BLOCK_SIZE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WriteBtn();

        credentials = new BasicAWSCredentials(key,secret);
        s3 = new AmazonS3Client(credentials);


        transferUtility = new TransferUtility(s3, this);

      /*  observer = transferUtility.upload(
               // "https://s3.ap-south-1.amazonaws.com/kaapiwaala/bell_on.png",
                "https://s3.ap-south-1.amazonaws.com/kaapiwaala/",
                "MyfileName",
                new File("bell_on.png.jpg")
        );*/


        transferObserver = transferUtility.upload(
                bucket,     /* The bucket to upload to */
                "mytextfile.txt",    /* The key for the uploaded object */
                uploadToS3       /* The file where the data to upload exists */
        );


        filetext= (TextView) findViewById(R.id.textfile);
        _status= (TextView) findViewById(R.id.textViewstatus);
        Button uplod = (Button) findViewById(R.id.uploadSd3);
         download = (Button) findViewById(R.id.downloadSd3);
        Button fetchfile = (Button) findViewById(R.id.fetchFile);
        img_downl = (ImageView) findViewById(R.id.imageView_download);



        fetchfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReadBtn();

            }
        });



        uplod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                transferObserver.setTransferListener(new TransferListener() {
            @Override
            public void onStateChanged(int id, TransferState state) {

            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                long _bytesCurrent = bytesCurrent;
                long _bytesTotal = bytesTotal;

                float percentage =  ((float)_bytesCurrent /(float)_bytesTotal * 100);
                Log.d("percentage","" +percentage);
              //  pb.setProgress((int) percentage);
                _status.setText(percentage + "%");
                Toast.makeText(Demo.this, "Image uploded to server sucessfully " + percentage, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(int id, Exception ex) {
                Toast.makeText(Demo.this, "" + ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


               // downloadButton = (Button) findViewById(R.id.cognito_s3DownloadButton);
                download.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Glide.with(getBaseContext())
                                .load("https://s3.amazonaws.com/sudeekesha/1024x600_05.png")
                                .centerCrop()
                                .into(img_downl);
                    }
                });



                //s3credentialsProvider();
            }
        });
    }

    public void WriteBtn() {
        // add-write text into file
        try {
            FileOutputStream fileout = openFileOutput("mytextfile.txt", MODE_PRIVATE);
            OutputStreamWriter outputWriter = new OutputStreamWriter(fileout);
            rawdata="0022010101"+"8001010101615151"+"0@#$010101";
            outputWriter.write(rawdata);
            outputWriter.close();

            //display file saved message
            Toast.makeText(getBaseContext(), "File saved successfully!",
                    Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Read text from file
    public void ReadBtn() {
        //reading text from file
        try {
            FileInputStream fileIn=openFileInput("mytextfile.txt");
            InputStreamReader InputRead= new InputStreamReader(fileIn);

            char[] inputBuffer= new char[READ_BLOCK_SIZE];
            String s="";
            int charRead;

            while ((charRead=InputRead.read(inputBuffer))>0) {
                // char to string conversion
                String readstring=String.copyValueOf(inputBuffer,0,charRead);
                s +=readstring;
            }
            InputRead.close();
            filetext.setText(s);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}