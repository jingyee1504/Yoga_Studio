package com.example.yogastudio;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.media.Image;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yogastudio.Database.YogaDB;
import com.example.yogastudio.Utils.Common;

import org.w3c.dom.Text;

public class ViewExercise extends AppCompatActivity {
    int image_id;
    String name;

    TextView timer, title, btnStart;
    ImageView detail_image;
    View divpage, bgprogressExercise;
    Animation bttone, bttwo, btthree;

    boolean isRunning = false;

    YogaDB yogaDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_exercise);

        //load animation
        bttone = AnimationUtils.loadAnimation(this, R.anim.bttone);
        bttwo = AnimationUtils.loadAnimation(this, R.anim.bttwo);
        btthree = AnimationUtils.loadAnimation(this, R.anim.btthree);

        //import font
        Typeface Vidaloka = Typeface.createFromAsset(getAssets(), "fonts/vidaloka.ttf");
        Typeface Comfortaa = Typeface.createFromAsset(getAssets(), "fonts/comfortaa.ttf");

        yogaDB = new YogaDB(this);

        timer = (TextView)findViewById(R.id.timer);
        title = (TextView)findViewById(R.id.title1);
        detail_image = (ImageView)findViewById(R.id.detail_image);
        divpage = (View) findViewById(R.id.divpage1);
        bgprogressExercise = (View) findViewById(R.id.bgprogressExercise);

        btnStart = (TextView) findViewById(R.id.btnStart);

        //customize font
        title.setTypeface(Vidaloka);
        btnStart.setTypeface(Comfortaa);

        //assign the animations
        title.startAnimation(bttone);
        divpage.startAnimation(bttone);
        detail_image.startAnimation(bttone);
        bgprogressExercise.startAnimation(bttwo);
        btnStart.startAnimation(bttwo);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isRunning){
                    btnStart.setText("DONE");

                    int timeLimit = 0;

                    if(yogaDB.getSettingMode() == 0)
                        timeLimit = Common.TIME_LIMIT_EASY;
                    else if(yogaDB.getSettingMode() == 1)
                        timeLimit = Common.TIME_LIMIT_MEDIUM;
                    else if(yogaDB.getSettingMode() == 2)
                        timeLimit = Common.TIME_LIMIT_HARD;

                    new CountDownTimer(timeLimit,1000){

                        @Override
                        public void onTick(long l) {
                            timer.setText("" + l/1000);
                        }

                        @Override
                        public void onFinish() {
                            Toast.makeText(ViewExercise.this, "Finish!!!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }.start();
                }
                else {
                    Toast.makeText(ViewExercise.this, "Finish!!!", Toast.LENGTH_SHORT).show();
                    finish();
                }

                isRunning = !isRunning;
            }
        });

        timer.setText("");

        if(getIntent() != null){
            image_id = getIntent().getIntExtra("image_id",-1);
            name = getIntent().getStringExtra("name");

            detail_image.setImageResource(image_id);
            title.setText(name);
        }
    }
}
