package unam.fi.ai.orangewormrevamped.ui.subwaymap;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import unam.fi.ai.orangewormrevamped.R;

public class LineaDetalleActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linea_detalle);

        ImageView imagenLinea = findViewById(R.id.imagenLinea);
        TextView tituloLinea = findViewById(R.id.tituloLinea);

        Intent intent = getIntent();
        int numeroLinea = intent.getIntExtra("linea", 0); // ← Clave corregida

        if (numeroLinea <= 0) {
            tituloLinea.setText("Mapa General del Metro");
            imagenLinea.setImageResource(R.drawable.mapa_general_metro);
        } else {
            tituloLinea.setText("Línea " + numeroLinea);
            int resourceId = getResources().getIdentifier(
                    "linea_" + numeroLinea,
                    "drawable",
                    getPackageName()
            );

            if (resourceId != 0) {
                imagenLinea.setImageResource(resourceId);
            } else {
                imagenLinea.setImageResource(R.drawable.mapa_general_metro);
            }
        }
    }
}
