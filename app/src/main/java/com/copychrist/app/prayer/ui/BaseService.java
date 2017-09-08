package com.copychrist.app.prayer.ui;

/**
 * Created by jim on 9/7/17.
 */

public interface BaseService<M, V> {
    void getValue(final V view, final M model);
    void saveValue(final M model);
    void deleteValue(String id);
    void destroy();
}
