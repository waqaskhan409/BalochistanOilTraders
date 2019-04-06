package com.example.balochistanoiltraders;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static org.bitbucket.dollar.Dollar.$;

public class EachListItemDetail extends AppCompatActivity {
    private static final String TAG = "FormForListActivity";
    private Button mSubmitBtn, mDeclineBtn;
    private FloatingActionButton fab;
//    ArrayList<String> arrayList = new ArrayList<>();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference mDatabase, mDatabasepartyName, mStock;
    private TextView mInvoiceNumberText, mStockValueText, mTotalRate;
    private AutoCompleteTextView mPurchaserName, mBrandName;
    String validCharacters = $('0', '9').join() + $('A', 'Z').join();
    private TextInputEditText mQuantity, mRate, mConcession, mSpecialConcession;
    ArrayList<String> arrayList = new ArrayList<>();
    String partyName, brandName, quantity,rate,
            concession, specialConcession, invoiceNumber,
            totalRate, stock, key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_each_list_item_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        final Bundle data = getIntent().getExtras();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("stock_available");
        partyName = data.getString("partyName");
        brandName = data.getString("brandName");
        quantity= data.getString("quantity");
        rate = data.getString("rate");
        concession = data.getString("concession");
        specialConcession = data.getString("specialConcession");
        invoiceNumber = data.getString("invoiceNumber");
        totalRate = data.getString("totalRate");
        key = data.getString("key");

        mDatabasepartyName = FirebaseDatabase.getInstance().getReference().child("accounts_for_party");

        mStock = FirebaseDatabase.getInstance().getReference().child("stock_available");

        toolbar.setTitle("Sale Detail");
        getSupportActionBar().setTitle("Sale Detail");
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                if(mAuth.getCurrentUser().getEmail().equals(
                        EachListItemDetail.this.getResources().getString(R.string.email))) {
                    Editable();
                }else {
                    Toast.makeText(EachListItemDetail.this, "Sorry You are not Authenticated", Toast.LENGTH_SHORT).show();

                }
            }
        });
        mInvoiceNumberText = (TextView) findViewById(R.id.invoiceNumberForList);
        mStockValueText = (TextView) findViewById(R.id.stockInList);
        mTotalRate = (TextView) findViewById(R.id.overAllRateForlist);

        mPurchaserName = (AutoCompleteTextView) findViewById(R.id.partyNameForList);
        mBrandName = (AutoCompleteTextView) findViewById(R.id.brandNameForList);
        mQuantity = (TextInputEditText) findViewById(R.id.listForQuantity);
        mRate = (TextInputEditText) findViewById(R.id.listForRate);
        mConcession = (TextInputEditText) findViewById(R.id.listForConsession);
        mSpecialConcession = (TextInputEditText) findViewById(R.id.listForSpecialConcession);
