package com.copychrist.app.prayer.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.copychrist.app.prayer.R;
import com.copychrist.app.prayer.model.BiblePassage;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jim on 8/20/17.
 *
 */

public class BibleVerseRecyclerViewAdapter extends RecyclerView.Adapter<BibleVerseRecyclerViewAdapter.VerseViewHolder> {

    private List<BiblePassage> verses;
    private OnBibleVerseClickListener onBibleVerseClickListener;

    public BibleVerseRecyclerViewAdapter() {}

    @Override
    public VerseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                                                R.layout.item_bible_passage,
                                                parent,
                                                false);

        return new VerseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VerseViewHolder holder, int position) {
        // get the verse
        final BiblePassage biblePassage = verses.get(position);
        if(biblePassage != null) {
            holder.bind(biblePassage);
        }
    }

    @Override
    public int getItemCount() {
        return verses != null ? verses.size() : 0;
    }

    public void setBibleVerseClickListener(final OnBibleVerseClickListener onBibleVerseClickListener) {
        this.onBibleVerseClickListener = onBibleVerseClickListener;
    }

    public void setBibleVerses(final List<BiblePassage> verses) {
        this.verses = verses;
//        this.verses.addChangeListener(this);
        notifyDataSetChanged();
    }

    class VerseViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.layout_item_container)
        LinearLayout layoutItem;
        @BindView(R.id.text_bible_passage)
        TextView biblePassage;
        @BindView(R.id.text_verse_text)
        TextView verseText;

        VerseViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final BiblePassage biblePassage) {
            this.biblePassage.setText(biblePassage.getPassageReference());
            verseText.setText(biblePassage.getText());
            layoutItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onBibleVerseClickListener != null) {
                        onBibleVerseClickListener.onBibleVerseClick(biblePassage.getPassageReference());
                    }
                }
            });
        }
    }

    public interface OnBibleVerseClickListener {
        void onBibleVerseClick(String biblePassage);
    }

}
