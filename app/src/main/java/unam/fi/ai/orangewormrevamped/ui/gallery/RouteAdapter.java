package unam.fi.ai.orangewormrevamped.ui.gallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import unam.fi.ai.orangewormrevamped.R;
import unam.fi.ai.orangewormrevamped.appobjects.Route;
import unam.fi.ai.orangewormrevamped.appobjects.Station;
import unam.fi.ai.orangewormrevamped.appobjects.UserManager;

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.RouteViewHolder> {
    private final List<Route> routeList;
    private final OnRouteClickListener listener;

    public interface OnRouteClickListener {
        void onRouteClick(Route route);
    }

    public RouteAdapter(List<Route> routes, OnRouteClickListener listener) {
        this.routeList = routes;
        this.listener = listener;
    }

    public static class RouteViewHolder extends RecyclerView.ViewHolder {
        ImageView startIcon, endIcon;
        TextView routeName;
        private Bitmap loadStationLogo(Context context, String logoFile) {
            try {
                InputStream inputStream = context.getAssets().open(logoFile);
                return BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        public RouteViewHolder(View itemView) {
            super(itemView);
            startIcon = itemView.findViewById(R.id.startStationIcon);
            endIcon = itemView.findViewById(R.id.endStationIcon);
            routeName = itemView.findViewById(R.id.routeName);
        }

        public void bind(Route route, OnRouteClickListener listener, Context context) {
            routeName.setText(route.getName());

            int start_id = route.queryStationList(0);
            int end_id = route.queryStationList(route.getNumberOfStations() - 1);

            Station startStation = UserManager.current_user.subway.queryStation(start_id);
            Station endStation = UserManager.current_user.subway.queryStation(end_id);

            loadImageFromAssets(context, startStation.getLogo_file(), startIcon);
            loadImageFromAssets(context, endStation.getLogo_file(), endIcon);

            itemView.setOnClickListener(v -> listener.onRouteClick(route));
        }

        private void loadImageFromAssets(Context context, String path, ImageView imageView) {
            try (InputStream is = context.getAssets().open(path)) {
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                imageView.setImageResource(R.drawable.ic_station); // fallback icon
            }
        }

    }

    @NonNull
    @Override
    public RouteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.route_item, parent, false);
        return new RouteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RouteViewHolder holder, int position) {
        holder.bind(routeList.get(position), listener, holder.itemView.getContext());
    }


    @Override
    public int getItemCount() {
        return routeList.size();
    }
}
