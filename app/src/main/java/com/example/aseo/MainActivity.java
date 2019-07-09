package com.example.aseo;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private final float transparency = (float)0.3;
    private SQLiteHelper db;
    private Spinner sArea;
    private Button btnRate;
    private ImageView imageView;
    private EditText etDate;

    private final String[] areas = {"Administración",
            "Area de Psicología",
            "Unidad Especializada de Seguimiento de Personas Adolescentes",
            "Unidad de Condiciones en Libertad",
            "Suspensión Condicional del Proceso",
            "Unidad de Medidas Cautelares",
            "Coordinación Jurídica",
            "Unidad de Archivo y Estadística",
            "Coordinación de Ejecución de Penas",
            "Unidad de Servicios de Atención Médica, Salud Mental y Reinserción Social",
            "Unidad Administrativa",
            "Coordinación de Planeación",
            "Dirección General",
            "Áreas Comunes",
            "Firmas"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db=new SQLiteHelper(this);

        sArea = (Spinner) findViewById(R.id.spinner);

        //https://stackoverflow.com/questions/14139106/spinner-does-not-wrap-text-is-this-an-android-bug
        ArrayAdapter<String> aa = new ArrayAdapter<String>(this, R.layout.spinner_item, android.R.id.text1, areas)
        {
            @Override
            public View getDropDownView(final int position, final View convertView, final ViewGroup parent)
            {
                final View v = super.getDropDownView(position, convertView, parent);
                v.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        ((TextView)v.findViewById(android.R.id.text1)).setSingleLine(false);

                        int pos = sArea.getSelectedItemPosition();

                        db.getReadableDatabase();
                        String fecha = etDate.getText().toString();
                        ArrayList<String> lista = db.getAreas(fecha);
                        if(convertView == null && lista.contains(areas[position])){
                            ((TextView)v.findViewById(android.R.id.text1)).setTextColor(Color.GRAY);
                        }
                    }
                });
                return v;
            }
        };

        sArea.setAdapter(aa);

        sArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                refresh(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnRate = (Button) findViewById(R.id.btnRate);

        btnRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputDialog();
            }
        });

        imageView = (ImageView) findViewById(R.id.imageView);

        etDate = (EditText) findViewById(R.id.etDate);
        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.etDate) {
                    showDatePickerDialog(etDate);
                }
            }
        });

        SimpleDateFormat formatoFecha = new SimpleDateFormat("d/ M / yyyy");
        String fecha = formatoFecha.format(new Date());
        etDate.setText(fecha);
    }

    public void onRadioButtonClicked(View view) {
        RadioButton rbtn1 = findViewById(R.id.radioButton1);
        RadioButton rbtn2 = findViewById(R.id.radioButton2);
        RadioButton rbtn3 = findViewById(R.id.radioButton3);
        RadioButton rbtn4 = findViewById(R.id.radioButton4);
        RadioButton rbtn5 = findViewById(R.id.radioButton5);

        rbtn1.setAlpha(transparency);
        rbtn2.setAlpha(transparency);
        rbtn3.setAlpha(transparency);
        rbtn4.setAlpha(transparency);
        rbtn5.setAlpha(transparency);

        rbtn1.setButtonDrawable(R.drawable.face1);
        rbtn2.setButtonDrawable(R.drawable.face2);
        rbtn3.setButtonDrawable(R.drawable.face3);
        rbtn4.setButtonDrawable(R.drawable.face4);
        rbtn5.setButtonDrawable(R.drawable.face5);

        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioButton1:
                if (checked)
                    view.setAlpha(1);
                    break;
            case R.id.radioButton2:
                if (checked)
                    view.setAlpha(1);
                break;
            case R.id.radioButton3:
                if (checked)
                    view.setAlpha(1);
                break;
            case R.id.radioButton4:
                if (checked)
                    view.setAlpha(1);
                break;
            case R.id.radioButton5:
                if (checked)
                    view.setAlpha(1);
                break;
        }
    }

    private void showInputDialog() {
        LayoutInflater layoutInflater=LayoutInflater.from(this);
        View promtView= layoutInflater.inflate(R.layout.input_dialog,null);
        AlertDialog.Builder alertDialogBuilder= new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promtView);

        final EditText etComments = (EditText) promtView.findViewById(R.id.etComments);
        final EditText etName = (EditText) promtView.findViewById(R.id.etName);
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
                        int radioButtonID = radioGroup.getCheckedRadioButtonId();
                        RadioButton rbtn = (RadioButton)radioGroup.findViewById(radioButtonID);
                        int position = radioGroup.indexOfChild(rbtn);
                        String rValoracion = Integer.toString(position + 1);

                        String rArea = sArea.getSelectedItem().toString();

                        String rFecha = etDate.getText().toString();

                        String rComments = etComments.getText().toString().toUpperCase();

                        String rName = etName.getText().toString().toUpperCase();

                        db.insertarDatos(rValoracion, rArea, rFecha, rComments, rName);

                        radioGroup.setVisibility(View.GONE);
                        btnRate.setVisibility(View.GONE);
                        imageView.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Guardado", Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    //region Crear y seleccionar DatePicker
    private void showDatePickerDialog(final EditText editText) {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because january is zero
                final String selectedDate = day + " / " + (month + 1) + " / " + year;
                editText.setText(selectedDate);

                int pos = sArea.getSelectedItemPosition();
                refresh(pos);
            }
        });
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private void refresh(int position){
        db.getReadableDatabase();

        btnRate = (Button) findViewById(R.id.btnRate);
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        TextView tvSaved = (TextView) findViewById(R.id.textView);

        String fecha = etDate.getText().toString();

        if(db.saved(areas[position], fecha)){
            btnRate.setVisibility(View.GONE);
            radioGroup.setVisibility(View.GONE);
            imageView.setVisibility(View.GONE);
            tvSaved.setVisibility(View.VISIBLE);
        }
        else {
            btnRate.setVisibility(View.VISIBLE);
            radioGroup.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.VISIBLE);
            tvSaved.setVisibility(View.GONE);
        }

        RadioButton rbtn1 = findViewById(R.id.radioButton1);
        RadioButton rbtn2 = findViewById(R.id.radioButton2);
        RadioButton rbtn3 = findViewById(R.id.radioButton3);
        RadioButton rbtn4 = findViewById(R.id.radioButton4);
        RadioButton rbtn5 = findViewById(R.id.radioButton5);

        rbtn1.setAlpha(transparency);
        rbtn2.setAlpha(transparency);
        rbtn3.setAlpha(transparency);
        rbtn4.setAlpha(transparency);
        rbtn5.setAlpha(transparency);

        rbtn1.setButtonDrawable(R.drawable.face1);
        rbtn2.setButtonDrawable(R.drawable.face2);
        rbtn3.setButtonDrawable(R.drawable.face3);
        rbtn4.setButtonDrawable(R.drawable.face4);
        rbtn5.setButtonDrawable(R.drawable.face5);
    }
}
