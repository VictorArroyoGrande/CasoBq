package caso.victor_arroyo.dropboxepubs;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.Entry;
import com.dropbox.client2.DropboxAPI.ThumbFormat;
import com.dropbox.client2.DropboxAPI.ThumbSize;
import com.dropbox.client2.exception.DropboxException;

public class EpubsDownloader extends AsyncTask<Void, Long, Boolean> {

	private Context context;
	private DropboxAPI<?> api;

	private final String dbPath = "/Epubs/";

	private final ProgressDialog pDialog;
	private String errorMsg;

	private FileOutputStream mFos;

	private DBManager dbm;

	public EpubsDownloader(Context context, DropboxAPI<?> api) {
		this.context = context;
		this.api = api;

		// Creamos una instacia de la base de datos
		dbm = new DBManager(context);

		pDialog = new ProgressDialog(context);
		pDialog.setMessage("Descargando EPUBs");
		pDialog.show();

	}

	@Override
	protected Boolean doInBackground(Void... arg0) {

		try {
			// Obtenemos los metadatos de la ruta especificada
			Entry md = api.metadata(dbPath, 1000, null, true, null);

			// Comprobamos que existe el directorio y que no esta vacio
			if ((!md.isDir) || (md.contents == null)) {
				errorMsg = "No existe el directorio o está vacío.";
				Log.e("EpubsDownloader:doInBackground", errorMsg);
				return false;
			}

			// De todos los contenidos del directorio, nos quedamos con los que
			// tengan miniatura
			ArrayList<Entry> miniaturas = new ArrayList<Entry>();
			for (Entry ent : md.contents) {
				if (ent.thumbExists) {
					// Lo insertamos en la lista
					miniaturas.add(ent);
				}
			}

			if (miniaturas.size() == 0) {
				// No hay mediaturas
				errorMsg = "No hay libros en el directorio.";
				Log.e("EpubsDownloader:doInBackground", errorMsg);
				return false;
			}

			// Tratamos los contenidos uno a uno
			for (int i = 0; i < miniaturas.size(); i++) {

				// Creamos un objeto Epub con las caracteristicas del archivo
				// tratado
				Entry ent = miniaturas.get(i);
				String path = ent.path;
				String name = ent.fileName();
				String fileName = ent.fileName();
				String nombreFichero = "dropboxepub" + i + ".png";
				String cachePath = context.getCacheDir().getAbsolutePath()
						+ "/" + nombreFichero;				
			    
				Epub libro = new Epub(name, generaFechaAleatoria(1600,2014), fileName,
						path, cachePath);
				// Y lo insertamos en la base de datos
				dbm.addEpub(libro);

				try {
					mFos = new FileOutputStream(cachePath);
				} catch (FileNotFoundException e) {
					errorMsg = "Couldn't create a local file to store the image";
					return false;
				}

				// Descargamos una miniatura que se encuentra en path y la
				// almacenamos en la caché en el fichero mFos con formato JPEG
				api.getThumbnail(path, mFos, ThumbSize.BESTFIT_960x640,
						ThumbFormat.JPEG, null);
			}

			return true;

		} catch (DropboxException e) {
			errorMsg = "DropboxException";
			Log.e("EpubsDownloader:doInBackground",
					"DropboxException: " + e.getLocalizedMessage());
			return false;
		}
	}

	@Override
	protected void onPostExecute(Boolean result) {
		pDialog.dismiss();

		CharSequence text = "";
		int duration = Toast.LENGTH_SHORT;

		if (result) {
			// Descarga correcta
			text = "Descarga completa.";
			Log.i("EpubsDownloader:onPostExecute", (String) text);

			// Lanzamos la actividad que muestra los libros descargados
			Intent intent = new Intent(context, BookListActivity.class);

			// Creamos la información a pasar entre actividades
			Bundle b = new Bundle();
			b.putString("NOMBRE", "Hola actividad nueva");

			// Añadimos la información al intent
			intent.putExtras(b);

			// Iniciamos la nueva actividad
			context.startActivity(intent);
		} else {
			// Ha ocurrido un error
			text = errorMsg;
			Log.e("EpubsDownloader:onPostExecute", errorMsg);
		}

		// Mostramos el mensaje
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}

	public java.sql.Date generaFechaAleatoria(int yearMin, int yearMax){
		Calendar cal = Calendar.getInstance();
		
		int año, mes;
        int dia = 0;
        año = (int) ((yearMax - yearMin + 1) * Math.random()) + yearMin;
        mes = (int) (12 * Math.random());
	    
        if (mes == 2) {//Mes de febrero
            if (año % 400 == 0 || año % 4 == 0) {//es bisiesto
                dia = (int) (29 * Math.random());
            } else {//No es año bisiesto
                dia = (int) (28 * Math.random());
            }
        } else {
            if (mes == 1 || mes == 3 || mes == 5 || mes == 7 || mes == 8 || mes == 10 || mes == 12) {
                //Mes de 31 días
                dia = (int) (31 * Math.random());
            } else {//Mes de 30 días
                dia = (int) Math.random();
            }
        } 
	    // set Date portion to January 1, 1970
	    cal.set( Calendar.YEAR, año );
	    cal.set( Calendar.MONTH, mes );
	    cal.set( Calendar.DATE, dia );
	    
	    cal.set( Calendar.HOUR_OF_DAY, 0 );
	    cal.set( Calendar.MINUTE, 0 );
	    cal.set( Calendar.SECOND, 0 );
	    cal.set( Calendar.MILLISECOND, 0 );
	    
	    return new java.sql.Date( cal.getTime().getTime() );
	}
}
