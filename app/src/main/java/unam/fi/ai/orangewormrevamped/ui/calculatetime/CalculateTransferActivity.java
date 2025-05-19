package unam.fi.ai.orangewormrevamped.ui.calculatetime;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import unam.fi.ai.orangewormrevamped.R;
import unam.fi.ai.orangewormrevamped.appobjects.UserManager;
import unam.fi.ai.orangewormrevamped.appobjects.heuristics.*;

public class CalculateTransferActivity extends AppCompatActivity {

    EditText startStationField, endStationField;
    Button bfsButton, astarButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate_transfer);

        startStationField = findViewById(R.id.startStation);
        endStationField = findViewById(R.id.endStation);
        bfsButton = findViewById(R.id.bfsButton);
        astarButton = findViewById(R.id.astarButton);

        bfsButton.setOnClickListener(view -> {
            try {
                int start = Integer.parseInt(startStationField.getText().toString());
                int end = Integer.parseInt(endStationField.getText().toString());

                ArrayList<Integer> path = UserManager.current_user.subway.leastStationsPath(start, end);
                if (path.isEmpty()) {
                    Toast.makeText(this, "No path found (BFS)", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "BFS Path: " + path, Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show();
            }
        });

        astarButton.setOnClickListener(view -> {
            try {
                int start = Integer.parseInt(startStationField.getText().toString());
                int end = Integer.parseInt(endStationField.getText().toString());

                ArrayList<Integer> path = UserManager.current_user.subway.optimalPath(start, end, new HaversineHeuristic());
                if (path.isEmpty()) {
                    Toast.makeText(this, "No path found (A*)", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "A* Path: " + path, Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
