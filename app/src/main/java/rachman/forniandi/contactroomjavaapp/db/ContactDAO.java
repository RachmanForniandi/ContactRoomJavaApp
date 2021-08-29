package rachman.forniandi.contactroomjavaapp.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ContactDAO {

    @Insert
    public long addContact(Contact contact);

    @Update
    public void updateContact(Contact contact);

    @Delete
    public void deleteContact(Contact contact);


    @Query("select * from contacts")
    public List<Contact>getContacts();

    @Query("select * from contacts Where contact_id ==:contactId")
    public Contact getContact(long contactId);


}
