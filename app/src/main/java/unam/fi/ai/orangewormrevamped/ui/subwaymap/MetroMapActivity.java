package unam.fi.ai.orangewormrevamped.ui.subwaymap;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;


import androidx.appcompat.app.AppCompatActivity;

import unam.fi.ai.orangewormrevamped.R;

public class MetroMapActivity extends AppCompatActivity {
    private LinearLayout layoutLineas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metro_map); // Asegúrate que este archivo XML existe

        layoutLineas = findViewById(R.id.layoutLineas);

        for (int i = 1; i <= 12; i++) {
            final int numeroLinea = i;

            Button btnLinea = new Button(this);
            btnLinea.setText("Ver Línea " + numeroLinea);

            btnLinea.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MetroMapActivity.this, LineaDetalleActivity.class);
                    intent.putExtra("linea", numeroLinea);
                    startActivity(intent);
                }
            });

            layoutLineas.addView(btnLinea);
        }
    }
}