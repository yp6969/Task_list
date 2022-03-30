package com.example.tasklist;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.tasklist.R;

import java.util.Calendar;

public class ModifyTaskActivity extends Activity implements View.OnClickListener {
    private EditText titleText;
    private Button updateBtn, deleteBtn;
    private DatePickerDialog picker;
    private EditText descText;
    private RadioGroup progressRadio;
    private RadioGroup urgencyRadio;
    private long _id;

    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Modify Task");

        setContentView(R.layout.activity_modify_task);

        dbManager = new DBManager(this);
        dbManager.open();

        titleText = (EditText) findViewById(R.id.add_your_task);
        descText = (EditText) findViewById(R.id.date_to_perform);
        descText.setInputType(InputType.TYPE_NULL);
        descText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCalender(v);
            }
        });

        updateBtn = (Button) findViewById(R.id.btn_update);
        deleteBtn = (Button) findViewById(R.id.btn_delete);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String name = intent.getStringExtra("title");
        String desc = intent.getStringExtra("desc");
        String done = intent.getStringExtra("checkbox");
        String urgency = intent.getStringExtra("urgent");

        _id = Long.parseLong(id);

        titleText.setText(name);
        descText.setText(desc);

        progressRadio = (RadioGroup) findViewById(R.id.progressRadio);
        setDone(done);

        urgencyRadio = (RadioGroup) findViewById(R.id.urgencyRadio);
        setUrgency(urgency);

        updateBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_update:
                updateBtn();
                break;

            case R.id.btn_delete:
                dbManager.delete(_id);
                this.returnHome();
                break;
        }
    }

    public void returnHome() {
        Intent home_intent = new Intent(getApplicationContext(), MainActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(home_intent);
    }

    private void setCalender(View v){
        final Calendar calender = Calendar.getInstance();
        int day = calender.get(Calendar.DAY_OF_MONTH);
        int month = calender.get(Calendar.MONTH);
        int year = calender.get(Calendar.YEAR);
        // date picker dialog
        picker = new DatePickerDialog(ModifyTaskActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        descText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                }, year, month, day);
        picker.show();
    }

    private void updateBtn(){
        String title = titleText.getText().toString();
        String desc = descText.getText().toString();
        int status = progressRadio.getCheckedRadioButtonId();
        RadioButton rb = (RadioButton)findViewById(status);
        String checked = rb.getText().toString();

        status = urgencyRadio.getCheckedRadioButtonId();
        rb = (RadioButton)findViewById(status);
        String urgencyTask = rb.getText().toString();

        if(title.isEmpty()){
            Toast.makeText(getApplicationContext(), "Enter task title", Toast.LENGTH_LONG).show();
        }else if(desc.isEmpty()){
            Toast.makeText(getApplicationContext(), "Enter date to preform", Toast.LENGTH_LONG).show();
        }
        else{
            dbManager.update(_id, title, desc, checked, urgencyTask);
            this.returnHome();
        }
    }

    private void setDone(String s){
        RadioButton rbStart = (RadioButton)findViewById(R.id.in_progress);
        RadioButton rbDone = (RadioButton)findViewById(R.id.tasks_done);

        switch (s){
            case "In progress":
                rbStart.setChecked(true);
                rbDone.setChecked(false);
                break;
            case "Done":
                rbStart.setChecked(false);
                rbDone.setChecked(true);
                break;
        }
    }

    private void setUrgency(String s){
        RadioButton normal = (RadioButton)findViewById(R.id.normal);
        RadioButton urgent = (RadioButton)findViewById(R.id.urgent);

        switch (s){
            case "No pressure":
                normal.setChecked(true);
                urgent.setChecked(false);
                break;
            case "Urgent":
                normal.setChecked(false);
                urgent.setChecked(true);
                break;
        }
    }
}
