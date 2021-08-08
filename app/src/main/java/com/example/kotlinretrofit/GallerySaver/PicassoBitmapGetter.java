package com.example.kotlinretrofit.GallerySaver;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import android.content.Context;
import com.squareup.picasso.Target;

public class PicassoBitmapGetter {
   private Bitmap bitmapR;
   private Context contextL;

    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
               bitmapR = bitmap;
        }

        @Override
        public void onBitmapFailed(Exception e, Drawable errorDrawable) {
            Toast.makeText(contextL, "", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
        }
    };

    public Bitmap getBitmapR(String url) {
        Picasso.get().load(url).into(target);
        if (bitmapR != null){
            return bitmapR;
        } else {
            return null;
        }
    }

    private void getContext (Context context){
        contextL = context;
    }
}
