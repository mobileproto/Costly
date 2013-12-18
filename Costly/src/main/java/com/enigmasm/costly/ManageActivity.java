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
 * Created by kaustin on 10/16/13.
 */
public class ManageActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Firebase ref = new Firebase("https://costly.firebaseIO-demo.com/converts");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);

        Intent intent = getIntent();

        //File system
        final TextView name = (TextView) findViewById(R.id.nameItemField);
        final TextView price = (TextView) findViewById(R.id.priceField);


        List<String> files = new ArrayList<String>(Arrays.asList(fileList()));

        final ItemListAdapter aa = new ItemListAdapter(this, android.R.layout.simple_list_item_1, files);

        final ListView notes = (ListView) findViewById(R.id.itemList);

        notes.setAdapter(aa);


        //Retrieving past DB information to display
        String[] oldCols = {SpenDBHelper.FeedEntry._ID,
                SpenDBHelper.FeedEntry.COLUMN_NAME,
                SpenDBHelper.FeedEntry.COLUMN_PRICE};
        //Turning DB into string array
        Cursor savedDB = MainActivity.mDBHelper.getWritableDatabase().query(SpenDBHelper.FeedEntry.TABLE_NAME,
                oldCols, null, null, null, null, null);

        savedDB.moveToFirst();
        while(!savedDB.isAfterLast()){
            String itemName = savedDB.getString(1);
            System.out.println("RETRIEVED: " + itemName);
            aa.insert(itemName, 0);
            aa.notifyDataSetChanged();
            savedDB.moveToNext();
        }

        //Adding the item to the database
        Button add = (Button)findViewById(R.id.addButton);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Gets the data repository in write mode
                SQLiteDatabase db = MainActivity.mDBHelper.getWritableDatabase();

                //Gets the text created by the user
                String itemName = name.getText().toString();
                String itemPrice = price.getText().toString();
                if (itemName != null && itemPrice != null){
                    // Create a new map of values, where column names are the keys
                    ContentValues values = new ContentValues();
                    values.put(SpenDBHelper.FeedEntry.COLUMN_NAME, itemName);
                    values.put(SpenDBHelper.FeedEntry.COLUMN_PRICE, itemPrice);
                    DiscoverItem item = new DiscoverItem(itemName,itemPrice);
                    ref.push().setValue(item);

                    System.out.println("ADDED TO DATABASE");
                    // Insert the new row, returning the primary key value of the new row
                    long newRowId;
                    newRowId = db.insert(
                            SpenDBHelper.FeedEntry.TABLE_NAME,
                            null,
                            values);
                    aa.insert(itemName,aa.getCount());
                    aa.notifyDataSetChanged();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
