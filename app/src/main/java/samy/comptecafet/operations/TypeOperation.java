package samy.comptecafet.operations;

/**
 * Created by samy on 17/04/17.
 */

public enum TypeOperation {
    ACHAT,
    DEPOT,
    RETRAIT;

    @Override
    public String toString() {
        switch (this) {
            case ACHAT:
                return "Achat";
            case DEPOT:
                return "Dépôt";
            case RETRAIT:
                return "Retrait";
            default:
                throw new IllegalArgumentException();
        }
    }

    public boolean isAchat() {
        return (this == ACHAT);
    }

    public boolean isDepot() {
        return (this == DEPOT);
    }

    public boolean isRetrait() {
        return (this == RETRAIT);
    }

}
