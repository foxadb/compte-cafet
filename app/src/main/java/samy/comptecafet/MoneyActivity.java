package samy.comptecafet;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MoneyActivity extends AppCompatActivity {

    private Compte compte;
    private Virement virement;

    private TextView soldeActuel;
    private TextView nouveauSolde;

    private EditText montant;

    private RadioButton depot;
    private RadioButton retrait;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money);

        compte = getIntent().getParcelableExtra("compte");

        virement = new Virement(TypeVirement.DEPOT, 0);

        soldeActuel = (TextView) findViewById(R.id.soldeActuel);
        nouveauSolde = (TextView) findViewById(R.id.nouveauSolde);
        montant = (EditText) findViewById(R.id.montant);
        depot = (RadioButton) findViewById(R.id.depotButton);
        retrait = (RadioButton) findViewById(R.id.retraitButton);

        montant.setText("");

        displaySolde();

        montant.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                setVirement();
                displaySolde();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

        });

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.retraitDepot);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                setVirement();
                displaySolde();
            }
        });

        Button effectuer = (Button) findViewById(R.id.effectuerButton);
        effectuer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setVirement();
                Intent returnIntent = new Intent();
                returnIntent.putExtra("virement", virement);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

        Button annuler = (Button) findViewById(R.id.annulerButton);
        annuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
            }
        });

    }

    private void displaySolde() {
        soldeActuel.setText(compte.getSoldeString() + " €");

        double resultat = compte.getSolde();
        if (virement.isDepot()) {
            resultat = Math.round((resultat + virement.getMontant()) * 100) / 100.;
        }
        if (virement.isRetrait()) {
            resultat = Math.round((resultat - virement.getMontant()) * 100) / 100.;
        }
        nouveauSolde.setText(String.valueOf(resultat) + " €");
    }

    private void setVirement() {
        if (depot.isChecked()) {
            virement.setType(TypeVirement.DEPOT);
        }
        if (retrait.isChecked()) {
            virement.setType(TypeVirement.RETRAIT);
        }

        if (montant.getText().toString().equals("")) {
            virement.setMontant(0);
        } else {
            virement.setMontant(Double.parseDouble(montant.getText().toString()));
        }
    }

}
