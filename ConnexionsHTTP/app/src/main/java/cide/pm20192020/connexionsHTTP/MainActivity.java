package cide.pm20192020.connexionsHTTP;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends Activity {
    public final String tag="DescarregaHTTP";
    public EditText edURL;
    public TextView txtDescarrega;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void Descarregar(View v){
        edURL=(EditText)findViewById(R.id.edURL);
        txtDescarrega =(TextView) findViewById(R.id.txtDescarga);
        txtDescarrega.setMovementMethod(new ScrollingMovementMethod());

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new DescarregaPaginaWeb().execute(edURL.getText().toString());
        } else {
            edURL.setText("No s'ha pogut establir connexió a Internet");
        }
    }


    private class DescarregaPaginaWeb extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            // els paràmetres venen del mètode execute()
            try {
                return descarregaUrl(urls[0]);
            } catch (IOException e) {
                return "Impossible obtenir pàgina web. La URL pot ser invàlida.";
            }
        }
        // onPostExecute visualitza els resultats de AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            txtDescarrega.setText(result);
        }

        /**
        Aquest mètode llegeix el inputstream i l'aconverteix en una cadena
        amb l'ajud d'un ByteArrayOutputStream()
        */
        private String Llegir(InputStream is) {
            try {
                ByteArrayOutputStream bo = new ByteArrayOutputStream();
                int i = is.read();
                while(i != -1) {
                    bo.write(i);
                    i = is.read();
                }
                return bo.toString();
            } catch (IOException e) {
                return "";
            }
        }

        // Donada una URL, estableix una connexió HttpUrlConnection i retorna
        // el contingut de la pàgina web amb un InputStream, que es transforma en un String.
        private String descarregaUrl(String myurl) throws IOException {
            InputStream is = null;

            try {
                URL url = new URL(myurl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milisegons */);
                conn.setConnectTimeout(15000 /* milisegons */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                // comença la consulta
                conn.connect();
                int response = conn.getResponseCode();
                is = conn.getInputStream();

                // convertir el InputStream a string
                return Llegir(is);

                //Tanquem el inputStream.
            } finally {
                if (is != null) {
                    is.close();
                }
            }
        }

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
}
