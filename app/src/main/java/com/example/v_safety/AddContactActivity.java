package com.example.v_safety;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;

import static com.example.v_safety.SetContactsActivity.contactNamesList;
import static com.example.v_safety.SetContactsActivity.phoneNumsList;

public class AddContactActivity extends AppCompatActivity implements View.OnKeyListener{

    EditText nameEditText;
    EditText phoneNumberEditText;
    Button confirmContactButton;

    SharedPreferences sharedPreferences;

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN){
            confirmContact(v);
        }
        return false;
    }

    public void confirmContact(View view) {
        if (nameEditText.getText().toString().equals("") && phoneNumberEditText.getText().toString().equals("")){
            Toast.makeText(this, "No name and phone number entered!", Toast.LENGTH_SHORT).show();
        } else if (nameEditText.getText().toString().equals("") || nameEditText.getText() == null) {
            Toast.makeText(this, "No name is entered!", Toast.LENGTH_SHORT).show();
        } else if (phoneNumberEditText.getText().toString().equals("") || phoneNumberEditText.getText() == null){
            Toast.makeText(this, "No phone number is entered!", Toast.LENGTH_SHORT).show();
        } else {
            contactNamesList.add(nameEditText.getText().toString().trim());
            phoneNumsList.add(phoneNumberEditText.getText().toString().trim());
            SetContactsActivity.arrayAdapter.notifyDataSetChanged();

            Toast.makeText(this, "Added Contact!", Toast.LENGTH_SHORT).show();

            nameEditText.setText("");
            phoneNumberEditText.setText("");

            saveContacts();

            Intent intent = new Intent(getApplicationContext(), SetContactsActivity.class);
            startActivity(intent);
        }
    }

    public void saveContacts(){
        sharedPreferences = getApplicationContext().getSharedPreferences("com.example.v_safety", Context.MODE_PRIVATE);
        HashSet<String> contactSet = new HashSet<>(contactNamesList);

        HashSet<String> phoneNumsSet = new HashSet<>(phoneNumsList);

        sharedPreferences.edit().putStringSet("Contacts", contactSet).apply();
        sharedPreferences.edit().putStringSet("PhoneNumbers", phoneNumsSet).apply();
    }

    public void back(View view){
        Intent intent = new Intent(getApplicationContext(), SetContactsActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);


        nameEditText = findViewById(R.id.nameEditText);
        phoneNumberEditText= (EditText) findViewById(R.id.phoneNumberEditTextNumber);
        confirmContactButton = findViewById(R.id.confirmContactButton);



    }
}