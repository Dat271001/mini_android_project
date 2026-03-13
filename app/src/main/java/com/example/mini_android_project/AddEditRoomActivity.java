package com.example.roomrentalmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;

public class AddEditRoomActivity extends AppCompatActivity {

    public static final String EXTRA_MODE = "mode";
    public static final String EXTRA_POSITION = "position";
    public static final String EXTRA_ROOM_ID = "room_id";
    public static final String EXTRA_ROOM_NAME = "room_name";
    public static final String EXTRA_PRICE = "price";
    public static final String EXTRA_IS_AVAILABLE = "is_available";
    public static final String EXTRA_TENANT_NAME = "tenant_name";
    public static final String EXTRA_PHONE = "phone";
    public static final String MODE_ADD = "add";
    public static final String MODE_EDIT = "edit";

    private TextInputEditText etRoomId, etRoomName, etPrice, etTenantName, etPhone;
    private RadioGroup rgStatus;
    private RadioButton rbAvailable, rbRented;
    private LinearLayout layoutTenantInfo;
    private Button btnSave;
    private MaterialToolbar toolbar;

    private String mode = MODE_ADD;
    private int editPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_room);

        initViews();
        setupToolbar();
        setupStatusListener();
        loadIntentData();

        btnSave.setOnClickListener(v -> saveRoom());
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        etRoomId = findViewById(R.id.etRoomId);
        etRoomName = findViewById(R.id.etRoomName);
        etPrice = findViewById(R.id.etPrice);
        etTenantName = findViewById(R.id.etTenantName);
        etPhone = findViewById(R.id.etPhone);
        rgStatus = findViewById(R.id.rgStatus);
        rbAvailable = findViewById(R.id.rbAvailable);
        rbRented = findViewById(R.id.rbRented);
        layoutTenantInfo = findViewById(R.id.layoutTenantInfo);
        btnSave = findViewById(R.id.btnSave);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupStatusListener() {
        rgStatus.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbRented) {
                layoutTenantInfo.setVisibility(View.VISIBLE);
            } else {
                layoutTenantInfo.setVisibility(View.GONE);
            }
        });
    }

    private void loadIntentData() {
        Intent intent = getIntent();
        mode = intent.getStringExtra(EXTRA_MODE);
        if (mode == null) mode = MODE_ADD;

        if (MODE_EDIT.equals(mode)) {
            toolbar.setTitle("Sửa thông tin phòng");
            editPosition = intent.getIntExtra(EXTRA_POSITION, -1);

            etRoomId.setText(intent.getStringExtra(EXTRA_ROOM_ID));
            etRoomId.setEnabled(false); // không cho sửa mã phòng khi edit
            etRoomName.setText(intent.getStringExtra(EXTRA_ROOM_NAME));
            double price = intent.getDoubleExtra(EXTRA_PRICE, 0);
            etPrice.setText(price == (long) price ? String.valueOf((long) price) : String.valueOf(price));

            boolean isAvailable = intent.getBooleanExtra(EXTRA_IS_AVAILABLE, true);
            if (isAvailable) {
                rbAvailable.setChecked(true);
            } else {
                rbRented.setChecked(true);
                layoutTenantInfo.setVisibility(View.VISIBLE);
                etTenantName.setText(intent.getStringExtra(EXTRA_TENANT_NAME));
                etPhone.setText(intent.getStringExtra(EXTRA_PHONE));
            }
        }
    }

    private void saveRoom() {
        String roomId = etRoomId.getText() != null ? etRoomId.getText().toString().trim() : "";
        String roomName = etRoomName.getText() != null ? etRoomName.getText().toString().trim() : "";
        String priceStr = etPrice.getText() != null ? etPrice.getText().toString().trim() : "";
        boolean isAvailable = rbAvailable.isChecked();
        String tenantName = etTenantName.getText() != null ? etTenantName.getText().toString().trim() : "";
        String phone = etPhone.getText() != null ? etPhone.getText().toString().trim() : "";

        // Validate
        if (roomId.isEmpty()) {
            etRoomId.setError("Vui lòng nhập mã phòng");
            etRoomId.requestFocus();
            return;
        }
        if (roomName.isEmpty()) {
            etRoomName.setError("Vui lòng nhập tên phòng");
            etRoomName.requestFocus();
            return;
        }
        if (priceStr.isEmpty()) {
            etPrice.setError("Vui lòng nhập giá thuê");
            etPrice.requestFocus();
            return;
        }
        double price;
        try {
            price = Double.parseDouble(priceStr);
            if (price <= 0) {
                etPrice.setError("Giá thuê phải lớn hơn 0");
                etPrice.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            etPrice.setError("Giá thuê không hợp lệ");
            etPrice.requestFocus();
            return;
        }

        if (!isAvailable && tenantName.isEmpty()) {
            etTenantName.setError("Vui lòng nhập tên người thuê");
            etTenantName.requestFocus();
            return;
        }

        // Gửi kết quả về MainActivity
        Intent result = new Intent();
        result.putExtra(EXTRA_MODE, mode);
        result.putExtra(EXTRA_POSITION, editPosition);
        result.putExtra(EXTRA_ROOM_ID, roomId);
        result.putExtra(EXTRA_ROOM_NAME, roomName);
        result.putExtra(EXTRA_PRICE, price);
        result.putExtra(EXTRA_IS_AVAILABLE, isAvailable);
        result.putExtra(EXTRA_TENANT_NAME, tenantName);
        result.putExtra(EXTRA_PHONE, phone);

        setResult(RESULT_OK, result);
        finish();
    }
}
