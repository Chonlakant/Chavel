package com.chavel.chavel;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import android.widget.TextView;

import com.chavel.chavel.Home.HomeActivity;
import com.chavel.chavel.Register.ForgotPassword;
import com.chavel.chavel.Register.SignUpPortal;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.widget.Toast;


public class MainActivity extends Activity {

    private LoginButton login_button;
    private CallbackManager callbackManager;
    public String METHOD ="";
    public String PHONE = "";
    public   String EMAIL = "";
    public   String FB_ID = "";
    public  String FULLNAME = "";
    public  String PASSWORD = "";
    public String USER_IMG="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(getApplication());
        setContentView(R.layout.activity_main);
        try{
            String versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            TextView txtVersion = (TextView)findViewById(R.id.lblVersion);
            txtVersion.setText("App Version : "+versionName);
        }catch (PackageManager.NameNotFoundException ex){

        }
// Add code to print out the key hash
//        try {
//            PackageInfo info = getPackageManager().getPackageInfo(
//                    "com.chavel.chavel",
//                    PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//
//        } catch (NoSuchAlgorithmException e) {
//
//        }
        if(CheckSession()){
            Intent staticPage = new Intent(getApplicationContext(),HomeActivity.class);
            startActivity(staticPage);
        }
        ImageButton btnLogin = (ImageButton) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_item));
                do{
                    EditText txtUsername = (EditText) findViewById(R.id.txtUsername);
                    EditText txtPassword = (EditText) findViewById(R.id.txtPassword);
                    String username = txtUsername.getText().toString();
                    String pwd = txtPassword.getText().toString();
                    if(TextUtils.isEmpty(username)){
                        Toast.makeText(getApplication(), "กรุณาระบุ Username",
                                Toast.LENGTH_LONG).show();
                        break;
                    }
                    if(TextUtils.isEmpty(pwd)){
                        Toast.makeText(getApplication(), "กรุณาระบุ Password",
                                Toast.LENGTH_LONG).show();
                        break;
                    }
                    EMAIL = username;
                    PASSWORD = pwd;
                    METHOD = "/login";
                    new CallService().execute(AppConfig.apiUrl);
                }while(false);
            }
        });

        SpannableString ss = new SpannableString("Don't have an account? Sign Up.");

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Intent signup = new Intent(MainActivity.this,SignUpPortal.class);
                startActivity(signup);
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                ds.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                ds.setColor(Color.WHITE);
            }
        };
        ss.setSpan(clickableSpan, 23, 31, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        TextView textView = (TextView) findViewById(R.id.lblHomebottom);
        textView.setText(ss);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setHighlightColor(Color.TRANSPARENT);

        TextView lblForgotpassword = (TextView)findViewById(R.id.lblForgotPassword);
        lblForgotpassword.setClickable(true);
        lblForgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forgot = new Intent(MainActivity.this,ForgotPassword.class);
                startActivity(forgot);
            }
        });


        LoginManager.getInstance().logOut();
        login_button = (LoginButton) findViewById(R.id.login_button);
        login_button.setBackgroundResource(R.drawable.btn_fb_login_);
        callbackManager = CallbackManager.Factory.create();

