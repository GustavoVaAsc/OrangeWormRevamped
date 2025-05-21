package unam.fi.ai.orangewormrevamped.ui.subwaymap;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import unam.fi.ai.orangewormrevamped.R;

public class LineaDetalleActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linea_detalle);

        ImageView imagenLinea = findViewById(R.id.imagenLinea);
        //TextView tituloLinea = findViewById(R.id.tituloLinea);

        Intent intent = getIntent();
        String lineaId = intent.getStringExtra("linea_id");

        if (lineaId == null || lineaId.isEmpty()) {
            //tituloLinea.setText("Mapa General del Metro");
            imagenLinea.setImageResource(R.drawable.mapa_general_metro);
        } else {
            //tituloLinea.setText("Línea " + lineaId);

            int resourceId = getResources().getIdentifier(
                    "linea_" + lineaId.toLowerCase(),
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
