package samy.comptecafet;

/**
 * Created by samy on 15/04/17.
 */

public enum TypeVirement {
    DEPOT,
    RETRAIT;

    @Override
    public String toString() {
        switch (this) {
            case DEPOT:
                return "Dépôt";
            case RETRAIT:
                return "Retrait";
            default:
                throw new IllegalArgumentException();
        }
    }

}
