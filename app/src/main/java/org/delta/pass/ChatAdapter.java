package org.delta.pass;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.File;
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
        public FrameLayout videoFrame;
        public ViewHolder(View v) {
            super(v);
            Message = (TextView)v.findViewById(R.id.Message);
            TimeStamp = (TextView)v.findViewById(R.id.Time);
            box = (LinearLayout)v.findViewById(R.id.singlelistlayout);
            Contact =(TextView)v.findViewById(R.id.Contact);
            ppic =(ImageView)v.findViewById(R.id.picmessage);
            videoFrame = (FrameLayout)v.findViewById(R.id.videoFrameLayout);
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


        holder.ppic.setVisibility(View.GONE);
        holder.Message.setText("");
        holder.Message.setTypeface(t);
       // holder.TimeStamp.setTypeface(t);
        if(!message.get(position).substring(0,message.get(position).length()-1).equals("null")) {

            holder.ppic.setVisibility(View.GONE);
            holder.videoFrame.setVisibility(View.GONE);
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
                    holder.videoFrame.setVisibility(View.GONE);
                    holder.ppic.setVisibility(View.VISIBLE);
                    break;
                case "2":
                    holder.Message.setText("AUD-"+Utilities.media.get(jid+timestamp.get(position))[0]+".aac");
                    holder.videoFrame.setVisibility(View.GONE);
                    holder.ppic.setVisibility(View.GONE);
                    break;
                case "3":
                    holder.Message.setText("VID-"+Utilities.media.get(jid+timestamp.get(position))[0]+".mp4");
                    File file = new File("/storage/emulated/0/WhatsApp/Media/WhatsApp Video/VID-"+Utilities.media.get(jid+timestamp.get(position))[0]+".mp4");
                    if(file.exists()) {
                        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                        holder.videoFrame.setVisibility(View.VISIBLE);
                        final VideoView videoView = (VideoView) inflater.inflate(R.layout.video_view, null);
                        MediaController mediaController = new MediaController(context);
                        mediaController.setAnchorView(videoView);
                        videoView.setMediaController(mediaController);
                        videoView.setVideoPath("/storage/emulated/0/WhatsApp/Media/WhatsApp Video/VID-" + Utilities.media.get(jid + timestamp.get(position))[0] + ".mp4");

                        /*Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail("/storage/emulated/0/WhatsApp/Media/WhatsApp Video/VID-" + Utilities.media.get(jid + timestamp.get(position))[0] + ".mp4",
                                MediaStore.Images.Thumbnails.MINI_KIND);
                        BitmapDrawable bitmapDrawable = new BitmapDrawable(thumbnail);
                        videoView.setBackgroundDrawable(bitmapDrawable);
*/
                        videoView.seekTo(1000);
                        holder.videoFrame.addView(videoView);
                        Log.e("Videoset", "vide");
                    }
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
