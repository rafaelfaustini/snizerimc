package rafaelfaustini.com.szfit;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    EditText ed1;
    EditText ed2;
    Button botao;
    TextView imcEdit;
    TextView faixaSaudavel;
    Pessoa pessoa;
    Spinner medida_altura;
    Spinner medida_peso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ed1 = (EditText)findViewById(R.id.editText2);
        ed2 = (EditText)findViewById(R.id.editText3);
        botao = (Button)findViewById(R.id.btnCalcular);
        imcEdit = (TextView)findViewById(R.id.textoImc);
        faixaSaudavel = (TextView)findViewById(R.id.textView3);
        medida_altura = (Spinner) findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.altura_medida, android.R.layout.simple_spinner_item);

        medida_altura.setAdapter(adapter);



        medida_peso = (Spinner) findViewById(R.id.spinner3);
        adapter= ArrayAdapter.createFromResource(this,
                R.array.peso_medida, android.R.layout.simple_spinner_item);
        medida_peso.setAdapter(adapter);
        pessoa = new Pessoa(this);

        ed2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // TODO do something
                    botao.performClick();
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(ed2.getWindowToken(), 0);
                    handled = true;
                }
                return handled;
            }
        });

        botao.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                int altura;
                Double peso;
                Resources res = getResources();
                if(TextUtils.isEmpty(ed1.getText().toString()) || TextUtils.isEmpty(ed2.getText().toString())){

                    AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
                    dlg.setTitle(res.getString(R.string.erro_titulo));
                    dlg.setMessage(res.getString(R.string.erro_corpo));
                    dlg.setNeutralButton("OK", null);
                    dlg.show();
                    return;
                }
                altura = Integer.parseInt(ed1.getText().toString());

                switch(medida_altura.getSelectedItemPosition()){
                    case 0:
                        break;
                    case 1:
                        altura /= 100;
                        break;
                    case 2:
                        altura*= 2.54;
                        break;
                }
                pessoa.setAltura(altura);

                peso = Double.parseDouble(ed2.getText().toString());

                switch(medida_peso.getSelectedItemPosition()){
                    case 0:
                        break;
                    case 1:
                        peso *= 0.453592;
                        break;
                }

                pessoa.setPeso(peso);
                int min = (int) Math.abs(pessoa.getIdealMin());
                int max = (int) Math.abs(pessoa.getIdealMax());
                String faixa = String.format(res.getString(R.string.label_faixapeso), pessoa.faixaMaior(), pessoa.faixaMenor());
                faixaSaudavel.setText(faixa);
                String texto;
                switch (pessoa.getNivel()) {
                    case 0:
                        texto = String.format(res.getString(R.string.imc_msg_abaixo), String.valueOf(pessoa.getImc()), min, max,  medida_peso.getSelectedItem().toString());
                        imcEdit.setText(texto);
                        break;
                    case 1:
                        texto = String.format(res.getString(R.string.imc_msg_saudavel), String.valueOf(pessoa.getImc()));
                        imcEdit.setText(texto);
                        break;
                    case 2:
                        texto = String.format(res.getString(R.string.imc_msg_sobrepeso, String.valueOf(pessoa.getImc()), pessoa.getSaude(), min, max, medida_peso.getSelectedItem().toString()));
                        imcEdit.setText(texto);
                        break;
                    case 3:
                        texto = String.format(res.getString(R.string.imc_msg_sobrepeso), String.valueOf(pessoa.getImc()), pessoa.getSaude(), min, max,  medida_peso.getSelectedItem().toString());
                        imcEdit.setText(texto);
                        break;
                    case 4:
                        texto = String.format(res.getString(R.string.imc_msg_sobrepeso), String.valueOf(pessoa.getImc()), pessoa.getSaude(), min, max,  medida_peso.getSelectedItem().toString());
                        imcEdit.setText(texto);
                        break;
                    case 5:
                        texto = String.format(res.getString(R.string.imc_msg_sobrepeso), String.valueOf(pessoa.getImc()), pessoa.getSaude(), min, max,  medida_peso.getSelectedItem().toString());
                        imcEdit.setText(texto);
                        break;

                }


            }

        });

    }
}
