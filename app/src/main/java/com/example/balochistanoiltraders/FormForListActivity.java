package com.example.balochistanoiltraders;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.support.constraint.Constraints.TAG;
import static org.bitbucket.dollar.Dollar.$;

public class FormForListActivity extends AppCompatActivity {
    private static final String TAG = "FormForListActivity";
    private Button mSubmitBtn, mDeclineBtn;
    private TextView mInvoiceNumberText, mStockValueText, mTotalRate;
    private AutoCompleteTextView mPurchaserName, mBrandName;
    String validCharacters = $('0', '9').join() + $('A', 'Z').join();
    private TextInputEditText mQuantity, mRate, mConcession, mSpecialConcession;
    ArrayList<String> arrayList = new ArrayList<>();
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase, mDatabasepartyName, mStock;
    final String randomName = randomString(12);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_for_list);

        FirebaseApp.initializeApp(this);

        mSubmitBtn = (Button) findViewById(R.id.submitForList);
        mDeclineBtn = (Button) findViewById(R.id.declineForList);
        mInvoiceNumberText = (TextView) findViewById(R.id.invoiceNumberForList);
        mStockValueText = (TextView) findViewById(R.id.stockInList);
        mTotalRate = (TextView) findViewById(R.id.overAllRateForlist);

        mPurchaserName = (AutoCompleteTextView) findViewById(R.id.partyNameForList);
        mBrandName = (AutoCompleteTextView) findViewById(R.id.brandNameForList);
        mQuantity = (TextInputEditText) findViewById(R.id.listForQuantity);
        mRate = (TextInputEditText) findViewById(R.id.listForRate);
        mConcession = (TextInputEditText) findViewById(R.id.listForConsession);
        mSpecialConcession = (TextInputEditText) findViewById(R.id.listForSpecialConcession);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabasepartyName = FirebaseDatabase.getInstance().getReference().child("accounts_for_party");
        mStock = FirebaseDatabase.getInstance().getReference().child("stock_available");
        mDatabasepartyName.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    arrayList.add(snapshot.child("party_name").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mStock.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mStockValueText.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        mInvoiceNumberText.setText(randomName);


        mQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String quantity, stock;
                quantity = mQuantity.getText().toString();
                stock = mStockValueText.getText().toString();
//

                if(!(Integer.parseInt(quantity) <= Integer.parseInt(stock))){
                    Toast.makeText(FormForListActivity.this, "Not enough mall", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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




        ArrayAdapter arrayAdapter0 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList);
        mPurchaserName.setAdapter(arrayAdapter0);

        ArrayAdapter arrayAdapter1 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList);
        mBrandName.setAdapter(arrayAdapter1);

        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Validation for list farm");
                validation();


            }
        });


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
            long timeStamp = 1 * new Date().getTime();
            String NewDescTimeStamp= String.valueOf((9999999999999L+(-1 * timeStamp)));
            mDatabase = mDatabase.child("sell").child(NewDescTimeStamp);
            final int finalTotalRate = totalRate;
            mDatabase.child("party_name").setValue(pName).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        mDatabase.child("brand_name").setValue(bName);
                        mDatabase.child("quantity").setValue(quantity);
                        mDatabase.child("rate").setValue(rate);
                        mDatabase.child("concession").setValue(concession);
                        mDatabase.child("special_concession").setValue(sConcession);
                        mDatabase.child("total_rate").setValue(String.valueOf((finalTotalRate -
                                (Integer.parseInt(concession) +
                                Integer.parseInt(sConcession)))));
                        mDatabase.child("invoice_number").setValue(String.valueOf(randomName));

                        Date date = new Date();
                        SimpleDateFormat format=new SimpleDateFormat("dd/MM/yyyy");

                        Log.d(TAG, "Dialogue: "+format.format(date));
                        mDatabase.child("date").setValue(format.format(date));

                        String available= String.valueOf((Integer.parseInt(stock)-Integer.parseInt(quantity)));
                        mStock.setValue(available);
                        Intent intent = new Intent(FormForListActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }
            });
        }else {
            item.requestFocus();
        }
    }

    String randomString(int length) {
        return $(validCharacters).shuffle().slice(length).toString();
    }
    public void buildFiveRandomStrings() {
        for (int i : $(1)) {
            System.out.println(randomString(12));
        }
    }
}
