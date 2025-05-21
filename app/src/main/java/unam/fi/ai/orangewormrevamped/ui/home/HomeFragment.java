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
        HashMap<String, Integer> times = UserManager.current_user.getTransfer_times(); // Keys like "1", "2", ..., "12", "A", "B"

        if (times != null) {
            gridLayout.setColumnCount(4); // 4 blocks per row
            gridLayout.removeAllViews();

            // List of line names (sorted as you prefer)
            String[] lineKeys = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "12"};

            for (int i = 0; i < lineKeys.length; i++) {
                String line = lineKeys[i];

                TextView block = new TextView(getContext());

                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 0;
                params.height = dpToPx(60);
                params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
                params.setMargins(8, 8, 8, 8);
                block.setLayoutParams(params);

                Integer time = times.get(line);
                String displayText = (time != null) ? time + " min" : "N/A";
                block.setText(line + "\n" + displayText);
                block.setGravity(Gravity.CENTER);
                block.setTextColor(Color.BLACK);

                if (time != null) {
                    if (time < 5) block.setBackgroundColor(Color.parseColor("#A5D6A7")); // green
                    else if (time < 10) block.setBackgroundColor(Color.parseColor("#FFF59D")); // yellow
                    else block.setBackgroundColor(Color.parseColor("#EF9A9A")); // red
                } else {
                    block.setBackgroundColor(Color.LTGRAY); // unknown
                }

                gridLayout.addView(block);
            }
        }

        // --- 2. RECOMMENDED ROUTES LIST ---
        RecyclerView routeRecyclerView = root.findViewById(R.id.routeRecyclerView);
        routeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        RouteAdapter adapter = new RouteAdapter(UserManager.current_user.getSavedRoutes(), route -> {
            Toast.makeText(getContext(), "Clicked: " + route.getName(), Toast.LENGTH_SHORT).show();
        });

        routeRecyclerView.setAdapter(adapter);

        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

}
