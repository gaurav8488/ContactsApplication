package test.com.contactsapplication.database;

import android.app.Activity;
import android.os.AsyncTask;

import java.util.List;

import test.com.contactsapplication.activity.ContactDetailsActivity;
import test.com.contactsapplication.entity.ContactsInfoEntity;

/**
 * Created by Gaurav.Arora on 29-05-2017.
 */

public class ContactsDbTask extends AsyncTask<String, String, List<ContactsInfoEntity>> {
    private ContactsDbHelper mDatabaseHelper;
    private List<ContactsInfoEntity> mContactsList;
    private ContactDetailsActivity mActivity;

    public ContactsDbTask(Activity activity, ContactsDbHelper databaseHelper, List<ContactsInfoEntity> contactsList) {
        mDatabaseHelper = databaseHelper;
        mContactsList = contactsList;
        mActivity = (ContactDetailsActivity) activity;
    }

    @Override
    protected List<ContactsInfoEntity> doInBackground(String... url) {
        if (mContactsList != null) {
            mDatabaseHelper.addContactsIntoDB(mContactsList);
        }
        List<ContactsInfoEntity> contactList = mDatabaseHelper.getContactsFromDB();
        return contactList;
    }

    @Override
    protected void onPostExecute(List<ContactsInfoEntity> contactsInfoList) {
        super.onPostExecute(contactsInfoList);
        mActivity.updateView(contactsInfoList);
    }
}
