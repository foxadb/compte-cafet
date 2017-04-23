package samy.comptecafet.systeme;

import android.os.Parcel;

/**
 * Created by samy on 15/04/17.
 */

public class Depot extends Operation {

    public Depot(double montant) {
        super(TypeOperation.DEPOT, montant);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    protected Depot(Parcel in) {
        super(in);
    }

    public static final Creator<Depot> CREATOR = new Creator<Depot>() {
        @Override
        public Depot createFromParcel(Parcel source) {
            return new Depot(source);
        }

        @Override
        public Depot[] newArray(int size) {
            return new Depot[size];
        }
    };

}