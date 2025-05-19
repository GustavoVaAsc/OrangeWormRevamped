package unam.fi.ai.orangewormrevamped.ui.calculatetime;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import unam.fi.ai.orangewormrevamped.R;
import unam.fi.ai.orangewormrevamped.appobjects.UserManager;
import unam.fi.ai.orangewormrevamped.appobjects.heuristics.HaversineHeuristic;

public class CalculateTransferFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_calculate_transfer, container, false);

        EditText startStationField = root.findViewById(R.id.startStation);
        EditText endStationField = root.findViewById(R.id.endStation);
        Button bfsButton = root.findViewById(R.id.bfsButton);
        Button astarButton = root.findViewById(R.id.astarButton);

        bfsButton.setOnClickListener(v -> {
            try {
                int start = Integer.parseInt(startStationField.getText().toString());
                int end = Integer.parseInt(endStationField.getText().toString());

                ArrayList<Integer> path = UserManager.current_user.subway.leastStationsPath(start, end);
                showToast(path.isEmpty() ? "No path found (BFS)" : "BFS Path: " + path);
            } catch (Exception e) {
                showToast("Invalid input");
            }
        });

        astarButton.setOnClickListener(v -> {
            try {
                int start = Integer.parseInt(startStationField.getText().toString());
                int end = Integer.parseInt(endStationField.getText().toString());

                ArrayList<Integer> path = UserManager.current_user.subway.optimalPath(start, end, new HaversineHeuristic());
                showToast(path.isEmpty() ? "No path found (A*)" : "A* Path: " + path);
            } catch (Exception e) {
                showToast("Invalid input");
            }
        });

        return root;
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show();
    }
}
