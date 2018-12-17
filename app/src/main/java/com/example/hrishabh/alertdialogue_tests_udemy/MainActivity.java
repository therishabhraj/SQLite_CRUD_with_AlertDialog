package com.example.hrishabh.alertdialogue_tests_udemy;


import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity {

    AlertDialog.Builder builder;
    AlertDialog dialog;
    EditText editTextName;
    EditText editTextEmail;
    Button saveButton;
    RecyclerView recyclerView;
    DetailsAdapter adapter;
    DatabaseHelper dbhelper = new DatabaseHelper(this);

    List<Details> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        data = dbhelper.getAllDetailDB();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager( new LinearLayoutManager(this));

        adapter = new DetailsAdapter(data ,this);
        recyclerView.setAdapter(adapter);

//        dbhelper.isEmailNotExists("emailones");
// A Space

    }

    public void showDialog(){
        builder = new AlertDialog.Builder(this);
        final View view  = getLayoutInflater().inflate(R.layout.popup_layout,null);
        editTextName = view.findViewById(R.id.inputsF);
        editTextEmail = view.findViewById(R.id.inputsS);
        saveButton = view.findViewById(R.id.saveBtn);
        builder.setCancelable(true);
        builder.setView(view);
        dialog = builder.create();
        dialog.show();

        editTextName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(25)});
        editTextEmail.setFilters(new InputFilter[]{new InputFilter.LengthFilter(25)});

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogProcessing();
            }
        });
    }

    private void dialogProcessing() {
        String name = editTextName.getText().toString();
        final String Email = editTextEmail.getText().toString();

        if (name.isEmpty() || Email.isEmpty()){
            Toasty.error(this, "Some error with the Input", Toast.LENGTH_SHORT).show();
        }else{
//            Snackbar.make(v, "snackbar Test", Snackbar.LENGTH_SHORT).show();
            boolean isInserted = dbhelper.insertDetailDB(name, Email);
            if (isInserted){
                data.add(new Details(name, Email));
                Toasty.success(this, "Data added Successfully", Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }else {
                Toasty.error(this, "Error inserting details !", Toast.LENGTH_LONG).show();
                editTextEmail.setError("Make sure not used before");
            }
        }


//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Toasty.success(MainActivity.this, "Got your email "+Email+" successfully", Toast.LENGTH_SHORT).show();
//            }
//        }, 3000);

    }
}
