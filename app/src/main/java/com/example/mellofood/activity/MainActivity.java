package com.example.mellofood.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.mellofood.R;
import com.example.mellofood.database.UserDBHelper;

public class MainActivity extends AppCompatActivity {
    static Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_main);
        overridePendingTransition(R.anim.do_not_move, R.anim.do_not_move);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        splsh splsh = new splsh();
        /*UserDBHelper userDBHelper = new UserDBHelper(this);
        SQLiteDatabase db = userDBHelper.getWritableDatabase();
        userDBHelper.dropTable(db);*/
        splsh.start();
        activity= MainActivity.this;

    }

    private class splsh extends Thread {
        @Override
        public void run() {
            try {
                sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            /*UserDBHelper userDBHelper = new UserDBHelper(MainActivity.this);
            SQLiteDatabase db = userDBHelper.getWritableDatabase();
            userDBHelper.dropTable(db);
            userDBHelper.close();*/
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.putExtra("ActivityName","MainActivity");
            startActivity(intent);
        }
    }
}
