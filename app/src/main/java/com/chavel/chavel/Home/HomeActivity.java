package com.chavel.chavel.Home;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.LinkAddress;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.os.Parcelable;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telecom.Call;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import  android.support.v4.app.Fragment;
import android.widget.Toast;

import com.chavel.chavel.AppConfig;
import com.chavel.chavel.JSONParser;
import com.chavel.chavel.MainActivity;
import com.chavel.chavel.R;
import com.chavel.chavel.Utility.AnimatedTabHostListener;
import com.chavel.chavel.Utility.FileUtils;
import com.chavel.chavel.Utility.ImageUtils;
import com.chavel.chavel.Utility.MainUtility;
import com.chavel.chavel.Utility.WorkaroundMapFragment;
import com.facebook.login.LoginManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.vanniktech.emoji.EmojiEditText;
import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.EmojiTextView;
import com.vanniktech.emoji.one.EmojiOneProvider;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;

import static android.Manifest.permission_group.LOCATION;
import static android.graphics.Bitmap.Config.ARGB_8888;
import static android.view.View.GONE;
import static android.view.View.TEXT_ALIGNMENT_CENTER;


public class HomeActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private TabHost mTabHost;
    public  String USER_ID = "",USER_NAME="",USER_EMAIL="",USER_PHONE="";
    public  String METHOD = "",UPD_METHOD="";
    public  Boolean isCreatePin = false;
    public  Boolean TAB1_LOADED = false;
    public  Boolean TAB2_LOADED = false;
    public  Boolean TAB3_LOADED = false;
    public  Boolean TAB4_LOADED = false;
    public  Boolean TAB5_LOADED = false;
    public  Boolean PINTAB1_LOADED = false;
    public  Boolean PINTAB2_LOADED = false;
    public  Boolean PVPINTAB1_LOADED = false;
    public  Boolean PVPINTAB2_LOADED = false;
    public  Boolean NOTISTAB1_LOADED = false;
    public  Boolean NOTISTAB2_LOADED = false;
    public  Boolean flagFinishMove = false;
    public  String TAB1_TAG = "Home",TAB2_TAG="Explore",TAB3_TAG="Route",TAB4_TAG="Notis",TAB5_TAG="Setting";
    public  String PIN_TAB1_TAG ="New Route",PIN_TAB2_TAG="From Draft",NOTIS_TAB1_TAG = "Following",NOTIS_TAB2_TAG="You",PREVIEW_TAB1_TAG="Overview",PREVIEW_TAB2_TAG="Pins";
    public String R_ID="",R_TITLE="",R_DESC="",R_LIKE="",R_ACTIVITY="",R_CITY="",R_TRAVEl_METHOD="",R_BUDGET_MIN="",R_BUDGET_MAX="",P_NAME="",P_DESC="";
    public  String LAST_PLACE_ID="",EDIT_PLACE_ID="",FOLLOW_USER_ID="";
    public String PREVIEW_R_ID="",HOME_PREVIEW_R_ID="",USER_PREVIEW_R_ID="",EXPLORE_PREVIEW_R_ID="",DRAFT_R_ID="",FIND_F_TXT="",USER_DETAIL_ID="";
    public String HOME_COMMENT_ROUTE_ID="",HOME_COMMENT_DETAIL="",USER_COMMENT_ROUTE_ID="",USER_COMMENT_DETAIL="";
    public String R_SUGGESTION="",R_LAT="",R_LNG="",EXPLORE_SEARCH_TEXT = "";
    public String EXPLORE_PREVIEW_PIN_ID = "",HOME_PREVIEW_PIN_ID = "",USER_PREVIEW_PIN_ID = "";
    private GoogleMap mMap,mMapEdit;
    public int mapType = 1;
    Marker currLocationMarker;
    ArrayList<HashMap<String, String>> allPinsAdd = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> allPinsEdit = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> allPinsListEdit = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> allImageRouteAdd = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> allImageRouteEdit = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> allImagePinsAdd = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> allImagePinsEdit = new ArrayList<HashMap<String, String>>();
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    private static final int REQUEST_FINE_LOCATION=0,REQUEST_PERMISSION_CAMERA=1,REQUEST_PERMISSION_CAMERA_GALLERY=2,REQUEST_PERMISSION_CAMERA_GALLERY_PROFILE=3;
    LatLng latLng,LATLNG_PIN_PREVIEW,currentLocation;
    int CURRENT_UPLOAD_ROUTE_COUNT = 0,ALL_ROUTE_UPLOAD_COUNT=0,CURRENT_UPLOAD_ROUTE_INDEX=0;
    int CURRENT_UPLOAD_PLACE_COUNT = 0,ALL_PLACE_UPLOAD_COUNT=0;
    public String CURRENT_BASE64_ROUTE_IMG = "";
    int REQUEST_CAMERA =0,REQUEST_CAMERA_PROFILE =2,SELECT_FILE=1;
    boolean ismapZoomFirst = false,isSavePopup=false,isPreviewFromAdd=false,isPreviewFromDraft=false,isPublishFromDraft=false,isPublishPreview = false;
    public  String userChoosenTask="";
    public int FOLLOW_CURRENT_INDEX = -1,LIKE_CURRENT_INDEX = -1;
    public int BACK_TO_TAB = 0;
    public String VIEW_USER_ID = "0";
    public String PROFILE_IMG_URL = "";
    public Boolean isOwnRoute = false;
    public ArrayList<HashMap<String, String>> globalFindFriendList = new ArrayList<HashMap<String, String>>();
    public ArrayList<HashMap<String, String>> globalHomeFeedList = new ArrayList<HashMap<String, String>>();
    public ArrayList<HashMap<String, String>> globalUserFeedList = new ArrayList<HashMap<String, String>>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EmojiManager.install(new EmojiOneProvider());
        getSession();
        setContentView(R.layout.activity_home);
        setTab();
        doLoadDataHome();
        buttonExploreEvent();
        buttonCreateRouteEvent();
        buttonSettingEvent();
        buttonHomeEvent();
        buttonUserEvent();
        loadPermissions(Manifest.permission.ACCESS_FINE_LOCATION,REQUEST_FINE_LOCATION);
    }
    public void getSession(){
        SharedPreferences prefs = getSharedPreferences(AppConfig.preRef, MODE_PRIVATE);
        USER_ID =prefs.getString("user_id", "");
        USER_NAME=prefs.getString("user_name","");
        USER_EMAIL=prefs.getString("user_email","");
        USER_PHONE=prefs.getString("user_phone","");
    }
    public void buttonSettingEvent(){
        /* Setting */
        ImageButton btnSetting = (ImageButton)findViewById(R.id.btnUserSetting);
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout layoutUserDetail = (RelativeLayout)findViewById(R.id.layoutUserDetail);
                layoutUserDetail.setVisibility(View.GONE);
                RelativeLayout layoutSetting = (RelativeLayout)findViewById(R.id.layoutSetting);
                layoutSetting.setVisibility(View.VISIBLE);
            }
        });
        ImageButton btnSettingBack = (ImageButton)findViewById(R.id.btnSettingback);
        btnSettingBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout layoutSetting = (RelativeLayout)findViewById(R.id.layoutSetting);
                layoutSetting.setVisibility(GONE);
                RelativeLayout layoutUserDetail = (RelativeLayout)findViewById(R.id.layoutUserDetail);
                layoutUserDetail.setVisibility(View.VISIBLE);
            }
        });
        LinearLayout logoutRow = (LinearLayout)findViewById(R.id.menuLogout);
        logoutRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logOut();
                SharedPreferences settings = getApplicationContext().getSharedPreferences(AppConfig.preRef, Context.MODE_PRIVATE);
                settings.edit().clear().commit();
                Intent staticPage = new Intent(getApplicationContext(),MainActivity.class);
                staticPage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(staticPage);
                finish();
            }
        });

        LinearLayout AboutRow = (LinearLayout)findViewById(R.id.menuAbout);
        AboutRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("DEBUG","About Click");
                Intent staticPage = new Intent(getApplicationContext(),DetailModalActivity.class);
                startActivity(staticPage);
            }
        });

        LinearLayout findfriendsRow = (LinearLayout)findViewById(R.id.menuFindfriends);
        findfriendsRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout layoutSetting = (RelativeLayout)findViewById(R.id.layoutSetting);
                layoutSetting.setVisibility(View.GONE);
                RelativeLayout layoutFindfriends = (RelativeLayout)findViewById(R.id.layoutSearchFriends);
                layoutFindfriends.setVisibility(View.VISIBLE);
            }
        });

        Button btnFindFriendsCancel = (Button)findViewById(R.id.btnActSearchFriendsCancel);
        btnFindFriendsCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout layoutFindfriends = (RelativeLayout)findViewById(R.id.layoutSearchFriends);
                layoutFindfriends.setVisibility(View.GONE);
                RelativeLayout layoutSetting = (RelativeLayout)findViewById(R.id.layoutSetting);
                layoutSetting.setVisibility(View.VISIBLE);
                EditText txtSearchFriends = (EditText)findViewById(R.id.txtSearchFriends);
                txtSearchFriends.setText("");
                ListView listviewFindFriends = (ListView) findViewById(R.id.listviewSearchFriends);
                listviewFindFriends.setAdapter(null);
                globalFindFriendList = new ArrayList<HashMap<String, String>>();
            }
        });
        Button btnFindFriends = (Button)findViewById(R.id.btnActSearchFriends);
        btnFindFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText txtSearchFriends = (EditText)findViewById(R.id.txtSearchFriends);
                if(!TextUtils.isEmpty(txtSearchFriends.getText())) {
                    METHOD = "/user/search";
                    FIND_F_TXT = txtSearchFriends.getText().toString();
                    new HomeActivity.CallService().execute(AppConfig.apiUrl);
                }
            }
        });
        /* Setting*/
    }
    public  void buttonExploreEvent(){
        /* Explore Search Bars*/
        ImageButton btnSearch = (ImageButton) findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_item));
                do{
                    LinearLayout mainlayout = (LinearLayout)findViewById(R.id.layoutMainExplore);
                    mainlayout.setVisibility(GONE);
                    LinearLayout searchlayout = (LinearLayout)findViewById(R.id.layoutSearchExplore);
                    searchlayout.setVisibility(LinearLayout.VISIBLE);
                }while(false);
            }
        });
        Button btnActSearch = (Button)findViewById(R.id.btnActSearch);
        btnActSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView txtSearch = (TextView)findViewById(R.id.txtSearch);
                EXPLORE_SEARCH_TEXT = txtSearch.getText().toString();
                METHOD="/listSearchRoutesExplore";
                new HomeActivity.CallService().execute(AppConfig.apiUrl);
            }
        });
        /* Explore Search Bars*/

        /* Explore Cancel Search Bars*/
        Button btnSearchCancel = (Button)findViewById(R.id.btnActSearchCancel);
        btnSearchCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_item));
                do{
                    LinearLayout searchlayout = (LinearLayout)findViewById(R.id.layoutSearchExplore);
                    searchlayout.setVisibility(GONE);
                    LinearLayout mainlayout = (LinearLayout)findViewById(R.id.layoutMainExplore);
                    mainlayout.setVisibility(LinearLayout.VISIBLE);
//                    GridView gridSearch = (GridView)findViewById(R.id.gridExploreSearch);
//                    gridSearch.setAdapter(null);
                }while(false);
            }
        });
        /* Explore Cancel Search Bars*/

        ImageButton btnExplorePreviewBack = (ImageButton)findViewById(R.id.btnExplorePreviewback);
        btnExplorePreviewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout layoutExplorePreview = (RelativeLayout)findViewById(R.id.layoutExplorePreview);
                layoutExplorePreview.setVisibility(GONE);
                GridView gridOverview = (GridView)findViewById(R.id.gridExploreOverview);
                gridOverview.setAdapter(null);
                TextView lbltitle = (TextView)findViewById(R.id.lblExploreOverviewTitle);
                TextView lbldetail = (TextView)findViewById(R.id.lblExploreOverviewDetail);
                lbldetail.setText("Loading...");
                lbltitle.setText("");
                ListView listPins = (ListView)findViewById(R.id.listViewExplorePreview);
                listPins.setAdapter(null);
                ImageView imgPreview = (ImageView)findViewById(R.id.imgExploreOverview);
                imgPreview.setImageDrawable(null);
                ViewGroup.LayoutParams imgLayoutParams = imgPreview.getLayoutParams();
                imgLayoutParams.height=0;
                imgPreview.setLayoutParams(imgLayoutParams);
            }
        });

        Button btnExploreViewAllPins = (Button)findViewById(R.id.btnExploreViewAllPins);
        btnExploreViewAllPins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout layoutExploreViewAllPins = (RelativeLayout)findViewById(R.id.layoutExploreViewAllPins);
                RelativeLayout layoutExplorePreview = (RelativeLayout)findViewById(R.id.layoutExplorePreview);
                layoutExplorePreview.setVisibility(GONE);
                layoutExploreViewAllPins.setVisibility(View.VISIBLE);
                loadPermissionsMapExploreViewAllPins(Manifest.permission.ACCESS_FINE_LOCATION,REQUEST_FINE_LOCATION);
            }
        });
        ImageButton btnExploreViewAllPinsBack = (ImageButton)findViewById(R.id.btnExploreViewAllPinsback);
        btnExploreViewAllPinsBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout layoutExploreViewAllPins = (RelativeLayout)findViewById(R.id.layoutExploreViewAllPins);
                RelativeLayout layoutExplorePreview = (RelativeLayout)findViewById(R.id.layoutExplorePreview);
                layoutExplorePreview.setVisibility(View.VISIBLE);
                layoutExploreViewAllPins.setVisibility(View.GONE);
            }
        });
        ImageButton btnExplorePinPreviewback = (ImageButton)findViewById(R.id.btnExplorePinPreviewback);
        btnExplorePinPreviewback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout layoutExplorePinPreview = (RelativeLayout)findViewById(R.id.layoutExplorePinPreview);
                RelativeLayout layoutExplorePreview = (RelativeLayout)findViewById(R.id.layoutExplorePreview);
                layoutExplorePinPreview.setVisibility(View.GONE);
                layoutExplorePreview.setVisibility(View.VISIBLE);

                TextView lblExplorePinPreviewHead = (TextView)findViewById(R.id.lblExplorePinPreviewHead);
                TextView lblExplorePinPreviewDesc = (TextView)findViewById(R.id.lblExplorePinPreviewDesc);
                ImageView imgExplorePinPreview = (ImageView)findViewById(R.id.imgExplorePinPreviewImage);
                lblExplorePinPreviewHead.setText("");
                lblExplorePinPreviewDesc.setText("");
                String imgExplorePreviewPlaceUrl = AppConfig.noImgUrl;
                Picasso.with(HomeActivity.this).load(imgExplorePreviewPlaceUrl).into(imgExplorePinPreview);
            }
        });
    }
    public void buttonHomeEvent(){
        ImageButton btnHomePreviewBack = (ImageButton)findViewById(R.id.btnHomePreviewback);
        ImageButton btnHomeCommentBack = (ImageButton)findViewById(R.id.btnHomeCommentback);
        Button btnHomeAddComment = (Button)findViewById(R.id.btnCommentSend);
        btnHomePreviewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout layoutHomemain = (LinearLayout)  findViewById(R.id.layoutHomemain);
                layoutHomemain.setVisibility(View.VISIBLE);
                RelativeLayout layoutHomePreview = (RelativeLayout)findViewById(R.id.layoutHomePreview);
                layoutHomePreview.setVisibility(GONE);
                GridView gridOverview = (GridView)findViewById(R.id.gridHomeOverview);
                gridOverview.setAdapter(null);
                TextView lbltitle = (TextView)findViewById(R.id.lblHomeOverviewTitle);
                TextView lbldetail = (TextView)findViewById(R.id.lblHomeOverviewDetail);
                lbldetail.setText("Loading...");
                lbltitle.setText("");
                ListView listPins = (ListView)findViewById(R.id.listViewHomePreview);
                listPins.setAdapter(null);
                ImageView imgPreview = (ImageView)findViewById(R.id.imgHomeOverview);
                imgPreview.setImageDrawable(null);
                ViewGroup.LayoutParams imgLayoutParams = imgPreview.getLayoutParams();
                imgLayoutParams.height=0;
                imgPreview.setLayoutParams(imgLayoutParams);
            }
        });
        btnHomeCommentBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout layoutHomemain = (LinearLayout)  findViewById(R.id.layoutHomemain);
                layoutHomemain.setVisibility(View.VISIBLE);
                RelativeLayout layoutHomeComment = (RelativeLayout)findViewById(R.id.layoutHomeComment);
                layoutHomeComment.setVisibility(GONE);
                ListView listComment = (ListView)findViewById(R.id.listviewHomeComment);
                listComment.setAdapter(null);
            }
        });
        btnHomeAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EmojiEditText txtComment = (EmojiEditText)findViewById(R.id.txtCommentDetailHome);
                String strComment = txtComment.getText().toString().trim();
                strComment = StringEscapeUtils.escapeJava(strComment);
                //Log.d("DEBUG","TEXT  = "+ StringEscapeUtils.escapeJava(strComment));
                if(!strComment.equals("")){
                    METHOD="/addCommentHome";
                    HOME_COMMENT_DETAIL =strComment;
                    new HomeActivity.CallServiceNoLoad().execute(AppConfig.apiUrl);
                    txtComment.setText("");
                }

            }
        });
        Button btnFeedViewAllPins = (Button)findViewById(R.id.btnFeedViewAllPins);
        btnFeedViewAllPins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout layoutFeedViewAllPins = (RelativeLayout)findViewById(R.id.layoutFeedViewAllPins);
                RelativeLayout layoutFeedPreview = (RelativeLayout)findViewById(R.id.layoutHomePreview);
                layoutFeedPreview.setVisibility(GONE);
                layoutFeedViewAllPins.setVisibility(View.VISIBLE);
                loadPermissionsMapFeedViewAllPins(Manifest.permission.ACCESS_FINE_LOCATION,REQUEST_FINE_LOCATION);
            }
        });
        ImageButton btnFeedViewAllPinsBack = (ImageButton)findViewById(R.id.btnFeedViewAllPinsback);
        btnFeedViewAllPinsBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout layoutFeedViewAllPins = (RelativeLayout)findViewById(R.id.layoutFeedViewAllPins);
                RelativeLayout layoutFeedPreview = (RelativeLayout)findViewById(R.id.layoutHomePreview);
                layoutFeedPreview.setVisibility(View.VISIBLE);
                layoutFeedViewAllPins.setVisibility(View.GONE);
            }
        });
        ImageButton btnHomePinPreviewback = (ImageButton)findViewById(R.id.btnHomePinPreviewback);
        btnHomePinPreviewback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout layoutHomePinPreview = (RelativeLayout)findViewById(R.id.layoutHomePinPreview);
                RelativeLayout layoutHomePreview = (RelativeLayout)findViewById(R.id.layoutHomePreview);
                layoutHomePinPreview.setVisibility(View.GONE);
                layoutHomePreview.setVisibility(View.VISIBLE);

                TextView lblHomePinPreviewHead = (TextView)findViewById(R.id.lblHomePinPreviewHead);
                TextView lblHomePinPreviewDesc = (TextView)findViewById(R.id.lblHomePinPreviewDesc);
                ImageView imgHomePinPreview = (ImageView)findViewById(R.id.imgHomePinPreviewImage);
                lblHomePinPreviewHead.setText("");
                lblHomePinPreviewDesc.setText("");
                String imgHomePreviewPlaceUrl = AppConfig.noImgUrl;
                Picasso.with(HomeActivity.this).load(imgHomePreviewPlaceUrl).into(imgHomePinPreview);
            }
        });
    }
    public  void buttonUserEvent(){
        ImageButton btnUserPreviewBack = (ImageButton)findViewById(R.id.btnUserPreviewback);
        ImageButton btnCommentUserBack = (ImageButton)findViewById(R.id.btnUserCommentback);
        Button btnUserAddComment = (Button)findViewById(R.id.btnUserCommentSend);
        btnUserPreviewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout layoutUserPreview = (RelativeLayout)findViewById(R.id.layoutUserPreview);
                layoutUserPreview.setVisibility(GONE);
                GridView gridOverview = (GridView)findViewById(R.id.gridUserOverview);
                gridOverview.setAdapter(null);
                TextView lbltitle = (TextView)findViewById(R.id.lblUserOverviewTitle);
                TextView lbldetail = (TextView)findViewById(R.id.lblUserOverviewDetail);
                lbldetail.setText("Loading...");
                lbltitle.setText("");
                ListView listPins = (ListView)findViewById(R.id.listViewUserPreview);
                listPins.setAdapter(null);
                ImageView imgPreview = (ImageView)findViewById(R.id.imgUserOverview);
                imgPreview.setImageDrawable(null);
                ViewGroup.LayoutParams imgLayoutParams = imgPreview.getLayoutParams();
                imgLayoutParams.height=0;
                imgPreview.setLayoutParams(imgLayoutParams);
            }
        });
        btnCommentUserBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout layoutComment = (RelativeLayout)findViewById(R.id.layoutUserComment);
                layoutComment.setVisibility(View.GONE);
                RelativeLayout layoutUsermain = (RelativeLayout)  findViewById(R.id.layoutUserDetail);
                layoutUsermain.setVisibility(View.VISIBLE);
            }
        });
        btnUserAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EmojiEditText txtComment = (EmojiEditText)findViewById(R.id.txtCommentDetailUser);
                String strComment = txtComment.getText().toString();
                strComment = StringEscapeUtils.escapeJava(strComment);
                if(!strComment.equals("")){
                    METHOD="/addCommentUser";
                    USER_COMMENT_DETAIL =strComment;
                    new HomeActivity.CallServiceNoLoad().execute(AppConfig.apiUrl);
                    txtComment.setText("");
                }
            }
        });
        Button btnUserViewAllPins = (Button)findViewById(R.id.btnUserViewAllPins);
        btnUserViewAllPins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout layoutUserViewAllPins = (RelativeLayout)findViewById(R.id.layoutUserViewAllPins);
                RelativeLayout layoutUserPreview = (RelativeLayout)findViewById(R.id.layoutUserPreview);
                layoutUserPreview.setVisibility(GONE);
                layoutUserViewAllPins.setVisibility(View.VISIBLE);
                loadPermissionsMapUserViewAllPins(Manifest.permission.ACCESS_FINE_LOCATION,REQUEST_FINE_LOCATION);
            }
        });
        ImageButton btnUserViewAllPinsBack = (ImageButton)findViewById(R.id.btnUserViewAllPinsback);
        btnUserViewAllPinsBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout layoutUserViewAllPins = (RelativeLayout)findViewById(R.id.layoutUserViewAllPins);
                RelativeLayout layoutUserPreview = (RelativeLayout)findViewById(R.id.layoutUserPreview);
                layoutUserPreview.setVisibility(View.VISIBLE);
                layoutUserViewAllPins.setVisibility(View.GONE);
            }
        });

        ImageButton btnUserPinPreviewback = (ImageButton)findViewById(R.id.btnUserPinPreviewback);
        btnUserPinPreviewback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout layoutUserPinPreview = (RelativeLayout)findViewById(R.id.layoutUserPinPreview);
                RelativeLayout layoutUserPreview = (RelativeLayout)findViewById(R.id.layoutUserPreview);
                layoutUserPinPreview.setVisibility(View.GONE);
                layoutUserPreview.setVisibility(View.VISIBLE);

                TextView lblUserPinPreviewHead = (TextView)findViewById(R.id.lblUserPinPreviewHead);
                TextView lblUserPinPreviewDesc = (TextView)findViewById(R.id.lblUserPinPreviewDesc);
                ImageView imgUserPinPreview = (ImageView)findViewById(R.id.imgUserPinPreviewImage);
                lblUserPinPreviewHead.setText("");
                lblUserPinPreviewDesc.setText("");
                String imgUserPreviewPlaceUrl = AppConfig.noImgUrl;
                Picasso.with(HomeActivity.this).load(imgUserPreviewPlaceUrl).into(imgUserPinPreview);
                LATLNG_PIN_PREVIEW = null;
            }
        });
        ImageView userPhoto = (ImageView) findViewById(R.id.userPhoto);
        userPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] items = { "View", "Change" };
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                builder.setTitle("Profile Photo.");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (items[item].equals("View")) {
                            RelativeLayout layoutImagePreview = (RelativeLayout)findViewById(R.id.layoutTab5ImagePreview);
                            layoutImagePreview.setVisibility(View.VISIBLE);
//                            LinearLayout layoutStartPin = (LinearLayout)findViewById(R.id.layoutStartPin);
//                            layoutStartPin.setVisibility(GONE);

                            ImageView imgPreview = (ImageView)findViewById(R.id.imgTab5ImagePreview);
                            Picasso.with(getApplicationContext()).load(PROFILE_IMG_URL).into(imgPreview);
                        } else if (items[item].equals("Change")) {
                            //camera or pick
                            loadPermissionsCameraGalleryUserProfile(Manifest.permission.CAMERA,REQUEST_PERMISSION_CAMERA_GALLERY_PROFILE);
                        }
                    }
                });
                builder.show();
            }
        });
        ImageButton btnTab5CloseImagePreview = (ImageButton) findViewById(R.id.btnTab5CloseImagePreview);
        btnTab5CloseImagePreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout layoutImagePreview = (RelativeLayout)findViewById(R.id.layoutTab5ImagePreview);
                layoutImagePreview.setVisibility(View.GONE);
                ImageView imgPreview = (ImageView)findViewById(R.id.imgTab5ImagePreview);
                imgPreview.setImageBitmap(null);
            }
        });
        ImageButton btnFindUserBack = (ImageButton)findViewById(R.id.btnFindUserBack);
        btnFindUserBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout layoutFindFriend = (RelativeLayout)findViewById(R.id.layoutUserFindDetail);
                mTabHost.setCurrentTab(BACK_TO_TAB);
                layoutFindFriend.setVisibility(View.GONE);
            }
        });

        Button btnEditProfile = (Button)findViewById(R.id.btnEditProfile);
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout layoutEditProfile = (RelativeLayout)findViewById(R.id.layoutEditProfile);
                layoutEditProfile.setVisibility(View.VISIBLE);
                SharedPreferences prefs = getSharedPreferences(AppConfig.preRef, MODE_PRIVATE);
                EditText txtFullname = (EditText)findViewById(R.id.txtFullname);
                EditText txtEmail = (EditText) findViewById(R.id.txtEmail);
                EditText txtPhone = (EditText) findViewById(R.id.txtPhone);
                txtFullname.setText(USER_NAME);
                txtEmail.setText(USER_EMAIL);
                txtPhone.setText(USER_PHONE);
            }
        });
        ImageButton btnEditProfileback = (ImageButton)findViewById(R.id.btnEditProfileback);
        btnEditProfileback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout layoutEditProfile = (RelativeLayout)findViewById(R.id.layoutEditProfile);
                layoutEditProfile.setVisibility(View.GONE);
            }
        });
        Button btnSaveEditProfile = (Button)findViewById(R.id.btnSaveEditProfile);
        btnSaveEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText txtFullname = (EditText)findViewById(R.id.txtFullname);
                EditText txtEmail = (EditText) findViewById(R.id.txtEmail);
                EditText txtPhone = (EditText) findViewById(R.id.txtPhone);
                USER_NAME = txtFullname.getText().toString();
                USER_EMAIL = txtEmail.getText().toString();
                USER_PHONE = txtPhone.getText().toString();
                METHOD = "/updateUser";
                new HomeActivity.CallService().execute(AppConfig.apiUrl);
            }
        });
        ImageButton btnFindViewGrid = (ImageButton)findViewById(R.id.btnFindViewGrid);
        btnFindViewGrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GridView findgrid = (GridView)findViewById(R.id.gridFindUser);
                ListView findlist = (ListView)findViewById(R.id.listFindUser);
                findgrid.setVisibility(View.VISIBLE);
                findlist.setVisibility(GONE);
                doLoadDataFindUserGrid();
            }
        });
        ImageButton btnFindViewList = (ImageButton)findViewById(R.id.btnFindViewList);
        btnFindViewList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GridView findgrid = (GridView)findViewById(R.id.gridFindUser);
                ListView findlist = (ListView)findViewById(R.id.listFindUser);
                findgrid.setVisibility(View.GONE);
                findlist.setVisibility(View.VISIBLE);
                doLoadDataFindUserList();
            }
        });
        Button btnFindFollow = (Button)findViewById(R.id.btnFindFollow);
        btnFindFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tag = (int)v.getTag();
                FOLLOW_USER_ID = USER_DETAIL_ID;
                Log.d("DEBUG","TAG = "+String.valueOf(tag));
                if(tag==0){ // follow
                    METHOD = "/addFollow";
                    new HomeActivity.CallServiceNoLoad().execute(AppConfig.apiUrl);
                }
                else{ //unfollow
                    METHOD = "/delFollow";
                    new HomeActivity.CallServiceNoLoad().execute(AppConfig.apiUrl);
                }
            }
        });
    }
    public void buttonCreateRouteEvent(){
        /* Create Route -> Add Pin */
        final ImageButton btnAddPin = (ImageButton) findViewById(R.id.btnPointPin);
        btnAddPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_item));
                if(!isSavePopup){
                    do{
                        int numpin = allPinsAdd.size()+1;
                        TextView lblPinHead  = (TextView)findViewById(R.id.lblCreatePinHead);
                        TextView lblPinname  = (TextView)findViewById(R.id.lblPinname);
                        TextView lblPindesc = (TextView)findViewById(R.id.lblPindesc);
                        EditText txtPinname = (EditText)findViewById(R.id.txtPinname);
                        EditText txtPindesc = (EditText)findViewById(R.id.txtPinDesc);
                        txtPinname.setText("Pin "+String.valueOf(numpin));
                        GridView grid = (GridView) findViewById(R.id.gridPlaceImgPreview);
                        ImageButton btnAddPic = (ImageButton)findViewById(R.id.btnAddPinImage);
                        Button btnSavepin = (Button)findViewById(R.id.btnInnerSavepin);
                        LinearLayout layoutBtnPin = (LinearLayout)findViewById(R.id.layoutBtnPin);
                        layoutBtnPin.setVisibility(GONE);
                        lblPinname.setVisibility(View.VISIBLE);
                        lblPindesc.setVisibility(View.VISIBLE);
                        txtPinname.setVisibility(View.VISIBLE);
                        txtPindesc.setVisibility(View.VISIBLE);
                        btnAddPic.setVisibility(View.VISIBLE);
                        btnSavepin.setVisibility(View.VISIBLE);
                        grid.setVisibility(View.VISIBLE);
                        lblPinHead.setText("Add Pin");
                        btnAddPin.setImageResource(R.drawable.ic_pin_active);
                        if(allImagePinsAdd.size()<1) {
                            userChoosenTask = "TP_PIN";
                            cameraIntent();
                        }
                        LinearLayout layoutMap = (LinearLayout)findViewById(R.id.layoutMap);

                        layoutMap.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 500));
                        isCreatePin = true;
                    }while(false);
                }
            }
        });
        /* Create Route Add Pin */

        /* Add Pin */
        final Button btnInnerSavePin = (Button) findViewById(R.id.btnInnerSavepin);
        btnInnerSavePin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_item));
                do{
                    EditText txtPinname = (EditText)findViewById(R.id.txtPinname);
                    EditText txtPindesc = (EditText)findViewById(R.id.txtPinDesc);
                    P_NAME = txtPinname.getText().toString();
                    P_DESC = txtPindesc.getText().toString();
                    if(TextUtils.isEmpty(P_NAME)){
                        Toast.makeText(getApplication(), "Please input Pin name",
                                Toast.LENGTH_LONG).show();
                        break;
                    }
//                    if(TextUtils.isEmpty(P_DESC)){
//                        Toast.makeText(getApplication(), "Please input Pin description",
//                                Toast.LENGTH_LONG).show();
//                        break;
//                    }
                    btnAddPin.setImageResource(R.drawable.ic_pin);
                    METHOD="/addPlace";
                    new HomeActivity.CallService().execute(AppConfig.apiUrl);

//                    ImageButton btnFinishPin = (ImageButton)findViewById(R.id.btnFinishPin);
//                    btnFinishPin.setImageResource(R.drawable.ic_finish_pin_active);
//                    RelativeLayout layoutFinish = (RelativeLayout)findViewById(R.id.layoutFinishAddPin);
//                    layoutFinish.setVisibility(View.VISIBLE);
//                    ImageButton btnFinishYes = (ImageButton)findViewById(R.id.btnFinishYes);
//                    ImageButton btnFinishNo = (ImageButton)findViewById(R.id.btnFinishNo);
//                    btnFinishYes.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            METHOD="/addPlace";
//                            new HomeActivity.CallService().execute(AppConfig.apiUrl);
//                        }
//                    });
//                    btnFinishNo.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            ImageButton btnFinishPin = (ImageButton)findViewById(R.id.btnFinishPin);
//                            btnFinishPin.setImageResource(R.drawable.ic_finish_pin);
//                            RelativeLayout layoutFinish = (RelativeLayout)findViewById(R.id.layoutFinishAddPin);
//                            layoutFinish.setVisibility(View.GONE);
//                            btnAddPin.setImageResource(R.drawable.ic_pin_active);
//                        }
//                    });

                }while(false);
            }
        });
        /* Add Pin */
        Button btnAddRouteCollapse = (Button)findViewById(R.id.btnAddRouteCollapse);
        Button btnEditRouteCollapse = (Button)findViewById(R.id.btnEditRouteCollapse);
        btnAddRouteCollapse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                RelativeLayout layoutAddRouteCollapse = (RelativeLayout)findViewById(R.id.layoutAddRouteDetail);
//                layoutAddRouteCollapse.setVisibility(View.VISIBLE);

                LinearLayout layoutAddRouteCollapse = (LinearLayout)findViewById(R.id.layoutAddRouteDetail);
                ImageView imgArrowUpdown = (ImageView) findViewById(R.id.imgAddRouteCollapse);
                if (layoutAddRouteCollapse.getVisibility() == View.VISIBLE) {
                    // Its visible
                    expandOrCollapse(layoutAddRouteCollapse,"collapse");
                    imgArrowUpdown.setImageResource(R.drawable.arrow_down);
                } else {
                    // Either gone or invisible
                    expandOrCollapse(layoutAddRouteCollapse,"expand");
                    imgArrowUpdown.setImageResource(R.drawable.arrow_up);

                }

            }
        });
//        ImageButton btnCloseRoutePopup = (ImageButton)findViewById(R.id.btnCloseRoutePopup);
//        btnCloseRoutePopup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                RelativeLayout layoutAddRouteCollapse = (RelativeLayout)findViewById(R.id.layoutAddRouteDetail);
//                layoutAddRouteCollapse.setVisibility(View.GONE);
////                ImageView imgArrowUpdown = (ImageView) findViewById(R.id.imgAddRouteCollapse);
////                if (layoutAddRouteCollapse.getVisibility() == View.VISIBLE) {
////                    // Its visible
////                    expandOrCollapse(layoutAddRouteCollapse,"collapse");
////                    imgArrowUpdown.setImageResource(R.drawable.arrow_down);
////                } else {
////                    // Either gone or invisible
////                    expandOrCollapse(layoutAddRouteCollapse,"expand");
////                    imgArrowUpdown.setImageResource(R.drawable.arrow_up);
////
////                }
//
//            }
//        });
//        ImageButton btnCloseEditRoutePopup = (ImageButton)findViewById(R.id.btnCloseEditRoutePopup);
//        btnCloseEditRoutePopup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                RelativeLayout layoutEditRouteCollapse = (RelativeLayout)findViewById(R.id.layoutEditRouteDetail);
//                layoutEditRouteCollapse.setVisibility(View.GONE);
////                ImageView imgArrowUpdown = (ImageView) findViewById(R.id.imgAddRouteCollapse);
////                if (layoutAddRouteCollapse.getVisibility() == View.VISIBLE) {
////                    // Its visible
////                    expandOrCollapse(layoutAddRouteCollapse,"collapse");
////                    imgArrowUpdown.setImageResource(R.drawable.arrow_down);
////                } else {
////                    // Either gone or invisible
////                    expandOrCollapse(layoutAddRouteCollapse,"expand");
////                    imgArrowUpdown.setImageResource(R.drawable.arrow_up);
////
////                }
//
//            }
//        });
        btnEditRouteCollapse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout layoutEditRouteCollapse = (RelativeLayout)findViewById(R.id.layoutEditRouteDetail);
                layoutEditRouteCollapse.setVisibility(View.VISIBLE);
//                ImageView imgArrowUpdown = (ImageView) findViewById(R.id.imgEditRouteCollapse);
//                if (layoutEditRouteCollapse.getVisibility() == View.VISIBLE) {
//                    // Its visible
//                    expandOrCollapse(layoutEditRouteCollapse,"collapse");
//                    imgArrowUpdown.setImageResource(R.drawable.arrow_down);
//                } else {
//                    // Either gone or invisible
//                    expandOrCollapse(layoutEditRouteCollapse,"expand");
//                    imgArrowUpdown.setImageResource(R.drawable.arrow_up);
//
//                }
            }
        });


        /* Add Route Pic */
        ImageButton btnAddPicRoute = (ImageButton)findViewById(R.id.btnAddPic);
        btnAddPicRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(allImageRouteAdd.size()==0){
                    selectImageWithGallery();
                }else{
                    final CharSequence[] items = { "View", "Delete",
                            "Cancel" };
                    AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                    builder.setTitle("Manage Photo.");
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            if (items[item].equals("View")) {
                                RelativeLayout layoutImagePreview = (RelativeLayout)findViewById(R.id.layoutTab3ImagePreview);
                                layoutImagePreview.setVisibility(View.VISIBLE);
                                LinearLayout layoutStartPin = (LinearLayout)findViewById(R.id.layoutStartPin);
                                layoutStartPin.setVisibility(GONE);

                                ImageView imgPreview = (ImageView)findViewById(R.id.imgTab3ImagePreview);
                                byte[] decodedBytes = new byte[0];
                                    decodedBytes = Base64.decode(allImageRouteAdd.get(0).get("route_img"), Base64.DEFAULT);
                                final Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes , 0, decodedBytes.length);
                                imgPreview.setImageBitmap(bitmap);
                            } else if (items[item].equals("Delete")) {
                                    allImageRouteAdd.remove(0);
                                    ImageButton btnAddPic = (ImageButton)findViewById(R.id.btnAddPic);
                                    btnAddPic.setImageResource(R.drawable.cover_img_plus);
                            } else if (items[item].equals("Cancel")) {
                                dialog.dismiss();
                            }
                        }
                    });
                    builder.show();
                }

            }
        });
        Button btnCameraPicRoute = (Button)findViewById(R.id.btnRouteTakePhoto);
        btnCameraPicRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(allImageRouteAdd.size()==0){
                    selectImage();
                }else{
                    final CharSequence[] items = { "View", "Delete",
                            "Cancel" };
                    AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                    builder.setTitle("Manage Photo.");
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            if (items[item].equals("View")) {
                                RelativeLayout layoutImagePreview = (RelativeLayout)findViewById(R.id.layoutTab3ImagePreview);
                                layoutImagePreview.setVisibility(View.VISIBLE);
                                LinearLayout layoutStartPin = (LinearLayout)findViewById(R.id.layoutStartPin);
                                layoutStartPin.setVisibility(GONE);

                                ImageView imgPreview = (ImageView)findViewById(R.id.imgTab3ImagePreview);
                                byte[] decodedBytes = new byte[0];
                                decodedBytes = Base64.decode(allImageRouteAdd.get(0).get("route_img"), Base64.DEFAULT);
                                final Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes , 0, decodedBytes.length);
                                imgPreview.setImageBitmap(bitmap);
                            } else if (items[item].equals("Delete")) {
                                allImageRouteAdd.remove(0);
                                ImageButton btnAddPic = (ImageButton)findViewById(R.id.btnAddPic);
                                btnAddPic.setImageResource(R.drawable.cover_img_plus);
                            } else if (items[item].equals("Cancel")) {
                                dialog.dismiss();
                            }
                        }
                    });
                    builder.show();
                }

            }
        });
        /* Add Route Pic */

        /* Add Pin Image */
        ImageButton btnAddPinImage = (ImageButton)findViewById(R.id.btnAddPinImage);
        btnAddPinImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(allImagePinsAdd.size()==0){
                    selectImagePin();
                }else{
                    final CharSequence[] items = { "View", "Delete",
                            "Cancel" };
                    AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                    builder.setTitle("Manage Photo.");
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            if (items[item].equals("View")) {
                                RelativeLayout layoutImagePreview = (RelativeLayout)findViewById(R.id.layoutTab3ImagePreview);
                                layoutImagePreview.setVisibility(View.VISIBLE);
                                RelativeLayout layoutAddPin = (RelativeLayout)findViewById(R.id.layoutAddPin);
                                layoutAddPin.setVisibility(GONE);

                                ImageView imgPreview = (ImageView)findViewById(R.id.imgTab3ImagePreview);
                                byte[] decodedBytes = new byte[0];
                                decodedBytes = Base64.decode(allImagePinsAdd.get(0).get("place_img"), Base64.DEFAULT);
                                final Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes , 0, decodedBytes.length);
                                imgPreview.setImageBitmap(bitmap);
                            } else if (items[item].equals("Delete")) {
                                allImagePinsAdd.remove(0);
                                ImageButton btnAddPic = (ImageButton)findViewById(R.id.btnAddPinImage);
                                btnAddPic.setImageResource(R.drawable.cover_img_plus);
                            } else if (items[item].equals("Cancel")) {
                                dialog.dismiss();
                            }
                        }
                    });
                    builder.show();
                }

            }
        });
        /* Add Pin Image */

        /* Save Pin */
        final ImageButton btnSavePin = (ImageButton) findViewById(R.id.btnSavePin);
        btnSavePin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_item));
                do{
                    ClearAddPinView();
                    ClearAddPinValue();
                    isSavePopup = true;
                    btnSavePin.setImageResource(R.drawable.ic_pin_save_active);
                    RelativeLayout layoutSavePin = (RelativeLayout)findViewById(R.id.layoutSaveDraftAddPin);
                    layoutSavePin.setVisibility(View.VISIBLE);
                    ImageButton btnSaveDraftYes = (ImageButton)findViewById(R.id.btnSaveDraftYes);
                    ImageButton btnSaveDraftNo = (ImageButton)findViewById(R.id.btnSaveDraftNo);
                    btnSaveDraftYes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            do{
//                                if(allPinsAdd.size()==0){
//                                    Toast.makeText(getApplication(), "Please add pin at least 1 location.",
//                                            Toast.LENGTH_LONG).show();
//                                    break;
//                                }
                                btnSavePin.setImageResource(R.drawable.ic_pin_save);
//                                ImageButton btnFinishPin = (ImageButton)findViewById(R.id.btnFinishPin);
//                                btnFinishPin.setImageResource(R.drawable.ic_finish_pin_active);
                                RelativeLayout layoutSavePin = (RelativeLayout)findViewById(R.id.layoutSaveDraftAddPin);
                                layoutSavePin.setVisibility(GONE);


                            }while (false);
                        }
                    });
                    btnSaveDraftNo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            isSavePopup = false;
                            btnSavePin.setImageResource(R.drawable.ic_pin_save);
                            RelativeLayout layoutSavePin = (RelativeLayout)findViewById(R.id.layoutSaveDraftAddPin);
                            layoutSavePin.setVisibility(GONE);
                        }
                    });

                }while(false);
            }
        });
        /* Save Pin */

        /* Finish Pin */
        final ImageButton btnFinishPin = (ImageButton) findViewById(R.id.btnFinishPin);
        btnFinishPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_item));
                do{
                    ClearAddPinView();
                    ClearAddPinValue();
                    isSavePopup = true;
                    ImageButton btnFinishPin = (ImageButton)findViewById(R.id.btnFinishPin);
                    btnFinishPin.setImageResource(R.drawable.ic_finish_pin_active);
                    RelativeLayout layoutSavePin = (RelativeLayout)findViewById(R.id.layoutSaveDraftAddPin);
                    layoutSavePin.setVisibility(GONE);
                    RelativeLayout layoutFinish = (RelativeLayout)findViewById(R.id.layoutFinishAddPin);
                    layoutFinish.setVisibility(View.VISIBLE);

                }while(false);
            }
        });
        ImageButton btnFinishYes = (ImageButton)findViewById(R.id.btnFinishYes);
        ImageButton btnFinishNo = (ImageButton)findViewById(R.id.btnFinishNo);
        btnFinishYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                METHOD="/finishRoute";
                new HomeActivity.CallService().execute(AppConfig.apiUrl);
            }
        });
        btnFinishNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSavePopup = false;
                btnFinishPin.setImageResource(R.drawable.ic_finish_pin);
                RelativeLayout layoutFinishPin = (RelativeLayout)findViewById(R.id.layoutFinishAddPin);
                layoutFinishPin.setVisibility(GONE);
            }
        });

        /* Finish Pin */

        /* Publish Pin */
        final Button btnPublish = (Button) findViewById(R.id.btnPublishPreview);
        btnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(HomeActivity.this)
                        .setTitle("Publish Confirm")
                        .setMessage("Do you really want to publish?")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                METHOD="/publishRoute";
                                new HomeActivity.CallService().execute(AppConfig.apiUrl);
                            }})
                        .setNegativeButton(android.R.string.no, null).show();

            }
        });

        /* Publish Pin */

        /* Create Route */
        Button btnAddRoute = (Button)findViewById(R.id.btnContinue);
        btnAddRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_item));
                //validate input
                EditText txtTitle = (EditText) findViewById(R.id.txtTitle);
                EditText txtRouteDesc = (EditText) findViewById(R.id.txtRouteDesc);
                Spinner txtActivity = (Spinner) findViewById(R.id.txtActivity) ;
                EditText txtCity = (EditText)findViewById(R.id.txtCity);
                Spinner txtTravelMethod = (Spinner) findViewById(R.id.txtTravelMethod);
                EditText txtBudgetMin = (EditText)findViewById(R.id.txtBudgetMin);
                EditText txtBudgetMax = (EditText)findViewById(R.id.txtBudgetMax);
                EditText txtSuggestion = (EditText) findViewById(R.id.txtSuggestion);
                String routeTitle = txtTitle.getText().toString();
                String routeDesc = txtRouteDesc.getText().toString();
                do{
                    if(TextUtils.isEmpty(routeTitle)){
                        Toast.makeText(getApplication(), "Please input Title",
                                Toast.LENGTH_LONG).show();
                        break;
                    }
//                    if(TextUtils.isEmpty(routeDesc)){
//                        Toast.makeText(getApplication(), "Please input Description",
//                                Toast.LENGTH_LONG).show();
//                        break;
//                    }
                    R_TITLE = routeTitle;
                    R_DESC=routeDesc;
                    R_ACTIVITY = txtActivity.getSelectedItem().toString();
                    R_CITY = txtCity.getText().toString();
                    R_TRAVEl_METHOD=txtTravelMethod.getSelectedItem().toString();
                    R_BUDGET_MIN = txtBudgetMin.getText().toString();
                    R_BUDGET_MAX = txtBudgetMax.getText().toString();
                    R_SUGGESTION = txtSuggestion.getText().toString();
                    if(TextUtils.isEmpty(R_ID)){
                        METHOD = "/addRoute";
                    }
                    else{
                        METHOD = "/updateRoute";
                    }
                    Log.d("JSON_CALL","METHOD = "+METHOD);
                    Log.d("JSON_CALL","R_ID = "+R_ID);
                    CallService service = new HomeActivity.CallService();
                    service.execute(AppConfig.apiUrl);
                }while(false);
            }
        });
        ImageButton btnBackPin = (ImageButton)findViewById(R.id.btnCreatePinback);
        btnBackPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_item));
                if(!isCreatePin){ //Hide Map layout
                    LinearLayout layoutAddPlace = (LinearLayout)findViewById(R.id.layoutStartPin);
                    RelativeLayout layoutCreatePin = (RelativeLayout)findViewById(R.id.layoutAddPin);
                    layoutCreatePin.setVisibility(GONE);
                    layoutAddPlace.setVisibility(View.VISIBLE);
                }
                else{ //Hide Add Pin Layout
                    ClearAddPinView();
                }

            }
        });
        /* Create Route */

        /* Preview Route */
        ImageButton btnBackCreatePinPreview = (ImageButton)findViewById(R.id.btnCreatePinPreviewback);
        btnBackCreatePinPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPreviewFromAdd){
                    v.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_item));
                    LinearLayout layoutAddRoute = (LinearLayout)findViewById(R.id.layoutStartPin);
                    layoutAddRoute.setVisibility(View.VISIBLE);
                    RelativeLayout layoutCreatePreviewPin = (RelativeLayout)findViewById(R.id.layoutAddPinPreview);
                    layoutCreatePreviewPin.setVisibility(GONE);
                    GridView gridOverview = (GridView)findViewById(R.id.gridCreatePinOverview);
                    gridOverview.setAdapter(null);
                    TextView lbltitle = (TextView)findViewById(R.id.lblCreatePinOverviewTitle);
                    TextView lbldetail = (TextView)findViewById(R.id.lblCreatePinOverviewDetail);
                    lbldetail.setText("Loading...");
                    lbltitle.setText("");
                    ListView listPins = (ListView)findViewById(R.id.listViewAddPinPreview);
                    listPins.setAdapter(null);
                    ImageView imgPreview = (ImageView)findViewById(R.id.imgCreatePinOverview);
                    imgPreview.setImageBitmap(null);
                    ViewGroup.LayoutParams imgLayoutParams = imgPreview.getLayoutParams();
                    imgLayoutParams.height=0;
                    imgPreview.setLayoutParams(imgLayoutParams);
                    isPreviewFromAdd=false;
                }

                if(isPreviewFromDraft){
                    LinearLayout layoutStartPin = (LinearLayout)findViewById(R.id.layoutStartPin);
                    layoutStartPin.setVisibility(View.VISIBLE);
                    RelativeLayout layoutCreatePreviewPin = (RelativeLayout)findViewById(R.id.layoutAddPinPreview);
                    layoutCreatePreviewPin.setVisibility(View.GONE);
                    GridView gridOverview = (GridView)findViewById(R.id.gridCreatePinOverview);
                    gridOverview.setAdapter(null);
                    TextView lbltitle = (TextView)findViewById(R.id.lblCreatePinOverviewTitle);
                    TextView lbldetail = (TextView)findViewById(R.id.lblCreatePinOverviewDetail);
                    lbldetail.setText("Loading...");
                    lbltitle.setText("");
                    ListView listPins = (ListView)findViewById(R.id.listViewAddPinPreview);
                    listPins.setAdapter(null);
                    ImageView imgPreview = (ImageView)findViewById(R.id.imgCreatePinOverview);
                    imgPreview.setImageBitmap(null);
                    ViewGroup.LayoutParams imgLayoutParams = imgPreview.getLayoutParams();
                    imgLayoutParams.height=0;
                    imgPreview.setLayoutParams(imgLayoutParams);
                    isPreviewFromDraft=false;
                    doLoadPinDraft();
                }

                isPublishPreview=false;
                ClearRouteEditValue();
            }
        });

        Button btnEditRoute = (Button)findViewById(R.id.btnEditRoute);
        btnEditRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout layoutCreatePreviewPin = (RelativeLayout)findViewById(R.id.layoutAddPinPreview);
                layoutCreatePreviewPin.setVisibility(View.GONE);
                RelativeLayout layoutEditPin = (RelativeLayout)findViewById(R.id.layoutEditRoute);
                layoutEditPin.setVisibility(View.VISIBLE);
            }
        });
        Button btnCameraPicRouteEdit = (Button)findViewById(R.id.btnRouteTakePhotoEdit);
        btnCameraPicRouteEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(allImageRouteEdit.size()==0){
                    selectImageEdit();
                }else{
                    final CharSequence[] items = { "Delete",
                            "Cancel" };
                    AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                    builder.setTitle("Manage Photo.");
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            if (items[item].equals("Delete")) {
                                allImageRouteEdit.clear();
                                ImageButton btnAddPic = (ImageButton)findViewById(R.id.btnAddPicEdit);
                                btnAddPic.setImageResource(R.drawable.cover_img_plus);
                            } else if (items[item].equals("Cancel")) {
                                dialog.dismiss();
                            }
                        }
                    });
                    builder.show();
                }
            }
        });
        ImageButton btnBackEditRoute = (ImageButton)findViewById(R.id.btnEditPreviewback);
        btnBackEditRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScrollView scrollEdit = (ScrollView)findViewById(R.id.scrollEditRoute);
                scrollEdit.smoothScrollTo(0,0);
                RelativeLayout layoutCreatePreviewPin = (RelativeLayout)findViewById(R.id.layoutAddPinPreview);
                layoutCreatePreviewPin.setVisibility(View.VISIBLE);
                RelativeLayout layoutEditPin = (RelativeLayout)findViewById(R.id.layoutEditRoute);
                layoutEditPin.setVisibility(View.GONE);
                doLoadDataPreviewOverview();
            }
        });
        Button btnSaveEditRoute = (Button)findViewById(R.id.btnContinueEdit);
        btnSaveEditRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_item));
                //validate input
                EditText txtTitleEdit = (EditText)findViewById(R.id.txtTitleEdit);
                EditText txtRouteDescEdit = (EditText)findViewById(R.id.txtRouteDescEdit);
                Spinner txtActivityEdit = (Spinner)findViewById(R.id.txtActivityEdit);
//                                EditText txtCountryEdit = (EditText)findViewById(R.id.txtCountryEdit);
                EditText txtCityEdit = (EditText)findViewById(R.id.txtCityEdit);
                Spinner txtTravelEdit = (Spinner)findViewById(R.id.txtTravelMethodEdit);
//                                EditText txtTimeFrameEdit = (EditText)findViewById(R.id.txtTimeframeEdit);
                EditText txtBudgetMinEdit = (EditText)findViewById(R.id.txtBudgetMinEdit);
                EditText txtBudgetMaxEdit = (EditText)findViewById(R.id.txtBudgetMaxEdit);
                EditText txtSuggestionEdit = (EditText)findViewById(R.id.txtSuggestionEdit);
                String routeTitle = txtTitleEdit.getText().toString();
                String routeDesc = txtRouteDescEdit.getText().toString();
                do{
                    if(TextUtils.isEmpty(routeTitle)){
                        Toast.makeText(getApplication(), "Please input Title",
                                Toast.LENGTH_LONG).show();
                        break;
                    }
//                    if(TextUtils.isEmpty(routeDesc)){
//                        Toast.makeText(getApplication(), "Please input Description",
//                                Toast.LENGTH_LONG).show();
//                        break;
//                    }
                    R_TITLE = routeTitle;
                    R_DESC=routeDesc;
                    R_ACTIVITY = txtActivityEdit.getSelectedItem().toString();
                    R_CITY = txtCityEdit.getText().toString();
                    R_TRAVEl_METHOD=txtTravelEdit.getSelectedItem().toString();
                    R_BUDGET_MIN = txtBudgetMinEdit.getText().toString();
                    R_BUDGET_MAX = txtBudgetMaxEdit.getText().toString();
                    R_SUGGESTION = txtSuggestionEdit.getText().toString();
                    METHOD = "/updateRouteEdit";
                    Log.d("JSON_CALL","METHOD = "+METHOD);
                    Log.d("JSON_CALL","R_ID = "+R_ID);
                    CallService service = new HomeActivity.CallService();
                    service.execute(AppConfig.apiUrl);
                }while(false);
            }
        });
        ImageButton btnEditPicRoute = (ImageButton)findViewById(R.id.btnAddPicEdit);
        btnEditPicRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(allImageRouteEdit.size()==0){
                    selectImageWithGalleryEdit();
                }else{
                    final CharSequence[] items = { "Delete",
                            "Cancel" };
                    AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                    builder.setTitle("Manage Photo.");
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            if (items[item].equals("Delete")) {
                                allImageRouteEdit.clear();
                                ImageButton btnAddPic = (ImageButton)findViewById(R.id.btnAddPicEdit);
                                btnAddPic.setImageResource(R.drawable.cover_img_plus);
                            } else if (items[item].equals("Cancel")) {
                                dialog.dismiss();
                            }
                        }
                    });
                    builder.show();
                }

            }
        });

        Button btnAddPinEdit = (Button)findViewById(R.id.btnAddPinEdit);
        btnAddPinEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int numcount = allPinsListEdit.size()+1;
                EditText txtPinnameEdit = (EditText)findViewById(R.id.txtPinnameEdit);
                txtPinnameEdit.setText("Pin "+String.valueOf(numcount));
                EDIT_PLACE_ID = "";
                ScrollView scrollEditPin = (ScrollView)findViewById(R.id.scrollEditPin);
                scrollEditPin.smoothScrollTo(0,0);
                TextView lblEditPinHead = (TextView)findViewById(R.id.lblEditPinHead);
                lblEditPinHead.setText("Add Pin");
                RelativeLayout layoutEditPin = (RelativeLayout)findViewById(R.id.layoutEditPin);
                layoutEditPin.setVisibility(View.VISIBLE);
                RelativeLayout layoutPreview = (RelativeLayout)findViewById(R.id.layoutAddPinPreview);
                layoutPreview.setVisibility(GONE);
                ismapZoomFirst=false;
                if(allImagePinsEdit.size()==0){
                    selectImagePinEdit();
                }
                loadPermissionsMapEdit(Manifest.permission.ACCESS_FINE_LOCATION,REQUEST_FINE_LOCATION);
            }
        });
        ImageButton btnEditPinback = (ImageButton)findViewById(R.id.btnEditPinback);
        btnEditPinback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EDIT_PLACE_ID = "";
                TextView lblEditPinHead = (TextView)findViewById(R.id.lblEditPinHead);
                lblEditPinHead.setText("");
                EditText txtPinname = (EditText)findViewById(R.id.txtPinnameEdit);
                EditText txtPindesc = (EditText)findViewById(R.id.txtPinDescEdit);
                txtPinname.setText("");
                txtPindesc.setText("");
                ImageButton btnEditPicPin = (ImageButton)findViewById(R.id.btnEditPinImage);
                btnEditPicPin.setImageResource(R.drawable.cover_img_plus);
                allImagePinsEdit = new ArrayList<HashMap<String, String>>();
                allPinsEdit = new ArrayList<HashMap<String, String>>();
                mMap.clear();
                RelativeLayout layoutEditPin = (RelativeLayout)findViewById(R.id.layoutEditPin);
                layoutEditPin.setVisibility(View.GONE);
                RelativeLayout layoutPreview = (RelativeLayout)findViewById(R.id.layoutAddPinPreview);
                layoutPreview.setVisibility(View.VISIBLE);
                doLoadPreviewPins();
            }
        });
        ImageButton btnInnerSavepinEdit = (ImageButton)findViewById(R.id.btnInnerSavepinEdit);
        btnInnerSavepinEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_item));
                do{
                    EditText txtPinname = (EditText)findViewById(R.id.txtPinnameEdit);
                    EditText txtPindesc = (EditText)findViewById(R.id.txtPinDescEdit);
                    P_NAME = txtPinname.getText().toString();
                    P_DESC = txtPindesc.getText().toString();
                    if(TextUtils.isEmpty(P_NAME)){
                        Toast.makeText(getApplication(), "Please input Pin name",
                                Toast.LENGTH_LONG).show();
                        break;
                    }
//                    if(TextUtils.isEmpty(P_DESC)){
//                        Toast.makeText(getApplication(), "Please input Pin description",
//                                Toast.LENGTH_LONG).show();
//                        break;
//                    }
                    if(TextUtils.isEmpty(EDIT_PLACE_ID)){ //AddPin
                        METHOD="/addPlaceEdit";
                        new HomeActivity.CallService().execute(AppConfig.apiUrl);
                    }else{ // EditPin
                        METHOD="/updatePlace";
                        new HomeActivity.CallService().execute(AppConfig.apiUrl);
                    }

                }while(false);
            }
        });
        final ImageButton btnEditPicPin = (ImageButton)findViewById(R.id.btnEditPinImage);
        btnEditPicPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(allImagePinsEdit.size()==0){
                    selectImagePinEdit();
                }else{
                    final CharSequence[] items = { "Delete",
                            "Cancel" };
                    AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                    builder.setTitle("Manage Photo.");
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            if (items[item].equals("Delete")) {
                                    allImagePinsEdit.remove(0);
                                    btnEditPicPin.setImageResource(R.drawable.cover_img_plus);

                            } else if (items[item].equals("Cancel")) {
                                dialog.dismiss();
                            }
                        }
                    });
                    builder.show();
                }

            }
        });
        /* Preview Route */
        Button btnViewAllPins = (Button)findViewById(R.id.btnViewAllPins);
        btnViewAllPins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout layoutAddpinPreview = (RelativeLayout)findViewById(R.id.layoutAddPinPreview);
                layoutAddpinPreview.setVisibility(GONE);
                RelativeLayout layoutViewAllpins = (RelativeLayout)findViewById(R.id.layoutViewAllPins);
                layoutViewAllpins.setVisibility(View.VISIBLE);
                loadPermissionsMapViewAllPins(Manifest.permission.ACCESS_FINE_LOCATION,REQUEST_FINE_LOCATION);

            }
        });
        ImageButton btnViewAllPinsback = (ImageButton)findViewById(R.id.btnViewAllPinsback);
        btnViewAllPinsback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout layoutAddpinPreview = (RelativeLayout)findViewById(R.id.layoutAddPinPreview);
                layoutAddpinPreview.setVisibility(View.VISIBLE);
                RelativeLayout layoutViewAllpins = (RelativeLayout)findViewById(R.id.layoutViewAllPins);
                layoutViewAllpins.setVisibility(View.GONE);
            }
        });


        /* Image Preview */
        ImageButton btnTab3ImgPreview = (ImageButton)findViewById(R.id.btnTab3CloseImagePreview);
        btnTab3ImgPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout layoutImagePreview = (RelativeLayout)findViewById(R.id.layoutTab3ImagePreview);
                layoutImagePreview.setVisibility(GONE);
                if(userChoosenTask.endsWith("_ROUTE")){
                    LinearLayout layoutStartPin = (LinearLayout)findViewById(R.id.layoutStartPin);
                    layoutStartPin.setVisibility(View.VISIBLE);
                }
                if(userChoosenTask.endsWith("_PIN")) {
                    RelativeLayout layoutAddPin = (RelativeLayout)findViewById(R.id.layoutAddPin);
                    layoutAddPin.setVisibility(View.VISIBLE);
                }
                ImageView imgPreview = (ImageView)findViewById(R.id.imgTab3ImagePreview);
                imgPreview.setImageBitmap(null);
            }
        });
    }
    public void expandOrCollapse(final View v,String exp_or_colpse) {
        TranslateAnimation anim = null;
        if(exp_or_colpse.equals("expand"))
        {
            v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            final int targetHeight = v.getMeasuredHeight();

            // Older versions of android (pre API 21) cancel animations for views with a height of 0.
            v.getLayoutParams().height = 1;
            v.setVisibility(View.VISIBLE);
            Animation a = new Animation()
            {
                @Override
                protected void applyTransformation(float interpolatedTime, android.view.animation.Transformation t) {
                    v.getLayoutParams().height = interpolatedTime == 1
                            ? ViewGroup.LayoutParams.WRAP_CONTENT
                            : (int)(targetHeight * interpolatedTime);
                    v.requestLayout();
                }

                @Override
                public boolean willChangeBounds() {
                    return true;
                }
            };

            // 1dp/ms
            a.setDuration((int)(targetHeight / v.getContext().getResources().getDisplayMetrics().density));
            v.startAnimation(a);
        }
        else{
            final int initialHeight = v.getMeasuredHeight();

            Animation a = new Animation()
            {
                @Override
                protected void applyTransformation(float interpolatedTime, android.view.animation.Transformation t) {
                    if(interpolatedTime == 1){
                        v.setVisibility(View.GONE);
                    }else{
                        v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                        v.requestLayout();
                    }
                }

                @Override
                public boolean willChangeBounds() {
                    return true;
                }
            };

            // 1dp/ms
            a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density));
            v.startAnimation(a);
        }

    }
    public  void ClearRouteEditValue(){
        EditText txtTitleEdit = (EditText)findViewById(R.id.txtTitleEdit);
        EditText txtRouteDescEdit = (EditText)findViewById(R.id.txtRouteDescEdit);
        Spinner txtActivityEdit = (Spinner)findViewById(R.id.txtActivityEdit);
//                                EditText txtCountryEdit = (EditText)findViewById(R.id.txtCountryEdit);
        EditText txtCityEdit = (EditText)findViewById(R.id.txtCityEdit);
        Spinner txtTravelEdit = (Spinner)findViewById(R.id.txtTravelMethodEdit);
//                                EditText txtTimeFrameEdit = (EditText)findViewById(R.id.txtTimeframeEdit);
        EditText txtBudgetMinEdit = (EditText)findViewById(R.id.txtBudgetMinEdit);
        EditText txtBudgetMaxEdit = (EditText)findViewById(R.id.txtBudgetMaxEdit);
        EditText txtSuggestionEdit = (EditText)findViewById(R.id.txtSuggestionEdit);
        ImageButton btnAddPicEdit = (ImageButton) findViewById(R.id.btnAddPicEdit);
        txtTitleEdit.setText("");
        txtRouteDescEdit.setText("");
        txtActivityEdit.setSelection(0);
        txtCityEdit.setText("");
        txtTravelEdit.setSelection(0);
        txtBudgetMinEdit.setText("");
        txtBudgetMaxEdit.setText("");
        txtSuggestionEdit.setText("");
        RelativeLayout layoutEditRouteDetail = (RelativeLayout)findViewById(R.id.layoutEditRouteDetail);
        layoutEditRouteDetail.setVisibility(GONE);
        ImageView imgEditRouteCollapse = (ImageView) findViewById(R.id.imgEditRouteCollapse);
        imgEditRouteCollapse.setImageResource(R.drawable.arrow_down);
        allImageRouteEdit = new ArrayList<HashMap<String, String>>();
        btnAddPicEdit.setImageResource(R.drawable.cover_img_plus);
    }
    private void selectImageEdit() {
//        final CharSequence[] items = { "Take Photo", "Choose from Library",
//                "Cancel" };
//        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
//        builder.setTitle("Add Photo!");
//        builder.setItems(items, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int item) {
//                boolean result=MainUtility.checkPermission(HomeActivity.this);
//                if (items[item].equals("Take Photo")) {
//                    userChoosenTask="TP_ROUTE";
//                    if(result)
//                        cameraIntent();
//                } else if (items[item].equals("Choose from Library")) {
//                    userChoosenTask="CH_ROUTE";
//                    if(result)
//                        galleryIntent();
//                } else if (items[item].equals("Cancel")) {
//                    dialog.dismiss();
//                }
//            }
//        });
//        builder.show();
        if(allImageRouteEdit.size()<1) {
            userChoosenTask="TP_ROUTE_EDIT";
            cameraIntent();
        }else{
            Toast.makeText(this,"Limit 5 photos",Toast.LENGTH_SHORT).show();
        }


    }
    private void selectImageWithGalleryEdit() {
        if(allImageRouteAdd.size()<1) {
            userChoosenTask="TP_ROUTE_EDIT";
            cameraGalleryIntent();
        }else{
            Toast.makeText(this,"Limit 5 photos",Toast.LENGTH_SHORT).show();
        }

    }
    private void selectImage() {
//        final CharSequence[] items = { "Take Photo", "Choose from Library",
//                "Cancel" };
//        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
//        builder.setTitle("Add Photo!");
//        builder.setItems(items, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int item) {
//                boolean result=MainUtility.checkPermission(HomeActivity.this);
//                if (items[item].equals("Take Photo")) {
//                    userChoosenTask="TP_ROUTE";
//                    if(result)
//                        cameraIntent();
//                } else if (items[item].equals("Choose from Library")) {
//                    userChoosenTask="CH_ROUTE";
//                    if(result)
//                        galleryIntent();
//                } else if (items[item].equals("Cancel")) {
//                    dialog.dismiss();
//                }
//            }
//        });
//        builder.show();
        if(allImageRouteAdd.size()<1) {
            userChoosenTask="TP_ROUTE";
            cameraIntent();
        }else{
            Toast.makeText(this,"Limit 5 photos",Toast.LENGTH_SHORT).show();
        }


    }
    private void selectImageWithGallery() {
        if(allImageRouteAdd.size()<1) {
            userChoosenTask="TP_ROUTE";
            cameraGalleryIntent();
        }else{
            Toast.makeText(this,"Limit 5 photos",Toast.LENGTH_SHORT).show();
        }

    }
    private void selectImagePin() {
//        final CharSequence[] items = { "Take Photo", "Choose from Library",
//                "Cancel" };
//        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
//        builder.setTitle("Add Photo!");
//        builder.setItems(items, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int item) {
//                boolean result=MainUtility.checkPermission(HomeActivity.this);
//                if (items[item].equals("Take Photo")) {
//                    userChoosenTask="TP_PIN";
//                    if(result)
//                        cameraIntent();
//                } else if (items[item].equals("Choose from Library")) {
//                    userChoosenTask="CH_PIN";
//                    if(result)
//                        galleryIntent();
//                } else if (items[item].equals("Cancel")) {
//                    dialog.dismiss();
//                }
//            }
//        });
//        builder.show();
        if(allImagePinsAdd.size()<1) {
            userChoosenTask = "TP_PIN";
            cameraIntent();
        }else{
            Toast.makeText(this,"Limit 5 photos",Toast.LENGTH_SHORT).show();
        }
    }
    private void selectImagePinEdit() {
//        final CharSequence[] items = { "Take Photo", "Choose from Library",
//                "Cancel" };
//        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
//        builder.setTitle("Add Photo!");
//        builder.setItems(items, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int item) {
//                boolean result=MainUtility.checkPermission(HomeActivity.this);
//                if (items[item].equals("Take Photo")) {
//                    userChoosenTask="TP_PIN";
//                    if(result)
//                        cameraIntent();
//                } else if (items[item].equals("Choose from Library")) {
//                    userChoosenTask="CH_PIN";
//                    if(result)
//                        galleryIntent();
//                } else if (items[item].equals("Cancel")) {
//                    dialog.dismiss();
//                }
//            }
//        });
//        builder.show();
        if(allImagePinsEdit.size()<1) {
            userChoosenTask = "TP_PIN_EDIT";
            cameraIntent();
        }else{
            Toast.makeText(this,"Limit 5 photos",Toast.LENGTH_SHORT).show();
        }
    }
    private static int THUMBNAIL_SIZE = 700;
    private static Uri outputFileUri;
    private Bitmap bmp;
    private void cameraIntent()
    {
        loadPermissionsCamera(Manifest.permission.CAMERA,REQUEST_PERMISSION_CAMERA);
//        File file = new File(Environment.getExternalStorageDirectory()+File.separator + "image.jpg");
//        if(file.exists())
//            file.delete();
//            //Toast.makeText(getApplicationContext(), "Device man"+BX1, Toast.LENGTH_LONG).show();
//            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
//            startActivityForResult(intent, REQUEST_CAMERA);
        // Determine Uri of camera image to save.


    }
    private void cameraGalleryIntent()
    {
        loadPermissionsCameraGallery(Manifest.permission.CAMERA,REQUEST_PERMISSION_CAMERA_GALLERY);
//        File file = new File(Environment.getExternalStorageDirectory()+File.separator + "image.jpg");
//        if(file.exists())
//            file.delete();
//            //Toast.makeText(getApplicationContext(), "Device man"+BX1, Toast.LENGTH_LONG).show();
//            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
//            startActivityForResult(intent, REQUEST_CAMERA);
        // Determine Uri of camera image to save.


    }
    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }
    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight)
    { // BEST QUALITY MATCH

        //First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize, Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        int inSampleSize = 1;

        if (height > reqHeight)
        {
            inSampleSize = Math.round((float)height / (float)reqHeight);
        }
        int expectedWidth = width / inSampleSize;

        if (expectedWidth > reqWidth)
        {
            //if(Math.round((float)width / (float)reqWidth) > inSampleSize) // If bigger SampSize..
            inSampleSize = Math.round((float)width / (float)reqWidth);
        }

        options.inSampleSize = inSampleSize;

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bmp != null && !bmp.isRecycled()) {
            bmp.recycle();
            bmp = null;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_FILE){
                Log.d("DEBUG","RESULT Gallery OK");
                onSelectFromGalleryResult(data);
            }
            else if (requestCode == REQUEST_CAMERA){
                Log.d("DEBUG","RESULT Camera OK");
                //onCaptureImageResult(data);
                if (data != null) {
                    outputFileUri = data.getData();
                }
                try {
                    bmp = ImageUtils.getThumbnail(this, outputFileUri, THUMBNAIL_SIZE);
                }catch (IOException e) {
                    System.out.println(e.getMessage());
                }
                onCaptureImageResult(bmp);
            }
            else if (requestCode == REQUEST_CAMERA_PROFILE){
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
    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("ERROR",e.getMessage());
            }
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 90, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        if(userChoosenTask.equals("CH_ROUTE")){
            HashMap<String,String> hashRoutePic = new HashMap<String,String>();
            hashRoutePic.put("route_img_id","");
            hashRoutePic.put("route_img",encodedImage);
            allImageRouteAdd.add(hashRoutePic);
            ImageButton imgRoutePic = (ImageButton)findViewById(R.id.btnAddPic);
            imgRoutePic.setImageBitmap(bm);
//            GridView grid1 = (GridView)findViewById(R.id.gridRouteImgPreview);
//            grid1.setAdapter(new RouteImagePreviewAdapter(this,allImageRouteAdd));
//            MainUtility mUtility = new MainUtility();
//            mUtility.setGridViewHeightBasedOnChildren(grid1,3);
        }
        else if(userChoosenTask.equals("CH_PIN")){
            HashMap<String,String> hashRoutePic = new HashMap<String,String>();
            hashRoutePic.put("place_img_id","");
            hashRoutePic.put("place_img",encodedImage);
            allImagePinsAdd.add(hashRoutePic);
            ImageButton imgRoutePic = (ImageButton)findViewById(R.id.btnAddPinImage);
            imgRoutePic.setImageBitmap(bm);
//            GridView grid1 = (GridView)findViewById(R.id.gridPlaceImgPreview);
//            grid1.setAdapter(new RouteImagePreviewAdapter(this,allImagePinsAdd));
//            MainUtility mUtility = new MainUtility();
//            mUtility.setGridViewHeightBasedOnChildren(grid1,3);
        }
//        ImageView ivImage = (ImageView)findViewById(R.id.ivImage);
//        ivImage.setImageBitmap(bm);
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
        UPD_METHOD = "/updateUserImage";
        if (Build.VERSION.SDK_INT >= 11) {
            new UploadImageService(0,CURRENT_UPLOAD_ROUTE_COUNT,encodedImage).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,AppConfig.apiUrl);
        } else {
            new UploadImageService(0,CURRENT_UPLOAD_ROUTE_COUNT,encodedImage).execute(AppConfig.apiUrl);
        }

    }
    private void onCaptureImageResult(Bitmap thumbnail)  {
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
        if(userChoosenTask.equals("TP_ROUTE")){
            HashMap<String,String> hashRoutePic = new HashMap<String,String>();
            hashRoutePic.put("route_img_id","");
            hashRoutePic.put("route_img",encodedImage);
            allImageRouteAdd.add(hashRoutePic);
            ImageButton imgRoutePic = (ImageButton)findViewById(R.id.btnAddPic);
            imgRoutePic.setImageBitmap(thumbnail);
//            GridView grid1 = (GridView)findViewById(R.id.gridRouteImgPreview);
//            grid1.setAdapter(new RouteImagePreviewAdapter(this,allImageRouteAdd));
//            MainUtility mUtility = new MainUtility();
//            mUtility.setGridViewHeightBasedOnChildren(grid1,3);
        }
        else if(userChoosenTask.equals("TP_ROUTE_EDIT")){
            HashMap<String,String> hashRoutePic = new HashMap<String,String>();
            hashRoutePic.put("route_img_id","");
            hashRoutePic.put("route_img",encodedImage);
            allImageRouteEdit.add(hashRoutePic);
            ImageButton imgRoutePic = (ImageButton)findViewById(R.id.btnAddPicEdit);
            imgRoutePic.setImageBitmap(thumbnail);
//            GridView grid1 = (GridView)findViewById(R.id.gridRouteImgPreview);
//            grid1.setAdapter(new RouteImagePreviewAdapter(this,allImageRouteAdd));
//            MainUtility mUtility = new MainUtility();
//            mUtility.setGridViewHeightBasedOnChildren(grid1,3);
        }
        else if(userChoosenTask.equals("TP_PIN")){
            HashMap<String,String> hashRoutePic = new HashMap<String,String>();
            hashRoutePic.put("place_img_id","");
            hashRoutePic.put("place_img",encodedImage);
            allImagePinsAdd.add(hashRoutePic);
            ImageButton imgPinPic = (ImageButton)findViewById(R.id.btnAddPinImage);
            imgPinPic.setImageBitmap(thumbnail);
//            GridView grid1 = (GridView)findViewById(R.id.gridPlaceImgPreview);
//            grid1.setAdapter(new RouteImagePreviewAdapter(this,allImagePinsAdd));
//            MainUtility mUtility = new MainUtility();
//            mUtility.setGridViewHeightBasedOnChildren(grid1,3);
        }
        else if(userChoosenTask.equals("TP_PIN_EDIT")){
            HashMap<String,String> hashRoutePic = new HashMap<String,String>();
            hashRoutePic.put("place_img_id","");
            hashRoutePic.put("place_img",encodedImage);
            allImagePinsEdit.add(hashRoutePic);
            ImageButton imgPinPic = (ImageButton)findViewById(R.id.btnEditPinImage);
            imgPinPic.setImageBitmap(thumbnail);
//            GridView grid1 = (GridView)findViewById(R.id.gridPlaceImgPreview);
//            grid1.setAdapter(new RouteImagePreviewAdapter(this,allImagePinsAdd));
//            MainUtility mUtility = new MainUtility();
//            mUtility.setGridViewHeightBasedOnChildren(grid1,3);
        }
//        ImageView ivImage = (ImageView)findViewById(R.id.ivImage);
//        ivImage.setImageBitmap(thumbnail);
    }
    public static String getRealPathFromURI(Context context, Uri contentUri) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return getPathForV19AndUp(context, contentUri);
        } else {
            return getPathForPreV19(context, contentUri);
        }
    }

    /**
     * Handles pre V19 uri's
     * @param context
     * @param contentUri
     * @return
     */
    public static String getPathForPreV19(Context context, Uri contentUri) {
        String res = null;

        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        if(cursor.moveToFirst()){;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();

        return res;
    }

    /**
     * Handles V19 and up uri's
     * @param context
     * @param contentUri
     * @return path
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPathForV19AndUp(Context context, Uri contentUri) {
        String wholeID = DocumentsContract.getDocumentId(contentUri);

        // Split at colon, use second item in the array
        String id = wholeID.split(":")[1];
        String[] column = { MediaStore.Images.Media.DATA };

        // where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";
        Cursor cursor = context.getContentResolver().
                query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        column, sel, new String[]{ id }, null);

        String filePath = "";
        int columnIndex = cursor.getColumnIndex(column[0]);
        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }

        cursor.close();
        return filePath;
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
                FileUtils.createDefaultFolder(HomeActivity.this);
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
            FileUtils.createDefaultFolder(HomeActivity.this);
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
    private void loadPermissionsCameraGallery(String perm,int requestCode) {
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
                        REQUEST_PERMISSION_CAMERA_GALLERY);
                return;
            }
            else{
                FileUtils.createDefaultFolder(HomeActivity.this);
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
                startActivityForResult(chooserIntent, REQUEST_CAMERA);
            }
        }
        else{
            FileUtils.createDefaultFolder(HomeActivity.this);
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
            startActivityForResult(chooserIntent, REQUEST_CAMERA);
        }

    }
    private void loadPermissionsCamera(String perm,int requestCode) {
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
                        REQUEST_PERMISSION_CAMERA);
                return;
            }
            else{
                FileUtils.createDefaultFolder(HomeActivity.this);
                final File file = FileUtils.createFile(FileUtils.IMAGE_FILE);
                outputFileUri = Uri.fromFile(file);

                // Camera.
                final Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

//                final Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                galleryIntent.setType("image/*");
//                //Filesystems
//                galleryIntent.setAction(Intent.ACTION_GET_CONTENT); // To allow file managers or any other app that are not gallery app.

                //final Intent chooserIntent = Intent.createChooser(galleryIntent, "Select Image");

                final Intent chooserIntent = Intent.createChooser(captureIntent, "Select Image");
                // Add the gallery options.
                //chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] { captureIntent });
                startActivityForResult(captureIntent, REQUEST_CAMERA);
            }
        }
        else{
            FileUtils.createDefaultFolder(HomeActivity.this);
            final File file = FileUtils.createFile(FileUtils.IMAGE_FILE);
            outputFileUri = Uri.fromFile(file);

            // Camera.
            final Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

            //final Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            //galleryIntent.setType("image/*");
            // Filesystems
            // galleryIntent.setAction(Intent.ACTION_GET_CONTENT); // To allow file managers or any other app that are not gallery app.

            //final Intent chooserIntent = Intent.createChooser(galleryIntent, "Select Image");

            final Intent chooserIntent = Intent.createChooser(captureIntent, "Select Image");
            // Add the camera options.
            //chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] { captureIntent });
            startActivityForResult(captureIntent, REQUEST_CAMERA);
        }

    }
    private void loadPermissions(String perm,int requestCode) {
        ismapZoomFirst = false;
        mapType = 1;
        if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, perm)) {
                ActivityCompat.requestPermissions(this, new String[]{perm},requestCode);
            }
        }
        else{
            WorkaroundMapFragment mapFragment = (WorkaroundMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(HomeActivity.this);
            final ScrollView mScrollView = (ScrollView) findViewById(R.id.scrollCreatePin); //parent scrollview in xml, give your scrollview id value

            ((WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .setListener(new WorkaroundMapFragment.OnTouchListener() {
                        @Override
                        public void onTouch() {
                            mScrollView.requestDisallowInterceptTouchEvent(true);
                        }
                    });
        }
    }
    private void loadPermissionsMapEdit(String perm,int requestCode) {
        mapType = 2;
        if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, perm)) {
                ActivityCompat.requestPermissions(this, new String[]{perm},requestCode);
            }
        }
        else{
            WorkaroundMapFragment mapFragment = (WorkaroundMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.mapEdit);
            mapFragment.getMapAsync(HomeActivity.this);
            final ScrollView mScrollView = (ScrollView) findViewById(R.id.scrollEditPin); //parent scrollview in xml, give your scrollview id value

            ((WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapEdit))
                    .setListener(new WorkaroundMapFragment.OnTouchListener() {
                        @Override
                        public void onTouch() {
                            mScrollView.requestDisallowInterceptTouchEvent(true);
                        }
                    });
        }
    }
    private void loadPermissionsMapViewAllPins(String perm,int requestCode) {
        ismapZoomFirst = true;
        mapType = 3;
        if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, perm)) {
                ActivityCompat.requestPermissions(this, new String[]{perm},requestCode);
            }
        }
        else{
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.mapViewAllPins);
            mapFragment.getMapAsync(HomeActivity.this);
//            final ScrollView mScrollView = (ScrollView) findViewById(R.id.scrollEditPin); //parent scrollview in xml, give your scrollview id value
//
//            ((WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapEdit))
//                    .setListener(new WorkaroundMapFragment.OnTouchListener() {
//                        @Override
//                        public void onTouch() {
//                            mScrollView.requestDisallowInterceptTouchEvent(true);
//                        }
//                    });
        }
    }
    private void loadPermissionsMapFeedViewAllPins(String perm,int requestCode) {
        ismapZoomFirst = true;
        mapType = 4;
        if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, perm)) {
                ActivityCompat.requestPermissions(this, new String[]{perm},requestCode);
            }
        }
        else{
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.mapFeedViewAllPins);
            mapFragment.getMapAsync(HomeActivity.this);
//            final ScrollView mScrollView = (ScrollView) findViewById(R.id.scrollEditPin); //parent scrollview in xml, give your scrollview id value
//
//            ((WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapEdit))
//                    .setListener(new WorkaroundMapFragment.OnTouchListener() {
//                        @Override
//                        public void onTouch() {
//                            mScrollView.requestDisallowInterceptTouchEvent(true);
//                        }
//                    });
        }
    }
    private void loadPermissionsMapUserViewAllPins(String perm,int requestCode) {
        ismapZoomFirst = true;
        mapType = 5;
        if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, perm)) {
                ActivityCompat.requestPermissions(this, new String[]{perm},requestCode);
            }
        }
        else{
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.mapUserViewAllPins);
            mapFragment.getMapAsync(HomeActivity.this);
//            final ScrollView mScrollView = (ScrollView) findViewById(R.id.scrollEditPin); //parent scrollview in xml, give your scrollview id value
//
//            ((WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapEdit))
//                    .setListener(new WorkaroundMapFragment.OnTouchListener() {
//                        @Override
//                        public void onTouch() {
//                            mScrollView.requestDisallowInterceptTouchEvent(true);
//                        }
//                    });
        }
    }
    private void loadPermissionsMapExploreViewAllPins(String perm,int requestCode) {
        ismapZoomFirst = true;
        mapType = 6;
        if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, perm)) {
                ActivityCompat.requestPermissions(this, new String[]{perm},requestCode);
            }
        }
        else{
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.mapExploreViewAllPins);
            mapFragment.getMapAsync(HomeActivity.this);
//            final ScrollView mScrollView = (ScrollView) findViewById(R.id.scrollEditPin); //parent scrollview in xml, give your scrollview id value
//
//            ((WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapEdit))
//                    .setListener(new WorkaroundMapFragment.OnTouchListener() {
//                        @Override
//                        public void onTouch() {
//                            mScrollView.requestDisallowInterceptTouchEvent(true);
//                        }
//                    });
        }
    }
    private void loadPermissionsMapHomeViewPin(String perm,int requestCode) {
        ismapZoomFirst = true;
        mapType = 7;
        if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, perm)) {
                ActivityCompat.requestPermissions(this, new String[]{perm},requestCode);
            }
        }
        else{
            WorkaroundMapFragment mapFragment = (WorkaroundMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.mapHomeViewPin);
            mapFragment.getMapAsync(HomeActivity.this);
            final ScrollView mScrollView = (ScrollView) findViewById(R.id.scrollHomePinPreview); //parent scrollview in xml, give your scrollview id value

            ((WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapHomeViewPin))
                    .setListener(new WorkaroundMapFragment.OnTouchListener() {
                        @Override
                        public void onTouch() {
                            mScrollView.requestDisallowInterceptTouchEvent(true);
                        }
                    });
        }
    }
    private void loadPermissionsMapExploreViewPin(String perm,int requestCode) {
        ismapZoomFirst = true;
        mapType = 8;
        if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, perm)) {
                ActivityCompat.requestPermissions(this, new String[]{perm},requestCode);
            }
        }
        else{
            WorkaroundMapFragment mapFragment = (WorkaroundMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.mapExploreViewPin);
            mapFragment.getMapAsync(HomeActivity.this);
            final ScrollView mScrollView = (ScrollView) findViewById(R.id.scrollExplorePinPreview); //parent scrollview in xml, give your scrollview id value

            ((WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapExploreViewPin))
                    .setListener(new WorkaroundMapFragment.OnTouchListener() {
                        @Override
                        public void onTouch() {
                            mScrollView.requestDisallowInterceptTouchEvent(true);
                        }
                    });
        }
    }
    private void loadPermissionsMapUserViewPin(String perm,int requestCode) {
        ismapZoomFirst = true;
        mapType = 9;
        if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, perm)) {
                ActivityCompat.requestPermissions(this, new String[]{perm},requestCode);
            }
        }
        else{
            WorkaroundMapFragment mapFragment = (WorkaroundMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.mapUserViewPin);
            mapFragment.getMapAsync(HomeActivity.this);
            final ScrollView mScrollView = (ScrollView) findViewById(R.id.scrollUserPinPreview); //parent scrollview in xml, give your scrollview id value

            ((WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapUserViewPin))
                    .setListener(new WorkaroundMapFragment.OnTouchListener() {
                        @Override
                        public void onTouch() {
                            mScrollView.requestDisallowInterceptTouchEvent(true);
                        }
                    });
        }
    }
    private int getIndex(Spinner spinner, String myString){

        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).equals(myString)){
                index = i;
            }
        }
        return index;
    }
    public String GetFeedTimeText(String timeMinutes){
        String timetext = "";
        int minute = Integer.parseInt(timeMinutes);
        if(minute<1){
            timetext= "just now";
        }
        else if(minute<60){
            timetext= String.valueOf(minute)+" mins ago";
        }else if(minute < 1440){
            int hours = (int)minute/60;
            timetext = String.valueOf(hours)+ " hours ago";
        }
        else if(minute<43200){
            int day = (int)minute/1440;
            timetext = String.valueOf(day)+ " days ago";
        }
        else if(minute<518400){
            int month = (int)minute/43200;
            timetext = String.valueOf(month)+ " months ago";
        }
        return timetext;
    }
    public  void ClearAddRouteValue(){
        EditText txtTitle = (EditText) findViewById(R.id.txtTitle);
        EditText txtRouteDesc = (EditText) findViewById(R.id.txtRouteDesc);
        Spinner txtActivity = (Spinner)findViewById(R.id.txtActivity);
//                                EditText txtCountryEdit = (EditText)findViewById(R.id.txtCountryEdit);
        EditText txtCity = (EditText)findViewById(R.id.txtCity);
        Spinner txtTravel = (Spinner)findViewById(R.id.txtTravelMethod);
//                                EditText txtTimeFrameEdit = (EditText)findViewById(R.id.txtTimeframeEdit);
        EditText txtBudgetMin = (EditText)findViewById(R.id.txtBudgetMin);
        EditText txtBudgetMax = (EditText)findViewById(R.id.txtBudgetMax);
        EditText txtSuggestion = (EditText)findViewById(R.id.txtSuggestion);
        txtTitle.requestFocus();
        txtTitle.setText("");
        txtRouteDesc.setText("");
        txtCity.setText("");
        txtBudgetMin.setText("");
        txtBudgetMax.setText("");
        txtSuggestion.setText("");
        txtActivity.setSelection(0);
        txtTravel.setSelection(0);
        LinearLayout layoutAddRouteDetail = (LinearLayout)findViewById(R.id.layoutAddRouteDetail);
        layoutAddRouteDetail.setVisibility(GONE);
        ImageView imgAddRouteCollapse = (ImageView) findViewById(R.id.imgAddRouteCollapse);
        imgAddRouteCollapse.setImageResource(R.drawable.arrow_down);
        allImageRouteAdd = new ArrayList<HashMap<String, String>>();
        ImageButton btnAddPic = (ImageButton)findViewById(R.id.btnAddPic);
        btnAddPic.setImageResource(R.drawable.cover_img_plus);
        final GridView grid = (GridView) findViewById(R.id.gridRouteImgPreview);
        grid.setAdapter(null);
        ViewGroup.LayoutParams params = grid.getLayoutParams();
        params.height = 0;
        grid.setLayoutParams(params);
        ScrollView scCreateRoute = (ScrollView)findViewById(R.id.scrollCreateRoute);
        scCreateRoute.smoothScrollTo(0,0);
    }
    public  void ClearAddPinValue() {
        EditText txtPinname = (EditText)findViewById(R.id.txtPinname);
        EditText txtPindesc = (EditText)findViewById(R.id.txtPinDesc);
        txtPinname.requestFocus();
        txtPinname.setText("");
        txtPindesc.setText("");
        ScrollView scCreatePin = (ScrollView)findViewById(R.id.scrollCreatePin);
        scCreatePin.smoothScrollTo(0,0);
        isSavePopup = false;
    }
    public void ClearAddPinMap(){
        mMap.clear();
        allPinsAdd = new ArrayList<HashMap<String, String>>();
        ismapZoomFirst=false;
        updateMapMarker();
    }
    public  void ClearAddPinView(){
        TextView lblPinHead  = (TextView)findViewById(R.id.lblCreatePinHead);
        TextView lblPinname  = (TextView)findViewById(R.id.lblPinname);
        TextView lblPindesc = (TextView)findViewById(R.id.lblPindesc);
        EditText txtPinname = (EditText)findViewById(R.id.txtPinname);
        EditText txtPindesc = (EditText)findViewById(R.id.txtPinDesc);
        ImageButton btnAddPic = (ImageButton)findViewById(R.id.btnAddPinImage);
        GridView gridPreview = (GridView) findViewById(R.id.gridPlaceImgPreview);
        final ImageButton btnAddPin = (ImageButton) findViewById(R.id.btnPointPin);
        Button btnSavepin = (Button)findViewById(R.id.btnInnerSavepin);
        lblPinname.setVisibility(GONE);
        lblPindesc.setVisibility(GONE);
        txtPinname.setVisibility(GONE);
        txtPindesc.setVisibility(GONE);
        btnSavepin.setVisibility(GONE);
        btnAddPic.setVisibility(GONE);
        gridPreview.setVisibility(GONE);
        lblPinHead.setText("Create Route");
        btnAddPin.setImageResource(R.drawable.ic_pin);
        LinearLayout layoutBtnPin = (LinearLayout)findViewById(R.id.layoutBtnPin);
        layoutBtnPin.setVisibility(View.VISIBLE);
        ImageButton btnFinishPin = (ImageButton)findViewById(R.id.btnFinishPin);
        btnFinishPin.setImageResource(R.drawable.ic_finish_pin);
        RelativeLayout layoutFinish = (RelativeLayout)findViewById(R.id.layoutFinishAddPin);
        layoutFinish.setVisibility(GONE);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        LinearLayout layoutMap = (LinearLayout)findViewById(R.id.layoutMap);
        layoutMap.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height-350));
        ScrollView scpin = (ScrollView)findViewById(R.id.scrollCreatePin);
        scpin.scrollTo(0,0);
        isCreatePin = false;
    }
    public void doLoadViewPin(){
        mMap.clear();
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(LATLNG_PIN_PREVIEW).zoom(13).build();

        mMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));
        MarkerOptions pinOptions = new MarkerOptions();
        pinOptions.position(LATLNG_PIN_PREVIEW);
        pinOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        mMap.addMarker(pinOptions);
    }
    public void doLoadViewAllPins(){
        mMap.clear();
        int allsize = allPinsListEdit.size();
        if(allsize>0){
            for (int i = 0; i < allsize; i++) {
                HashMap<String,String> pins = allPinsListEdit.get(i);
                LatLng pinLat = new LatLng(Double.parseDouble(pins.get("place_latitude")),Double.parseDouble(pins.get("place_longitude")));
                if(i==0){
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(pinLat).zoom(13).build();

                    mMap.animateCamera(CameraUpdateFactory
                            .newCameraPosition(cameraPosition));
                }
                MarkerOptions pinOptions = new MarkerOptions();
                pinOptions.position(pinLat);
                pinOptions.title(pins.get("title"));
                pinOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                mMap.addMarker(pinOptions);
            }
            if(allsize>1){
                String waypoints="";
                Double startLat = 0.0;
                Double startLng = 0.0;
                Double endLat = 0.0;
                Double endLng = 0.0;
                int wpCount = 0;
                for (int i = 0; i < allsize; i++) {
                    HashMap<String,String> pins = allPinsListEdit.get(i);
                    if(i==0){
                        startLat = Double.parseDouble(pins.get("place_latitude"));
                        startLng = Double.parseDouble(pins.get("place_longitude"));
                    }

                    if(i>0&&i<(allsize-1)){
                        if(wpCount!=0){
                            waypoints+="%7C";
                        }
                        waypoints+="via:"+pins.get("place_latitude")+"%2C"+pins.get("place_longitude");

                        wpCount++;
                    }

                    if(i==(allsize-1)){
                        endLat = Double.parseDouble(pins.get("place_latitude"));
                        endLng = Double.parseDouble(pins.get("place_longitude"));
                    }
                }
                String url = makeURL(startLat,startLng,endLat,endLng,waypoints);
                Log.d("DEBUG DIRECTION URL",url);
                new HomeActivity.ConnectDirectionAsyncTask(url).execute();
            }
        }
    }
    public  void updateMapMarker(){
        int allsize = allPinsAdd.size();
        mMap.clear();
//        MarkerOptions markerOptions = new MarkerOptions();
//        markerOptions.position(latLng);
//        markerOptions.title("Your Location");
//        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
//        currLocationMarker = mMap.addMarker(markerOptions);
        if(allsize>0){
            for (int i = 0; i < allsize; i++) {
                HashMap<String,String> pins = allPinsAdd.get(i);
                LatLng pinLat = new LatLng(Double.parseDouble(pins.get("place_latitude")),Double.parseDouble(pins.get("place_longitude")));
                MarkerOptions pinOptions = new MarkerOptions();
                pinOptions.position(pinLat);
                pinOptions.title(pins.get("place_name"));
                pinOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                mMap.addMarker(pinOptions);
            }
            if(allsize>1){
                String waypoints="";
                Double startLat = 0.0;
                Double startLng = 0.0;
                Double endLat = 0.0;
                Double endLng = 0.0;
                int wpCount = 0;
                for (int i = 0; i < allsize; i++) {
                    HashMap<String,String> pins = allPinsAdd.get(i);
                    if(i==0){
                        startLat = Double.parseDouble(pins.get("place_latitude"));
                        startLng = Double.parseDouble(pins.get("place_longitude"));
                    }

                    if(i>0&&i<(allsize-1)){
                        if(wpCount!=0){
                            waypoints+="%7C";
                        }
                        waypoints+="via:"+pins.get("place_latitude")+"%2C"+pins.get("place_longitude");

                        wpCount++;
                    }

                    if(i==(allsize-1)){
                        endLat = Double.parseDouble(pins.get("place_latitude"));
                        endLng = Double.parseDouble(pins.get("place_longitude"));
                    }
                }
                String url = makeURL(startLat,startLng,endLat,endLng,waypoints);
                Log.d("DEBUG DIRECTION URL",url);
                new HomeActivity.ConnectDirectionAsyncTask(url).execute();
            }
            latLng = mMap.getCameraPosition().target;
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Current Position");
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            currLocationMarker = mMap.addMarker(markerOptions);
        }
    }
    public  void updateMapMarkerEdit(){
        int allsize = allPinsEdit.size();
        mMap.clear();
//        MarkerOptions markerOptions = new MarkerOptions();
//        markerOptions.position(latLng);
//        markerOptions.title("Your Location");
//        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
//        currLocationMarker = mMap.addMarker(markerOptions);
        if(allsize>0){
            for (int i = 0; i < allsize; i++) {
                HashMap<String,String> pins = allPinsEdit.get(i);
                LatLng pinLat = new LatLng(Double.parseDouble(pins.get("place_latitude")),Double.parseDouble(pins.get("place_longitude")));
                latLng = pinLat;
                MarkerOptions pinOptions = new MarkerOptions();
                pinOptions.position(pinLat);
                pinOptions.title(pins.get("place_name"));
                pinOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                mMap.addMarker(pinOptions);
                pinOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                currLocationMarker = mMap.addMarker(pinOptions);
            }

        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            //All location services are disabled
            latLng = new LatLng(15.8700, 100.9925);
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLng).zoom(6).build();

            mMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));
            AlertDialog alertDialog = new AlertDialog.Builder(HomeActivity.this).create();
            alertDialog.setTitle("Warning");
            alertDialog.setMessage("Please open GPS to mark location.");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            // Show rationale and request permission.
        }
        if(mapType==1||mapType==2){
            mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
                @Override
                public void onCameraMove() {
                    LatLng center = mMap.getCameraPosition().target;
    //                Log.d("DEBUG","lat = "+String.valueOf(center.latitude));
    //                Log.d("DEBUG","long = "+String.valueOf(center.longitude));
    //                MarkerOptions markerOptions = new MarkerOptions();
    //                markerOptions.position(center);
    //                markerOptions.title("Current Position");
    //                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                    if(currLocationMarker==null){
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(latLng);
                        markerOptions.title("Current Position");
                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                        currLocationMarker = mMap.addMarker(markerOptions);
                    }
                        if(flagFinishMove){
                            if(mapType==1){
                                currLocationMarker.setPosition(center);
                                latLng = center;
                            }
                            else if(mapType==2){
                                currLocationMarker.setPosition(center);
                                latLng = center;
                            }

                        }

                    }
                });
                mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                    @Override
                    public void onCameraIdle() {
                        if(!flagFinishMove){
                            flagFinishMove = true;
                        }
                    }
                });
        }
        if(mapType==3||mapType==4||mapType==5||mapType==6){
            doLoadViewAllPins();
        }
        else if(mapType==7||mapType==8||mapType==9) {
            doLoadViewPin();
        }
        buildGoogleApiClient();

        mGoogleApiClient.connect();
        // Add a marker in Sydney, Australia, and move the camera.
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
    protected synchronized void buildGoogleApiClient() {
        //Toast.makeText(this,"buildGoogleApiClient",Toast.LENGTH_SHORT).show();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        //Toast.makeText(this,"onConnected",Toast.LENGTH_SHORT).show();
        Location mLastLocation = new Location("");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
           mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
        }

        if (mLastLocation != null) {
            //place marker at current position
            //mGoogleMap.clear();
            currentLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            Log.d("Location","lat = "+String.valueOf(mLastLocation.getLatitude())+",lng = "+mLastLocation.getLatitude());
            if(mMap!=null){
                if(currLocationMarker!=null){
                    currLocationMarker.remove();
                }
                if(mapType==1){ //ADD ROUTE PIN
                    latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    markerOptions.title("Current Position");
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    currLocationMarker = mMap.addMarker(markerOptions);
                }else if(mapType==2){ //EDIT ROUTE PIN
                    if(allPinsEdit.size()>0){
                        HashMap<String,String> hmPin = allPinsEdit.get(0);
                        LatLng pinLatlng = new LatLng(Double.parseDouble(hmPin.get("place_latitude")),Double.parseDouble(hmPin.get("place_longitude")));
                        CameraPosition cameraPosition1 = new CameraPosition.Builder()
                                .target(pinLatlng).zoom(12).build();

                        mMap.animateCamera(CameraUpdateFactory
                                .newCameraPosition(cameraPosition1));
                        updateMapMarkerEdit();
                        flagFinishMove = false;
                    }
                    else{
                        latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(latLng);
                        markerOptions.title("Current Position");
                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                        currLocationMarker = mMap.addMarker(markerOptions);
                    }

                }
            }
        }

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000); //5 seconds
        mLocationRequest.setFastestInterval(3000); //3 seconds
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        //mLocationRequest.setSmallestDisplacement(0.1F); //1/10 meter

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);


    }

    @Override
    public void onConnectionSuspended(int i) {
        //Toast.makeText(this,"onConnectionSuspended",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this,"onConnectionFailed",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
        Log.d("Location","On Location Changed");
        //place marker at current position
        //mMap.clear();
//        if (currLocationMarker != null) {
//            currLocationMarker.remove();
//        }

//        MarkerOptions markerOptions = new MarkerOptions();
//        markerOptions.position(latLng);
//        markerOptions.title("Your Location");
//        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
//        currLocationMarker = mMap.addMarker(markerOptions);
        //updateMapMarker();
        //Toast.makeText(this,"Location Changed",Toast.LENGTH_SHORT).show();

        //zoom to current position:
        if(!ismapZoomFirst){
            if(mMap!=null){
                latLng = new LatLng(location.getLatitude(), location.getLongitude());
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(latLng).zoom(12).build();

                mMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(cameraPosition));
                flagFinishMove = false;
                ismapZoomFirst=true;
                if(currLocationMarker!=null){
                    currLocationMarker.remove();
                }
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("Current Position");
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                currLocationMarker = mMap.addMarker(markerOptions);
            }
        }
        else{

        }
        //If you only need one location, unregister the listener
        //LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);

    }

    @Override
    public void onBackPressed()
    {
        // super.onBackPressed(); // Comment this super call to avoid calling finish()
    }
    public  float lastX=0;
    public  void setTab(){
        mTabHost = (TabHost) findViewById(R.id.tabHost);
        mTabHost.setup();

        //Tab 1
        TabHost.TabSpec spec = mTabHost.newTabSpec(TAB1_TAG);
        spec.setContent(R.id.tab1);
        Drawable dr1 = ResourcesCompat.getDrawable(getResources(), R.drawable.home_tab1, null);
        spec.setIndicator("", dr1);
        mTabHost.addTab(spec);

        //Tab 2
        spec = mTabHost.newTabSpec(TAB2_TAG);
        spec.setContent(R.id.tab2);
        Drawable dr2 = ResourcesCompat.getDrawable(getResources(), R.drawable.home_tab2, null);
        spec.setIndicator("", dr2);
        mTabHost.addTab(spec);

        //Tab 3
        spec = mTabHost.newTabSpec(TAB3_TAG);
        spec.setContent(R.id.tab3);
        Drawable dr3 = ResourcesCompat.getDrawable(getResources(), R.drawable.home_tab3, null);
        spec.setIndicator("", dr3);
        mTabHost.addTab(spec);

        //Tab 4
        spec = mTabHost.newTabSpec(TAB4_TAG);
        spec.setContent(R.id.tab4);
        Drawable dr4 = ResourcesCompat.getDrawable(getResources(), R.drawable.home_tab4, null);
        spec.setIndicator("", dr4);
        mTabHost.addTab(spec);

        //Tab 5
        spec = mTabHost.newTabSpec(TAB5_TAG);
        spec.setContent(R.id.tab5);
        Drawable dr5 = ResourcesCompat.getDrawable(getResources(), R.drawable.home_tab5, null);
        spec.setIndicator("", dr5);
        mTabHost.addTab(spec);
        mTabHost.setCurrentTab(0);
        mTabHost.setOnTabChangedListener(new AnimatedTabHostListener(HomeActivity.this,mTabHost){
//            @Override
//            public void onTabChanged(String tabId) {
//                if(TAB1_TAG.equals(tabId)){
//                    doLoadDataHome();
//                }
//                if(TAB2_TAG.equals(tabId)){
//                    if(!TAB2_LOADED){
//                        //Init Load Explore
//                        Log.d("TAB_2","INIT LOAD");
//                        doLoadDataExplore();
//                        Log.d("TAB_2","End LOAD");
//                        TAB2_LOADED=true;
//                    }
//                }
//                if(TAB3_TAG.equals(tabId)){
//                    if(!TAB3_LOADED){
//                        //Init Load Route
//                        setPinTab();
//                        TAB3_LOADED=true;
//                    }
//                }
//                if(TAB4_TAG.equals(tabId)){
//                    if(!TAB4_LOADED){
//                        //Init Load Notification
//                        setNotisTab();
//                        doLoadNotisFollowing();
//                        TAB4_LOADED=true;
//                    }
//                }
//                if(TAB5_TAG.equals(tabId)){
//                    if(!TAB5_LOADED){
//                        //Init Load Notification
//                        doLoadUserNow();
//                        TAB5_LOADED=true;
//                    }
//                }
//            }
        });
    }

    /* Home Tab Method*/
    public  void doLoadDataHome(){
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        METHOD="/listRoutesFeed";
                        new HomeActivity.CallService().execute(AppConfig.apiUrl);
                    }
                }, 500);

    }
    /* Home Tab Method*/

    /* Explore Tab Method */
    public  void doLoadDataExplore(){
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        METHOD="/listRoutesFeedExplore";
                        new HomeActivity.CallService().execute(AppConfig.apiUrl);
                    }
                }, 500);


    }
    /* Explore Tab Method */

    /* Pin Tab Method */
    public  void doLoadPinDraft(){
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        METHOD="/listRoutesFeedByUserDraft";
                        new HomeActivity.CallService().execute(AppConfig.apiUrl);
                    }
                }, 500);
    }
    public  void setPinTab(){
        final TabHost host = (TabHost) findViewById(R.id.tabHostSub);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec(PIN_TAB1_TAG);

        spec.setContent(R.id.pintab1);
        //Drawable dr1 = ResourcesCompat.getDrawable(getResources(), R.drawable.home_tab1, null);
        spec.setIndicator(PIN_TAB1_TAG);
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec(PIN_TAB2_TAG);
        spec.setContent(R.id.pintab2);
        //Drawable dr2 = ResourcesCompat.getDrawable(getResources(), R.drawable.home_tab2, null);
        spec.setIndicator(PIN_TAB2_TAG);
        host.addTab(spec);
        host.setCurrentTab(0);
        final TextView tv =  (TextView) host.getTabWidget().getChildAt(0).findViewById(android.R.id.title);
        tv.setTextColor(Color.parseColor("#51b7dc"));
        tv.setAllCaps(false);
        final TextView tv1 =  (TextView) host.getTabWidget().getChildAt(1).findViewById(android.R.id.title);
        tv1.setTextColor(Color.parseColor("#878787"));
        tv1.setAllCaps(false);
        host.setOnTabChangedListener(new TabHost.OnTabChangeListener(){
            @Override
            public void onTabChanged(String tabId) {
                tv.setTextColor(Color.parseColor("#878787"));
                tv1.setTextColor(Color.parseColor("#878787"));
                int tab = host.getCurrentTab();
                Log.d("DEBUG","Current Tab = "+String.valueOf(tab));
                if(tab==0){
                    tv.setTextColor(Color.parseColor("#51b7dc"));
                }
                else if(tab==1){
                    tv1.setTextColor(Color.parseColor("#51b7dc"));
                }
                if(PIN_TAB2_TAG.equals(tabId)){
                    if(!PINTAB2_LOADED){
                        //Init Load Route
                        doLoadPinDraft();
                        PINTAB2_LOADED=true;
                    }
                }
            }});

    }
    /* Pin Tab Method */

    /* Notification Tab Method */
    public  void doLoadNotisFollowing(){

//        NOTISTAB1_LOADED=true;
        METHOD="/listNotificationFollowing";
        new HomeActivity.CallService().execute(AppConfig.apiUrl);
    }
    public  void doLoadNotisYou(){
        METHOD="/listNotificationYou";
        new HomeActivity.CallService().execute(AppConfig.apiUrl);

    }
    public  void setNotisTab(){
        TabHost host = (TabHost) findViewById(R.id.tabHostSubNoti);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec(NOTIS_TAB1_TAG);
        spec.setContent(R.id.notistab1);
        //Drawable dr1 = ResourcesCompat.getDrawable(getResources(), R.drawable.home_tab1, null);
        spec.setIndicator(NOTIS_TAB1_TAG);
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec(NOTIS_TAB2_TAG);
        spec.setContent(R.id.notistab2);
        //Drawable dr2 = ResourcesCompat.getDrawable(getResources(), R.drawable.home_tab2, null);
        spec.setIndicator(NOTIS_TAB2_TAG);
        host.addTab(spec);
        host.setCurrentTab(0);

        host.setOnTabChangedListener(new TabHost.OnTabChangeListener(){
            @Override
            public void onTabChanged(String tabId) {
                if(NOTIS_TAB2_TAG.equals(tabId)){
                    if(!NOTISTAB1_LOADED){
                        //Init Load Route
                        doLoadNotisFollowing();
                        //NOTISTAB1_LOADED=true;
                    }
                    if(!NOTISTAB2_LOADED){
                        //Init Load Route
                        doLoadNotisYou();
                        //NOTISTAB2_LOADED=true;
                    }
                }
            }});
    }
    /* Notification Tab Method */

    /* User Tab Method */
    public void doLoadUserNow(){
        TextView lblFullName = (TextView)findViewById(R.id.lblUserFullname);
        lblFullName.setText(USER_NAME);
        doLoadCurrentUserProfile();
        //doLoadDataUserGrid();
        ImageButton btnList = (ImageButton) findViewById(R.id.btnViewList);
        ImageButton btnGrid = (ImageButton)findViewById(R.id.btnViewGrid);
        ImageButton btnViewLike = (ImageButton) findViewById(R.id.btnViewLike);
        ImageButton btnViewFavorite = (ImageButton) findViewById(R.id.btnViewFavorite);
        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_item));
               GridView gridUser = (GridView)findViewById(R.id.gridUser);
                ListView listUser = (ListView)findViewById(R.id.listUser);
                gridUser.setVisibility(GONE);
                listUser.setVisibility(View.VISIBLE);
                doLoadDataUserList();
            }
        });

        btnGrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_item));
                GridView gridUser = (GridView)findViewById(R.id.gridUser);
                ListView listUser = (ListView)findViewById(R.id.listUser);
                listUser.setVisibility(GONE);
                gridUser.setVisibility(View.VISIBLE);
                doLoadDataUserGrid();
            }
        });
        btnViewLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_item));
                GridView gridUser = (GridView)findViewById(R.id.gridUser);
                ListView listUser = (ListView)findViewById(R.id.listUser);
                gridUser.setVisibility(GONE);
                listUser.setVisibility(View.VISIBLE);
                doLoadDataUserListLike();
            }
        });
        btnViewFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_item));
                GridView gridUser = (GridView)findViewById(R.id.gridUser);
                ListView listUser = (ListView)findViewById(R.id.listUser);
                gridUser.setVisibility(GONE);
                listUser.setVisibility(View.VISIBLE);
                doLoadDataUserListFavorite();
            }
        });
    }
    public void doLoadCurrentUserProfile(){
        METHOD="/usersCurrent";
        new HomeActivity.CallService().execute(AppConfig.apiUrl);
    }
    public  void doLoadDataUserGrid(){
        METHOD = "/listRoutesFeedByUserGrid";
        new HomeActivity.CallService().execute(AppConfig.apiUrl);
    }
    public  void doLoadDataFindUserGrid(){
        METHOD = "/listRoutesFeedByFindUserGrid";
        new HomeActivity.CallService().execute(AppConfig.apiUrl);
    }
    public  void doLoadDataFindUserList(){
        METHOD = "/listRoutesFeedByFindUserList";
        new HomeActivity.CallService().execute(AppConfig.apiUrl);
    }
    public  void doLoadDataUserList(){
        METHOD = "/listRoutesFeedByUserList";
        new HomeActivity.CallService().execute(AppConfig.apiUrl);
    }
    public  void doLoadDataUserListLike(){
        METHOD = "/listRoutesFeedByUserListLike";
        new HomeActivity.CallService().execute(AppConfig.apiUrl);
    }
    public  void doLoadDataUserListFavorite(){
        METHOD = "/listRoutesFeedByUserListFavorite";
        new HomeActivity.CallService().execute(AppConfig.apiUrl);
    }
    /* User Tab Method */

    /* Preview Method*/
    public  void doLoadDataPreviewOverview(){
        PVPINTAB2_LOADED=false;
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        METHOD = "/getRouteAR";
                        new HomeActivity.CallService().execute(AppConfig.apiUrl);
                    }
                }, 1000);

    }
    public  void doLoadDataPreviewUserOverview(){
        PVPINTAB2_LOADED=false;
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        METHOD = "/getRouteUser";
                        new HomeActivity.CallService().execute(AppConfig.apiUrl);
                    }
                }, 1000);
    }
    public  void doLoadDataPreviewExploreOverview(){
        PVPINTAB2_LOADED=false;
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        METHOD = "/getRouteExplore";
                        new HomeActivity.CallService().execute(AppConfig.apiUrl);
                    }
                }, 1000);
    }
    public  void doLoadDataPreviewHomeOverview(){
        PVPINTAB2_LOADED=false;
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        METHOD = "/getRouteHome";
                        new HomeActivity.CallService().execute(AppConfig.apiUrl);
                    }
                }, 1000);

    }
    public  void doLoadPreviewPins(){
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        METHOD="/getPlacesByRouteAR";
                        new HomeActivity.CallService().execute(AppConfig.apiUrl);
                    }
                }, 1000);

    }
    public  void doLoadPreviewPinsHome(){
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        METHOD="/getPlacesByRouteHome";
                        new HomeActivity.CallService().execute(AppConfig.apiUrl);
                    }
                }, 1000);
    }
    public  void doLoadPreviewPinsUser(){
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        METHOD="/getPlacesByRouteUser";
                        new HomeActivity.CallService().execute(AppConfig.apiUrl);
                    }
                }, 1000);
    }
    public  void doLoadPreviewPinsExplore(){
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        METHOD="/getPlacesByRouteExplore";
                        new HomeActivity.CallService().execute(AppConfig.apiUrl);
                    }
                }, 1000);
    }
    public  void setAddPinPreviewTab(){
        TabHost host = (TabHost) findViewById(R.id.tabHostAddPinPreview);
        host.setup();
        if(host.getTabWidget().getChildCount()==0){
            //Tab 1
            TabHost.TabSpec spec = host.newTabSpec(PREVIEW_TAB1_TAG);
            spec.setContent(R.id.addpinpreviewtab1);
            //Drawable dr1 = ResourcesCompat.getDrawable(getResources(), R.drawable.home_tab1, null);
            spec.setIndicator(PREVIEW_TAB1_TAG);
            host.addTab(spec);

            //Tab 2
            spec = host.newTabSpec(PREVIEW_TAB2_TAG);
            spec.setContent(R.id.addpinpreviewtab2);
            //Drawable dr2 = ResourcesCompat.getDrawable(getResources(), R.drawable.home_tab2, null);
            spec.setIndicator(PREVIEW_TAB2_TAG);
            host.addTab(spec);
        }
        host.setCurrentTab(0);
        host.setOnTabChangedListener(new TabHost.OnTabChangeListener(){
            @Override
            public void onTabChanged(String tabId) {
                if(PREVIEW_TAB2_TAG.equals(tabId)){
                        //Init Load Route
                        //doLoadPinDraft();
                        doLoadPreviewPins();
                }
            }});

    }
    public  void setExplorePreviewTab(){
        TabHost host = (TabHost) findViewById(R.id.tabHostExplorePreview);
        host.setup();
        if(host.getTabWidget().getChildCount()==0){
            //Tab 1
            TabHost.TabSpec spec = host.newTabSpec(PREVIEW_TAB1_TAG);
            spec.setContent(R.id.explorepreviewtab1);
            //Drawable dr1 = ResourcesCompat.getDrawable(getResources(), R.drawable.home_tab1, null);
            spec.setIndicator(PREVIEW_TAB1_TAG);
            host.addTab(spec);

            //Tab 2
            spec = host.newTabSpec(PREVIEW_TAB2_TAG);
            spec.setContent(R.id.explorepreviewtab2);
            //Drawable dr2 = ResourcesCompat.getDrawable(getResources(), R.drawable.home_tab2, null);
            spec.setIndicator(PREVIEW_TAB2_TAG);
            host.addTab(spec);
        }
        host.setCurrentTab(0);
        host.setOnTabChangedListener(new TabHost.OnTabChangeListener(){
            @Override
            public void onTabChanged(String tabId) {
                if(PREVIEW_TAB2_TAG.equals(tabId)){
                    //Init Load Route
                    //doLoadPinDraft();
                    if(!PVPINTAB2_LOADED){
                        doLoadPreviewPinsExplore();
                        PVPINTAB2_LOADED=true;
                    }

                }
                if(PREVIEW_TAB1_TAG.equals(tabId)){

                }
            }});

    }
    public void hideEditAddPin(){
        EDIT_PLACE_ID = "";
        TextView lblEditPinHead = (TextView)findViewById(R.id.lblEditPinHead);
        lblEditPinHead.setText("");
        EditText txtPinname = (EditText)findViewById(R.id.txtPinnameEdit);
        EditText txtPindesc = (EditText)findViewById(R.id.txtPinDescEdit);
        txtPinname.setText("");
        txtPindesc.setText("");
        ImageButton btnEditPicPin = (ImageButton)findViewById(R.id.btnEditPinImage);
        btnEditPicPin.setImageResource(R.drawable.cover_img_plus);
        allImagePinsEdit = new ArrayList<HashMap<String, String>>();
        allPinsEdit = new ArrayList<HashMap<String, String>>();
        mMap.clear();
        RelativeLayout layoutEditPin = (RelativeLayout)findViewById(R.id.layoutEditPin);
        layoutEditPin.setVisibility(View.GONE);
        RelativeLayout layoutPreview = (RelativeLayout)findViewById(R.id.layoutAddPinPreview);
        layoutPreview.setVisibility(View.VISIBLE);
        doLoadPreviewPins();
    }
    public  void setUserPreviewTab(){
        TabHost host = (TabHost) findViewById(R.id.tabHostUserPreview);
        host.setup();
        if(host.getTabWidget().getChildCount()==0){
            //Tab 1
            TabHost.TabSpec spec = host.newTabSpec(PREVIEW_TAB1_TAG);
            spec.setContent(R.id.userpreviewtab1);
            //Drawable dr1 = ResourcesCompat.getDrawable(getResources(), R.drawable.home_tab1, null);
            spec.setIndicator(PREVIEW_TAB1_TAG);
            host.addTab(spec);

            //Tab 2
            spec = host.newTabSpec(PREVIEW_TAB2_TAG);
            spec.setContent(R.id.userpreviewtab2);
            //Drawable dr2 = ResourcesCompat.getDrawable(getResources(), R.drawable.home_tab2, null);
            spec.setIndicator(PREVIEW_TAB2_TAG);
            host.addTab(spec);
        }
        host.setCurrentTab(0);
        host.setOnTabChangedListener(new TabHost.OnTabChangeListener(){
            @Override
            public void onTabChanged(String tabId) {
                if(PREVIEW_TAB2_TAG.equals(tabId)){
                    //Init Load Route
                    //doLoadPinDraft();
                    if(!PVPINTAB2_LOADED){
                        doLoadPreviewPinsUser();
                        PVPINTAB2_LOADED=true;
                    }

                }
                if(PREVIEW_TAB1_TAG.equals(tabId)){

                }
            }});

    }
    public  void setHomePreviewTab(){
        TabHost host = (TabHost) findViewById(R.id.tabHostHomePreview);
        host.setup();
        if(host.getTabWidget().getChildCount()==0){
            //Tab 1
            TabHost.TabSpec spec = host.newTabSpec(PREVIEW_TAB1_TAG);
            spec.setContent(R.id.homepreviewtab1);
            //Drawable dr1 = ResourcesCompat.getDrawable(getResources(), R.drawable.home_tab1, null);
            spec.setIndicator(PREVIEW_TAB1_TAG);
            host.addTab(spec);

            //Tab 2
            spec = host.newTabSpec(PREVIEW_TAB2_TAG);
            spec.setContent(R.id.homepreviewtab2);
            //Drawable dr2 = ResourcesCompat.getDrawable(getResources(), R.drawable.home_tab2, null);
            spec.setIndicator(PREVIEW_TAB2_TAG);
            host.addTab(spec);
        }
        host.setCurrentTab(0);
        host.setOnTabChangedListener(new TabHost.OnTabChangeListener(){
            @Override
            public void onTabChanged(String tabId) {
                if(PREVIEW_TAB2_TAG.equals(tabId)){
                        //Init Load Route
                        //doLoadPinDraft();
                    if(!PVPINTAB2_LOADED){
                        doLoadPreviewPinsHome();
                        PVPINTAB2_LOADED=true;
                    }

                }
                if(PREVIEW_TAB1_TAG.equals(tabId)){

                }
            }});

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
    public void doUploadRouteImage(){
        CURRENT_UPLOAD_ROUTE_COUNT = 1;
        ALL_ROUTE_UPLOAD_COUNT=0;
        UPD_METHOD = "/addImageRoute";
        if(allImageRouteAdd.size()>0){
            for(int i = 0;i<allImageRouteAdd.size();i++) {
                HashMap<String,String> hm = allImageRouteAdd.get(i);
                if(TextUtils.isEmpty(hm.get("route_img_id"))) {
                    ALL_ROUTE_UPLOAD_COUNT++;
                }
            }
            for(int i = 0;i<allImageRouteAdd.size();i++){
                final int ii = i;
                final HashMap<String,String> hm = allImageRouteAdd.get(i);
                if(TextUtils.isEmpty(hm.get("route_img_id"))){
                    if (Build.VERSION.SDK_INT >= 11) {
                        new UploadImageService(ii,CURRENT_UPLOAD_ROUTE_COUNT,hm.get("route_img")).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,AppConfig.apiUrl);
                    } else {
                        new UploadImageService(ii,CURRENT_UPLOAD_ROUTE_COUNT,hm.get("route_img")).execute(AppConfig.apiUrl);
                    }

                    CURRENT_UPLOAD_ROUTE_COUNT++;
                }

            }
        }
    }
    public void doUploadRouteImageEdit(){
        CURRENT_UPLOAD_ROUTE_COUNT = 1;
        ALL_ROUTE_UPLOAD_COUNT=0;
        UPD_METHOD = "/addImageRouteEdit";
        if(allImageRouteEdit.size()>0){
            for(int i = 0;i<allImageRouteEdit.size();i++) {
                HashMap<String,String> hm = allImageRouteEdit.get(i);
                if(TextUtils.isEmpty(hm.get("route_img_id"))) {
                    ALL_ROUTE_UPLOAD_COUNT++;
                }
            }
            for(int i = 0;i<allImageRouteEdit.size();i++){
                final int ii = i;
                final HashMap<String,String> hm = allImageRouteEdit.get(i);
                if(TextUtils.isEmpty(hm.get("route_img_id"))){
                    if (Build.VERSION.SDK_INT >= 11) {
                        new UploadImageService(ii,CURRENT_UPLOAD_ROUTE_COUNT,hm.get("route_img")).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,AppConfig.apiUrl);
                    } else {
                        new UploadImageService(ii,CURRENT_UPLOAD_ROUTE_COUNT,hm.get("route_img")).execute(AppConfig.apiUrl);
                    }

                    CURRENT_UPLOAD_ROUTE_COUNT++;
                }

            }
        }
        else{
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            METHOD="/delImageRoute";
                            new HomeActivity.CallService().execute(AppConfig.apiUrl);
                        }
                    }, 500);

        }
    }
    public void doUploadPlaceImage(){
        CURRENT_UPLOAD_PLACE_COUNT = 1;
        ALL_PLACE_UPLOAD_COUNT=0;
        UPD_METHOD = "/addImagePlace";
        if(allImagePinsAdd.size()>0){
            for(int i = 0;i<allImagePinsAdd.size();i++) {
                HashMap<String,String> hm = allImagePinsAdd.get(i);
                if(TextUtils.isEmpty(hm.get("place_img_id"))) {
                    ALL_PLACE_UPLOAD_COUNT++;
                }
            }
            for(int i = 0;i<allImagePinsAdd.size();i++){
                HashMap<String,String> hm = allImagePinsAdd.get(i);
                    if(TextUtils.isEmpty(hm.get("place_img_id"))){
                        if (Build.VERSION.SDK_INT >= 11) {
                            new UploadImageService(i,CURRENT_UPLOAD_PLACE_COUNT,hm.get("place_img")).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,AppConfig.apiUrl);
                        } else {
                            new UploadImageService(i,CURRENT_UPLOAD_PLACE_COUNT,hm.get("place_img")).execute(AppConfig.apiUrl);
                        }
                        CURRENT_UPLOAD_PLACE_COUNT++;
                    }
            }
            allImagePinsAdd = new ArrayList<HashMap<String,String>>();
            ImageButton btnAddpinImage = (ImageButton)findViewById(R.id.btnAddPinImage);
            btnAddpinImage.setImageResource(R.drawable.cover_img_plus);
//            final GridView grid1 = (GridView)findViewById(R.id.gridPlaceImgPreview);
//            grid1.setAdapter(null);
//            ViewGroup.LayoutParams params = grid1.getLayoutParams();
//            params.height = 0;
//            grid1.setLayoutParams(params);
        }
    }
    public void doLoadViewUserDetail(){
        BACK_TO_TAB = 0;
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        METHOD = "/userFindDetail";
                        new HomeActivity.CallService().execute(AppConfig.apiUrl);
                    }
                }, 1000);

    }
    public void doUploadPlaceImageEdit(){
        CURRENT_UPLOAD_PLACE_COUNT = 1;
        ALL_PLACE_UPLOAD_COUNT=0;
        UPD_METHOD = "/addImagePlaceEdit";
        if(allImagePinsEdit.size()>0){
            for(int i = 0;i<allImagePinsEdit.size();i++) {
                HashMap<String,String> hm = allImagePinsEdit.get(i);
                if(TextUtils.isEmpty(hm.get("place_img_id"))) {
                    ALL_PLACE_UPLOAD_COUNT++;
                }
            }
            for(int i = 0;i<allImagePinsEdit.size();i++){
                HashMap<String,String> hm = allImagePinsEdit.get(i);
                if(TextUtils.isEmpty(hm.get("place_img_id"))){
                    if (Build.VERSION.SDK_INT >= 11) {
                        new UploadImageService(i,CURRENT_UPLOAD_PLACE_COUNT,hm.get("place_img")).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,AppConfig.apiUrl);
                    } else {
                        new UploadImageService(i,CURRENT_UPLOAD_PLACE_COUNT,hm.get("place_img")).execute(AppConfig.apiUrl);
                    }
                    CURRENT_UPLOAD_PLACE_COUNT++;
                }
            }
//            final GridView grid1 = (GridView)findViewById(R.id.gridPlaceImgPreview);
//            grid1.setAdapter(null);
//            ViewGroup.LayoutParams params = grid1.getLayoutParams();
//            params.height = 0;
//            grid1.setLayoutParams(params);
        }else{
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            METHOD="/delImagePlace";
                            new HomeActivity.CallService().execute(AppConfig.apiUrl);
                        }
                    }, 500);

        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
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
    public class HomeFeedAdapter extends BaseAdapter
    {
        private Context context;
        private ArrayList<HashMap<String, String>> MyArr = new ArrayList<HashMap<String, String>>();

        public HomeFeedAdapter(Context c, ArrayList<HashMap<String, String>> list)
        {
            // TODO Auto-generated method stub
            context = c;
            MyArr = list;
        }

        public int getCount() {
            // TODO Auto-generated method stub
            return MyArr.size();
        }

        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.home_column, null);
            }
            final int curIndex = position;
            ListView grid = (ListView) parent;
            int size = grid.getWidth();
//            rowView.setLayoutParams(new GridView.LayoutParams(size, size));

            // ColImage
            ImageView imageView = (ImageView) convertView.findViewById(R.id.ColImgPath);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(size, size);
            imageView.setLayoutParams(layoutParams);
            imageView.setPadding(2,2,2,2);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Picasso.with(getApplicationContext()).load(MyArr.get(position).get("imagepath")).into(imageView);
            imageView.setClickable(true);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RelativeLayout layoutHomePreview = (RelativeLayout)findViewById(R.id.layoutHomePreview);
                    layoutHomePreview.setVisibility(View.VISIBLE);
                    LinearLayout layoutHomemain = (LinearLayout)  findViewById(R.id.layoutHomemain);
                    layoutHomemain.setVisibility(GONE);
                    HOME_PREVIEW_R_ID = MyArr.get(curIndex).get("route_id");
                    setHomePreviewTab();
                    doLoadDataPreviewHomeOverview();
                    PVPINTAB2_LOADED=false;
                }
            });
            ImageView userImgview = (ImageView)convertView.findViewById(R.id.ColImgUser);
            Picasso.with(getApplicationContext()).load(MyArr.get(position).get("user_image")).transform(new CircleTransform()).into(userImgview);


            // ColTitle
//            TextView txtPosition = (TextView) convertView.findViewById(R.id.ColTitle);
//            txtPosition.setPadding(15,15,0,0);
//            txtPosition.setText(MyArr.get(position).get("title"));

            // ColAuthor
            TextView txtTAuthor = (TextView) convertView.findViewById(R.id.ColTitleAhthor);
            txtTAuthor.setPadding(15,15,0,0);
            txtTAuthor.setText(Html.fromHtml("<b>"+MyArr.get(position).get("author")+"</b><br/><small>"+MyArr.get(position).get("title")+" ></small>"));
            // ColAuthor
//            TextView txtAuthor = (TextView) convertView.findViewById(R.id.ColAuthor);
//            txtAuthor.setPadding(15,15,0,0);
//            txtAuthor.setText(MyArr.get(position).get("author"));
            userImgview.setClickable(true);
            txtTAuthor.setClickable(true);
            userImgview.setTag(position);
            txtTAuthor.setTag(position);
            userImgview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int tag = (int)v.getTag();
                    USER_DETAIL_ID = MyArr.get(tag).get("user_id");
                    mTabHost.setCurrentTab(4);
                    doLoadViewUserDetail();
                }
            });
            txtTAuthor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int tag = (int)v.getTag();
                    USER_DETAIL_ID = MyArr.get(tag).get("user_id");
                    mTabHost.setCurrentTab(4);
                    doLoadViewUserDetail();
                }
            });
            // ColDetail
            TextView txtPicName = (TextView) convertView.findViewById(R.id.ColDesc);
            txtPicName.setPadding(15, 0, 0, 15);
            txtPicName.setText(Html.fromHtml("<b>"+MyArr.get(position).get("author")+"</b>  <span>"+MyArr.get(position).get("detail")+"</span>"));

            TextView txtTime = (TextView) convertView.findViewById(R.id.ColTime);
            txtTime.setPadding(15, 0, 0, 15);
            txtTime.setText(MyArr.get(position).get("time"));

            ImageButton btnDetail = (ImageButton)convertView.findViewById(R.id.btnFeedDetail);
            btnDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent modal = new Intent(getApplicationContext(),DetailModalActivity.class);
//                    startActivity(modal);
                    final CharSequence[] items = { "แชร์...",
                            "คัดลอก url" };
                    AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                    //builder.setTitle("Manage Photo.");
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            if (items[item].equals("แชร์...")) {
                                String sharedUrl = AppConfig.viewUrl+"?rid="+MyArr.get(curIndex).get("route_id");
                                Intent shareIntent = new Intent();
                                shareIntent.setAction(Intent.ACTION_SEND);
                                shareIntent.putExtra(Intent.EXTRA_TEXT, sharedUrl);
                                shareIntent.setType("text/plain");
                                startActivity(Intent.createChooser(shareIntent, "Choose sharing method"));
                            } else if (items[item].equals("คัดลอก url")) {
                                String sharedUrl = AppConfig.viewUrl+"?rid="+MyArr.get(curIndex).get("route_id");
                                setClipboard(HomeActivity.this,sharedUrl);
                                Toast.makeText(getApplication(), "Copied to clipboard",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    builder.show();
                }
            });

            ImageButton btnLike = (ImageButton)convertView.findViewById(R.id.btnHeart);
            ImageButton btnComment = (ImageButton)convertView.findViewById(R.id.btnComment);
            ImageButton btnFavorite = (ImageButton)convertView.findViewById(R.id.btnFavorite);
            ImageButton btnShare = (ImageButton)convertView.findViewById(R.id.btnShare);
            btnShare.setTag(position);
            btnLike.setTag(position);
            btnComment.setTag(position);
            btnFavorite.setTag(position);
            if(MyArr.get(position).get("like_status").equals("0")){
                btnLike.setImageResource(R.drawable.ic_heart);
            }
            else{
                btnLike.setImageResource(R.drawable.ic_heart_active);
            }
            if(MyArr.get(position).get("favorite_status").equals("0")){
                btnFavorite.setImageResource(R.drawable.ic_star);
            }
            else{
                btnFavorite.setImageResource(R.drawable.ic_star_active);
            }
            btnShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String sharedUrl = AppConfig.viewUrl+"?rid="+MyArr.get(curIndex).get("route_id");
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, sharedUrl);
                    shareIntent.setType("text/plain");
                    startActivity(Intent.createChooser(shareIntent, "Choose sharing method"));
                }
            });
            btnLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int tag = (int)v.getTag();
                    if(MyArr.get(tag).get("like_status").equals("0")){
                        LIKE_CURRENT_INDEX = tag;
                        METHOD = "/addLikeHome";
                        new HomeActivity.CallServiceNoLoad().execute(AppConfig.apiUrl);
                    }else{
                        LIKE_CURRENT_INDEX = tag;
                        METHOD = "/delLikeHome";
                        new HomeActivity.CallServiceNoLoad().execute(AppConfig.apiUrl);
                    }

                }
            });
            btnComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int tag = (int)v.getTag();
                    RelativeLayout layoutComment = (RelativeLayout)findViewById(R.id.layoutHomeComment);
                    layoutComment.setVisibility(View.VISIBLE);
                    LinearLayout layoutHomemain = (LinearLayout)  findViewById(R.id.layoutHomemain);
                    layoutHomemain.setVisibility(GONE);
                    HOME_COMMENT_ROUTE_ID = MyArr.get(tag).get("route_id");
                    METHOD="/listCommentHome";
                    new HomeActivity.CallServiceNoLoad().execute(AppConfig.apiUrl);
                }
            });
            btnFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int tag = (int)v.getTag();
                    if(MyArr.get(tag).get("favorite_status").equals("0")){
                        LIKE_CURRENT_INDEX = tag;
                        METHOD = "/addFavoriteHome";
                        new HomeActivity.CallServiceNoLoad().execute(AppConfig.apiUrl);
                    }else{
                        LIKE_CURRENT_INDEX = tag;
                        METHOD = "/delFavoriteHome";
                        new HomeActivity.CallServiceNoLoad().execute(AppConfig.apiUrl);
                    }

                }
            });
            return convertView;

        }

    }
    private void setClipboard(Context context, String text) {
        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
            clipboard.setPrimaryClip(clip);
        }
    }
    public class CommentFeedAdapter extends BaseAdapter
    {
        private Context context;
        private ArrayList<HashMap<String, String>> MyArr = new ArrayList<HashMap<String, String>>();

        public CommentFeedAdapter(Context c, ArrayList<HashMap<String, String>> list)
        {
            // TODO Auto-generated method stub
            context = c;
            MyArr = list;
        }

        public int getCount() {
            // TODO Auto-generated method stub
            return MyArr.size();
        }

        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.comment_column, null);
            }
            TextView txtUsername = (TextView) convertView.findViewById(R.id.ColCommentUsername);
            txtUsername.setText(MyArr.get(position).get("user_name"));
            // ColDetail
            EmojiTextView txtPicName = (EmojiTextView) convertView.findViewById(R.id.ColCommentDesc);
            txtPicName.setPadding(15, 0, 0, 15);
            txtPicName.setText(StringEscapeUtils.unescapeJava(MyArr.get(position).get("comment_detail")));


            TextView txtTime = (TextView) convertView.findViewById(R.id.ColCommentTime);
            txtTime.setPadding(15, 0, 0, 15);
            txtTime.setText(MyArr.get(position).get("time"));

            return convertView;

        }

    }
    public class PinDraftAdapter extends BaseAdapter
    {
        private Context context;
        private ArrayList<HashMap<String, String>> MyArr = new ArrayList<HashMap<String, String>>();

        public PinDraftAdapter(Context c, ArrayList<HashMap<String, String>> list)
        {
            // TODO Auto-generated method stub
            context = c;
            MyArr = list;
        }

        public int getCount() {
            // TODO Auto-generated method stub
            return MyArr.size();
        }

        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.pin_draft_column, null);
            }
            ListView grid = (ListView) parent;
            int size = grid.getWidth();
            // ColImage
            ImageView imageView = (ImageView) convertView.findViewById(R.id.ColPic);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(size/2, size/2);
            imageView.setLayoutParams(layoutParams);
            imageView.setPadding(2,2,2,2);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Picasso.with(getApplicationContext()).load(MyArr.get(position).get("imagepath")).into(imageView);
            ImageView imageView2 = (ImageView) convertView.findViewById(R.id.ColPic2);
            layoutParams = new LinearLayout.LayoutParams(size/2, size/2);
            imageView2.setLayoutParams(layoutParams);
            imageView2.setPadding(2,2,2,2);
            imageView2.setScaleType(ImageView.ScaleType.CENTER_CROP);
            // ColTitle
//            TextView txtPosition = (TextView) convertView.findViewById(R.id.ColTitle);
//            txtPosition.setPadding(15,15,0,0);
//            txtPosition.setText(MyArr.get(position).get("title"));

            // ColAuthor
            TextView txtRoutename = (TextView) convertView.findViewById(R.id.ColTitle);
            txtRoutename.setText(MyArr.get(position).get("title"));
            // ColAuthor
//            TextView txtAuthor = (TextView) convertView.findViewById(R.id.ColAuthor);
//            txtAuthor.setPadding(15,15,0,0);
//            txtAuthor.setText(MyArr.get(position).get("author"));

            // ColDetail
            TextView txtDetail = (TextView) convertView.findViewById(R.id.ColDetail);
            txtDetail.setText(MyArr.get(position).get("detail"));


            final int currentIndex = position;
            //Delete Button
            Button btnDelete = (Button)convertView.findViewById(R.id.ColBtnDelete);
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(HomeActivity.this)
                            .setTitle("Delete Draft Confirm")
                            .setMessage("Do you really want to delete?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                    DRAFT_R_ID = MyArr.get(currentIndex).get("route_id");
                                    METHOD="/delRoute";
                                    new CallService().execute(AppConfig.apiUrl);
                                }})
                            .setNegativeButton(android.R.string.no, null).show();
                }
            });

            //Preview Button
            Button btnPreview = (Button)convertView.findViewById(R.id.ColBtnPreview);
            btnPreview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PREVIEW_R_ID = MyArr.get(currentIndex).get("route_id");
                    isPreviewFromDraft=true;
                    LinearLayout layoutStartPin = (LinearLayout)findViewById(R.id.layoutStartPin);
                    layoutStartPin.setVisibility(GONE);
                    RelativeLayout layoutCreatePreviewPin = (RelativeLayout)findViewById(R.id.layoutAddPinPreview);
                    layoutCreatePreviewPin.setVisibility(View.VISIBLE);
                    setAddPinPreviewTab();
                    doLoadDataPreviewOverview();
                }
            });

            //Continue Button
            Button btnContinue = (Button)convertView.findViewById(R.id.ColBtnContinue);
            btnContinue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(HomeActivity.this)
                            .setTitle("Publish Confirm")
                            .setMessage("Do you really want to publish?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                    PREVIEW_R_ID = MyArr.get(currentIndex).get("route_id");
                                    isPublishFromDraft = true;
                                    METHOD="/publishRoute";
                                    new CallService().execute(AppConfig.apiUrl);
                                }})
                            .setNegativeButton(android.R.string.no, null).show();
                }
            });
            return convertView;

        }

    }
    public class FollowingAdapter extends BaseAdapter
    {
        private Context context;
        private ArrayList<HashMap<String, String>> MyArr = new ArrayList<HashMap<String, String>>();

        public FollowingAdapter(Context c, ArrayList<HashMap<String, String>> list)
        {
            // TODO Auto-generated method stub
            context = c;
            MyArr = list;
        }

        public int getCount() {
            // TODO Auto-generated method stub
            return MyArr.size();
        }

        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.following_column, null);
            }

            // ColImage
            ImageView imageView = (ImageView) convertView.findViewById(R.id.ColPicUser);
            Picasso.with(getApplicationContext()).load(MyArr.get(position).get("userimg")).transform(new CircleTransform()).into(imageView);

            //Name
            TextView txtUsername = (TextView) convertView.findViewById(R.id.ColUsername);
            txtUsername.setText(MyArr.get(position).get("username"));
            txtUsername.setText(Html.fromHtml("<b>"+MyArr.get(position).get("username")+"</b>&nbsp;<span>"+MyArr.get(position).get("activity")+"</span><br/><small style='color:#ccc;'>"+MyArr.get(position).get("time")+"</small>"));

//            //Activity
//            TextView txtActivity = (TextView) convertView.findViewById(R.id.ColActivity);
//            txtActivity.setText(MyArr.get(position).get("activity"));
//
//            //Time
//            TextView txtTime = (TextView) convertView.findViewById(R.id.ColTime);
//            txtTime.setText(MyArr.get(position).get("time"));

            //Action
            ImageView imgAction = (ImageView)convertView.findViewById(R.id.btnAction);

            if(MyArr.get(position).get("noti_type").equals("like")){
                Picasso.with(getApplicationContext()).load(MyArr.get(position).get("imagepath")).into(imgAction);
            }
            else if(MyArr.get(position).get("noti_type").equals("comment")){
                Picasso.with(getApplicationContext()).load(MyArr.get(position).get("imagepath")).into(imgAction);
            }
            else if(MyArr.get(position).get("noti_type").equals("follow")){
                Picasso.with(getApplicationContext()).load(MyArr.get(position).get("user_passive_image")).transform(new CircleTransform()).into(imgAction);
            }
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent modal = new Intent(getApplicationContext(),DetailModalActivity.class);
//                    startActivity(modal);
                }
            });

            return convertView;

        }

    }
    public class NotisAdapter extends BaseAdapter
    {
        private Context context;
        private ArrayList<HashMap<String, String>> MyArr = new ArrayList<HashMap<String, String>>();

        public NotisAdapter(Context c, ArrayList<HashMap<String, String>> list)
        {
            // TODO Auto-generated method stub
            context = c;
            MyArr = list;
        }

        public int getCount() {
            // TODO Auto-generated method stub
            return MyArr.size();
        }

        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.following_column, null);
            }

            // ColImage
            ImageView imageView = (ImageView) convertView.findViewById(R.id.ColPicUser);
            Picasso.with(getApplicationContext()).load(MyArr.get(position).get("userimg")).transform(new CircleTransform()).into(imageView);

            //Name
            TextView txtUsername = (TextView) convertView.findViewById(R.id.ColUsername);
            txtUsername.setText(MyArr.get(position).get("username"));
            txtUsername.setText(Html.fromHtml("<b>"+MyArr.get(position).get("username")+"</b>&nbsp;<span>"+MyArr.get(position).get("activity")+"</span><br/><small style='color:#ccc;'>"+MyArr.get(position).get("time")+"</small>"));

//            //Activity
//            TextView txtActivity = (TextView) convertView.findViewById(R.id.ColActivity);
//            txtActivity.setText(MyArr.get(position).get("activity"));
//
//            //Time
//            TextView txtTime = (TextView) convertView.findViewById(R.id.ColTime);
//            txtTime.setText(MyArr.get(position).get("time"));

            //Action
            ImageView imgAction = (ImageView)convertView.findViewById(R.id.btnAction);
            Picasso.with(getApplicationContext()).load(MyArr.get(position).get("imagepath")).into(imgAction);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent modal = new Intent(getApplicationContext(),DetailModalActivity.class);
//                    startActivity(modal);
                }
            });

            return convertView;

        }

    }
    public class FindFriendsAdapter extends BaseAdapter
    {
        private Context context;
        private ArrayList<HashMap<String, String>> MyArr = new ArrayList<HashMap<String, String>>();

        public FindFriendsAdapter(Context c, ArrayList<HashMap<String, String>> list)
        {
            // TODO Auto-generated method stub
            context = c;
            MyArr = list;
        }

        public int getCount() {
            // TODO Auto-generated method stub
            return MyArr.size();
        }

        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.following_column, null);
            }
            final int currentIndex = position;
            // ColImage
            ImageView imageView = (ImageView) convertView.findViewById(R.id.ColPicUser);
            Picasso.with(getApplicationContext()).load(MyArr.get(position).get("user_image")).transform(new CircleTransform()).into(imageView);

            //Name
            TextView txtUsername = (TextView) convertView.findViewById(R.id.ColUsername);
            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) txtUsername.getLayoutParams();
            mlp.setMargins(30,50,0,0);
            txtUsername.setLayoutParams(mlp);
            txtUsername.setText(MyArr.get(position).get("user_name"));
            //Activity
            TextView txtActivity = (TextView) convertView.findViewById(R.id.ColActivity);
            txtActivity.setText("");

            //Time
            TextView txtTime = (TextView) convertView.findViewById(R.id.ColTime);
            txtTime.setText("");

            //Action
            final ImageView imgAction = (ImageView)convertView.findViewById(R.id.btnAction);
            imgAction.setTag(position);
            imgAction.setClickable(true);
            imgAction.setPadding(15,15,15,15);
            //Log.d("DEBUG","POSITION = "+String.valueOf(position));
            if(MyArr.get(position).get("followed").equals("0")){
                imgAction.setImageResource(R.drawable.ic_follow_no);
                imgAction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Log.d("DEBUG","followed = "+MyArr.get((int)v.getTag()).get("followed"));
                        if(MyArr.get((int)v.getTag()).get("followed").equals("0")){
                            //Follow from list
                            FOLLOW_USER_ID = MyArr.get((int)v.getTag()).get("user_id");
                            FOLLOW_CURRENT_INDEX = (int)v.getTag();
                            METHOD = "/findfriendFollow";
                            new HomeActivity.CallServiceNoLoad().execute(AppConfig.apiUrl);
                        }

                    }
                });
            }else {
                imgAction.setImageResource(R.drawable.ic_follow_yes);
            }
            //Picasso.with(getApplicationContext()).load().into(imgAction);

//            convertView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
////                    Intent modal = new Intent(getApplicationContext(),DetailModalActivity.class);
////                    startActivity(modal);
//                    RelativeLayout layoutFindfriends = (RelativeLayout)findViewById(R.id.layoutSearchFriends);
//                    layoutFindfriends.setVisibility(View.GONE);
//                    RelativeLayout layoutFindUserDetail = (RelativeLayout)findViewById(R.id.layoutUserFindDetail);
//                    layoutFindUserDetail.setVisibility(View.VISIBLE);
//                    USER_DETAIL_ID =MyArr.get(currentIndex).get("user_id");
//                    METHOD="/userFindDetail";
//                    new HomeActivity.CallService().execute(AppConfig.apiUrl);
//                    ImageButton btnList = (ImageButton) findViewById(R.id.btnFindViewList);
//                    ImageButton btnGrid = (ImageButton)findViewById(R.id.btnFindViewGrid);
//                    ImageButton btnBack = (ImageButton) findViewById(R.id.btnFindUserBack);
//                    btnBack.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            RelativeLayout layoutFindUserDetail = (RelativeLayout)findViewById(R.id.layoutUserFindDetail);
//                            layoutFindUserDetail.setVisibility(View.GONE);
//                            RelativeLayout layoutFindfriends = (RelativeLayout)findViewById(R.id.layoutSearchFriends);
//                            layoutFindfriends.setVisibility(View.VISIBLE);
//                            USER_DETAIL_ID="";
//                        }
//                    });
//                    btnList.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            view.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_item));
//                            GridView gridUser = (GridView)findViewById(R.id.gridFindUser);
//                            ListView listUser = (ListView)findViewById(R.id.listFindUser);
//                            gridUser.setVisibility(GONE);
//                            listUser.setVisibility(View.VISIBLE);
//                            METHOD = "/listRoutesFeedByFindUserList";
//                            new HomeActivity.CallService().execute(AppConfig.apiUrl);
//                        }
//                    });
//                    btnGrid.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            view.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_item));
//                            GridView gridUser = (GridView)findViewById(R.id.gridFindUser);
//                            ListView listUser = (ListView)findViewById(R.id.listFindUser);
//                            listUser.setVisibility(GONE);
//                            gridUser.setVisibility(View.VISIBLE);
//                            METHOD = "/listRoutesFeedByFindFindUserGrid";
//                            new HomeActivity.CallService().execute(AppConfig.apiUrl);
//                        }
//                    });
//                }
//            });

            return convertView;

        }

    }
    public class ExploreFeedAdapter extends BaseAdapter
    {
        private Context context;
        private ArrayList<HashMap<String, String>> MyArr = new ArrayList<HashMap<String, String>>();
        private LayoutInflater inflater=null;
        public ExploreFeedAdapter(Context c, ArrayList<HashMap<String, String>> list)
        {
            // TODO Auto-generated method stub
            context = c;
            MyArr = list;
            inflater = ( LayoutInflater )context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public int getCount() {
            // TODO Auto-generated method stub
            return MyArr.size();
        }

        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            View rowView;
            final int curIndex = position;
            rowView = inflater.inflate(R.layout.explore_column, null);
            GridView grid = (GridView) parent;
            final int size = grid.getColumnWidth();
            //Log.d("DEBUG","Grid Width = "+String.valueOf(size)+",position="+String.valueOf(position));

            if(size>0){
                ImageView imageView = (ImageView) rowView.findViewById(R.id.ColImgPath);
//                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(size, size);
                imageView.setLayoutParams(new LinearLayout.LayoutParams(size,size));
                imageView.setPadding(2,2,2,2);
                Picasso.with(getApplicationContext()).load(MyArr.get(position).get("imagepath")).into(imageView, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {

                    }
                });
                rowView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RelativeLayout layoutExplorePreview = (RelativeLayout)findViewById(R.id.layoutExplorePreview);
                        layoutExplorePreview.setVisibility(View.VISIBLE);
                        EXPLORE_PREVIEW_R_ID = MyArr.get(curIndex).get("route_id");
                        setExplorePreviewTab();
                        doLoadDataPreviewExploreOverview();
                        PVPINTAB2_LOADED=false;
                    }
                });
            }




            return rowView;
        }

    }
    public class RouteGridExploreFeedAdapter extends BaseAdapter
    {
        private Context context;
        private ArrayList<HashMap<String, String>> MyArr = new ArrayList<HashMap<String, String>>();
        private LayoutInflater inflater=null;
        public RouteGridExploreFeedAdapter(Context c, ArrayList<HashMap<String, String>> list)
        {
            // TODO Auto-generated method stub
            context = c;
            MyArr = list;
            inflater = ( LayoutInflater )context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public int getCount() {
            // TODO Auto-generated method stub
            return MyArr.size();
        }

        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            View rowView;
            final int curIndex = position;
            rowView = inflater.inflate(R.layout.explore_column, null);
            GridView grid = (GridView) parent;
            final int size = grid.getColumnWidth();
            //Log.d("DEBUG","Grid Width = "+String.valueOf(size)+",position="+String.valueOf(position));

            if(size>0){
                ImageView imageView = (ImageView) rowView.findViewById(R.id.ColImgPath);
//                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(size, size);
                imageView.setLayoutParams(new LinearLayout.LayoutParams(size,size));
                imageView.setPadding(2,2,2,2);
                Picasso.with(getApplicationContext()).load(MyArr.get(position).get("imagepath")).into(imageView, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {

                    }
                });

            }
            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EXPLORE_PREVIEW_PIN_ID = MyArr.get(curIndex).get("place_id");
                    METHOD ="/getPlaceExplorePreview";
                    new HomeActivity.CallService().execute(AppConfig.apiUrl);
                }
            });




            return rowView;
        }

    }
    public class RouteGridHomeFeedAdapter extends BaseAdapter
    {
        private Context context;
        private ArrayList<HashMap<String, String>> MyArr = new ArrayList<HashMap<String, String>>();
        private LayoutInflater inflater=null;
        public RouteGridHomeFeedAdapter(Context c, ArrayList<HashMap<String, String>> list)
        {
            // TODO Auto-generated method stub
            context = c;
            MyArr = list;
            inflater = ( LayoutInflater )context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public int getCount() {
            // TODO Auto-generated method stub
            return MyArr.size();
        }

        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            View rowView;
            final int curIndex = position;
            rowView = inflater.inflate(R.layout.explore_column, null);
            GridView grid = (GridView) parent;
            final int size = grid.getColumnWidth();
            //Log.d("DEBUG","Grid Width = "+String.valueOf(size)+",position="+String.valueOf(position));

            if(size>0){
                ImageView imageView = (ImageView) rowView.findViewById(R.id.ColImgPath);
//                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(size, size);
                imageView.setLayoutParams(new LinearLayout.LayoutParams(size,size));
                imageView.setPadding(2,2,2,2);
                Picasso.with(getApplicationContext()).load(MyArr.get(position).get("imagepath")).into(imageView, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {

                    }
                });
            }
            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HOME_PREVIEW_PIN_ID = MyArr.get(curIndex).get("place_id");
                    METHOD ="/getPlaceHomePreview";
                    new HomeActivity.CallService().execute(AppConfig.apiUrl);
                }
            });



            return rowView;
        }

    }
    public class RouteGridUserFeedAdapter extends BaseAdapter
    {
        private Context context;
        private ArrayList<HashMap<String, String>> MyArr = new ArrayList<HashMap<String, String>>();
        private LayoutInflater inflater=null;
        public RouteGridUserFeedAdapter(Context c, ArrayList<HashMap<String, String>> list)
        {
            // TODO Auto-generated method stub
            context = c;
            MyArr = list;
            inflater = ( LayoutInflater )context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public int getCount() {
            // TODO Auto-generated method stub
            return MyArr.size();
        }

        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            View rowView;
            final int curIndex = position;
            rowView = inflater.inflate(R.layout.explore_column, null);
            GridView grid = (GridView) parent;
            final int size = grid.getColumnWidth();
            //Log.d("DEBUG","Grid Width = "+String.valueOf(size)+",position="+String.valueOf(position));

            if(size>0){
                ImageView imageView = (ImageView) rowView.findViewById(R.id.ColImgPath);
//                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(size, size);
                imageView.setLayoutParams(new LinearLayout.LayoutParams(size,size));
                imageView.setPadding(2,2,2,2);
                Picasso.with(getApplicationContext()).load(MyArr.get(position).get("imagepath")).into(imageView, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {

                    }
                });
            }
            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    USER_PREVIEW_PIN_ID = MyArr.get(curIndex).get("place_id");
                    METHOD ="/getPlaceUserPreview";
                    new HomeActivity.CallService().execute(AppConfig.apiUrl);
                }
            });



            return rowView;
        }

    }
    public class RouteGridFeedAdapter extends BaseAdapter
    {
        private Context context;
        private ArrayList<HashMap<String, String>> MyArr = new ArrayList<HashMap<String, String>>();
        private LayoutInflater inflater=null;
        public RouteGridFeedAdapter(Context c, ArrayList<HashMap<String, String>> list)
        {
            // TODO Auto-generated method stub
            context = c;
            MyArr = list;
            inflater = ( LayoutInflater )context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public int getCount() {
            // TODO Auto-generated method stub
            return MyArr.size();
        }

        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            View rowView;
            final int curIndex = position;
            rowView = inflater.inflate(R.layout.explore_column, null);
            GridView grid = (GridView) parent;
            final int size = grid.getColumnWidth();
            //Log.d("DEBUG","Grid Width = "+String.valueOf(size)+",position="+String.valueOf(position));

            if(size>0){
                ImageView imageView = (ImageView) rowView.findViewById(R.id.ColImgPath);
//                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(size, size);
                imageView.setLayoutParams(new LinearLayout.LayoutParams(size,size));
                imageView.setPadding(2,2,2,2);
                Picasso.with(getApplicationContext()).load(MyArr.get(position).get("imagepath")).into(imageView, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {

                    }
                });
            }




            return rowView;
        }

    }
    public class RouteImagePreviewAdapter extends BaseAdapter
    {
        private Context context;
        private ArrayList<HashMap<String, String>> MyArr = new ArrayList<HashMap<String, String>>();
        private LayoutInflater inflater=null;
        public RouteImagePreviewAdapter(Context c, ArrayList<HashMap<String, String>> list)
        {
            // TODO Auto-generated method stub
            context = c;
            MyArr = list;
            inflater = ( LayoutInflater )context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public int getCount() {
            // TODO Auto-generated method stub
            return MyArr.size();
        }

        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            View rowView;

            rowView = inflater.inflate(R.layout.explore_column, null);
            GridView grid = (GridView) parent;
            int size = grid.getColumnWidth();
//            rowView.setLayoutParams(new GridView.LayoutParams(size, size));
            ImageView imageView = (ImageView) rowView.findViewById(R.id.ColImgPath);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(size, size);
            imageView.setLayoutParams(layoutParams);
            imageView.setPadding(2,2,2,2);
            byte[] decodedBytes = new byte[0];
            if(userChoosenTask.endsWith("_ROUTE")){
                decodedBytes = Base64.decode(MyArr.get(position).get("route_img"), Base64.DEFAULT);
            }
            else if(userChoosenTask.endsWith("_PIN")){
                decodedBytes = Base64.decode(MyArr.get(position).get("place_img"), Base64.DEFAULT);
            }
            final Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes , 0, decodedBytes.length);
            imageView.setImageBitmap(bitmap);
            //Picasso.with(getApplicationContext()).load(MyArr.get(position).get("imagepath")).into(imageView);

            rowView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    final CharSequence[] items = { "View", "Delete",
                            "Cancel" };
                    AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                    builder.setTitle("Manage Photo.");
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            if (items[item].equals("View")) {
                                RelativeLayout layoutImagePreview = (RelativeLayout)findViewById(R.id.layoutTab3ImagePreview);
                                layoutImagePreview.setVisibility(View.VISIBLE);
                                if(userChoosenTask.endsWith("_ROUTE")){
                                    LinearLayout layoutStartPin = (LinearLayout)findViewById(R.id.layoutStartPin);
                                    layoutStartPin.setVisibility(GONE);
                                }
                                else if(userChoosenTask.endsWith("_PIN")){
                                    RelativeLayout layoutAddPin = (RelativeLayout)findViewById(R.id.layoutAddPin);
                                    layoutAddPin.setVisibility(GONE);
                                }
                                ImageView imgPreview = (ImageView)findViewById(R.id.imgTab3ImagePreview);
                                imgPreview.setImageBitmap(bitmap);
                            } else if (items[item].equals("Delete")) {
                                if(userChoosenTask.endsWith("_ROUTE")){
                                    allImageRouteAdd.remove(position);
                                    GridView grid1 = (GridView)findViewById(R.id.gridRouteImgPreview);
                                    if(allImageRouteAdd.size()==0){
                                        grid1.setAdapter(null);
                                    }else{
                                        grid1.setAdapter(new RouteImagePreviewAdapter(HomeActivity.this,allImageRouteAdd));
                                    }
                                    MainUtility mUtility = new MainUtility();
                                    mUtility.setGridViewHeightBasedOnChildren(grid1,3);
                                }else if(userChoosenTask.endsWith("_PIN")){
                                    allImagePinsAdd.remove(position);
                                    GridView grid1 = (GridView)findViewById(R.id.gridPlaceImgPreview);
                                    if(allImagePinsAdd.size()==0){
                                        grid1.setAdapter(null);
                                    }else{
                                        grid1.setAdapter(new RouteImagePreviewAdapter(HomeActivity.this,allImagePinsAdd));
                                    }
                                    MainUtility mUtility = new MainUtility();
                                    mUtility.setGridViewHeightBasedOnChildren(grid1,3);
                                }


                            } else if (items[item].equals("Cancel")) {
                                dialog.dismiss();
                            }
                        }
                    });
                    builder.show();
                }
            });

            return rowView;
        }

    }
    public class UserGridFeedAdapter extends BaseAdapter
    {
        private Context context;
        private ArrayList<HashMap<String, String>> MyArr = new ArrayList<HashMap<String, String>>();
        private LayoutInflater inflater=null;
        public UserGridFeedAdapter(Context c, ArrayList<HashMap<String, String>> list)
        {
            // TODO Auto-generated method stub
            context = c;
            MyArr = list;
            inflater = ( LayoutInflater )context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public int getCount() {
            // TODO Auto-generated method stub
            return MyArr.size();
        }

        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            View rowView;
            final int curIndex = position;
            rowView = inflater.inflate(R.layout.explore_column, null);
            GridView grid = (GridView) parent;
            final int size = grid.getColumnWidth();
            ImageView imageView = (ImageView) rowView.findViewById(R.id.ColImgPath);
            if(size>0){
                imageView.setLayoutParams(new LinearLayout.LayoutParams(size,size));
                imageView.setPadding(2,2,2,2);
                Picasso.with(getApplicationContext()).load(MyArr.get(position).get("imagepath")).into(imageView);
            }
            rowView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    RelativeLayout layoutHomePreview = (RelativeLayout)findViewById(R.id.layoutUserPreview);
                    layoutHomePreview.setVisibility(View.VISIBLE);
                    USER_PREVIEW_R_ID = MyArr.get(curIndex).get("route_id");
                    setUserPreviewTab();
                    doLoadDataPreviewUserOverview();
                    PVPINTAB2_LOADED=false;
                }
            });

            return rowView;
        }

    }
    public class UserListFeedAdapter extends BaseAdapter
    {
        private Context context;
        private ArrayList<HashMap<String, String>> MyArr = new ArrayList<HashMap<String, String>>();

        public UserListFeedAdapter(Context c, ArrayList<HashMap<String, String>> list)
        {
            // TODO Auto-generated method stub
            context = c;
            MyArr = list;
        }

        public int getCount() {
            // TODO Auto-generated method stub
            return MyArr.size();
        }

        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.home_column, null);
            }
            final int curIndex = position;
            // ColImage
            ListView grid = (ListView) parent;
            int size = grid.getWidth();
//            rowView.setLayoutParams(new GridView.LayoutParams(size, size));

            // ColImage
            ImageView imageView = (ImageView) convertView.findViewById(R.id.ColImgPath);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(size, size);
            imageView.setLayoutParams(layoutParams);
            imageView.setPadding(2,2,2,2);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Picasso.with(getApplicationContext()).load(MyArr.get(position).get("imagepath")).into(imageView);
            imageView.setClickable(true);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RelativeLayout layoutHomePreview = (RelativeLayout)findViewById(R.id.layoutUserPreview);
                    layoutHomePreview.setVisibility(View.VISIBLE);
                    USER_PREVIEW_R_ID = MyArr.get(curIndex).get("route_id");
                    setUserPreviewTab();
                    doLoadDataPreviewUserOverview();
                    PVPINTAB2_LOADED=false;
                }
            });
            ImageView userImgview = (ImageView)convertView.findViewById(R.id.ColImgUser);
            Picasso.with(getApplicationContext()).load(MyArr.get(position).get("user_image")).transform(new CircleTransform()).into(userImgview);
            // ColTitle
//            TextView txtPosition = (TextView) convertView.findViewById(R.id.ColTitle);
//            txtPosition.setPadding(15,15,0,0);
//            txtPosition.setText(MyArr.get(position).get("title"));

            // ColAuthor
            TextView txtTAuthor = (TextView) convertView.findViewById(R.id.ColTitleAhthor);
            txtTAuthor.setPadding(15,15,0,0);
            txtTAuthor.setText(Html.fromHtml("<b>"+MyArr.get(position).get("author")+"</b><br/><small>"+MyArr.get(position).get("title")+" ></small>"));
            // ColAuthor
//            TextView txtAuthor = (TextView) convertView.findViewById(R.id.ColAuthor);
//            txtAuthor.setPadding(15,15,0,0);
//            txtAuthor.setText(MyArr.get(position).get("author"));

            // ColDetail
            TextView txtPicName = (TextView) convertView.findViewById(R.id.ColDesc);
            txtPicName.setPadding(15, 0, 0, 15);
            txtPicName.setText(Html.fromHtml("<b>"+MyArr.get(position).get("author")+"</b>  <span>"+MyArr.get(position).get("detail")+"</span>"));

            TextView txtTime = (TextView) convertView.findViewById(R.id.ColTime);
            txtTime.setPadding(15, 0, 0, 15);
            txtTime.setText(MyArr.get(position).get("time"));


            ImageButton btnDetail = (ImageButton)convertView.findViewById(R.id.btnFeedDetail);
            btnDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent modal = new Intent(getApplicationContext(),DetailModalActivity.class);
//                    startActivity(modal);
                    CharSequence[] tmp ;
                    if(MyArr.get(curIndex).get("user_id").equals(USER_ID)){
                        tmp = new CharSequence[]{ "เปลี่ยนเป็น Draft","แชร์...","คัดลอก url" };

                    }
                    else{
                        tmp = new CharSequence[]{ "แชร์...","คัดลอก url" };
                    }
                    final CharSequence[] items = tmp;
                    AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                    //builder.setTitle("Manage Photo.");
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            if (items[item].equals("แชร์...")) {
                                String sharedUrl = AppConfig.viewUrl+"?rid="+MyArr.get(curIndex).get("route_id");
                                Intent shareIntent = new Intent();
                                shareIntent.setAction(Intent.ACTION_SEND);
                                shareIntent.putExtra(Intent.EXTRA_TEXT, sharedUrl);
                                shareIntent.setType("text/plain");
                                startActivity(Intent.createChooser(shareIntent, "Choose sharing method"));
                            } else if (items[item].equals("คัดลอก url")) {
                                String sharedUrl = AppConfig.viewUrl+"?rid="+MyArr.get(curIndex).get("route_id");
                                setClipboard(HomeActivity.this,sharedUrl);
                                Toast.makeText(getApplication(), "Copied to clipboard",
                                        Toast.LENGTH_LONG).show();
                            }
                            else if (items[item].equals("เปลี่ยนเป็น Draft")) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                                builder.setMessage("Are you sure you want to set to draft?")
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                METHOD = "/draftRoute";
                                                USER_PREVIEW_R_ID = MyArr.get(curIndex).get("route_id");
                                                new HomeActivity.CallService().execute(AppConfig.apiUrl);
                                            }
                                        })
                                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();

                            }
                        }
                    });
                    builder.show();
                }
            });
            ImageButton btnLike = (ImageButton)convertView.findViewById(R.id.btnHeart);
            ImageButton btnComment = (ImageButton)convertView.findViewById(R.id.btnComment);
            ImageButton btnFavorite = (ImageButton)convertView.findViewById(R.id.btnFavorite);
            btnLike.setTag(position);
            btnComment.setTag(position);
            btnFavorite.setTag(position);
            if(MyArr.get(position).get("like_status").equals("0")){
                btnLike.setImageResource(R.drawable.ic_heart);
            }
            else{
                btnLike.setImageResource(R.drawable.ic_heart_active);
            }
            if(MyArr.get(position).get("favorite_status").equals("0")){
                btnFavorite.setImageResource(R.drawable.ic_star);
            }
            else{
                btnFavorite.setImageResource(R.drawable.ic_star_active);
            }
            btnLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int tag = (int)v.getTag();
                    if(MyArr.get(tag).get("like_status").equals("0")){
                        LIKE_CURRENT_INDEX = tag;
                        METHOD = "/addLikeUser";
                        new HomeActivity.CallServiceNoLoad().execute(AppConfig.apiUrl);
                    }else{
                        LIKE_CURRENT_INDEX = tag;
                        METHOD = "/delLikeUser";
                        new HomeActivity.CallServiceNoLoad().execute(AppConfig.apiUrl);
                    }

                }
            });
            btnComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int tag = (int)v.getTag();
                    RelativeLayout layoutComment = (RelativeLayout)findViewById(R.id.layoutUserComment);
                    layoutComment.setVisibility(View.VISIBLE);
                    RelativeLayout layoutUsermain = (RelativeLayout)  findViewById(R.id.layoutUserDetail);
                    layoutUsermain.setVisibility(GONE);
                    USER_COMMENT_ROUTE_ID = MyArr.get(tag).get("route_id");
                    METHOD="/listCommentUser";
                    new HomeActivity.CallServiceNoLoad().execute(AppConfig.apiUrl);
                }
            });
            btnFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int tag = (int)v.getTag();
                    if(MyArr.get(tag).get("favorite_status").equals("0")){
                        LIKE_CURRENT_INDEX = tag;
                        METHOD = "/addFavoriteUser";
                        new HomeActivity.CallServiceNoLoad().execute(AppConfig.apiUrl);
                    }else{
                        LIKE_CURRENT_INDEX = tag;
                        METHOD = "/delFavoriteUser";
                        new HomeActivity.CallServiceNoLoad().execute(AppConfig.apiUrl);
                    }

                }
            });
            return convertView;

        }

    }
    public class PreviewPinsHomeAdapter extends BaseAdapter
    {
        private Context context;
        private ArrayList<HashMap<String, String>> MyArr = new ArrayList<HashMap<String, String>>();

        public PreviewPinsHomeAdapter(Context c, ArrayList<HashMap<String, String>> list)
        {
            // TODO Auto-generated method stub
            context = c;
            MyArr = list;
        }

        public int getCount() {
            // TODO Auto-generated method stub
            return MyArr.size();
        }

        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.preview_pins_column, null);

            }
            final int curIndex = position;

            //title
            TextView txtUsername = (TextView) convertView.findViewById(R.id.ColLocation);
            txtUsername.setText(MyArr.get(position).get("title"));
            //detail
            TextView txtActivity = (TextView) convertView.findViewById(R.id.ColLocationDetail);
            txtActivity.setText(MyArr.get(position).get("detail"));

            ImageView imgAction = (ImageView)convertView.findViewById(R.id.ColPic);
            ViewGroup.LayoutParams layoutParamsImg = imgAction.getLayoutParams();
            int w = layoutParamsImg.width;
            if(w>0){

//                Log.d("DEBUG","PIN WIDTH = "+imgAction.getMeasuredWidth()+",HEiGHT = "+imgAction.getMeasuredHeight());
                layoutParamsImg.height = layoutParamsImg.width;
                imgAction.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            imgAction.setBackground(null);
                imgAction.setLayoutParams(layoutParamsImg);
            }

//            Log.d("JSON","Image = "+MyArr.get(position).get("imagepath"));
            Picasso.with(getApplicationContext()).load(MyArr.get(position).get("imagepath")).into(imgAction);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HOME_PREVIEW_PIN_ID = MyArr.get(curIndex).get("place_id");
                    METHOD ="/getPlaceHomePreview";
                    new HomeActivity.CallService().execute(AppConfig.apiUrl);
                }
            });

            return convertView;

        }

    }
    public class PreviewPinsUserAdapter extends BaseAdapter
    {
        private Context context;
        private ArrayList<HashMap<String, String>> MyArr = new ArrayList<HashMap<String, String>>();

        public PreviewPinsUserAdapter(Context c, ArrayList<HashMap<String, String>> list)
        {
            // TODO Auto-generated method stub
            context = c;
            MyArr = list;
        }

        public int getCount() {
            // TODO Auto-generated method stub
            return MyArr.size();
        }

        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.preview_pins_column, null);

            }
            final int curIndex = position;

            //title
            TextView txtUsername = (TextView) convertView.findViewById(R.id.ColLocation);
            txtUsername.setText(MyArr.get(position).get("title"));
            //detail
            TextView txtActivity = (TextView) convertView.findViewById(R.id.ColLocationDetail);
            txtActivity.setText(MyArr.get(position).get("detail"));

            ImageView imgAction = (ImageView)convertView.findViewById(R.id.ColPic);
            ViewGroup.LayoutParams layoutParamsImg = imgAction.getLayoutParams();
            int w = layoutParamsImg.width;
            if(w>0){

//                Log.d("DEBUG","PIN WIDTH = "+imgAction.getMeasuredWidth()+",HEiGHT = "+imgAction.getMeasuredHeight());
                layoutParamsImg.height = layoutParamsImg.width;
                imgAction.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            imgAction.setBackground(null);
                imgAction.setLayoutParams(layoutParamsImg);
            }

//            Log.d("JSON","Image = "+MyArr.get(position).get("imagepath"));
            Picasso.with(getApplicationContext()).load(MyArr.get(position).get("imagepath")).into(imgAction);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    USER_PREVIEW_PIN_ID = MyArr.get(curIndex).get("place_id");
                    METHOD ="/getPlaceUserPreview";
                    new HomeActivity.CallService().execute(AppConfig.apiUrl);
                }
            });

            return convertView;

        }

    }
    public class PreviewPinsExploreAdapter extends BaseAdapter
    {
        private Context context;
        private ArrayList<HashMap<String, String>> MyArr = new ArrayList<HashMap<String, String>>();

        public PreviewPinsExploreAdapter(Context c, ArrayList<HashMap<String, String>> list)
        {
            // TODO Auto-generated method stub
            context = c;
            MyArr = list;
        }

        public int getCount() {
            // TODO Auto-generated method stub
            return MyArr.size();
        }

        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.preview_pins_column, null);

            }
            final int curIndex = position;

            //title
            TextView txtUsername = (TextView) convertView.findViewById(R.id.ColLocation);
            txtUsername.setText(MyArr.get(position).get("title"));
            //detail
            TextView txtActivity = (TextView) convertView.findViewById(R.id.ColLocationDetail);
            txtActivity.setText(MyArr.get(position).get("detail"));

            ImageView imgAction = (ImageView)convertView.findViewById(R.id.ColPic);
            ViewGroup.LayoutParams layoutParamsImg = imgAction.getLayoutParams();
            int w = layoutParamsImg.width;
            if(w>0){

//                Log.d("DEBUG","PIN WIDTH = "+imgAction.getMeasuredWidth()+",HEiGHT = "+imgAction.getMeasuredHeight());
                layoutParamsImg.height = layoutParamsImg.width;
                imgAction.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            imgAction.setBackground(null);
                imgAction.setLayoutParams(layoutParamsImg);
            }

//            Log.d("JSON","Image = "+MyArr.get(position).get("imagepath"));
            Picasso.with(getApplicationContext()).load(MyArr.get(position).get("imagepath")).into(imgAction);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EXPLORE_PREVIEW_PIN_ID = MyArr.get(curIndex).get("place_id");
                    METHOD ="/getPlaceExplorePreview";
                    new HomeActivity.CallService().execute(AppConfig.apiUrl);
                }
            });

            return convertView;

        }

    }
    public class PreviewPinsAdapter extends BaseAdapter
    {
        private Context context;
        private ArrayList<HashMap<String, String>> MyArr = new ArrayList<HashMap<String, String>>();

        public PreviewPinsAdapter(Context c, ArrayList<HashMap<String, String>> list)
        {
            // TODO Auto-generated method stub
            context = c;
            MyArr = list;
        }

        public int getCount() {
            // TODO Auto-generated method stub
            return MyArr.size();
        }

        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {
                    convertView = inflater.inflate(R.layout.preview_pins_column, null);

            }


            //title
            TextView txtUsername = (TextView) convertView.findViewById(R.id.ColLocation);
            txtUsername.setText(MyArr.get(position).get("title"));
            //detail
            TextView txtActivity = (TextView) convertView.findViewById(R.id.ColLocationDetail);
            txtActivity.setText(MyArr.get(position).get("detail"));

            ImageView imgAction = (ImageView)convertView.findViewById(R.id.ColPic);
            ViewGroup.LayoutParams layoutParamsImg = imgAction.getLayoutParams();
            int w = layoutParamsImg.width;
            if(w>0){

//                Log.d("DEBUG","PIN WIDTH = "+imgAction.getMeasuredWidth()+",HEiGHT = "+imgAction.getMeasuredHeight());
                layoutParamsImg.height = layoutParamsImg.width;
                imgAction.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            imgAction.setBackground(null);
                imgAction.setLayoutParams(layoutParamsImg);
            }

//            Log.d("JSON","Image = "+MyArr.get(position).get("imagepath"));
            Picasso.with(getApplicationContext()).load(MyArr.get(position).get("imagepath")).into(imgAction);
            return convertView;

        }

    }
    public class PreviewPinsEditAdapter extends BaseAdapter
    {
        private Context context;
        private ArrayList<HashMap<String, String>> MyArr = new ArrayList<HashMap<String, String>>();

        public PreviewPinsEditAdapter(Context c, ArrayList<HashMap<String, String>> list)
        {
            // TODO Auto-generated method stub
            context = c;
            MyArr = list;
        }

        public int getCount() {
            // TODO Auto-generated method stub
            return MyArr.size();
        }

        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.edit_pins_column, null);

            }

            final int currentIndex = position;
            //title
            TextView txtUsername = (TextView) convertView.findViewById(R.id.ColLocation);
            txtUsername.setText(MyArr.get(position).get("title"));
            //detail
            TextView txtActivity = (TextView) convertView.findViewById(R.id.ColLocationDetail);
            txtActivity.setText(MyArr.get(position).get("detail"));

            ImageView imgAction = (ImageView)convertView.findViewById(R.id.ColPic);
            ViewGroup.LayoutParams layoutParamsImg = imgAction.getLayoutParams();
            int w = layoutParamsImg.width;
            if(w>0){

//                Log.d("DEBUG","PIN WIDTH = "+imgAction.getMeasuredWidth()+",HEiGHT = "+imgAction.getMeasuredHeight());
                layoutParamsImg.height = layoutParamsImg.width;
                imgAction.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            imgAction.setBackground(null);
                imgAction.setLayoutParams(layoutParamsImg);
            }
            Button btnEditPin = (Button)convertView.findViewById(R.id.btnEditPins);
            Button btnDelPin = (Button)convertView.findViewById(R.id.btnDeletePins);
            if(isPublishPreview){
                Log.d("DEBUG","isPublish Preview");
                btnEditPin.setVisibility(GONE);
                btnDelPin.setVisibility(GONE);
            }
//            Log.d("JSON","Image = "+MyArr.get(position).get("imagepath"));
            Picasso.with(getApplicationContext()).load(MyArr.get(position).get("imagepath")).into(imgAction);
            btnEditPin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EDIT_PLACE_ID = MyArr.get(currentIndex).get("place_id");
                    ismapZoomFirst=true;
                    ScrollView scrollEditPin = (ScrollView)findViewById(R.id.scrollEditPin);
                    scrollEditPin.smoothScrollTo(0,0);
                    TextView lblEditPinHead = (TextView)findViewById(R.id.lblEditPinHead);
                    lblEditPinHead.setText("Edit Pin");
                    RelativeLayout layoutEditPin = (RelativeLayout)findViewById(R.id.layoutEditPin);
                    layoutEditPin.setVisibility(View.VISIBLE);
                    RelativeLayout layoutPreview = (RelativeLayout)findViewById(R.id.layoutAddPinPreview);
                    layoutPreview.setVisibility(GONE);
                    METHOD="/getPlaceEdit";
                    new CallService().execute(AppConfig.apiUrl);
                }
            });
            btnDelPin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(HomeActivity.this)
                            .setTitle("Delete Pin Confirm")
                            .setMessage("Do you really want to this pin?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                    EDIT_PLACE_ID = MyArr.get(currentIndex).get("place_id");
                                    METHOD="/delPlace";
                                    new CallService().execute(AppConfig.apiUrl);
                                }})
                            .setNegativeButton(android.R.string.no, null).show();

                }
            });
            return convertView;

        }

    }
    public class CircleTransform implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }

            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap,
                    BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);

            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }
    }
    public class AnimatedTabHostListener implements TabHost.OnTabChangeListener
    {

        private static final int ANIMATION_TIME = 240;
        private TabHost tabHost;
        private View previousView;
        private View currentView;
        private GestureDetector gestureDetector;
        private int currentTab;

        /**
         * Constructor that takes the TabHost as a parameter and sets previousView to the currentView at instantiation
         *
         * @param context
         * @param tabHost
         */
        public AnimatedTabHostListener(Context context, TabHost tabHost)
        {
            this.tabHost = tabHost;
            this.previousView = tabHost.getCurrentView();
            gestureDetector = new GestureDetector(context, new MyGestureDetector());
            tabHost.setOnTouchListener(new View.OnTouchListener()
            {
                public boolean onTouch(View v, MotionEvent event)
                {
                    if (gestureDetector.onTouchEvent(event))
                    {
                        return false;
                    }
                    else
                    {
                        return true;
                    }
                    //return gestureDetector.onTouchEvent(event);
                }
            });
        }

        /**
         * When tabs change we fetch the current view that we are animating to and animate it and the previous view in the
         * appropriate directions.
         */

        @Override
        public void onTabChanged(String tabId)
        {

            currentView = tabHost.getCurrentView();
            if (tabHost.getCurrentTab() > currentTab)
            {
                previousView.setAnimation(outToLeftAnimation());
                currentView.setAnimation(inFromRightAnimation());
            }
            else
            {
                previousView.setAnimation(outToRightAnimation());
                currentView.setAnimation(inFromLeftAnimation());
            }
            previousView = currentView;
            currentTab = tabHost.getCurrentTab();
                if(TAB1_TAG.equals(tabId)){
                    RelativeLayout layoutFindeUserDetail = (RelativeLayout)findViewById(R.id.layoutUserFindDetail);
                    layoutFindeUserDetail.setVisibility(View.GONE);
                    doLoadDataHome();
                }
                if(TAB2_TAG.equals(tabId)){
                    RelativeLayout layoutFindeUserDetail = (RelativeLayout)findViewById(R.id.layoutUserFindDetail);
                    layoutFindeUserDetail.setVisibility(View.GONE);
                    if(!TAB2_LOADED){
                        //Init Load Explore
                        Log.d("TAB_2","INIT LOAD");
                        doLoadDataExplore();
                        Log.d("TAB_2","End LOAD");
                        TAB2_LOADED=true;
                    }
                }
                if(TAB3_TAG.equals(tabId)){
                    RelativeLayout layoutFindeUserDetail = (RelativeLayout)findViewById(R.id.layoutUserFindDetail);
                    layoutFindeUserDetail.setVisibility(View.GONE);
                    if(!TAB3_LOADED){
                        //Init Load Route
                        setPinTab();
                        TAB3_LOADED=true;
                    }
                }
                if(TAB4_TAG.equals(tabId)){
                    RelativeLayout layoutFindeUserDetail = (RelativeLayout)findViewById(R.id.layoutUserFindDetail);
                    layoutFindeUserDetail.setVisibility(View.GONE);
                    if(!TAB4_LOADED){
                        //Init Load Notification
                        setNotisTab();
                        //doLoadNotisFollowing();
                        TAB4_LOADED=true;
                    }
                    doLoadNotisFollowing();
                }
                if(TAB5_TAG.equals(tabId)){
                    if(!TAB5_LOADED){
                        //Init Load Notification
                        doLoadUserNow();
                        //TAB5_LOADED=true;
                    }
                }

        }

        /**
         * Custom animation that animates in from right
         *
         * @return Animation the Animation object
         */
        private Animation inFromRightAnimation()
        {
            Animation inFromRight = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 1.0f,
                    Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT,
                    0.0f);
            return setProperties(inFromRight);
        }

        /**
         * Custom animation that animates out to the right
         *
         * @return Animation the Animation object
         */
        private Animation outToRightAnimation()
        {
            Animation outToRight = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT,
                    1.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
            return setProperties(outToRight);
        }

        /**
         * Custom animation that animates in from left
         *
         * @return Animation the Animation object
         */
        private Animation inFromLeftAnimation()
        {
            Animation inFromLeft = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, -1.0f,
                    Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT,
                    0.0f);
            return setProperties(inFromLeft);
        }

        /**
         * Custom animation that animates out to the left
         *
         * @return Animation the Animation object
         */
        private Animation outToLeftAnimation()
        {
            Animation outtoLeft = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT,
                    -1.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
            return setProperties(outtoLeft);
        }

        /**
         * Helper method that sets some common properties
         *
         * @param animation
         *            the animation to give common properties
         * @return the animation with common properties
         */
        private Animation setProperties(Animation animation)
        {
            animation.setDuration(ANIMATION_TIME);
            animation.setInterpolator(new AccelerateInterpolator());
            return animation;
        }

        /**
         * A gesture listener that listens for a left or right swipe and uses the swip gesture to navigate a TabHost that
         * uses an AnimatedTabHost listener.
         *
         * @author Daniel Kvist
         *
         */
        class MyGestureDetector extends GestureDetector.SimpleOnGestureListener
        {
            private static final int SWIPE_MIN_DISTANCE = 120;
            private static final int SWIPE_MAX_OFF_PATH = 250;
            private static final int SWIPE_THRESHOLD_VELOCITY = 200;
            private int maxTabs;

            /**
             * An empty constructor that uses the tabhosts content view to decide how many tabs there are.
             */
            public MyGestureDetector()
            {
                maxTabs = tabHost.getTabContentView().getChildCount();
            }

            /**
             * Listens for the onFling event and performs some calculations between the touch down point and the touch up
             * point. It then uses that information to calculate if the swipe was long enough. It also uses the swiping
             * velocity to decide if it was a "true" swipe or just some random touching.
             */
            @Override
            public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY)
            {
                int newTab = 0;
                if (Math.abs(event1.getY() - event2.getY()) <= SWIPE_MAX_OFF_PATH)
                {
                    return false;
                }
                if (event1.getX() - event2.getX() >= SWIPE_MIN_DISTANCE && Math.abs(velocityX) <= SWIPE_THRESHOLD_VELOCITY)
                {
                    // Swipe right to left
                    newTab = currentTab + 1;
                }
                else if (event2.getX() - event1.getX() >= SWIPE_MIN_DISTANCE
                        && Math.abs(velocityX) <= SWIPE_THRESHOLD_VELOCITY)
                {
                    // Swipe left to right
                    newTab = currentTab - 1;
                }
                if (newTab == 0 || newTab <= (maxTabs - 1))
                {
                    return false;
                }
                tabHost.setCurrentTab(newTab);
                return super.onFling(event1, event2, velocityX, velocityY);
            }
        }

    }

    //Call Service to get JSON
    private class CallService  extends AsyncTask<String, Integer, Void> {

        // Required initialization

        //private final HttpClient Client = new DefaultHttpClient();
        private String Content;
        private String Error = null;
        private ProgressDialog Dialog = new ProgressDialog(HomeActivity.this);
        String data ="";
        private boolean isConnected=false;


        protected void onPreExecute() {
            // NOTE: You can call UI Element here.

            //Start Progress Dialog (Message)
            isConnected = CheckNetwork();
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
        HttpPost httpPost ;
        HttpGet httpGet;
        HttpPut httpPut;
        HttpResponse response;
        HttpClient httpClient;
        // Call after onPreExecute method
        protected Void doInBackground(String... urls) {
            //Looper.prepare();
            StringBuilder stringBuilder = new StringBuilder();
            httpClient = new DefaultHttpClient();
            //HttpGet httpGet = new HttpGet(urls[0]+data);

            List<NameValuePair> nameValuePairs;
            if(isConnected) {
                System.setProperty("http.keepAlive", "false");
                // Add your data
                try{
                    Date datenow = new Date();
                    SimpleDateFormat postFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String strDatenow = postFormater.format(datenow);

                    switch (METHOD){
                        case "/addRoute":
                            Log.d("JSONRequest",urls[0]+METHOD);
                            httpPost = new HttpPost(urls[0]+METHOD);
                            Log.d("JSON_SEND","ID = "+USER_ID);
                            Log.d("JSON_SEND","TITLE = "+R_TITLE);
                            Log.d("JSON_SEND","DESC = "+R_DESC);
                            String routeImg = "";
                            Log.d("JSON_DEBUG","IMAGE TEXT = "+routeImg);
                            nameValuePairs = new ArrayList<NameValuePair>(13);
                            nameValuePairs.add(new BasicNameValuePair("user_id", USER_ID));
                            nameValuePairs.add(new BasicNameValuePair("route_detail", R_DESC));
                            nameValuePairs.add(new BasicNameValuePair("route_create", strDatenow));
                            nameValuePairs.add(new BasicNameValuePair("route_like", "0"));
                            nameValuePairs.add(new BasicNameValuePair("route_title", R_TITLE));
                            nameValuePairs.add(new BasicNameValuePair("route_activity", R_ACTIVITY));
                            nameValuePairs.add(new BasicNameValuePair("route_city", R_CITY));
                            nameValuePairs.add(new BasicNameValuePair("route_travel_method", R_TRAVEl_METHOD));
                            nameValuePairs.add(new BasicNameValuePair("route_budgetmin", R_BUDGET_MIN));
                            nameValuePairs.add(new BasicNameValuePair("route_budgetmax", R_BUDGET_MAX));
                            nameValuePairs.add(new BasicNameValuePair("route_suggestion", R_SUGGESTION));
                            nameValuePairs.add(new BasicNameValuePair("route_latitude", "0"));
                            nameValuePairs.add(new BasicNameValuePair("route_longitude", "0"));
                            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                            response = httpClient.execute(httpPost);
                            break;
                        case "/updateUser":
                            Log.d("JSONRequest",urls[0]+METHOD+"/"+USER_ID);
                            httpPost = new HttpPost(urls[0]+METHOD+"/"+USER_ID);
                            nameValuePairs = new ArrayList<NameValuePair>(3);
                            nameValuePairs.add(new BasicNameValuePair("user_name", USER_NAME));
                            nameValuePairs.add(new BasicNameValuePair("user_email", USER_EMAIL));
                            nameValuePairs.add(new BasicNameValuePair("user_phone", USER_PHONE));
                            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                            //httpPut.addHeader("Content-Type","application/x-www-form-urlencoded");
                            response = httpClient.execute(httpPost);
                            break;
                        case "/updateRoute":
                            Log.d("JSONRequest",urls[0]+METHOD+"/"+R_ID);
                            httpPost = new HttpPost(urls[0]+METHOD+"/"+R_ID);
                            nameValuePairs = new ArrayList<NameValuePair>(14);
                            nameValuePairs.add(new BasicNameValuePair("route_id", R_ID));
                            nameValuePairs.add(new BasicNameValuePair("user_id", USER_ID));
                            nameValuePairs.add(new BasicNameValuePair("route_detail", R_DESC));
                            nameValuePairs.add(new BasicNameValuePair("route_create", strDatenow));
                            nameValuePairs.add(new BasicNameValuePair("route_like", "0"));
                            nameValuePairs.add(new BasicNameValuePair("route_title", R_TITLE));
                            nameValuePairs.add(new BasicNameValuePair("route_activity", R_ACTIVITY));
                            nameValuePairs.add(new BasicNameValuePair("route_city", R_CITY));
                            nameValuePairs.add(new BasicNameValuePair("route_travel_method", R_TRAVEl_METHOD));
                            nameValuePairs.add(new BasicNameValuePair("route_budgetmin", R_BUDGET_MIN));
                            nameValuePairs.add(new BasicNameValuePair("route_budgetmax", R_BUDGET_MAX));
                            nameValuePairs.add(new BasicNameValuePair("route_suggestion", R_SUGGESTION));
                            nameValuePairs.add(new BasicNameValuePair("route_latitude", "0"));
                            nameValuePairs.add(new BasicNameValuePair("route_longitude", "0"));
                            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                            //httpPut.addHeader("Content-Type","application/x-www-form-urlencoded");
                            response = httpClient.execute(httpPost);
                            break;
                        case "/updateRouteEdit":
                            Log.d("JSONRequest",urls[0]+METHOD+"/"+R_ID);
                            httpPost = new HttpPost(urls[0]+"/updateRoute/"+PREVIEW_R_ID);
                            nameValuePairs = new ArrayList<NameValuePair>(14);
                            nameValuePairs.add(new BasicNameValuePair("route_id", PREVIEW_R_ID));
                            nameValuePairs.add(new BasicNameValuePair("user_id", USER_ID));
                            nameValuePairs.add(new BasicNameValuePair("route_detail", R_DESC));
                            nameValuePairs.add(new BasicNameValuePair("route_create", strDatenow));
                            nameValuePairs.add(new BasicNameValuePair("route_like", "0"));
                            nameValuePairs.add(new BasicNameValuePair("route_title", R_TITLE));
                            nameValuePairs.add(new BasicNameValuePair("route_activity", R_ACTIVITY));
                            nameValuePairs.add(new BasicNameValuePair("route_city", R_CITY));
                            nameValuePairs.add(new BasicNameValuePair("route_travel_method", R_TRAVEl_METHOD));
                            nameValuePairs.add(new BasicNameValuePair("route_budgetmin", R_BUDGET_MIN));
                            nameValuePairs.add(new BasicNameValuePair("route_budgetmax", R_BUDGET_MAX));
                            nameValuePairs.add(new BasicNameValuePair("route_suggestion", R_SUGGESTION));
                            nameValuePairs.add(new BasicNameValuePair("route_latitude", "0"));
                            nameValuePairs.add(new BasicNameValuePair("route_longitude", "0"));
                            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                            //httpPut.addHeader("Content-Type","application/x-www-form-urlencoded");
                            response = httpClient.execute(httpPost);
                            break;
                        case "/delRoute":
                            httpPost = new HttpPost(urls[0]+METHOD);
                            nameValuePairs = new ArrayList<NameValuePair>(1);
                            nameValuePairs.add(new BasicNameValuePair("route_id", DRAFT_R_ID));
                            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                            response = httpClient.execute(httpPost);
                            break;
                        case "/delPlace":
                            Log.d("JSONRequest",urls[0]+METHOD);
                            httpPost = new HttpPost(urls[0]+METHOD);
                            nameValuePairs = new ArrayList<NameValuePair>(1);
                            nameValuePairs.add(new BasicNameValuePair("place_id", EDIT_PLACE_ID));
                            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                            response = httpClient.execute(httpPost);
                            break;
                        case "/delImageRoute":
                            httpPost = new HttpPost(urls[0]+METHOD);
                            nameValuePairs = new ArrayList<NameValuePair>(1);
                            nameValuePairs.add(new BasicNameValuePair("route_id", PREVIEW_R_ID));
                            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                            response = httpClient.execute(httpPost);
                            break;
                        case "/getRouteAR":
                            Log.d("JSONRequest",urls[0]+METHOD);
                            httpPost = new HttpPost(urls[0]+"/getRoute");
                            nameValuePairs = new ArrayList<NameValuePair>(1);
                            nameValuePairs.add(new BasicNameValuePair("route_id", PREVIEW_R_ID));
                            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                            response = httpClient.execute(httpPost);
                            break;
                        case "/getRouteHome":
                            Log.d("JSONRequest",urls[0]+METHOD);
                            httpPost = new HttpPost(urls[0]+"/getRoute");
                            nameValuePairs = new ArrayList<NameValuePair>(1);
                            nameValuePairs.add(new BasicNameValuePair("route_id", HOME_PREVIEW_R_ID));
                            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                            response = httpClient.execute(httpPost);
                            break;
                        case "/getRouteUser":
                            Log.d("JSONRequest",urls[0]+METHOD);
                            httpPost = new HttpPost(urls[0]+"/getRoute");
                            nameValuePairs = new ArrayList<NameValuePair>(1);
                            nameValuePairs.add(new BasicNameValuePair("route_id", USER_PREVIEW_R_ID));
                            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                            response = httpClient.execute(httpPost);
                            break;
                        case "/getRouteExplore":
                            Log.d("JSONRequest",urls[0]+METHOD);
                            httpPost = new HttpPost(urls[0]+"/getRoute");
                            nameValuePairs = new ArrayList<NameValuePair>(1);
                            nameValuePairs.add(new BasicNameValuePair("route_id", EXPLORE_PREVIEW_R_ID));
                            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                            response = httpClient.execute(httpPost);
                            break;
                        case "/getPlacesByRouteAR":
                            Log.d("JSONRequest",urls[0]+METHOD);
                            httpPost = new HttpPost(urls[0]+"/getPlacesByRoute");
                            nameValuePairs = new ArrayList<NameValuePair>(1);
                            nameValuePairs.add(new BasicNameValuePair("route_id", PREVIEW_R_ID));
                            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                            response = httpClient.execute(httpPost);
                            break;
                        case "/getPlacesByRouteHome":
                            Log.d("JSONRequest",urls[0]+METHOD);
                            httpPost = new HttpPost(urls[0]+"/getPlacesByRoute");
                            nameValuePairs = new ArrayList<NameValuePair>(1);
                            nameValuePairs.add(new BasicNameValuePair("route_id", HOME_PREVIEW_R_ID));
                            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                            response = httpClient.execute(httpPost);
                            break;
                        case "/getPlacesByRouteUser":
                            Log.d("JSONRequest",urls[0]+METHOD);
                            httpPost = new HttpPost(urls[0]+"/getPlacesByRoute");
                            nameValuePairs = new ArrayList<NameValuePair>(1);
                            nameValuePairs.add(new BasicNameValuePair("route_id", USER_PREVIEW_R_ID));
                            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                            response = httpClient.execute(httpPost);
                            break;
                        case "/getPlacesByRouteExplore":
                            Log.d("JSONRequest",urls[0]+METHOD);
                            httpPost = new HttpPost(urls[0]+"/getPlacesByRoute");
                            nameValuePairs = new ArrayList<NameValuePair>(1);
                            nameValuePairs.add(new BasicNameValuePair("route_id", EXPLORE_PREVIEW_R_ID));
                            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                            response = httpClient.execute(httpPost);
                            break;
                        case "/finishRoute":
                            Log.d("JSONRequest",urls[0]+METHOD+"/"+R_ID);
                            httpPost = new HttpPost(urls[0]+METHOD);
                            nameValuePairs = new ArrayList<NameValuePair>(2);
                            nameValuePairs.add(new BasicNameValuePair("route_id", R_ID));
                            nameValuePairs.add(new BasicNameValuePair("route_finish", "1"));
                            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                            response = httpClient.execute(httpPost);
                            break;
                        case "/publishRoute":
                            Log.d("JSONRequest",urls[0]+METHOD+"/"+R_ID);
                            httpPost = new HttpPost(urls[0]+METHOD);
                            nameValuePairs = new ArrayList<NameValuePair>(2);
                            nameValuePairs.add(new BasicNameValuePair("route_id", PREVIEW_R_ID));
                            nameValuePairs.add(new BasicNameValuePair("route_publish", "1"));
                            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                            response = httpClient.execute(httpPost);
                            break;
                        case "/draftRoute":
                            Log.d("JSONRequest",urls[0]+METHOD+"/"+R_ID);
                            httpPost = new HttpPost(urls[0]+METHOD);
                            nameValuePairs = new ArrayList<NameValuePair>(1);
                            nameValuePairs.add(new BasicNameValuePair("route_id", USER_PREVIEW_R_ID));
                            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                            response = httpClient.execute(httpPost);
                            break;
                        case "/listRoutesFeed":
                            httpPost = new HttpPost(urls[0]+"/listRoutesFeedHome");
                        nameValuePairs = new ArrayList<NameValuePair>(1);
                        nameValuePairs.add(new BasicNameValuePair("user_id", USER_ID));
//                        nameValuePairs.add(new BasicNameValuePair("route_publish", "1"));
                        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                            response = httpClient.execute(httpPost);
                            break;
                        case "/listRoutesFeedExplore":
                            Log.d("JSONRequest",urls[0]+METHOD+"/"+R_ID);
                            httpPost = new HttpPost(urls[0]+"/listRoutesFeedExplore");
                            String latitude = "0",longitude = "0";
                            if(currentLocation!=null){
                                latitude = String.valueOf(currentLocation.latitude);
                                longitude = String.valueOf(currentLocation.longitude);
                            }
                        nameValuePairs = new ArrayList<NameValuePair>(2);
                        nameValuePairs.add(new BasicNameValuePair("latitude",latitude ));
                        nameValuePairs.add(new BasicNameValuePair("longitude", longitude));
                        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                            response = httpClient.execute(httpPost);
                            break;
                        case "/listSearchRoutesExplore":
                            httpPost = new HttpPost(urls[0]+"/listRoutesSearchExplore");
                        nameValuePairs = new ArrayList<NameValuePair>(1);
                        nameValuePairs.add(new BasicNameValuePair("searchtext", EXPLORE_SEARCH_TEXT));
//                        nameValuePairs.add(new BasicNameValuePair("route_publish", "1"));
                        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                            response = httpClient.execute(httpPost);
                            break;
                        case "/listRoutesFeedByUserDraft" :
                            httpPost = new HttpPost(urls[0]+"/listRoutesFeedByUser");
                            nameValuePairs = new ArrayList<NameValuePair>(2);
                            nameValuePairs.add(new BasicNameValuePair("user_id", USER_ID));
                            nameValuePairs.add(new BasicNameValuePair("status", "draft"));
                            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                            response = httpClient.execute(httpPost);
                            break;
                        case "/listRoutesFeedByUserGrid":
                            httpPost = new HttpPost(urls[0]+"/listRoutesFeedByUser");
                            nameValuePairs = new ArrayList<NameValuePair>(1);
                            nameValuePairs.add(new BasicNameValuePair("user_id", USER_ID));
                            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                            response = httpClient.execute(httpPost);
                            break;
                        case "/listRoutesFeedByFindUserGrid":
                            httpPost = new HttpPost(urls[0]+"/listRoutesFeedByUser");
                            nameValuePairs = new ArrayList<NameValuePair>(1);
                            nameValuePairs.add(new BasicNameValuePair("user_id", USER_DETAIL_ID));
                            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                            response = httpClient.execute(httpPost);
                            break;
                        case "/listRoutesFeedByUserList":
                            httpPost = new HttpPost(urls[0]+"/listRoutesFeedByUser");
                            nameValuePairs = new ArrayList<NameValuePair>(1);
                            nameValuePairs.add(new BasicNameValuePair("user_id", USER_ID));
                            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                            response = httpClient.execute(httpPost);
                            break;
                        case "/listRoutesFeedByFindUserList":
                            httpPost = new HttpPost(urls[0]+"/listRoutesFeedByUser");
                            nameValuePairs = new ArrayList<NameValuePair>(1);
                            nameValuePairs.add(new BasicNameValuePair("user_id", USER_DETAIL_ID));
                            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                            response = httpClient.execute(httpPost);
                            break;
                        case "/listRoutesFeedByUserListLike":
                            httpPost = new HttpPost(urls[0]+"/listRoutesFeedByUserLike");
                            nameValuePairs = new ArrayList<NameValuePair>(1);
                            nameValuePairs.add(new BasicNameValuePair("user_id", USER_ID));
                            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                            response = httpClient.execute(httpPost);
                            break;
                        case "/listRoutesFeedByUserListFavorite":
                            httpPost = new HttpPost(urls[0]+"/listRoutesFeedByUserFavorite");
                            nameValuePairs = new ArrayList<NameValuePair>(1);
                            nameValuePairs.add(new BasicNameValuePair("user_id", USER_ID));
                            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                            response = httpClient.execute(httpPost);
                            break;
                        case "/usersCurrent":
                            httpGet = new HttpGet(urls[0]+"/user/"+USER_ID);
//                            nameValuePairs = new ArrayList<NameValuePair>(1);
//                            nameValuePairs.add(new BasicNameValuePair("user_id", USER_ID));
//                            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                            response = httpClient.execute(httpGet);
                            break;
                        case "/userFindDetail":
                            httpGet = new HttpGet(urls[0]+"/user/"+USER_DETAIL_ID+"?user_chk_id="+USER_ID);
//                            nameValuePairs = new ArrayList<NameValuePair>(1);
//                            nameValuePairs.add(new BasicNameValuePair("user_chk_id", USER_ID));
//                            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                            response = httpClient.execute(httpGet);
                            break;
                        case "/user/search":
                            httpGet = new HttpGet(urls[0]+METHOD+"/"+FIND_F_TXT+"?by="+USER_ID);
                            response = httpClient.execute(httpGet);
                            break;
                        case "/getPlaceEdit":
                            httpPost = new HttpPost(urls[0]+"/getPlace");
                            nameValuePairs = new ArrayList<NameValuePair>(1);
                            nameValuePairs.add(new BasicNameValuePair("place_id", EDIT_PLACE_ID));
                            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                            response = httpClient.execute(httpPost);
                            break;
                        case "/getPlaceExplorePreview" :
                            httpPost = new HttpPost(urls[0]+"/getPlace");
                            nameValuePairs = new ArrayList<NameValuePair>(1);
                            nameValuePairs.add(new BasicNameValuePair("place_id", EXPLORE_PREVIEW_PIN_ID));
                            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                            response = httpClient.execute(httpPost);
                            break;
                        case "/getPlaceHomePreview" :
                            httpPost = new HttpPost(urls[0]+"/getPlace");
                            nameValuePairs = new ArrayList<NameValuePair>(1);
                            nameValuePairs.add(new BasicNameValuePair("place_id", HOME_PREVIEW_PIN_ID));
                            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                            response = httpClient.execute(httpPost);
                            break;
                        case "/getPlaceUserPreview" :
                            httpPost = new HttpPost(urls[0]+"/getPlace");
                            nameValuePairs = new ArrayList<NameValuePair>(1);
                            nameValuePairs.add(new BasicNameValuePair("place_id", USER_PREVIEW_PIN_ID));
                            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                            response = httpClient.execute(httpPost);
                            break;
                        case "/addPlace":

                            httpPost = new HttpPost(urls[0]+METHOD);
                            nameValuePairs = new ArrayList<NameValuePair>(8);
                            nameValuePairs.add(new BasicNameValuePair("user_id", USER_ID));
                            nameValuePairs.add(new BasicNameValuePair("route_id", R_ID));
                            nameValuePairs.add(new BasicNameValuePair("place_name", P_NAME));
                            nameValuePairs.add(new BasicNameValuePair("place_detail", P_DESC));
                            nameValuePairs.add(new BasicNameValuePair("place_create", strDatenow));
                            nameValuePairs.add(new BasicNameValuePair("place_like", "0"));
                            nameValuePairs.add(new BasicNameValuePair("place_latitude", String.valueOf(latLng.latitude)));
                            nameValuePairs.add(new BasicNameValuePair("place_longitude", String.valueOf(latLng.longitude)));
                            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                            response = httpClient.execute(httpPost);
                            break;
                        case "/updatePlace":
                            httpPost = new HttpPost(urls[0]+METHOD+"/"+EDIT_PLACE_ID);
                            HashMap<String,String> hmPlace = allPinsEdit.get(0);
                            hmPlace.put("place_latitude",String.valueOf(latLng.latitude));
                            hmPlace.put("place_longitude",String.valueOf(latLng.longitude));
                            nameValuePairs = new ArrayList<NameValuePair>(7);
                            nameValuePairs.add(new BasicNameValuePair("place_id", EDIT_PLACE_ID));
                            nameValuePairs.add(new BasicNameValuePair("place_name", P_NAME));
                            nameValuePairs.add(new BasicNameValuePair("place_detail", P_DESC));
                            nameValuePairs.add(new BasicNameValuePair("place_like", "0"));
                            nameValuePairs.add(new BasicNameValuePair("place_latitude", hmPlace.get("place_latitude")));
                            nameValuePairs.add(new BasicNameValuePair("place_longitude", hmPlace.get("place_longitude")));
                            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                            response = httpClient.execute(httpPost);
                            break;
                        case "/addPlaceEdit":
                            httpPost = new HttpPost(urls[0]+"/addPlace");
                            nameValuePairs = new ArrayList<NameValuePair>(8);
                            nameValuePairs.add(new BasicNameValuePair("user_id", USER_ID));
                            nameValuePairs.add(new BasicNameValuePair("route_id", PREVIEW_R_ID));
                            nameValuePairs.add(new BasicNameValuePair("place_name", P_NAME));
                            nameValuePairs.add(new BasicNameValuePair("place_detail", P_DESC));
                            nameValuePairs.add(new BasicNameValuePair("place_create", strDatenow));
                            nameValuePairs.add(new BasicNameValuePair("place_like", "0"));
                            nameValuePairs.add(new BasicNameValuePair("place_latitude", String.valueOf(latLng.latitude)));
                            nameValuePairs.add(new BasicNameValuePair("place_longitude", String.valueOf(latLng.longitude)));
                            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                            response = httpClient.execute(httpPost);
                            break;
                        case "/delImagePlace":
                            httpPost = new HttpPost(urls[0]+METHOD);
                            nameValuePairs = new ArrayList<NameValuePair>(1);
                            nameValuePairs.add(new BasicNameValuePair("place_id", EDIT_PLACE_ID));
                            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                            response = httpClient.execute(httpPost);
                            break;
                        case "/findfriendFollow":
                            httpPost = new HttpPost(urls[0]+"/addFollowUser");
                            nameValuePairs = new ArrayList<NameValuePair>(2);
                            nameValuePairs.add(new BasicNameValuePair("user_id", USER_ID));
                            nameValuePairs.add(new BasicNameValuePair("follow_user_id", FOLLOW_USER_ID));
                            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                            response = httpClient.execute(httpPost);
                            break;
                        case "/addLikeHome":
                            String likeRouteID = globalHomeFeedList.get(LIKE_CURRENT_INDEX).get("route_id");
                            httpPost = new HttpPost(urls[0]+"/addLikeRoute");
                            nameValuePairs = new ArrayList<NameValuePair>(2);
                            nameValuePairs.add(new BasicNameValuePair("user_id", USER_ID));
                            nameValuePairs.add(new BasicNameValuePair("route_id", likeRouteID));
                            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                            response = httpClient.execute(httpPost);
                            break;
                        case "/delLikeHome":
                            String unlikeRouteID = globalHomeFeedList.get(LIKE_CURRENT_INDEX).get("route_id");
                            httpPost = new HttpPost(urls[0]+"/deleteLikeRoute");
                            nameValuePairs = new ArrayList<NameValuePair>(2);
                            nameValuePairs.add(new BasicNameValuePair("user_id", USER_ID));
                            nameValuePairs.add(new BasicNameValuePair("route_id", unlikeRouteID));
                            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                            response = httpClient.execute(httpPost);
                            break;
                        case "/listNotificationFollowing":
                            httpPost = new HttpPost(urls[0]+"/listNotification");
                            nameValuePairs = new ArrayList<NameValuePair>(2);
                            nameValuePairs.add(new BasicNameValuePair("user_id", USER_ID));
                            nameValuePairs.add(new BasicNameValuePair("type", "follow"));
//                        nameValuePairs.add(new BasicNameValuePair("route_publish", "1"));
                            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                            response = httpClient.execute(httpPost);
                            break;
                        case "/listNotificationYou":
                            httpPost = new HttpPost(urls[0]+"/listNotification");
                            nameValuePairs = new ArrayList<NameValuePair>(2);
                            nameValuePairs.add(new BasicNameValuePair("user_id", USER_ID));
                            nameValuePairs.add(new BasicNameValuePair("type", "you"));
//                        nameValuePairs.add(new BasicNameValuePair("route_publish", "1"));
                            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                            response = httpClient.execute(httpPost);
                            break;

                    }
                }catch (UnsupportedEncodingException ex){
                    Log.d("ERROR",ex.getMessage());
                }
                catch (Exception e){
                    Log.d("readJSONFeed", e.getLocalizedMessage());
                    Error = e.getLocalizedMessage();
                }
                try {

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
                        //Toast.makeText(getApplication(), "Failed to download file",
                        // Toast.LENGTH_LONG).show();
                        Log.d("JSON", "Failed to download file -> "+String.valueOf(statusCode));
                        Log.d("JSON", "Failed to download file -> "+String.valueOf(statusLine.getReasonPhrase()));
                    }
                    Content = stringBuilder.toString();
                } catch (Exception e) {
                    Log.d("readJSONFeed", e.getLocalizedMessage());
                    Error = e.getLocalizedMessage();
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
                    HashMap<String,String> newMarker;
                    HashMap<String, String> map;
                    JSONArray jsonArray;
                    /****** Creates a new JSONObject with name/value mappings from the JSON string. ********/
                    jsonResponse = new JSONObject(Content);
                    JSONObject jsonErrors = new JSONObject(jsonResponse.getString("errors"));
                    if(jsonErrors.getString("status").equals("200")) {
                        switch (METHOD){
                            case "/addRoute":
                                //Call Google Map
                                PINTAB2_LOADED=false;
                                R_ID = jsonResponse.getString("route_id");
                                loadPermissions(Manifest.permission.ACCESS_FINE_LOCATION,REQUEST_FINE_LOCATION);
                                LinearLayout layoutAddPlace = (LinearLayout)findViewById(R.id.layoutStartPin);
                                RelativeLayout layoutCreatePin = (RelativeLayout)findViewById(R.id.layoutAddPin);
                                layoutAddPlace.setVisibility(GONE);
                                layoutCreatePin.setVisibility(View.VISIBLE);
                                Display display = getWindowManager().getDefaultDisplay();
                                Point size = new Point();
                                display.getSize(size);
                                int height = size.y;
                                LinearLayout layoutMap = (LinearLayout)findViewById(R.id.layoutMap);
                                layoutMap.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height-350));
                                doUploadRouteImage();
                                break;
                            case "/updateUser":
                                RelativeLayout layoutEditProfile = (RelativeLayout)findViewById(R.id.layoutEditProfile);
                                layoutEditProfile.setVisibility(View.GONE);
                                Toast.makeText(getApplication(), "Save Profile Successful.",
                                        Toast.LENGTH_LONG).show();
                                break;
                            case "/updateRoute":
                                //Call Google Map
                                R_ID = jsonResponse.getString("route_id");
                                loadPermissions(Manifest.permission.ACCESS_FINE_LOCATION,REQUEST_FINE_LOCATION);
                                LinearLayout layoutAddPlace1 = (LinearLayout)findViewById(R.id.layoutStartPin);
                                RelativeLayout layoutCreatePin1 = (RelativeLayout)findViewById(R.id.layoutAddPin);
                                layoutAddPlace1.setVisibility(GONE);
                                layoutCreatePin1.setVisibility(View.VISIBLE);
                                Display display1 = getWindowManager().getDefaultDisplay();
                                Point size1 = new Point();
                                display1.getSize(size1);
                                int height1 = size1.y;
                                LinearLayout layoutMap1 = (LinearLayout)findViewById(R.id.layoutMap);
                                layoutMap1.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height1-350));
                                doUploadRouteImage();
                                break;
                            case "/updateRouteEdit":
                                doUploadRouteImageEdit();
                                Toast.makeText(getApplication(), "Save Route Successful.",
                                        Toast.LENGTH_LONG).show();
                                break;
                            case "/delRoute":
                                Toast.makeText(getApplication(), "Delete Route Successful.",
                                        Toast.LENGTH_LONG).show();
                                doLoadPinDraft();
                                break;
                            case "/delPlace":
                                Toast.makeText(getApplication(), "Delete Place Successful.",
                                        Toast.LENGTH_LONG).show();
                                EDIT_PLACE_ID="";
                                doLoadPreviewPins();
                                break;
                            case "/delImageRoute":

                                break;
                            case "/addPlace":
//                                layoutCreatePin = (RelativeLayout)findViewById(R.id.layoutAddPin);
//                                layoutCreatePin.setVisibility(View.GONE);
                                ClearAddPinView();
                                ClearAddPinValue();
                                newMarker = new HashMap<String,String>();
                                newMarker.put("place_id",jsonResponse.getString("place_id"));
                                newMarker.put("place_name",jsonResponse.getString("place_name"));
                                newMarker.put("place_latitude",jsonResponse.getString("place_latitude"));
                                newMarker.put("place_longitude",jsonResponse.getString("place_longitude"));
                                LAST_PLACE_ID = jsonResponse.getString("place_id");
                                allPinsAdd.add(newMarker);
                                updateMapMarker();
                                Toast.makeText(getApplication(), "Save Pin Successful.",
                                        Toast.LENGTH_LONG).show();
                                doUploadPlaceImage();
                                break;
                            case "/addPlaceEdit":
                                EDIT_PLACE_ID = jsonResponse.getString("place_id");
                                newMarker = new HashMap<String,String>();
                                newMarker.put("place_id",jsonResponse.getString("place_id"));
                                newMarker.put("place_name",jsonResponse.getString("place_name"));
                                newMarker.put("place_latitude",jsonResponse.getString("place_latitude"));
                                newMarker.put("place_longitude",jsonResponse.getString("place_longitude"));
//                                allPinsEdit.add(newMarker);
//                                updateMapMarkerEdit();
                                Toast.makeText(getApplication(), "Save Pin Successful.",
                                        Toast.LENGTH_LONG).show();
                                doUploadPlaceImageEdit();
                                break;
                            case "/delImagePlace":

                                break;
                            case "/updatePlace":
                                Toast.makeText(getApplication(), "Save Pin Successful.",
                                        Toast.LENGTH_LONG).show();
                                updateMapMarkerEdit();
                                doUploadPlaceImageEdit();
                                break;
                            case "/finishRoute":
                                PREVIEW_R_ID = R_ID;
                                isPreviewFromAdd=true;
                                R_ID="";
                                ClearAddPinValue();
                                ClearAddRouteValue();
                                RelativeLayout layoutAddPin = (RelativeLayout)findViewById(R.id.layoutAddPin);
                                RelativeLayout layoutCreatePreviewPin = (RelativeLayout)findViewById(R.id.layoutAddPinPreview);
                                layoutCreatePreviewPin.setVisibility(View.VISIBLE);
                                RelativeLayout layoutFinishPin = (RelativeLayout)findViewById(R.id.layoutFinishAddPin);
                                layoutFinishPin.setVisibility(GONE);
                                layoutAddPin.setVisibility(GONE);
                                ImageButton btnFinishPin = (ImageButton)findViewById(R.id.btnFinishPin);
                                btnFinishPin.setImageResource(R.drawable.ic_finish_pin);
                                ClearAddPinMap();
                                setAddPinPreviewTab();
                                doLoadDataPreviewOverview();
                                break;
                            case "/publishRoute":
                                TAB5_LOADED=false;
                                Button btnPublish = (Button)findViewById(R.id.btnPublishPreview);
                                btnPublish.setVisibility(GONE);
                                Toast.makeText(getApplication(), "Your route have been published.",
                                        Toast.LENGTH_LONG).show();
                                if(isPublishFromDraft){
                                    doLoadPinDraft();
                                    isPublishFromDraft=false;
                                }
                                else{
                                    Button btnEditRoute = (Button)findViewById(R.id.btnEditRoute);
                                    btnEditRoute.setVisibility(GONE);
                                    Button btnAddPinEdit = (Button)findViewById(R.id.btnAddPinEdit);
                                    btnAddPinEdit.setVisibility(GONE);
                                    isPublishPreview=true;
                                    doLoadPreviewPins();
                                }
                                break;
                            case "/draftRoute":
                                Toast.makeText(getApplication(), "Your route have been drafted.",
                                        Toast.LENGTH_LONG).show();
                                doLoadDataUserList();
                                break;
                            case "/getRouteAR" :
                                ScrollView scCreateRoute = (ScrollView)findViewById(R.id.scrollPreviewOverview);
                                scCreateRoute.smoothScrollTo(0,0);
                                Log.d("JSON_LIST","Route Title = "+jsonResponse.getString("route_title"));
                                Log.d("JSON_LIST","Route Detail = "+jsonResponse.getString("route_detail"));
                                TextView txtTitle = (TextView)findViewById(R.id.lblCreatePinOverviewTitle);
                                TextView txtDetail = (TextView)findViewById(R.id.lblCreatePinOverviewDetail);
                                txtTitle.setText(jsonResponse.getString("route_title"));
                                txtDetail.setText(jsonResponse.getString("route_detail"));
                                if(jsonResponse.getString("route_publish").equals("0")){

                                    Button btnPublishAR = (Button)findViewById(R.id.btnPublishPreview);
                                    btnPublishAR.setVisibility(View.VISIBLE);
                                    Button btnEditRoute = (Button)findViewById(R.id.btnEditRoute);
                                    btnEditRoute.setVisibility(View.VISIBLE);
                                    Button btnAddPinEdit = (Button)findViewById(R.id.btnAddPinEdit);
                                    btnAddPinEdit.setVisibility(View.VISIBLE);
                                }
                                if(jsonResponse.getString("route_publish").equals("1")){
                                    Button btnEditRoute = (Button)findViewById(R.id.btnEditRoute);
                                    btnEditRoute.setVisibility(GONE);
                                    isPublishPreview=true;
                                }
                                ImageView imgCoverAR = (ImageView)findViewById(R.id.imgCreatePinOverview);
                                JSONArray imgPreviewARList = new JSONArray(jsonResponse.getString("route_img"));
                                String imgPreviewARUrl = AppConfig.noImgUrl;
                                if(imgPreviewARList.length()>0){
                                    JSONObject jsonObjImgCoverAR = imgPreviewARList.getJSONObject(0);
                                    imgPreviewARUrl=AppConfig.imageUrl+"/"+jsonObjImgCoverAR.getString("img_text");
                                }
                                ViewGroup.LayoutParams layoutCoverParamsAR = imgCoverAR.getLayoutParams();
                                layoutCoverParamsAR.height = Resources.getSystem().getDisplayMetrics().widthPixels;
                                imgCoverAR.setLayoutParams(layoutCoverParamsAR);
                                imgCoverAR.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                Picasso.with(HomeActivity.this).load(imgPreviewARUrl).into(imgCoverAR);

                                MyArrList = new ArrayList<HashMap<String, String>>();
                                JSONArray imgPreviewPinARList = new JSONArray(jsonResponse.getString("route_pin"));
                                if(imgPreviewPinARList.length()>0){
                                    for(int i = 0;i<imgPreviewPinARList.length();i++){
                                        JSONObject jsonObjImgPvBottom = imgPreviewPinARList.getJSONObject(i);
                                        map = new HashMap<String, String>();
                                        map.put("imagepath", AppConfig.imageUrl+"/"+jsonObjImgPvBottom.getString("img_text"));
                                        MyArrList.add(map);

                                    }
                                }
                                // grid popular
                                final GridView gridPreviewAR = (GridView)findViewById(R.id.gridCreatePinOverview);
                                if(MyArrList.size()>0){
                                    gridPreviewAR.setAdapter(new RouteGridFeedAdapter(HomeActivity.this,MyArrList));
                                    final MainUtility mUtilityAR = new MainUtility();

                                    new android.os.Handler().postDelayed(
                                            new Runnable() {
                                                public void run() {
                                                    mUtilityAR.setGridViewHeightBasedOnChildren(gridPreviewAR,4);
                                                }
                                            }, 500);
                                }else{
                                    gridPreviewAR.setAdapter(null);
                                    ViewGroup.LayoutParams layoutPreviewAR = gridPreviewAR.getLayoutParams();
                                    layoutPreviewAR.height=0;
                                    gridPreviewAR.setLayoutParams(layoutPreviewAR);
                                }

                                /* EDIT Field */
                                EditText txtTitleEdit = (EditText)findViewById(R.id.txtTitleEdit);
                                EditText txtRouteDescEdit = (EditText)findViewById(R.id.txtRouteDescEdit);
                                Spinner txtActivityEdit = (Spinner)findViewById(R.id.txtActivityEdit);
//                                EditText txtCountryEdit = (EditText)findViewById(R.id.txtCountryEdit);
                                EditText txtCityEdit = (EditText)findViewById(R.id.txtCityEdit);
                                Spinner txtTravelEdit = (Spinner)findViewById(R.id.txtTravelMethodEdit);
//                                EditText txtTimeFrameEdit = (EditText)findViewById(R.id.txtTimeframeEdit);
                                EditText txtBudgetMinEdit = (EditText)findViewById(R.id.txtBudgetMinEdit);
                                EditText txtBudgetMaxEdit = (EditText)findViewById(R.id.txtBudgetMaxEdit);
                                EditText txtSuggestionEdit = (EditText)findViewById(R.id.txtSuggestionEdit);
                                ImageButton btnAddPicEdit = (ImageButton) findViewById(R.id.btnAddPicEdit);
                                txtTitleEdit.setText(jsonResponse.getString("route_title"));
                                txtRouteDescEdit.setText(jsonResponse.getString("route_detail"));
                                txtActivityEdit.setSelection(getIndex(txtActivityEdit,jsonResponse.getString("route_activity")));
                                txtCityEdit.setText(jsonResponse.getString("route_city"));
                                txtTravelEdit.setSelection(getIndex(txtTravelEdit,jsonResponse.getString("route_travel_method")));
                                txtBudgetMinEdit.setText(jsonResponse.getString("route_budgetmin"));
                                txtBudgetMaxEdit.setText(jsonResponse.getString("route_budgetmax"));
                                txtSuggestionEdit.setText(jsonResponse.getString("route_suggestion"));
                                if(imgPreviewARList.length()>0){
                                    JSONObject jsonObjImgCoverAR = imgPreviewARList.getJSONObject(0);
                                    HashMap<String,String> hashPicEdit = new HashMap<String,String>();
                                    hashPicEdit.put("route_img_id",jsonObjImgCoverAR.getString("img_id"));
                                    hashPicEdit.put("route_img",AppConfig.imageUrl+"/"+jsonObjImgCoverAR.getString("img_text"));
                                    allImageRouteEdit.add(hashPicEdit);
                                    Picasso.with(HomeActivity.this).load(AppConfig.imageUrl+"/"+jsonObjImgCoverAR.getString("img_text")).into(btnAddPicEdit);
                                }
                                break;
                            case "/getRouteHome" :
                                ScrollView scHomeRoute = (ScrollView)findViewById(R.id.scrollHomeOverview);
                                scHomeRoute.smoothScrollTo(0,0);
                                TextView txtTitleHome = (TextView)findViewById(R.id.lblHomeOverviewTitle);
                                TextView txtDetailHome = (TextView)findViewById(R.id.lblHomeOverviewDetail);
                                txtTitleHome.setText(jsonResponse.getString("route_title"));
                                txtDetailHome.setText(jsonResponse.getString("route_detail"));
                                ImageView imgCoverHome = (ImageView)findViewById(R.id.imgHomeOverview);
                                JSONArray imgPreviewList = new JSONArray(jsonResponse.getString("route_img"));
                                String imgPreviewUrl = AppConfig.noImgUrl;
                                if(imgPreviewList.length()>0){
                                    JSONObject jsonObjImgCover = imgPreviewList.getJSONObject(0);
                                    imgPreviewUrl=AppConfig.imageUrl+"/"+jsonObjImgCover.getString("img_text");
                                }
                                ViewGroup.LayoutParams layoutCoverParams = imgCoverHome.getLayoutParams();
                                layoutCoverParams.height = Resources.getSystem().getDisplayMetrics().widthPixels;
                                imgCoverHome.setLayoutParams(layoutCoverParams);
                                imgCoverHome.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                Picasso.with(HomeActivity.this).load(imgPreviewUrl).into(imgCoverHome);
                                MyArrList = new ArrayList<HashMap<String, String>>();
                                JSONArray imgPreviewPinHomeList = new JSONArray(jsonResponse.getString("route_pin"));
                                if(imgPreviewPinHomeList.length()>0){
                                    for(int i = 0;i<imgPreviewPinHomeList.length();i++){
                                        JSONObject jsonObjImgPvBottom = imgPreviewPinHomeList.getJSONObject(i);
                                        map = new HashMap<String, String>();
                                        map.put("place_id",jsonObjImgPvBottom.getString("place_id"));
                                        map.put("imagepath", AppConfig.imageUrl+"/"+jsonObjImgPvBottom.getString("img_text"));
                                        MyArrList.add(map);
                                    }
                                }
                                // grid popular
                                final GridView gridPreviewHome = (GridView)findViewById(R.id.gridHomeOverview);
                                if(MyArrList.size()>0){
                                    gridPreviewHome.setAdapter(new RouteGridHomeFeedAdapter(HomeActivity.this,MyArrList));
                                    final MainUtility mUtilityHome = new MainUtility();

                                    new android.os.Handler().postDelayed(
                                            new Runnable() {
                                                public void run() {
                                                    mUtilityHome.setGridViewHeightBasedOnChildren(gridPreviewHome,4);
                                                }
                                            }, 500);
                                }else{
                                    gridPreviewHome.setAdapter(null);
                                    ViewGroup.LayoutParams layoutPreview = gridPreviewHome.getLayoutParams();
                                    layoutPreview.height=0;
                                    gridPreviewHome.setLayoutParams(layoutPreview);
                                }

                                break;
                            case "/getRouteUser":
                                ScrollView scUserRoute = (ScrollView)findViewById(R.id.scrollUserOverview);
                                scUserRoute.smoothScrollTo(0,0);
                                TextView txtTitleUser = (TextView)findViewById(R.id.lblUserOverviewTitle);
                                TextView txtDetailUser = (TextView)findViewById(R.id.lblUserOverviewDetail);
                                txtTitleUser.setText(jsonResponse.getString("route_title"));
                                txtDetailUser.setText(jsonResponse.getString("route_detail"));
                                ImageView imgCoverUser = (ImageView)findViewById(R.id.imgUserOverview);
                                JSONArray imgPreviewUserList = new JSONArray(jsonResponse.getString("route_img"));
                                String imgPreviewUserUrl = AppConfig.noImgUrl;
                                if(imgPreviewUserList.length()>0){
                                    JSONObject jsonObjImgCover = imgPreviewUserList.getJSONObject(0);
                                    imgPreviewUserUrl=AppConfig.imageUrl+"/"+jsonObjImgCover.getString("img_text");
                                }
                                ViewGroup.LayoutParams layoutCoverUserParams = imgCoverUser.getLayoutParams();
                                layoutCoverUserParams.height = Resources.getSystem().getDisplayMetrics().widthPixels;
                                imgCoverUser.setLayoutParams(layoutCoverUserParams);
                                imgCoverUser.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                Picasso.with(HomeActivity.this).load(imgPreviewUserUrl).into(imgCoverUser);
                                MyArrList = new ArrayList<HashMap<String, String>>();
                                JSONArray imgPreviewPinUserList = new JSONArray(jsonResponse.getString("route_pin"));
                                if(imgPreviewPinUserList.length()>0){
                                    for(int i = 0;i<imgPreviewPinUserList.length();i++){
                                        JSONObject jsonObjImgPvBottom = imgPreviewPinUserList.getJSONObject(i);
                                        map = new HashMap<String, String>();
                                        map.put("place_id",jsonObjImgPvBottom.getString("place_id"));
                                        map.put("imagepath", AppConfig.imageUrl+"/"+jsonObjImgPvBottom.getString("img_text"));
                                        MyArrList.add(map);
                                    }
                                }
                                // grid popular
                                final GridView gridPreviewUser = (GridView)findViewById(R.id.gridUserOverview);
                                if(MyArrList.size()>0){
                                    gridPreviewUser.setAdapter(new RouteGridUserFeedAdapter(HomeActivity.this,MyArrList));
                                    final MainUtility mUtilityHome = new MainUtility();

                                    new android.os.Handler().postDelayed(
                                            new Runnable() {
                                                public void run() {
                                                    mUtilityHome.setGridViewHeightBasedOnChildren(gridPreviewUser,4);
                                                }
                                            }, 500);
                                }else{
                                    gridPreviewUser.setAdapter(null);
                                    ViewGroup.LayoutParams layoutPreview = gridPreviewUser.getLayoutParams();
                                    layoutPreview.height=0;
                                    gridPreviewUser.setLayoutParams(layoutPreview);
                                }
                                break;
                            case "/getRouteExplore":
                                ScrollView scExploreRoute = (ScrollView)findViewById(R.id.scrollExploreOverview);
                                scExploreRoute.smoothScrollTo(0,0);
                                TextView txtTitleExplore = (TextView)findViewById(R.id.lblExploreOverviewTitle);
                                TextView txtDetailExplore = (TextView)findViewById(R.id.lblExploreOverviewDetail);
                                txtTitleExplore.setText(jsonResponse.getString("route_title"));
                                txtDetailExplore.setText(jsonResponse.getString("route_detail"));
                                ImageView imgCoverExplore = (ImageView)findViewById(R.id.imgExploreOverview);
                                JSONArray imgPreviewExploreList = new JSONArray(jsonResponse.getString("route_img"));
                                String imgPreviewExploreUrl = AppConfig.noImgUrl;
                                if(imgPreviewExploreList.length()>0){
                                    JSONObject jsonObjImgCover = imgPreviewExploreList.getJSONObject(0);
                                    imgPreviewExploreUrl=AppConfig.imageUrl+"/"+jsonObjImgCover.getString("img_text");
                                }
                                ViewGroup.LayoutParams layoutCoverExploreParams = imgCoverExplore.getLayoutParams();
                                layoutCoverExploreParams.height = Resources.getSystem().getDisplayMetrics().widthPixels;
                                imgCoverExplore.setLayoutParams(layoutCoverExploreParams);
                                imgCoverExplore.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                Picasso.with(HomeActivity.this).load(imgPreviewExploreUrl).into(imgCoverExplore);
                                MyArrList = new ArrayList<HashMap<String, String>>();
                                JSONArray imgPreviewPinExploreList = new JSONArray(jsonResponse.getString("route_pin"));
                                if(imgPreviewPinExploreList.length()>0){
                                    for(int i = 0;i<imgPreviewPinExploreList.length();i++){
                                        JSONObject jsonObjImgPvBottom = imgPreviewPinExploreList.getJSONObject(i);
                                        map = new HashMap<String, String>();
                                        map.put("place_id",jsonObjImgPvBottom.getString("place_id"));
                                        map.put("imagepath", AppConfig.imageUrl+"/"+jsonObjImgPvBottom.getString("img_text"));
                                        MyArrList.add(map);
                                    }
                                }
                                // grid popular
                                final GridView gridPreviewExplore = (GridView)findViewById(R.id.gridExploreOverview);
                                if(MyArrList.size()>0){
                                    gridPreviewExplore.setAdapter(new RouteGridExploreFeedAdapter(HomeActivity.this,MyArrList));
                                    final MainUtility mUtilityHome = new MainUtility();

                                    new android.os.Handler().postDelayed(
                                            new Runnable() {
                                                public void run() {
                                                    mUtilityHome.setGridViewHeightBasedOnChildren(gridPreviewExplore,4);
                                                }
                                            }, 500);
                                }else{
                                    gridPreviewExplore.setAdapter(null);
                                    ViewGroup.LayoutParams layoutPreview = gridPreviewExplore.getLayoutParams();
                                    layoutPreview.height=0;
                                    gridPreviewExplore.setLayoutParams(layoutPreview);
                                }
                                break;
                            case "/getPlaceEdit":
                                allImagePinsEdit = new ArrayList<HashMap<String, String>>();
                                ScrollView scEditPin = (ScrollView)findViewById(R.id.scrollEditPin);
                                scEditPin.smoothScrollTo(0,0);
                                EditText txtPinname = (EditText)findViewById(R.id.txtPinnameEdit);
                                EditText txtPindesc = (EditText)findViewById(R.id.txtPinDescEdit);
                                ImageButton btnEditPicPin = (ImageButton)findViewById(R.id.btnEditPinImage);
                                txtPinname.setText(jsonResponse.getString("place_name"));
                                txtPindesc.setText(jsonResponse.getString("place_detail"));
                                JSONArray imgPreviewPlace = new JSONArray(jsonResponse.getString("place_img"));
                                String imgPreviewPlaceUrl = AppConfig.noImgUrl;
                                if(imgPreviewPlace.length()>0){
                                    JSONObject jsonObjImgCover = imgPreviewPlace.getJSONObject(0);
                                    imgPreviewPlaceUrl=AppConfig.imageUrl+"/"+jsonObjImgCover.getString("img_text");
                                    HashMap<String,String> hashPicEdit = new HashMap<String,String>();
                                    hashPicEdit.put("place_img_id",jsonObjImgCover.getString("img_id"));
                                    hashPicEdit.put("place_img",imgPreviewPlaceUrl);
                                    allImagePinsEdit.add(hashPicEdit);
                                    Picasso.with(HomeActivity.this).load(imgPreviewPlaceUrl).into(btnEditPicPin);
                                }else{
                                    btnEditPicPin.setImageResource(R.drawable.cover_img_plus);
                                }

                                newMarker = new HashMap<String,String>();
                                newMarker.put("place_id",jsonResponse.getString("place_id"));
                                newMarker.put("place_name",jsonResponse.getString("place_name"));
                                newMarker.put("place_latitude",jsonResponse.getString("place_latitude"));
                                newMarker.put("place_longitude",jsonResponse.getString("place_longitude"));
                                allPinsEdit.add(newMarker);
                                loadPermissionsMapEdit(Manifest.permission.ACCESS_FINE_LOCATION,REQUEST_FINE_LOCATION);
                                break;
                            case "/getPlaceExplorePreview" :
                                RelativeLayout layoutExplorePinPreview = (RelativeLayout)findViewById(R.id.layoutExplorePinPreview);
                                RelativeLayout layoutExplorePreview = (RelativeLayout)findViewById(R.id.layoutExplorePreview);
                                layoutExplorePinPreview.setVisibility(View.VISIBLE);
                                layoutExplorePreview.setVisibility(GONE);

                                TextView lblExplorePinPreviewHead = (TextView)findViewById(R.id.lblExplorePinPreviewHead);
                                TextView lblExplorePinPreviewDesc = (TextView)findViewById(R.id.lblExplorePinPreviewDesc);
                                ImageView imgExplorePinPreview = (ImageView)findViewById(R.id.imgExplorePinPreviewImage);
                                lblExplorePinPreviewHead.setText(jsonResponse.getString("place_name"));
                                lblExplorePinPreviewDesc.setText(jsonResponse.getString("place_detail"));
                                LATLNG_PIN_PREVIEW = new LatLng(Double.parseDouble(jsonResponse.getString("place_latitude")),Double.parseDouble(jsonResponse.getString("place_longitude")));
                                JSONArray imgExplorePreviewPlace = new JSONArray(jsonResponse.getString("place_img"));
                                String imgExplorePreviewPlaceUrl = AppConfig.noImgUrl;
                                if(imgExplorePreviewPlace.length()>0){
                                    JSONObject jsonObjImgCover = imgExplorePreviewPlace.getJSONObject(0);
                                    imgExplorePreviewPlaceUrl=AppConfig.imageUrl+"/"+jsonObjImgCover.getString("img_text");
                                }
                                Picasso.with(HomeActivity.this).load(imgExplorePreviewPlaceUrl).into(imgExplorePinPreview);
                                loadPermissionsMapExploreViewPin(Manifest.permission.ACCESS_FINE_LOCATION,REQUEST_FINE_LOCATION);
                                break;
                            case "/getPlaceHomePreview" :
                                RelativeLayout layoutHomePinPreview = (RelativeLayout)findViewById(R.id.layoutHomePinPreview);
                                RelativeLayout layoutHomePreview = (RelativeLayout)findViewById(R.id.layoutHomePreview);
                                layoutHomePinPreview.setVisibility(View.VISIBLE);
                                layoutHomePreview.setVisibility(GONE);

                                TextView lblHomePinPreviewHead = (TextView)findViewById(R.id.lblHomePinPreviewHead);
                                TextView lblHomePinPreviewDesc = (TextView)findViewById(R.id.lblHomePinPreviewDesc);
                                ImageView imgHomePinPreview = (ImageView)findViewById(R.id.imgHomePinPreviewImage);
                                lblHomePinPreviewHead.setText(jsonResponse.getString("place_name"));
                                lblHomePinPreviewDesc.setText(jsonResponse.getString("place_detail"));
                                LATLNG_PIN_PREVIEW = new LatLng(Double.parseDouble(jsonResponse.getString("place_latitude")),Double.parseDouble(jsonResponse.getString("place_longitude")));
                                JSONArray imgHomePreviewPlace = new JSONArray(jsonResponse.getString("place_img"));
                                String imgHomePreviewPlaceUrl = AppConfig.noImgUrl;
                                if(imgHomePreviewPlace.length()>0){
                                    JSONObject jsonObjImgCover = imgHomePreviewPlace.getJSONObject(0);
                                    imgHomePreviewPlaceUrl=AppConfig.imageUrl+"/"+jsonObjImgCover.getString("img_text");
                                }
                                Picasso.with(HomeActivity.this).load(imgHomePreviewPlaceUrl).into(imgHomePinPreview);
                                loadPermissionsMapHomeViewPin(Manifest.permission.ACCESS_FINE_LOCATION,REQUEST_FINE_LOCATION);
                                break;
                            case "/getPlaceUserPreview" :
                                RelativeLayout layoutUserPinPreview = (RelativeLayout)findViewById(R.id.layoutUserPinPreview);
                                RelativeLayout layoutUserPreview = (RelativeLayout)findViewById(R.id.layoutUserPreview);
                                layoutUserPinPreview.setVisibility(View.VISIBLE);
                                layoutUserPreview.setVisibility(GONE);

                                TextView lblUserPinPreviewHead = (TextView)findViewById(R.id.lblUserPinPreviewHead);
                                TextView lblUserPinPreviewDesc = (TextView)findViewById(R.id.lblUserPinPreviewDesc);
                                ImageView imgUserPinPreview = (ImageView)findViewById(R.id.imgUserPinPreviewImage);
                                lblUserPinPreviewHead.setText(jsonResponse.getString("place_name"));
                                lblUserPinPreviewDesc.setText(jsonResponse.getString("place_detail"));
                                LATLNG_PIN_PREVIEW = new LatLng(Double.parseDouble(jsonResponse.getString("place_latitude")),Double.parseDouble(jsonResponse.getString("place_longitude")));
                                JSONArray imgUserPreviewPlace = new JSONArray(jsonResponse.getString("place_img"));
                                String imgUserPreviewPlaceUrl = AppConfig.noImgUrl;
                                if(imgUserPreviewPlace.length()>0){
                                    JSONObject jsonObjImgCover = imgUserPreviewPlace.getJSONObject(0);
                                    imgUserPreviewPlaceUrl=AppConfig.imageUrl+"/"+jsonObjImgCover.getString("img_text");
                                }
                                Picasso.with(HomeActivity.this).load(imgUserPreviewPlaceUrl).into(imgUserPinPreview);
                                loadPermissionsMapUserViewPin(Manifest.permission.ACCESS_FINE_LOCATION,REQUEST_FINE_LOCATION);
                                break;
                            case "/getPlacesByRouteAR":
                                MyArrList = new ArrayList<HashMap<String, String>>();
                                Log.d("JSON_LIST",jsonResponse.getString("list"));
                                jsonArray = new JSONArray(jsonResponse.getString("list"));
                                if(jsonArray!=null){
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonRow = jsonArray.getJSONObject(i);
                                        map = new HashMap<String, String>();
                                        map.put("place_id",jsonRow.getString("place_id"));
                                        map.put("title", jsonRow.getString("place_name"));
                                        map.put("detail", jsonRow.getString("place_detail"));
                                        map.put("place_latitude",jsonRow.getString("place_latitude"));
                                        map.put("place_longitude",jsonRow.getString("place_longitude"));
                                        JSONArray imgPlaceARList = new JSONArray(jsonRow.getString("place_img"));
                                        String imgPlaceARUrl = AppConfig.noImgUrl;
                                        if(imgPlaceARList.length()>0){
                                            JSONObject jsonObjImgPlaceHomelist = imgPlaceARList.getJSONObject(0);
                                            imgPlaceARUrl=AppConfig.imageUrl+"/"+jsonObjImgPlaceHomelist.getString("img_text");
                                        }
                                        map.put("imagepath", imgPlaceARUrl);
                                        MyArrList.add(map);
                                    }
                                    allPinsListEdit = MyArrList;
                                    Button btnViewAllPins = (Button)findViewById(R.id.btnViewAllPins);
                                    if(allPinsListEdit.size()>0){
                                        btnViewAllPins.setVisibility(View.VISIBLE);
                                    }
                                    else{
                                        btnViewAllPins.setVisibility(View.GONE);
                                    }
                                }
                                // listView1
                                final ListView lstView2 = (ListView)findViewById(R.id.listViewAddPinPreview);
                                lstView2.setAdapter(new PreviewPinsEditAdapter(HomeActivity.this,MyArrList));
                                break;
                            case "/getPlacesByRouteHome":
                                //JSONObject jsonList = new JSONObject(jsonResponse.getString("list"));

                                MyArrList = new ArrayList<HashMap<String, String>>();
                                Log.d("JSON_LIST",jsonResponse.getString("list"));
                                jsonArray = new JSONArray(jsonResponse.getString("list"));
                                if(jsonArray!=null){
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonRow = jsonArray.getJSONObject(i);
                                        map = new HashMap<String, String>();
                                        map.put("place_id",jsonRow.getString("place_id"));
                                        map.put("title", jsonRow.getString("place_name"));
                                        map.put("detail", jsonRow.getString("place_detail"));
                                        map.put("place_latitude",jsonRow.getString("place_latitude"));
                                        map.put("place_longitude",jsonRow.getString("place_longitude"));
                                        JSONArray imgPlaceHomeList = new JSONArray(jsonRow.getString("place_img"));
                                        String imgPlaceHomeUrl = AppConfig.noImgUrl;
                                        if(imgPlaceHomeList.length()>0){
                                            JSONObject jsonObjImgPlaceHomelist = imgPlaceHomeList.getJSONObject(0);
                                            imgPlaceHomeUrl=AppConfig.imageUrl+"/"+jsonObjImgPlaceHomelist.getString("img_text");
                                        }
                                        map.put("imagepath", imgPlaceHomeUrl);
                                        MyArrList.add(map);
                                    }
                                    allPinsListEdit = MyArrList;
                                    Button btnFeedViewAllPins = (Button)findViewById(R.id.btnFeedViewAllPins);
                                    if(allPinsListEdit.size()>0){
                                        btnFeedViewAllPins.setVisibility(View.VISIBLE);
                                    }
                                    else{
                                        btnFeedViewAllPins.setVisibility(View.GONE);
                                    }
                                }
                                // listView1
                                final ListView lstViewHome = (ListView)findViewById(R.id.listViewHomePreview);
                                lstViewHome.setAdapter(new PreviewPinsHomeAdapter(HomeActivity.this,MyArrList));
                                break;
                            case "/getPlacesByRouteUser":
                                MyArrList = new ArrayList<HashMap<String, String>>();
                                Log.d("JSON_LIST",jsonResponse.getString("list"));
                                jsonArray = new JSONArray(jsonResponse.getString("list"));
                                if(jsonArray!=null){
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonRow = jsonArray.getJSONObject(i);
                                        map = new HashMap<String, String>();
                                        map.put("place_id",jsonRow.getString("place_id"));
                                        map.put("title", jsonRow.getString("place_name"));
                                        map.put("detail", jsonRow.getString("place_detail"));
                                        map.put("place_latitude",jsonRow.getString("place_latitude"));
                                        map.put("place_longitude",jsonRow.getString("place_longitude"));
                                        JSONArray imgPlaceUserList = new JSONArray(jsonRow.getString("place_img"));
                                        String imgPlaceUserUrl = AppConfig.noImgUrl;
                                        if(imgPlaceUserList.length()>0){
                                            JSONObject jsonObjImgPlaceHomelist = imgPlaceUserList.getJSONObject(0);
                                            imgPlaceUserUrl=AppConfig.imageUrl+"/"+jsonObjImgPlaceHomelist.getString("img_text");
                                        }
                                        map.put("imagepath", imgPlaceUserUrl);
                                        MyArrList.add(map);
                                    }
                                    allPinsListEdit = MyArrList;
                                    Button btnUserViewAllPins = (Button)findViewById(R.id.btnUserViewAllPins);
                                    if(allPinsListEdit.size()>0){
                                        btnUserViewAllPins.setVisibility(View.VISIBLE);
                                    }
                                    else{
                                        btnUserViewAllPins.setVisibility(View.GONE);
                                    }
                                }
                                // listView1
                                final ListView lstViewUser = (ListView)findViewById(R.id.listViewUserPreview);
                                lstViewUser.setAdapter(new PreviewPinsUserAdapter(HomeActivity.this,MyArrList));
                                break;
                            case "/usersCurrent":
                                String userimgurl = "";
                                String routeCount = jsonResponse.getString("routeCount");
                                String followerCount = jsonResponse.getString("followerCount");
                                String followingCount = jsonResponse.getString("followingCount");
                                if(jsonResponse.getString("user_image").equals("")){
                                    userimgurl = AppConfig.noUserImgUrl;
                                }else{
                                    userimgurl = AppConfig.imageUrl+"/"+jsonResponse.getString("user_image");
                                }
                                PROFILE_IMG_URL = userimgurl;
                                ImageView userImage = (ImageView)findViewById(R.id.userPhoto);
                                TextView lblRoutes = (TextView)findViewById(R.id.lblRoutes);
                                TextView lblFollowers = (TextView)findViewById(R.id.lblFollowers);
                                TextView lblFollowing = (TextView)findViewById(R.id.lblFollowings);
                                lblRoutes.setText(routeCount);
                                lblFollowers.setText(followerCount);
                                lblFollowing.setText(followingCount);
                                Picasso.with(getApplicationContext()).load(userimgurl).transform(new CircleTransform()).into(userImage);
                                doLoadDataUserGrid();
                                break;
                            case "/userFindDetail":
                                String userimgurlFind = "";
                                String routeCountFind = jsonResponse.getString("routeCount");
                                String followerCountFind = jsonResponse.getString("followerCount");
                                String followingCountFind = jsonResponse.getString("followingCount");
                                if(jsonResponse.getString("user_image").equals("")){
                                    userimgurlFind = AppConfig.noImgUrl;
                                }else{
                                    userimgurlFind = AppConfig.imageUrl+"/"+jsonResponse.getString("user_image");
                                }
                                Button btnFindFollow = (Button)findViewById(R.id.btnFindFollow);
                                btnFindFollow.setTag(Integer.parseInt(jsonResponse.getString("following")));
                                if(Integer.parseInt(jsonResponse.getString("following"))==0){
                                    btnFindFollow.setText("Follow");
                                    btnFindFollow.setBackgroundResource(R.drawable.btn_border_radius_blue);
                                }
                                else{
                                    btnFindFollow.setText("Unfollow");
                                    btnFindFollow.setBackgroundResource(R.drawable.btn_border_radius_gray);

                                }
                                ImageView userImageFind = (ImageView)findViewById(R.id.findUserPhoto);
                                TextView lblRoutesFind = (TextView)findViewById(R.id.lblFindRoutes);
                                TextView lblFollowersFind = (TextView)findViewById(R.id.lblFindFollowers);
                                TextView lblFollowingFind = (TextView)findViewById(R.id.lblFindFollowings);
                                TextView lblFullnameFind = (TextView)findViewById(R.id.lblFindUserFullname);
                                lblFullnameFind.setText(jsonResponse.getString("user_name"));
                                lblRoutesFind.setText(routeCountFind);
                                lblFollowersFind.setText(followerCountFind);
                                lblFollowingFind.setText(followingCountFind);
                                Picasso.with(getApplicationContext()).load(userimgurlFind).transform(new CircleTransform()).into(userImageFind);
                                RelativeLayout layoutFindeUserDetail = (RelativeLayout)findViewById(R.id.layoutUserFindDetail);
                                layoutFindeUserDetail.setVisibility(View.VISIBLE);
                                doLoadDataFindUserGrid();
//                                METHOD = "/listRoutesFeedByFindUserGrid";
//                                new HomeActivity.CallService().execute(AppConfig.apiUrl);
                                break;
                            case "/user/search":
                                MyArrList = new ArrayList<HashMap<String, String>>();
                                Log.d("JSON_LIST",jsonResponse.getString("list"));
                                jsonArray = new JSONArray(jsonResponse.getString("list"));
                                if(jsonArray!=null){
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonRow = jsonArray.getJSONObject(i);
                                        map = new HashMap<String, String>();
                                        map.put("user_id",jsonRow.getString("user_id"));
                                        map.put("user_name", jsonRow.getString("user_name"));
                                        map.put("user_email", jsonRow.getString("user_email"));
                                        map.put("followed",jsonRow.getString("followed"));
                                        String imgUserUrl = AppConfig.noImgUrl;
                                        if(!jsonRow.getString("user_image").equals("")){
                                            imgUserUrl=AppConfig.imageUrl+"/"+jsonRow.getString("user_image");
                                        }
                                        map.put("user_image", imgUserUrl);
                                        MyArrList.add(map);
                                    }
                                    globalFindFriendList = MyArrList;
                                }
                                // listView1
                                final ListView lstViewFindfriends = (ListView)findViewById(R.id.listviewSearchFriends);
                                FindFriendsAdapter ffAdap = new FindFriendsAdapter(HomeActivity.this,globalFindFriendList);
                                lstViewFindfriends.setAdapter(ffAdap);
                                break;
                            case "/getPlacesByRouteExplore":
                                MyArrList = new ArrayList<HashMap<String, String>>();
                                Log.d("JSON_LIST",jsonResponse.getString("list"));
                                jsonArray = new JSONArray(jsonResponse.getString("list"));
                                if(jsonArray!=null){
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonRow = jsonArray.getJSONObject(i);
                                        map = new HashMap<String, String>();
                                        map.put("place_id",jsonRow.getString("place_id"));
                                        map.put("title", jsonRow.getString("place_name"));
                                        map.put("detail", jsonRow.getString("place_detail"));
                                        map.put("place_latitude",jsonRow.getString("place_latitude"));
                                        map.put("place_longitude",jsonRow.getString("place_longitude"));
                                        JSONArray imgPlaceUserList = new JSONArray(jsonRow.getString("place_img"));
                                        String imgPlaceUserUrl = AppConfig.noImgUrl;
                                        if(imgPlaceUserList.length()>0){
                                            JSONObject jsonObjImgPlaceHomelist = imgPlaceUserList.getJSONObject(0);
                                            imgPlaceUserUrl=AppConfig.imageUrl+"/"+jsonObjImgPlaceHomelist.getString("img_text");
                                        }
                                        map.put("imagepath", imgPlaceUserUrl);
                                        MyArrList.add(map);
                                    }
                                    allPinsListEdit = MyArrList;
                                    Button btnExploreViewAllPins = (Button)findViewById(R.id.btnExploreViewAllPins);
                                    if(allPinsListEdit.size()>0){
                                        btnExploreViewAllPins.setVisibility(View.VISIBLE);
                                    }
                                    else{
                                        btnExploreViewAllPins.setVisibility(View.GONE);
                                    }
                                }
                                // listView1
                                final ListView lstViewExplore = (ListView)findViewById(R.id.listViewExplorePreview);
                                lstViewExplore.setAdapter(new PreviewPinsExploreAdapter(HomeActivity.this,MyArrList));
                                break;
                            case "/listRoutesFeedExplore":
                                MyArrList = new ArrayList<HashMap<String, String>>();
                                Log.d("JSON_LIST",jsonResponse.getString("listpop"));
                                jsonArray = new JSONArray(jsonResponse.getString("listpop"));
                                if(jsonArray!=null){
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonRow = jsonArray.getJSONObject(i);
                                        map = new HashMap<String, String>();
                                        map.put("route_id",jsonRow.getString("route_id"));
                                        map.put("title", jsonRow.getString("route_title"));
                                        map.put("detail", jsonRow.getString("route_detail"));
                                        map.put("author", jsonRow.getString("user_name"));
                                        JSONArray imgList = new JSONArray(jsonRow.getString("route_img"));
                                        if(imgList.length()>0){
                                            JSONObject jsonObjImglist = imgList.getJSONObject(0);
                                            map.put("imagepath", AppConfig.imageUrl+"/"+jsonObjImglist.getString("img_text"));
                                        }else{
                                            map.put("imagepath",AppConfig.noImgUrl);
                                        }

                                        map.put("time",GetFeedTimeText(jsonRow.getString("diffDate")));
                                        MyArrList.add(map);
                                    }
                                }
                                if(MyArrList.size()>0){
                                    // grid popular
                                    final GridView grid1 = (GridView)findViewById(R.id.gridPoppular);
                                    grid1.setAdapter(new ExploreFeedAdapter(HomeActivity.this,MyArrList));
                                    final MainUtility mUtility = new MainUtility();
                                    new android.os.Handler().postDelayed(
                                            new Runnable() {
                                                public void run() {
                                                    mUtility.setGridViewHeightBasedOnChildren(grid1,3);
                                                }
                                            }, 500);
                                }


                                ArrayList<HashMap<String, String>> MyArrList1 = new ArrayList<HashMap<String, String>>();
                                jsonArray = new JSONArray(jsonResponse.getString("listnear"));
                                if(jsonArray!=null){
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonRow = jsonArray.getJSONObject(i);
                                        map = new HashMap<String, String>();
                                        map.put("route_id",jsonRow.getString("route_id"));
                                        map.put("title", jsonRow.getString("route_title"));
                                        map.put("detail", jsonRow.getString("route_detail"));
                                        map.put("author", jsonRow.getString("user_name"));
                                        JSONArray imgList = new JSONArray(jsonRow.getString("route_img"));
                                        if(imgList.length()>0){
                                            JSONObject jsonObjImglist = imgList.getJSONObject(0);
                                            map.put("imagepath", AppConfig.imageUrl+"/"+jsonObjImglist.getString("img_text"));
                                        }else{
                                            map.put("imagepath",AppConfig.noImgUrl);
                                        }

                                        map.put("time",GetFeedTimeText(jsonRow.getString("diffDate")));
                                        MyArrList1.add(map);
                                    }
                                }
                                if(MyArrList1.size()>0) {
                                    // grid nearby
                                    final GridView grid2 = (GridView)findViewById(R.id.gridNearby);
                                    grid2.setAdapter(new ExploreFeedAdapter(HomeActivity.this,MyArrList1));
                                    final MainUtility mUtility = new MainUtility();
                                    new android.os.Handler().postDelayed(
                                            new Runnable() {
                                                public void run() {
                                                    mUtility.setGridViewHeightBasedOnChildren(grid2,3);
                                                }
                                            }, 500);
                                }

                                break;
                            case "/listSearchRoutesExplore":
                                MyArrList = new ArrayList<HashMap<String, String>>();
                                Log.d("JSON_LIST",jsonResponse.getString("listpop"));
                                jsonArray = new JSONArray(jsonResponse.getString("listpop"));
                                if(jsonArray!=null){
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonRow = jsonArray.getJSONObject(i);
                                        map = new HashMap<String, String>();
                                        map.put("route_id",jsonRow.getString("route_id"));
                                        map.put("title", jsonRow.getString("route_title"));
                                        map.put("detail", jsonRow.getString("route_detail"));
                                        map.put("author", jsonRow.getString("user_name"));
                                        JSONArray imgList = new JSONArray(jsonRow.getString("route_img"));
                                        if(imgList.length()>0){
                                            JSONObject jsonObjImglist = imgList.getJSONObject(0);
                                            map.put("imagepath", AppConfig.imageUrl+"/"+jsonObjImglist.getString("img_text"));
                                        }else{
                                            map.put("imagepath",AppConfig.noImgUrl);
                                        }

                                        map.put("time",GetFeedTimeText(jsonRow.getString("diffDate")));
                                        MyArrList.add(map);
                                    }
                                }
                                if(MyArrList.size()>0){
                                    // grid popular
                                    final GridView grid1 = (GridView)findViewById(R.id.gridExploreSearch);
                                    grid1.setAdapter(new ExploreFeedAdapter(HomeActivity.this,MyArrList));
                                    final MainUtility mUtility = new MainUtility();
                                    new android.os.Handler().postDelayed(
                                            new Runnable() {
                                                public void run() {
                                                    mUtility.setGridViewHeightBasedOnChildren(grid1,3);
                                                }
                                            }, 500);
                                }
                                break;
                            case "/listRoutesFeed":
                                MyArrList = new ArrayList<HashMap<String, String>>();
                                Log.d("JSON_LIST",jsonResponse.getString("list"));
                                jsonArray = new JSONArray(jsonResponse.getString("list"));
                                if(jsonArray!=null){
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonRow = jsonArray.getJSONObject(i);
                                        map = new HashMap<String, String>();
                                        map.put("route_id",jsonRow.getString("route_id"));
                                        map.put("title", jsonRow.getString("route_title"));
                                        map.put("detail", jsonRow.getString("route_detail"));
                                        map.put("author", jsonRow.getString("user_name"));
                                        String userImgUrl = AppConfig.noImgUrl;
                                        if(!jsonRow.getString("user_image").equals("")){
                                            userImgUrl = AppConfig.imageUrl+"/"+jsonRow.getString("user_image");
                                        }
                                        map.put("user_id",jsonRow.getString("user_id"));
                                        map.put("user_image", userImgUrl);
                                        map.put("like_status", jsonRow.getString("like_status"));
                                        map.put("favorite_status", jsonRow.getString("favorite_status"));
                                        JSONArray imgList = new JSONArray(jsonRow.getString("route_img"));
                                        if(imgList.length()>0){
                                            JSONObject jsonObjImglist = imgList.getJSONObject(0);
                                            map.put("imagepath", AppConfig.imageUrl+"/"+jsonObjImglist.getString("img_text"));
                                        }else{
                                            map.put("imagepath",AppConfig.noImgUrl);
                                        }

                                        map.put("time",GetFeedTimeText(jsonRow.getString("diffDate")));
                                        MyArrList.add(map);
                                    }
                                    globalHomeFeedList = MyArrList;
                                }
                                // listView1
                                final ListView lstView1 = (ListView)findViewById(R.id.listView1);
                                lstView1.setAdapter(new HomeFeedAdapter(HomeActivity.this,MyArrList));
                                TAB1_LOADED=true;
                                break;
                            case "/listRoutesFeedByUserDraft" :
                                MyArrList = new ArrayList<HashMap<String, String>>();
                                jsonArray = new JSONArray(jsonResponse.getString("list"));
                                if(jsonArray!=null){
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonRow = jsonArray.getJSONObject(i);
                                        map = new HashMap<String, String>();
                                        map.put("route_id",jsonRow.getString("route_id"));
                                        map.put("title", jsonRow.getString("route_title"));
                                        map.put("detail", jsonRow.getString("route_detail"));
                                        map.put("author", jsonRow.getString("user_name"));
                                        JSONArray imgList = new JSONArray(jsonRow.getString("route_img"));
                                        if(imgList.length()>0){
                                            JSONObject jsonObjImglist = imgList.getJSONObject(0);
                                            map.put("imagepath", AppConfig.imageUrl+"/"+jsonObjImglist.getString("img_text"));
                                        }else{
                                            map.put("imagepath",AppConfig.noImgUrl);
                                        }
                                        map.put("time",GetFeedTimeText(jsonRow.getString("diffDate")));
                                        MyArrList.add(map);
                                    }
                                }
                                // listView1
                                final ListView lstViewDraft = (ListView)findViewById(R.id.listViewDraft);
                                lstViewDraft.setAdapter(new PinDraftAdapter(HomeActivity.this,MyArrList));
                                break;
                            case "/listRoutesFeedByUserGrid" :
                                MyArrList = new ArrayList<HashMap<String, String>>();
                                jsonArray = new JSONArray(jsonResponse.getString("list"));
                                if(jsonArray!=null){
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonRow = jsonArray.getJSONObject(i);
                                        map = new HashMap<String, String>();
                                        map.put("route_id",jsonRow.getString("route_id"));
                                        map.put("title", jsonRow.getString("route_title"));
                                        map.put("detail", jsonRow.getString("route_detail"));
                                        map.put("author", jsonRow.getString("user_name"));
                                        JSONArray imgList = new JSONArray(jsonRow.getString("route_img"));
                                        if(imgList.length()>0){
                                            JSONObject jsonObjImglist = imgList.getJSONObject(0);
                                            map.put("imagepath", AppConfig.imageUrl+"/"+jsonObjImglist.getString("img_text"));
                                        }else{
                                            map.put("imagepath",AppConfig.noImgUrl);
                                        }
                                        map.put("time",GetFeedTimeText(jsonRow.getString("diffDate")));
                                        MyArrList.add(map);
                                    }
                                    globalUserFeedList = MyArrList;
                                }
                                // grid
                                GridView gridUserFeed = (GridView)findViewById(R.id.gridUser);
                                gridUserFeed.setAdapter(new UserGridFeedAdapter(HomeActivity.this,MyArrList));
                                break;
                            case "/listRoutesFeedByFindUserGrid" :
                                MyArrList = new ArrayList<HashMap<String, String>>();
                                jsonArray = new JSONArray(jsonResponse.getString("list"));
                                if(jsonArray!=null){
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonRow = jsonArray.getJSONObject(i);
                                        map = new HashMap<String, String>();
                                        map.put("route_id",jsonRow.getString("route_id"));
                                        map.put("title", jsonRow.getString("route_title"));
                                        map.put("detail", jsonRow.getString("route_detail"));
                                        map.put("author", jsonRow.getString("user_name"));
                                        JSONArray imgList = new JSONArray(jsonRow.getString("route_img"));
                                        if(imgList.length()>0){
                                            JSONObject jsonObjImglist = imgList.getJSONObject(0);
                                            map.put("imagepath", AppConfig.imageUrl+"/"+jsonObjImglist.getString("img_text"));
                                        }else{
                                            map.put("imagepath",AppConfig.noImgUrl);
                                        }
                                        map.put("time",GetFeedTimeText(jsonRow.getString("diffDate")));
                                        MyArrList.add(map);
                                    }
                                    //globalUserFeedList = MyArrList;
                                }
                                // grid
                                GridView gridFindUserFeed = (GridView)findViewById(R.id.gridFindUser);
                                gridFindUserFeed.setAdapter(new UserGridFeedAdapter(HomeActivity.this,MyArrList));
                                break;
                            case "/listRoutesFeedByFindUserList":
                                MyArrList = new ArrayList<HashMap<String, String>>();
                                jsonArray = new JSONArray(jsonResponse.getString("list"));
                                if(jsonArray!=null){
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonRow = jsonArray.getJSONObject(i);
                                        map = new HashMap<String, String>();
                                        map.put("user_id",jsonRow.getString("user_id"));
                                        map.put("route_id",jsonRow.getString("route_id"));
                                        map.put("title", jsonRow.getString("route_title"));
                                        map.put("detail", jsonRow.getString("route_detail"));
                                        map.put("author", jsonRow.getString("user_name"));
                                        String userImgUrl = AppConfig.noImgUrl;
                                        if(!jsonRow.getString("user_image").equals("")){
                                            userImgUrl = AppConfig.imageUrl+"/"+jsonRow.getString("user_image");
                                        }
                                        map.put("user_image", userImgUrl);
                                        map.put("like_status", jsonRow.getString("like_status"));
                                        map.put("favorite_status", jsonRow.getString("favorite_status"));
                                        JSONArray imgList = new JSONArray(jsonRow.getString("route_img"));
                                        if(imgList.length()>0){
                                            JSONObject jsonObjImglist = imgList.getJSONObject(0);
                                            map.put("imagepath", AppConfig.imageUrl+"/"+jsonObjImglist.getString("img_text"));
                                        }else{
                                            map.put("imagepath",AppConfig.noImgUrl);
                                        }
                                        map.put("time",GetFeedTimeText(jsonRow.getString("diffDate")));
                                        MyArrList.add(map);
                                    }
                                }
                                // listView1
                                final ListView lstViewFindUserPreview = (ListView)findViewById(R.id.listFindUser);
                                lstViewFindUserPreview.setAdapter(new UserListFeedAdapter(HomeActivity.this,MyArrList));
                                break;
                            case "/listRoutesFeedByUserList":
                                MyArrList = new ArrayList<HashMap<String, String>>();
                                jsonArray = new JSONArray(jsonResponse.getString("list"));
                                if(jsonArray!=null){
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonRow = jsonArray.getJSONObject(i);
                                        map = new HashMap<String, String>();
                                        map.put("user_id",jsonRow.getString("user_id"));
                                        map.put("route_id",jsonRow.getString("route_id"));
                                        map.put("title", jsonRow.getString("route_title"));
                                        map.put("detail", jsonRow.getString("route_detail"));
                                        map.put("author", jsonRow.getString("user_name"));
                                        String userImgUrl = AppConfig.noImgUrl;
                                        if(!jsonRow.getString("user_image").equals("")){
                                            userImgUrl = AppConfig.imageUrl+"/"+jsonRow.getString("user_image");
                                        }
                                        map.put("user_image", userImgUrl);
                                        map.put("like_status", jsonRow.getString("like_status"));
                                        map.put("favorite_status", jsonRow.getString("favorite_status"));
                                        JSONArray imgList = new JSONArray(jsonRow.getString("route_img"));
                                        if(imgList.length()>0){
                                            JSONObject jsonObjImglist = imgList.getJSONObject(0);
                                            map.put("imagepath", AppConfig.imageUrl+"/"+jsonObjImglist.getString("img_text"));
                                        }else{
                                            map.put("imagepath",AppConfig.noImgUrl);
                                        }
                                        map.put("time",GetFeedTimeText(jsonRow.getString("diffDate")));
                                        MyArrList.add(map);
                                    }
                                    globalUserFeedList = MyArrList;
                                }
                                // listView1
                                final ListView lstViewUserPreview = (ListView)findViewById(R.id.listUser);
                                lstViewUserPreview.setAdapter(new UserListFeedAdapter(HomeActivity.this,MyArrList));
                                break;
                            case "/listRoutesFeedByUserListLike":
                                MyArrList = new ArrayList<HashMap<String, String>>();
                                jsonArray = new JSONArray(jsonResponse.getString("list"));
                                if(jsonArray!=null){
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonRow = jsonArray.getJSONObject(i);
                                        map = new HashMap<String, String>();
                                        map.put("user_id",jsonRow.getString("user_id"));
                                        map.put("route_id",jsonRow.getString("route_id"));
                                        map.put("title", jsonRow.getString("route_title"));
                                        map.put("detail", jsonRow.getString("route_detail"));
                                        map.put("author", jsonRow.getString("user_name"));
                                        String userImgUrl = AppConfig.noImgUrl;
                                        if(!jsonRow.getString("user_image").equals("")){
                                            userImgUrl = AppConfig.imageUrl+"/"+jsonRow.getString("user_image");
                                        }
                                        map.put("user_image", userImgUrl);
                                        map.put("like_status", jsonRow.getString("like_status"));
                                        map.put("favorite_status", jsonRow.getString("favorite_status"));
                                        JSONArray imgList = new JSONArray(jsonRow.getString("route_img"));
                                        if(imgList.length()>0){
                                            JSONObject jsonObjImglist = imgList.getJSONObject(0);
                                            map.put("imagepath", AppConfig.imageUrl+"/"+jsonObjImglist.getString("img_text"));
                                        }else{
                                            map.put("imagepath",AppConfig.noImgUrl);
                                        }
                                        map.put("time",GetFeedTimeText(jsonRow.getString("diffDate")));
                                        MyArrList.add(map);
                                    }
                                    globalUserFeedList = MyArrList;
                                }
                                // listView1
                                final ListView lstViewUserPreviewLike = (ListView)findViewById(R.id.listUser);
                                lstViewUserPreviewLike.setAdapter(new UserListFeedAdapter(HomeActivity.this,MyArrList));
                                break;
                            case "/listRoutesFeedByUserListFavorite":
                                MyArrList = new ArrayList<HashMap<String, String>>();
                                jsonArray = new JSONArray(jsonResponse.getString("list"));
                                if(jsonArray!=null){
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonRow = jsonArray.getJSONObject(i);
                                        map = new HashMap<String, String>();
                                        map.put("user_id",jsonRow.getString("user_id"));
                                        map.put("route_id",jsonRow.getString("route_id"));
                                        map.put("title", jsonRow.getString("route_title"));
                                        map.put("detail", jsonRow.getString("route_detail"));
                                        map.put("author", jsonRow.getString("user_name"));
                                        String userImgUrl = AppConfig.noImgUrl;
                                        if(!jsonRow.getString("user_image").equals("")){
                                            userImgUrl = AppConfig.imageUrl+"/"+jsonRow.getString("user_image");
                                        }
                                        map.put("user_image", userImgUrl);
                                        map.put("like_status", jsonRow.getString("like_status"));
                                        map.put("favorite_status", jsonRow.getString("favorite_status"));
                                        JSONArray imgList = new JSONArray(jsonRow.getString("route_img"));
                                        if(imgList.length()>0){
                                            JSONObject jsonObjImglist = imgList.getJSONObject(0);
                                            map.put("imagepath", AppConfig.imageUrl+"/"+jsonObjImglist.getString("img_text"));
                                        }else{
                                            map.put("imagepath",AppConfig.noImgUrl);
                                        }
                                        map.put("time",GetFeedTimeText(jsonRow.getString("diffDate")));
                                        MyArrList.add(map);
                                    }
                                    globalUserFeedList = MyArrList;
                                }
                                // listView1
                                final ListView lstViewUserPreviewFavorite = (ListView)findViewById(R.id.listUser);
                                lstViewUserPreviewFavorite.setAdapter(new UserListFeedAdapter(HomeActivity.this,MyArrList));
                                break;
                            case "/findfriendFollow":
                                if(FOLLOW_CURRENT_INDEX > -1){
                                    final ListView lstViewFollowFindfriends = (ListView)findViewById(R.id.listviewSearchFriends);
                                    globalFindFriendList.get(FOLLOW_CURRENT_INDEX).put("followed","1");
                                    FindFriendsAdapter fffollowAdap = new FindFriendsAdapter(HomeActivity.this,globalFindFriendList);
                                    Parcelable state = lstViewFollowFindfriends.onSaveInstanceState();
                                    lstViewFollowFindfriends.setAdapter(fffollowAdap);
                                    lstViewFollowFindfriends.onRestoreInstanceState(state);
                                    FOLLOW_CURRENT_INDEX = -1;
                                }

                                break;
                            case "/addLikeHome":
                                if(LIKE_CURRENT_INDEX > -1){
                                    final ListView lstViewHomeFeed = (ListView)findViewById(R.id.listView1);
                                    globalHomeFeedList.get(LIKE_CURRENT_INDEX).put("like_status","1");
                                    HomeFeedAdapter fffollowAdap = new HomeFeedAdapter(HomeActivity.this,globalHomeFeedList);
                                    Parcelable state = lstViewHomeFeed.onSaveInstanceState();
                                    lstViewHomeFeed.setAdapter(fffollowAdap);
                                    lstViewHomeFeed.onRestoreInstanceState(state);
                                    LIKE_CURRENT_INDEX = -1;
                                }
                                break;
                            case "/delLikeHome":
                                if(LIKE_CURRENT_INDEX > -1){
                                    final ListView lstViewHomeFeed = (ListView)findViewById(R.id.listView1);
                                    globalHomeFeedList.get(LIKE_CURRENT_INDEX).put("like_status","0");
                                    HomeFeedAdapter fffollowAdap = new HomeFeedAdapter(HomeActivity.this,globalHomeFeedList);
                                    Parcelable state = lstViewHomeFeed.onSaveInstanceState();
                                    lstViewHomeFeed.setAdapter(fffollowAdap);
                                    lstViewHomeFeed.onRestoreInstanceState(state);
                                    LIKE_CURRENT_INDEX = -1;
                                }
                                break;
                            case "/listNotificationFollowing":
                                Log.d("JSON_LIST",jsonResponse.getString("list"));
                                MyArrList = new ArrayList<HashMap<String, String>>();
                                jsonArray = new JSONArray(jsonResponse.getString("list"));
                                if(jsonArray!=null){
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonRow = jsonArray.getJSONObject(i);
                                        String userImgUrl = AppConfig.noImgUrl;
                                        String userPassiveUrl = AppConfig.noImgUrl;
                                        String routeImgUrl = AppConfig.noImgUrl;
                                        if(!jsonRow.getString("user_active_image").equals("")){
                                            userImgUrl = AppConfig.imageUrl+"/"+jsonRow.getString("user_active_image");
                                            routeImgUrl = AppConfig.imageUrl+"/"+jsonRow.getString("noti_route_img");
                                        }
                                        if(!jsonRow.getString("noti_route_img").equals("")){
                                            routeImgUrl = AppConfig.imageUrl+"/"+jsonRow.getString("noti_route_img");
                                        }
                                        map = new HashMap<String, String>();
                                        map.put("userimg", userImgUrl);
                                        map.put("username", jsonRow.getString("user_active_name"));
                                        String activityText = "";
                                        if(jsonRow.getString("noti_type").equals("like")){
                                            activityText = "is liked route.";
                                        }
                                        else if(jsonRow.getString("noti_type").equals("comment")){
                                            activityText = "commented your route.";
                                        }
                                        else if(jsonRow.getString("noti_type").equals("follow")){
                                            activityText = "is following "+jsonRow.getString("user_passive_name")+".";
                                        }
                                        if(!jsonRow.getString("user_passive_image").equals("")){
                                            userPassiveUrl = AppConfig.imageUrl+"/"+jsonRow.getString("user_passive_image");
                                        }

                                        map.put("user_passive_image",userPassiveUrl);
                                        map.put("noti_type",jsonRow.getString("noti_type"));
                                        map.put("activity", activityText);
                                        map.put("imagepath", routeImgUrl);

                                        map.put("time",GetFeedTimeText(jsonRow.getString("diffDate")));
                                        MyArrList.add(map);
                                    }
                                }
                                // listView1
                                final ListView lstViewNotiFollow = (ListView)findViewById(R.id.listViewNotisFollowing);
                                lstViewNotiFollow.setAdapter(new FollowingAdapter(HomeActivity.this,MyArrList));
                                break;
                            case "/listNotificationYou":
                                Log.d("JSON_LIST",jsonResponse.getString("list"));
                                MyArrList = new ArrayList<HashMap<String, String>>();
                                jsonArray = new JSONArray(jsonResponse.getString("list"));
                                if(jsonArray!=null){
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonRow = jsonArray.getJSONObject(i);
                                        String userImgUrl = AppConfig.noImgUrl;
                                        String routeImgUrl = AppConfig.noImgUrl;
                                        if(!jsonRow.getString("user_active_image").equals("")){
                                            userImgUrl = AppConfig.imageUrl+"/"+jsonRow.getString("user_active_image");
                                            routeImgUrl = AppConfig.imageUrl+"/"+jsonRow.getString("noti_route_img");
                                        }
                                        if(!jsonRow.getString("noti_route_img").equals("")){
                                            routeImgUrl = AppConfig.imageUrl+"/"+jsonRow.getString("noti_route_img");
                                        }
                                        map = new HashMap<String, String>();
                                        map.put("userimg", userImgUrl);
                                        map.put("username", jsonRow.getString("user_active_name"));
                                        String activityText = "";
                                        if(jsonRow.getString("noti_type").equals("like")){
                                            activityText = "is liked your route.";
                                        }
                                        else if(jsonRow.getString("noti_type").equals("comment")){
                                            activityText = "commented your route.";
                                        }
                                        else if(jsonRow.getString("noti_type").equals("follow")){
                                            activityText = "is following you.";
                                        }
                                        map.put("noti_type",jsonRow.getString("noti_type"));
                                        map.put("activity", activityText);
                                        map.put("imagepath", routeImgUrl);
                                        map.put("time",GetFeedTimeText(jsonRow.getString("diffDate")));
                                        MyArrList.add(map);
                                    }
                                }
                                // listView1
                                final ListView lstViewNotiYou = (ListView)findViewById(R.id.listViewNotisYou);
                                lstViewNotiYou.setAdapter(new NotisAdapter(HomeActivity.this,MyArrList));
                                break;
                        }

                    }
                    else{
                        Toast.makeText(getApplication(), jsonErrors.getString("detail"),
                                Toast.LENGTH_LONG).show();
                    }


                } catch (JSONException e) {
                    e.getMessage();
                    e.printStackTrace();
                }
                //bypass
                //R_ID = "1";


            }
        }



    }

    private class CallServiceNoLoad  extends AsyncTask<String, Integer, Void> {

        // Required initialization

        //private final HttpClient Client = new DefaultHttpClient();
        private String Content;
        private String Error = null;
        private ProgressDialog Dialog = new ProgressDialog(HomeActivity.this);
        String data ="";
        private boolean isConnected=false;


        protected void onPreExecute() {
            // NOTE: You can call UI Element here.

            //Start Progress Dialog (Message)
            isConnected = CheckNetwork();
//            Dialog.setMessage("Please wait..");
//            Dialog.show();
            //Log.d("JSON", "PreExcute");
            // Set Request parameter
//            try{
////                data +="&" + URLEncoder.encode("user_name", "UTF-8") + "="+PHONE;
////                data +="&" + URLEncoder.encode("user_pass", "UTF-8") + "="+PHONE;
//            }catch (UnsupportedEncodingException ex){
//                Log.d("ERROR",ex.getMessage());
//            }

        }
        HttpPost httpPost ;
        HttpGet httpGet;
        HttpPut httpPut;
        HttpResponse response;
        HttpClient httpClient;
        // Call after onPreExecute method
        protected Void doInBackground(String... urls) {
            //Looper.prepare();
            StringBuilder stringBuilder = new StringBuilder();
            httpClient = new DefaultHttpClient();
            //HttpGet httpGet = new HttpGet(urls[0]+data);

            List<NameValuePair> nameValuePairs;
            if(isConnected) {
                System.setProperty("http.keepAlive", "false");
                // Add your data
                try{
                    Date datenow = new Date();
                    SimpleDateFormat postFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String strDatenow = postFormater.format(datenow);

                    switch (METHOD){
                        case "/findfriendFollow":
                            httpPost = new HttpPost(urls[0]+"/addFollowUser");
                            nameValuePairs = new ArrayList<NameValuePair>(2);
                            nameValuePairs.add(new BasicNameValuePair("user_id", USER_ID));
                            nameValuePairs.add(new BasicNameValuePair("follow_user_id", FOLLOW_USER_ID));
                            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                            response = httpClient.execute(httpPost);
                            break;
                        case "/addFollow":
                            httpPost = new HttpPost(urls[0]+"/addFollowUser");
                            nameValuePairs = new ArrayList<NameValuePair>(2);
                            nameValuePairs.add(new BasicNameValuePair("user_id", USER_ID));
                            nameValuePairs.add(new BasicNameValuePair("follow_user_id", FOLLOW_USER_ID));
                            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                            response = httpClient.execute(httpPost);
                            break;
                        case "/delFollow":
                            httpPost = new HttpPost(urls[0]+"/unFollowUser");
                            nameValuePairs = new ArrayList<NameValuePair>(2);
                            nameValuePairs.add(new BasicNameValuePair("user_id", USER_ID));
                            nameValuePairs.add(new BasicNameValuePair("follow_user_id", FOLLOW_USER_ID));
                            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                            response = httpClient.execute(httpPost);
                            break;
                        case "/addLikeHome":
                            String likeRouteID = globalHomeFeedList.get(LIKE_CURRENT_INDEX).get("route_id");
                            httpPost = new HttpPost(urls[0]+"/addLikeRoute");
                            nameValuePairs = new ArrayList<NameValuePair>(2);
                            nameValuePairs.add(new BasicNameValuePair("user_id", USER_ID));
                            nameValuePairs.add(new BasicNameValuePair("route_id", likeRouteID));
                            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                            response = httpClient.execute(httpPost);
                            break;
                        case "/delLikeHome":
                            String unlikeRouteID = globalHomeFeedList.get(LIKE_CURRENT_INDEX).get("route_id");
                            httpPost = new HttpPost(urls[0]+"/deleteLikeRoute");
                            nameValuePairs = new ArrayList<NameValuePair>(2);
                            nameValuePairs.add(new BasicNameValuePair("user_id", USER_ID));
                            nameValuePairs.add(new BasicNameValuePair("route_id", unlikeRouteID));
                            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                            response = httpClient.execute(httpPost);
                            break;
                        case "/addLikeUser":
                            String likeUserRouteID = globalUserFeedList.get(LIKE_CURRENT_INDEX).get("route_id");
                            httpPost = new HttpPost(urls[0]+"/addLikeRoute");
                            nameValuePairs = new ArrayList<NameValuePair>(2);
                            nameValuePairs.add(new BasicNameValuePair("user_id", USER_ID));
                            nameValuePairs.add(new BasicNameValuePair("route_id", likeUserRouteID));
                            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                            response = httpClient.execute(httpPost);
                            break;
                        case "/delLikeUser":
                            String unlikeUserRouteID = globalUserFeedList.get(LIKE_CURRENT_INDEX).get("route_id");
                            httpPost = new HttpPost(urls[0]+"/deleteLikeRoute");
                            nameValuePairs = new ArrayList<NameValuePair>(2);
                            nameValuePairs.add(new BasicNameValuePair("user_id", USER_ID));
                            nameValuePairs.add(new BasicNameValuePair("route_id", unlikeUserRouteID));
                            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                            response = httpClient.execute(httpPost);
                            break;
                        case "/addFavoriteUser":
                            String favoriteUserRouteID = globalUserFeedList.get(LIKE_CURRENT_INDEX).get("route_id");
                            httpPost = new HttpPost(urls[0]+"/addFavoriteRoute");
                            nameValuePairs = new ArrayList<NameValuePair>(2);
                            nameValuePairs.add(new BasicNameValuePair("user_id", USER_ID));
                            nameValuePairs.add(new BasicNameValuePair("route_id", favoriteUserRouteID));
                            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                            response = httpClient.execute(httpPost);
                            break;
                        case "/delFavoriteUser":
                            String unFavoriteUserRouteID = globalUserFeedList.get(LIKE_CURRENT_INDEX).get("route_id");
                            httpPost = new HttpPost(urls[0]+"/deleteFavoriteRoute");
                            nameValuePairs = new ArrayList<NameValuePair>(2);
                            nameValuePairs.add(new BasicNameValuePair("user_id", USER_ID));
                            nameValuePairs.add(new BasicNameValuePair("route_id", unFavoriteUserRouteID));
                            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                            response = httpClient.execute(httpPost);
                            break;
                        case "/listCommentHome":
                            httpPost = new HttpPost(urls[0]+"/listCommentRoute");
                            nameValuePairs = new ArrayList<NameValuePair>(2);
                            nameValuePairs.add(new BasicNameValuePair("user_id", USER_ID));
                            nameValuePairs.add(new BasicNameValuePair("route_id", HOME_COMMENT_ROUTE_ID));
                            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                            response = httpClient.execute(httpPost);
                            break;
                        case "/listCommentUser":
                            httpPost = new HttpPost(urls[0]+"/listCommentRoute");
                            nameValuePairs = new ArrayList<NameValuePair>(2);
                            nameValuePairs.add(new BasicNameValuePair("user_id", USER_ID));
                            nameValuePairs.add(new BasicNameValuePair("route_id", USER_COMMENT_ROUTE_ID));
                            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                            response = httpClient.execute(httpPost);
                            break;
                        case "/addCommentHome":
                            httpPost = new HttpPost(urls[0]+"/addComment");
                            nameValuePairs = new ArrayList<NameValuePair>(2);
                            nameValuePairs.add(new BasicNameValuePair("user_id", USER_ID));
                            nameValuePairs.add(new BasicNameValuePair("route_id", HOME_COMMENT_ROUTE_ID));
                            nameValuePairs.add(new BasicNameValuePair("comment_detail", HOME_COMMENT_DETAIL));
                            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                            response = httpClient.execute(httpPost);
                            break;
                        case "/addCommentUser":
                            httpPost = new HttpPost(urls[0]+"/addComment");
                            nameValuePairs = new ArrayList<NameValuePair>(2);
                            nameValuePairs.add(new BasicNameValuePair("user_id", USER_ID));
                            nameValuePairs.add(new BasicNameValuePair("route_id", USER_COMMENT_ROUTE_ID));
                            nameValuePairs.add(new BasicNameValuePair("comment_detail", USER_COMMENT_DETAIL));
                            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                            response = httpClient.execute(httpPost);
                            break;
                        case "/addFavoriteHome":
                            String favRouteID = globalHomeFeedList.get(LIKE_CURRENT_INDEX).get("route_id");
                            httpPost = new HttpPost(urls[0]+"/addFavoriteRoute");
                            nameValuePairs = new ArrayList<NameValuePair>(2);
                            nameValuePairs.add(new BasicNameValuePair("user_id", USER_ID));
                            nameValuePairs.add(new BasicNameValuePair("route_id", favRouteID));
                            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                            response = httpClient.execute(httpPost);
                            break;
                        case "/delFavoriteHome":
                            String unFavoriteRouteID = globalHomeFeedList.get(LIKE_CURRENT_INDEX).get("route_id");
                            httpPost = new HttpPost(urls[0]+"/deleteFavoriteRoute");
                            nameValuePairs = new ArrayList<NameValuePair>(2);
                            nameValuePairs.add(new BasicNameValuePair("user_id", USER_ID));
                            nameValuePairs.add(new BasicNameValuePair("route_id", unFavoriteRouteID));
                            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                            response = httpClient.execute(httpPost);
                            break;
                    }
                }catch (UnsupportedEncodingException ex){
                    Log.d("ERROR",ex.getMessage());
                }
                catch (Exception e){
                    Log.d("readJSONFeed", e.getLocalizedMessage());
                    Error = e.getLocalizedMessage();
                }
                try {

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
                        //Toast.makeText(getApplication(), "Failed to download file",
                        // Toast.LENGTH_LONG).show();
                        Log.d("JSON", "Failed to download file -> "+String.valueOf(statusCode));
                        Log.d("JSON", "Failed to download file -> "+String.valueOf(statusLine.getReasonPhrase()));
                    }
                    Content = stringBuilder.toString();
                } catch (Exception e) {
                    Log.d("readJSONFeed", e.getLocalizedMessage());
                    Error = e.getLocalizedMessage();
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
                    HashMap<String,String> newMarker;
                    HashMap<String, String> map;
                    JSONArray jsonArray;
                    /****** Creates a new JSONObject with name/value mappings from the JSON string. ********/
                    jsonResponse = new JSONObject(Content);
                    JSONObject jsonErrors = new JSONObject(jsonResponse.getString("errors"));
                    if(jsonErrors.getString("status").equals("200")) {
                        switch (METHOD){
                            case "/findfriendFollow":
                                if(FOLLOW_CURRENT_INDEX > -1){
                                    final ListView lstViewFollowFindfriends = (ListView)findViewById(R.id.listviewSearchFriends);
                                    globalFindFriendList.get(FOLLOW_CURRENT_INDEX).put("followed","1");
                                    FindFriendsAdapter fffollowAdap = new FindFriendsAdapter(HomeActivity.this,globalFindFriendList);
                                    Parcelable state = lstViewFollowFindfriends.onSaveInstanceState();
                                    lstViewFollowFindfriends.setAdapter(fffollowAdap);
                                    lstViewFollowFindfriends.onRestoreInstanceState(state);
                                    FOLLOW_CURRENT_INDEX = -1;
                                }

                                break;
                            case "/addFollow":
                                Button btnAddFindFollow = (Button)findViewById(R.id.btnFindFollow);
                                btnAddFindFollow.setBackgroundResource(R.drawable.btn_border_radius_gray);
                                btnAddFindFollow.setText("Unfollow");
                                btnAddFindFollow.setTag(1);
                                break;
                            case "/delFollow":
                                Button btnDelFindFollow = (Button)findViewById(R.id.btnFindFollow);
                                btnDelFindFollow.setBackgroundResource(R.drawable.btn_border_radius_blue);
                                btnDelFindFollow.setText("Follow");
                                btnDelFindFollow.setTag(0);
                                break;
                            case "/addLikeHome":
                                if(LIKE_CURRENT_INDEX > -1){
                                    final ListView lstViewHomeFeed = (ListView)findViewById(R.id.listView1);
                                    globalHomeFeedList.get(LIKE_CURRENT_INDEX).put("like_status","1");
                                    HomeFeedAdapter fffollowAdap = new HomeFeedAdapter(HomeActivity.this,globalHomeFeedList);
                                    Parcelable state = lstViewHomeFeed.onSaveInstanceState();
                                    lstViewHomeFeed.setAdapter(fffollowAdap);
                                    lstViewHomeFeed.onRestoreInstanceState(state);
                                    LIKE_CURRENT_INDEX = -1;
                                }
                                break;
                            case "/delLikeHome":
                                if(LIKE_CURRENT_INDEX > -1){
                                    final ListView lstViewHomeFeed = (ListView)findViewById(R.id.listView1);
                                    globalHomeFeedList.get(LIKE_CURRENT_INDEX).put("like_status","0");
                                    HomeFeedAdapter fffollowAdap = new HomeFeedAdapter(HomeActivity.this,globalHomeFeedList);
                                    Parcelable state = lstViewHomeFeed.onSaveInstanceState();
                                    lstViewHomeFeed.setAdapter(fffollowAdap);
                                    lstViewHomeFeed.onRestoreInstanceState(state);
                                    LIKE_CURRENT_INDEX = -1;
                                }
                                break;
                            case "/addLikeUser":
                                if(LIKE_CURRENT_INDEX > -1){
                                    final ListView lstViewUserFeed = (ListView)findViewById(R.id.listUser);
                                    globalUserFeedList.get(LIKE_CURRENT_INDEX).put("like_status","1");
                                    UserListFeedAdapter fffollowAdap = new UserListFeedAdapter(HomeActivity.this,globalUserFeedList);
                                    Parcelable state = lstViewUserFeed.onSaveInstanceState();
                                    lstViewUserFeed.setAdapter(fffollowAdap);
                                    lstViewUserFeed.onRestoreInstanceState(state);
                                    LIKE_CURRENT_INDEX = -1;
                                }
                                break;
                            case "/delLikeUser":
                                if(LIKE_CURRENT_INDEX > -1){
                                    final ListView lstViewUserFeed = (ListView)findViewById(R.id.listUser);
                                    globalUserFeedList.get(LIKE_CURRENT_INDEX).put("like_status","0");
                                    UserListFeedAdapter fffollowAdap = new UserListFeedAdapter(HomeActivity.this,globalUserFeedList);
                                    Parcelable state = lstViewUserFeed.onSaveInstanceState();
                                    lstViewUserFeed.setAdapter(fffollowAdap);
                                    lstViewUserFeed.onRestoreInstanceState(state);
                                    LIKE_CURRENT_INDEX = -1;
                                }
                                break;
                            case "/addFavoriteUser":
                                if(LIKE_CURRENT_INDEX > -1){
                                    final ListView lstViewUserFeed = (ListView)findViewById(R.id.listUser);
                                    globalUserFeedList.get(LIKE_CURRENT_INDEX).put("favorite_status","1");
                                    UserListFeedAdapter fffollowAdap = new UserListFeedAdapter(HomeActivity.this,globalUserFeedList);
                                    Parcelable state = lstViewUserFeed.onSaveInstanceState();
                                    lstViewUserFeed.setAdapter(fffollowAdap);
                                    lstViewUserFeed.onRestoreInstanceState(state);
                                    LIKE_CURRENT_INDEX = -1;
                                }
                                break;
                            case "/delFavoriteUser":
                                if(LIKE_CURRENT_INDEX > -1){
                                    final ListView lstViewUserFeed = (ListView)findViewById(R.id.listUser);
                                    globalUserFeedList.get(LIKE_CURRENT_INDEX).put("favorite_status","0");
                                    UserListFeedAdapter fffollowAdap = new UserListFeedAdapter(HomeActivity.this,globalUserFeedList);
                                    Parcelable state = lstViewUserFeed.onSaveInstanceState();
                                    lstViewUserFeed.setAdapter(fffollowAdap);
                                    lstViewUserFeed.onRestoreInstanceState(state);
                                    LIKE_CURRENT_INDEX = -1;
                                }
                                break;
                            case "/listCommentHome":
                                MyArrList = new ArrayList<HashMap<String, String>>();
                                Log.d("JSON_LIST",jsonResponse.getString("list"));
                                jsonArray = new JSONArray(jsonResponse.getString("list"));
                                if(jsonArray!=null){
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonRow = jsonArray.getJSONObject(i);
                                        map = new HashMap<String, String>();
                                        map.put("route_id",jsonRow.getString("route_id"));
                                        map.put("user_name", jsonRow.getString("user_name"));
                                        map.put("comment_detail", jsonRow.getString("comment_detail"));
                                        map.put("time",GetFeedTimeText(jsonRow.getString("diffDate")));
                                        MyArrList.add(map);
                                    }
                                }
                                // listView1
                                final ListView lstViewCommentHome = (ListView)findViewById(R.id.listviewHomeComment);
                                lstViewCommentHome.setAdapter(new CommentFeedAdapter(HomeActivity.this,MyArrList));
                                break;
                            case "/listCommentUser":
                                MyArrList = new ArrayList<HashMap<String, String>>();
                                Log.d("JSON_LIST",jsonResponse.getString("list"));
                                jsonArray = new JSONArray(jsonResponse.getString("list"));
                                if(jsonArray!=null){
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonRow = jsonArray.getJSONObject(i);
                                        map = new HashMap<String, String>();
                                        map.put("route_id",jsonRow.getString("route_id"));
                                        map.put("user_name", jsonRow.getString("user_name"));
                                        map.put("comment_detail", jsonRow.getString("comment_detail"));
                                        map.put("time",GetFeedTimeText(jsonRow.getString("diffDate")));
                                        MyArrList.add(map);
                                    }
                                }
                                // listView1
                                final ListView lstViewCommentUser = (ListView)findViewById(R.id.listviewUserComment);
                                lstViewCommentUser.setAdapter(new CommentFeedAdapter(HomeActivity.this,MyArrList));
                                break;
                            case "/addCommentHome":
                                //refresh comment
                                METHOD = "/listCommentHome";
                                new HomeActivity.CallServiceNoLoad().execute(AppConfig.apiUrl);
                                break;
                            case "/addCommentUser":
                                //refresh comment
                                METHOD = "/listCommentUser";
                                new HomeActivity.CallServiceNoLoad().execute(AppConfig.apiUrl);
                                break;
                            case "/addFavoriteHome":
                                if(LIKE_CURRENT_INDEX > -1){
                                    final ListView lstViewHomeFeed = (ListView)findViewById(R.id.listView1);
                                    globalHomeFeedList.get(LIKE_CURRENT_INDEX).put("favorite_status","1");
                                    HomeFeedAdapter fffollowAdap = new HomeFeedAdapter(HomeActivity.this,globalHomeFeedList);
                                    Parcelable state = lstViewHomeFeed.onSaveInstanceState();
                                    lstViewHomeFeed.setAdapter(fffollowAdap);
                                    lstViewHomeFeed.onRestoreInstanceState(state);
                                    LIKE_CURRENT_INDEX = -1;
                                }
                                break;
                            case "/delFavoriteHome":
                                if(LIKE_CURRENT_INDEX > -1){
                                    final ListView lstViewHomeFeed = (ListView)findViewById(R.id.listView1);
                                    globalHomeFeedList.get(LIKE_CURRENT_INDEX).put("favorite_status","0");
                                    HomeFeedAdapter fffollowAdap = new HomeFeedAdapter(HomeActivity.this,globalHomeFeedList);
                                    Parcelable state = lstViewHomeFeed.onSaveInstanceState();
                                    lstViewHomeFeed.setAdapter(fffollowAdap);
                                    lstViewHomeFeed.onRestoreInstanceState(state);
                                    LIKE_CURRENT_INDEX = -1;
                                }
                                break;
                        }

                    }
                    else{
                        Toast.makeText(getApplication(), jsonErrors.getString("detail"),
                                Toast.LENGTH_LONG).show();
                    }


                } catch (JSONException e) {
                    e.getMessage();
                    e.printStackTrace();
                }
                //bypass
                //R_ID = "1";


            }
        }



    }

    private class ConnectDirectionAsyncTask extends AsyncTask<Void, Void, String>{
        private ProgressDialog progressDialog;
        String url;
        ConnectDirectionAsyncTask(String urlPass){
            url = urlPass;
        }
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog = new ProgressDialog(HomeActivity.this);
            progressDialog.setMessage("Fetching route, Please wait...");
            progressDialog.setIndeterminate(true);
            progressDialog.show();
        }
        @Override
        protected String doInBackground(Void... params) {
            JSONParser jParser = new JSONParser();
            String json = jParser.getJSONFromUrl(url);
            Log.d("DEBUG DIRECTION",json);
            return json;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.hide();
            if(result!=null){
                drawPath(result);
            }
        }
    }
    public List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng( (((double) lat / 1E5)),
                    (((double) lng / 1E5) ));
            poly.add(p);
        }

        return poly;
    }
    public String makeURL (double sourcelat, double sourcelog, double destlat, double destlog ,String waypoints){
        String apikey="";
        try {
            ApplicationInfo appInfo = getApplicationContext().getPackageManager().getApplicationInfo(
                    getApplicationContext().getPackageName(), PackageManager.GET_META_DATA);
            if (appInfo.metaData != null) {
                apikey = appInfo.metaData.getString("com.google.android.geo.API_KEY");
            }
        } catch (PackageManager.NameNotFoundException e) {
// if we can’t find it in the manifest, just return null
        }
        StringBuilder urlString = new StringBuilder();
        urlString.append("https://maps.googleapis.com/maps/api/directions/json");
        urlString.append("?origin=");// from
        urlString.append(Double.toString(sourcelat));
        urlString.append(",");
        urlString
                .append(Double.toString( sourcelog));
        urlString.append("&destination=");// to
        urlString
                .append(Double.toString( destlat));
        urlString.append(",");
        urlString.append(Double.toString( destlog));
        if(!waypoints.equals("")){
            urlString.append("&waypoints="+waypoints);
        }
        urlString.append("&sensor=false&mode=driving&alternatives=true");
        urlString.append("&key="+apikey);
        return urlString.toString();
    }
    public void drawPath(String  result) {

        try {
            //Tranform the string into a json object
            final JSONObject json = new JSONObject(result);
            JSONArray routeArray = json.getJSONArray("routes");
            JSONObject routes = routeArray.getJSONObject(0);
            JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
            String encodedString = overviewPolylines.getString("points");
            List<LatLng> list = decodePoly(encodedString);
            Log.d("DEBUG POLY","Size = "+String.valueOf(list.size()));
            Polyline line = mMap.addPolyline(new PolylineOptions()
                    .addAll(list)
                    .width(14)
                    .color(Color.parseColor("#05b1fb"))//Google maps blue color
                    .geodesic(true)
            );

        }
        catch (JSONException e) {

        }
    }
    private class UploadImageService  extends AsyncTask<String, Integer, Void> {

        // Required initialization

        //private final HttpClient Client = new DefaultHttpClient();
        private String Content;
        private String Error = null;
        private int allUpload = 0;
        private int nowUpload = 0;
        private int currentIndex = 0;
        private String strImage = "";
        private ProgressDialog Dialog = new ProgressDialog(HomeActivity.this);
        String data ="";
        private boolean isConnected=false;

        protected  UploadImageService(int curindex,int curupload,String imgBase64){
            switch (UPD_METHOD){
                case "/addImageRoute":
                    allUpload = ALL_ROUTE_UPLOAD_COUNT;
                    break;
                case "/addImageRouteEdit":
                    allUpload = ALL_ROUTE_UPLOAD_COUNT;
                    break;
                case "/addImagePlace":
                    allUpload = ALL_PLACE_UPLOAD_COUNT;
                    break;
                case "/addImagePlaceEdit":
                    allUpload = ALL_PLACE_UPLOAD_COUNT;
                    break;
                case "/updateUserImage":
                    allUpload = 1;
                    break;
            }

            nowUpload = 1;
            currentIndex = curindex;
            strImage = imgBase64;
        }
        protected void onPreExecute() {

            // NOTE: You can call UI Element here.
            //Start Progress Dialog (Message)
            isConnected = CheckNetwork();
            Toast.makeText(getApplication(), "Uploading Photo  "+String.valueOf(nowUpload)+"/"+String.valueOf(allUpload)+"...",
             Toast.LENGTH_LONG).show();
            Log.d("JSON", "PreExcute");
            Log.d("JSON", "All Upload = "+ String.valueOf(allUpload));
            Log.d("JSON", "now Upload = "+ String.valueOf(nowUpload));
            Log.d("JSON", "INDEX = "+ String.valueOf(currentIndex));
            Log.d("JSON", "Image = "+ String.valueOf(strImage));
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

                    switch (UPD_METHOD){
                        case "/addImageRoute":
                            httpPostUpload = new HttpPost(urls[0]+UPD_METHOD);
                            nameValuePairs = new ArrayList<NameValuePair>(4);
                            nameValuePairs.add(new BasicNameValuePair("route_id", R_ID));
                            nameValuePairs.add(new BasicNameValuePair("ref_id", String.valueOf(nowUpload)));
                            nameValuePairs.add(new BasicNameValuePair("user_id", USER_ID));
                            nameValuePairs.add(new BasicNameValuePair("image_base_64",strImage));
                            httpPostUpload.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                            responseUpload = httpClientUpload.execute(httpPostUpload);
                            break;
                        case "/addImageRouteEdit":
                            httpPostUpload = new HttpPost(urls[0]+"/addImageRoute");
                            nameValuePairs = new ArrayList<NameValuePair>(4);
                            nameValuePairs.add(new BasicNameValuePair("route_id", PREVIEW_R_ID));
                            nameValuePairs.add(new BasicNameValuePair("ref_id", String.valueOf(nowUpload)));
                            nameValuePairs.add(new BasicNameValuePair("user_id", USER_ID));
                            nameValuePairs.add(new BasicNameValuePair("image_base_64",strImage));
                            httpPostUpload.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                            responseUpload = httpClientUpload.execute(httpPostUpload);
                            break;
                        case "/addImagePlace":
                            httpPostUpload = new HttpPost(urls[0]+UPD_METHOD);
                            nameValuePairs = new ArrayList<NameValuePair>(4);
                            nameValuePairs.add(new BasicNameValuePair("place_id", LAST_PLACE_ID));
                            nameValuePairs.add(new BasicNameValuePair("ref_id", String.valueOf(nowUpload)));
                            nameValuePairs.add(new BasicNameValuePair("user_id", USER_ID));
                            nameValuePairs.add(new BasicNameValuePair("image_base_64",strImage));
                            httpPostUpload.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                            //httpPut.addHeader("Content-Type","application/x-www-form-urlencoded");
                            responseUpload = httpClientUpload.execute(httpPostUpload);
                            break;
                        case "/addImagePlaceEdit":
                            httpPostUpload = new HttpPost(urls[0]+"/addImagePlace");
                            nameValuePairs = new ArrayList<NameValuePair>(4);
                            nameValuePairs.add(new BasicNameValuePair("place_id", EDIT_PLACE_ID));
                            nameValuePairs.add(new BasicNameValuePair("ref_id", String.valueOf(nowUpload)));
                            nameValuePairs.add(new BasicNameValuePair("user_id", USER_ID));
                            nameValuePairs.add(new BasicNameValuePair("image_base_64",strImage));
                            httpPostUpload.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                            //httpPut.addHeader("Content-Type","application/x-www-form-urlencoded");
                            responseUpload = httpClientUpload.execute(httpPostUpload);
                            break;
                        case "/updateUserImage":
                            Log.d("REQUEST_UPLOAD","Request = "+urls[0]+"/updateUserImage/"+USER_ID);
                            httpPostUpload = new HttpPost(urls[0]+"/updateUserImage/"+USER_ID);
                            nameValuePairs = new ArrayList<NameValuePair>(4);
                            nameValuePairs.add(new BasicNameValuePair("image_base_64", strImage));
                            httpPostUpload.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                            //httpPut.addHeader("Content-Type","application/x-www-form-urlencoded");
                            responseUpload = httpClientUpload.execute(httpPostUpload);
                            break;
                    }
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
                        switch (UPD_METHOD){
                            case "/addImageRoute":
                                tmpHash = new HashMap<String,String>();
                                tmpHash = allImageRouteAdd.get(currentIndex);
                                tmpHash.put("route_img_id",jsonResponse.getString("img_id"));
                                allImageRouteAdd.set(currentIndex,tmpHash);
                                Toast.makeText(getApplication(), "Upload Photo Successful.",
                                        Toast.LENGTH_LONG).show();
                                break;
                            case "/addImageRouteEdit":
                                tmpHash = new HashMap<String,String>();
                                tmpHash = allImageRouteEdit.get(currentIndex);
                                tmpHash.put("route_img_id",jsonResponse.getString("img_id"));
                                allImageRouteEdit.set(currentIndex,tmpHash);
                                Toast.makeText(getApplication(), "Upload Photo Successful.",
                                        Toast.LENGTH_LONG).show();
                                break;
                            case "/addImagePlace":
                                Toast.makeText(getApplication(), "Upload Photo Successful.",
                                        Toast.LENGTH_LONG).show();
                                break;
                            case "/addImagePlaceEdit":
                                tmpHash = new HashMap<String,String>();
                                tmpHash = allImagePinsEdit.get(currentIndex);
                                tmpHash.put("place_img_id",jsonResponse.getString("img_id"));
                                Toast.makeText(getApplication(), "Upload Photo Successful.",
                                        Toast.LENGTH_LONG).show();
                                if(nowUpload==allUpload){
                                    hideEditAddPin();
                                }
                                break;
                            case "/updateUserImage":
                                Toast.makeText(getApplication(), "Upload Photo Successful.",
                                        Toast.LENGTH_LONG).show();
                                ImageView userPhoto = (ImageView)findViewById(R.id.userPhoto);
                                Picasso.with(getApplicationContext()).load(AppConfig.imageUrl+"/"+jsonResponse.getString("file_name")).transform(new CircleTransform()).into(userPhoto);
                                break;
                        }

                    }
                    else{
                        Toast.makeText(getApplication(), jsonErrors.getString("detail"),
                                Toast.LENGTH_LONG).show();
                    }


                } catch (JSONException e) {
                    e.getMessage();
                    e.printStackTrace();
                }
                //bypass
                //R_ID = "1";
                METHOD="";

            }
        }



    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if(mapType==1){ //map add pin
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        // granted
                        WorkaroundMapFragment mapFragment = (WorkaroundMapFragment) getSupportFragmentManager()
                                .findFragmentById(R.id.map);
                        mapFragment.getMapAsync(HomeActivity.this);
                        final ScrollView mScrollView = (ScrollView) findViewById(R.id.scrollCreatePin); //parent scrollview in xml, give your scrollview id value

                        ((WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                                .setListener(new WorkaroundMapFragment.OnTouchListener() {
                                    @Override
                                    public void onTouch() {
                                        mScrollView.requestDisallowInterceptTouchEvent(true);
                                    }
                                });
                    }
                    else{
                        // no granted
                        ClearAddPinView();
                    }
                }else if(mapType==2){ //map edit pin
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        // granted
                        WorkaroundMapFragment mapFragment = (WorkaroundMapFragment) getSupportFragmentManager()
                                .findFragmentById(R.id.mapEdit);
                        mapFragment.getMapAsync(HomeActivity.this);
                        final ScrollView mScrollView = (ScrollView) findViewById(R.id.scrollCreatePin); //parent scrollview in xml, give your scrollview id value

                        ((WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapEdit))
                                .setListener(new WorkaroundMapFragment.OnTouchListener() {
                                    @Override
                                    public void onTouch() {
                                        mScrollView.requestDisallowInterceptTouchEvent(true);
                                    }
                                });
                    }
                    else{
                        // no granted
                        RelativeLayout layoutEditPin = (RelativeLayout)findViewById(R.id.layoutEditPin);
                        layoutEditPin.setVisibility(View.GONE);
                        RelativeLayout layoutPreview = (RelativeLayout)findViewById(R.id.layoutAddPinPreview);
                        layoutPreview.setVisibility(View.VISIBLE);
                    }
                }
                else if(mapType==3){ //map view all pin
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        // granted
                        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                                .findFragmentById(R.id.mapViewAllPins);
                        mapFragment.getMapAsync(HomeActivity.this);
//                        final ScrollView mScrollView = (ScrollView) findViewById(R.id.scrollCreatePin); //parent scrollview in xml, give your scrollview id value
//
//                        ((WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapEdit))
//                                .setListener(new WorkaroundMapFragment.OnTouchListener() {
//                                    @Override
//                                    public void onTouch() {
//                                        mScrollView.requestDisallowInterceptTouchEvent(true);
//                                    }
//                                });
                    }
                    else{
                        // no granted
                        RelativeLayout layoutAddpinPreview = (RelativeLayout)findViewById(R.id.layoutAddPinPreview);
                        layoutAddpinPreview.setVisibility(View.VISIBLE);
                        RelativeLayout layoutViewAllpins = (RelativeLayout)findViewById(R.id.layoutViewAllPins);
                        layoutViewAllpins.setVisibility(View.GONE);
                    }
                }
                else if(mapType==4){ //map view all pin
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        // granted
                        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                                .findFragmentById(R.id.mapFeedViewAllPins);
                        mapFragment.getMapAsync(HomeActivity.this);
//                        final ScrollView mScrollView = (ScrollView) findViewById(R.id.scrollCreatePin); //parent scrollview in xml, give your scrollview id value
//
//                        ((WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapEdit))
//                                .setListener(new WorkaroundMapFragment.OnTouchListener() {
//                                    @Override
//                                    public void onTouch() {
//                                        mScrollView.requestDisallowInterceptTouchEvent(true);
//                                    }
//                                });
                    }
                    else{
                        // no granted
                        RelativeLayout layoutFeedViewAllPins = (RelativeLayout)findViewById(R.id.layoutFeedViewAllPins);
                        RelativeLayout layoutFeedPreview = (RelativeLayout)findViewById(R.id.layoutHomePreview);
                        layoutFeedPreview.setVisibility(View.VISIBLE);
                        layoutFeedViewAllPins.setVisibility(View.GONE);
                    }
                }
                else if(mapType==5){ //map view all pin
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        // granted
                        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                                .findFragmentById(R.id.mapUserViewAllPins);
                        mapFragment.getMapAsync(HomeActivity.this);
//                        final ScrollView mScrollView = (ScrollView) findViewById(R.id.scrollCreatePin); //parent scrollview in xml, give your scrollview id value
//
//                        ((WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapEdit))
//                                .setListener(new WorkaroundMapFragment.OnTouchListener() {
//                                    @Override
//                                    public void onTouch() {
//                                        mScrollView.requestDisallowInterceptTouchEvent(true);
//                                    }
//                                });
                    }
                    else{
                        // no granted
                        RelativeLayout layoutUserViewAllPins = (RelativeLayout)findViewById(R.id.layoutUserViewAllPins);
                        RelativeLayout layoutUserPreview = (RelativeLayout)findViewById(R.id.layoutUserPreview);
                        layoutUserPreview.setVisibility(View.VISIBLE);
                        layoutUserViewAllPins.setVisibility(View.GONE);
                    }
                }
                else if(mapType==6){ //map view all pin
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        // granted
                        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                                .findFragmentById(R.id.mapExploreViewAllPins);
                        mapFragment.getMapAsync(HomeActivity.this);
//                        final ScrollView mScrollView = (ScrollView) findViewById(R.id.scrollCreatePin); //parent scrollview in xml, give your scrollview id value
//
//                        ((WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapEdit))
//                                .setListener(new WorkaroundMapFragment.OnTouchListener() {
//                                    @Override
//                                    public void onTouch() {
//                                        mScrollView.requestDisallowInterceptTouchEvent(true);
//                                    }
//                                });
                    }
                    else{
                        // no granted
                        RelativeLayout layoutExploreViewAllPins = (RelativeLayout)findViewById(R.id.layoutExploreViewAllPins);
                        RelativeLayout layoutExplorePreview = (RelativeLayout)findViewById(R.id.layoutExplorePreview);
                        layoutExplorePreview.setVisibility(View.VISIBLE);
                        layoutExploreViewAllPins.setVisibility(View.GONE);
                    }
                }
                else if(mapType==7){ //map home view pin
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        WorkaroundMapFragment mapFragment = (WorkaroundMapFragment) getSupportFragmentManager()
                                .findFragmentById(R.id.mapHomeViewPin);
                        mapFragment.getMapAsync(HomeActivity.this);
                        final ScrollView mScrollView = (ScrollView) findViewById(R.id.scrollHomePinPreview); //parent scrollview in xml, give your scrollview id value

                        ((WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapHomeViewPin))
                                .setListener(new WorkaroundMapFragment.OnTouchListener() {
                                    @Override
                                    public void onTouch() {
                                        mScrollView.requestDisallowInterceptTouchEvent(true);
                                    }
                                });
                    }
                    else{
                        // no granted
                        RelativeLayout layoutHomePinPreview = (RelativeLayout)findViewById(R.id.layoutHomePinPreview);
                        RelativeLayout layoutHomePreview = (RelativeLayout)findViewById(R.id.layoutHomePreview);
                        layoutHomePinPreview.setVisibility(View.GONE);
                        layoutHomePreview.setVisibility(View.VISIBLE);
                    }
                }
                else if(mapType==8){ //map explore view pin
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        WorkaroundMapFragment mapFragment = (WorkaroundMapFragment) getSupportFragmentManager()
                                .findFragmentById(R.id.mapExploreViewPin);
                        mapFragment.getMapAsync(HomeActivity.this);
                        final ScrollView mScrollView = (ScrollView) findViewById(R.id.scrollExplorePinPreview); //parent scrollview in xml, give your scrollview id value

                        ((WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapExploreViewPin))
                                .setListener(new WorkaroundMapFragment.OnTouchListener() {
                                    @Override
                                    public void onTouch() {
                                        mScrollView.requestDisallowInterceptTouchEvent(true);
                                    }
                                });
                    }
                    else{
                        // no granted
                        RelativeLayout layoutHomePinPreview = (RelativeLayout)findViewById(R.id.layoutExplorePinPreview);
                        RelativeLayout layoutHomePreview = (RelativeLayout)findViewById(R.id.layoutExplorePreview);
                        layoutHomePinPreview.setVisibility(View.GONE);
                        layoutHomePreview.setVisibility(View.VISIBLE);
                    }
                }
                else if(mapType==9){ //map user view pin
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        WorkaroundMapFragment mapFragment = (WorkaroundMapFragment) getSupportFragmentManager()
                                .findFragmentById(R.id.mapUserViewPin);
                        mapFragment.getMapAsync(HomeActivity.this);
                        final ScrollView mScrollView = (ScrollView) findViewById(R.id.scrollUserPinPreview); //parent scrollview in xml, give your scrollview id value

                        ((WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapUserViewPin))
                                .setListener(new WorkaroundMapFragment.OnTouchListener() {
                                    @Override
                                    public void onTouch() {
                                        mScrollView.requestDisallowInterceptTouchEvent(true);
                                    }
                                });
                    }
                    else{
                        // no granted
                        RelativeLayout layoutHomePinPreview = (RelativeLayout)findViewById(R.id.layoutUserPinPreview);
                        RelativeLayout layoutHomePreview = (RelativeLayout)findViewById(R.id.layoutUserPreview);
                        layoutHomePinPreview.setVisibility(View.GONE);
                        layoutHomePreview.setVisibility(View.VISIBLE);
                    }
                }
                return;
            }
            case REQUEST_PERMISSION_CAMERA :
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // granted
                    FileUtils.createDefaultFolder(HomeActivity.this);
                    final File file = FileUtils.createFile(FileUtils.IMAGE_FILE);
                    outputFileUri = Uri.fromFile(file);

                    // Camera.
                    final Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

                    //final Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    //galleryIntent.setType("image/*");
                    // Filesystems
                    // galleryIntent.setAction(Intent.ACTION_GET_CONTENT); // To allow file managers or any other app that are not gallery app.

                    //final Intent chooserIntent = Intent.createChooser(galleryIntent, "Select Image");

                    final Intent chooserIntent = Intent.createChooser(captureIntent, "Select Image");
                    // Add the camera options.
                    //chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] { captureIntent });
                    startActivityForResult(captureIntent, REQUEST_CAMERA);
                }
                else{
                    // no granted

                }
                break;
            case REQUEST_PERMISSION_CAMERA_GALLERY :
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // granted
                    FileUtils.createDefaultFolder(HomeActivity.this);
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
                    startActivityForResult(chooserIntent, REQUEST_CAMERA);
                }
                else{
                    // no granted

                }
                break;
            case REQUEST_PERMISSION_CAMERA_GALLERY_PROFILE :
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // granted
                    FileUtils.createDefaultFolder(HomeActivity.this);
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
                break;
            case MainUtility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.startsWith("TP_"))
                        cameraIntent();
                    else if(userChoosenTask.startsWith("CH_"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;

        }

    }
}
