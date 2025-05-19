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

import unam.fi.ai.orangewormrevamped.MainActivity;
import unam.fi.ai.orangewormrevamped.R;
import unam.fi.ai.orangewormrevamped.appobjects.UserManager;
import unam.fi.ai.orangewormrevamped.appobjects.heuristics.HaversineHeuristic;

public class CalculateTransferFragment extends Fragment {

    private EditText startStationField;
    private EditText endStationField;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_calculate_transfer, container, false);

        startStationField = root.findViewById(R.id.startStation);
        endStationField = root.findViewById(R.id.endStation);
        Button bfsButton = root.findViewById(R.id.bfsButton);
        Button astarButton = root.findViewById(R.id.astarButton);

        bfsButton.setOnClickListener(v -> handleRouteCalculation(true));
        astarButton.setOnClickListener(v -> handleRouteCalculation(false));

        return root;
    }

    private void handleRouteCalculation(boolean useBFS) {
        String startName = startStationField.getText().toString().trim();
        String endName = endStationField.getText().toString().trim();

        if (startName.isEmpty() || endName.isEmpty()) {
            showToast("Please enter both station names.");
            return;
        }

        try {
            int startId = UserManager.current_user.subway.queryReverseDB(startName);
            int endId = UserManager.current_user.subway.queryReverseDB(endName);

            ArrayList<Integer> path = useBFS
                    ? UserManager.current_user.subway.leastStationsPath(startId, endId)
                    : UserManager.current_user.subway.optimalPath(startId, endId, new HaversineHeuristic());

            if (path.isEmpty()) {
                showToast(useBFS ? "No path found (BFS)." : "No path found (A*).");
            } else {
                showToast((useBFS ? "BFS Path: " : "A* Path: ") + path);
            }
        } catch (Exception e) {
            showToast("Invalid station name.");
        }
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setCalculateTransferFragmentOpen(false);
        }
    }
}
