package com.music.chords.adapter;

/**
 * Created by Pradeep Jadhav on 8/11/2017.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.music.chords.R;
import com.music.chords.helper.CircleTransform;
import com.music.chords.helper.FlipAnimator;
import com.music.chords.interfaces.SongAdapterListener;
import com.music.chords.objects.SongObject;

import java.util.ArrayList;
import java.util.List;

public class SongItemAdapter extends RecyclerView.Adapter<SongItemAdapter.MyViewHolder> {

    private Context context;
    private SongAdapterListener listener;
    private SparseBooleanArray selectedItems;

    // array used to perform multiple animation at once
    private SparseBooleanArray animationItemsIndex;
    private boolean reverseAllAnimations = false;

    // index is used to animate only the selected row
    // dirty fix, find a better solution
    private static int currentSelectedIndex = -1;

    private ArrayList<SongObject> listSongs;

    public SongItemAdapter(Context context, ArrayList<SongObject> listSongs) {
        this.context = context;
        this.listSongs = listSongs;
        selectedItems = new SparseBooleanArray();
        animationItemsIndex = new SparseBooleanArray();
    }

    public void setSongAdapterListener(SongAdapterListener itemClickListener) {
        this.listener = itemClickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        public LinearLayout messageContainer;

        public TextView from;
        public TextView subject;
        public TextView message;
        public TextView iconText;


        public ImageView imgProfile;
        public ImageView iconImp;

        public RelativeLayout iconContainer;
        public RelativeLayout iconBack;
        public RelativeLayout iconFront;

        public MyViewHolder(View view) {
            super(view);
            from = (TextView) view.findViewById(R.id.from);
            subject = (TextView) view.findViewById(R.id.txt_primary);
            message = (TextView) view.findViewById(R.id.txt_secondary);
            iconText = (TextView) view.findViewById(R.id.icon_text);
//            timestamp = (TextView) view.findViewById(R.id.timestamp);
            iconBack = (RelativeLayout) view.findViewById(R.id.icon_back);
            iconFront = (RelativeLayout) view.findViewById(R.id.icon_front);
            iconImp = (ImageView) view.findViewById(R.id.icon_star);
            imgProfile = (ImageView) view.findViewById(R.id.icon_profile);
            messageContainer = (LinearLayout) view.findViewById(R.id.message_container);
            iconContainer = (RelativeLayout) view.findViewById(R.id.icon_container);

            itemView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View view) {
            listener.onRowLongClicked(getAdapterPosition());
            view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
            return true;
        }

//        @Override
//        public void onClick(View view) {
//            if (clickListener != null) {
//                clickListener.onClick(view, getAdapterPosition());
//            }
//        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_song_recycle_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        SongObject songData = listSongs.get(position);
//        holder.tvTitle.setText(data.getSongTitle());
//        holder.tvSubtitle.setText(data.getSongSubtitle());

        // displaying text view data
        holder.from.setText(songData.getSongTitle());
        holder.subject.setText(songData.getSongSubtitle());
        holder.message.setText(songData.getSongArtist());
//        holder.timestamp.setText(message.getTimestamp());

        // displaying the first letter of From in icon text
        holder.iconText.setText(songData.getSongTitle().substring(0, 1));

        // change the row state to activated
        holder.itemView.setActivated(selectedItems.get(position, false));

//        // change the font style depending on message read status
//        applyReadStatus(holder, songData);

        // handle message star
        applyBookmarkIcon(holder, songData);

        // handle icon animation
        applyIconAnimation(holder, position);

        // display profile image
        applyProfilePicture(holder, songData);

        // apply click events
        applyClickEvents(holder, position);
    }

    private void applyBookmarkIcon(MyViewHolder holder, SongObject message) {
        if (message.getIsBookmark()) {
            holder.iconImp.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star_selected));
//            holder.iconImp.setColorFilter(ContextCompat.getColor(context, R.color.icon_tint_selected));
        } else {
            holder.iconImp.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star_unselected));
//            holder.iconImp.setColorFilter(ContextCompat.getColor(context, R.color.icon_tint_normal));
        }
    }

    private void applyClickEvents(MyViewHolder holder, final int position) {
//        holder.iconContainer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                listener.onIconClicked(position);
//            }
//        });

        holder.iconImp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onIconImportantClicked(position);
            }
        });

        holder.messageContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onMessageRowClicked(position);
            }
        });

        holder.messageContainer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listener.onRowLongClicked(position);
                view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                return true;
            }
        });
    }

    //    private void applyReadStatus(MyViewHolder holder, Song message) {
//        if (message.isRead()) {
//            holder.from.setTypeface(null, Typeface.NORMAL);
//            holder.subject.setTypeface(null, Typeface.NORMAL);
//            holder.from.setTextColor(ContextCompat.getColor(context, R.color.main_dark_text));
//            holder.subject.setTextColor(ContextCompat.getColor(context, R.color.subtext));
//        } else {
//            holder.from.setTypeface(null, Typeface.BOLD);
//            holder.subject.setTypeface(null, Typeface.BOLD);
//            holder.from.setTextColor(ContextCompat.getColor(context, R.color.main_dark_text));
//            holder.subject.setTextColor(ContextCompat.getColor(context, R.color.main_text));
//        }
//    }
//
    private void applyProfilePicture(MyViewHolder holder, SongObject message) {
        holder.imgProfile.setImageResource(R.drawable.bg_circle);
        holder.imgProfile.setColorFilter(message.getSongIconColor());
        holder.iconText.setVisibility(View.VISIBLE);

//        if (!TextUtils.isEmpty(message.getSongYouTubeURL())) {
//            Glide.with(context).load(message.getSongYouTubeURL())
//                    .thumbnail(0.5f)
//                    .crossFade()
//                    .transform(new CircleTransform(context))
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .into(holder.imgProfile);
//            holder.imgProfile.setColorFilter(null);
//            holder.iconText.setVisibility(View.GONE);
//        } else {
//            holder.imgProfile.setImageResource(R.drawable.bg_circle);
//            holder.imgProfile.setColorFilter(getRandomMaterialColor("400"));
//            holder.iconText.setVisibility(View.VISIBLE);
//        }
    }

    private int getRandomMaterialColor(String typeColor) {
        int returnColor = Color.GRAY;
        int arrayId = context.getResources().getIdentifier("mdcolor_" + typeColor, "array", context.getPackageName());

        if (arrayId != 0) {
            TypedArray colors = context.getResources().obtainTypedArray(arrayId);
            int index = (int) (Math.random() * colors.length());
            returnColor = colors.getColor(index, Color.GRAY);
            colors.recycle();
        }
        return returnColor;
    }


    private void applyIconAnimation(MyViewHolder holder, int position) {
        if (selectedItems.get(position, false)) {
            holder.iconFront.setVisibility(View.GONE);
            resetIconYAxis(holder.iconBack);
            holder.iconBack.setVisibility(View.VISIBLE);
            holder.iconBack.setAlpha(1);
            if (currentSelectedIndex == position) {
                FlipAnimator.flipView(context, holder.iconBack, holder.iconFront, true);
                resetCurrentIndex();
            }
        } else {
            holder.iconBack.setVisibility(View.GONE);
            resetIconYAxis(holder.iconFront);
            holder.iconFront.setVisibility(View.VISIBLE);
            holder.iconFront.setAlpha(1);
            if ((reverseAllAnimations && animationItemsIndex.get(position, false)) || currentSelectedIndex == position) {
                FlipAnimator.flipView(context, holder.iconBack, holder.iconFront, false);
                resetCurrentIndex();
            }
        }
    }

    // As the views will be reused, sometimes the icon appears as
    // flipped because older view is reused. Reset the Y-axis to 0
    private void resetIconYAxis(View view) {
        if (view.getRotationY() != 0) {
            view.setRotationY(0);
        }
    }

    public void resetAnimationIndex() {
        reverseAllAnimations = false;
        animationItemsIndex.clear();
    }

    public void toggleSelection(int pos) {
        currentSelectedIndex = pos;
        if (selectedItems.get(pos, false)) {
            selectedItems.delete(pos);
            animationItemsIndex.delete(pos);
        } else {
            selectedItems.put(pos, true);
            animationItemsIndex.put(pos, true);
        }
        notifyItemChanged(pos);
    }

    public void clearSelections() {
        reverseAllAnimations = true;
        selectedItems.clear();
        notifyDataSetChanged();
    }

    public int getSelectedItemCount() {
        return selectedItems.size();
    }

    public List<Integer> getSelectedItems() {
        List<Integer> items =
                new ArrayList<>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++) {
            items.add(selectedItems.keyAt(i));
        }
        return items;
    }

//    public boolean checkAtLeastOneUnsavedItem() {
////        List<SongObject> items = new ArrayList<>(selectedItems.size());
//        for (int i = 0; i < selectedItems.size(); i++) {
//            int selectedIndex = selectedItems.keyAt(i);
////            items.add(listSongs.get(selectedIndex));
//            boolean isSaved = listSongs.get(selectedIndex).getIsBookmark();
//            if (!isSaved) {
//                return true;
//            }
//        }
//        return false;
//    }

    public void removeData(int position) {
        listSongs.remove(position);
        resetCurrentIndex();
    }

    private void resetCurrentIndex() {
        currentSelectedIndex = -1;
    }

    public long getItemId(int position) {
        return listSongs.get(position).getSongId();
    }

    @Override
    public int getItemCount() {
        return listSongs.size();
    }
}