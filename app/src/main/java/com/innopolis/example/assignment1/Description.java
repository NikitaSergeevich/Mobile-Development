package com.innopolis.example.assignment1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import com.innopolis.example.assignment1.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Description extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        final Intent intent = getIntent();
        boolean create = intent.getBooleanExtra("modify", false);

        if (create)
        {

            return;
        }

        int index = intent.getIntExtra("index", 0);
        String[] descriptions = getResources().getStringArray(R.array.descriptions);
        final RatingBar prj_rating = (RatingBar) findViewById(R.id.rating_bar);
        final TextView prj_description = (TextView) findViewById(R.id.activity_description);
        if (descriptions.length >= index){
            prj_description.setText(descriptions[index]);
        }

        prj_rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener(){
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser){
                if (fromUser == true)
                {
                    float scores = prj_rating.getRating();
                    intent.putExtra("rating", scores);

                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_description, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
