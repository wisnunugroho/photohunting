package com.wisnu.photohunting.ui.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.orhanobut.hawk.Hawk;
import com.wisnu.photohunting.PhotoHuntingApp;
import com.wisnu.photohunting.R;
import com.wisnu.photohunting.model.User;
import com.wisnu.photohunting.savingstate.UserData;
import com.wisnu.photohunting.system.Utils;

import static android.view.View.*;

public class WelcomeActivity extends AppCompatActivity implements OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        TextView signIn = (TextView) findViewById(R.id.welcome_btn_signin);
        TextView signUp = (TextView) findViewById(R.id.welcome_btn_signup);

        signIn.setOnClickListener(this);
        signUp.setOnClickListener(this);

        signUp.setPaintFlags(signUp.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        if (Hawk.get(PhotoHuntingApp.USER_ID) != null) {
            User user = new User();
            user.setUserId((String) Hawk.get(PhotoHuntingApp.USER_ID));
            user.setUserName((String) Hawk.get(PhotoHuntingApp.USER_NAME));
            user.setUserEmail((String) Hawk.get(PhotoHuntingApp.USER_MAIL));
            user.setUserPhotoProfile((String) Hawk.get(PhotoHuntingApp.USER_PHOTO));
            user.setUserStatus((String) Hawk.get(PhotoHuntingApp.USER_STATUS));

            UserData.getInstance().setUser(user);
            Utils.showOnConsole("onResponse", "user Id : " + user.getUserId());
            Utils.showOnConsole("onResponse", "userName : " + user.getUserName());
            Utils.showOnConsole("onResponse", "userMail : " + user.getUserEmail());

            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.welcome_btn_signin:
                startActivity(new Intent(this, SignInActivity.class));
                finish();
                break;
            case R.id.welcome_btn_signup:
                startActivity(new Intent(this, RegisterActivity.class));
                finish();
                break;
        }
    }
}
