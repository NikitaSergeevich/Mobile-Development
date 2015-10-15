package com.innopolis.example.assignment1;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import android.app.LoaderManager;
import android.content.Loader;

import java.util.ArrayList;

import com.innopolis.example.assignment1.Const.Code;
import com.innopolis.example.assignment1.Const.Key;
import com.innopolis.example.assignment1.ProjectContract.ProjectEntry;
import com.innopolis.example.assignment1.R.*;
import com.innopolis.example.assignment1.Utils.ServerHelper;

import android.widget.Toast;


public class ProjectsListActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor>{

    private  ArrayList<Project> projects;
    private SimpleCursorAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_projects_list);

        synchronization();
        //setupListView();
    }

    private void synchronization()
    {
        try {

            new AsyncTask<String, Void, Intent>() {
                @Override
                protected Intent doInBackground(String... params) {
                    Bundle data = new Bundle();
                    boolean result = false;
                    try {
                        projects = ServerHelper.getProjects();
                        data.putBoolean("result", true);
                    } catch (Exception e) {
                        data.putString("error_msg", e.getMessage());
                        data.putBoolean("result", false);
                    }

                    final Intent res = new Intent();
                    res.putExtras(data);
                    return res;
                }

                @Override
                protected void onPostExecute(Intent intent) {
                    if (intent.hasExtra("error_msg")) {
                        Toast.makeText(getBaseContext(), intent.getStringExtra("sync error"), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        addDataToDatabase();
                        setupListView();
                        getLoaderManager().initLoader(0, null, ProjectsListActivity.this);
                    }
                }
            }.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addDataToDatabase() {
        ProjectDbHelper mDbHelper = new ProjectDbHelper(getBaseContext());
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        mDbHelper.onClean(db);

        for (Project p : projects) {
            final ContentValues values = new ContentValues();
            values.put(ProjectEntry.COLUMN_NAME_TITLE, p.getName());
            values.put(ProjectEntry.COLUMN_NAME_AUTHOR, p.getAuthor());
            values.put(ProjectEntry.COLUMN_NAME_LINK, p.getLink());
            values.put(ProjectEntry.COLUMN_NAME_DESCRIPTION, p.getDescription());

            //values.put(ProjectEntry.COLUMN_NAME_RATING, p.getRate());

            getContentResolver().insert(
                    ProjectEntry.CONTENT_URI,
                    values
            );
        }
    }

    private void setupListView() {
        final String[] from = new String[] {
                ProjectEntry.COLUMN_NAME_TITLE,
        };

        final int[] to = new int[] {
                R.id.project_title
        };

        final ListView list = (ListView) findViewById(R.id.list);
        mAdapter = new SimpleCursorAdapter(this, layout.single_row, null, from, to, 0);
        list.setAdapter(mAdapter);
    }

    /*Loader */

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                ProjectEntry._ID,
                ProjectEntry.COLUMN_NAME_TITLE
        };

        String sortOrder =
                ProjectEntry.COLUMN_NAME_TITLE + " DESC";

        return new CursorLoader(this, ProjectEntry.CONTENT_URI, projection, null, null, sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        cursor.setNotificationUri(getContentResolver(), ProjectEntry.CONTENT_URI);
        cursor.registerContentObserver(new ContentObserver(new Handler()) {
            @Override
            public void onChange(boolean selfChange) {
                getLoaderManager().restartLoader(0, null, ProjectsListActivity.this);
            }
        });

        mAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    /*Context menu*/

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId()== id.list){
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_main, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int listPosition = info.position;
        long _id = info.id;
        switch (item.getItemId()){
            case id.project_add:
                Intent intent = new Intent(ProjectsListActivity.this, CreateModify.class);
                startActivityForResult(intent, 1);
                return true;
            case id.project_delete:
                ProjectContract.removeProject(ProjectsListActivity.this, _id);
            case id.project_edit:
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        float rate = data.getFloatExtra("rating", 0);
        int index = data.getIntExtra("index", 0);

        Project p = projects.get(index);
        p.setRate(rate);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

/*    @Override
    protected void onSaveInstanceState(Bundle SavedInstanceState) {

        final ListView = ListView FindViewById(R.id.edit_text);
        super.onSaveInstanceState(SavedInstanceState);
        SavedInstanceState.putInt("salavat-count", a);
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void logoff(View view) {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(Key.USERNAME);

        final Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent); //todo: seem like not proper way. One visible issue - back button would work;
    }

}
