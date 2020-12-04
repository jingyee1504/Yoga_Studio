package com.example.yogastudio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.yogastudio.Adapter.RecyclerViewAdapter;
import com.example.yogastudio.Model.Exercise;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ListExercises extends AppCompatActivity {
    TextView title, subtitlepage, intropage, subIntropage;
    Animation bttone, bttwo,ltr;
    View divpage;
    FloatingActionButton fab;

    List<Exercise> exerciseList = new ArrayList<>();
    RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_exercises);

        initData();
        //load animation
        bttone = AnimationUtils.loadAnimation(this, R.anim.bttone);
        bttwo = AnimationUtils.loadAnimation(this, R.anim.bttwo);
        ltr = AnimationUtils.loadAnimation(this, R.anim.ltrtwo);

        //import fonts
        Typeface Vidaloka = Typeface.createFromAsset(getAssets(), "fonts/vidaloka.ttf");
        Typeface Barlow = Typeface.createFromAsset(getAssets(), "fonts/barlow.ttf");

        title = (TextView) findViewById(R.id.title);
        subtitlepage = (TextView) findViewById(R.id.subtitlepage2);
        intropage = (TextView) findViewById(R.id.intropage);
        subIntropage = (TextView) findViewById(R.id.subIntropage);
        divpage = (View) findViewById(R.id.divpage);
        fab = (FloatingActionButton) findViewById(R.id.fab_btn);

        //customize font item
        title.setTypeface(Vidaloka);
        subtitlepage.setTypeface(Barlow);
        intropage.setTypeface(Vidaloka);
        subIntropage.setTypeface(Barlow);

        //assign the animations
        title.startAnimation(bttone);
        subtitlepage.startAnimation(bttone);
        divpage.startAnimation(bttone);

        intropage.startAnimation(bttwo);
        subIntropage.startAnimation(bttwo);

        recyclerView = (RecyclerView)findViewById(R.id.list_ex);
        adapter = new RecyclerViewAdapter(exerciseList, getBaseContext());
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        recyclerView.startAnimation(ltr);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListExercises.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initData() {

        exerciseList.add(new Exercise(R.drawable.yoga1,"Vajrasana Pose"));
        exerciseList.add(new Exercise(R.drawable.yoga2,"Dhanurasana Pose"));
        exerciseList.add(new Exercise(R.drawable.yoga3,"Mayusarana Pose"));
        exerciseList.add(new Exercise(R.drawable.yoga4,"Bhadrasana Pose"));
        exerciseList.add(new Exercise(R.drawable.yoga5,"Chakrasana Pose"));
        exerciseList.add(new Exercise(R.drawable.yoga6,"Halasana Pose"));
        exerciseList.add(new Exercise(R.drawable.yoga7,"Utkatasana Pose"));
        exerciseList.add(new Exercise(R.drawable.yoga8,"Sarvangasana Pose"));
        exerciseList.add(new Exercise(R.drawable.yoga9,"Trikonasana Pose"));
        exerciseList.add(new Exercise(R.drawable.yoga10,"Uttanasana Pose"));
        exerciseList.add(new Exercise(R.drawable.yoga11,"Balasana Pose"));
    }
}