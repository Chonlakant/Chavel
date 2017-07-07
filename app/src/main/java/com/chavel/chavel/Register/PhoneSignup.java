package com.chavel.chavel.Register;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.chavel.chavel.MainActivity;
import com.chavel.chavel.PrivacyActivity;
import com.chavel.chavel.R;
import com.chavel.chavel.StaticActivity;
import com.chavel.chavel.Utility.CountryCodes;

import org.w3c.dom.Text;


public class PhoneSignup extends Activity {
    public  String prefixTel = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_signup);
        buttonEvent();
        assignLinkInText();
        Spinner s = ( Spinner )findViewById( R.id.spTel );  // id of your spinner
        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                TextView lblTel = (TextView)findViewById(R.id.lblTel);
                String cCode = CountryCodes.getCode(position);
                lblTel.setText("+"+cCode);
                prefixTel = "+"+cCode;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });
        CountryCodes cc = new CountryCodes( this );
        s.setAdapter( cc );
//        TelephonyManager man = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        TextView lblTel = (TextView)findViewById(R.id.lblTel);

        int index = CountryCodes.getIndex("TH");
        String cCode = CountryCodes.getCode(index);
        lblTel.setText("+"+cCode);
        prefixTel = "+"+cCode;
        if( index > -1 )
        {
            s.setSelection( index );
        }
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
                do {
                    EditText txtphone = (EditText) findViewById(R.id.txtPhone);
                    String phone = txtphone.getText().toString();
                    if (TextUtils.isEmpty(phone)) {
                        //Log.d("DEBUG","IS Empty");
                        Toast.makeText(getApplication(), "กรุณาระบุหมายเลขโทรศัพท์",
                                Toast.LENGTH_LONG).show();
                        break;
                    } else {
                        //Log.d("DEBUG",phone);
                        if (phone.length() < 9) {
                            Toast.makeText(getApplication(), "กรุณาระบุหมายเลขโทรศัพท์ให้ครบถ้วน",
                                    Toast.LENGTH_LONG).show();
                            break;
                        }
                        Intent takepic = new Intent(getApplicationContext(),PhotoSignup.class);
                        takepic.putExtra("phone",prefixTel+phone);
                        takepic.putExtra("email","");
                        startActivity(takepic);
                    }
                } while (false);

            }

        });
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
    public void assignLinkInText(){
        //assign link to text
        SpannableString ss = new SpannableString("Already have an account? Sign In.");

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Intent home = new Intent(PhoneSignup.this,MainActivity.class);
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
                Intent privacyPage = new Intent(getApplicationContext(), PrivacyActivity.class);
                startActivity(privacyPage);
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

        TextView lblSignupEmail = (TextView)findViewById(R.id.lblSignupEmail);
        lblSignupEmail.setClickable(true);
        lblSignupEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailSignup = new Intent(PhoneSignup.this,EmailSigup.class);
                emailSignup.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(emailSignup);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_phone_signup, menu);
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
