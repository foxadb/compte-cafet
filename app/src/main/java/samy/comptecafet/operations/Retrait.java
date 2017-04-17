package samy.comptecafet.operations;

import android.os.Parcel;

/**
 * Created by samy on 17/04/17.
 */

public class Retrait extends Operation {

    public Retrait(double montant) {
        super(TypeOperation.RETRAIT, montant);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    protected Retrait(Parcel in) {
        super(in);
    }

    public static final Creator<Retrait> CREATOR = new Creator<Retrait>() {
        @Override
        public Retrait createFromParcel(Parcel source) {
            return new Retrait(source);
        }

        @Override
        public Retrait[] newArray(int size) {
            return new Retrait[size];
        }
    };

}