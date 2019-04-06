package com.example.balochistanoiltraders;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EmployeeDetails extends AppCompatActivity {
private TextInputEditText name, phoneNumber, jobDescription;
private DatabaseReference mDatabase;
private EditText address;
private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String partyName, phoneNo, jobDes, partyAddress, key;

private  FloatingActionButton fab;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Employee Detail");


        Bundle data = getIntent().getExtras();
        partyName = data.getString("partyName");
        phoneNo = data.getString("phoneNumber");
        jobDes = data.getString("jobDescription");
        partyAddress = data.getString("address");
        key = data.getString("key");

        name = (TextInputEditText) findViewById(R.id.employeeNameForEmployee);
        phoneNumber = (TextInputEditText) findViewById(R.id.employeePhoneNumberForEmployee);
        jobDescription = (TextInputEditText) findViewById(R.id.jobDescriptionForEmployee);
        address = (EditText) findViewById(R.id.addressForEmployee);

       fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                if(mAuth.getCurrentUser().getEmail().equals(
                        EmployeeDetails.this.getResources().getString(R.string.email))) {
                    editable();
                }else {
                    Toast.makeText(EmployeeDetails.this, "Sorry You are not Authenticated", Toast.LENGTH_SHORT).show();

                }
            }
        });
        setTextOnEditText();
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

    private void updateFirebase() {
        String employeeName, phoneNo, salary, addressS;
        employeeName = name.getText().toString();
        phoneNo = phoneNumber.getText().toString();
        salary = jobDescription.getText().toString();
        addressS = address.getText().toString();

        mDatabase = FirebaseDatabase.getInstance().getReference()
                .child("employees").child(key);
        mDatabase.child("employee_name").setValue(employeeName);
        mDatabase.child("phone_number").setValue(phoneNo);
        mDatabase.child("salary").setValue(salary);
        mDatabase.child("address").setValue(addressS);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

    void setTextOnEditText(){
        name.setText(partyName);
        phoneNumber.setText(phoneNo);
        jobDescription.setText(jobDes);
        address.setText(partyAddress);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void editable(){
        if(!name.isEnabled()){
            fab.setImageDrawable(getDrawable(android.R.drawable.ic_delete));
            name.setEnabled(true);
            phoneNumber.setEnabled(true);
            jobDescription.setEnabled(true);
            address.setEnabled(true);
        }else if(name.isEnabled()){
            fab.setImageDrawable(getDrawable(android.R.drawable.ic_menu_edit));
            name.setEnabled(false);
            phoneNumber.setEnabled(false);
            jobDescription.setEnabled(false);
            address.setEnabled(false);
            dialogShow();
        }
    }
}
