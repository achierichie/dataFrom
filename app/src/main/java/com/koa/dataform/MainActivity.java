package com.koa.dataform;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

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

    private static final int SAVE_FORM = 1;
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
            String date = ((TextInputLayout)findViewById(R.id.textInputLayout4)).getEditText().getText().toString().trim();
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
            String typeOfFungucide = ((TextInputLayout)findViewById(R.id.textInputLayout16)).getEditText().getText().toString().trim();
            String fungicideTimes = ((AppCompatSpinner)findViewById(R.id.fungicideTimes)).getSelectedItem().toString().trim();
            String typeOfFertilizer = ((TextInputLayout)findViewById(R.id.textInputLayout17)).getEditText().getText().toString().trim();
            String fertilizerTimes = ((AppCompatSpinner)findViewById(R.id.fertilizerTimes)).getSelectedItem().toString().trim();
            String pollinationTimes = ((AppCompatSpinner)findViewById(R.id.pollinationTimes)).getSelectedItem().toString().trim();
            String buyingCompany = ((TextInputLayout)findViewById(R.id.textInputLayout18)).getEditText().getText().toString().trim();


            boolean validInput = true;
            //todo validate the fields and throw and error if not valid
            if(!validInput) {
                Toast.makeText(this, R.string.form_error, Toast.LENGTH_LONG).show();
                return;
            }

            //todo ensure user has granted write permissions

            //save the form entries to a file
            String fileName = farmerName + sdf.format(new Date())+ ".csv";
            try {
                ArrayList<String> values = new ArrayList<>();
                values.add(date);
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
                values.add(typeOfFungucide);
                values.add(fungicideTimes);
                values.add(typeOfFertilizer);
                values.add(fertilizerTimes);
                values.add(pollinationTimes);
                values.add(buyingCompany);
                StringBuilder sb = new StringBuilder();

                for(String val : values) {
                    sb.append(val).append("|");
                }
                File dest = new File(Environment.getExternalStorageDirectory(), "farm_assessment/" + fileName);
                dest.getParentFile().mkdirs();
                String dataToSave = sb.toString().substring(0, sb.length()-1);
//                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//                    createFile(Uri.fromFile(dest.getParentFile()), dest.getName());
//                }
//                else {
                    FileWriter fileWriter = new FileWriter(dest);
                    PrintWriter printer = new PrintWriter(fileWriter);
                    printer.println(dataToSave);
                    printer.flush();
                    printer.close();
//                }
                Toast.makeText(this, R.string.save_successful, Toast.LENGTH_LONG).show();
                resetForm(this);
                System.out.println("Saved " + dest.getAbsolutePath());;
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


    }

    private void resetForm(MainActivity mainActivity) {
        ((TextInputLayout)findViewById(R.id.textInputLayout6)).getEditText().setText("");
        ((TextInputLayout)findViewById(R.id.textInputLayout7)).getEditText().setText("");
        ((TextInputLayout)findViewById(R.id.textInputLayout8)).getEditText().setText("");
        ((TextInputLayout)findViewById(R.id.textInputLayout9)).getEditText().setText("");
        ((TextInputLayout)findViewById(R.id.textInputLayout11)).getEditText().setText("");

        ((AppCompatCheckBox)findViewById(R.id.hybrid)).setChecked(false);
        ((AppCompatCheckBox)findViewById(R.id.appCompatCheckBox)).setChecked(false);

        ((TextInputLayout)findViewById(R.id.textInputLayout14)).getEditText().setText("");
        ((TextInputLayout)findViewById(R.id.textInputLayout15)).getEditText().setText("");
        ((AppCompatSpinner)findViewById(R.id.roadCondition)).setSelection(0);
        ((TextInputLayout)findViewById(R.id.textInputLayout16)).getEditText().setText("");
        ((AppCompatSpinner)findViewById(R.id.fungicideTimes)).setSelection(0);
        ((TextInputLayout)findViewById(R.id.textInputLayout17)).getEditText().setText("");
        ((AppCompatSpinner)findViewById(R.id.fertilizerTimes)).setSelection(0);
        ((AppCompatSpinner)findViewById(R.id.pollinationTimes)).setSelection(0);
        ((TextInputLayout)findViewById(R.id.textInputLayout18)).getEditText().setText("");

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


    private void createFile(Uri pickerInitialUri, String name) {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");
        intent.putExtra(Intent.EXTRA_TITLE, name);

        // Optionally, specify a URI for the directory that should be opened in
        // the system file picker when your app creates the document.
        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri);

        startActivityForResult(intent, SAVE_FORM);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SAVE_FORM) {

        }
    }
}