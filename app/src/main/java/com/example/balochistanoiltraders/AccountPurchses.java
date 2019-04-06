package com.example.balochistanoiltraders;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

public class AccountPurchses extends AppCompatActivity {
    private TextView fullText;
    private static final String TAG = "AccountPurchses";
    private RecyclerView recyclerView;
    private DatabaseReference mDatabase;
    private PurchasesAdapter mAdapter;
    private String partyName, phoneNumber, jobDes, address, key;
    private ArrayList<String> partyNameA, brandName, concession, quantity
            , rate, specialConcession, invoice, totalRate, keyY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_purchses);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bundle data = getIntent().getExtras();
        partyNameA = new ArrayList<>();
        brandName = new ArrayList<>();
        concession = new ArrayList<>();
        quantity = new ArrayList<>();
        rate = new ArrayList<>();
        specialConcession = new ArrayList<>();
        invoice = new ArrayList<>();
        totalRate = new ArrayList<>();
        keyY = new ArrayList<>();
        partyName = data.getString("partyName");
        phoneNumber = data.getString("phoneNumber");
        jobDes = data.getString("jobDescription");
        address = data.getString("address");
        key = data.getString("key");
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(partyName);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("sell");
        mDatabase.keepSynced(true);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snap :dataSnapshot.getChildren()) {
                    Log.d(TAG, "onDataChange: "+ snap.child("party_name").getValue());
                    if(partyName.equals(snap.child("party_name").getValue().toString())){
                        keyY.add(snap.getKey());
                        partyNameA.add(snap.child("party_name").getValue().toString());
                        brandName.add(snap.child("brand_name").getValue().toString());
                        concession.add(snap.child("concession").getValue().toString());
                        rate.add(snap.child("rate").getValue().toString());
                        quantity.add(snap.child("quantity").getValue().toString());
                        specialConcession.add(snap.child("special_concession").getValue().toString());
                        totalRate.add(snap.child("total_rate").getValue().toString());
                        invoice.add(snap.child("invoice_number").getValue().toString());
                        Log.d(TAG, "onDataChange: "+partyNameA+brandName+ concession+ rate+ quantity+ specialConcession);
                        setupRecycler(partyName);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        fullText =(TextView) findViewById(R.id.fullText);
        recyclerView = (RecyclerView) findViewById(R.id.accountPurchases);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager li = new LinearLayoutManager(this);
        li.setReverseLayout(true);
        recyclerView.setLayoutManager(li);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_account_purchses, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings){
            Intent intent = new Intent(AccountPurchses.this, AccountDetail.class);
            intent.putExtra("partyName", partyName);
            intent.putExtra("phoneNumber", phoneNumber);
            intent.putExtra("jobDescription", jobDes);
            intent.putExtra("address", address);
            intent.putExtra("key", key);
            startActivity(intent);
        }


        return super.onOptionsItemSelected(item);
    }

    private void setupRecycler(String partyName) {
        Log.d(TAG, "setupRecycler: Executed");
        mAdapter = new PurchasesAdapter(partyNameA, brandName, concession,
                quantity, rate, specialConcession, invoice, totalRate, keyY, recyclerView
        );
        recyclerView.setAdapter(mAdapter);


    }
    public class PurchasesAdapter extends RecyclerView.Adapter<PurchasesAdapter.PurchasesHolder> {
        private static final String TAG = "PurchasesAdapter";
        private ArrayList<String> partyNameA, brandName, concession, quantity
                , rate, specialConcession, invoice, totalRate, key;
        RecyclerView recyclerView;
        private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("sell");


        public PurchasesAdapter(ArrayList<String> partyNameA, ArrayList<String> brandName,
                                ArrayList<String> concession, ArrayList<String> quantity,
                                ArrayList<String> rate, ArrayList<String> specialConcession,
                                ArrayList<String> invoice, ArrayList<String> totalRate, ArrayList<String> key, RecyclerView recyclerView) {

            this.partyNameA = partyNameA;
            this.brandName = brandName;
            this.concession = concession;
            this.quantity = quantity;
            this.rate = rate;
            this.specialConcession = specialConcession;
            this.invoice = invoice;
            this.totalRate = totalRate;
            this.key = key;
            this.recyclerView = recyclerView;
            Log.d(TAG, "PurchasesAdapter: Initialization" );
            Log.d(TAG, "PurchasesAdapter: "+partyNameA+brandName+ concession+ rate+ quantity+ specialConcession);

        }


        @NonNull
        @Override
        public PurchasesHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_for_selling_item, viewGroup, false);
            Log.d(TAG, "onCreateViewHolder: createViewHolder");
            return new PurchasesHolder(view);
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onBindViewHolder(@NonNull final PurchasesHolder holder, final int i) {

            ArrayList<Integer> colors = new ArrayList<>();
            colors.add(Color.BLUE);
            colors.add(Color.GREEN);
            colors.add(Color.RED);
            colors.add(Color.YELLOW);
            colors.add(Color.MAGENTA);
            colors.add(Color.CYAN);
            colors.add(Color.DKGRAY);
            colors.add(Color.BLACK);
            Random rand = new Random();

            // Obtain a number between [0 - 13].
            int n = rand.nextInt(colors.size());
            Drawable mDraw = holder.itemView.getContext().getDrawable(R.drawable.circle);
            mDraw.setColorFilter(colors.get(n), PorterDuff.Mode.MULTIPLY);
            holder.chractername.setBackground(mDraw);
            char ch = partyNameA.get(i).charAt(0);
            holder.chractername.setText(String.valueOf(i));
            Log.d(TAG, "onBindViewHolder: setting the text views");
            holder.partyName.setText(partyNameA.get(i));
            holder.brandName.setText(brandName.get(i));
            holder.quantity.setText(quantity.get(i));
//            holder.rate.setText(rate.get(i));
            holder.concession.setText(concession.get(i));
//            holder.specialConcession.setText(specialConcession.get(i));


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(holder.itemView.getContext(),EachListItemDetail.class);
                    intent.putExtra("partyName", partyNameA.get(i));
                    intent.putExtra("brandName", brandName.get(i));
                    intent.putExtra("quantity", quantity.get(i));
                    intent.putExtra("rate",rate.get(i));
                    intent.putExtra("concession",concession.get(i) );
                    intent.putExtra("specialConcession",specialConcession.get(i));
                    intent.putExtra("invoiceNumber", invoice.get(i));
                    intent.putExtra("totalRate", totalRate.get(i));
                    intent.putExtra("key",key.get(i));


//                intent.putExtra("stock", model.getSpecial_concession());

                    holder.itemView.getContext().startActivity(intent);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    menuDisplay(holder,i);


                    return true;
                }
            });

        }

        private void dialogShow(final PurchasesHolder holder, final String uid, final int i) {
            LayoutInflater li = LayoutInflater.from(holder.itemView.getContext());
            View view= li.inflate(R.layout.layout_for_confirm, null);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    holder.itemView.getContext());

            // set prompts.xml to alertdialog builder
            alertDialogBuilder.setView(view);

