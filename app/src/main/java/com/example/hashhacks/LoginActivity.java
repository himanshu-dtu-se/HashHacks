package com.example.hashhacks;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int WRITING_PERMISSION = 100;
    private EditText et_user, et_pass;
    private Button login;
    private CheckBox register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences preferences = this.getSharedPreferences("login_prefs", MODE_PRIVATE);
        if(preferences.getBoolean("loggedin", false)==true){
            finish();
            startActivity(
                    new Intent(LoginActivity.this, MainActivity2.class)
            );
        }

        et_user = (EditText)findViewById(R.id.login_user);
        et_pass = (EditText)findViewById(R.id.login_pass);

        login = (Button)findViewById(R.id.login_button);
        login.setOnClickListener(this);
        register = (CheckBox)findViewById(R.id.register_box);

        if(PermissionChecker.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)==PermissionChecker.PERMISSION_DENIED){

            ActivityCompat.requestPermissions(this , new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITING_PERMISSION);

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode==WRITING_PERMISSION){

            if(grantResults[0]==PermissionChecker.PERMISSION_GRANTED){
                //Permission Granted
            }else {
                Toast.makeText(getApplicationContext(), "Allow app to read and write files", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(this , new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITING_PERMISSION);
            }

        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.login_button:

                if(!TextUtils.isEmpty(et_user.getText().toString()) && !TextUtils.isEmpty(et_pass.getText().toString())){
                    SharedPreferences sharedPreferences = this.getSharedPreferences("login_prefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("username", et_user.getText().toString());
                    editor.putBoolean("loggedin", true);
                    editor.commit();
                    finish();
                    startActivity(
                            new Intent(LoginActivity.this, MainActivity2.class)
                    );
                }else {
                    Toast.makeText(getApplicationContext(), "Enter Valid Credentials", Toast.LENGTH_LONG).show();
                }
                break;

        }
    }
}
