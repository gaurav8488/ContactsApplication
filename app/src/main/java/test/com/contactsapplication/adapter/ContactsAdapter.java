package test.com.contactsapplication.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import test.com.contactsapplication.R;
import test.com.contactsapplication.entity.ContactsInfoEntity;

/**
 * Created by Gaurav.Arora on 29-05-2017.
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder> {
    private List<ContactsInfoEntity> mContactsList;
    private Context mContext;
    private OnItemClickListener mItemClickListener;

    public ContactsAdapter(List<ContactsInfoEntity> list, Context context, OnItemClickListener listener) {
        mContactsList = list;
        mContext = context;
        mItemClickListener = listener;
    }

    @Override
    public ContactsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(mContext).inflate(R.layout.contact_row, parent, false);
        ContactsViewHolder viewHolder = new ContactsViewHolder(rowView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ContactsViewHolder holder, int position) {
        holder.mName.setText(mContactsList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mContactsList.size();
    }


    public class ContactsViewHolder extends RecyclerView.ViewHolder {
        public TextView mName;
        public ImageView mDeleteIcon;

        ContactsViewHolder(View rowView) {
            super(rowView);
            mName = (TextView) rowView.findViewById(R.id.contact_name);
            mDeleteIcon = (ImageView) rowView.findViewById(R.id.delete_icon);
            mDeleteIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemClickListener.onItemClick(getLayoutPosition());
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
