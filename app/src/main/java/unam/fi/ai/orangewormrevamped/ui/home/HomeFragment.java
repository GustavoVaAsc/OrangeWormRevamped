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
import unam.fi.ai.orangewormrevamped.databinding.FragmentHomeBinding;


class Station {
    String name;
    int id;

    public Station(String name, int id) {
        this.name = name;
        this.id = id;
    }
}

class StationAdapter extends RecyclerView.Adapter<StationAdapter.StationViewHolder> {

    public interface OnStationClickListener {
        void onStationClick(HomeFragment.Station station);
    }

    private List<HomeFragment.Station> stationList;
    private OnStationClickListener listener;

    public StationAdapter(List<HomeFragment.Station> stationList, OnStationClickListener listener) {
        this.stationList = stationList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public StationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_route, parent, false);
        return new StationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StationViewHolder holder, int position) {
        HomeFragment.Station station = stationList.get(position);
        holder.stationName.setText(station.name);

        // 🔥 Set click listener
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onStationClick(station);
            }
        });
    }

    @Override
    public int getItemCount() {
        return stationList.size();
    }

    public static class StationViewHolder extends RecyclerView.ViewHolder {
        TextView stationName;

        public StationViewHolder(@NonNull View itemView) {
            super(itemView);
            stationName = itemView.findViewById(R.id.stationName);
        }
    }
}



public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public static class Station {
        String name;
        int id;

        public Station(String name, int id) {
            this.name = name;
            this.id = id;
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Create stations
        List<Station> stationList = new ArrayList<>();
        stationList.add(new Station("Observatorio", 1));
        stationList.add(new Station("Tacubaya", 2));
        stationList.add(new Station("Chapultepec", 3));

        // Create and assign adapter with listener
        StationAdapter adapter = new StationAdapter(stationList, station -> {
            // 💥 Handle click here
            Toast.makeText(getContext(), "Clicked: " + station.name, Toast.LENGTH_SHORT).show();
        });

        binding.stationRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.stationRecyclerView.setAdapter(adapter);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
