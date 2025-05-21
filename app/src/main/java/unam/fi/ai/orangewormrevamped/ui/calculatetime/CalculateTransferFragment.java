package unam.fi.ai.orangewormrevamped.ui.calculatetime;

import android.content.Intent;
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
import java.util.List;

import unam.fi.ai.orangewormrevamped.MainActivity;
import unam.fi.ai.orangewormrevamped.R;
import unam.fi.ai.orangewormrevamped.appobjects.Route;
import unam.fi.ai.orangewormrevamped.appobjects.UserManager;
import unam.fi.ai.orangewormrevamped.appobjects.heuristics.HaversineHeuristic;
import unam.fi.ai.orangewormrevamped.ui.routedetails.RouteDetailsActivity;

public class CalculateTransferFragment extends Fragment {

    private TextView resultText;
    private ArrayList<Integer> lastRoute;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_calculate_transfer, container, false);

        EditText startStationField = root.findViewById(R.id.startStation);
        EditText endStationField = root.findViewById(R.id.endStation);
        EditText routeNameInput = root.findViewById(R.id.routeNameInput);
        Button bfsButton = root.findViewById(R.id.bfsButton);
        Button astarButton = root.findViewById(R.id.astarButton);
        Button saveButton = root.findViewById(R.id.saveRouteButton);
        Button showDetailsButton = root.findViewById(R.id.showDetailsButton);
        resultText = root.findViewById(R.id.resultText);

        bfsButton.setOnClickListener(v -> {
            try {
                String st_station = startStationField.getText().toString();
                String end_station = endStationField.getText().toString();

                String lw_st_station = UserManager.current_user.toLowerSnake(st_station);
                String lw_end_station = UserManager.current_user.toLowerSnake(end_station);

                int start = UserManager.current_user.subway.queryReverseDB(lw_st_station);
                int end = UserManager.current_user.subway.queryReverseDB(lw_end_station);

                lastRoute = UserManager.current_user.subway.leastStationsPath(start, end);
                int total_time = UserManager.current_user.subway.calculateTransferTime(lastRoute, new HaversineHeuristic());
                displayRoute(lastRoute, "Ruta con menos estaciones", total_time);
                UserManager.current_user.subway.resetPredeccesors();
            } catch (Exception e) {
                showToast("Revise el nombre de las estaciones");
            }
        });

        astarButton.setOnClickListener(v -> {
            try {
                String st_station = startStationField.getText().toString();
                String end_station = endStationField.getText().toString();

                String lw_st_station = UserManager.current_user.toLowerSnake(st_station);
                String lw_end_station = UserManager.current_user.toLowerSnake(end_station);

                int start = UserManager.current_user.subway.queryReverseDB(lw_st_station);
                int end = UserManager.current_user.subway.queryReverseDB(lw_end_station);

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
                String routeName = routeNameInput.getText().toString().trim();
                if (routeName.isEmpty()) {
                    routeName = "Ruta guardada " + (UserManager.current_user.getNumberOfRoutes() + 1);
                }
                Route route = new Route(routeName, lastRoute, new ArrayList<Integer>(), new ArrayList<Boolean>());
                UserManager.current_user.addNewRoute(route, requireContext());
                showToast("Ruta guardada como: " + routeName);
            } else {
                showToast("No hay una ruta por guardar");
            }
        });

        showDetailsButton.setOnClickListener(v -> {
            if (lastRoute != null && !lastRoute.isEmpty()) {
                Intent intent = new Intent(requireContext(), RouteDetailsActivity.class);
                intent.putExtra("station_ids", lastRoute);
                startActivity(intent);
            } else {
                showToast("No hay ruta generada para mostrar");
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

        // Clear result text
        if (resultText != null) {
            resultText.setText("");
        }

        // Clear inputs and lastRoute
        lastRoute = null;

        View root = getView();
        if (root != null) {
            EditText startStation = root.findViewById(R.id.startStation);
            EditText endStation = root.findViewById(R.id.endStation);
            EditText routeName = root.findViewById(R.id.routeNameInput);

            if (startStation != null) startStation.setText("");
            if (endStation != null) endStation.setText("");
            if (routeName != null) routeName.setText("");
        }

        // Mark fragment as closed in MainActivity
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setCalculateTransferFragmentOpen(false);
        }
    }


}
