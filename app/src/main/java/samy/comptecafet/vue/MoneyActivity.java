package samy.comptecafet.vue;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import samy.comptecafet.R;
import samy.comptecafet.systeme.Compte;
import samy.comptecafet.systeme.Operation;
import samy.comptecafet.systeme.TypeOperation;
import samy.comptecafet.systeme.Depot;
import samy.comptecafet.systeme.Retrait;

public class MoneyActivity extends AppCompatActivity {

    private Compte compte;
    private TypeOperation typeOperation = TypeOperation.DEPOT;
    private Operation operation;

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
                if (depot.isChecked()) {
                    typeOperation = TypeOperation.DEPOT;
                }

                if (retrait.isChecked()) {
                    typeOperation = TypeOperation.RETRAIT;
                }

                displaySolde();
            }
        });

        Button effectuer = (Button) findViewById(R.id.effectuerButton);
        effectuer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!montant.getText().toString().equals("")) {
                    operation = createOperation();
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("operation", operation);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
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

        double montant;
        if (this.montant.getText().toString().equals("")) {
            montant = 0;
        } else {
            montant = Double.parseDouble(this.montant.getText().toString());
        }

        if (typeOperation.isDepot()) {
            resultat = Math.round((resultat + montant) * 100) / 100.;
        }
        if (typeOperation.isRetrait()) {
            resultat = Math.round((resultat - montant) * 100) / 100.;
        }

        nouveauSolde.setText(String.valueOf(resultat) + " €");
    }

    private Operation createOperation() {
        switch (typeOperation) {
            case DEPOT:
                return new Depot(Math.round(Double.parseDouble(montant.getText().toString())) * 100 / 100.);
            case RETRAIT:
                return new Retrait(Math.round(Double.parseDouble(montant.getText().toString())) * 100 / 100.);
            default:
                throw new IllegalArgumentException();
        }
    }

}