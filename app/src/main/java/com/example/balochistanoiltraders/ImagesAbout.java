package com.example.balochistanoiltraders;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

public class ImagesAbout extends PagerAdapter {
    Context mContext;

    private int[] urls;

    ImagesAbout(Context c, int[] list){
        this.mContext = c;
        this.urls = list;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView  = new ImageView(mContext);
//        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        imageView.setImageResource(urls[position]);

        Picasso.get().load(urls[position]).fit().centerCrop().into(imageView);
        container.addView(imageView, 0);
        return imageView;
    }

    @Override
    public int getCount() {
        return urls.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }
}
