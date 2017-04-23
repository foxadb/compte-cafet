package samy.comptecafet.vue;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Iterator;
import java.util.Map;

import samy.comptecafet.R;
import samy.comptecafet.systeme.Achat;
import samy.comptecafet.systeme.Operation;
import samy.comptecafet.systeme.Produit;

public class HistoryActivity extends AppCompatActivity {

    private TableLayout table;

    private Button quitter;

    private static final TableRow.LayoutParams rowLp = new TableRow.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        table = (TableLayout) findViewById(R.id.historyTable);

        quitter = (Button) findViewById(R.id.quitter);
        quitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();
        if (intent.hasExtra("operation")) {
            Operation operation = getIntent().getParcelableExtra("operation");
            addRows(operation);
        }

        if (intent.hasExtra("string")) {
            String s = getIntent().getStringExtra("string");
            addRows(s);
        }
    }

    public void addRows(Operation operation) {

        if (operation.isAchat()) {
            Iterator it = ((Achat) operation).getListe().entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<Produit, Integer> pair = (Map.Entry) it.next();

                TableRow row = new TableRow(this);
                row.setLayoutParams(rowLp);

                TextView produit = new TextView(this);
                produit.setText(pair.getKey().toString());

                TextView quantite = new TextView(this);
                int quantiteProduit = pair.getValue();
                quantite.setText(String.valueOf(quantiteProduit));

                TextView prixUnitaire = new TextView(this);
                double prixUnitaireProduit = ((Achat) operation).getPrixProduits().get(pair.getKey());
                prixUnitaire.setText(String.valueOf(prixUnitaireProduit));

                TextView prixTotal = new TextView(this);
                double prixTotalProduit = quantiteProduit * prixUnitaireProduit;
                prixTotal.setText(String.valueOf(prixTotalProduit));

                row.addView(produit);
                row.addView(quantite);
                row.addView(prixUnitaire);
                row.addView(prixTotal);
                table.addView(row);
            }

        } else {
            TableRow row = new TableRow(this);
            row.setLayoutParams(rowLp);

            TextView produit = new TextView(this);
            if (operation.isDepot()) {
                produit.setText("Dépôt");
            }
            if (operation.isRetrait()) {
                produit.setText("Retrait");
            }

            TextView quantite = new TextView(this);
            quantite.setText(String.valueOf(1));

            TextView prixUnitaire = new TextView(this);
            prixUnitaire.setText(String.valueOf(operation.getMontant()));

            TextView prixTotal = new TextView(this);
            prixTotal.setText(String.valueOf(operation.getMontant()));

            row.addView(produit);
            row.addView(quantite);
            row.addView(prixUnitaire);
            row.addView(prixTotal);
            table.addView(row);
        }

    }

    public void addRows(String s) {
        String[] chaine = s.split("[,]");
        int i = 3;

        while (i < chaine.length) {
            TableRow row = new TableRow(this);
            row.setLayoutParams(rowLp);

            TextView produit = new TextView(this);
            produit.setText(chaine[i]);
            ++i;

            TextView quantite = new TextView(this);
            quantite.setText(chaine[i]);
            ++i;

            TextView prixUnitaire = new TextView(this);
            prixUnitaire.setText(chaine[i]);
            ++i;

            TextView prixTotal = new TextView(this);
            prixTotal.setText(chaine[i]);
            ++i;

            row.addView(produit);
            row.addView(quantite);
            row.addView(prixUnitaire);
            row.addView(prixTotal);
            table.addView(row);
        }
    }

}
