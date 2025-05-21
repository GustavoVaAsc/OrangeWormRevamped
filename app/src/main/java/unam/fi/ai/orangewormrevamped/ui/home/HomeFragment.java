package unam.fi.ai.orangewormrevamped.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;


import java.util.List;
import java.util.ArrayList;

import unam.fi.ai.orangewormrevamped.R;
import unam.fi.ai.orangewormrevamped.appobjects.Station;
import unam.fi.ai.orangewormrevamped.appobjects.User;
import unam.fi.ai.orangewormrevamped.appobjects.UserManager;
import unam.fi.ai.orangewormrevamped.databinding.FragmentHomeBinding;



public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    User user = UserManager.current_user;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        //TextView stationText = root.findViewById(R.id.stationTextView);

        if (UserManager.current_user != null) {
            StringBuilder output = new StringBuilder();
            for (Station station : UserManager.current_user.getStation_db().values()) {
                output.append(station.getName()).append(" (ID: ").append(station.getId()).append(")\n");
            }
            //stationText.setText(output.toString());
        } else {
            //stationText.setText("No user loaded.");
        }

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
