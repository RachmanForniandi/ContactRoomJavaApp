package rachman.forniandi.contactroomjavaapp.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import rachman.forniandi.contactroomjavaapp.db.Contact;

@Database(entities = {Contact.class},version = 1)
public abstract class ContactAppDb extends RoomDatabase {

    public abstract ContactDAO getContactDAO();
}
