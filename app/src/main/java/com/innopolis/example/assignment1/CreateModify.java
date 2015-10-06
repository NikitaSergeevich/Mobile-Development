package com.innopolis.example.assignment1;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.CursorAdapter;
import android.widget.ListView;

public class CreateModify extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_modify);

        final Button b = (Button)findViewById(R.id.create_modify_button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProjectDbHelper mDbHelper = new ProjectDbHelper(getApplicationContext());
                // Gets the data repository in write mode
                SQLiteDatabase db = mDbHelper.getWritableDatabase();
                String title = ((EditText) findViewById(R.id.create_modify_title)).getText().toString();
                String description = ((EditText) findViewById(R.id.create_modify_title)).getText().toString();
                String author = ((EditText) findViewById(R.id.create_modify_author)).getText().toString();
                final ContentValues values = new ContentValues();
                values.put(ProjectContract.ProjectEntry.COLUMN_NAME_TITLE, title + " ");
                values.put(ProjectContract.ProjectEntry.COLUMN_NAME_DESCRIPTION, description + " ");
                values.put(ProjectContract.ProjectEntry.COLUMN_NAME_IMAGE, 0 + " ");

                long newRowId;
                newRowId = db.insert(
                        ProjectContract.ProjectEntry.TABLE_NAME,
                        null,
                        values
                );

                final ListView list = (ListView) findViewById(R.id.list);

                String[] projection = {
                        ProjectContract.ProjectEntry._ID,
                        ProjectContract.ProjectEntry.COLUMN_NAME_IMAGE,
                        ProjectContract.ProjectEntry.COLUMN_NAME_TITLE
                };

                Cursor c = db.query(
                        ProjectContract.ProjectEntry.TABLE_NAME,                  // The table to query
                        projection,                               // The columns to return
                        null,                                     // The columns for the WHERE clause
                        null,                                     // The values for the WHERE clause
                        null,                                     // don't group the rows
                        null,                                     // don't filter by row groups
                        null                                 // The sort order
                );

                CursorAdapter sa = (CursorAdapter) list.getAdapter();
                sa.changeCursor(c);
                sa.notifyDataSetChanged();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_modify, menu);
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
