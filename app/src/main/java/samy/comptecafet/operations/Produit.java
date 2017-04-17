package samy.comptecafet.operations;

/**
 * Created by samy on 15/04/17.
 */

public enum Produit {
    TIER,
    DEMI,
    DTIER,
    HOTDOG,
    PANINI,
    PIZZA,
    COCA,
    ICETEA,
    KITKAT,
    BUENO;

    @Override
    public String toString() {
        switch (this) {
            case TIER:
                return "1/3";
            case DEMI:
                return "1/2";
            case DTIER:
                return "2/3";
            case HOTDOG:
                return "Hot-dog";
            case PANINI:
                return "Panini";
            case PIZZA:
                return "Pizza";
            case COCA:
                return "Coca";
            case ICETEA:
                return "Ice Tea";
            case KITKAT:
                return "Kit Kat";
            case BUENO:
                return "Bueno";
            default:
                throw new IllegalArgumentException();
        }
    }
}
