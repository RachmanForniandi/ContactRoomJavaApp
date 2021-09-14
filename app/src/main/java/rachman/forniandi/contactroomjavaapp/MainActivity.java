package rachman.forniandi.contactroomjavaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import rachman.forniandi.contactroomjavaapp.adapter.ContactsAdapter;
import rachman.forniandi.contactroomjavaapp.databinding.ActivityMainBinding;
import rachman.forniandi.contactroomjavaapp.databinding.ContactListItemBinding;
import rachman.forniandi.contactroomjavaapp.databinding.ContentMainBinding;
import rachman.forniandi.contactroomjavaapp.databinding.LayoutAddContactBinding;
import rachman.forniandi.contactroomjavaapp.db.Contact;
import rachman.forniandi.contactroomjavaapp.db.ContactAppDb;

public class MainActivity extends AppCompatActivity{

    /*@BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.list_contacts)
    RecyclerView listContacts;

    @BindView(R.id.fab)
    FloatingActionButton fabButton;*/

    private ActivityMainBinding activityMainBinding;
    private ContentMainBinding contentMainBinding;
    private LayoutAddContactBinding binding;
    private ContactsAdapter contactsAdapter;
    private final ArrayList<Contact> contactArrayList = new ArrayList<>();
    private ContactAppDb contactsAppDb;
    private static final String TAG="MainActivityTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        contentMainBinding = ContentMainBinding.bind(activityMainBinding.getRoot());
        //setContentView(R.layout.activity_main);
        setContentView(activityMainBinding.getRoot());
        //setSupportActionBar(activityMainBinding.toolbar);
        getSupportActionBar().setTitle(" Contacts Manager ");

        contactsAppDb =Room.databaseBuilder(getApplicationContext(),ContactAppDb.class,"ContactDB").allowMainThreadQueries().build();

        contactArrayList.addAll(contactsAppDb.getContactDAO().getContacts());

        //new GetAllContactsAsyncTask().execute();

        contactsAdapter = new ContactsAdapter(this, contactArrayList, MainActivity.this);
        /*RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);*/
        contentMainBinding.listContacts.setItemAnimator(new DefaultItemAnimator());
        contentMainBinding.listContacts.setAdapter(contactsAdapter);

        activityMainBinding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAndEditContacts(false, null, -1);
            }


        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void addAndEditContacts(final boolean isUpdate, final Contact contact, final int position) {
        binding = LayoutAddContactBinding.inflate(LayoutInflater.from(this));
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(binding.getRoot());

        binding.newContactTitle.setText(!isUpdate ? "Add New Contact" : "Edit Contact");

        if (isUpdate && contact != null) {
            binding.name.setText(contact.getName());
            binding.email.setText(contact.getEmail());
        }

        builder
                .setCancelable(false)
                .setPositiveButton(isUpdate ? "Update" : "Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {

                    }
                })
                .setNegativeButton("Delete",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {

                                if (isUpdate) {

                                    deleteContact(contact, position);
                                } else {

                                    dialogBox.cancel();

                                }

                            }
                        });


        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(binding.name.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Enter contact name!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    alertDialog.dismiss();
                }


                if (isUpdate && contact != null) {

                    updateContact(binding.name.getText().toString(), binding.email.getText().toString(), position);
                } else {

                    createContact(binding.name.getText().toString(), binding.email.getText().toString());
                }
            }
        });

    }


    private void deleteContact(Contact contact, int position) {

        contactArrayList.remove(position);
        contactsAppDb.getContactDAO().deleteContact(contact);
        contactsAdapter.notifyDataSetChanged();

        //new DeleteContactAsyncTask().execute(contact);

    }

    private void updateContact(String name, String email, int position) {

        Contact contact = contactArrayList.get(position);

        contact.setName(name);
        contact.setEmail(email);

        contactsAppDb.getContactDAO().updateContact(contact);
        //new UpdateContactAsyncTask().execute(contact);

        contactArrayList.set(position, contact);
        contactsAdapter.notifyDataSetChanged();

    }

    private void createContact(String name, String email) {

        long id = contactsAppDb.getContactDAO().addContact(new Contact(0,name,email));
        Contact contact = contactsAppDb.getContactDAO().getContact(id);

        if (contact != null){
            contactArrayList.add(0,contact);
            contactsAdapter.notifyDataSetChanged();
        }
        //new CreateContactAsyncTask().execute(new Contact(0,name,email));

    }

    private class GetAllContactsAsyncTask extends AsyncTask<Void,Void,Void> {


        @Override
        protected Void doInBackground(Void... voids) {

            contactArrayList.addAll(contactsAppDb.getContactDAO().getContacts());
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            contactsAdapter.notifyDataSetChanged();
        }
    }

    private class CreateContactAsyncTask extends AsyncTask<Contact,Void,Void>{



        @Override
        protected Void doInBackground(Contact... contacts) {

            long id = contactsAppDb.getContactDAO().addContact(contacts[0]);


            Contact contact = contactsAppDb.getContactDAO().getContact(id);

            if (contact != null) {

                contactArrayList.add(0, contact);


            }

            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            contactsAdapter.notifyDataSetChanged();
        }
    }

    private class UpdateContactAsyncTask extends AsyncTask<Contact,Void,Void>{


        @Override
        protected Void doInBackground(Contact... contacts) {

            contactsAppDb.getContactDAO().updateContact(contacts[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            contactsAdapter.notifyDataSetChanged();
        }
    }

    private class DeleteContactAsyncTask extends AsyncTask<Contact,Void,Void>{

        @Override
        protected Void doInBackground(Contact... contacts) {

            contactsAppDb.getContactDAO().deleteContact(contacts[0]);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            contactsAdapter.notifyDataSetChanged();
        }
    }

    RoomDatabase.Callback callback= new RoomDatabase.Callback() {


        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            //Toast.makeText(getApplicationContext()," On Create Called ",Toast.LENGTH_LONG).show();
            Log.i(TAG, " on create invoked ");

            createContact("name 1","email 1");
            createContact("name 2","email 2");
            createContact("name 3","email 3");

        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);

            //  Toast.makeText(getApplicationContext()," On Create Called ",Toast.LENGTH_LONG).show();
            Log.i(TAG, " on open invoked ");

        }

    };
}