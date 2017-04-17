package samy.comptecafet;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import java.util.List;

import samy.comptecafet.operations.Achat;
import samy.comptecafet.operations.Compte;
import samy.comptecafet.operations.Operation;

public class MainActivity extends AppCompatActivity {

    private static final int EAT_ACTIVITY_RESULT_CODE = 1;
    private static final int MONEY_ACTIVITY_RESULT_CODE = 2;

    private static final String soldeSavefile = "solde";
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm");

    private Compte compte = new Compte(0);
    private HistoryDb historiqueDb;

    private TextView solde;
    private TextView resultat;
    private EditText resetSolde;
    private LinearLayout historiqueLl;

    private static final LinearLayout.LayoutParams historiqueLp = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.TEXT_ALIGNMENT_CENTER);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadSolde(soldeSavefile);

        solde = (TextView) findViewById(R.id.solde);
        solde.setText(String.valueOf(Math.round(compte.getSolde() * 100) / 100.) + " €");

        Button consommer = (Button) findViewById(R.id.consommer);
        consommer.setOnClickListener(consommerListener);

        Button virement = (Button) findViewById(R.id.virement);
        virement.setOnClickListener(virementListener);

        resultat = (TextView) findViewById(R.id.resultatTransaction);
        resultat.setText("");

        resetSolde = (EditText) findViewById(R.id.resetSolde);
        resetSolde.setText("");

        Button reset = (Button) findViewById(R.id.resetSoldeButton);
        reset.setOnClickListener(resetListener);

        historiqueLl = (LinearLayout) findViewById(R.id.historique);
        historiqueDb = new HistoryDb(this);
        loadHistorique(historiqueDb);

    }

    private View.OnClickListener consommerListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, EatActivity.class);
            intent.putExtra("compte", compte);
            startActivityForResult(intent, EAT_ACTIVITY_RESULT_CODE);
        }
    };

    private View.OnClickListener virementListener = new View.OnClickListener() {
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
                        solde.setText(String.valueOf(nouveauSolde) + " €");
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
                Achat achat = intent.getParcelableExtra("achat");
                compte.achat(achat.getMontant());
                resultat.setText("Achat effectué : " + achat.getMontantString());
                addHistoryDb(achat);
                addHistorique(achat);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                resultat.setText("Achat annulé");
            }

        } else if (requestCode == MONEY_ACTIVITY_RESULT_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Operation operation = intent.getParcelableExtra("operation");
                if (operation.isDepot()) {
                    compte.depot(operation.getMontant());
                    resultat.setText("Dépôt effectué : " + operation.getMontantString());
                }
                if (operation.isRetrait()) {
                    compte.retrait(operation.getMontant());
                    resultat.setText("Retrait effectué : " + operation.getMontantString());
                }
                addHistoryDb(operation);
                addHistorique(operation);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                resultat.setText("Opération annulée");
            }
        }

        solde.setText(String.valueOf(Math.round(compte.getSolde() * 100) / 100.) + " €");
        saveCompte();
    }

    private void loadSolde(String savefile) {
        try {
            FileInputStream inputStream = openFileInput(savefile);
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
    }

    private void loadHistorique(HistoryDb historyDb) {
        List<HistoryDb.Row> rowList = historyDb.fetchAllRows();
        Iterator it = rowList.iterator();
        while (it.hasNext()) {
            HistoryDb.Row row = (HistoryDb.Row) it.next();
            StringBuilder sb = new StringBuilder();

            sb.append(row.getDate());
            sb.append(",");
            sb.append(row.getType());
            sb.append(",");
            sb.append(row.getPrix());
            sb.append(",");
            sb.append(row.getListe());

            addHistorique(sb.toString());
        }
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

    private void addHistoryDb(Operation operation) {
        String date = sdf.format(operation.getDate());
        String type = operation.getTypeOperation().toString();
        String prix = operation.getMontantString();
        String liste = null;
        if (operation.isAchat()) {
            liste = ((Achat) operation).getListeString();
        }
        historiqueDb.createRow(date, type, prix, liste);
    }

    private void addHistorique(final Operation operation) {
        Button b = new Button(this);
        String date = sdf.format(operation.getDate());
        String type = operation.getTypeOperation().toString();
        String prix = operation.getMontantString();

        String descr = date + " \t" + type + " \t" + prix;
        b.setBackgroundColor(0);
        b.setTextSize(18);
        b.setText(descr);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                intent.putExtra("operation", operation);
                startActivity(intent);
            }
        });

        historiqueLl.addView(b, 0, historiqueLp);
    }

    private void addHistorique(final String operationString) {
        Button b = new Button(this);
        String[] chaine = operationString.split("[,]");

        String descr = chaine[0] + " \t " + chaine[1] + " \t" + chaine[2];
        b.setBackgroundColor(0);
        b.setTextSize(18);
        b.setText(descr);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                intent.putExtra("string", operationString);
                startActivity(intent);
            }
        });

        historiqueLl.addView(b, 0, historiqueLp);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        historiqueDb.close();
    }

}