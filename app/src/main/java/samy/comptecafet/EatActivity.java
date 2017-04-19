package samy.comptecafet;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.ToggleButton;

import samy.comptecafet.operations.Achat;
import samy.comptecafet.operations.Compte;
import samy.comptecafet.operations.Produit;

public class EatActivity extends AppCompatActivity {

    private Compte compte;
    private Achat achat;

    private TextView soldeRestant;
    private TextView prix;

    private NumberPicker produitNp;

    private Produit lastProdChanged;
    private TextView lastNbChanged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eat);

        compte = getIntent().getParcelableExtra("compte");

        achat = new Achat(0);

        soldeRestant = (TextView) findViewById(R.id.soldeRestant);
        prix = (TextView) findViewById(R.id.prix);

        displayPrix();

        ToggleButton tier = (ToggleButton) findViewById(R.id.tierButton);
        TextView tierNb = (TextView) findViewById(R.id.tierNb);
        setProduitListener(tier, tierNb, Produit.TIER);

        ToggleButton demi = (ToggleButton) findViewById(R.id.demiButton);
        TextView demiNb = (TextView) findViewById(R.id.demiNb);
        setProduitListener(demi, demiNb, Produit.DEMI);

        ToggleButton dtier = (ToggleButton) findViewById(R.id.dtierButton);
        TextView dtierNb = (TextView) findViewById(R.id.dtierNb);
        setProduitListener(dtier, dtierNb, Produit.DTIER);

        ToggleButton hotdog = (ToggleButton) findViewById(R.id.hotdogButton);
        TextView hotdogNb = (TextView) findViewById(R.id.hotdogNb);
        setProduitListener(hotdog, hotdogNb, Produit.HOTDOG);

        ToggleButton panini = (ToggleButton) findViewById(R.id.paniniButton);
        TextView paniniNb = (TextView) findViewById(R.id.paniniNb);
        setProduitListener(panini, paniniNb, Produit.PANINI);

        ToggleButton pizza = (ToggleButton) findViewById(R.id.pizzaButton);
        TextView pizzaNb = (TextView) findViewById(R.id.pizzaNb);
        setProduitListener(pizza, pizzaNb, Produit.PIZZA);

        ToggleButton sevenup = (ToggleButton) findViewById(R.id.sevenupButton);
        TextView sevenupNb = (TextView) findViewById(R.id.sevenupNb);
        setProduitListener(sevenup, sevenupNb, Produit.SEVENUP);

        ToggleButton coca = (ToggleButton) findViewById(R.id.cocaButton);
        TextView cocaNb = (TextView) findViewById(R.id.cocaNb);
        setProduitListener(coca, cocaNb, Produit.COCA);

        ToggleButton cherry = (ToggleButton) findViewById(R.id.cherryButton);
        TextView cherryNb = (TextView) findViewById(R.id.cherryNb);
        setProduitListener(cherry, cherryNb, Produit.CHERRY);

        ToggleButton oasis = (ToggleButton) findViewById(R.id.oasisButton);
        TextView oasisNb = (TextView) findViewById(R.id.oasisNb);
        setProduitListener(oasis, oasisNb, Produit.OASIS);

        ToggleButton fanta = (ToggleButton) findViewById(R.id.fantaButton);
        TextView fantaNb = (TextView) findViewById(R.id.fantaNb);
        setProduitListener(fanta, fantaNb, Produit.FANTA);

        ToggleButton icetea = (ToggleButton) findViewById(R.id.iceteaButton);
        TextView iceteaNb = (TextView) findViewById(R.id.iceteaNb);
        setProduitListener(icetea, iceteaNb, Produit.ICETEA);

        ToggleButton twix = (ToggleButton) findViewById(R.id.twixButton);
        TextView twixNb = (TextView) findViewById(R.id.twixNb);
        setProduitListener(twix, twixNb, Produit.TWIX);

        ToggleButton snickers = (ToggleButton) findViewById(R.id.snickersButton);
        TextView snickersNb = (TextView) findViewById(R.id.snickersNb);
        setProduitListener(snickers, snickersNb, Produit.SNICKERS);

        ToggleButton mars = (ToggleButton) findViewById(R.id.marsButton);
        TextView marsNb = (TextView) findViewById(R.id.marsNb);
        setProduitListener(mars, marsNb, Produit.MARS);

        ToggleButton marsglace = (ToggleButton) findViewById(R.id.marsglaceButton);
        TextView marsglaceNb = (TextView) findViewById(R.id.marsglaceNb);
        setProduitListener(marsglace, marsglaceNb, Produit.MARSGLACE);

        ToggleButton kitkat = (ToggleButton) findViewById(R.id.kitkatButton);
        TextView kitkatNb = (TextView) findViewById(R.id.kitkatNb);
        setProduitListener(kitkat, kitkatNb, Produit.KITKAT);

        ToggleButton bueno = (ToggleButton) findViewById(R.id.buenoButton);
        TextView buenoNb = (TextView) findViewById(R.id.buenoNb);
        setProduitListener(bueno, buenoNb, Produit.BUENO);

        produitNp = (NumberPicker) findViewById(R.id.produitNp);
        produitNp.setMinValue(0);
        produitNp.setMaxValue(9);
        produitNp.setWrapSelectorWheel(true);

        produitNp.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                if (lastNbChanged != null && lastProdChanged != null) {
                    achat.putProduit(lastProdChanged, newVal);
                    lastNbChanged.setText(String.valueOf(newVal));
                    displayPrix();
                }
            }
        });

        Button acheter = (Button) findViewById(R.id.acheter);
        acheter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("achat", achat);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

        Button annuler = (Button) findViewById(R.id.annuler);
        annuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
            }
        });
    }

    private void displayPrix() {
        prix.setText(achat.getMontantString());
        double resultat = Math.round((compte.getSolde() - achat.getMontant()) * 100) / 100.;
        soldeRestant.setText(String.valueOf(resultat) + " €");
    }

    private void setProduitListener(final ToggleButton button, final TextView number, final Produit produit) {

        final CompoundButton.OnCheckedChangeListener buttonListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    produitNp.setValue(1);

                    lastNbChanged = number;
                    lastProdChanged = produit;

                    achat.putProduit(produit, produitNp.getValue());
                    number.setText(String.valueOf(produitNp.getValue()));
                } else {
                    lastNbChanged = null;
                    lastProdChanged = null;

                    achat.removeProduit(produit);
                    produitNp.setValue(0);
                    number.setText(String.valueOf(0));
                }
                displayPrix();
            }
        };
        button.setOnCheckedChangeListener(buttonListener);
    }

}