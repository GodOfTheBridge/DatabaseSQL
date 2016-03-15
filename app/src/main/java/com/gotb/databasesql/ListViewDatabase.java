package com.gotb.databasesql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


public class ListViewDatabase extends AppCompatActivity implements View.OnCreateContextMenuListener{
    private ListView listViewDB;
    private SQLController sqlController;
    private SimpleCursorAdapter simpleCursorAdapter;
    private Cursor cursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_database);
        listViewDB = (ListView) findViewById(R.id.lvDatabase);

        sqlController = new SQLController(this);
        listViewDB.setOnCreateContextMenuListener(this);

        buildListDB();
    }


    private void buildListDB(){
        SQLiteDatabase sqLiteDatabase = sqlController.openDatabaseGetWritableDatabase();
        cursor = sqLiteDatabase.query(Database.TABLE_NAME, null, null, null, null, null, null);

        String[] from = new String[] { Database.COLUMN_ID, Database.COLUMN_NAME, Database.COLUMN_TELEPHONE};
        int[] to = new int[] {R.id.tvId, R.id.tvName, R.id.tvTelephone };

        simpleCursorAdapter = new SimpleCursorAdapter(this, R.layout.items, cursor, from, to, 0);
        listViewDB.setAdapter(simpleCursorAdapter);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_context_menu, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo adapterContextMenuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int preId = (int) adapterContextMenuInfo.id;
        String id = Integer.toString(preId);

        switch (item.getItemId()){
            case R.id.itemDelete:
                Toast.makeText(this, id, Toast.LENGTH_SHORT).show();
                sqlController.deleteContactById(id);
                buildListDB();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cursor.close();
        sqlController.closeDatabase();
    }
}
