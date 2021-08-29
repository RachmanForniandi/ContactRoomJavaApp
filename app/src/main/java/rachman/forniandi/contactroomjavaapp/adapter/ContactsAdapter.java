package rachman.forniandi.contactroomjavaapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import rachman.forniandi.contactroomjavaapp.R;

import butterknife.ButterKnife;
import rachman.forniandi.contactroomjavaapp.MainActivity;
import rachman.forniandi.contactroomjavaapp.db.Contact;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactsHolder>{

    private final Context context;
    private final ArrayList<Contact> contactsList;
    private final MainActivity mainActivity;


    public ContactsAdapter(Context context, ArrayList<Contact> contacts, MainActivity mainActivity) {
        this.context = context;
        this.contactsList = contacts;
        this.mainActivity = mainActivity;
    }

    @Override
    public ContactsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_list_item,parent,false);
        return new ContactsHolder(view);
    }

    @Override
    public void onBindViewHolder(ContactsHolder holder, int position) {
        final Contact contact = contactsList.get(position);
        holder.tvName.setText(contact.getName());
        holder.tvEmail.setText(contact.getEmail());
    }

    @Override
    public int getItemCount() {
        return contactsList.size();
    }

    public class ContactsHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_name) TextView tvName;
        @BindView(R.id.tv_email)TextView tvEmail;
        public ContactsHolder(View itemView){

            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
