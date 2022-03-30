package com.example.tasklist;

import android.annotation.SuppressLint;
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

import java.util.Calendar;

public class AddTaskActivity extends Activity implements View.OnClickListener {
    private Button addTodoBtn;
    private Button cancelBtn;
    private EditText subjectEditText;
    private EditText descEditText;
    private RadioGroup progressRadio;
    private RadioGroup urgencyRadio;
    private DBManager dbManager;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Add Task");

        setContentView(R.layout.activity_add_task);

        subjectEditText = (EditText) findViewById(R.id.add_your_task);
        descEditText = (EditText) findViewById(R.id.date_to_perform);
        descEditText.setInputType(InputType.TYPE_NULL);
        descEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCalender(v);
            }
        });

        progressRadio = (RadioGroup) findViewById(R.id.progressRadio);
        urgencyRadio = (RadioGroup) findViewById(R.id.urgencyRadio);
        addTodoBtn = (Button) findViewById(R.id.add_task);
        cancelBtn = (Button) findViewById(R.id.cancel);

        dbManager = new DBManager(this);
        dbManager.open();

        addTodoBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_task:
                setAddTask();
                break;
            case R.id.cancel:
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
        }
    }

    private void setCalender(View v){
        final Calendar calender = Calendar.getInstance();
        int day = calender.get(Calendar.DAY_OF_MONTH);
        int month = calender.get(Calendar.MONTH);
        int year = calender.get(Calendar.YEAR);
        DatePickerDialog picker = new DatePickerDialog(AddTaskActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        descEditText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                }, year, month, day);
        picker.show();
    }

    private void setAddTask(){
        final String name = subjectEditText.getText().toString();
        final String desc = descEditText.getText().toString();
        int status = progressRadio.getCheckedRadioButtonId();
        RadioButton rb = (RadioButton)findViewById(status);
        String checked = rb.getText().toString();
        setDone(checked);

        status = urgencyRadio.getCheckedRadioButtonId();
        rb = (RadioButton)findViewById(status);
        String urgencyTask = rb.getText().toString();
        setUrgency(urgencyTask);
        if(name.isEmpty()){
            Toast.makeText(getApplicationContext(), "Enter task title", Toast.LENGTH_LONG).show();
        }else if(desc.isEmpty()){
            Toast.makeText(getApplicationContext(), "Enter date to preform", Toast.LENGTH_LONG).show();
        }
        else{
            dbManager.insert(name, desc, checked, urgencyTask);
            Intent main = new Intent(AddTaskActivity.this, MainActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(main);
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