//        String randomName = randomString(12);
//        mInvoiceNumberText.setText(randomName);

        mRate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String rate = mRate.getText().toString();
                String quantity = mQuantity.getText().toString();
                try {
                    int rateInt = Integer.parseInt(rate);
                    int quantityInt = Integer.parseInt(quantity);
                    if(TextUtils.isEmpty(rate) || TextUtils.isEmpty(quantity)){
                        mTotalRate.setText("0 Rupees");
                    }else {
                        mTotalRate.setText(String.valueOf(quantityInt * rateInt) + " Rupees");
                    }

                }catch (Exception e){
                    Log.d(TAG, "onTextChanged: " + e);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                String rate = mRate.getText().toString();
                String quantity = mQuantity.getText().toString();
                if(TextUtils.isEmpty(rate) || TextUtils.isEmpty(quantity)){
                    mTotalRate.setText("0 Rupees");

                }else {

                }




            }
        });
        mDatabasepartyName.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Log.d(TAG, "onDataChange: " + arrayList);
                    arrayList.add(snapshot.child("party_name").getValue().toString());
                }
                ArrayAdapter arrayAdapter0 = new ArrayAdapter(EachListItemDetail.this, android.R.layout.simple_list_item_1, arrayList);
                mPurchaserName.setAdapter(arrayAdapter0);

                ArrayAdapter arrayAdapter1 = new ArrayAdapter(EachListItemDetail.this, android.R.layout.simple_list_item_1, arrayList);
                mBrandName.setAdapter(arrayAdapter1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mStock.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                stock = dataSnapshot.getValue().toString();
                mStockValueText.setText(stock);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        setTextOnEditText();

    }
    void setTextOnEditText(){
        mPurchaserName.setText(partyName);
        mBrandName.setText(brandName);
        mQuantity.setText(quantity);
        mRate.setText(rate);
        mConcession.setText(concession);
        mSpecialConcession.setText(specialConcession);
        mTotalRate.setText(totalRate);
        mInvoiceNumberText.setText(invoiceNumber);
        mStockValueText.setText(stock);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    void Editable(){
        if(!mBrandName.isEnabled()){
            fab.setImageDrawable(getDrawable(android.R.drawable.ic_delete));
            mPurchaserName.setEnabled(true);
            mBrandName.setEnabled(true);
            mQuantity.setEnabled(true);
            mRate.setEnabled(true);
            mConcession.setEnabled(true);
            mSpecialConcession.setEnabled(true);
        }
        else if(mBrandName.isEnabled()){
            fab.setImageDrawable(getDrawable(android.R.drawable.ic_menu_edit));
                mPurchaserName.setEnabled(false);
                mBrandName.setEnabled(false);
                mQuantity.setEnabled(false);
                mRate.setEnabled(false);
                mConcession.setEnabled(false);
                mSpecialConcession.setEnabled(false);
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
                               validation();
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

        Log.d(TAG, "updateFirebase: Firebase Uploading");
        final String pName, bName, quantity, rate, concession, sConcession, stock;
        pName = mPurchaserName.getText().toString();
        bName = mBrandName.getText().toString();
        quantity = mQuantity.getText().toString();
        rate = mRate.getText().toString();
        concession = mConcession.getText().toString();
        sConcession = mSpecialConcession.getText().toString();

        mDatabase = FirebaseDatabase.getInstance().getReference()
                .child("sell").child(key);
        mDatabase.child("party_name").setValue(pName);
        mDatabase.child("brand_name").setValue(bName);
        mDatabase.child("quantity").setValue(quantity);
        mDatabase.child("rate").setValue(rate);
        mDatabase.child("concession").setValue(concession);
        mDatabase.child("special_concession").setValue(sConcession);
        mDatabase.child("total_rate").setValue(String.valueOf((Integer.parseInt(rate)
                *Integer.parseInt(quantity))));
        mDatabase.child("invoice_number").setValue(invoiceNumber);
    }
    void validation() {
        View item = null;
        boolean bool = true;
        final String pName, bName, quantity, rate, concession, sConcession, stock;
        pName = mPurchaserName.getText().toString();
        bName = mBrandName.getText().toString();
        quantity = mQuantity.getText().toString();
        rate = mRate.getText().toString();
        concession = mConcession.getText().toString();
        sConcession = mSpecialConcession.getText().toString();
        stock = mStockValueText.getText().toString();
        int totalRate = 0;
        try {
            totalRate = Integer.parseInt(quantity) * Integer.parseInt(rate);
        }catch (Exception e){

        }
        if (TextUtils.isEmpty(pName)) {
            mPurchaserName.setError("Please fill this field");
            item = mPurchaserName;
            bool = false;
        } else if (TextUtils.isEmpty(bName)) {
            mBrandName.setError("Please fill this field");
            item = mBrandName;
            bool = false;
        } else if (TextUtils.isEmpty(quantity)) {
            mQuantity.setError("Please fill this field");
            item = mQuantity;
            bool = false;
        } else if (TextUtils.isEmpty(rate)) {
            mRate.setError("Please fill this field");
            item = mRate;
            bool = false;
        } else if (TextUtils.isEmpty(concession)) {
            mConcession.setError("Please fill this field");
            item = mConcession;
            bool = false;
        } else if (TextUtils.isEmpty(sConcession)) {
            mSpecialConcession.setError("Please fill this field");
            item = mSpecialConcession;
            bool = false;
        } else if (Integer.parseInt(quantity) > Integer.parseInt(stock)) {
            mQuantity.setError("Not enough Stock");
            item = mQuantity;
            bool = false;
        }else if(!arrayList.contains(pName)){
            mPurchaserName.setError("This item is not contain in the list");
            item = mQuantity;
            bool = false;

        }else if(!arrayList.contains(bName)){
            mBrandName.setError("This item is not contain in the list");
            item = mQuantity;
            bool = false;
        }
        if(bool){
            Log.d(TAG, "validation: "+ totalRate);
            mDatabase = mDatabase.child("sell").push();
            final int finalTotalRate = totalRate;
            mDatabase.child("party_name").setValue(pName).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        mDatabase = FirebaseDatabase.getInstance().getReference()
                                .child("sell").child(key);
                        mDatabase.child("party_name").setValue(pName);
                        mDatabase.child("brand_name").setValue(bName);
                        mDatabase.child("quantity").setValue(quantity);
                        mDatabase.child("rate").setValue(rate);
                        mDatabase.child("concession").setValue(concession);
                        mDatabase.child("special_concession").setValue(sConcession);
                        mDatabase.child("total_rate").setValue(
                                String.valueOf((Integer.parseInt(rate)
                                *Integer.parseInt(quantity) -
                        (Integer.parseInt(concession) + Integer.parseInt(sConcession)))));
                    }
                }
            });
        }else {
            item.requestFocus();
        }
    }
}
