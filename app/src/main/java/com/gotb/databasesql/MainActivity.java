package com.gotb.databasesql;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnAdd, btnShow, btnDeleteTable, btnUpdate, btnFind, btnDeleteById, btnShowList;
    private EditText editName, editTelephone, editId;
    private RadioButton rbtnName, rbtnId, rbtnTelephone;
    private TableLayout tableLayout;
    private SQLController sqlController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);
        btnShow = (Button) findViewById(R.id.btnShow);
        btnShow.setOnClickListener(this);
        btnDeleteTable = (Button) findViewById(R.id.btnDeleteTable);
        btnDeleteTable.setOnClickListener(this);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(this);
        btnFind = (Button) findViewById(R.id.btnFind);
        btnFind.setOnClickListener(this);
        btnDeleteById = (Button) findViewById(R.id.btnDeleteById);
        btnDeleteById.setOnClickListener(this);
        btnShowList = (Button) findViewById(R.id.btnShowList);
        btnShowList.setOnClickListener(this);

        rbtnId = (RadioButton) findViewById(R.id.radioBtnById);
        rbtnId.setChecked(true);
        rbtnTelephone = (RadioButton) findViewById(R.id.radioBtnByTelephone);
        rbtnName = (RadioButton) findViewById(R.id.radioBtnByName);

        editName = (EditText) findViewById(R.id.editName);
        editTelephone = (EditText) findViewById(R.id.editTelephone);
        editId = (EditText) findViewById(R.id.editId);

        tableLayout = (TableLayout) findViewById(R.id.tableLayout);

        sqlController = new SQLController(this);
    }

    @Override
    public void onClick(View view) {
        sqlController.openDatabaseGetWritableDatabase();
        switch (view.getId()){
            case R.id.btnAdd:
                if (checkFieldsIsEmpty()) {
                    sqlController.addContact(editName.getText().toString(),
                            editTelephone.getText().toString());
                    editName.setText("");
                    editTelephone.setText("");
                    tableLayout.removeAllViews();
                    buildTable();
                }
                editName.requestFocus();
                break;

            case R.id.btnShow:
                tableLayout.removeAllViews();
                buildTable();
                break;

            case R.id.btnDeleteTable:
                sqlController.deleteTable();
                tableLayout.removeAllViews();
                break;

            case R.id.btnUpdate:
                if (checkFieldsIsEmpty()) {
                    sqlController.updateContactById(editId.getText().toString(),
                            editName.getText().toString(),
                            editTelephone.getText().toString());
                    editName.setText("");
                    editTelephone.setText("");
                    editId.setText("");
                    tableLayout.removeAllViews();
                    buildTable();
                }
                editName.requestFocus();
                break;

            case R.id.btnFind:
                sqlController.findContactById(Integer.parseInt(editId.getText().toString()),
                                              editName,editTelephone);
                editName.requestFocus();
                break;

            case R.id.btnDeleteById:
                sqlController.deleteContactById(editId.getText().toString());
                tableLayout.removeAllViews();
                buildTable();
                break;

            case R.id.btnShowList:
                Intent intent = new Intent(this, ListViewDatabase.class);
                startActivity(intent);
                break;
        }
        sqlController.closeDatabase();
    }

    protected Boolean checkFieldsIsEmpty(){
        if (editName.getText().toString().equals("") || editTelephone.getText().toString().equals("")){
            Toast.makeText(this, "Please, input data", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void buildTable (){
        String sortByColumn = Database.COLUMN_ID;
        if (rbtnId.isChecked()){
            sortByColumn = Database.COLUMN_ID;
        }else if (rbtnName.isChecked()){
            sortByColumn = Database.COLUMN_NAME;
        }else if (rbtnTelephone.isChecked()){
            sortByColumn = Database.COLUMN_TELEPHONE;
        }

        SQLiteDatabase sqLiteDatabase = sqlController.openDatabaseGetWritableDatabase();
        Cursor cursor = sqLiteDatabase.query(Database.TABLE_NAME, null, null, null, null, null, sortByColumn);
        int rows = cursor.getCount();
        int columns = cursor.getColumnCount();
        if (cursor != null) {cursor.moveToFirst();}

        for (int i = 0; i < rows; i++) {
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            for (int j = 0; j < columns; j++) {
                TextView textView = new TextView(this);
                textView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                textView.setBackgroundResource(R.drawable.cell_shape);
                textView.setGravity(Gravity.CENTER);
                textView.setTextSize(18);
                textView.setPadding(0, 5, 0, 5);
                textView.setText(cursor.getString(j));
                tableRow.addView(textView);
            }
            cursor.moveToNext();
            tableLayout.addView(tableRow);
        }
        cursor.close();
        sqlController.closeDatabase();
    }

    @Override
    protected void onStart() {
        super.onStart();
        tableLayout.removeAllViews();
        buildTable();
    }

}
