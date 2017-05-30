package test.com.contactsapplication.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Gaurav.Arora on 29-05-2017.
 */

public class ContactsEntity implements Serializable {

    @SerializedName("status")
    private String status;

    @SerializedName("result")
    private List<ContactsInfoEntity> contactsInfo;

    public List<ContactsInfoEntity> getContactsInfo(){
        return contactsInfo;
    }

}
