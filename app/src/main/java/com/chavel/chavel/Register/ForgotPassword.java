package com.chavel.chavel.Register;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.chavel.chavel.R;

public class ForgotPassword extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        buttonEvent();
    }
    public void buttonEvent(){
        ImageButton btnBack = (ImageButton)findViewById(R.id.btnForgotBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Button btnSubmitForgot = (Button) findViewById(R.id.btnSubmitForgot);
        btnSubmitForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_item));
//                do {
//                    EditText txtphone = (EditText) findViewById(R.id.txtPhone);
//                    String email = txtphone.getText().toString();
//                    if (TextUtils.isEmpty(email)) {
//                        //Log.d("DEBUG","IS Empty");
//                        Toast.makeText(getApplication(), "กรุณาระบุอีเมล์",
//                                Toast.LENGTH_LONG).show();
//                        break;
//                    } else {
//                        //Log.d("DEBUG",phone);
//                        if (!isEmailValid(email)) {
//                            Toast.makeText(getApplication(), "กรุณาระบุอีเมล์ให้ถูกต้อง",
//                                    Toast.LENGTH_LONG).show();
//                            break;
//                        }
//                        Intent takepic = new Intent(getApplicationContext(), PhotoSignup.class);
//                        takepic.putExtra("phone","");
//                        takepic.putExtra("email", email);
//                        startActivity(takepic);
//                    }
//                } while (false);
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
}
