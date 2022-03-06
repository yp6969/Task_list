package com.example.tasklist;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.SimpleCursorAdapter;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tasklist.R;

public class MainActivity extends AppCompatActivity {
    private DBManager dbManager;
    private SimpleCursorAdapter adapter;
    private ListView listView;

    final String[] from = new String[] { DatabaseHelper._ID,
             DatabaseHelper.SUBJECT, DatabaseHelper.DESC , DatabaseHelper.CHECK, DatabaseHelper.URGENT };

    final int[] to = new int[] { R.id.id, R.id.title, R.id.desc, R.id.todoCheckBox, R.id.emergency };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main); // show the task list

        dbManager = new DBManager(this);
        dbManager.open();
        Cursor cursor = dbManager.read();

        listView = (ListView) findViewById(R.id.list_view);
        listView.setEmptyView(findViewById(R.id.empty));

        adapter = new SimpleCursorAdapter(this, R.layout.activity_view_task, cursor, from, to, 0);
        adapter.notifyDataSetChanged();

        listView.setAdapter(adapter);

        // OnCLickListiner For List Items
        listView.setOnItemClickListener((parent, view, position, viewId) -> {
            TextView idTextView = (TextView) view.findViewById(R.id.id);
            TextView titleTextView = (TextView) view.findViewById(R.id.title);
            TextView descTextView = (TextView) view.findViewById(R.id.desc);
            TextView checkTextView = (TextView) view.findViewById(R.id.todoCheckBox);
            TextView emergencyTextView = (TextView) view.findViewById(R.id.emergency);

            String id = idTextView.getText().toString();
            String title = titleTextView.getText().toString();
            String desc = descTextView.getText().toString();
            String checkbox = checkTextView.getText().toString();
            String emergency = emergencyTextView.getText().toString();

            Intent modify_intent = new Intent(getApplicationContext(), ModifyTaskActivity.class);
            modify_intent.putExtra("title", title);
            modify_intent.putExtra("desc", desc);
            modify_intent.putExtra("id", id);
            modify_intent.putExtra("checkbox", checkbox);
            modify_intent.putExtra("urgent", emergency);

            startActivity(modify_intent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
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