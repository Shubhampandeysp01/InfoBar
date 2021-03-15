package com.example.newsapp;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

public class MyPageTransformer implements ViewPager2.PageTransformer {
    @Override
    public void transformPage(@NonNull View page, float position) {
        int pageHeight = page.getHeight();

        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            page.setAlpha(0);

        } else if (position <= 0) { // [-1,0]
            // Use the default slide transition when moving to the left page
            page.setAlpha(1);
            page.setTranslationY(page.getHeight()* -position);
            page.setTranslationY(0);
            page.setTranslationZ(0);
            page.setScaleX(1);
            page.setScaleY(1);

        } else if (position <= 1) {
            page.setAlpha(1 - position);
            page.setTranslationY(pageHeight * -position);
            page.setTranslationZ(-1f);
            float MIN_SCALE = 0.90f;
            float scaleFactor = MIN_SCALE
                    + (1 - MIN_SCALE) * (1 - Math.abs(position));
            page.setScaleX(scaleFactor);
            page.setScaleY(scaleFactor);


        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            page.setAlpha(0);
        }

    }
}
