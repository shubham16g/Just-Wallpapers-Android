package com.shubhamgupta16.wallpaperapp.data;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class ImagePagerAdapter extends PagerAdapter {

    Activity activity;
    int imageArray[];
    String[] stringArray;

    public ImagePagerAdapter(Activity act, int[] imgArra, String[] stringArra) {
        imageArray = imgArra;
        activity = act;
        stringArray = stringArra;
    }

    public int getCount() {
        return imageArray.length;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        return super.instantiateItem(container, position);

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.destroyItem(container, position, object);
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == ((View) arg1);
    }



}