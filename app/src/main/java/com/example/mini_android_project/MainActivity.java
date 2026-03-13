package com.example.roomrentalmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roomrentalmanagement.adapter.RoomAdapter;
import com.example.roomrentalmanagement.controller.RoomController;
import com.example.roomrentalmanagement.model.Room;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RoomAdapter roomAdapter;
    private RoomController roomController;
    private TextView tvRoomCount;
    private FloatingActionButton fabAddRoom;

    private final ActivityResultLauncher<Intent> addEditLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Intent data = result.getData();
                    String mode = data.getStringExtra(AddEditRoomActivity.EXTRA_MODE);
                    String roomId = data.getStringExtra(AddEditRoomActivity.EXTRA_ROOM_ID);
                    String roomName = data.getStringExtra(AddEditRoomActivity.EXTRA_ROOM_NAME);
                    double price = data.getDoubleExtra(AddEditRoomActivity.EXTRA_PRICE, 0);
                    boolean isAvailable = data.getBooleanExtra(AddEditRoomActivity.EXTRA_IS_AVAILABLE, true);
                    String tenantName = data.getStringExtra(AddEditRoomActivity.EXTRA_TENANT_NAME);
                    String phone = data.getStringExtra(AddEditRoomActivity.EXTRA_PHONE);

                    Room room = new Room(roomId, roomName, price, isAvailable, tenantName, phone);

                    if (AddEditRoomActivity.MODE_ADD.equals(mode)) {
                        if (roomController.isRoomIdExists(roomId)) {
                            Snackbar.make(recyclerView, "Mã phòng đã tồn tại!", Snackbar.LENGTH_LONG).show();
                            return;
                        }
                        roomController.addRoom(room);
                        roomAdapter.notifyItemInserted(roomController.getRoomList().size() - 1);
                        Snackbar.make(recyclerView, "Đã thêm phòng thành công!", Snackbar.LENGTH_SHORT).show();
                    } else if (AddEditRoomActivity.MODE_EDIT.equals(mode)) {
                        int position = data.getIntExtra(AddEditRoomActivity.EXTRA_POSITION, -1);
                        if (position >= 0) {
                            if (roomController.isRoomIdExistsExcept(roomId, position)) {
                                Snackbar.make(recyclerView, "Mã phòng đã tồn tại!", Snackbar.LENGTH_LONG).show();
                                return;
                            }
                            roomController.updateRoom(position, room);
                            roomAdapter.notifyItemChanged(position);
                            Snackbar.make(recyclerView, "Đã cập nhật phòng thành công!", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                    updateRoomCount();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        roomController = new RoomController();

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recyclerView);
        tvRoomCount = findViewById(R.id.tvRoomCount);
        fabAddRoom = findViewById(R.id.fabAddRoom);

        roomAdapter = new RoomAdapter(roomController.getRoomList(), new RoomAdapter.OnRoomClickListener() {
            @Override
            public void onItemClick(int position) {
                openEditRoom(position);
            }

            @Override
            public void onItemLongClick(int position) {
                showDeleteDialog(position);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(roomAdapter);

        updateRoomCount();

        fabAddRoom.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddEditRoomActivity.class);
            intent.putExtra(AddEditRoomActivity.EXTRA_MODE, AddEditRoomActivity.MODE_ADD);
            addEditLauncher.launch(intent);
        });
    }

    private void openEditRoom(int position) {
        Room room = roomController.getRoomList().get(position);
        Intent intent = new Intent(this, AddEditRoomActivity.class);
        intent.putExtra(AddEditRoomActivity.EXTRA_MODE, AddEditRoomActivity.MODE_EDIT);
        intent.putExtra(AddEditRoomActivity.EXTRA_POSITION, position);
        intent.putExtra(AddEditRoomActivity.EXTRA_ROOM_ID, room.getRoomId());
        intent.putExtra(AddEditRoomActivity.EXTRA_ROOM_NAME, room.getRoomName());
        intent.putExtra(AddEditRoomActivity.EXTRA_PRICE, room.getPrice());
        intent.putExtra(AddEditRoomActivity.EXTRA_IS_AVAILABLE, room.isAvailable());
        intent.putExtra(AddEditRoomActivity.EXTRA_TENANT_NAME, room.getTenantName());
        intent.putExtra(AddEditRoomActivity.EXTRA_PHONE, room.getPhoneNumber());
        addEditLauncher.launch(intent);
    }

    private void showDeleteDialog(int position) {
        Room room = roomController.getRoomList().get(position);
        new AlertDialog.Builder(this)
                .setTitle("Xóa phòng")
                .setMessage("Bạn có chắc muốn xóa \"" + room.getRoomName() + "\" không?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    roomController.deleteRoom(position);
                    roomAdapter.notifyItemRemoved(position);
                    roomAdapter.notifyItemRangeChanged(position, roomController.getRoomList().size());
                    updateRoomCount();
                    Snackbar.make(recyclerView, "Đã xóa phòng!", Snackbar.LENGTH_SHORT).show();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void updateRoomCount() {
        tvRoomCount.setText("Tổng: " + roomController.getRoomList().size() + " phòng");
    }
}
