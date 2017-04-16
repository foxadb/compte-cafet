package samy.comptecafet;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.TextView;

import java.io.Serializable;

/**
 * Created by samy on 14/04/17.
 */

public class Compte implements Parcelable {

    private double solde;

    public Compte(double solde) {
        this.solde = solde;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.solde);
    }

    protected Compte(Parcel in) {
        this.solde = in.readDouble();
    }

    public static final Parcelable.Creator<Compte> CREATOR = new Parcelable.Creator<Compte>() {
        @Override
        public Compte createFromParcel(Parcel source) {
            return new Compte(source);
        }

        @Override
        public Compte[] newArray(int size) {
            return new Compte[size];
        }
    };

    public double getSolde() {
        return solde;
    }

    public void setSolde(double solde) {
        this.solde = solde;
    }

    public String getSoldeString() {
        return String.valueOf(Math.round(solde * 100) / 100.);
    }

    public void achat(double prix) {
        solde -= prix;
    }

    public void depot(double montant) {
        solde += montant;
    }

    public void retrait(double montant) {
        solde -= montant;
    }


}
