package unam.fi.ai.orangewormrevamped.ui.home;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;


import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import unam.fi.ai.orangewormrevamped.R;
import unam.fi.ai.orangewormrevamped.appobjects.Station;
import unam.fi.ai.orangewormrevamped.appobjects.User;
import unam.fi.ai.orangewormrevamped.appobjects.UserManager;
import unam.fi.ai.orangewormrevamped.databinding.FragmentHomeBinding;
import unam.fi.ai.orangewormrevamped.ui.gallery.RouteAdapter;


public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    User user = UserManager.current_user;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_transfer_widget, container, false);

        // --- 1. TRANSFER TIME GRID ---
        GridLayout gridLayout = root.findViewById(R.id.transferTimeGrid);
        HashMap<String, Integer> times = UserManager.current_user.getTransfer_times();

        if (times != null) {
            gridLayout.setColumnCount(4);
            gridLayout.removeAllViews();

            String[] lineKeys = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "12"};

            for (String line : lineKeys) {
                TextView block = new TextView(getContext());

                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 0;
                params.height = dpToPx(80);  // Slightly taller for three lines of text
                params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
                params.setMargins(8, 8, 8, 8);
                block.setLayoutParams(params);

                Integer time = times.get(line);
                String displayText = (time != null) ? "Linea\n" + line + "\n" + time + " min" : "Linea\n" + line + "\nN/A";
                block.setText(displayText);
                block.setGravity(Gravity.CENTER);
                block.setTextColor(Color.BLACK);

                block.setBackgroundColor(getLineColor(line));
                gridLayout.addView(block);
            }
        }

        // --- 2. RECOMMENDED ROUTES LIST ---
        RecyclerView routeRecyclerView = root.findViewById(R.id.routeRecyclerView);
        routeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        RouteAdapter adapter = new RouteAdapter(UserManager.current_user.getSavedRoutes(), route ->
                Toast.makeText(getContext(), "Clicked: " + route.getName(), Toast.LENGTH_SHORT).show()
        );

        routeRecyclerView.setAdapter(adapter);

        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Clear GridLayout views if it exists
        View root = getView();
        if (root != null) {
            GridLayout gridLayout = root.findViewById(R.id.transferTimeGrid);
            if (gridLayout != null) {
                gridLayout.removeAllViews();
            }

            RecyclerView routeRecyclerView = root.findViewById(R.id.routeRecyclerView);
            if (routeRecyclerView != null) {
                routeRecyclerView.setAdapter(null);
            }
        }

        // Nullify binding reference
        binding = null;
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    private int getLineColor(String line) {
        switch (line) {
            case "1":
                return Color.parseColor("#f54890"); // Pink
            case "2":
                return Color.parseColor("#0a5aa6"); // Blue
            case "3":
                return Color.parseColor("#b4ae04"); // Olive Green
            case "4":
                return Color.parseColor("#B2FFFF"); // Light Aqua
            case "5":
                return Color.parseColor("#FFFF00"); // Yellow
            case "6":
                return Color.parseColor("#FF0000"); // Red
            case "7":
                return Color.parseColor("#ff6309"); // Orange
            case "8":
                return Color.parseColor("#06884a"); // Aqua Green
            case "9":
                return Color.parseColor("#A52A2A"); // Brown
            case "A":
                return Color.parseColor("#800080"); // Purple
            case "B":
                return Color.parseColor("#808080"); // Gray
            case "12":
                return Color.parseColor("#b89d4d"); // Gold
            default:
                return Color.LTGRAY; // Fallback color
        }
    }

    public void clearScreen() {
        if (getView() != null) {
            // Clear GridLayout
            GridLayout gridLayout = getView().findViewById(R.id.transferTimeGrid);
            if (gridLayout != null) {
                gridLayout.removeAllViews();
            }

            // Clear RecyclerView
            RecyclerView recyclerView = getView().findViewById(R.id.routeRecyclerView);
            if (recyclerView != null) {
                recyclerView.setAdapter(null);
            }

            // Clear TextViews
            TextView greeting = getView().findViewById(R.id.legendRoutes);
            if (greeting != null) {
                greeting.setText("");
            }

            greeting = getView().findViewById(R.id.legendTransfer);
            if (greeting != null) {
                greeting.setText("");
            }


        }
    }
}