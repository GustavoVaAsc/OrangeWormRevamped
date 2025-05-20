package unam.fi.ai.orangewormrevamped.ui.calculatetime;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import unam.fi.ai.orangewormrevamped.MainActivity;
import unam.fi.ai.orangewormrevamped.R;
import unam.fi.ai.orangewormrevamped.appobjects.Route;
import unam.fi.ai.orangewormrevamped.appobjects.UserManager;
import unam.fi.ai.orangewormrevamped.appobjects.heuristics.HaversineHeuristic;

public class CalculateTransferFragment extends Fragment {

    private TextView resultText;
    private ArrayList<Integer> lastRoute;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_calculate_transfer, container, false);

        EditText startStationField = root.findViewById(R.id.startStation);
        EditText endStationField = root.findViewById(R.id.endStation);
        Button bfsButton = root.findViewById(R.id.bfsButton);
        Button astarButton = root.findViewById(R.id.astarButton);
        Button saveButton = root.findViewById(R.id.saveRouteButton);
        resultText = root.findViewById(R.id.resultText);

        bfsButton.setOnClickListener(v -> {
            try {
                String st_station = startStationField.getText().toString();
                String end_station = endStationField.getText().toString();

                int start = UserManager.current_user.subway.queryReverseDB(st_station);
                int end = UserManager.current_user.subway.queryReverseDB(end_station);

                lastRoute = UserManager.current_user.subway.leastStationsPath(start, end);
                displayRoute(lastRoute, "Ruta con menos estaciones", 0);
                UserManager.current_user.subway.resetPredeccesors();
            } catch (Exception e) {
                showToast("Revise el nombre de las estaciones");
            }
        });

        astarButton.setOnClickListener(v -> {
            try {
                String st_station = startStationField.getText().toString();
                String end_station = endStationField.getText().toString();

                int start = UserManager.current_user.subway.queryReverseDB(st_station);
                int end = UserManager.current_user.subway.queryReverseDB(end_station);

                lastRoute = UserManager.current_user.subway.optimalPath(start, end, new HaversineHeuristic());
                lastRoute.removeIf(id -> id == 0);

                displayRoute(lastRoute, "Ruta con menor tiempo de traslado",UserManager.current_user.subway.queryDistance(end));
                UserManager.current_user.subway.resetPredeccesors();
            } catch (Exception e) {
                e.printStackTrace();
                showToast("Revise el nombre de las estaciones");
            }
        });

        saveButton.setOnClickListener(v -> {
            if (lastRoute != null && !lastRoute.isEmpty()) {
                Route route = new Route("Saved Route " + (UserManager.current_user.getNumberOfRoutes() + 1), lastRoute);
                UserManager.current_user.addNewRoute(route);
                showToast("Ruta guardada con éxito");
            } else {
                showToast("No hay una ruta por guardar");
            }
        });

        return root;
    }

    private void displayRoute(ArrayList<Integer> path, String method,Integer time) {
        if (path == null || path.isEmpty()) {
            resultText.setText("No se encontró una ruta (" + method + ")");
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(method).append(":\n");
            for (int stationId : path) {
                String name = UserManager.current_user.subway.queryByNameOnDB(stationId);
                sb.append(name).append(" -> ");
            }
            sb.setLength(sb.length() - 4); // Remove last arrow
            sb.append("\n\n").append("Tiempo de traslado: ").append(time);
            resultText.setText(sb.toString());
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
