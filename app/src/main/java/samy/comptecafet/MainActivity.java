package samy.comptecafet;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final int EAT_ACTIVITY_RESULT_CODE = 1;
    private static final int MONEY_ACTIVITY_RESULT_CODE = 2;

    private static final String soldeSavefile = "solde";
    private static final String historiqueSavefile = "historique";
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm");

    private Compte compte = new Compte(0);

    private TextView solde;
    private TextView resultat;
    private EditText resetSolde;
    private LinearLayout historique;

    private static final LinearLayout.LayoutParams historiqueLp = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.TEXT_ALIGNMENT_CENTER);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            FileInputStream inputStream = openFileInput(soldeSavefile);
            BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
            String line = r.readLine();

            if (line != null) {
                compte.setSolde(Double.parseDouble(line));
            }

            r.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            compte.setSolde(100);
        }

        try {
            FileInputStream inputStream = openFileInput(historiqueSavefile);
            BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            while ((line = r.readLine()) != null) {
                addHistorique(line);
            }

            r.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        solde = (TextView) findViewById(R.id.solde);
        solde.setText(String.valueOf(Math.round(compte.getSolde()*100)/100.) + "€");

        Button consommer = (Button) findViewById(R.id.consommer);
        consommer.setOnClickListener(consommerListener);

        Button depot = (Button) findViewById(R.id.depot);
        depot.setOnClickListener(depotListener);

        resultat = (TextView) findViewById(R.id.resultatTransaction);
        resultat.setText("");

        resetSolde = (EditText) findViewById(R.id.resetSolde);
        resetSolde.setText("");

        Button reset = (Button) findViewById(R.id.resetSoldeButton);
        reset.setOnClickListener(resetListener);

        historique = (LinearLayout) findViewById(R.id.historique);

    }

    private View.OnClickListener consommerListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, EatActivity.class);
            intent.putExtra("compte", compte);
            startActivityForResult(intent, EAT_ACTIVITY_RESULT_CODE);
        }
    };

    private View.OnClickListener depotListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, MoneyActivity.class);
            intent.putExtra("compte", compte);
            startActivityForResult(intent, MONEY_ACTIVITY_RESULT_CODE);
        }
    };

    private View.OnClickListener resetListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!resetSolde.getText().toString().equals("")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(R.string.app_name);
                final double nouveauSolde = Math.round(Double.parseDouble(
                        resetSolde.getText().toString()) * 100) / 100.;
                builder.setMessage("Voulez-vous vraiment reset le solde à \r\n"
                        + nouveauSolde + " € ?");
                builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        compte.setSolde(nouveauSolde);
                        solde.setText(String.valueOf(nouveauSolde) + "€");
                        saveCompte();
                    }
                });
                builder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == EAT_ACTIVITY_RESULT_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Transaction t = (Transaction) intent.getParcelableExtra("transaction");
                compte.achat(t.getPrix());
                resultat.setText("Transaction effectuée : " + t.getPrixString());
                addHistorique(t);
                saveHistorique(t);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                resultat.setText("Transaction annulée");
            }

        } else if (requestCode == MONEY_ACTIVITY_RESULT_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Virement v = (Virement) intent.getParcelableExtra("virement");
                if (v.isDepot()) {
                    compte.depot(v.getMontant());
                    resultat.setText("Dépôt effectué : " + v.getMontantString());
                }
                if (v.isRetrait()) {
                    compte.retrait(v.getMontant());
                    resultat.setText("Retrait effectué : " + v.getMontantString());
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                resultat.setText("Virement annulé");
            }
        }

        solde.setText(String.valueOf(Math.round(compte.getSolde() * 100)/100.) + "€");
        saveCompte();
    }

    private void saveCompte() {
        try {
            FileOutputStream outputStream = openFileOutput(soldeSavefile, Context.MODE_PRIVATE);
            outputStream.write(String.valueOf(compte.getSolde()).getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveHistorique(Transaction t) {
        try {
            FileOutputStream outputStream = openFileOutput(historiqueSavefile, Context.MODE_APPEND);
            String s;

            s = sdf.format(t.getDate());
            outputStream.write(s.getBytes());

            s = String.valueOf(t.getPrix());
            outputStream.write(",".getBytes());
            outputStream.write(s.getBytes());

            Iterator it = t.getListe().entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<Produit, Integer> pair = (Map.Entry) it.next();
                outputStream.write(",".getBytes());
                s = pair.getKey().toString() + ",";
                outputStream.write(s.getBytes());
                s = pair.getValue().toString();
                outputStream.write(s.getBytes());
            }

            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addHistorique(final Transaction t) {
        Button b = new Button(this);
        String formatedDate = sdf.format(t.getDate());
        String descr = "Date : " + formatedDate + "\t\t Prix : " + t.getPrixString();
        b.setText(descr);
        b.setBackgroundColor(0);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                intent.putExtra("transaction", t);
                startActivity(intent);
            }
        });

        historique.addView(b, historiqueLp);
    }

    private void addHistorique(final String s) {
        String[] chaine = s.split("[,]");
        Button b = new Button(this);
        String descr = "Date : " + chaine[0] + "\t\t Prix : " + chaine[1] + " €";
        b.setText(descr);
        b.setBackgroundColor(0);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                intent.putExtra("string", s);
                startActivity(intent);
            }
        });

        historique.addView(b, historiqueLp);
    }

}