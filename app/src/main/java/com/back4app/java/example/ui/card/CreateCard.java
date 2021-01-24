package com.back4app.java.example.ui.card;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.back4app.java.example.HomeScreen;
import com.back4app.java.example.R;
import com.back4app.java.example.ui.graph.GraphActivity;
import com.back4app.java.example.ui.pound.PoundActivity;
import com.back4app.java.example.ui.settings.SettingsActivity;

public class CreateCard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_card);
//        final ImageView splash = (ImageView) findViewById(R.id.testImage);
//        ObjectAnimator animation = ObjectAnimator.ofFloat(splash, "translationX", 100f);
//        animation.setDuration(2000);
//        animation.start();
        ImageView view = (ImageView) findViewById(R.id.cardCreate);


//        RotateAnimation anim = new RotateAnimation(0f, 10f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//
////Setup anim with desired properties
//        anim.setInterpolator(new BounceInterpolator());
//        anim.setRepeatCount(Animation.INFINITE); //Repeat animation indefinitely
//        anim.setDuration(1000); //Put desired duration per anim cycle here, in milliseconds
//        ObjectAnimator test = ObjectAnimator.ofObject(view, )

//
////Start animation
//        view.startAnimation(anim);


// Start animating the image


// Later.. stop the animation
//        splash.setAnimation(null);
    }
    public void addCardOnClick(View v){
        Intent intent = new Intent(getApplicationContext(), ChoosePinForNewCard.class);
        startActivity(intent);
    }
    public void homeButtonOnClick(View v){
        Intent intent = new Intent(getApplicationContext(), HomeScreen.class);
        startActivity(intent);
    }
    public void graphButtonOnClick(View v){
        Intent intent = new Intent(getApplicationContext(), GraphActivity.class);
        startActivity(intent);
    }
    public void poundButtonOnClick(View v){
        Intent intent = new Intent(getApplicationContext(), PoundActivity.class);
        startActivity(intent);
    }
    public void cardButtonOnClick(View v){
//        Intent intent = new Intent(getApplicationContext(), CreateCard.class);
//        startActivity(intent);
    }
    public void gearsButtonOnClick(View v){
        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(intent);
    }
}