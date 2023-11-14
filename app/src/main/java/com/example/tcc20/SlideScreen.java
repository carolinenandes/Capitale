package com.example.tcc20;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SlideScreen extends AppCompatActivity {

    ViewPager mSlideViewPager;
    LinearLayout mDotLayout;
    Button btnprox,btnvoltar;
    ImageView btnpular;

    TextView[] dots;
    ViewPagerAdapter viewPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_screen);

        btnprox = findViewById(R.id.btnproximo);
        btnvoltar = findViewById(R.id.btnvoltar);
        btnpular = findViewById(R.id.btnpular);

        btnvoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (getitem(0) > 0 ){
                    mSlideViewPager.setCurrentItem(getitem(-1),true);
                }
            }
        });

        btnprox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getitem(1) > 0 ){
                    mSlideViewPager.setCurrentItem(getitem(1),true);
                } else {
//                    ADICIONAR O INTENT AQUI MEN
                    Intent i = new Intent (SlideScreen.this,MainActivity.class);
                    startActivity(i);
                    finish();
                }

            }
        });

        btnpular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                Intent i = new Intent (SlideScreen.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        mSlideViewPager = (ViewPager) findViewById(R.id.view_pagerr);
        mDotLayout = (LinearLayout) findViewById(R.id.indicador_layout);


        viewPagerAdapter = new ViewPagerAdapter(this);

        mSlideViewPager.setAdapter(viewPagerAdapter);

        setupIndicator(0);
        mSlideViewPager.addOnPageChangeListener(viewListener);
    }

    public void setupIndicator(int position) {

        dots = new TextView[3];
        mDotLayout.removeAllViews();

        for(int i = 0 ; i < dots.length ; i++) {

            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(35);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                dots[i].setTextColor(getResources().getColor(R.color.inactive,getApplicationContext().getTheme()));
            }
            mDotLayout.addView(dots[i]);
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            dots[position].setTextColor(getResources().getColor(R.color.active,getApplicationContext().getTheme()));
        }
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
       setupIndicator(position);

       if (position > 0) {
           btnvoltar.setVisibility(View.VISIBLE);
       } else {
           btnvoltar.setVisibility(View.INVISIBLE);
       }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private  int getitem(int i) {
        return mSlideViewPager.getCurrentItem() + i;

    }
}