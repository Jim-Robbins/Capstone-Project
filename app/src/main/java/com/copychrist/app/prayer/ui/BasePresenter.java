package com.copychrist.app.prayer.ui;

/**
 * Created by jim on 8/14/17.
 */

public interface BasePresenter<T> {
    void setView(T view);
    void resetView(T view);
    void saveState();
    void clearView();
}
