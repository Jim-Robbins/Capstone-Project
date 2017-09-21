package com.copychrist.app.prayer.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.copychrist.app.prayer.R;
import com.copychrist.app.prayer.model.BiblePassage;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jim on 8/20/17.
 * Inspired by Ravi Tamada on 21/02/17.
 * https://www.androidhive.info/2017/02/android-creating-gmail-like-inbox-using-recyclerview/
 */

public class BiblePassageSwipableAdapter extends RecyclerView.Adapter<BiblePassageSwipableAdapter.ViewHolder> {
    private List<BiblePassage> biblePassages;
    private BiblePassageAdapterListener biblePassageAdapterListener;
    private ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.layout_swipeable_item_container) protected SwipeRevealLayout swipeLayout;
        @BindView(R.id.swipe_btn_remove) protected RelativeLayout btnRemove;
        @BindView(R.id.txt_bible_passage) protected TextView biblePassage;
        @BindView(R.id.txt_verse_text) protected TextView verseText;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final BiblePassage biblePassage) {
            this.biblePassage.setText(biblePassage.getPassageReference());
            verseText.setText(biblePassage.getText());
        }
    }

    public interface BiblePassageAdapterListener {
        void onRemoveClicked(int position);
        void onRowLongClicked(int position);
    }

    public BiblePassageSwipableAdapter(BiblePassageAdapterListener listener) {
        super();
        this.biblePassageAdapterListener = listener;
        viewBinderHelper.setOpenOnlyOne(true);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                                                R.layout.item_bible_passage_swipeable,
                                                parent,
                                                false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // get the verse
        final BiblePassage biblePassage = biblePassages.get(position);
        viewBinderHelper.bind(holder.swipeLayout, biblePassage.getKey());
        holder.bind(biblePassage);

        // apply click events
        applyClickEvents(holder, position);
    }

    @Override
    public int getItemCount() {
        return biblePassages != null ? biblePassages.size() : 0;
    }

    public void setAdapterData(final List<BiblePassage> verses) {
        this.biblePassages = verses;
        notifyDataSetChanged();
    }

    private void applyClickEvents(ViewHolder holder, final int position) {

        holder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BiblePassage biblePassage = biblePassages.get(position);
                viewBinderHelper.closeLayout(biblePassage.getKey());
                biblePassageAdapterListener.onRemoveClicked(position);
            }
        });

        holder.swipeLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                biblePassageAdapterListener.onRowLongClicked(position);
                view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                return true;
            }
        });
    }

}
