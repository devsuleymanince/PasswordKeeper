package com.suleymanince.realmsimpleapp1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;

import java.util.List;

import io.paperdb.Paper;

public class PatternPassword extends AppCompatActivity {

    String save_pattern_key = "pattern_code";

    String final_pattern = "";

    PatternLockView mPatternLockView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        Paper.init(this);
        final String save_pattern = Paper.book().read(save_pattern_key);

        if(save_pattern != null && !save_pattern.equals("null")){
            setContentView(R.layout.pattern_screen);
            mPatternLockView = findViewById(R.id.pattern_lock_view);
            mPatternLockView.addPatternLockListener(new PatternLockViewListener() {
                @Override
                public void onStarted() {

                }

                @Override
                public void onProgress(List<PatternLockView.Dot> progressPattern) {

                }

                @Override
                public void onComplete(List<PatternLockView.Dot> pattern) {
                    final_pattern = PatternLockUtils.patternToString(mPatternLockView,pattern);

                    if(final_pattern.equals(save_pattern)){
                        Toast.makeText(getApplicationContext(),"Şifre Doğru",Toast.LENGTH_SHORT).show();

                        Intent i = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(i);

                        mPatternLockView.clearPattern();
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Şifre Yanlış",Toast.LENGTH_SHORT).show();
                        mPatternLockView.clearPattern();
                    }
                }

                @Override
                public void onCleared() {

                }
            });
        }
        else{
            setContentView(R.layout.activity_pattern_password);
            mPatternLockView = findViewById(R.id.pattern_lock_view);
            mPatternLockView.addPatternLockListener(new PatternLockViewListener() {
                @Override
                public void onStarted() {

                }

                @Override
                public void onProgress(List<PatternLockView.Dot> progressPattern) {

                }

                @Override
                public void onComplete(List<PatternLockView.Dot> pattern) {
                    final_pattern = PatternLockUtils.patternToString(mPatternLockView,pattern);
                }

                @Override
                public void onCleared() {

                }
            });

            Button btnPatternOlustur = findViewById(R.id.btn_pattern_olustur);
            btnPatternOlustur.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Paper.book().write(save_pattern_key,final_pattern);
                    Toast.makeText(getApplicationContext(),"Şekil Kaydedildi",Toast.LENGTH_SHORT).show();
                    finish();

                    //  finish dedigimiz icin uygulama kapanıyor tekrar bu sayfayı açması için alttaki kodu yazdım
                    Intent i = getBaseContext().getPackageManager().
                            getLaunchIntentForPackage(getBaseContext().getPackageName());
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                }
            });
        }
    }
}
