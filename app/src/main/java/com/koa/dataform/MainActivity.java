package com.koa.dataform;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private Date date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //    TextInputLayout dropdown;
        //    AutoCompleteTextView act_season;
        //    ArrayList<String> arrayList_dropdown;
        //    ArrayAdapter<String> arrayAdapter_dropdown;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_hhmm", Locale.ENGLISH);
        Button saveButton = findViewById(R.id.saveBtn);
        TextInputEditText dateInput = findViewById(R.id.dateField);
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        dateInput.setText(sdf2.format(new Date()));
        dateInput.setOnFocusChangeListener((v,a) -> {
            if(a) {
                showDatePickerDialog(dateInput);
            }
        });

        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            saveButton.setEnabled(false);
        }

        saveButton.setOnClickListener(v -> {
            //get the values from the form
//            String name = ((TextInputLayout)findViewById(R.id.textInputLayout4)).getEditText().getText().toString().trim();
            String enumerator = ((TextInputLayout)findViewById(R.id.textInputLayout6)).getEditText().getText().toString().trim();
            String farmerName = ((TextInputLayout)findViewById(R.id.textInputLayout7)).getEditText().getText().toString().trim();
            String farmLocation = ((TextInputLayout)findViewById(R.id.textInputLayout8)).getEditText().getText().toString().trim();
            String farmSize = ((TextInputLayout)findViewById(R.id.textInputLayout9)).getEditText().getText().toString().trim();
            String farmAge = ((TextInputLayout)findViewById(R.id.textInputLayout11)).getEditText().getText().toString().trim();

            boolean isHybrid = ((AppCompatCheckBox)findViewById(R.id.hybrid)).isChecked();
            boolean isTettehQuashie = ((AppCompatCheckBox)findViewById(R.id.appCompatCheckBox)).isChecked();

            String yieldPerSeason = ((TextInputLayout)findViewById(R.id.textInputLayout14)).getEditText().getText().toString().trim();
            String otherCrops = ((TextInputLayout)findViewById(R.id.textInputLayout15)).getEditText().getText().toString().trim();
            String roadCondition = ((AppCompatSpinner)findViewById(R.id.roadCondition)).getSelectedItem().toString().trim();

            boolean validInput = true;
            //todo validate the fields and throw and error if not valid
            if(!validInput) {
                Toast.makeText(this, R.string.form_error, Toast.LENGTH_LONG).show();
                return;
            }

            //todo ensure user has granted write permissions

            //save the form entries to a file
            String fileName = "Farm_Assessment_Form_" + sdf.format(new Date())+ ".csv";
            try {
                ArrayList<String> values = new ArrayList<>();
//                values.add(name);
                values.add(enumerator);
                values.add(farmerName);
                values.add(farmLocation);
                values.add(farmSize);
                values.add(farmAge);
                values.add(isHybrid + "");
                values.add(isTettehQuashie + "");
                values.add(yieldPerSeason);
                values.add(otherCrops);
                values.add(roadCondition);
                StringBuilder sb = new StringBuilder();

                for(String val : values) {
                    sb.append(val).append("|");
                }
                File dest = new File(Environment.getExternalStorageDirectory(), "farm_assessment/" + fileName);
                dest.getParentFile().mkdirs();
                FileWriter fileWriter = new FileWriter(dest);
                PrintWriter printer = new PrintWriter(fileWriter);
                String dataToSave = sb.toString().substring(0, sb.length()-1);
                printer.println(dataToSave);
                printer.flush();
                printer.close();
                Toast.makeText(this, R.string.save_successful, Toast.LENGTH_LONG).show();
                System.out.println("Saved " + dest.getAbsolutePath());;
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


    }

    private static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState);
    }

    private static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(extStorageState);
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }


    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
            Calendar c = Calendar.getInstance();
            c.set(year, month, day);
            Date date = c.getTime();
            ((TextInputEditText)getActivity().findViewById(R.id.dateField)).setText(sdf.format(date));
        }
    }

}