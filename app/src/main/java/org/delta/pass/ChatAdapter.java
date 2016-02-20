package org.delta.pass;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by HP on 19-02-2016.
 */
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private String[] mDataset;
    ArrayList<String> message;
    ArrayList<String> timestamp;
    ArrayList<String> contact;
    Context context;
    String jid;
    Typeface t;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView Message;
        public TextView TimeStamp;
        public TextView Contact;
        public ImageView ppic;
        public LinearLayout box;
        public ViewHolder(View v) {
            super(v);
            Message = (TextView)v.findViewById(R.id.Message);
            TimeStamp = (TextView)v.findViewById(R.id.Time);
            box = (LinearLayout)v.findViewById(R.id.singlelistlayout);
            Contact =(TextView)v.findViewById(R.id.Contact);
            ppic =(ImageView)v.findViewById(R.id.picmessage);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ChatAdapter(ArrayList<String> message,ArrayList<String> tm,ArrayList<String> c,String jid,Context context) {
        this.message=message;
        this.timestamp=tm;
        this.contact=c;
        this.context=context;
        this.jid=jid;
        this.t= Typeface.createFromAsset(context.getAssets(), "fonts/hn.otf");
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_single, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element


        holder.Message.setTypeface(t);
       // holder.TimeStamp.setTypeface(t);
        if(!message.get(position).substring(0,message.get(position).length()-1).equals("null")) {

            holder.ppic.setVisibility(View.GONE);
            holder.Message.setText(message.get(position).substring(0, message.get(position).length() - 1));
        }

        else if(Utilities.media.containsKey(jid+timestamp.get(position)))
        {

            switch (Utilities.media.get(jid+timestamp.get(position))[1])
            {
                case "1":
                    holder.Message.setText("IMG-"+Utilities.media.get(jid+timestamp.get(position))[0]+".jpeg");
                    Bitmap b = BitmapFactory.decodeFile("/storage/emulated/0/WhatsApp/Media/WhatsApp Images/IMG-"+Utilities.media.get(jid+timestamp.get(position))[0]+ ".jpg");
                    holder.ppic.setImageBitmap(b);
                    holder.ppic.setVisibility(View.VISIBLE);
                    break;
                case "2":
                    holder.Message.setText("AUD-"+Utilities.media.get(jid+timestamp.get(position))[0]+".aac");
                    break;
                case "3":
                    holder.Message.setText("VID-"+Utilities.media.get(jid+timestamp.get(position))[0]+".mp4");
                    break;
            }
        }
        else{
            holder.ppic.setVisibility(View.GONE);
        }


        //holder.TimeStamp.setText(timestamp.get(position));

        Date d=ChatListAdapter.EpochConvert(timestamp.get(position));
        holder.TimeStamp.setText(String.format("%02d",d.getHours())+":"+String.format("%02d",d.getMinutes())+"  "+d.getDate()+"/"+String.valueOf(d.getMonth()+1)+"/"+d.getYear());

        if(message.get(position).endsWith("0")) {
            holder.Contact.setText(Utilities.contacts.get(contact.get(position)).name);
            holder.box.setGravity(Gravity.LEFT);
        }
        else {
            holder.Contact.setText("me");
            holder.box.setGravity(Gravity.RIGHT);
        }



    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return message.size();
    }
}
