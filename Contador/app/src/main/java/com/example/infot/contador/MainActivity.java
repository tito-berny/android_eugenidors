package com.example.infot.contador;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import static java.lang.Integer.parseInt;

public class MainActivity extends AppCompatActivity {

    //variales

    public int contador;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contador=0;

    }

    public void sumacontador(View vista){
        contador++;
        mostrarresultado();
    }

    public void restacontador(View vista){

        CheckBox negatiu = (CheckBox)findViewById(R.id.checkBoxNegativos);
        TextView numinicial = (TextView)findViewById(R.id.textInici);

        if (numinicial.getText() == ""){

        }
        else {

            contador = numinicial.isNum;
        }

        if (negatiu.isChecked()){

            contador--;
            if (contador < 0){
                contador = 0;
            }
        }
        else {
            contador--;
        }

        mostrarresultado();
    }

    public void resetcontador(View vista){
        contador=0;
        mostrarresultado();
    }

    public void mostrarresultado(){
        TextView resultado = (TextView)findViewById(R.id.editText);  //Creamos una nueva vista y con findViewId buscamo la Vista
        resultado.setText("" + contador);
    }
}
