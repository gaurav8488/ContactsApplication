package test.com.contactsapplication.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Gaurav.Arora on 29-05-2017.
 */

public class ContactsInfoEntity implements Serializable {

    @SerializedName("name")
    private String name;

    @SerializedName("uid")
    private String uid;

    public void setName(String name) {
        this.name = name;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public String getUid() {
        return uid;
    }


}
