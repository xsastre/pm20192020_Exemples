package cide.pm20192020.elsmeusdiscs;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.pm20192020.ilm.elsmeusdiscs.R;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {

    EditText txtGrup, txtDisc;
    ListView llistaDiscs;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtGrup =(EditText)findViewById(R.id.txtGrup);
        txtDisc =(EditText)findViewById(R.id.txtDisc);
        llistaDiscs =(ListView)findViewById(R.id.llistaDiscs);
        db=openOrCreateDatabase("elsMeusDiscs", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS elsMeusDiscs(Grup VARCHAR,Disc VARCHAR);");
        Listar();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

public void Añadir(View v){
    db.execSQL("INSERT INTO elsMeusDiscs VALUES ('"+ txtGrup.getText().toString()+"','"+
                txtDisc.getText().toString()+"')");
    Toast.makeText(this,"S'ha afegit el disc "+ txtDisc.getText().toString(),Toast.LENGTH_LONG).show();
    Listar();
}
public void Borrar(View v){
    try {
        db.execSQL("DELETE FROM elsMeusDiscs WHERE Grup = '" + txtGrup.getText().toString() + "' AND Disc='" +
                txtDisc.getText().toString() + "'");
        Toast.makeText(this, "S'ha eliminat el disc " + txtDisc.getText().toString(), Toast.LENGTH_LONG).show();
    }
    catch(SQLException s){
        Toast.makeText(this, "Error esborrant!", Toast.LENGTH_LONG).show();
    }
    Listar();
}
public void Listar(){
    ArrayAdapter<String> adaptador;
    List<String> lista = new ArrayList<String>();
    Cursor c=db.rawQuery("SELECT * FROM elsMeusDiscs", null);
    if(c.getCount()==0)
        lista.add("No hi ha registres");
    else{
        while(c.moveToNext())
            lista.add(c.getString(0)+"-"+c.getString(1));
    }
    adaptador=new ArrayAdapter<String>(getApplicationContext(),R.layout.llista_fila,lista);
    llistaDiscs.setAdapter(adaptador);
}
}
