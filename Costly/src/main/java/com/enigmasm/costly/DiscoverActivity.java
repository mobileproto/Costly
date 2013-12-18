package com.enigmasm.costly;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by lego6245 on 12/12/13.
 */
public class DiscoverActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover);

        Intent intent = getIntent();

        //File system


        List<String> files = new ArrayList<String>(Arrays.asList(fileList()));
        Firebase ref = new Firebase("https://costly.firebaseIO-demo.com/converts");

        final DiscoverListAdapter aa = new DiscoverListAdapter(ref, this, R.layout.discover_item);

        final ListView notes = (ListView) findViewById(R.id.firebaseList);

        notes.setAdapter(aa);

        //Adding the item to the database

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
