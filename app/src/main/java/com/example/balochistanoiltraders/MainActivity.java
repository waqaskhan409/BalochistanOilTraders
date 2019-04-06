package com.example.balochistanoiltraders;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        Employees.OnFragmentInteractionListener,
        DepositAndPayment.OnFragmentInteractionListener,
        Inventory.OnFragmentInteractionListener,
        Account.OnFragmentInteractionListener,
        About.OnFragmentInteractionListener {
    private static final String TAG = "MainActivity";
    private FirebaseAuth mAuth;
    int count = 1;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       /* new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, SplashS.class);
                startActivity(intent);
                finish();
            }
        }, 3000);*/
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Log.d(TAG, "onCreate: Initialization of Firebase");

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null){
            Log.d(TAG, "onCreate: " + mAuth.getCurrentUser());
        }else {

            Intent intent = new Intent(this, LoginActivity.class);

            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View view = navigationView.getHeaderView(0);
        ImageView image = view.findViewById(R.id.imageView);
        TextView name = view.findViewById(R.id.name);
        TextView email = view.findViewById(R.id.email);

        String nam = "";
        String a = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        email.setText(a);
        for (int i = 0; i < a.length(); i++) {
            if(a.charAt(i) == '@'){
                break;
            }
            nam = nam + a.charAt(i);
        }
        name.setText(nam);

        getSupportFragmentManager().beginTransaction().replace(R.id.flContent, new ListForEachSell()).commit();
//        Intent intent = new Intent(this, CashBorrowed.class);
//        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            dialogShow();

        }

    }
    private void dialogShow() {
        final LayoutInflater li = LayoutInflater.from(this);
        View view= li.inflate(R.layout.layout_for_confirm, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(view);

//      final TextView textView = (TextView) view.findViewById(R.id.confirm) ;
        // set dialog message
        alertDialogBuilder.setTitle("Are You Sure to Exit the App?");
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {

                                finish();
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
    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.list) {
//            Intent intent = new Intent(this, List.class);
//            startActivity(intent);
            // Handle the camera action
            getSupportFragmentManager().beginTransaction().replace(R.id.flContent, new ListForEachSell()).commit();

        } else if (id == R.id.account) {
            getSupportFragmentManager().beginTransaction().replace(R.id.flContent, new Account()).commit();

//        }else if (id == R.id.expance) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.flContent, new Expances()).commit();

        } else if (id == R.id.employees) {
            getSupportFragmentManager().beginTransaction().replace(R.id.flContent, new Employees()).commit();

        } else if (id == R.id.depreciations) {
            getSupportFragmentManager().beginTransaction().replace(R.id.flContent, new Depreciation()).commit();

        } else if (id == R.id.logout) {

            mAuth.signOut();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        } else if (id == R.id.about) {
            getSupportFragmentManager().beginTransaction().replace(R.id.flContent, new About()).commit();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
