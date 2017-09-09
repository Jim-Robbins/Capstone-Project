package com.copychrist.app.prayer.ui;

/**
 * Created by jim on 9/7/17.
 */

public interface BaseService<M, P> {
    void getValues(P presenter);
    void saveValue(final M model);
    void deleteValue();
    void destroy();
}
