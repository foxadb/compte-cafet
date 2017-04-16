package samy.comptecafet;

import android.icu.text.DisplayContext;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by samy on 15/04/17.
 */

public class Virement implements Parcelable {

    private TypeVirement type;

    private double montant;

    public Virement() {
        this.type = TypeVirement.DEPOT;
        this.montant = 0;
    }

    public Virement(TypeVirement type, double montant) {
        this.type = type;
        this.montant = montant;
    }

    public TypeVirement getType() {
        return type;
    }

    public void setType(TypeVirement type) {
        this.type = type;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public String getMontantString() {
        return String.valueOf(Math.round(montant * 100) / 100.) + " €";
    }

    public boolean isDepot() {
        return (type == TypeVirement.DEPOT);
    }

    public boolean isRetrait() {
        return (type == TypeVirement.RETRAIT);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type == null ? -1 : this.type.ordinal());
        dest.writeDouble(this.montant);
    }

    protected Virement(Parcel in) {
        int tmpType = in.readInt();
        this.type = tmpType == -1 ? null : TypeVirement.values()[tmpType];
        this.montant = in.readDouble();
    }

    public static final Parcelable.Creator<Virement> CREATOR = new Parcelable.Creator<Virement>() {
        @Override
        public Virement createFromParcel(Parcel source) {
            return new Virement(source);
        }

        @Override
        public Virement[] newArray(int size) {
            return new Virement[size];
        }
    };
}
