package unam.fi.ai.orangewormrevamped.ui.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import unam.fi.ai.orangewormrevamped.appobjects.UserManager;
import unam.fi.ai.orangewormrevamped.databinding.FragmentGalleryBinding;
import unam.fi.ai.orangewormrevamped.ui.routedetails.RouteDetailsActivity;

public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RecyclerView routeRecyclerView = binding.routeRecyclerView;
        routeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        RouteAdapter adapter = new RouteAdapter(UserManager.current_user.getSavedRoutes(), route -> {
            Intent intent = new Intent(getContext(), RouteDetailsActivity.class);
            intent.putIntegerArrayListExtra("station_ids", new ArrayList<>(route.getStation_list()));
            startActivity(intent);
        });


        routeRecyclerView.setAdapter(adapter);

        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}