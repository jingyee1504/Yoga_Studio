package com.example.yogastudio;

import androidx.appcompat.app.AppCompatActivity;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.yogastudio.Database.YogaDB;
import com.example.yogastudio.Model.Exercise;
import com.example.yogastudio.Utils.Common;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Daily_Training extends AppCompatActivity {

    ImageView ex_image;
    TextView txtGetReady, txtCountdown, txtTimer, ex_name, btnStart;
    ProgressBar progressBar;
    LinearLayout layoutGetReady;
    View bgprogressExercise;
    Animation bttone, bttwo, btthree;

    int ex_id = 0, limit_time = 0;

    List<Exercise> list = new ArrayList<>();

    YogaDB yogaDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily__training);

        initData();

        //load animation
        bttone = AnimationUtils.loadAnimation(this, R.anim.bttone);
        bttwo = AnimationUtils.loadAnimation(this, R.anim.bttwo);
        btthree = AnimationUtils.loadAnimation(this, R.anim.btthree);

        //import font
        Typeface Vidaloka = Typeface.createFromAsset(getAssets(), "fonts/vidaloka.ttf");
        Typeface Comfortaa = Typeface.createFromAsset(getAssets(), "fonts/comfortaa.ttf");

        yogaDB = new YogaDB(this);

        btnStart = (TextView) findViewById(R.id.btnStart);
        ex_image = (ImageView)findViewById(R.id.detail_image);
        txtCountdown = (TextView)findViewById(R.id.txtCountdown);
        txtGetReady = (TextView)findViewById(R.id.txtGetReady);
        txtTimer = (TextView)findViewById(R.id.timer);
        ex_name = (TextView)findViewById(R.id.title1);
        bgprogressExercise = (View) findViewById(R.id.bgprogressExercise);

        layoutGetReady = (LinearLayout)findViewById(R.id.layout_get_ready);

        progressBar = (MaterialProgressBar)findViewById(R.id.progressBar);

        //customize font
        ex_name.setTypeface(Vidaloka);
        btnStart.setTypeface(Comfortaa);

        //assign the animations
        ex_name.startAnimation(bttone);
        ex_image.startAnimation(bttone);
        bgprogressExercise.startAnimation(bttwo);
        btnStart.startAnimation(bttwo);

        //Set data
        progressBar.setMax(list.size());

        setExerciseInformation(ex_id);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btnStart.getText().toString().toLowerCase().equals("start")){
                    showGetReady();
                    btnStart.setText("done");
                }
                else if(btnStart.getText().toString().toLowerCase().equals("done")){
                    // if user click done while the Exercise is running,
                    // Cancel the count down and show rest time
                    if(yogaDB.getSettingMode() == 0)
                        exercisesEasyModeCountDown.cancel();
                    else if(yogaDB.getSettingMode() == 1)
                        exercisesMediumModeCountDown.cancel();
                    else if(yogaDB.getSettingMode() == 2)
                        exercisesHardModeCountDown.cancel();

                    if(ex_id < list.size()){
                        showRestTime();

                        //Set to next exercise
                        ex_id++;
                        progressBar.setProgress(ex_id);
                        txtTimer.setText("");

                    }
                    else{
                        showFinished();
                    }
                }
                else{
                    if(yogaDB.getSettingMode() == 0)
                        exercisesEasyModeCountDown.cancel();
                    else if(yogaDB.getSettingMode() == 1)
                        exercisesMediumModeCountDown.cancel();
                    else if(yogaDB.getSettingMode() == 2)
                        exercisesHardModeCountDown.cancel();

                    restTimeCountDown.cancel();

                    if(ex_id < list.size())
                        setExerciseInformation(ex_id);
                    else
                        showFinished();
                }
            }
        });
    }

    private void showRestTime() {
        ex_image.setVisibility(View.INVISIBLE);
        txtTimer.setVisibility(View.INVISIBLE);
        btnStart.setText("Skip");
        btnStart.setVisibility(View.VISIBLE);
        layoutGetReady.setVisibility(View.VISIBLE);

        restTimeCountDown.start();
        txtGetReady.setText("REST TIME");
    }

    private void showGetReady() {
        ex_image.setVisibility(View.INVISIBLE);
        btnStart.setVisibility(View.INVISIBLE);
        txtTimer.setVisibility(View.VISIBLE);

        layoutGetReady.setVisibility(View.VISIBLE);


        new CountDownTimer(6000,1000){

            @Override
            public void onTick(long l) {

                txtCountdown.setText("" + (l -1000) / 1000);
            }

            @Override
            public void onFinish() {
                showExercise();
            }
        }.start();
    }

    private void showExercise() {
        if(ex_id < list.size()) // list size contain all exercise
        {
            ex_image.setVisibility(View.VISIBLE);
            btnStart.setVisibility(View.VISIBLE);
            layoutGetReady.setVisibility(View.INVISIBLE);

            if(yogaDB.getSettingMode() == 0)
                exercisesEasyModeCountDown.start();
            else if(yogaDB.getSettingMode() == 1)
                exercisesMediumModeCountDown.start();
            else if(yogaDB.getSettingMode() == 2)
                exercisesHardModeCountDown.start();

            //Set data
            ex_image.setImageResource(list.get(ex_id).getImage_id());
            ex_name.setText(list.get(ex_id).getName());
        }
        else{
            showFinished();
        }
    }

    private void showFinished() {
        ex_image.setVisibility(View.INVISIBLE);
        ex_name.setVisibility(View.INVISIBLE);
        btnStart.setVisibility(View.INVISIBLE);
        txtTimer.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);

        layoutGetReady.setVisibility(View.VISIBLE);

        txtGetReady.setText("Finished !!!");
        txtCountdown.setText("Congratulation ! \n  You're done with today exercises");
        txtCountdown.setTextSize(20);

        // Save workout done to DB
        yogaDB.saveDay("" + Calendar.getInstance().getTimeInMillis());

        //When Finish go back to home page
        int timeout = 3000;

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                finish();
                Intent intent = new Intent(Daily_Training.this, MainActivity.class);
                startActivity(intent);
            }
        },timeout);
    }

    CountDownTimer exercisesEasyModeCountDown = new CountDownTimer(Common.TIME_LIMIT_EASY, 1000) {
        @Override
        public void onTick(long l) {
            txtTimer.setText("" + (l/1000));
        }

        @Override
        public void onFinish() {
            if(ex_id < list.size() - 1){
                ex_id ++;
                progressBar.setProgress(ex_id);
                txtTimer.setText("");

                setExerciseInformation(ex_id);
                btnStart.setText("Start");
            }
            else{
                showFinished();
            }
        }
    };
    CountDownTimer exercisesMediumModeCountDown = new CountDownTimer(Common.TIME_LIMIT_EASY, 1000) {
        @Override
        public void onTick(long l) {
            txtTimer.setText("" + (l/1000));
        }

        @Override
        public void onFinish() {
            if(ex_id < list.size() - 1 ){
                ex_id ++;
                progressBar.setProgress(ex_id);
                txtTimer.setText("");

                setExerciseInformation(ex_id);
                btnStart.setText("Start");
            }
            else{
                showFinished();
            }
        }
    };
    CountDownTimer exercisesHardModeCountDown = new CountDownTimer(Common.TIME_LIMIT_EASY, 1000) {
        @Override
        public void onTick(long l) {
            txtTimer.setText("" + (l/1000));
        }

        @Override
        public void onFinish() {
            if(ex_id < list.size() - 1){
                ex_id ++;
                progressBar.setProgress(ex_id);
                txtTimer.setText("");

                setExerciseInformation(ex_id);
                btnStart.setText("Start");
            }
            else{
                showFinished();
            }
        }
    };

    CountDownTimer restTimeCountDown = new CountDownTimer(10000, 1000) {
        @Override
        public void onTick(long l) {
            txtCountdown.setText("" + (l/1000));
        }

        @Override
        public void onFinish() {
            setExerciseInformation(ex_id);
            showExercise();
        }
    };

    private void setExerciseInformation(int id) {
        txtGetReady.setText("GET READY");
        ex_image.setImageResource(list.get(id).getImage_id());
        ex_name.setText(list.get(id).getName());
        btnStart.setText("Start");

        ex_image.setVisibility(View.VISIBLE);
        btnStart.setVisibility(View.VISIBLE);
        txtTimer.setVisibility(View.INVISIBLE);

        layoutGetReady.setVisibility(View.INVISIBLE);

    }

    private void initData() {

        list.add(new Exercise(R.drawable.yoga1,"Vajrasana Pose"));
        list.add(new Exercise(R.drawable.yoga2,"Dhanurasana Pose"));
        list.add(new Exercise(R.drawable.yoga3,"Mayusarana Pose"));
        list.add(new Exercise(R.drawable.yoga4,"Bhadrasana Pose"));
        list.add(new Exercise(R.drawable.yoga5,"Chakrasana Pose"));
        list.add(new Exercise(R.drawable.yoga6,"Halasana Pose"));
        list.add(new Exercise(R.drawable.yoga7,"Utkatasana Pose"));
        list.add(new Exercise(R.drawable.yoga8,"Sarvangasana Pose"));
        list.add(new Exercise(R.drawable.yoga9,"Trikonasana Pose"));
        list.add(new Exercise(R.drawable.yoga10,"Uttanasana Pose"));
        list.add(new Exercise(R.drawable.yoga11,"Balasana Pose"));
    }
}
