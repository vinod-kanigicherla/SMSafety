
package com.example.v_safety;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.SettingInjectorService;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class SetContactsActivity extends AppCompatActivity{

    ListView contactsListView;

    static ArrayList<String> contactNamesList;
    static ArrayList<String> phoneNumsList;

    SharedPreferences sharedPreferences;
    static ArrayAdapter arrayAdapter;

    HashSet<String> savedContacts;
    HashSet<String> savedPhoneNumbers;


    public void addContact(View view) {
        Intent intent = new Intent(getApplicationContext(), AddContactActivity.class);
        startActivity(intent);
    }

    public void displaySavedContacts() {
        sharedPreferences = getApplicationContext().getSharedPreferences("com.example.v_safety", Context.MODE_PRIVATE);

        savedContacts = (HashSet<String>) sharedPreferences.getStringSet("Contacts", null);
        savedPhoneNumbers = (HashSet<String>) sharedPreferences.getStringSet("PhoneNumbers", null);

        if (savedContacts != null &&  savedPhoneNumbers != null) {
            contactNamesList = new ArrayList<String>(savedContacts);
            phoneNumsList = new ArrayList<String>(savedPhoneNumbers);
        }
    }

    public void home(View view){
        Intent intent  = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    public void clear(View view){
        new AlertDialog.Builder(view.getContext())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Clear Contacts!?")
                .setMessage("Are you sure you want to clear you contacts?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        contactNamesList.clear();
                        phoneNumsList.clear();
                        savedContacts.clear();
                        savedPhoneNumbers.clear();
                        arrayAdapter.notifyDataSetChanged();
                        Toast.makeText(SetContactsActivity.this, "Cleared Contacts!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_contacts);

        contactsListView = findViewById(R.id.contactsListView);

        contactNamesList = new ArrayList<>();
        phoneNumsList = new ArrayList<>();

        displaySavedContacts();

        arrayAdapter =  new ArrayAdapter(this, android.R.layout.simple_list_item_1, contactNamesList);
        contactsListView.setAdapter(arrayAdapter);

        contactsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                new AlertDialog.Builder(view.getContext())
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setTitle(contactNamesList.get(position))
                        .setMessage(phoneNumsList.get(position))
                        .show();
            }
        });

    }
}