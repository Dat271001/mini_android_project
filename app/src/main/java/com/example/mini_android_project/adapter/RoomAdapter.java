package com.example.mini_android_project.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mini_android_project.R;
import com.example.mini_android_project.model.Room;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {

    private List<Room> roomList;
    private OnRoomClickListener listener;

    public interface OnRoomClickListener {
        void onItemClick(int position);
        void onItemLongClick(int position);
    }

    public RoomAdapter(List<Room> roomList, OnRoomClickListener listener) {
        this.roomList = roomList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_room, parent, false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        Room room = roomList.get(position);

        holder.tvRoomId.setText(room.getRoomId());
        holder.tvRoomName.setText(room.getRoomName());

        NumberFormat fmt = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
        holder.tvPrice.setText(fmt.format(room.getPrice()) + " đ/tháng");

        holder.tvStatus.setText(room.getStatusText());

        if (room.isAvailable()) {
            holder.tvStatus.setTextColor(Color.parseColor("#2E7D32"));
            holder.tvStatus.setBackgroundResource(R.drawable.bg_status_available);
            holder.cardView.setCardBackgroundColor(Color.parseColor("#F1F8E9"));
        } else {
            holder.tvStatus.setTextColor(Color.parseColor("#C62828"));
            holder.tvStatus.setBackgroundResource(R.drawable.bg_status_rented);
            holder.cardView.setCardBackgroundColor(Color.parseColor("#FFEBEE"));
        }

        holder.itemView.setOnClickListener(v -> {
            int pos = holder.getBindingAdapterPosition();
            if (pos != RecyclerView.NO_ID) listener.onItemClick(pos);
        });
        holder.itemView.setOnLongClickListener(v -> {
            int pos = holder.getBindingAdapterPosition();
            if (pos != RecyclerView.NO_ID) listener.onItemLongClick(pos);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    public static class RoomViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView tvRoomId, tvRoomName, tvPrice, tvStatus;

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            tvRoomId = itemView.findViewById(R.id.tvRoomId);
            tvRoomName = itemView.findViewById(R.id.tvRoomName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
}