//        String usersUrl = "http://chavel.me/api/public/v1/users";
//        new CallService().execute(usersUrl);
        login_button.setReadPermissions(Arrays.asList(
                "public_profile", "email"));
        login_button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                try{
                    Log.d("FACEBOOK","FB_ID = "+loginResult.getAccessToken().getUserId() );
                    String accessToken = loginResult.getAccessToken().getToken();
                    Log.i("FACEBOOK_accessToken", accessToken);

                    //prepare fields with email
                    String[] requiredFields = new String[]{"id","name","email"};
                    Bundle parameters = new Bundle();
                    parameters.putString("fields", TextUtils.join(",", requiredFields));

                    GraphRequest requestEmail = new GraphRequest(loginResult.getAccessToken(), "/me", parameters, null, new GraphRequest.Callback()
                    {
                        @Override
                        public void onCompleted (GraphResponse response)
                        {
                            //Log.d("FACEBOOK_RESPONSE_P", response.toString());
                            if (response != null)
                            {
                                GraphRequest.GraphJSONObjectCallback callbackEmail = new GraphRequest.GraphJSONObjectCallback()
                                {
                                    @Override
                                    public void onCompleted (JSONObject me, GraphResponse response)
                                    {
                                        Log.d("FACEBOOK_RESPONSE", response.toString());
                                        if (response.getError() != null)
                                        {
                                            Log.d("FACEBOOK", "FB: cannot parse email");
                                        }
                                        else
                                        {
                                            String email = me.optString("email");
                                            String fullname = me.optString("name");
                                            String fb_id = me.optString("id");
                                            Log.d("FACEBOOK_DATA","ID = "+fb_id);
                                            Log.d("FACEBOOK_DATA","NAME = "+fullname);
                                            Log.d("FACEBOOK_DATA","email = "+email);
                                            // send email and id to your web server
                                            FB_ID = fb_id;
                                            EMAIL = email;
                                            FULLNAME=fullname;
                                            PASSWORD = "";
                                            METHOD = "/loginFB";
                                            PHONE="";
                                            new CallService().execute(AppConfig.apiUrl);
                                            //...
                                        }
                                    }
                                };

                                callbackEmail.onCompleted(response.getJSONObject(), response);
                            }
                        }
                    });

                    requestEmail.executeAsync();
                }
                catch (Exception ex){
                    android.support.v7.app.AlertDialog alertDialog = new android.support.v7.app.AlertDialog.Builder(MainActivity.this).create();
                    alertDialog.setTitle("Warning");
                    alertDialog.setMessage(ex.getMessage());
                    alertDialog.setButton(android.support.v7.app.AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }

            }

            @Override
            public void onCancel() {
                Log.d("FACEBOOK_CANCEL","CANCEL FACEBOOK");
               android.support.v7.app.AlertDialog alertDialog = new android.support.v7.app.AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("Warning");
                alertDialog.setMessage("Facebook Cancel");
                alertDialog.setButton(android.support.v7.app.AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }

            @Override
            public void onError(FacebookException e) {

                android.support.v7.app.AlertDialog alertDialog = new android.support.v7.app.AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("Warning");
                alertDialog.setMessage(e.getMessage());
                alertDialog.setButton(android.support.v7.app.AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
                Log.d("FACEBOOK_ERROR",e.getMessage());
            }
        });


    }
    public void ClearLoginForm(){
        EditText txtUsername = (EditText) findViewById(R.id.txtUsername);
        EditText txtPassword = (EditText) findViewById(R.id.txtPassword);
        txtUsername.setText("");
        txtPassword.setText("");
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
    protected boolean CheckSession(){

        try{
            SharedPreferences prefs = getSharedPreferences(AppConfig.preRef, MODE_PRIVATE);
            String restoredText = prefs.getString("user_id", null);
            if (restoredText != null) {
                //Log.d("DEBUG","username = "+prefs.getString("username",""));
                return true;
            }
        }
        catch (Exception ex){
            Log.e("ERROR",ex.getMessage());
        }
        finally {

        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
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
        private ProgressDialog Dialog = new ProgressDialog(MainActivity.this);
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
            HttpPost httpPost = new HttpPost(urls[0]+METHOD);
            // Add your data
            try{
                //EditText fname = (EditText)findViewById(R.id.txtFullname);
                //EditText pwd = (EditText)findViewById(R.id.txtPassword);
                //String fullname= fname.getText().toString();
                //String password = pwd.getText().toString();
                switch (METHOD){
                    case "/login":
                        Log.d("JSON_SEND","Username = "+EMAIL+",PASS = "+PASSWORD);
                        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                        nameValuePairs.add(new BasicNameValuePair("username", EMAIL));
                        nameValuePairs.add(new BasicNameValuePair("user_pass", PASSWORD));
                        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

                        break;
                    case "/loginFB":
                        Log.d("JSON_SEND","ID = "+FB_ID);
                        Log.d("JSON_SEND","NAME = "+FULLNAME);
                        Log.d("JSON_SEND","EMAIL = "+EMAIL);

                        List<NameValuePair> nameValuePairs2 = new ArrayList<NameValuePair>(5);
                        nameValuePairs2.add(new BasicNameValuePair("user_name", FULLNAME));
                        nameValuePairs2.add(new BasicNameValuePair("user_email", EMAIL));
                        nameValuePairs2.add(new BasicNameValuePair("user_image", USER_IMG));
                        nameValuePairs2.add(new BasicNameValuePair("user_fb_id", FB_ID));
                        nameValuePairs2.add(new BasicNameValuePair("user_phone", PHONE));
                        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs2, "UTF-8"));
                        break;
                }


            }catch (UnsupportedEncodingException ex){
                Log.d("ERROR",ex.getMessage());
            }

            try {
                HttpResponse response = httpClient.execute(httpPost);
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
                JSONObject jsonResponse;

                try {
                    Log.d("JSON_POST",Content);
                    /****** Creates a new JSONObject with name/value mappings from the JSON string. ********/
                    jsonResponse = new JSONObject(Content);

                    JSONObject jsonErrors = new JSONObject(jsonResponse.getString("errors"));
                    if(jsonErrors.getString("status").equals("200")){
//                        Log.d("JSON","ID = "+jsonResponse.getString("id"));
//                        Log.d("JSON","NAME = "+jsonErrors.getString("detail"));
                        SharedPreferences pref = getApplicationContext().getSharedPreferences(AppConfig.preRef,MODE_PRIVATE); // 0 - for private mode
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("user_id", jsonResponse.getString("user_id"));
                        editor.putString("user_name", jsonResponse.getString("user_name"));
                        editor.putString("user_email", jsonResponse.getString("user_email"));
                        editor.putString("user_phone", jsonResponse.getString("user_phone"));
                        editor.commit();
                        ClearLoginForm();
                        Intent staticPage = new Intent(getApplicationContext(),HomeActivity.class);
                        startActivity(staticPage);
                    }
                    else if(jsonErrors.getString("status").equals("401")){
                        String alertText = "";
                        switch (METHOD){
                            case "/login":
                                alertText = "Username หรือ Password ไม่ถูกต้อง";
                                EditText txtPassword = (EditText) findViewById(R.id.txtPassword);
                                txtPassword.setText("");
                                break;
                            case "/loginFB":
                                alertText ="Not found Facebook Id.";
                                break;
                        }
                        Toast.makeText(getApplication(), alertText,
                                Toast.LENGTH_LONG).show();
                    }
                    METHOD="";
                } catch (JSONException e) {

                    e.printStackTrace();
                }


            }
        }

    }
}
