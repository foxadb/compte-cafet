package samy.comptecafet;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by samy on 15/04/17.
 */

public class Transaction implements Parcelable {

    private Date date;

    private double prix;

    private HashMap<Produit, Integer> liste;

    private static final HashMap<Produit, Double> prixProduits;
    static {
        prixProduits = new HashMap<Produit, Double>();
        prixProduits.put(Produit.TIER, 1.5);
        prixProduits.put(Produit.DEMI, 2.);
        prixProduits.put(Produit.DTIER, 2.5);
        prixProduits.put(Produit.HOTDOG, 1.);
        prixProduits.put(Produit.PANINI, 1.5);
        prixProduits.put(Produit.PIZZA, 2.);
        prixProduits.put(Produit.COCA, 0.6);
        prixProduits.put(Produit.ICETEA, 0.7);
        prixProduits.put(Produit.KITKAT, 0.5);
        prixProduits.put(Produit.BUENO, 0.6);
    }

    public Transaction() {
        this.date = new Date();
        this.prix = 0;
        this.liste = new HashMap<Produit, Integer>();
    }
    public Date getDate() {
        return date;
    }

    public double getPrix() {
        return prix;
    }

    public String getPrixString() {
        return String.valueOf(Math.round(prix*100)/100.) + " €";
    }

    public HashMap<Produit, Integer> getListe() {
        return liste;
    }

    public static HashMap<Produit, Double> getPrixProduits() {
        return prixProduits;
    }

    public void addProduit(Produit produit, int quantite) {
        prix += prixProduits.get(produit) * quantite;
        liste.put(produit, quantite);
    }

    public void removeProduit(Produit produit) {
        prix -= prixProduits.get(produit) * liste.get(produit);
        liste.remove(produit);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.date != null ? this.date.getTime() : -1);
        dest.writeDouble(this.prix);
        dest.writeSerializable(this.liste);
    }

    protected Transaction(Parcel in) {
        long tmpDate = in.readLong();
        this.date = tmpDate == -1 ? null : new Date(tmpDate);
        this.prix = in.readDouble();
        this.liste = (HashMap<Produit, Integer>) in.readSerializable();
    }

    public static final Creator<Transaction> CREATOR = new Creator<Transaction>() {
        @Override
        public Transaction createFromParcel(Parcel source) {
            return new Transaction(source);
        }

        @Override
        public Transaction[] newArray(int size) {
            return new Transaction[size];
        }
    };
}
