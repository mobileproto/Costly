package com.enigmasm.costly;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.client.Query;

/**
 * Created by lego6245 on 12/12/13.
 */
public class DiscoverListAdapter extends FirebaseListAdapter<DiscoverItem> {

    // The username for this client. We use this to indicate which messages originated from this user

    public DiscoverListAdapter(Query ref, Activity activity, int layout) {
        super(ref, DiscoverItem.class, layout, activity);
    }
    @Override
    protected void populateView(View view, DiscoverItem item) {
        String name = item.getName();
        TextView nameText = (TextView)view.findViewById(R.id.nameText);
        nameText.setText(name);
        ((TextView)view.findViewById(R.id.conversionText)).setText(item.getConversion());

        Button add = (Button)view.findViewById(R.id.addButton);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Gets the data repository in write mode
                SQLiteDatabase db = MainActivity.mDBHelper.getWritableDatabase();

                //Gets the text created by the user
                String itemName = ((TextView)view.findViewById(R.id.nameText)).getText().toString();
                String itemPrice = ((TextView)view.findViewById(R.id.conversionText)).getText().toString();
                if (itemName != null && itemPrice != null){
                    // Create a new map of values, where column names are the keys
                    ContentValues values = new ContentValues();
                    values.put(SpenDBHelper.FeedEntry.COLUMN_NAME, itemName);
                    values.put(SpenDBHelper.FeedEntry.COLUMN_PRICE, itemPrice);

                    // Insert the new row, returning the primary key value of the new row
                    long newRowId;
                    newRowId = db.insert(
                            SpenDBHelper.FeedEntry.TABLE_NAME,
                            null,
                            values);
                }
            }
        });
    }
}

