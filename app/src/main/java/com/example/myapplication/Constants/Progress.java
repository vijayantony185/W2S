package com.example.myapplication.Constants;

import android.app.Activity;
import android.app.Dialog;
import android.widget.ProgressBar;

import com.example.myapplication.R;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Circle;
import com.github.ybq.android.spinkit.style.DoubleBounce;


public class Progress {
    public Dialog dialog;
    public ProgressBar progress;

    public Progress(Activity activity) {
        dialog = new Dialog(activity);

        dialog.setContentView(R.layout.progress_dialog);

        progress = (ProgressBar)dialog.findViewById(R.id.spin_kit);
        Sprite doubleBounce = new DoubleBounce();
        progress.setIndeterminateDrawable(doubleBounce);
        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        dialog.getWindow().setDimAmount(0.0f);
        Circle fadingCircle = new Circle();
        progress.setIndeterminateDrawable(fadingCircle);
        dialog.setCancelable(false);
    }

    public void show() {

        try {
            dialog.show();
        } catch (Exception e) {
            //do nothing
        }
    }

    public void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }
}
