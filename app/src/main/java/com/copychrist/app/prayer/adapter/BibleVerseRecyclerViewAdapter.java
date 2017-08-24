package com.copychrist.app.prayer.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.copychrist.app.prayer.R;
import com.copychrist.app.prayer.model.BibleVerse;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmList;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by jim on 8/20/17.
 */

public class BibleVerseRecyclerViewAdapter extends RealmRecyclerViewAdapter<BibleVerse, BibleVerseRecyclerViewAdapter.VerseViewHolder> {

    private OnBibleVerseClickListener onBibleVerseClickListener;

    public BibleVerseRecyclerViewAdapter(RealmList<BibleVerse> bibleVerses) {
        super(bibleVerses, true);
    }

    @Override
    public VerseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VerseViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_bible_passage,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(VerseViewHolder holder, int position) {
        // get the verse
        final BibleVerse bibleVerse = getItem(position);
        if(bibleVerse != null) {
            holder.bind(bibleVerse);
        }
    }

    public void setBibleVerseClickListener(final OnBibleVerseClickListener onBibleVerseClickListener) {
        this.onBibleVerseClickListener = onBibleVerseClickListener;
    }

    public class VerseViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.layout_item_container)
        LinearLayout layoutItem;
        @BindView(R.id.text_bible_passage)
        TextView biblePassage;
        @BindView(R.id.text_verse_text)
        TextView verseText;

        public VerseViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final BibleVerse bibleVerse) {
            biblePassage.setText(bibleVerse.getPassage());
            verseText.setText(bibleVerse.getText());
            layoutItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onBibleVerseClickListener != null) {
                        onBibleVerseClickListener.onBibleVerseClick(bibleVerse.getPassage());
                    }
                }
            });
        }
    }

    public interface OnBibleVerseClickListener {
        void onBibleVerseClick(String biblePassage);
    }

}