//      final TextView textView = (TextView) view.findViewById(R.id.confirm) ;
            // set dialog message
            alertDialogBuilder.setTitle("Are You Sure to Delete this item?");
            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    mDatabase.child(uid).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(holder.itemView.getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                                                partyNameA.remove(i);
                                                brandName.remove(i);
                                                quantity.remove(i);
                                                rate.remove(i);
                                                concession.remove(i);
                                                specialConcession.remove(i);
                                                totalRate.remove(i);
                                                invoice.remove(i);
                                                key.remove(i);
                                                mAdapter.notifyDataSetChanged();

//                                            Intent intent = holder.itemView.getContext().get;
//                                            finish();
//                                            startActivity(intent);
//                                            holder.itemView.getContext().startActivity(intent);
//                                            recyclerView.no(i);

                                            }
                                        }
                                    });


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

        void menuDisplay(final PurchasesAdapter.PurchasesHolder holder, final int i){

            PopupMenu popupMenu = new PopupMenu(holder.itemView.getContext(), holder.itemView);
            popupMenu.getMenuInflater().inflate(R.menu.menu_for_long_click, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if(item.getItemId() == R.id.editItem){
                        Intent intent = new Intent(holder.itemView.getContext(),EachListItemDetail.class);
                        intent.putExtra("partyName", partyNameA.get(i));
                        intent.putExtra("brandName", brandName.get(i));
                        intent.putExtra("quantity", quantity.get(i));
                        intent.putExtra("rate",rate.get(i));
                        intent.putExtra("concession",concession.get(i) );
                        intent.putExtra("specialConcession",specialConcession.get(i));
                        intent.putExtra("invoiceNumber", invoice.get(i));
                        intent.putExtra("totalRate", totalRate.get(i));
                        intent.putExtra("key",key.get(i));

//                intent.putExtra("stock", model.getSpecial_concession());

                        holder.itemView.getContext().startActivity(intent);
                    } else if(item.getItemId() == R.id.deleteItem){
                        dialogShow(holder, key.get(i), i);
                    }
                    return true;
                }
            });

            popupMenu.show();
        }
        @Override
        public int getItemCount() {
            return partyNameA.size();
        }

        class PurchasesHolder extends RecyclerView.ViewHolder{
            private TextView partyName, brandName, quantity, chractername, concession;

            public PurchasesHolder(@NonNull View itemView) {
                super(itemView);
                partyName = (TextView) itemView.findViewById(R.id.partyNameEachItem);
                brandName = (TextView) itemView.findViewById(R.id.brandnameEachList);
                quantity = (TextView) itemView.findViewById(R.id.QuantityEachList);
                chractername = (TextView) itemView.findViewById(R.id.chracterName);
                concession = (TextView) itemView.findViewById(R.id.concessionEachList);
//                specialConcession = (TextView) itemView.findViewById(R.id.specialConcessionEachList);


            }
        }
    }

}
