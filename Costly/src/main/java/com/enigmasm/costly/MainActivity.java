package com.enigmasm.costly;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity {

    static Map<String, Double> conversions = new HashMap<String, Double>();
    public static SpenDBHelper mDBHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDBHelper = new SpenDBHelper(this);
        refreshMap();
        Log.println(Log.INFO, "status", "cleared database");
        final EditText inputText = (EditText)this.findViewById(R.id.inputText);
        final TextView resultText = (TextView)this.findViewById(R.id.resultText);
        resultText.setText("");
        conversions.put("five dollar footlongs", 5.0);
        conversions.put("brand new video games", 59.99);
        conversions.put("hours of work", 8.5);

        Button convert = (Button)this.findViewById(R.id.costButton);
        convert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = (int)Math.floor(Math.random() * conversions.keySet().size());
                String s = (String)conversions.keySet().toArray()[i];
                double trueCost = Double.parseDouble(inputText.getText().toString()) / conversions.get(s);
                resultText.setText(new DecimalFormat("#.##").format(trueCost) + " " + s);

            }
        });
        Log.println(Log.INFO, "status", "cleared onclickone");
        Button manage = (Button) findViewById(R.id.manageButton);

        manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(MainActivity.this, ManageActivity.class);
                startActivityForResult(in, 1);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        refreshMap();
    }

    private void refreshMap() {
        conversions.clear();
        final TextView resultText = (TextView)this.findViewById(R.id.resultText);
        resultText.setText("");
        Cursor cursor = mDBHelper.getReadableDatabase().rawQuery("select * from " + SpenDBHelper.FeedEntry.TABLE_NAME, null);
        Log.println(Log.INFO, "status", "cleared cursor");
        if(cursor.moveToFirst()) {
            while(!cursor.isAfterLast())
            {
                String keyVal = cursor.getString(2);
                double priceVal = Double.parseDouble(cursor.getString(1));
                conversions.put(keyVal, priceVal);
                cursor.moveToNext();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
