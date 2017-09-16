package com.copychrist.app.prayer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.copychrist.app.prayer.R;
import com.copychrist.app.prayer.model.BiblePassage;
import com.copychrist.app.prayer.util.FlipAnimator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jim on 8/20/17.
 * Inspired by Ravi Tamada on 21/02/17.
 * https://www.androidhive.info/2017/02/android-creating-gmail-like-inbox-using-recyclerview/
 */

public class BibleVerseRecyclerViewAdapter extends RecyclerView.Adapter<BibleVerseRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private List<BiblePassage> biblePassages;
    private BiblePassageAdapterListener biblePassageAdapterListener;
    private SparseBooleanArray selectedItems;

    // array used to perform multiple animation at once
    private SparseBooleanArray animationItemsIndex;
    private boolean reverseAllAnimations = false;

    // index is used to animate only the selected row
    // dirty fix, find a better solution
    private static int currentSelectedIndex = -1;

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.layout_item_container) LinearLayout itemContainer;
        @BindView(R.id.text_bible_passage) TextView biblePassage;
        @BindView(R.id.text_verse_text) TextView verseText;
        @BindView(R.id.icon_container) RelativeLayout iconContainer;
        @BindView(R.id.icon_back) RelativeLayout iconBack;
        @BindView(R.id.icon_front) RelativeLayout iconFront;

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
        void onRowClicked(int position);
        void onRowLongClicked(int position);
    }

    public BibleVerseRecyclerViewAdapter(Context context, BiblePassageAdapterListener listener) {
        super();
        this.context = context;
        this.biblePassageAdapterListener = listener;
        selectedItems = new SparseBooleanArray();
        animationItemsIndex = new SparseBooleanArray();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                                                R.layout.item_bible_passage,
                                                parent,
                                                false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // get the verse
        final BiblePassage biblePassage = biblePassages.get(position);
        if(biblePassage != null) {
            holder.bind(biblePassage);
        }

        if(selectedItems != null) {
            holder.itemView.setActivated(selectedItems.get(position, false));
        }

        // handle icon animation
        applyIconAnimation(holder, position);

        // apply click events
        applyClickEvents(holder, position);
    }

    @Override
    public int getItemCount() {
        return biblePassages != null ? biblePassages.size() : 0;
    }

    public void setAdapterData(final List<BiblePassage> verses) {
        this.biblePassages = verses;
        clearSelections();
    }

    private void applyClickEvents(ViewHolder holder, final int position) {

        holder.iconContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                biblePassageAdapterListener.onRowClicked(position);
            }
        });

        holder.itemContainer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                biblePassageAdapterListener.onRowLongClicked(position);
                view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                return true;
            }
        });
    }


    // As the views will be reused, sometimes the icon appears as
    // flipped because older view is reused. Reset the Y-axis to 0
    private void resetIconYAxis(View view) {
        if (view.getRotationY() != 0) {
            view.setRotationY(0);
        }
    }

    private void resetCurrentIndex() {
        currentSelectedIndex = -1;
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

    public List<Integer> getSelectedItems() {
        List<Integer> items = new ArrayList<>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++) {
            items.add(selectedItems.keyAt(i));
        }

        return items;
    }

    private void applyIconAnimation(ViewHolder holder, int position) {
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

}
