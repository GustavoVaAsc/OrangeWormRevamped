package unam.fi.ai.orangewormrevamped.ui.routedetails;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import unam.fi.ai.orangewormrevamped.R;
import unam.fi.ai.orangewormrevamped.appobjects.Station;
import unam.fi.ai.orangewormrevamped.appobjects.UserManager;
import unam.fi.ai.orangewormrevamped.appobjects.heuristics.HaversineHeuristic;
import unam.fi.ai.orangewormrevamped.appobjects.heuristics.ZeroHeuristic;

public class RouteDetailsActivity extends Activity {

    private ArrayList<Integer> stationIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_details);

        stationIds = (ArrayList<Integer>) getIntent().getSerializableExtra("station_ids");
        LinearLayout container = findViewById(R.id.stationListContainer);

        for (int stationId : stationIds) {
            Station current = UserManager.current_user.subway.queryStation(stationId);
            String name = current.getName();
            String logoPath = current.getLogo_file();

            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setPadding(16, 16, 16, 16);
            row.setGravity(Gravity.CENTER_VERTICAL);

            ImageView icon = new ImageView(this);
            Drawable logo = loadImageFromAssets(logoPath);
            if (logo != null) {
                icon.setImageDrawable(logo);
            } else {
                icon.setImageResource(R.drawable.ic_station);
            }

            LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(100, 100);
            icon.setLayoutParams(iconParams);

            TextView label = new TextView(this);
            label.setText(name);
            label.setTextSize(18);
            label.setPadding(24, 0, 0, 0);

            row.addView(icon);
            row.addView(label);

            container.addView(row);
        }

        Button bfsButton = findViewById(R.id.bfsTimeButton);
        Button astarButton = findViewById(R.id.astarTimeButton);
        Button useButton = findViewById(R.id.useRouteButton);

        bfsButton.setOnClickListener(v -> {
            int time = UserManager.current_user.subway.calculateTransferTime(stationIds,new ZeroHeuristic());
            showToast("Tiempo estimado: " + time + " min");
        });

        astarButton.setOnClickListener(v -> {
            int start = stationIds.get(0);
            int end = stationIds.get(stationIds.size() - 1);
            int time = UserManager.current_user.subway.calculateTransferTime(stationIds,new HaversineHeuristic());
            showToast("Tiempo estimado: " + time + " min");
        });

        useButton.setOnClickListener(v -> {
            showToast("Ruta activada");
            //UserManager.current_user.setCurrentRoute(new ArrayList<>(stationIds));
            finish();
        });
    }

    private Drawable loadImageFromAssets(String assetPath) {
        try {
            InputStream inputStream = getAssets().open(assetPath);
            return Drawable.createFromStream(inputStream, null);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}
