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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.chavel.chavel.R;


public class SignUpPortal extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_portal);
        ImageButton btnBack = (ImageButton)findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //assign link to text
        SpannableString ss = new SpannableString("Already have an account? Sign In.");

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                finish();
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

        ss = new SpannableString("Sign Up With Phone or Email");
        ClickableSpan phoneRegis = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Intent signup = new Intent(getApplicationContext(),PhoneSignup.class);
                startActivity(signup);
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true);
                ds.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                ds.setColor(Color.WHITE);
            }
        };
        ClickableSpan emailRegis = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Intent signup = new Intent(getApplicationContext(),EmailSigup.class);
                startActivity(signup);
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true);
                ds.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                ds.setColor(Color.WHITE);
            }
        };
        ss.setSpan(phoneRegis, 13, 18, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(emailRegis, 22, 27, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        TextView txtWay = (TextView) findViewById(R.id.lblRegisway);
        txtWay.setText(ss);
        txtWay.setMovementMethod(LinkMovementMethod.getInstance());
        txtWay.setHighlightColor(Color.TRANSPARENT);
        //.assign link to text
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up_portal, menu);
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
