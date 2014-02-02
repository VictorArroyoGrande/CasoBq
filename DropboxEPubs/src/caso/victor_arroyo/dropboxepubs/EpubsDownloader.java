package caso.victor_arroyo.dropboxepubs;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.epub.EpubReader;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.DropboxFileInfo;
import com.dropbox.client2.DropboxAPI.Entry;
import com.dropbox.client2.exception.DropboxException;

public class EpubsDownloader extends AsyncTask<Void, Long, Boolean> {

	private Context context;
	private DropboxAPI<?> api;

	private final String dbPath = "/Epubs/";

	private final ProgressDialog pDialog;
	private String errorMsg;

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

			// De todos los contenidos del directorio, nos quedamos con los de
			// extension .epub
			ArrayList<Entry> epubs = new ArrayList<Entry>();
			for (Entry ent : md.contents) {
				if (ent.fileName().endsWith(".epub")) {
					// Lo insertamos en la lista
					epubs.add(ent);
				}
			}

			if (epubs.size() == 0) {
				// No hay epubs
				errorMsg = "No hay libros en el directorio.";
				Log.e("EpubsDownloader:doInBackground", errorMsg);
				return false;
			}

			// Tratamos los contenidos uno a uno
			for (int i = 0; i < epubs.size(); i++) {

				Entry ent = epubs.get(i);

				// Creamos un fichero en la cache del dispositivo
				String nombreFichero = "dropboxepub" + i + ".epub";
				String cachePath = context.getCacheDir().getAbsolutePath()
						+ "/" + nombreFichero;

				File file = new File(cachePath);
				OutputStream out = null;
				try {
					out = new BufferedOutputStream(new FileOutputStream(file));
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}

				try {
					// Descargamos el archivo de extension .epub y lo
					// almacenamos en el fichero que hemos creado
					DropboxFileInfo info = api.getFile(ent.path, null, out,
							null);

					Log.d("EpubsDownloader:doInBackground",
							"El epub se añadió: "
									+ info.getMetadata().clientMtime);

					// Obtenemos un objeto de tipo Book a partir del fichero
					// creado
					InputStream is = null;
					try {
						is = new FileInputStream(file);
						Book book = (new EpubReader()).readEpub(is);

						// Creamos un objeto de tipo Epub gracias a los medios
						// que nos proporciona el de tipo Book
						String titulo = book.getTitle();
						Log.d("EpubsDownloader:doInBackground", "Titulo: "
								+ titulo);
						String fecha = info.getMetadata().clientMtime;
						Log.d("EpubsDownloader:doInBackground", "Fecha: "
								+ fecha);

						/*
						DateFormat df = DateFormat.getDateInstance();
						try {
							Date date = (Date) df.parse(fecha);
							Log.d("EpubsDownloader:doInBackground", "Fecha 2: "
									+ date.toString());
						} catch (ParseException e) {
							Log.e("EpubsDownloader:doInBackground", "Error de formato en la fecha");
						}
						*/
						
						Epub libro = new Epub(titulo, generaFechaAleatoria(
								1600, 2014), ent.fileName(), ent.path,
								cachePath);

						// Y lo insertamos en la base de datos
						dbm.addEpub(libro);

						// Cerramos el fichero
						is.close();
					} catch (IOException e) {
						Log.e("EpubsDownloader:doInBackground",
								"Error al almacenar el epub.");
						try {
							is.close();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}

				} catch (DropboxException e) {
					Log.e("EpubsDownloader:doInBackground",
							"Error de descarga.");
					file.delete();
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
			Intent intent = new Intent(context, BookListActivity.class);

			// Creamos la información a pasar entre actividades
			Bundle b = new Bundle();
			b.putString("NOMBRE", "");

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

	public java.sql.Date generaFechaAleatoria(int yearMin, int yearMax) {
		Calendar cal = Calendar.getInstance();

		int año, mes;
		int dia = 0;
		año = (int) ((yearMax - yearMin + 1) * Math.random()) + yearMin;
		mes = (int) (12 * Math.random());

		if (mes == 2) {// Mes de febrero
			if (año % 400 == 0 || año % 4 == 0) {// es bisiesto
				dia = (int) (29 * Math.random());
			} else {// No es año bisiesto
				dia = (int) (28 * Math.random());
			}
		} else {
			if (mes == 1 || mes == 3 || mes == 5 || mes == 7 || mes == 8
					|| mes == 10 || mes == 12) {
				// Mes de 31 días
				dia = (int) (31 * Math.random());
			} else {// Mes de 30 días
				dia = (int) Math.random();
			}
		}
		// set Date portion to January 1, 1970
		cal.set(Calendar.YEAR, año);
		cal.set(Calendar.MONTH, mes);
		cal.set(Calendar.DATE, dia);

		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		return new java.sql.Date(cal.getTime().getTime());
	}
}
