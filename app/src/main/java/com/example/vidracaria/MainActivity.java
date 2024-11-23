package com.example.vidracaria;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // Declaração dos botões da interface
    ImageButton btnTiposDeVidro, btnEspelho, btnAbrirMapa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Configura o layout da atividade

        // Inicializa os botões usando o ID dos elementos da interface definidos no XML
        btnTiposDeVidro = findViewById(R.id.bt_tiposdeVidro);
        btnEspelho = findViewById(R.id.bt_Espelho);
        btnAbrirMapa = findViewById(R.id.bt_abrirMapa); // Inicializa o botão para abrir o mapa

        // Configura o listener para o botão `bt_tiposdeVidro`
        btnTiposDeVidro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Cria uma intenção para abrir a tela de vidro
                Intent intent = new Intent(MainActivity.this, TelaVidro.class);
                startActivity(intent); // Inicia a tela de vidro
            }
        });

        // Configura o listener para o botão `bt_Espelho`
        btnEspelho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Cria uma intenção para abrir a tela de espelho
                Intent intent = new Intent(MainActivity.this, TelaEspelho.class);
                startActivity(intent); // Inicia a tela de espelho
            }
        });

        // Configura o listener para o `btnAbrirMapa`
        btnAbrirMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // URL do local da vidraçaria no Google Maps
                String url = "https://www.google.com/maps/place/Vidra%C3%A7aria+Vidro+Box/@-16.0225565,-48.0581582,322m/data=!3m1!1e3!4m6!3m5!1s0x935981c34db864d3:0x806b9abc44d36b08!8m2!3d-16.0228845!4d-48.0587942!16s%2Fg%2F11f5tw92_h?authuser=0&entry=ttu&g_ep=EgoyMDI0MTExMy4xIKXMDSoASAFQAw%3D%3D";
                // Cria uma intenção para abrir o Google Maps com a URL fornecida
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent); // Inicia a aplicação de mapa com a URL
            }
        });
    }
}
