package org.delta.pass;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by HP on 19-02-2016.
 */
public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {
    private String[] mDataset;
    ArrayList<String> contact;
    ArrayList<String> timestamp;
    ArrayList<String> jid;
    int ppicpos;
    Typeface t;
    Context context;


    public static Date EpochConvert(String date)
    {
        return new Date(Long.parseLong(date));
    }
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView Contact;
        public TextView TimeStamp;
        public ImageView ppic;
        public ImageView unread;
        public ViewHolder(View v) {
            super(v);
            Contact = (TextView)v.findViewById(R.id.Contact);
            TimeStamp = (TextView)v.findViewById(R.id.Time);
            ppic =(ImageView)v.findViewById(R.id.profilepic);
            unread=(ImageView)v.findViewById(R.id.unreadview);


        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ChatListAdapter(ArrayList<String> contact,ArrayList<String> tm,ArrayList<String> jid,Context context) {
        this.contact=contact;
        this.timestamp=tm;
        this.jid=jid;
        this.context=context;
        this.ppicpos=-1;
        this.t=Typeface.createFromAsset(context.getAssets(),"fonts/hn.otf");
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ChatListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chatlist_single, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        holder.Contact.setTypeface(t);
        holder.TimeStamp.setTypeface(t);

        if(Utilities.contacts.get(contact.get(position)).name!=null)
        holder.Contact.setText(Utilities.contacts.get(contact.get(position)).name);

        else
            holder.Contact.setText(contact.get(position).substring(2,contact.get(position).indexOf("@")));

        holder.TimeStamp.setText(timestamp.get(position));

        Date d=EpochConvert(timestamp.get(position));
        holder.TimeStamp.setText(d.getHours()+":"+d.getMinutes()+"  "+d.getDate()+"/"+String.valueOf(d.getMonth()+1)+"/"+d.getYear());

        if(timestamp.get(position).compareTo(Utilities.lastread)>0)
        {
            holder.unread.setVisibility(View.VISIBLE);
        }
        else
            holder.unread.setVisibility(View.INVISIBLE);


        if(position==ppicpos) {
            if (holder.ppic.getVisibility() == View.GONE) {
                //jid.get(position).substring(0,jid.indexOf("@"))
                holder.ppic.setVisibility(View.VISIBLE);
                Bitmap b = BitmapFactory.decodeFile("/storage/emulated/0/WhatsApp/Profile Pictures/" +contact.get(position).substring(0,contact.get(position).indexOf("@"))+ ".jpg");
                holder.ppic.setImageBitmap(b);
            } else {
                holder.ppic.setVisibility(View.GONE);
            }
        }
        else{

            holder.ppic.setVisibility(View.GONE);
        }

        holder.Contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent i=new Intent(context,ChatScreen.class);

                i.putExtra("jid",jid.get(position));


                context.startActivity(i);

            }
        });

        holder.TimeStamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ppicpos=position;

                if(position==ppicpos) {
                    if (holder.ppic.getVisibility() == View.GONE) {
                        //jid.get(position).substring(0,jid.indexOf("@"))
                        holder.ppic.setVisibility(View.VISIBLE);
                        Bitmap b = BitmapFactory.decodeFile("/storage/emulated/0/WhatsApp/Profile Pictures/" +contact.get(position).substring(0,contact.get(position).indexOf("@"))+ ".jpg");
                        holder.ppic.setImageBitmap(b);
                    } else {
                        holder.ppic.setVisibility(View.GONE);
                        ppicpos=-1;
                    }
                }
                else{

                    holder.ppic.setVisibility(View.GONE);
                }



            }
        });



    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return contact.size();
    }
}
