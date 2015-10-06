package com.innopolis.example.assignment1;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.R.array;
import com.innopolis.example.assignment1.R;
import android.app.ListActivity;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.content.Intent;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView;
import android.widget.TextView;
import android.view.View.OnClickListener;
import com.innopolis.example.assignment1.ProjectContract.ProjectEntry;
import com.innopolis.example.assignment1.R.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class MainActivity extends Activity {

    private  ArrayList<Project> projects;

    {
        projects = new ArrayList<>(10);
        projects.add(new Project("CrimeFighter", "Description", R.mipmap.ic_launcher));
        projects.add(new Project("MemoryBook", "Description", R.mipmap.ic_launcher));
        projects.add(new Project("MimeDriver", "Description", R.mipmap.ic_launcher));
        projects.add(new Project("HolisticHealth", "Description", R.mipmap.ic_launcher));
        projects.add(new Project("TimeKeeper", "Description", R.mipmap.ic_launcher));
        projects.add(new Project("OK Tour Guide", "Description", R.mipmap.ic_launcher));
        projects.add(new Project("911 Reporter", "Description", R.mipmap.ic_launcher));
        projects.add(new Project("Kitchen King", "Description", R.mipmap.ic_launcher));
        projects.add(new Project("Third Eye", "Description", R.mipmap.ic_launcher));
        projects.add(new Project("Smart City", "Description", R.mipmap.ic_launcher));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_main);

        addDataToDatabase();
        useDatabaseToDisplayProjects();
    }

    public void addDataToDatabase() {
        ProjectDbHelper mDbHelper = new ProjectDbHelper(this);
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        mDbHelper.onClean(db);

        Iterator<Project> it = projects.iterator();
        while (it.hasNext()) {
            Project p = it.next();
            final ContentValues values = new ContentValues();
            values.put(ProjectEntry.COLUMN_NAME_TITLE, p.getName() + " ");
            values.put(ProjectEntry.COLUMN_NAME_IMAGE, p.getImage() + " ");

            values.put(ProjectEntry.COLUMN_NAME_DESCRIPTION, p.getDescription() + " ");

            long newRowId;
            newRowId = db.insert(
                    ProjectEntry.TABLE_NAME,
                    null,
                    values
            );
        }
    }

    public void useDatabaseToDisplayProjects() {
        ProjectDbHelper mDbHelper = new ProjectDbHelper(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                ProjectEntry._ID,
                ProjectEntry.COLUMN_NAME_IMAGE,
                ProjectEntry.COLUMN_NAME_TITLE
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                ProjectEntry.COLUMN_NAME_TITLE + " DESC";

        Cursor c = db.query(
                ProjectEntry.TABLE_NAME,                  // The table to query
                projection,                               // The columns to return
                null,                                     // The columns for the WHERE clause
                null,                                     // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        int count = c.getCount();

        final String[] from = new String[] {
                ProjectEntry.COLUMN_NAME_IMAGE,
                ProjectEntry.COLUMN_NAME_TITLE
        };

        final int[] to = new int[] {
                R.id.project_icon,
                R.id.project_title
        };

        final ListView list = (ListView) findViewById(R.id.list);
        final CursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.single_row, c, from, to, 0);
        list.setAdapter(adapter);

        registerForContextMenu(list);

/*        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View clickView, int position, long id) {
                Intent intent = new Intent(MainActivity.this, Description.class);
                intent.putExtra("modify", false);
                intent.putExtra("index", position);
                startActivityForResult(intent, 1);
            }
        });*/

        list.deferNotifyDataSetChanged();
    }

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
                Intent intent = new Intent(MainActivity.this, CreateModify.class);
                startActivityForResult(intent, 1);
                return true;
            case id.project_delete:
                ProjectDbHelper mDbHelper = new ProjectDbHelper(this);
                SQLiteDatabase db = mDbHelper.getWritableDatabase();
                String q = ProjectEntry._ID + "=" + Long.toString(info.id);
                db.delete(ProjectEntry.TABLE_NAME, q, null);
                final ListView list = (ListView) findViewById(R.id.list);

                String[] projection = {
                        ProjectEntry._ID,
                        ProjectEntry.COLUMN_NAME_IMAGE,
                        ProjectEntry.COLUMN_NAME_TITLE
                };

                Cursor c = db.query(
                        ProjectEntry.TABLE_NAME,                  // The table to query
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

                return true;
            case id.project_edit:
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

/*    private class ProjectsAdapter extends SimpleCursorAdapter {
        private final Context context;
        private LayoutInflater cursorInflater;

        private class ViewHolder {
            public TextView title;
            public TextView rate;
            public ImageView image;

            public ViewHolder(TextView title, TextView rate, ImageView image) {
                this.title = title;
                this.image = image;
                this.rate = rate;

            }
        }

        public void updateUI()
        {
*//*            ProjectDbHelper mDbHelper = new ProjectDbHelper(context);
            SQLiteDatabase db = mDbHelper.getReadableDatabase();*//*
            swapCursor(getCursor());
            notifyDataSetChanged();
        }

        public ProjectsAdapter(Context context, int id, Cursor data, String[] fields, int[] is) {
            super(context, id, data, fields, is);
            cursorInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.context = context;
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            // final LayoutInflater inflater = LayoutInflater.from(ProjectsActivity.this);
            final View view = cursorInflater.inflate(R.layout.single_row, parent, false);
            final TextView title = (TextView) view.findViewById(id.project_title);
            final TextView rate = (TextView) view.findViewById(id.rate);
            final ImageView image = (ImageView) view.findViewById(id.project_icon);
            ViewHolder holder = new ViewHolder(title, rate, image);
            view.setTag(holder);
            return view;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            final ViewHolder holder = (ViewHolder) view.getTag();
            holder.title.setText(cursor.getString(cursor.getColumnIndex(ProjectContract.ProjectEntry.COLUMN_NAME_TITLE)));
            holder.image.setImageResource(cursor.getInt(cursor.getColumnIndex(ProjectContract.ProjectEntry.COLUMN_NAME_IMAGE)));
            holder.rate.setText(cursor.getInt(cursor.getColumnIndex(ProjectEntry.COLUMN_NAME_TITLE)));
        }
    }*/





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

}
