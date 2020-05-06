package com.example.android.pets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;
import androidx.annotation.StyleableRes;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android.pets.R;

public class EmptyViewComponent extends RelativeLayout {

    @StyleableRes
    int index1 = 1;
    @StyleableRes
    int index2 = 2;


    ImageView shelterImageView;
    TextView lonelyTextView;
    TextView text2TextView;

    public EmptyViewComponent(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }
    private void init(Context context, AttributeSet attrs) {
        inflate(context, R.layout.empty_view_component, this);

        int[] sets = {R.attr.Image, R.attr.lonelyTextView,R.attr.text2};
        TypedArray typedArray = context.obtainStyledAttributes(attrs, sets);
        Drawable Image = typedArray.getDrawable(R.styleable.EmptyViewComponent_Image);
        CharSequence lonelyText = typedArray.getText(index1);
        CharSequence text2 = typedArray.getText(index2);
        typedArray.recycle();

        initComponents();

        setImage(Image);
        setTheLonelyTextView(lonelyText);
        setText2(text2);
    }

    private void initComponents() {
        shelterImageView = findViewById(R.id.empty_shelter_image);
        lonelyTextView = findViewById(R.id.empty_title_text);
        text2TextView = findViewById(R.id.text2);
    }

    public void setImage(Drawable value) {
        shelterImageView.setImageDrawable(value);
    }

    public void setTheLonelyTextView(CharSequence value) {
        lonelyTextView.setText(value);
    }

    public void setText2(CharSequence value) {
        text2TextView.setText(value);
    }

}

