package caso.victor_arroyo.dropboxepubs;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
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

	private final String dbPath = "/Photos/";

	private final ProgressDialog pDialog;

	private boolean canceled;
	private String errorMsg;

    private FileOutputStream mFos;
    
    private DBManager dbm;

	@SuppressWarnings("deprecation")
	public EpubsDownloader(Context context, DropboxAPI<?> api) {
		this.context = context;
		this.api = api;
		
		// Creamos una instacia de la base de datos
		dbm = new DBManager(context);

		pDialog = new ProgressDialog(context);
		pDialog.setMessage("Descargando EPUBs");
		pDialog.setButton("Cancel", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				canceled = true;
				errorMsg = "Canceled";

				// This will cancel the getThumbnail operation by closing
				// its stream
				/*
				 * if (mFos != null) { try { mFos.close(); } catch (IOException
				 * e) { } }
				 */
			}
		});

		pDialog.show();

	}

	@Override
	protected Boolean doInBackground(Void... arg0) {

		// Comprobamos si el usuario ha cancelado la descarga
		if (canceled) {
			return false;
		}

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
					// Add it to the list of thumbs we can choose from
					miniaturas.add(ent);
				}
			}

			// Comprobamos si el usuario ha cancelado la descarga
			if (canceled) {
				return false;
			}

			if (miniaturas.size() == 0) {
				// No hay mediaturas
				errorMsg = "No hay libros en el directorio.";
				Log.e("EpubsDownloader:doInBackground", errorMsg);
				return false;
			}

			for(int i=0;i<miniaturas.size();i++){
				Entry ent = miniaturas.get(i);
	            String path = ent.path;
	            String name = ent.fileName();
	            String fileName = ent.fileName();
	            String date = "fecha";
	            String nombreFichero = "dropboxepub" + i + ".png";
	            String cachePath = context.getCacheDir().getAbsolutePath() + "/" + nombreFichero;
	            Epub libro = new Epub(name, date, fileName, path, cachePath);
	            dbm.addEpub(libro);

	            try {
	                mFos = new FileOutputStream(cachePath);
	            } catch (FileNotFoundException e) {
	                errorMsg = "Couldn't create a local file to store the image";
	                return false;
	            }

	            // This downloads a smaller, thumbnail version of the file.  The
	            // API to download the actual file is roughly the same.
	            api.getThumbnail(path, mFos, ThumbSize.BESTFIT_960x640,
	                    ThumbFormat.JPEG, null);
	            if (canceled) {
	                return false;
	            }	            
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
			Intent intent = new Intent(context,
					BookListActivity.class);

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

}
