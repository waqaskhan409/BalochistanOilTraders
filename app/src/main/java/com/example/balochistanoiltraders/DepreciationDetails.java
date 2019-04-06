package com.example.balochistanoiltraders;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DepreciationDetails extends AppCompatActivity {
    private static final String TAG = "DepreciationDetails";
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("depreciation");
    FloatingActionButton fab;
    String nam, loa, des, key;
    TextInputLayout name, loan, description;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_depreciation_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bundle data = getIntent().getExtras();
        nam = data.getString("name");
        loa = data.getString("loan");
        des = data.getString("description");
        key = data.getString("key");

        name = (TextInputLayout) findViewById(R.id.name);
        loan = (TextInputLayout) findViewById(R.id.loan);
        description = (TextInputLayout) findViewById(R.id.description);




        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                if(mAuth.getCurrentUser().getEmail().equals(
                        DepreciationDetails.this.getResources().getString(R.string.email))) {
                    editable();
                }else {
                    Toast.makeText(DepreciationDetails.this, "Sorry You are not Authenticated", Toast.LENGTH_SHORT).show();

                }
            }
        });
        setText();
    }
    void setText(){
        name.getEditText().setText(nam);
        loan.getEditText().setText(loa);
        description.getEditText().setText(des);
    }
    private void updateFirebase() {
        String name, loan, des;
        name = this.name.getEditText().getText().toString();
        loan = this.loan.getEditText().getText().toString();
        des = description.getEditText().getText().toString();

        mDatabase = FirebaseDatabase.getInstance().getReference()
                .child("depreciation").child(key);
        mDatabase.child("name").setValue(name);
        mDatabase.child("loan").setValue(loan);
        mDatabase.child("description").setValue(des);
        Date date = new Date();
        SimpleDateFormat format=new SimpleDateFormat("dd/MM/yyyy");
        
        Log.d(TAG, "Dialogue: "+format.format(date));
        mDatabase.child("date").setValue(format.format(date));
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void editable(){
        if(!name.getEditText().isEnabled()){
            fab.setImageDrawable(getDrawable(android.R.drawable.ic_delete));
            name.getEditText().setEnabled(true);
            loan.getEditText().setEnabled(true);
            description.getEditText().setEnabled(true);
        }else if(name.isEnabled()){
            fab.setImageDrawable(getDrawable(android.R.drawable.ic_menu_edit));
            name.getEditText().setEnabled(false);
            loan.getEditText().setEnabled(false);
            description.getEditText().setEnabled(false);
            dialogShow();
        }
    }
    private void dialogShow() {
        LayoutInflater li = LayoutInflater.from(this);
        View view= li.inflate(R.layout.layout_for_confirm, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(view);

//      final TextView textView = (TextView) view.findViewById(R.id.confirm) ;
        // set dialog message
        alertDialogBuilder.setTitle("Are You Sure to Act this?");
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                updateFirebase();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

}
