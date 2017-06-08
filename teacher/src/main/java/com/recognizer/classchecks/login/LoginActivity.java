package com.recognizer.classchecks.login;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.library.basic.BasicActivity;
import com.library.utils.EditTextClearTools;
import com.library.widget.CustomToolbar;
import com.library.widget.ToastUtils;
import com.recognizer.R;
import com.recognizer.classchecks.home.HomeActivity;
import com.recognizer.classchecks.login.fragment.LoginFragment;
import com.recognizer.classchecks.login.fragment.RegisterFragment;

public class LoginActivity extends BasicActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(null == savedInstanceState) {
            addFragment(new LoginFragment());
        }
    }

    @Override
    protected void createToolbar() {
        mCustomToolbar = (CustomToolbar) findViewById(R.id.custom_toolbar_login);
        mCustomToolbar.setMainTitleLeftDrawable(null);
        mCustomToolbar.setMainTitle(getString(R.string.activity_login_main_title));
        mCustomToolbar.setMainTitleRightText(getString(R.string.register_text));
        mCustomToolbar.setMainTitleRightOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fl_fragment_login_container, new RegisterFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_login;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_login) {
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            finish();
        }
    }

    private void addFragment(Fragment fragment) {
        FragmentManager fm= getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.fl_fragment_login_container, fragment);
        ft.commit();
    }

}
