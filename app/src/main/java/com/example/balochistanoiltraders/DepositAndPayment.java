package com.example.balochistanoiltraders;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DepositAndPayment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DepositAndPayment#newInstance} factory method to
 * create an instance of this fragment.
 */
@SuppressLint("ValidFragment")
public class DepositAndPayment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    RecyclerView recyclerView;
    private DepositeAdapter mAdapter;
    private ArrayList<String> arrayList = new ArrayList<>();
    private ArrayList<String> LIST = new ArrayList<>();
    String preDeposite, key;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public DepositAndPayment(String key) {

        // Required empty public constructor
        this.key = key;
        Log.d(TAG, "DepositAndPayment: " + key);

    }

    public DepositAndPayment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DepositAndPayment.
     */
    // TODO: Rename and change types and number of parameters
    public static DepositAndPayment newInstance(String param1, String param2) {
        DepositAndPayment fragment = new DepositAndPayment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_deposit_and_payment, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Deposite and payment");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("cash_borrow");
        recyclerView = (RecyclerView) view.findViewById(R.id.depositeAndPayment);
        LinearLayoutManager li = new LinearLayoutManager(getActivity());
//        li.setReverseLayout(true);
        recyclerView.setLayoutManager(li);
        recyclerView.setHasFixedSize(true);
        LIST.add("Bank");

        LIST.add("Others");


        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    arrayList.add(snapshot.getKey());
                }
                Log.d(TAG, "onDataChange: "+ arrayList);
                setupRecycler(true);
                preDeposite = dataSnapshot.child(arrayList.get(0))
                        .child("money")
                        .child("deposite_money")
                        .getValue().toString();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
        setupRecycler(false);
        return view;
    }

    private void setupRecycler(boolean bool) {

            FirebaseRecyclerOptions<Note> options = new FirebaseRecyclerOptions.Builder<Note>()
                    .setQuery(mDatabase.child(key).child("deposite_money"), Note.class).build();
            mAdapter = new DepositeAdapter(options, key);
            mAdapter.startListening();
            recyclerView.setAdapter(mAdapter);


        }

    @Override
    public void onStop() {
        super.onStop();
            mAdapter.stopListening();

    }

    @Override
    public void onStart() {
        super.onStart();
            mAdapter.startListening();


    }
    private void dialogShowForStock() {
        LayoutInflater li = LayoutInflater.from(getActivity());
        View view= li.inflate(R.layout.account_details, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(view);

        final TextInputEditText moneyTransfer = (TextInputEditText) view
                .findViewById(R.id.moneyTransferForAccountDetails);
        final AutoCompleteTextView bankName = (AutoCompleteTextView) view
                .findViewById(R.id.autoComplete);
        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, LIST);
        bankName.setAdapter(arrayAdapter);

        final EditText description = (EditText) view
                .findViewById(R.id.descriptionForAccountDetails);
        // set dialog message
        alertDialogBuilder.setTitle("Deposite and Payment");
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                String moneyT, bankN, remainingM, des;
                                moneyT = moneyTransfer.getText().toString();
                                bankN = bankName.getText().toString();
//                                remainingM = remainingMoney.getText().toString();
                                des = description.getText().toString();
                                if(TextUtils.isEmpty(moneyT) || TextUtils.isEmpty(bankN) || TextUtils.isEmpty(des)) {
                                    Toast.makeText(getContext(), "Sorry, Data didn't post due to Blank fields", Toast.LENGTH_SHORT).show();

                                }
                                else {

                                    long timeStamp = 1 * new Date().getTime();

                                    String NewDescTimeStamp= String.valueOf((9999999999999L+(-1 * timeStamp)));
                                    int total = Integer.parseInt(preDeposite) + Integer.parseInt(moneyT);
                                    mDatabase.child(key).child("money").child("deposite_money").setValue(String.valueOf(total));
                                    mDatabase = mDatabase.child(key).child("deposite_money").child(NewDescTimeStamp);
                                    mDatabase.child("money_transfer").setValue(moneyT);
                                    mDatabase.child("bank_name").setValue(bankN);
                                    mDatabase.child("description").setValue(des);

                                    Date date = new Date();
                                    SimpleDateFormat format=new SimpleDateFormat("dd/MM/yyyy");

                                    Log.d(TAG, "Dialogue: "+format.format(date));
                                    mDatabase.child("date").setValue(format.format(date));

                                    mDatabase = FirebaseDatabase.getInstance().getReference().child("cash_borrow");

                                    DatabaseReference databaseReference = mDatabase;
                                    final ArrayList<String> arrayList1 = new ArrayList<>();
                                    final ArrayList<String> arrayListAddition = new ArrayList<>();

                                    databaseReference.child(key).child("deposite_money").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            for(DataSnapshot snapshot: dataSnapshot.getChildren()){

                                                arrayList1.add(snapshot.getKey());
                                            }
                                            Log.d(TAG, "onDataChange: addition" + arrayList);
                                            for(int i = 0 ; i < dataSnapshot.getChildrenCount() ; i++ ){
                                                String value = dataSnapshot
                                                        .child(arrayList1.get(i))
                                                        .child("money_transfer").getValue().toString();
                                                arrayListAddition.add(value);
                                            }
                                            Log.d(TAG, "onDataChange: " + arrayListAddition);

                                            FirebaseDatabase.getInstance()
                                                    .getReference().child("cash_borrow").child(key).child("money").child("deposite_money").setValue(setupOverViewValue(arrayListAddition));


                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                    mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            preDeposite = dataSnapshot.child(key)
                                                    .child("money")
                                                    .child("deposite_money")
                                                    .getValue()
                                                    .toString();
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

    private String setupOverViewValue(ArrayList<String> arrayListAddition) {

            int t = 0;
            for (int i = 0; i < arrayListAddition.size(); i++) {
                t = t + Integer.parseInt(arrayListAddition.get(i));
            }
            return String.valueOf(t);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
