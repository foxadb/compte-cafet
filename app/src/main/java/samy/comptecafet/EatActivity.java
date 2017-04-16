package samy.comptecafet;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

public class EatActivity extends AppCompatActivity {

    private Compte compte;
    private Transaction transaction;

    private TextView soldeRestant;
    private TextView prix;

    private ToggleButton tier;
    private ToggleButton demi;
    private ToggleButton dtier;
    private ToggleButton hotdog;
    private ToggleButton panini;
    private ToggleButton pizza;
    private ToggleButton coca;
    private ToggleButton icetea;
    private ToggleButton kitkat;
    private ToggleButton bueno;

    private Button acheter;
    private Button annuler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eat);

        compte = getIntent().getParcelableExtra("compte");

        transaction = new Transaction();

        soldeRestant = (TextView) findViewById(R.id.soldeRestant);
        prix = (TextView) findViewById(R.id.prix);

        displayPrix();

        tier = (ToggleButton) findViewById(R.id.tierButton);
        demi = (ToggleButton) findViewById(R.id.demiButton);
        dtier = (ToggleButton) findViewById(R.id.dtierButton);
        hotdog = (ToggleButton) findViewById(R.id.hotdogButton);
        panini = (ToggleButton) findViewById(R.id.paniniButton);
        pizza = (ToggleButton) findViewById(R.id.pizzaButton);
        coca = (ToggleButton) findViewById(R.id.cocaButton);
        icetea = (ToggleButton) findViewById(R.id.iceteaButton);
        kitkat = (ToggleButton) findViewById(R.id.kitkatButton);
        bueno = (ToggleButton) findViewById(R.id.buenoButton);

        tier.setOnCheckedChangeListener(tierListener);
        demi.setOnCheckedChangeListener(demiListener);
        dtier.setOnCheckedChangeListener(dtierListener);
        hotdog.setOnCheckedChangeListener(hotdogListener);
        panini.setOnCheckedChangeListener(paniniListener);
        pizza.setOnCheckedChangeListener(pizzaListener);
        coca.setOnCheckedChangeListener(cocaListener);
        icetea.setOnCheckedChangeListener(iceteaListener);
        kitkat.setOnCheckedChangeListener(kitkatListener);
        bueno.setOnCheckedChangeListener(buenoListener);

        acheter = (Button) findViewById(R.id.acheter);
        acheter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("transaction", transaction);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

        annuler = (Button) findViewById(R.id.annuler);
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
        prix.setText(transaction.getPrixString());
        double resultat = Math.round((compte.getSolde() - transaction.getPrix()) * 100) / 100.;
        soldeRestant.setText(String.valueOf(resultat) + "€");
    }

    private CompoundButton.OnCheckedChangeListener tierListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                transaction.addProduit(Produit.TIER, 1);
            } else {
                transaction.removeProduit(Produit.TIER);
            }
            displayPrix();
        }
    };

    private CompoundButton.OnCheckedChangeListener demiListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                transaction.addProduit(Produit.DEMI, 1);
            } else {
                transaction.removeProduit(Produit.DEMI);
            }
            displayPrix();
        }
    };

    private CompoundButton.OnCheckedChangeListener dtierListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                transaction.addProduit(Produit.DTIER, 1);
            } else {
                transaction.removeProduit(Produit.DTIER);
            }
            displayPrix();
        }
    };

    private CompoundButton.OnCheckedChangeListener hotdogListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                transaction.addProduit(Produit.HOTDOG, 1);
            } else {
                transaction.removeProduit(Produit.HOTDOG);
            }
            displayPrix();
        }
    };

    private CompoundButton.OnCheckedChangeListener paniniListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                transaction.addProduit(Produit.PANINI, 1);
            } else {
                transaction.removeProduit(Produit.PANINI);
            }
            displayPrix();
        }
    };

    private CompoundButton.OnCheckedChangeListener pizzaListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                transaction.addProduit(Produit.PIZZA, 1);
            } else {
                transaction.removeProduit(Produit.PIZZA);
            }
            displayPrix();
        }
    };
    private CompoundButton.OnCheckedChangeListener cocaListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                transaction.addProduit(Produit.COCA, 1);
            } else {
                transaction.removeProduit(Produit.COCA);
            }
            displayPrix();
        }
    };

    private CompoundButton.OnCheckedChangeListener iceteaListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                transaction.addProduit(Produit.ICETEA, 1);
            } else {
                transaction.removeProduit(Produit.ICETEA);
            }
            displayPrix();
        }
    };

    private CompoundButton.OnCheckedChangeListener kitkatListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                transaction.addProduit(Produit.KITKAT, 1);
            } else {
                transaction.removeProduit(Produit.KITKAT);
            }
            displayPrix();
        }
    };

    private CompoundButton.OnCheckedChangeListener buenoListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                transaction.addProduit(Produit.BUENO, 1);
            } else {
                transaction.removeProduit(Produit.BUENO);
            }
            displayPrix();
        }
    };

}
