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
        setContentView(R.layout.activity_metro_map);

        layoutLineas = findViewById(R.id.layoutLineas);

        // Líneas 1 a 12
        for (int i = 1; i <= 12; i++) {
            final String lineaId = String.valueOf(i); // ← Convertir a String

            Button btnLinea = new Button(this);
            btnLinea.setText("Ver Línea " + lineaId);

            btnLinea.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MetroMapActivity.this, LineaDetalleActivity.class);
                    intent.putExtra("linea_id", lineaId);
                    startActivity(intent);
                }
            });

            layoutLineas.addView(btnLinea);
        }

        // Línea A
        agregarBotonLinea("A");

        // Línea B
        agregarBotonLinea("B");
    }

    private void agregarBotonLinea(final String lineaId) {
        Button btnLinea = new Button(this);
        btnLinea.setText("Ver Línea " + lineaId);

        btnLinea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MetroMapActivity.this, LineaDetalleActivity.class);
                intent.putExtra("linea_id", lineaId);
                startActivity(intent);
            }
        });

        layoutLineas.addView(btnLinea);
    }
}
