package test.com.contactsapplication.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.List;

import test.com.contactsapplication.R;
import test.com.contactsapplication.adapter.ContactsAdapter;
import test.com.contactsapplication.constants.Constants;
import test.com.contactsapplication.database.ContactsDbHelper;
import test.com.contactsapplication.database.ContactsDbTask;
import test.com.contactsapplication.entity.ContactsEntity;
import test.com.contactsapplication.entity.ContactsInfoEntity;

public class ContactDetailsActivity extends AppCompatActivity {
    private RecyclerView mContactsView;
    private RequestQueue mRequestQueue;
    private ContactsAdapter mAdapter;
    private ProgressDialog mProgressDialog;
    private ContactsDbTask mTask;
    private List<ContactsInfoEntity> mContactsInfoList;
    private ContactsDbHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_details);
        mContactsView = (RecyclerView) findViewById(R.id.contact_list);
        mRequestQueue = Volley.newRequestQueue(this);
        mProgressDialog = new ProgressDialog(this);
        mDatabaseHelper = ContactsDbHelper.getDbHelperInstance(this);
        fetchContactsList();
    }

    private void fetchContactsList() {
        mProgressDialog.setMessage(Constants.DIALOG_MESSAGE);
        mProgressDialog.show();
        String url = Constants.CONTACTS_URL;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ContactsEntity contactsEntity = new Gson().fromJson(response, ContactsEntity.class);
                mTask = new ContactsDbTask(ContactDetailsActivity.this, mDatabaseHelper, contactsEntity.getContactsInfo());
                mTask.execute();
                dismissProgressDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ContactDetailsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                dismissProgressDialog();
            }
        });
        mRequestQueue.add(request);
    }


    public void updateView(List<ContactsInfoEntity> contactsInfoList) {
        mContactsInfoList = contactsInfoList;
        mAdapter = new ContactsAdapter(contactsInfoList, this, mItemClickListener);
        mContactsView.setAdapter(mAdapter);
        mContactsView.setLayoutManager(new LinearLayoutManager(this));
    }

    private ContactsAdapter.OnItemClickListener mItemClickListener = new ContactsAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(int position) {
            showAlertDialog(position);
        }
    };

    private void showAlertDialog(int position) {
        final int contact_position = position;
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(Constants.ALERT_DIALOG_TITLE);
        dialog.setMessage(Constants.ALERT_DIALOG_MESSAGE);
        dialog.setPositiveButton(Constants.ALERT_DIALOG_POSITIVE_BUTTON, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mDatabaseHelper.deleteContactsFromDB(mContactsInfoList.get(contact_position));
                mTask = new ContactsDbTask(ContactDetailsActivity.this, mDatabaseHelper, null);
                mTask.execute();
                dialogInterface.cancel();
            }
        });
        dialog.setNegativeButton(Constants.ALERT_DIALOG_NEGATIVE_BUTTON, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        dialog.show();
    }

    private void dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }
}
