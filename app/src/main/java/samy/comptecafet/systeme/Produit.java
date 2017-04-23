package samy.comptecafet.systeme;

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
    SEVENUP,
    COCA,
    CHERRY,
    FANTA,
    OASIS,
    ICETEA,
    TWIX,
    SNICKERS,
    MARS,
    MARSGLACE,
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
            case SEVENUP:
                return "7 Up";
            case COCA:
                return "Coca";
            case CHERRY:
                return "Coca Cherry";
            case OASIS:
                return "Oasis";
            case FANTA:
                return "Fanta";
            case ICETEA:
                return "Ice Tea";
            case TWIX:
                return "Twix";
            case SNICKERS:
                return "Snickers";
            case MARS:
                return "Mars";
            case MARSGLACE:
                return "Mars glacé";
            case KITKAT:
                return "Kit Kat";
            case BUENO:
                return "Bueno";
            default:
                throw new IllegalArgumentException();
        }
    }
}