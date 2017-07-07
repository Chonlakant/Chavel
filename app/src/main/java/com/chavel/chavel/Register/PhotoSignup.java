package com.chavel.chavel.Register;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chavel.chavel.AppConfig;
import com.chavel.chavel.Home.HomeActivity;
import com.chavel.chavel.MainActivity;
import com.chavel.chavel.R;
import com.chavel.chavel.StaticActivity;
import com.chavel.chavel.Utility.FileUtils;
import com.chavel.chavel.Utility.ImageUtils;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class PhotoSignup extends Activity {
    public String PHONE="";
    public String EMAIL="";
    public  String FULLNAME ="";
    public  String PASSWORD ="";
    public  String USERIMAGE = "";
    public  String USER_ID = "";
    private static final int REQUEST_PERMISSION_CAMERA_GALLERY_PROFILE=3;
    int REQUEST_CAMERA_PROFILE =2;
    private static Uri outputFileUri;
    private Bitmap bmp;
    private static int THUMBNAIL_SIZE = 700;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_signup);
        buttonEvent();
        assignLinkInText();
        Intent intent = getIntent();
        PHONE = intent.getStringExtra("phone");
        EMAIL = intent.getStringExtra("email");
        //Log.d("DEBUG","PHONE = "+PHONE+",EMAIL = "+EMAIL);
    }
    public void buttonEvent(){
        ImageButton btnBack = (ImageButton)findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ImageButton btnNext = (ImageButton) findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_item));
                do{
                    EditText fname = (EditText)findViewById(R.id.txtFullname);
                    EditText pwd = (EditText)findViewById(R.id.txtPassword);
                    String fullname= fname.getText().toString();
                    String password = pwd.getText().toString();
                    if(TextUtils.isEmpty(fullname)){
                        Toast.makeText(getApplication(), "กรุณาระบุชื่อ",
                                Toast.LENGTH_LONG).show();
                        break;
                    }
                    if(TextUtils.isEmpty(password)){
                        Toast.makeText(getApplication(), "กรุณาระบุรหัสผ่าน",
                                Toast.LENGTH_LONG).show();
                        break;
                    }
                    FULLNAME = fullname;
                    PASSWORD = password;
                    new CallService().execute(AppConfig.apiUrl);
//                    Intent nickname = new Intent(getApplicationContext(),NicknameSignup.class);
//                    nickname.putExtra("phone",PHONE);
//                    nickname.putExtra("email",EMAIL);
//                    nickname.putExtra("fullname",fullname);
//                    nickname.putExtra("password",password);
//                    startActivity(nickname);

                }while (false);

            }
        });

        ImageView imgHead = (ImageView)findViewById(R.id.HeadIcon);
        imgHead.setClickable(true);
        imgHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadPermissionsCameraGalleryUserProfile(Manifest.permission.CAMERA,REQUEST_PERMISSION_CAMERA_GALLERY_PROFILE);
            }
        });
    }
    private void onCaptureImageProfileResult(Bitmap thumbnail) {
        try{
            ExifInterface exif = new ExifInterface(FileUtils.getPath("")+FileUtils.IMAGE_FILE);
            int exifOrientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            int rotate = 0;

            switch (exifOrientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
            }

            if (rotate != 0) {
                int w = thumbnail.getWidth();
                int h = thumbnail.getHeight();

// Setting pre rotate
                Matrix mtx = new Matrix();
                mtx.preRotate(rotate);

                // Rotating Bitmap & convert to ARGB_8888, required by tess
                thumbnail = Bitmap.createBitmap(thumbnail, 0, 0, w, h, mtx, false);
                thumbnail = thumbnail.copy(Bitmap.Config.ARGB_8888, true);
            }
        }catch(Exception ex){
            Log.e("error",ex.getMessage());
        }
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG,90, bytes);
        byte[] imageBytes = bytes.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        USERIMAGE = encodedImage;
        ImageView imgHead = (ImageView)findViewById(R.id.HeadIcon);
        imgHead.setImageBitmap(thumbnail);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA_PROFILE){
                Log.d("DEBUG","RESULT Camera Profile OK");
                //onCaptureImageResult(data);
                if (data != null) {
                    outputFileUri = data.getData();
                }
                try {
                    bmp = ImageUtils.getThumbnail(this, outputFileUri, THUMBNAIL_SIZE);
                }catch (IOException e) {
                    System.out.println(e.getMessage());
                }
                onCaptureImageProfileResult(bmp);
            }

        }
    }
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_CAMERA_GALLERY_PROFILE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // granted
                    FileUtils.createDefaultFolder(PhotoSignup.this);
                    final File file = FileUtils.createFile(FileUtils.IMAGE_FILE);
                    outputFileUri = Uri.fromFile(file);

                    // Camera.
                    final Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

                    final Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    galleryIntent.setType("image/*");
                    //Filesystems
                    galleryIntent.setAction(Intent.ACTION_GET_CONTENT); // To allow file managers or any other app that are not gallery app.

                    final Intent chooserIntent = Intent.createChooser(galleryIntent, "Select Image");

