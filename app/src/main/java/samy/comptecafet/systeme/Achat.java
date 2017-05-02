package samy.comptecafet.systeme;

import android.os.Parcel;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by samy on 15/04/17.
 */

public class Achat extends Operation {

    private HashMap<Produit, Integer> liste;

    private static final HashMap<Produit, Double> prixProduits;
    static {
        prixProduits = new HashMap<>();
        prixProduits.put(Produit.TIER, 1.5);
        prixProduits.put(Produit.DEMI, 2.);
        prixProduits.put(Produit.DTIER, 2.5);
        prixProduits.put(Produit.HOTDOG, 1.);
        prixProduits.put(Produit.PANINI, 1.5);
        prixProduits.put(Produit.PIZZA, 2.);
        prixProduits.put(Produit.SEVENUP, 0.6);
        prixProduits.put(Produit.COCA, 0.6);
        prixProduits.put(Produit.CHERRY, 0.7);
        prixProduits.put(Produit.OASIS, 0.7);
        prixProduits.put(Produit.FANTA, 0.6);
        prixProduits.put(Produit.ICETEA, 0.6);
        prixProduits.put(Produit.COUNTRY, 0.35);
        prixProduits.put(Produit.MARS, 0.5);
        prixProduits.put(Produit.MARSGLACE, 0.7);
        prixProduits.put(Produit.TWIX, 0.5);
        prixProduits.put(Produit.SNICKERS, 0.5);
        prixProduits.put(Produit.KITKAT, 0.5);
        prixProduits.put(Produit.BUENO, 0.6);
    }

    public Achat(double montant) {
        super(TypeOperation.ACHAT, montant);
        this.liste = new HashMap<>();
    }

    public HashMap<Produit, Integer> getListe() {
        return liste;
    }

    public static HashMap<Produit, Double> getPrixProduits() {
        return prixProduits;
    }

    public void putProduit(Produit produit, int quantite) {
        if (quantite > 0) {
            if (liste.containsKey(produit)) {
                removeProduit(produit);
            }
            setMontant(getMontant() + prixProduits.get(produit) * quantite);
            liste.put(produit, quantite);
        }
    }

    public void removeProduit(Produit produit) {
        setMontant(getMontant() - prixProduits.get(produit) * liste.get(produit));
        liste.remove(produit);
    }

    public String getListeString() {
        StringBuilder sb = new StringBuilder();
        Iterator it = liste.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Produit, Integer> pair = (Map.Entry) it.next();
            sb.append(pair.getKey().toString());
            sb.append(",");
            sb.append(pair.getValue().toString());
            sb.append(",");
            sb.append(getPrixProduits().get(pair.getKey()).toString());
            sb.append(",");
            sb.append(String.valueOf(Math.round(
                    getPrixProduits().get(pair.getKey()) * pair.getValue() * 100) / 100.));
            sb.append(",");
        }
        return sb.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeSerializable(this.liste);
    }

    protected Achat(Parcel in) {
        super(in);
        this.liste = (HashMap<Produit, Integer>) in.readSerializable();
    }

    public static final Creator<Achat> CREATOR = new Creator<Achat>() {
        @Override
        public Achat createFromParcel(Parcel source) {
            return new Achat(source);
        }

        @Override
        public Achat[] newArray(int size) {
            return new Achat[size];
        }
    };

}