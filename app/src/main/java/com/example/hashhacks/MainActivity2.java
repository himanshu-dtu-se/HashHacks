package com.example.hashhacks;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class MainActivity2 extends AppCompatActivity implements View.OnClickListener {

    private FloatingActionButton fab;
    private PopupMenu popupMenu;

    private static final int IMAGE_CAPTURE = 994;
    private static final int IDENTITY = 568;
    private static final int PROPERTY = 865;
    private static final int BANK = 948;

    private String name;
    private String PATH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        SharedPreferences preferences = this.getSharedPreferences("login_prefs", MODE_PRIVATE);
        name = preferences.getString("username",null);
        setTitle("Hello, "+name);

        fab = (FloatingActionButton)findViewById(R.id.capture);
        fab.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.ac_logout:

                SharedPreferences preferences = this.getSharedPreferences("login_prefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("loggedin",false);
                editor.putString("username",null);
                editor.commit();

                finish();
                startActivity(
                        new Intent(MainActivity2.this, LoginActivity.class)
                );

                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        popupMenu = new PopupMenu(MainActivity2.this,v);

        Toast to = Toast.makeText(getApplicationContext(), "Choose what to upload", Toast.LENGTH_LONG);
        to.setGravity(Gravity.CENTER,0,0);
        to.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()){

                    case R.id.pop_identity:

                        takepic(IDENTITY);

                        return true;

                    case R.id.pop_property:

                        takepic(PROPERTY);

                        return true;

                    case R.id.pop_bank:

                        takepic(BANK);

                        return true;


                }

                return false;
            }
        });
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();
    }

    private void takepic(int which){

        String path = Environment.getExternalStorageDirectory().toString() + "/borrowchain/user/" + name;

        File dir = new File(path);
        if(!dir.exists()){
            dir.mkdir();
        }
        String filename="";
        if(which==IDENTITY){
            filename = "IdentityProof.jpg";


        }else if(which==PROPERTY){
            filename = "PropertyProof.jpg";


        }else if(which==BANK){
            filename = "BankStatement.jpg";


        }

        File image = new File(dir,filename);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(image));
        intent.putExtra("return-data",true);
        startActivityForResult(intent, IMAGE_CAPTURE);
        PATH = image.getAbsolutePath();


    }
}
