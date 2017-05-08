package kr.ac.kaist.vclab.bubble.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import kr.ac.kaist.vclab.bubble.R;
import kr.ac.kaist.vclab.bubble.events.BackPressCloseHandler;

public class GameOverActivity extends Activity implements View.OnClickListener{
    private BackPressCloseHandler backPressCloseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_over);


        Button button;
        button = (Button) findViewById(R.id.over_restart_button);
        button.setOnClickListener(this);
        backPressCloseHandler = new BackPressCloseHandler(this);

    }
    public void onClick(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }

}