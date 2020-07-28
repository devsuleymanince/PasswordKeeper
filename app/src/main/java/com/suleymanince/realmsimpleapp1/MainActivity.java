package com.suleymanince.realmsimpleapp1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;

import java.util.List;

import io.paperdb.Paper;
import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    Realm realm;
    EditText edtKullaniciAdi, edtAdi, edtSifre;
    Button btnKaydet,btnIptal;
    ImageButton btnYeniKayitOlustur;
    ListView listView;

    RealmResults<KisiModel> list;




    PatternLockView mPatternLockView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        realmTanimla();
        tanimla();
        tabloyuGoster();

        btnYeniKayitOlustur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kayitEkle();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                list = realm.where(KisiModel.class).findAll(); // listeyi getirdim
                alertDialogAc(position);
            }
        });


    }

    @Override
    protected void onPause() {

        // eger home ya da son uygulama tusuna basılırsa uygulamayı kapatsın (yeniden sifre sorması için)
        super.onPause();
        finish();
        System.exit(0);


    }

    public void realmTanimla() {
        realm = Realm.getDefaultInstance();
    }

    public void tanimla() {
        edtKullaniciAdi = findViewById(R.id.edt_ekle_kullanici_adi);
        edtAdi = findViewById(R.id.edt_ekle_adi);
        edtSifre = findViewById(R.id.edt_ekle_sifre);
        btnKaydet = findViewById(R.id.btn_ekle_kaydet);
        btnIptal = findViewById(R.id.btn_ekle_iptal);
        btnYeniKayitOlustur = findViewById(R.id.yeni_kayit_olustur);
        listView = findViewById(R.id.listView1);
    }

    // inner classta olduğumuz için final ile tanımladık
    public void yazdir(final String kullaniciAdi, final String isim, final String sifre) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                KisiModel kisiModel = realm.createObject(KisiModel.class);
                kisiModel.setKullaniciAdi(kullaniciAdi);
                kisiModel.setIsim(isim);
                kisiModel.setSifre(sifre);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Toast.makeText(getApplicationContext(), "Kayıt Başarılı", Toast.LENGTH_SHORT).show();

            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Toast.makeText(getApplicationContext(), "Kayıt Başarısız", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void tabloyuGoster(){

        RealmResults<KisiModel> kisiModels = realm.where(KisiModel.class).findAll();

            Adapter adapter = new Adapter(kisiModels,this);
            listView.setAdapter(adapter);
            if(kisiModels.size() == 0){
                Toast.makeText(getApplicationContext(),"Listenizde kayıtlı şifre yok",Toast.LENGTH_SHORT).show();
            }
    }


    public void tabloSil(final int position){

        final RealmResults<KisiModel> gelenList = realm.where(KisiModel.class).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                KisiModel kisi = gelenList.get(position);
                kisi.deleteFromRealm();
                tabloyuGoster();
            }
        });
    }

    public void alertDialogAc(final int position){
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_alert,null);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        Button btnSil = view.findViewById(R.id.btn_sil);
        Button btnIptal = view.findViewById(R.id.btn_iptal);

        alert.setView(view);
        alert.setCancelable(false); // ekranın farklı bi  yerine bastığında iptal olmayacak

        final AlertDialog dialog = alert.create();

        btnSil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabloSil(position);
                dialog.cancel(); // işlem tamamlanınca kapansın
            }
        });

        btnIptal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();

    }

    public void kayitEkle(){
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_ekle,null);
         AlertDialog.Builder alert = new AlertDialog.Builder(this);

        edtKullaniciAdi = view.findViewById(R.id.edt_ekle_kullanici_adi);
        edtAdi = view.findViewById(R.id.edt_ekle_adi);
        edtSifre= view.findViewById(R.id.edt_ekle_sifre);

        btnKaydet = view.findViewById(R.id.btn_ekle_kaydet);
        btnIptal = view.findViewById(R.id.btn_ekle_iptal);


        alert.setView(view);

        final AlertDialog dialog = alert.create();

        btnKaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String kullaniciAdiText = edtKullaniciAdi.getText().toString();
                final String adiText = edtAdi.getText().toString();
                final String sifreText = edtSifre.getText().toString();

                if(kullaniciAdiText.matches("") || adiText.matches("") || sifreText.matches("")){
                    Toast.makeText(getApplicationContext(),"Lütfen alanları doldurun",Toast.LENGTH_SHORT).show();
                    return;
                }

                yazdir(kullaniciAdiText, adiText, sifreText);

                tabloyuGoster();

                dialog.cancel();

            }
        });

        btnIptal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        dialog.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.hakkinda,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.hakkinda){
            Intent i = new Intent(this,Hakkinda.class);
            startActivity(i);
        }
        if(item.getItemId() == R.id.menu_sifre_degistir){

            final PatternPassword objPatternPassword = new PatternPassword();

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
                    objPatternPassword.final_pattern = PatternLockUtils.patternToString(mPatternLockView,pattern);
                }

                @Override
                public void onCleared() {

                }
            });

            Button btnPatternOlustur = findViewById(R.id.btn_pattern_olustur);
            btnPatternOlustur.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Paper.book().write(objPatternPassword.save_pattern_key,objPatternPassword.final_pattern);
                    Toast.makeText(getApplicationContext(),"Şekil Kaydedildi",Toast.LENGTH_SHORT).show();
                    finish();
                    System.exit(0);
                }
            });
        }

        return super.onOptionsItemSelected(item);
    }


}
