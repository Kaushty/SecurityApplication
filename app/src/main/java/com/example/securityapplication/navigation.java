package com.example.securityapplication;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.securityapplication.Helper.FirebaseHelper;
import com.example.securityapplication.model.Device;
import com.example.securityapplication.model.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class navigation extends AppCompatActivity {

    int count=0,aa;
    static User newUser=new User();
    Boolean is_home=true;

    SQLiteDBHelper db=new SQLiteDBHelper(navigation.this);
    public static Boolean test=false;
    public static TextView tmode;
    public static TextView tmode1;

    private int flag=0;
    Menu optionsMenu;

    //private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    /*private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mUsersDatabaseReference;
    private DatabaseReference mDevicesDatabaseReference;*/
    //private User user;
    private Device device;
    private String TAG = "NavigatonFragment";
    private String mImeiNumber;
    private TelephonyManager telephonyManager;

    private ValueEventListener mUsersDatabaseReferenceListener;
    private FirebaseHelper firebaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getImei();

        setContentView(R.layout.activity_navigation);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListner);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_continer,new home_fragment()).commit();

        firebaseHelper = FirebaseHelper.getInstance();
        firebaseHelper.initFirebase();
        firebaseHelper.initContext(navigation.this);
        firebaseHelper.initGoogleSignInClient(getString(R.string.server_client_id));
        /*mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();*/
        firebaseUser = firebaseHelper.getFirebaseAuth().getCurrentUser();
        // initialise database references
        //initDataBaseReferences();

        //sqlite db code here
        Log.d("SQL12","No. of rows in navigation1 "+db.numberOfRows());
        if((aa=db.numberOfRows())==0)
        {  getData(1);
            Log.d("SQL","No. of rows in navigation "+aa);}
        else
        {
            Log.d("checking","oncreate option menu 3 is running");
            getData(2);
            if(db.getTestmode())
            {
                Log.d("checking","oncreate option menu 2 is running");
                test=true;
            }
        }

        tmode1=(TextView)findViewById(R.id.testmode);

        tmode1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag==0){
                    flag=1;
                    tmode1.setText("TEST MODE : ON");

                  //  tmode1.setPadding(0,0,10,0);
                    tmode1.setTextColor(Color.GREEN);
                }
                else {
                    flag=0;
                    tmode1.setTextColor(Color.WHITE);
                    tmode1.setText("TEST MODE : OFF");
                   // tmode.setTextColor(Color.WHITE);

                }
            }
        });
    }

    private void checkFirstSosContact(HashMap<String,String> sosContacts){
        // check if first sos contact is added
        if (sosContacts == null){
            Intent sosPage = new Intent(navigation.this, sos_page.class);
            startActivityForResult(sosPage,1);
        }
    }

    public void getData(final int check){
  //      final FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            String uid = firebaseUser.getUid();
            mUsersDatabaseReferenceListener = firebaseHelper.getUsersDatabaseReference().child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    newUser = dataSnapshot.getValue(User.class);
//                    Log.d("Paid12345","schin1"+newUser.getName()+newUser.IsPaid());
                    // check if user signed in from two devices
                    recheckUserAuthentication();
                    if (check == 1) {
                        db.addUser(newUser);
//                        Log.d("FirebaseUsername", newUser.getName() + " 1 " + newUser.getEmail());
                        db.addsosContacts(newUser.getSosContacts()); //to fetch SOSContacts from Firebase
                    } else if (check == 2) {
//                        Log.d("FirebaseUsername", newUser.getName() + " 2 " + newUser.getEmail());
                        db.updateUser(newUser);
                        db.addsosContacts(newUser.getSosContacts()); //to fetch SOSContacts from Firebase even if tablepresent
                        SendSMSService.initContacts(); //to initialise SOS Contacts as soon as the database is ready
                    }
//                    Log.d("Paid12345","schin"+newUser.getName()+newUser.IsPaid());
                    if(String.valueOf(dataSnapshot.child("isPaid").getValue()).equals("true")){
                        Log.d("Paid12345","i am here");
                        home_fragment.setpaid(true);
                    }
                    else{
                        home_fragment.setpaid(false);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }


    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu,menu);
        optionsMenu=menu;
        MenuItem titem=optionsMenu.findItem(R.id.testmode);
        test=db.getTestmode();
        Log.d("checking","oncreate option menu is running"+db.getTestmode());
        if(test)
            titem.setChecked(true);
        else
            titem.setChecked(false);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            switch (item.getItemId()) {
                case R.id.testmode:
                    if (item.isChecked()) {
                        item.setChecked(false);
                        test = false;
                        db.updatetestmode(test);
                        //Log.d("checking1", String.valueOf(db.getTestmode()) + "home" + is_home);
                        Toast.makeText(this, "Test mode Off", Toast.LENGTH_SHORT).show();
                        if (is_home) {
                            TextView tv = (TextView) findViewById(R.id.textView3);
                            tv.setVisibility(View.INVISIBLE);
                        }
                    } else {
                        item.setChecked(true);
                        test = true;
                        db.updatetestmode(test);
                        //Log.d("checking2", String.valueOf(db.getTestmode()));
                        Toast.makeText(this, "Test mode On", Toast.LENGTH_SHORT).show();
                        if (is_home) {
                            TextView tv = (TextView) findViewById(R.id.textView3);
                            tv.setVisibility(View.VISIBLE);
                        }
                    }


                default:
                    return super.onOptionsItemSelected(item);
            }
        }
        catch (Exception e)
        {
            item.setChecked(db.getTestmode());
            Toast.makeText(this, "Loading.....please wait for a second", Toast.LENGTH_LONG).show();
        }
        finally {
            return true;
        }

    }
    Fragment selectedFragment = null;
    private BottomNavigationView.OnNavigationItemSelectedListener navListner =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    switch(menuItem.getItemId()){
                        case R.id.home:
                            is_home=true;
                            selectedFragment = new home_fragment();
                            break;
                        case R.id.setting:
                            is_home=false;
                            selectedFragment = new setting_fragment();
                            break;
                        case R.id.save:
                            is_home=false;
                            selectedFragment = new saviour_fragment();
                            break;
                        case R.id.profile:
                            is_home=false;
                            selectedFragment = new profile_fragment();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_continer,
                            selectedFragment).commit();

                    return true;
                }
    };

    @Override
    public void onBackPressed(){
        AlertDialog.Builder a_builder = new AlertDialog.Builder(navigation.this);
        a_builder.setMessage("Do you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog alert = a_builder.create();
        alert.setTitle("Message");
        alert.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==10 && requestCode==1)
            try {
                closeNow();
            }catch (Exception e){
                Log.d(TAG,"Exception on closing activity:"+e.getMessage());
                finish();
            }
    }

    public void sos(View view) {
        startActivity(new Intent(this,sos_page.class));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 101:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getImei();
                } else {
                    closeNow();
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void closeNow(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
            finishAffinity();
        }
        else{
            finish();
        }
    }

    private void getImei(){
        telephonyManager = (TelephonyManager) getSystemService(this.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 101);
            return;
        }
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mImeiNumber = telephonyManager.getImei(0);
                Log.d("IMEI", "IMEI Number of slot 1 is:" + mImeiNumber);
            } else {
                mImeiNumber = telephonyManager.getDeviceId();
            }
        }
    }

    private void recheckUserAuthentication(){
        if (FirebaseAuth.getInstance().getCurrentUser() == null)
            return;
        Log.d(TAG,FirebaseAuth.getInstance().getCurrentUser().getEmail());
        Log.d(TAG,firebaseUser.getEmail());
        Log.d(TAG,"Inside recheckUserAuthentication");
        getImei();
        Log.d(TAG,"Imei of device:"+mImeiNumber);
        Log.d(TAG,"Imei from firebase:"+newUser.getImei());
        if (!newUser.getImei().equals(mImeiNumber)){
            // same user trying to login from multiple devices -> logout the user
            Log.d(TAG, "User is LoggedIn in other device");
            Toast.makeText(navigation.this,"You are logged in another device .Please logout from old device to continue", Toast.LENGTH_LONG).show();
            LogOutAndStartMainActivity();
        }
        else{
            if (newUser != null)
                checkFirstSosContact(newUser.getSosContacts());
        }
    }

    public void LogOutAndStartMainActivity(){
        firebaseHelper.getUsersDatabaseReference().child(firebaseUser.getUid()).removeEventListener(mUsersDatabaseReferenceListener);
        firebaseHelper.getUsersDatabaseReference().removeEventListener(mUsersDatabaseReferenceListener);
        firebaseHelper.makeDeviceImeiNull(mImeiNumber);
        firebaseHelper.firebaseSignOut();
        firebaseHelper.googleSignOut(navigation.this);

        Intent mLogOutAndRedirect= new Intent(navigation.this, MainActivity.class);
        mLogOutAndRedirect.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mLogOutAndRedirect);
        //finishing the navigation activity
        try {
            closeNow();
            Log.d(TAG,"closed activity successfully");
        }catch (Exception e){
            Log.d(TAG,"Closing app exception:"+e.getMessage());
            finish();
        }
    }
}
