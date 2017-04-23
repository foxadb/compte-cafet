package samy.comptecafet.systeme;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by samy on 17/04/17.
 */

public abstract class Operation implements Parcelable {

    private Date date;

    private TypeOperation typeOperation;

    private double montant;

    public Operation(TypeOperation typeOperation, double montant) {
        this.date = new Date();
        this.typeOperation = typeOperation;
        this.montant = montant;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public TypeOperation getTypeOperation() {
        return typeOperation;
    }

    public void setTypeOperation(TypeOperation typeOperation) {
        this.typeOperation = typeOperation;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public boolean isAchat() {
        return (typeOperation == TypeOperation.ACHAT);
    }

    public boolean isDepot() {
        return (typeOperation == TypeOperation.DEPOT);
    }

    public boolean isRetrait() {
        return (typeOperation == TypeOperation.RETRAIT);
    }

    public String getMontantString() {
        return String.valueOf(Math.round(montant * 100) / 100.) + " €";
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.date != null ? this.date.getTime() : -1);
        dest.writeInt(this.typeOperation == null ? -1 : this.typeOperation.ordinal());
        dest.writeDouble(this.montant);
    }

    protected Operation(Parcel in) {
        long tmpDate = in.readLong();
        this.date = tmpDate == -1 ? null : new Date(tmpDate);
        int tmpTypeOperation = in.readInt();
        this.typeOperation = tmpTypeOperation == -1 ? null : TypeOperation.values()[tmpTypeOperation];
        this.montant = in.readDouble();
    }

}