package com.example.balochistanoiltraders;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import static android.support.constraint.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class CashBorrow extends Fragment implements DatePickerDialog.OnDateSetListener {


    RecyclerView recyclerView;
    DatabaseReference mDatabase;

    TextView textView;
    ExpancesAdapter mAdapter;
    String value;
    private ArrayList<String> arrayList = new ArrayList<>();
    private ViewPager viewPager;
    String key;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    int total = 0;
    @SuppressLint("ValidFragment")
    public CashBorrow(String key) {
        // Required empty public constructor
        this.key = key;
        Log.d(TAG, "CashBorrow: " + key);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_expances, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Cash Borrow");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("cash_borrow");
        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("accounts_for_party");
        mDatabase.keepSynced(true);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerForExpances);
        LinearLayoutManager li = new LinearLayoutManager(getActivity());
//        li.setReverseLayout(true);
        recyclerView.setLayoutManager(li);

        recyclerView.setHasFixedSize(true);
        textView = (TextView) view.findViewById(R.id.monthlyExpances);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                dialogShowForMonthlySxpence();
            }
        });
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAuth.getCurrentUser().getEmail().equals(
                        getActivity().getResources().getString(R.string.email))) {
                    dialogShowForStock();
                }else {
                    Toast.makeText(getActivity(), "Sorry You are not Authenticated", Toast.LENGTH_SHORT).show();

                }
            }
        });
        textView.setText("0");
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    arrayList.add(snapshot.child("party_name").getValue().toString());
                }
                Log.d(TAG, "onDataChange: "+ arrayList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                textView.setText(dataSnapshot.child(key).child("money").child("money_borrowed").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        setupRecycler(true);
        return view;
    }

    private void setupRecycler(boolean bool) {
        if(bool) {

            FirebaseRecyclerOptions<Note> options = new FirebaseRecyclerOptions.Builder<Note>()
                    .setQuery(mDatabase.child(key).child("cash_borrow"), Note.class).build();
            mAdapter = new ExpancesAdapter(options);
            mAdapter.startListening();
            recyclerView.setAdapter(mAdapter);
        }else {
            FirebaseRecyclerOptions<Note> options = new FirebaseRecyclerOptions.Builder<Note>()
                    .setQuery(mDatabase, Note.class).build();
            mAdapter = new ExpancesAdapter(options);
            recyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(!arrayList.isEmpty()) {
            mAdapter.stopListening();
        }
        total = 0;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(!arrayList.isEmpty()){
        mAdapter.startListening();
        }
    }

    private void dialogShowForMonthlySxpence() {
        LayoutInflater li = LayoutInflater.from(getActivity());
        View view= li.inflate(R.layout.prompts, null);


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(view);

        final TextInputEditText nameE = (TextInputEditText) view
                .findViewById(R.id.addToStock);

        // set dialog message
        alertDialogBuilder.setTitle("Monthly Expances Add Form");
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                Date date = new Date();
                                date.getDate();
                                String name, moneySpend, description, addressS;
                                name = nameE.getText().toString();
//                                mDatabase.child("monthly_expances").setValue(name);


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
    public String dat;
    TextView date;

    private void dialogShowForStock() {
        LayoutInflater li = LayoutInflater.from(getActivity());
        View view= li.inflate(R.layout.layout_for_expances, null);


        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(view);
        final View[] item = {null};
        final boolean[] cancel = {true};

        final AutoCompleteTextView nameE = (AutoCompleteTextView) view
                .findViewById(R.id.autoComplete);
        final TextInputEditText moneySpendE = (TextInputEditText) view
                .findViewById(R.id.moneySpend);
        date = (TextView)
                view.findViewById(R.id.date);
        final ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,arrayList);
        nameE.setAdapter(arrayAdapter);
        final Date date1 = new Date();
        final SimpleDateFormat format=new SimpleDateFormat("dd/MM/yyyy");
        date.setText(format.format(date1));
        final EditText descriptionE = (EditText) view
                .findViewById(R.id.description);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialogue();
            }
        });

        // set dialog message
        alertDialogBuilder.setTitle("Party Add Form");
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                String name, moneySpend, description, addressS;
                                name = nameE.getText().toString();
                                moneySpend = moneySpendE.getText().toString();
                                description = descriptionE.getText().toString();
                                if(TextUtils.isEmpty(name) || TextUtils.isEmpty(moneySpend) || TextUtils.isEmpty(description)){
                                    Toast.makeText(getContext(), "Sorry, Data didn't post due to Blank fields", Toast.LENGTH_SHORT).show();
                                }else if(!(arrayList.contains(name))){
                                    Toast.makeText(getContext(), "Sorry, Data didn't post due to not valid account name", Toast.LENGTH_SHORT).show();

                                }
                                else {

                                    long timeStamp = 1 * new Date().getTime();
                                    String NewDescTimeStamp= String.valueOf((9999999999999L+(-1 * timeStamp)));
                                  /*  FirebaseDatabase.getInstance().getReference()
                                            .child("monthly_expances").setValue(
                                            String.valueOf(
                                                    (Integer.parseInt(value)-Integer.parseInt(moneySpend))
                                            ));*/
                                  int total = Integer.parseInt(textView.getText().toString()) + Integer.parseInt(moneySpend);
                                    mDatabase = mDatabase.child(key).child("cash_borrow").child(NewDescTimeStamp);
                                    mDatabase.child("name").setValue(name);
                                    mDatabase.child("spend_money").setValue(moneySpend);
                                    mDatabase.child("description").setValue(description);
                                    if(dat == null) {
                                        dat = format.format(date1);
                                    }

                                    Log.d(TAG, "Dialogue: "+format.format(date1));
                                    mDatabase.child("date").setValue(dat);

                                    mDatabase = FirebaseDatabase.getInstance().getReference().child("cash_borrow");
                                    final ArrayList<String> arrayList = new ArrayList<>();
                                    final ArrayList<String> arrayListAddition = new ArrayList<>();



                                    DatabaseReference databaseReference = FirebaseDatabase
                                            .getInstance()
                                            .getReference().child("cash_borrow");
                                    databaseReference.child(key).child("cash_borrow").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            for(DataSnapshot snapshot: dataSnapshot.getChildren()){

                                                arrayList.add(snapshot.getKey());
                                            }
                                            Log.d(TAG, "onDataChange: " + arrayList);
                                            for(int i = 0 ; i < dataSnapshot.getChildrenCount() ; i++ ){
                                                String value = dataSnapshot
                                                        .child(arrayList.get(i))
                                                        .child("spend_money").getValue().toString();
                                                arrayListAddition.add(value);
                                            }
                                            Log.d(TAG, "onDataChange: " + arrayListAddition);
                                            FirebaseDatabase.getInstance()
                                                    .getReference().child("cash_borrow").child(key).child("money")
                                                    .child("money_borrowed").setValue(setupOverViewValue(arrayListAddition));

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });



                                    Toast.makeText(getContext(), "Data posted", Toast.LENGTH_SHORT).show();
                                }

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
    String setupOverViewValue(ArrayList<String> arrayListAddition){
        int t = 0;
        for (int i = 0; i < arrayListAddition.size(); i++) {
            t = t + Integer.parseInt(arrayListAddition.get(i));
        }
        return String.valueOf(t);
    }

    private void showDateDialogue() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getActivity(),this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        dat =  String.valueOf(dayOfMonth) + "/" +
                String.valueOf(month+1) + "/" +
                String.valueOf(year) ;
        date.setText(dat);
    }


    public class ExpancesAdapter extends FirebaseRecyclerAdapter<Note, com.example.balochistanoiltraders.ExpancesAdapter.ExpancesHolder> {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("cash_borrow");
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        ArrayList<Integer> colors = new ArrayList<>();
        Date date = new Date();
        SimpleDateFormat format=new SimpleDateFormat("dd/MM/yyyy");
        private static final String TAG = "ExpancesAdapter";



        String mDate = "";

        /**
         * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
         * {@link FirebaseRecyclerOptions} for configuration options.
         *
         * @param options
         */
        public ExpancesAdapter(@NonNull FirebaseRecyclerOptions<Note> options) {
            super(options);

        }

        @SuppressLint("ResourceType")
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected void onBindViewHolder(@NonNull final com.example.balochistanoiltraders.ExpancesAdapter.ExpancesHolder holder, final int position, @NonNull final Note model) {

//    mDate = model.getDate();
//            total = total + Integer.parseInt(model.getSpend_money());
//            textView.setText(String.valueOf(total));
            colors.add(Color.BLUE);
            colors.add(Color.GREEN);
            colors.add(Color.RED);
            colors.add(Color.YELLOW);
            colors.add(Color.MAGENTA);
            colors.add(Color.CYAN);
            colors.add(Color.DKGRAY);
            colors.add(Color.BLACK);


            String[] color = holder.itemView.getContext().getResources().getStringArray(R.array.colors);

            Random rand = new Random();
            // Obtain a number between [0 - 13].
            int n = rand.nextInt(colors.size());
            String ch = "T";
            try {
               ch  = String.valueOf(model.getName().charAt(0));
            } catch (Exception e){
//                holder.itemView.setVisibility(View.GONE);

            }
            final String uid = this.getRef(position).getKey();
            holder.name.setText(model.getName());
            holder.money.setText(model.getSpend_money());

            int count = 0;
            count = this.getItemCount();
            holder.text.setText(String.valueOf(ch));
            Drawable mDraw = holder.itemView.getContext().getDrawable(R.drawable.circle);
            mDraw.setColorFilter(colors.get(n), PorterDuff.Mode.MULTIPLY);
            holder.text.setBackground(mDraw);
            holder.description.setText(model.getDescription());
            final int finalCount = count;
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(holder.itemView.getContext(), ExpanceDetails.class);
                    intent.putExtra("name", model.getName());
                    intent.putExtra("moneySpend", model.getSpend_money());
                    intent.putExtra("description", model.getDescription());
                    intent.putExtra("date", model.getDate());
                    intent.putExtra("key", uid);
                    intent.putExtra("parentKey", key);

                    holder.itemView.getContext().startActivity(intent);

                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    menuDisplay(holder, model, uid);
                    return true;
                }
            });

            if(format.format(date).equals(model.getDate())) {
                holder.Date.setVisibility(View.VISIBLE);
                holder.Date.setTextColor(Color.DKGRAY);
                holder.Date.setText("Today");
            }else {
                holder.Date.setVisibility(View.VISIBLE);
                holder.Date.setText(model.getDate());
            }
            if(position+1 == this.getItemCount()){
//                holder.text.setText("Total");

            }

        }

       /* private void showSpecs(final com.example.balochistanoiltraders.ExpancesAdapter.ExpancesHolder holder, Note model) {
            LayoutInflater li = LayoutInflater.from(holder.itemView.getContext());
            View view= li.inflate(R.layout.layout_for_dailogue_total, null);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    holder.itemView.getContext());

            // set prompts.xml to alertdialog builder
            alertDialogBuilder.setView(view);

      final TextInputLayout cashBorroed = (TextInputLayout) view.findViewById(R.id.cashBorrowed) ;
            final TextInputLayout netCash = (TextInputLayout) view.findViewById(R.id.netCash) ;
            final TextInputLayout deposite = (TextInputLayout) view.findViewById(R.id.depositeToBank) ;
            final TextInputLayout liability = (TextInputLayout) view.findViewById(R.id.liability) ;
            alertDialogBuilder.setTitle("Overall overview");

            cashBorroed.getEditText().setText(model.getMoney_borrowed());
            netCash.getEditText().setText(model.getCash());
            deposite.getEditText().setText(model.getDeposite_money());
            liability.getEditText().setText(model.getLiability());
            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    if(mAuth.getCurrentUser().getEmail().equals(holder.itemView.
                                            getContext().getResources().getString(R.string.email))) {
                                        mDatabase.child(uid).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {

                                                    Toast.makeText(holder.itemView.getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }else {
                                        Toast.makeText(holder.itemView.getContext(), "Sorry You are not Authenticated", Toast.LENGTH_SHORT).show();

                                    }


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
        }*/

        @NonNull
        @Override
        public com.example.balochistanoiltraders.ExpancesAdapter.ExpancesHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_for_expances_list, viewGroup, true);
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_for_expances_list, viewGroup, false);
            return new com.example.balochistanoiltraders.ExpancesAdapter.ExpancesHolder(view);
        }
        private void dialogShow(final com.example.balochistanoiltraders.ExpancesAdapter.ExpancesHolder holder, final Note model, final String uid) {
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
                                    if(mAuth.getCurrentUser().getEmail().equals(holder.itemView.
                                            getContext().getResources().getString(R.string.email))) {

                                        final int[] total = new int[1];
                                        mDatabase.child(key).child("money").child("money_borrowed").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                total[0] = Integer.parseInt(dataSnapshot.getValue().toString()) - Integer.parseInt(model.getSpend_money());
                                                mDatabase.child(key).child("money").child("money_borrowed").setValue(String.valueOf(total[0]));
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                           mDatabase.child(key).child("cash_borrow").child(uid).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    final ArrayList<String> arrayList = new ArrayList<>();
                                                    final ArrayList<String> arrayListAddition = new ArrayList<>();



                                                    DatabaseReference databaseReference = FirebaseDatabase
                                                            .getInstance()
                                                            .getReference().child("cash_borrow");
                                                    databaseReference.child(key).child("cash_borrow").addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            for(DataSnapshot snapshot: dataSnapshot.getChildren()){

                                                                arrayList.add(snapshot.getKey());
                                                            }
                                                            Log.d(TAG, "onDataChange: " + arrayList);
                                                            for(int i = 0 ; i < dataSnapshot.getChildrenCount() ; i++ ){
                                                                String value = dataSnapshot
                                                                        .child(arrayList.get(i))
                                                                        .child("spend_money").getValue().toString();
                                                                arrayListAddition.add(value);
                                                            }
                                                            Log.d(TAG, "onDataChange: " + arrayListAddition);
                                                            FirebaseDatabase.getInstance()
                                                                    .getReference().child("cash_borrow").child(key).child("money")
                                                                    .child("money_borrowed").setValue(setupOverViewValue(arrayListAddition));

                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        }
                                                    });

                                                    Toast.makeText(holder.itemView.getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }else {
                                        Toast.makeText(holder.itemView.getContext(), "Sorry You are not Authenticated", Toast.LENGTH_SHORT).show();

                                    }


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

        void menuDisplay(final com.example.balochistanoiltraders.ExpancesAdapter.ExpancesHolder holder, final Note model, final String uid){

            PopupMenu popupMenu = new PopupMenu(holder.itemView.getContext(), holder.itemView);
            popupMenu.getMenuInflater().inflate(R.menu.menu_for_long_click, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if(item.getItemId() == R.id.editItem){
                        Intent intent = new Intent(holder.itemView.getContext(), ExpanceDetails.class);
                        intent.putExtra("name",model.getName());
                        intent.putExtra("moneySpend",model.getSpend_money());
                        intent.putExtra("description",model.getDescription());
                        intent.putExtra("key",uid);
                        holder.itemView.getContext().startActivity(intent);

                    } else if(item.getItemId() == R.id.deleteItem){
                        dialogShow(holder, model, uid);
                    }
                    return true;
                }
            });

            popupMenu.show();
        }


        class ExpancesHolder extends RecyclerView.ViewHolder{
            TextView text;
            TextView money, name, description, Date;


            public ExpancesHolder(@NonNull View itemView) {
                super(itemView);
                text = (TextView) itemView.findViewById(R.id.chracterName);
                name = (TextView) itemView.findViewById(R.id.name);
                money = (TextView) itemView.findViewById(R.id.spend);
                description = (TextView) itemView.findViewById(R.id.description);
                Date = (TextView) itemView.findViewById(R.id.head);

            }
        }
    }


}
