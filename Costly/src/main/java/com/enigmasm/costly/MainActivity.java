package com.enigmasm.costly;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher.ViewFactory;
import android.widget.ViewSwitcher;

import com.firebase.client.Firebase;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity {

    static Map<String, Double> conversions = new HashMap<String, Double>();
    public static SpenDBHelper mDBHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase ref = new Firebase("https://costly.firebaseIO-demo.com/converts");
        setContentView(R.layout.activity_main);
        mDBHelper = new SpenDBHelper(this);
        Animation in = AnimationUtils.loadAnimation(this,
                android.R.anim.fade_in);
        Animation out = AnimationUtils.loadAnimation(this,
                android.R.anim.fade_out);
        final TextSwitcher resultText = (TextSwitcher)this.findViewById(R.id.resultText);
        resultText.setFactory(new ViewFactory() {
            @Override
            public View makeView() {
                TextView t = new TextView(MainActivity.this);
                t.setGravity(Gravity.CENTER);
                t.setText("");
                return t;
            }
        });
        resultText.setInAnimation(in);
        resultText.setOutAnimation(out);
        final EditText inputText = (EditText)this.findViewById(R.id.inputText);
        refreshMap();

        resultText.setText("");
        conversions.put("five dollar footlongs", 5.0);
        conversions.put("brand new video games", 59.99);
        conversions.put("hours of work", 8.5);

        Button convert = (Button)this.findViewById(R.id.costButton);
        convert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int size = conversions.keySet().size();
                if(size > 0)
                {
                    int i = (int)Math.floor(Math.random() * size);
                    String s = (String)conversions.keySet().toArray()[i];
                    String input = inputText.getText().toString().replaceAll("[^0-9.]", "");
                    double value;
                    try {
                        value = Double.parseDouble(input);
                        double trueCost = value / conversions.get(s);
                        resultText.setText(new DecimalFormat("#.##").format(trueCost) + " " + s);
                    } catch (Exception e) {
                        resultText.setText("That number is not in the right format. Too many .s, perhaps?");
                    }
                } else {
                    resultText.setText("You have no relationships defined :(");
                }

            }
        });
        Button manage = (Button) findViewById(R.id.manageButton);

        manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(MainActivity.this, ManageActivity.class);
                startActivityForResult(in, 1);
            }
        });

        Button discover = (Button) findViewById(R.id.discoverButton);

        discover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(MainActivity.this, DiscoverActivity.class);
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
        Cursor cursor = mDBHelper.getReadableDatabase().rawQuery("select * from " + SpenDBHelper.FeedEntry.TABLE_NAME, null);
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
