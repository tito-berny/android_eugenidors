package com.example.infot.contador;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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
        contador --;
        mostrarresultado();

    }
    public void resetcontador(View vista){
        contador=0;
        mostrarresultado();

    }
    public void mostrarresultado(){
        TextView resultado = (TextView)findViewById(R.id.editText);
        resultado.setText("Contador : " + contador);
    }
}
