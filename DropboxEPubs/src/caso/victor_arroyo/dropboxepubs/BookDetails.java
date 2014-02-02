package caso.victor_arroyo.dropboxepubs;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.epub.EpubReader;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class BookDetails extends Activity {

	private Drawable mDrawable;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_book_detail);

		// Obtenemos los elementos que vamos a utilizar
		final TextView tv_nombre = (TextView) findViewById(R.id.tv_book_name);
		final ImageView imageView = (ImageView) findViewById(R.id.image_view);

		// Le damos valor al campo de texto
		Bundle bundle = this.getIntent().getExtras();
		tv_nombre.setText(bundle.getString("NOMBRE"));

		// Obtenemos el archivo .epub a partir de la ruta donde lo almacenamos
		// al descargarlo
		String cachePath = bundle.getString("CACHEPATH");
		File file = new File(cachePath);
		InputStream is = null;
		try {
			is = new FileInputStream(file);
			Book book = (new EpubReader()).readEpub(is);

			String titulo = book.getTitle();
			Log.d("BookDetails:onCreate", "Título libro seleccionado: " + titulo);

			// Le asignamos la ruta de la imagen que cache que se desea mostrar
			mDrawable = Drawable.createFromStream(book.getCoverImage()
					.getInputStream(), cachePath);
			imageView.setImageDrawable(mDrawable);

			is.close();

		} catch (IOException e) {
			Log.e("BookDetails:onCreate", "Error al obtener el libro.");
			try {
				is.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

}