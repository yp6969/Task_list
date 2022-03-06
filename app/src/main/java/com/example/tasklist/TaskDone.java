package com.example.tasklist;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tasklist.R;

public class TaskDone extends AppCompatActivity implements View.OnClickListener {

    private Cursor cursor;
    private DBManager dbManager;
    private SimpleCursorAdapter adapter;
    private ListView listView;

    final String[] from = new String[] { DatabaseHelper._ID,
            DatabaseHelper.SUBJECT, DatabaseHelper.DESC , DatabaseHelper.CHECK };

    final int[] to = new int[] { R.id.id, R.id.title, R.id.desc, R.id.todoCheckBox };

    @Override
    public void onClick(View v) {
    }


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_task_done);
        getSupportActionBar().setTitle("Completed Tasks");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        listView = (ListView) findViewById(R.id.listViewDone);
        listView.setEmptyView(findViewById(R.id.empty));

        dbManager = new DBManager(this);
        dbManager.open();
        cursor = dbManager.read();
        cursor = dbManager.searchTaskDone();

        adapter = new SimpleCursorAdapter(this, R.layout.activity_view_task, cursor, from, to, 0);
        adapter.notifyDataSetChanged();

        listView.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                startActivity(new Intent(this, MainActivity.class));
                finish();
                return true;
            case R.id.add_task:
                startActivity(new Intent(this, AddTaskActivity.class));
                return true;
            case R.id.search_by_date:
                startActivity(new Intent(this, SearchTasksByDate.class));
                return true;
            case R.id.task_to_perform:
                startActivity(new Intent(getApplicationContext(), TaskNotDone.class));
                return true;
            case R.id.tasks_done:
                startActivity(new Intent(getApplicationContext(), TaskDone.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
