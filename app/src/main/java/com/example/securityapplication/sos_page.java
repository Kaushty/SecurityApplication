package com.example.securityapplication;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.securityapplication.model.User;

import java.util.ArrayList;
import java.util.List;

public class sos_page extends AppCompatActivity {

    private ContentValues values;
    private String sos_n1,sos_n2,sos_n3,sos_n4,sos_n5;
    private String temp_n1,temp_n2,temp_n3,temp_n4,temp_n5;
    private SQLiteDBHelper mydb;
    private Validation val;
    private User user;
    private Button btn_Edit,btn_Save;
    private TextInputEditText c1, c2, c3, c4, c5;
    boolean c1added,c2added,c3added,c4added,c5added;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos_page);

         values = new ContentValues();
         mydb = new SQLiteDBHelper(this);
         user = new User();
         val = new Validation();

        initViews();
        initInitialContacts();
        initTempValues();
        initListeners();

    }

    private void initInitialContacts() {
        try{
            Cursor res;
            res = mydb.getSosContacts();
            if (res.getCount() == 0){
                Toast.makeText(getApplicationContext(),
                        "No SOS Contact records Found",
                        Toast.LENGTH_LONG).show();
                Log.d("SOS Activity","No Contact Data found ");
                initfirstvalues();
                FillViews();
            }
            while (res.moveToNext()) {
                user.setSosc1(res.getString(0));
                user.setSosc2(res.getString(1));
                user.setSosc3(res.getString(2));
                user.setSosc4(res.getString(3));
                user.setSosc5(res.getString(4));

                Log.d("SOS Activity", "User Object set in SOS activity successfully " +
                        "Sosc1 = " + user.getSosc1() + " Sosc2 = " + user.getSosc2());

                sos_n1 = user.getSosc1();
                sos_n2 = user.getSosc2();
                sos_n3 = user.getSosc3();
                sos_n4 = user.getSosc4();
                sos_n5 = user.getSosc5();
                Log.d("SOS Activity", "Current SOS Contacts loaded into Variables; sos_n1="+sos_n1+"sos_n2="+sos_n2);
                FillViews();
            }

        }
        catch (RuntimeException e){
            Log.d("SOS Contact page","Encountered Null pointer Exception; Setting initial values to empty");
            sos_n1 = "";
            sos_n2 = "";
            sos_n3 = "";
            sos_n4 = "";
            sos_n5 = "";
        }
    }

    private void FillViews() {
        c1.setText(sos_n1);
        c2.setText(sos_n2);
        c3.setText(sos_n3);
        c4.setText(sos_n4);
        c5.setText(sos_n5);
        Log.d("SOS Activity","FillViews() sos_n1="+sos_n1+" value");
        Log.d("SOS Activity","SOS contact views filled with values c1="+c1.getText()+"value before this");
    }

    private void initfirstvalues() {
        sos_n1 = "";
        sos_n2 = "";
        sos_n3 = "";
        sos_n4 = "";
        sos_n5 = "";
        Log.d("SOS Activity","SOS Contact table loaded for 1st time, empty values set; sos_n1=" +sos_n1+" value");
    }

    private void initTempValues() {
        temp_n1 = "";
        temp_n2 = "";
        temp_n3 = "";
        temp_n4 = "";
        temp_n5 = "";
    }


    private void initListeners(){

       /* c1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                sos_n1 = c1.getText().toString().trim();
                if (sos_n1.length() !=10){
                    c1.setError("please enter a valid mobile no.");
                }
                else if (sos_n1.length()==0) {
                    c1added = false;
                }
                else if (sos_n1.length()==10){
                    c1added = true;
                }
            }
        });
        c2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                sos_n2 = c2.getText().toString().trim();
                if (sos_n2.length() !=10){
                    c2.setError("please enter a valid mobile no.");
                }
                else if (sos_n2.length()==0){
                    c2added = false;
                }
                else if (sos_n2.length()==10){
                    c2added = true;
                }
            }
        });
        c3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if (c3.getText().toString().trim().length() !=10){
                    c3.setError("please enter a valid mobile no.");
                }
            }
        });
        c4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if (c4.getText().toString().trim().length() !=10){
                    c4.setError("please enter a valid mobile no.");
                }
            }
        });
        c5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {

                if (c5.getText().toString().trim().length() !=10){
                    c5.setError("please enter a valid mobile no.");
                }
            }
        });
*/

        btn_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*if(c1.getText().length()==10)
                {
                    if((c2.getText().length()==0||c2.getText().length()==10)&&(c3.getText().length()==0||
                            c3.getText().length()==10)&&(c4.getText().length()==0||c4.getText().length()==10)&&(c5.getText().length()==0||
                            c5.getText().length()==10)) {

                        c1.setEnabled(false);
                        c2.setEnabled(false);
                        c3.setEnabled(false);
                        c4.setEnabled(false);
                        c5.setEnabled(false);
                        Toast.makeText(sos_page.this, "Contact saved successfully", Toast.LENGTH_SHORT).show();
                        Intent intent;
                        intent = new Intent(sos_page.this,navigation.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(sos_page.this, "Please Field contact information Correctly", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(sos_page.this, "Emergency Contact 1 is mandodary", Toast.LENGTH_SHORT).show();
                    Toast.makeText(sos_page.this, "Emergency Contact 1 is mandatory", Toast.LENGTH_SHORT).show();
                }*/

                /*             if (c1.getText() != null) {
                    if (c2.getText().toString() != null) {
                        if (c3.getText().toString() != null) {
                            if (c4.getText().toString() != null) {
                                if (c5.getText().toString() != null) {
                                    //check if all numbers are unique and update unique
                                } else {
                                    //save 4 contacts
                                }
                            } else {
                                //save 3 contacts
                            }
                        } else {
                            //save 2 contacts
                        }
                    } else {
                        //save 1 contact
                    }
                }
                else{
                    //Toast Atleast 1 contact required
                    Toast.makeText(getApplicationContext(),"Atleast 1 SOS contact required",Toast.LENGTH_LONG).show();
                    return;
                }*/

                    //FOR c1
                Log.d("SOS","c1="+c1.getText());
                    if (c1.getText().toString().equals("")) {
                        //toast
                        Log.d("SOS Activity","C1 Empty");
                        Toast.makeText(getApplicationContext(), "1st contact is mandatory", Toast.LENGTH_LONG).show();
                        return;
                    } else {
                        temp_n1 = c1.getText().toString().trim();
                        if (!temp_n1.equals("")){
                            if (val.EditvalidatePhone(c1)){
                                if (temp_n1.equals(sos_n1)){
                                    //no change
                                    c1added = false;
                                }
                                if (!checkUnique(c1,1)){
                                    Log.d("SOS Activity", "OnClick : Duplicate c1 values found; c1="+c1.getText());
//                                    c2.setError("Duplicate Contact Found");
                                    c1added = false;
                                    return;
                                }
                                else {
                                    c1added = true;
//                                    user.setSosc1(temp_n1);
                                }
                            }
                            else {
//                                c1.setError("Enter a valid mobile number");
                                return;
                            }
                        }
                    }
                    //FOR C2
                    if (c2.getText().toString().equals("")) {
                        Log.d("Sos", "c2 found empty");
                        if (sos_n2.isEmpty()) {
                            //was empty previously too
                            c2added = false;
                            temp_n2="";
                        } else {
                            //update db .. was not empty previously
                            c2added = true;
                            temp_n2="";
//                            user.setSosc2(temp_n2);
                        }
                    } else {
                        temp_n2 = c2.getText().toString();
                        if (!temp_n2.equals("")){
                            if (val.EditvalidatePhone(c2)){
                                if (temp_n2.equals(sos_n2)) {
                                    //no change
                                    c2added = false;
                                }
                                if (!checkUnique(c2,2)) {
                                    //duplicate value raise alert
                                    Log.d("SOS Activity", "onClick : Duplicate c2 values found; c2="+c2.getText());
//                                    c2.setError("Duplicate Contact Found");
                                    c2added = false;
                                    return;
                                } else {
                                    //save c2
                                    c2.setError(null);
                                    c2added = true;
//                                    user.setSosc2(temp_n2);
                                }
                            }
                            else {
//                                c2.setError("Enter a valid Mobile number");
                                return;
                            }
                        }
                        else {
                            temp_n2.equals("");
                        }
                    }

                    //FOR C3
                    if (c3.getText().toString().equals("")) {
                        if (sos_n3.isEmpty()) {
                            //was empty previously
                            temp_n3="";
                            c3added = false;
                        } else {
                            c3added = true;
                            temp_n3="";
//                            user.setSosc3(temp_n3);
                        }
                    } else {
                        temp_n3 = c3.getText().toString();
                        if (!temp_n3.equals("")){
                            if (val.EditvalidatePhone(c3)){
                                if (temp_n3.equals(sos_n3)) {
                                    //no change
                                    c3added = false;
                                }
                                if (!checkUnique(c3,3)) {
                                    // duplicate value raise alert
                                    Log.d("SOS Activity","onClick : Duplicate C3 values found ; c3="+c3.getText());
//                                    c3.setError("Duplicate contact found");
                                    c3added = false;
                                    return;
                                } else {
                                    //save c3
                                    c3added = true;
                                    c3.setError(null);
//                                    user.setSosc3(temp_n3);
                                }
                            }
                            else {
//                                c3.setError("Enter a Valid Mobile Number");
                                return;
                            }
                        }
                    }
                    //FOR c4
                    if (c4.getText().toString().equals("")){
                        if (sos_n4.isEmpty()) {
                            //was empty previously
                            c4added = false;
                            temp_n4="";
                        } else {
                            //was not empty previously
                            c4added = true;
                            temp_n3="";
//                            user.setSosc4(temp_n4);
                        }
                    } else {
                        temp_n4 = c4.getText().toString();
                        if (!temp_n4.equals("")){
                           if (val.EditvalidatePhone(c4)){
                               if (temp_n4.equals(sos_n4)) {
                                   //no change
                                   c4added = false;
                               }
                               if (!checkUnique(c4,4)) {
                                   // duplicate value raise alert
                                   Log.d("SOS","onClick :Duplicate c4 values found; c4="+c4.getText());
//                                   c4.setError("Duplicate Contact found");
                                   c4added = false;
                                   return;
                               } else {
                                   //save c4
                                   c4added = true;
                                   c4.setError(null);
//                                   user.setSosc4(temp_n4);
                               }
                           }
                           else {
//                               c4.setError("Enter a valid mobile number");
                               return;
                           }
                        }
                    }
                    //FOR C5
                    if (c5.getText().toString().equals("")){
                        if (sos_n5.isEmpty()) {
                            //was empty previously
                            c5added = false;
                            temp_n5="";
                        }
                        else {
                            //was not empty previously
                            c5added = true;
                            temp_n5="";
//                            user.setSosc5(temp_n5);
                        }
                    } else {
                        temp_n5 = c5.getText().toString();
                        if (!temp_n5.equals("")){
                            if (val.EditvalidatePhone(c5)){
                                if (temp_n5.equals(sos_n5)) {
                                    //no change
                                    c5added = false;
                                }
                                if (!checkUnique(c5,5)) {
                                    // duplicate value raise alert
//                                    c5.setError("Duplicate contact found");
                                    Log.d("SOS","onClick :Duplicate c5 values found; c5="+c5.getText());
                                    c5added = false;
                                    return;
                                } else {
                                    //save c5
                                    c5.setError(null);
                                    c5added = true;
//                                    user.setSosc5(temp_n5);
                                }
                            }
                            else {
//                                c5.setError("Enter a valid mobile number");
                                return;
                            }
                        }
                    }

                    if (c1added||c2added||c3added||c4added||c5added){
                       user.setSosc1(temp_n1);
                       user.setSosc2(temp_n2);
                       user.setSosc3(temp_n3);
                       user.setSosc4(temp_n4);
                       user.setSosc5(temp_n5);

                       mydb.addsosContacts(user);
                    }
                    Toast.makeText(sos_page.this,"DATA saved successfully ",Toast.LENGTH_SHORT).show();

                c1.setEnabled(false);
                c2.setEnabled(false);
                c3.setEnabled(false);
                c4.setEnabled(false);
                c5.setEnabled(false);

                btn_Save.setEnabled(false);
                try {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
                catch (Exception e){
                    Log.d("SOS Activity","OnClick: Keyboard disappeared");
                }

               /* catch (Exception e){
//                    Toast.makeText(sos_page.this,"Encountered an issue while saving ")
                    Log.d("Sos Page","Encountered an issue while saving ");
                }*/

            }
        });

        btn_Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c1.setEnabled(true);
                c2.setEnabled(true);
                c3.setEnabled(true);
                c4.setEnabled(true);
                c5.setEnabled(true);
                btn_Save.setEnabled(true);
            }
        });
    }

    private boolean checkUnique(EditText contact,int i) {
        String check1 = c1.getText().toString();
        String check2 = c2.getText().toString();
        String check3 = c3.getText().toString();
        String check4 = c4.getText().toString();
        String check5 = c5.getText().toString();
        String input = contact.getText().toString();

        if (i==1){
            if (input.equals(check2)||input.equals(check3)||input.equals(check4)||input.equals(check5)){
                contact.setError("Duplicate mobile number found");
                return false;
            }
            else {
                return true;
            }
        }else if (i==2){
            if (input.equals(check1)||input.equals(check3)||input.equals(check4)||input.equals(check5)){
                contact.setError("Duplicate mobile number found");
                return false;
            }
            else {
                return true;
            }
        }else if (i==3){
            if (input.equals(check2)||input.equals(check1)||input.equals(check4)||input.equals(check5)){
                contact.setError("Duplicate mobile number found");
                return false;
            }
            else {
                return true;
            }
        }else if (i==4){
            if (input.equals(check2)||input.equals(check3)||input.equals(check1)||input.equals(check5)){
                contact.setError("Duplicate mobile number found");
                return false;
            }
            else {
                return true;
            }
        }else if (i==5){
            if (input.equals(check2)||input.equals(check3)||input.equals(check4)||input.equals(check1)){
                contact.setError("Duplicate mobile number found");
                return false;
            }
            else {
                return true;
            }
        }
        return true;
    }

    private void initViews() {

        btn_Save = (Button) findViewById(R.id.sossave);
        btn_Edit = (Button) findViewById(R.id.sosedit);

        c1 = (TextInputEditText) findViewById(R.id.sose1);
        c2 = (TextInputEditText) findViewById(R.id.sose2);
        c3 = (TextInputEditText) findViewById(R.id.sose3);
        c4 = (TextInputEditText) findViewById(R.id.sose4);
        c5 = (TextInputEditText) findViewById(R.id.sose5);

        c1.setEnabled(false);
        c2.setEnabled(false);
        c3.setEnabled(false);
        c4.setEnabled(false);
        c5.setEnabled(false);
        btn_Save.setEnabled(false);
    }
}
