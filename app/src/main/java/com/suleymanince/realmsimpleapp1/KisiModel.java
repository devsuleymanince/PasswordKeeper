package com.suleymanince.realmsimpleapp1;

import io.realm.RealmObject;
import io.realm.annotations.RealmClass;

@RealmClass
public class KisiModel extends RealmObject {
    private String kullaniciAdi;
    private String isim;
    private String sifre;


    public String getKullaniciAdi() {
        return kullaniciAdi;
    }

    public void setKullaniciAdi(String kullaniciAdi) {
        this.kullaniciAdi = kullaniciAdi;
    }

    public String getIsim() {
        return isim;
    }

    public void setIsim(String isim) {
        this.isim = isim;
    }

    public String getSifre() {
        return sifre;
    }

    public void setSifre(String sifre) {
        this.sifre = sifre;
    }


    @Override
    public String toString() {
        return "KisiModel{" +
                "kullaniciAdi='" + kullaniciAdi + '\'' +
                ", isim='" + isim + '\'' +
                ", sifre='" + sifre + '\'' +
                '}';
    }
}
