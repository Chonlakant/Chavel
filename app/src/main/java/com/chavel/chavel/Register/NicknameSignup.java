package com.chavel.chavel.Register;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

import com.chavel.chavel.MainActivity;
import com.chavel.chavel.R;


public class NicknameSignup extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nickname_signup);
        buttonEvent();
        assignLinkInText();
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
                Intent findfb = new Intent(getApplicationContext(),FindFBSignup.class);
                startActivity(findfb);
            }
        });
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

        ss = new SpannableString("By signing up,you agree to our\nTerms & Privacy Policy.");
        ClickableSpan privacySpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Log.d("Privacy Link", "Click Link !");
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                ds.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                ds.setColor(Color.WHITE);
            }
        };
        ss.setSpan(privacySpan, 31, 54, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        TextView txtPrivacy = (TextView) findViewById(R.id.lblPrivacy);
        txtPrivacy.setText(ss);
        txtPrivacy.setMovementMethod(LinkMovementMethod.getInstance());
        txtPrivacy.setHighlightColor(Color.TRANSPARENT);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_nickname_signup, menu);
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
}
