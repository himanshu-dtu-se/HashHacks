package com.example.hashhacks;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity2 extends AppCompatActivity implements View.OnClickListener {

    private FloatingActionButton fab;
    private PopupMenu popupMenu;

    private static final int IMAGE_CAPTURE = 994;
    private static final int IDENTITY = 568;
    private static final int PROPERTY = 865;
    private static final int BANK = 948;

    private RecyclerView recyclerView;
    private List<CardData> cardDataList;

    private RecyclerAdapter adapter;

    String name,path;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);

        cardDataList = new ArrayList<>();
        adapter = new RecyclerAdapter(MainActivity2.this, cardDataList);

        recyclerView.setAdapter(adapter);

        SharedPreferences preferences = this.getSharedPreferences("login_prefs", MODE_PRIVATE);
        name = preferences.getString("username",null);

        setTitle("Hello, "+name);

        fab = findViewById(R.id.capture);
        fab.setOnClickListener(this);

        path = Environment.getExternalStorageDirectory().toString() + "/borrowchain/user/" + name;

        load_data();

    }

    private void load_data() {

        File directory = new File(path);
        File[] files = directory.listFiles();
        if(files!=null){
            for(File file : files){
                CardData data = new CardData(file.getAbsolutePath(), file.getName());
                cardDataList.add(data);
            }
        }else {
            Toast.makeText(getApplicationContext(), "No Images Found. Try Uploading them using Camera button", Toast.LENGTH_LONG).show();
        }

        adapter.notifyDataSetChanged();
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

                        validate(IDENTITY);

                        return true;

                    case R.id.pop_property:

                        validate(PROPERTY);

                        return true;

                    case R.id.pop_bank:

                        validate(BANK);

                        return true;


                }

                return false;
            }
        });
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();
    }

    private void validate(int which){

        if(which==IDENTITY){

            final SharedPreferences preferences = this.getSharedPreferences("file_prefs",MODE_PRIVATE);

            if(preferences.getBoolean("identity", false)){

                new AlertDialog.Builder(MainActivity2.this).
                        setMessage("Image Already Exists. Want to replace?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                take_pic("IdentityProof.jpg");

                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putBoolean("identity",true);
                                editor.commit();

                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create().show();

            }else {
                take_pic("IdentityProof.jpg");
            }

        }else if(which==PROPERTY) {

            final SharedPreferences preferences = this.getSharedPreferences("file_prefs", MODE_PRIVATE);

            if (preferences.getBoolean("property", false)) {

                new AlertDialog.Builder(MainActivity2.this).
                        setMessage("Image Already Exists. Want to replace?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putBoolean("property", true);
                                editor.commit();

                                take_pic("PropertyProof.jpg");
                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create().show();

            }else {
                take_pic("PropertyProof.jpg");
            }
        }
        else if(which==BANK) {

            final SharedPreferences preferences = this.getSharedPreferences("file_prefs", MODE_PRIVATE);

            if (preferences.getBoolean("bank", false)) {

                new AlertDialog.Builder(MainActivity2.this).
                        setMessage("Image Already Exists. Want to replace?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putBoolean("bank", true);
                                editor.commit();

                                take_pic("BankProof.jpg");
                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create().show();

            }else {
                take_pic("BankProof.jpg");
            }

        }
    }

    private void take_pic(String filename) {

        File dir = new File(path);
        if(!dir.exists()){
            dir.mkdir();
        }

        File image = new File(dir,filename);
        if(image.exists()){
            image.delete();
            try {
                image.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(image));
        intent.putExtra("return-data",true);
        startActivityForResult(intent, IMAGE_CAPTURE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==IMAGE_CAPTURE){
            finish();
            startActivity(getIntent());
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