//                final Intent chooserIntent = Intent.createChooser(captureIntent, "Select Image");
                    // Add the gallery options.
                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] { captureIntent });
                    startActivityForResult(chooserIntent, REQUEST_CAMERA_PROFILE);
                }
                else{
                    // no granted

                }
            }
        }
    }
    private boolean addPermission(List<String> permissionsList, String permission) {
        if (Build.VERSION.SDK_INT >= 23)

            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission);

                // Check for Rationale Option
                if (!shouldShowRequestPermissionRationale(permission))
                    return false;
            }
        return true;
    }
    private void loadPermissionsCameraGalleryUserProfile(String perm,int requestCode) {
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permissionsNeeded = new ArrayList<String>();
            List<String> permissionsList = new ArrayList<String>();

            if (!addPermission(permissionsList, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissionsNeeded.add("WRITE_EXTERNAL_STORAGE");
            }

            if (!addPermission(permissionsList, android.Manifest.permission.CAMERA)) {
                permissionsNeeded.add("CAMERA");
            }

            if (!addPermission(permissionsList, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                permissionsNeeded.add("READ_EXTERNAL_STORAGE");
            }
//            Log.d("DEBUG","SIZE permission = "+permissionsList.size());
//            Log.d("DEBUG","SIZE permission = "+permissionsList.get(0));
            if (permissionsList.size() > 0) {
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_PERMISSION_CAMERA_GALLERY_PROFILE);
                return;
            }
            else{
                FileUtils.createDefaultFolder(PhotoSignup.this);
                final File file = FileUtils.createFile(FileUtils.IMAGE_FILE);
                outputFileUri = Uri.fromFile(file);

                // Camera.
                final Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

                final Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                galleryIntent.setType("image/*");
                //Filesystems
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT); // To allow file managers or any other app that are not gallery app.

                final Intent chooserIntent = Intent.createChooser(galleryIntent, "Select Image");

//                final Intent chooserIntent = Intent.createChooser(captureIntent, "Select Image");
                // Add the gallery options.
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] { captureIntent });
                startActivityForResult(chooserIntent, REQUEST_CAMERA_PROFILE);
            }
        }
        else{
            FileUtils.createDefaultFolder(PhotoSignup.this);
            final File file = FileUtils.createFile(FileUtils.IMAGE_FILE);
            outputFileUri = Uri.fromFile(file);

            // Camera.
            final Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

            final Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            galleryIntent.setType("image/*");
            //Filesystems
            galleryIntent.setAction(Intent.ACTION_GET_CONTENT); // To allow file managers or any other app that are not gallery app.

            //final Intent chooserIntent = Intent.createChooser(galleryIntent, "Select Image");

            final Intent chooserIntent = Intent.createChooser(galleryIntent, "Select Image");

//                final Intent chooserIntent = Intent.createChooser(captureIntent, "Select Image");
            // Add the gallery options.
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] { captureIntent });
            startActivityForResult(chooserIntent, REQUEST_CAMERA_PROFILE);
        }

    }
    public void assignLinkInText(){
        //assign link to text
        SpannableString ss = new SpannableString("Already have an account? Sign In.");

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Intent home = new Intent(getApplicationContext(),MainActivity.class);
                home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(home);
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                ds.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                ds.setColor(Color.WHITE);
            }
        };
        ss.setSpan(clickableSpan, 25, 33, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        TextView textView = (TextView) findViewById(R.id.lblSignupbottom);
        textView.setText(ss);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setHighlightColor(Color.TRANSPARENT);
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            /**
             * It gets into the above IF-BLOCK if anywhere the screen is touched.
             */

            View v = getCurrentFocus();
            if ( v instanceof EditText) {


                /**
                 * Now, it gets into the above IF-BLOCK if an EditText is already in focus, and you tap somewhere else
                 * to take the focus away from that particular EditText. It could have 2 cases after tapping:
                 * 1. No EditText has focus
                 * 2. Focus is just shifted to the other EditText
                 */

                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photo_signup, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private class CallService  extends AsyncTask<String, Void, Void> {

        // Required initialization

        //private final HttpClient Client = new DefaultHttpClient();
        private String Content;
        private String Error = null;
        private ProgressDialog Dialog = new ProgressDialog(PhotoSignup.this);
        String data ="";



        protected void onPreExecute() {
            // NOTE: You can call UI Element here.

            //Start Progress Dialog (Message)

            Dialog.setMessage("Please wait..");
            Dialog.show();
            //Log.d("JSON", "PreExcute");
            // Set Request parameter
//            try{
////                data +="&" + URLEncoder.encode("user_name", "UTF-8") + "="+PHONE;
////                data +="&" + URLEncoder.encode("user_pass", "UTF-8") + "="+PHONE;
//            }catch (UnsupportedEncodingException ex){
//                Log.d("ERROR",ex.getMessage());
//            }

        }

        // Call after onPreExecute method
        protected Void doInBackground(String... urls) {
            StringBuilder stringBuilder = new StringBuilder();
            HttpClient httpClient = new DefaultHttpClient();
            //HttpGet httpGet = new HttpGet(urls[0]+data);
            HttpPost httpPost = new HttpPost(urls[0]+"/addUser");
            // Add your data
            try{

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(6);
                nameValuePairs.add(new BasicNameValuePair("user_phone", PHONE));
                nameValuePairs.add(new BasicNameValuePair("user_pass", PASSWORD));
                nameValuePairs.add(new BasicNameValuePair("user_email", EMAIL));
                nameValuePairs.add(new BasicNameValuePair("user_image", ""));
                nameValuePairs.add(new BasicNameValuePair("user_fb_id", ""));
                nameValuePairs.add(new BasicNameValuePair("user_name", FULLNAME));
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));
            }catch (UnsupportedEncodingException ex){
                Log.d("ERROR",ex.getMessage());
            }

            try {
                HttpResponse response = httpClient.execute(httpPost);
//                HttpEntity entity = response.getEntity();
//                InputStream inputStream = entity.getContent();
//                BufferedReader reader = new BufferedReader(
//                        new InputStreamReader(inputStream));
//                String line;
//                while ((line = reader.readLine()) != null) {
//                    stringBuilder.append(line);
//                }
//                inputStream.close();
                StatusLine statusLine = response.getStatusLine();
                int statusCode = statusLine.getStatusCode();
                if (statusCode == 200) {
                    HttpEntity entity = response.getEntity();
                    InputStream inputStream = entity.getContent();
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(inputStream));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    inputStream.close();
                } else {
                    Log.d("JSON", "Failed to download file");
                }
                Content = stringBuilder.toString();
            } catch (Exception e) {
                Log.d("readJSONFeed", e.getLocalizedMessage());
                Error = e.getLocalizedMessage();
            }
            return null;
        }

        protected void onPostExecute(Void unused) {
            // NOTE: You can call UI Element here.

            // Close progress dialog
            Dialog.dismiss();

            if (Error != null) {

                //uiUpdate.setText("Output : "+Error);
                Log.d("JSON_ERROR",Error);
            } else {

                // Show Response Json On Screen (activity)
                //uiUpdate.setText( Content );

                /****************** Start Parse Response JSON Data *************/

                String OutputData = "";
                JSONObject jsonResponse;

                try {
                    Log.d("JSON_POST",Content);
                    /****** Creates a new JSONObject with name/value mappings from the JSON string. ********/
                    jsonResponse = new JSONObject(Content);

                    JSONObject jsonErrors = new JSONObject(jsonResponse.getString("errors"));
                    if(jsonErrors.getString("status").equals("200")){
                        if(!USERIMAGE.equals("")){
                            USER_ID=  jsonResponse.getString("id");
                            new UploadImageService(0,1,USERIMAGE).execute(AppConfig.apiUrl);
                        }
//                        Log.d("JSON","ID = "+jsonResponse.getString("id"));
//                        Log.d("JSON","NAME = "+jsonErrors.getString("detail"));
                        SharedPreferences pref = getApplicationContext().getSharedPreferences(AppConfig.preRef,MODE_PRIVATE); // 0 - for private mode
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("user_id", jsonResponse.getString("id"));
                        editor.putString("user_name", FULLNAME);
                        editor.putString("user_email", EMAIL);
                        editor.putString("user_phone", PHONE);
                        editor.commit();
                        Intent staticPage = new Intent(getApplicationContext(),HomeActivity.class);
                        staticPage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(staticPage);
                    }
                    else if(jsonErrors.getString("status").equals("207")){
                        Toast.makeText(getApplication(), "พบข้อมูลเบอร์โทรศัพท์หรืออีเมล์ซ้ำในระบบ",
                                Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {

                    e.printStackTrace();
                }


            }
        }

    }
    private boolean CheckNetwork() {
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr.getActiveNetworkInfo() != null
                && conMgr.getActiveNetworkInfo().isAvailable()
                && conMgr.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            return false;
        }

    }
    private class UploadImageService  extends AsyncTask<String, Integer, Void> {

        // Required initialization

        //private final HttpClient Client = new DefaultHttpClient();
        private String Content;
        private String Error = null;
        private int allUpload = 1;
        private int nowUpload = 0;
        private int currentIndex = 0;
        private String strImage = "";
        private ProgressDialog Dialog = new ProgressDialog(PhotoSignup.this);
        String data ="";
        private boolean isConnected=false;

        protected  UploadImageService(int curindex,int curupload,String imgBase64){


            nowUpload = 1;
            currentIndex = curindex;
            strImage = imgBase64;
        }
        protected void onPreExecute() {
            isConnected = CheckNetwork();
            // NOTE: You can call UI Element here.
            //Start Progress Dialog (Message)
            Toast.makeText(getApplication(), "Uploading Photo  "+String.valueOf(nowUpload)+"/"+String.valueOf(allUpload)+"...",
                    Toast.LENGTH_LONG).show();
            // Set Request parameter
//            try{
////                data +="&" + URLEncoder.encode("user_name", "UTF-8") + "="+PHONE;
////                data +="&" + URLEncoder.encode("user_pass", "UTF-8") + "="+PHONE;
//            }catch (UnsupportedEncodingException ex){
//                Log.d("ERROR",ex.getMessage());
//            }

        }

        // Call after onPreExecute method
        protected Void doInBackground(String... urls) {
            //Looper.prepare();
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
            HttpPost httpPostUpload;
            HttpResponse responseUpload = null;
            HttpClient httpClientUpload;
            StringBuilder stringBuilder = new StringBuilder();
            httpClientUpload = new DefaultHttpClient();

            //HttpGet httpGet = new HttpGet(urls[0]+data);

            List<NameValuePair> nameValuePairs;
            if(isConnected) {
                // Add your data
                try{
                    Date datenow = new Date();
                    SimpleDateFormat postFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String strDatenow = postFormater.format(datenow);
                    //Upload Image User
                    httpPostUpload = new HttpPost(urls[0]+"/updateUserImage/"+USER_ID);
                    nameValuePairs = new ArrayList<NameValuePair>(4);
                    nameValuePairs.add(new BasicNameValuePair("image_base_64", strImage));
                    httpPostUpload.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                    //httpPut.addHeader("Content-Type","application/x-www-form-urlencoded");
                    responseUpload = httpClientUpload.execute(httpPostUpload);
                    StatusLine statusLine = responseUpload.getStatusLine();
                    int statusCode = statusLine.getStatusCode();
                    if (statusCode == 200) {
                        HttpEntity entity = responseUpload.getEntity();
                        InputStream inputStream = entity.getContent();
                        BufferedReader reader = new BufferedReader(
                                new InputStreamReader(inputStream));
                        String line;
                        while ((line = reader.readLine()) != null) {
                            stringBuilder.append(line);
                        }
                        inputStream.close();
                    } else {
                        //Toast.makeText(getApplication(), "Failed to download file",
                        // Toast.LENGTH_LONG).show();
                        Log.d("JSON", "Failed to download file -> "+String.valueOf(statusCode));
                        Log.d("JSON", "Failed to download file -> "+String.valueOf(statusLine.getReasonPhrase()));
                    }
                    Content = stringBuilder.toString();
                }catch (UnsupportedEncodingException ex){
                    Log.d("ERROR",ex.getMessage());
                }
                catch (Exception e){
                    Log.d("readJSONFeed", e.getLocalizedMessage());
                    Log.d("readJSONFeed", e.getStackTrace().toString());
                    Error = e.getLocalizedMessage();
                }
                finally {
                    httpClientUpload.getConnectionManager().shutdown();
                }
            }


            return null;
        }
        protected void onPostExecute(Void unused) {
            // NOTE: You can call UI Element here.

            // Close progress dialog
            Dialog.dismiss();

            if (Error != null) {

                //uiUpdate.setText("Output : "+Error);
                Log.d("JSON_ERROR",Error);
            }
            else if(!isConnected){
                Toast.makeText(getApplication(), "Please check your internet connection.",
                        Toast.LENGTH_LONG).show();
            }
            else {

                // Show Response Json On Screen (activity)
                //uiUpdate.setText( Content );

                /****************** Start Parse Response JSON Data *************/
                JSONObject jsonResponse;

                try {
                    Log.d("JSON_POST",Content);
                    final ArrayList<HashMap<String, String>> MyArrList;
                    HashMap<String, String> map;
                    JSONArray jsonArray;
                    /****** Creates a new JSONObject with name/value mappings from the JSON string. ********/
                    jsonResponse = new JSONObject(Content);
                    JSONObject jsonErrors = new JSONObject(jsonResponse.getString("errors"));
                    if(jsonErrors.getString("status").equals("200")) {
                        HashMap<String,String> tmpHash ;
                        Toast.makeText(getApplication(), "Upload Photo Successful.",
                                Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(getApplication(), jsonErrors.getString("detail"),
                                Toast.LENGTH_LONG).show();
                    }


                } catch (JSONException e) {
                    e.getMessage();
                    e.printStackTrace();
                }

            }
        }



    }
}
