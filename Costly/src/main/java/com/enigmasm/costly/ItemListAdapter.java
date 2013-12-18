package com.enigmasm.costly;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

/**
 * Created by kaustin on 10/16/13.
 */
public class ItemListAdapter extends ArrayAdapter {

    private List<String> data;
    private Activity activity;

    public ItemListAdapter(Activity a, int viewResourceId, List<String> data){
        super(a, viewResourceId, data);
        this.data = data;
        this.activity = a;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        //Getting the database in the MainActivity
        final SQLiteDatabase db = MainActivity.mDBHelper.getWritableDatabase();


        View v = convertView;
        if (v==null){
            LayoutInflater vi = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.manage_item, null);
        }

        Button del = (Button) v.findViewById(R.id.deleteButton);
        final TextView name = (TextView) v.findViewById(R.id.titleTextView);
        name.setText(data.get(position));

        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String itemName = name.getText().toString();
                activity.deleteFile(itemName);
                data.remove(position);

                //Delete from the database
                // Define 'where' part of query.
                String selection = SpenDBHelper.FeedEntry.COLUMN_NAME + " LIKE ?";
                // Specify arguments in placeholder order.
                String[] selectionArgs = { String.valueOf(itemName) };
                // Issue SQL statement.
                db.delete("items", selection, selectionArgs);

                ItemListAdapter.this.notifyDataSetChanged();
            }
        });
        Animation in = AnimationUtils.loadAnimation(this.getContext(), android.R.anim.fade_in);
        v.startAnimation(in);

        return v;
    }

}
