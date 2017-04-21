package samy.comptecafet;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
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
    private TableLayout historyTable;

    private static final LinearLayout.LayoutParams historiqueLp = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,
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

        historyTable = (TableLayout) findViewById(R.id.historyTable);
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
                        resultat.setText("Solde reset à " + String.valueOf(nouveauSolde) + " €");
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
                long id = addHistoryDb(achat);
                addHistorique(achat, id);
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
                long id = addHistoryDb(operation);
                addHistorique(operation, id);
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

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle(R.string.app_name);
            builder.setMessage("Bienvenue dans l'application Compte Cafet Ensimag. Entrer votre solde actuel :");

            final EditText premierSolde = new EditText(MainActivity.this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            premierSolde.setLayoutParams(lp);
            premierSolde.setInputType(InputType.TYPE_CLASS_NUMBER);

            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                    double nouveauSolde;
                    if (premierSolde.getText().toString().equals("")) {
                        nouveauSolde = 0;
                    } else {
                        nouveauSolde = Math.round(Double.parseDouble(premierSolde.getText().toString()) * 100) / 100.;
                    }
                    compte.setSolde(nouveauSolde);
                    solde.setText(String.valueOf(nouveauSolde) + " €");
                    saveCompte();
                }
            });
            builder.setNegativeButton("Quitter", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                    MainActivity.this.finish();
                    System.exit(0);
                }
            });

            AlertDialog alert = builder.create();
            alert.setView(premierSolde);
            alert.show();
        }
    }

    private void loadHistorique(HistoryDb historyDb) {
        List<HistoryDb.Row> rowList = historyDb.fetchAllRows();
        Iterator it = rowList.iterator();
        while (it.hasNext()) {
            HistoryDb.Row row = (HistoryDb.Row) it.next();
            addHistorique(row, row.get_Id());
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

    private long addHistoryDb(Operation operation) {
        String date = sdf.format(operation.getDate());
        String type = operation.getTypeOperation().toString();
        String prix = operation.getMontantString();
        String liste = null;
        if (operation.isAchat()) {
            liste = ((Achat) operation).getListeString();
        }
        return historiqueDb.createRow(date, type, prix, liste);
    }

    private void addHistorique(final Operation operation, final long rowId) {
        final TableRow tableRow = new TableRow(this);
        tableRow.setLayoutParams(historiqueLp);

        Button button = new Button(this);
        String date = sdf.format(operation.getDate());
        String type = operation.getTypeOperation().toString();
        String prix = operation.getMontantString();

        String descr = date + " \t" + type + " \t" + prix;
        button.setBackgroundColor(Color.TRANSPARENT);
        button.setTextSize(18);
        button.setText(descr);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                intent.putExtra("operation", operation);
                startActivity(intent);
            }
        });

        Button supprButton = new Button(this);
        supprButton.setBackgroundColor(Color.TRANSPARENT);
        supprButton.setTextColor(Color.RED);
        supprButton.setTextSize(18);
        supprButton.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        supprButton.setText("Supprimer");

        supprButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)  {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(R.string.app_name);
                builder.setMessage("Voulez-vous reset le solde à la valeur précédent cette transaction ? "
                        + "\n\r" + operation.getTypeOperation().toString() + " " + operation.getMontantString());
                builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        double incr = operation.getMontant();
                        if (operation.isDepot()) {
                            compte.incrSolde(-incr);
                        } else {
                            compte.incrSolde(incr);
                        }
                        solde.setText(compte.getSoldeString() + " €");
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

                historyTable.removeView(tableRow);
                historiqueDb.deleteRow(rowId);
            }
        });

        View space = new View(this);
        space.setMinimumWidth(32);

        tableRow.addView(button);
        tableRow.addView(space);
        tableRow.addView(supprButton);
        historyTable.addView(tableRow, 0, historiqueLp);
    }

    private void addHistorique(final HistoryDb.Row row, final long rowId) {
        StringBuilder sb = new StringBuilder();
        sb.append(row.getDate());
        sb.append(",");
        sb.append(row.getType());
        sb.append(",");
        sb.append(row.getPrix());
        sb.append(",");
        sb.append(row.getListe());

        final String operationString = sb.toString();

        final TableRow tableRow = new TableRow(this);
        tableRow.setLayoutParams(historiqueLp);

        Button button = new Button(this);
        String[] chaine = operationString.split("[,]");

        String descr = chaine[0] + " \t " + chaine[1] + " \t" + chaine[2];
        button.setBackgroundColor(Color.TRANSPARENT);
        button.setTextSize(18);
        button.setText(descr);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                intent.putExtra("string", operationString);
                startActivity(intent);
            }
        });

        Button supprButton = new Button(this);
        supprButton.setBackgroundColor(Color.TRANSPARENT);
        supprButton.setTextColor(Color.RED);
        supprButton.setTextSize(18);
        supprButton.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        supprButton.setText("Supprimer");

        supprButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(R.string.app_name);
                builder.setMessage("Voulez-vous reset le solde à la valeur précédent cette transaction ? "
                        + "\n\r" + row.getType() + " " + row.getPrix());
                builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        double incr = Double.parseDouble(row.getPrix().replace(" €", ""));
                        if (row.getType().equals("Dépôt")) {
                            compte.incrSolde(-incr);
                        } else {
                            compte.incrSolde(incr);
                        }
                        solde.setText(compte.getSoldeString() + " €");
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

                historyTable.removeView(tableRow);
                historiqueDb.deleteRow(rowId);

            }
        });

        View space = new View(this);
        space.setMinimumWidth(32);

        tableRow.addView(button);
        tableRow.addView(space);
        tableRow.addView(supprButton);
        historyTable.addView(tableRow, 0, historiqueLp);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        historiqueDb.close();
    }

}