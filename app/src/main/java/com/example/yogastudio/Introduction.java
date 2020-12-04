package com.example.yogastudio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class Introduction extends AppCompatActivity {

    TextView titlePage, subtitlePage, btnExercise;
    ImageView imgPage;
    Animation animimgpage, bttone, bttwo, btthree,ltr;
    View bgprogress, bgprogresstop;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);

        //load animation
        animimgpage = AnimationUtils.loadAnimation(this,R.anim.animimgpage);
        bttone = AnimationUtils.loadAnimation(this, R.anim.bttone);
        bttwo = AnimationUtils.loadAnimation(this, R.anim.bttwo);
        btthree = AnimationUtils.loadAnimation(this, R.anim.btthree);
        ltr = AnimationUtils.loadAnimation(this, R.anim.lefttoright);

        //import font
        Typeface Vidaloka = Typeface.createFromAsset(getAssets(), "fonts/vidaloka.ttf");
        Typeface Barlow = Typeface.createFromAsset(getAssets(), "fonts/barlow.ttf");
        Typeface Comfortaa = Typeface.createFromAsset(getAssets(), "fonts/comfortaa.ttf");

        titlePage = (TextView) findViewById(R.id.titlePage);
        subtitlePage = (TextView) findViewById(R.id.subtitlepage);
        btnExercise = (TextView) findViewById(R.id.btnExercise);
        imgPage = (ImageView) findViewById(R.id.imgpage);
        bgprogress = (View) findViewById(R.id.bgprogress);
        bgprogresstop = (View) findViewById(R.id.bgprogresstop);

        //customize font
        titlePage.setTypeface(Vidaloka);
        subtitlePage.setTypeface(Barlow);
        btnExercise.setTypeface(Comfortaa);

        //export animate
        imgPage.startAnimation(animimgpage);
        titlePage.startAnimation(bttone);
        subtitlePage.startAnimation(bttone);

        btnExercise.startAnimation(btthree);
        bgprogress.startAnimation(bttwo);
        bgprogresstop.startAnimation(ltr);

        btnExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Introduction.this,ListExercises.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
    }
}
