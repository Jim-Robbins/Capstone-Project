package com.copychrist.app.prayer.ui;

/**
 * Created by jim on 9/7/17.
 */

public interface BaseService<M, V> {
    void getValues(final V view);
    void saveValue(final M model);
    void deleteValue(final V view, final String id);
    void destroy();
}
