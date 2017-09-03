package com.copychrist.app.prayer.data.local;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.AndroidTestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Created by jim on 9/1/17.
 */
@RunWith(AndroidJUnit4.class)
public class DatabaseHelperTest {

    private Context testContext;

    @Before
    public void setUp() throws Exception {
        Context testContext = InstrumentationRegistry.getContext();
        deleteTheDatabase();
    }

    void deleteTheDatabase() {

    }

    @Test
    public void onCreate() throws Exception {


    }

    @Test
    public void onOpen() throws Exception {

    }

    @Test
    public void onUpgrade() throws Exception {

    }

    @Test
    public void onDowngrade() throws Exception {

    }

}