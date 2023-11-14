package com.example.tcc20;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.tcc20.R;

public class ViewPagerAdapter  extends PagerAdapter {

//Aqui é aonde vai mudar as fotos, descrições e títulos

  Context context;

  int images[] = {
        R.drawable.moneyversebusinessanalytics,
        R.drawable.carteirapog,
        R.drawable.grafics

  };

  int headings [] = {
    R.string.heading_one,
          R.string.heading_two,
          R.string.heading_three
  };

  int descricao [] = {

          R.string.desc_one,
          R.string.desc_two,
          R.string.desc_three
  };

  public ViewPagerAdapter(Context context) {

    this.context = context;
  }

    @Override
    public int getCount() {
        return headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (LinearLayout) object;
    }

  @NonNull
  @Override
  public Object instantiateItem(@NonNull ViewGroup container, int position) {

    LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    View view = layoutInflater.inflate(R.layout.slider_layout,container,false);

    ImageView slideimage = (ImageView) view.findViewById(R.id.image_title);
    TextView slideheading = (TextView) view.findViewById(R.id.title_ti);
    TextView slideDescription = (TextView)  view.findViewById(R.id.descricao);

    slideimage.setImageResource(images[position]);
    slideheading.setText(headings[position]);
    slideDescription.setText(descricao[position]);

    container.addView((view));
    return view;
  }

  @Override
  public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

    container.removeView((LinearLayout)object);
  }
}