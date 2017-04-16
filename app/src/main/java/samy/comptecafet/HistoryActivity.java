package samy.comptecafet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Iterator;
import java.util.Map;

public class HistoryActivity extends AppCompatActivity {

    private TableLayout table;

    private static final TableRow.LayoutParams rowLp = new TableRow.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        table = (TableLayout) findViewById(R.id.historyTable);

        Intent intent = getIntent();
        if (intent.hasExtra("transaction")) {
            Transaction t = getIntent().getParcelableExtra("transaction");
            addRows(t);
        }
        if (intent.hasExtra("string")) {
            String s = getIntent().getStringExtra("string");
            addRows(s);
        }
    }

    public void addRows(Transaction t) {
        Iterator it = t.getListe().entrySet().iterator();
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
            double prixUnitaireProduit = t.getPrixProduits().get(pair.getKey());
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
    }

    public void addRows(String s) {
        TableRow row = new TableRow(this);
        row.setLayoutParams(rowLp);
        String[] chaine = s.split("[,]");
        int i = 2;

        while (i < chaine.length) {
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
