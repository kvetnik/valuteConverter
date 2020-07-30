package com.example.valuteconverter;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class MainActivity extends AppCompatActivity {

    Spinner firstValuteSpinner;
    Spinner secondValuteSpinner;
    EditText valueView;
    TextView resultView;
    Button convertBtn;
    Button requestBtn;
    Button btnDatePicker;
    ListView operationsListView;
    TextView currentDateTime;



    List<Valute> valutes = new ArrayList<>();
    Map<String, Integer> valutesMap = new HashMap<String, Integer>();
    String firstValName;
    String secondValName;
    String value;
    Valute firstValute = new Valute();
    Valute secondValute = new Valute();
    private int mYear, mMonth, mDay;

    ArrayList<String> operationsHistory= new ArrayList<>();
    ArrayList<String> operationsHistory_reverse= new ArrayList<>();

    String BASE_URL = "http://www.cbr.ru";
    String[] valutesList = {"EUR","USD","RUB","JPY"};
    String currentDate = "Дата не выбрана";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.valute_converter);

        valutesMap.put("EUR", 11);
        valutesMap.put("USD", 10);
        valutesMap.put("JPY", 33);


        firstValuteSpinner = findViewById(R.id.first_valute_spinner);
        secondValuteSpinner = findViewById(R.id.second_valute_spinner);
        valueView = findViewById(R.id.value_edit);
        resultView = findViewById(R.id.result);
        convertBtn = findViewById(R.id.convert_btn);
        requestBtn = findViewById(R.id.request_btn);
        operationsListView = findViewById(R.id.operationsListView);
        currentDateTime = findViewById(R.id.currentDateTime);
        btnDatePicker = (Button) findViewById(R.id.dateButton);

        currentDateTime.setText(currentDate);

        // выбор даты
        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callDatePicker();
            }
        });

        // выведение истории конвертации
        ArrayAdapter<String> operationsHistoryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, operationsHistory_reverse);
        operationsHistoryAdapter.setDropDownViewResource(android.R.layout.activity_list_item);
        operationsListView.setAdapter(operationsHistoryAdapter);

        // введение количества валюты
        valueView.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                value = s.toString();
            }
        });

        // обработчик нажатия кнопки обновления валют
        requestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(SimpleXmlConverterFactory.create())
                        .build();
                ValuteApi valuteApi = retrofit.create(ValuteApi.class);
                Call<ValCursFeed> call = valuteApi.getValuteCurs(currentDate);
                call.enqueue(new Callback<ValCursFeed>(){
                    @Override
                    public void onResponse(Call<ValCursFeed> call, Response<ValCursFeed> response) {
                        if (response.isSuccessful()) {
                            ValCursFeed vcf = response.body();

                            valutes = vcf.getValuteResponses();

                            System.out.println("Date: " + vcf.getDate());
                            vcf.getValuteResponses().forEach(valute -> System.out.println("Name: " + valute.getCharCode() + " - " + valute.getValue()));
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "Курсы валют обновлены", Toast.LENGTH_SHORT);
                            toast.show();

                        } else {
                            System.out.println(response.errorBody());
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "Ошибка: урсы валют не обновлены", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                    @Override
                    public void onFailure(Call<ValCursFeed> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
            }
        });

        // обработчик нажатия кнопки конвертации
        convertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operationsHistory_reverse.clear();

                if(firstValName != "RUB"){
                    Integer firstValId = valutesMap.get(firstValName);
                    firstValute = valutes.get(firstValId);
                }
                if (secondValName != "RUB"){
                    Integer secondValId = valutesMap.get(secondValName);
                    secondValute = valutes.get(secondValId);
                }
                String res = firstValute.convertTO(secondValute, Integer. parseInt (value));
                resultView.setText(res);
                operationsHistory.add(value + " " + firstValName + "   to   " + secondValName + "   --->   " + res);
                if (operationsHistory.size() > 10) {
                    operationsHistory.remove(0);
                }
                for (int i = operationsHistory.size()-1; i>=0; i--){
                    operationsHistory_reverse.add(operationsHistory.get(i));
                }
                operationsHistoryAdapter.notifyDataSetChanged();
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, valutesList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        firstValuteSpinner.setAdapter(adapter);
        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = (String)parent.getItemAtPosition(position);
                firstValName = item;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        firstValuteSpinner.setOnItemSelectedListener(itemSelectedListener);

        secondValuteSpinner.setAdapter(adapter);
        AdapterView.OnItemSelectedListener itemSelectedListener2 = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = (String)parent.getItemAtPosition(position);
                secondValName = item;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        secondValuteSpinner.setOnItemSelectedListener(itemSelectedListener2);
    }

    private void callDatePicker() {
        // получаем текущую дату
        final Calendar cal = Calendar.getInstance();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);

        // инициализируем диалог выбора даты текущими значениями
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        monthOfYear += 1;
                        String y = String.valueOf(year);
                        String m = String.valueOf(monthOfYear);
                        String d = String.valueOf(dayOfMonth);
                        if (m.length() == 1) {
                            m = "0" + m;
                        }
                        if (d.length() == 1) {
                            d = "0" + d;
                        }
                        currentDate = d + "/" + m + "/" + y;
                        currentDateTime.setText(currentDate);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }
}
