package com.chavel.chavel.Home;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.chavel.chavel.R;

public class DetailModalActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_detail_modal);
        //setPosition(15);
//        Button dismissbutton = (Button) findViewById(R.id.w_dismiss_btn);
//        dismissbutton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DetailModalActivity.this.finish();
//            }
//        });
        try{
            String versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            TextView lblVersion = (TextView)findViewById(R.id.lblVersion);
            lblVersion.setText("Chavel v."+versionName);
        }catch (PackageManager.NameNotFoundException ex){

        }

    }

}
