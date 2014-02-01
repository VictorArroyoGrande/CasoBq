package caso.victor_arroyo.dropboxepubs;

import caso.victor_arroyo.dropboxepubs.BookListFragment.LibrosListener;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class BookListActivity extends FragmentActivity implements
		LibrosListener {
	protected static final String EXTRA_TEXT = null;

	// Indicador del parámetro por el que se encuentra ordenada la lista de
	// Epubs
	// 0 = por nombre
	// 1 = por fecha ascendente
	// 2 = por fecha descendente
	int ordenadoPor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_book_list);

		BookListFragment frgListadoLibros = (BookListFragment) getSupportFragmentManager()
				.findFragmentById(R.id.FrgListadoLibros);

		frgListadoLibros.setLibrosListener(this);

		// Inicializamos el spinner con los valores propuestos
		final String[] datos = new String[] { "Nombre", "Fecha ascendente",
				"Fecha descendente" };

		ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, datos);

		final Spinner cmbOpciones;

		cmbOpciones = (Spinner) findViewById(R.id.CmbOpcionesOrden);

		adaptador
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		cmbOpciones.setAdapter(adaptador);

		// Inicialmente siempre va a estar ordenado por nombre
		ordenadoPor = 0;

		// Obtenemos el boton que hay que pulsar para ordenar
		final Button btnOrdenar = (Button) findViewById(R.id.BtnOrdenar);

		// Implementamos la accion ejecutada al pulsar el boton IR
		btnOrdenar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (cmbOpciones.getSelectedItem().toString().equals("Nombre")) {
					if (ordenadoPor != 0) {
						Log.i("BookListActivity",
								"Reordenando la lista por nombre.");
						((BookListFragment) getSupportFragmentManager()
								.findFragmentById(R.id.FrgListadoLibros))
								.actualizaListaPorNombre();

						ordenadoPor = 0;
					}
				} else if (cmbOpciones.getSelectedItem().toString()
						.equals("Fecha ascendente")) {
					if (ordenadoPor != 1) {
						Log.i("BookListActivity",
								"Reordenando la lista por fecha ascendente.");
						((BookListFragment) getSupportFragmentManager()
								.findFragmentById(R.id.FrgListadoLibros))
								.actualizaListaPorFechaAsc();
						ordenadoPor = 1;
					}
				} else {
					if (ordenadoPor != 2) {
						Log.i("BookListActivity",
								"Reordenando la lista por fecha descendente.");
						((BookListFragment) getSupportFragmentManager()
								.findFragmentById(R.id.FrgListadoLibros))
								.actualizaListaPorFechaDesc();
						ordenadoPor = 2;
					}
				}
			}
		});
	}

	@Override
	public void onLibroSeleccionado(Epub l) {
		Context context1 = getApplicationContext();
		CharSequence text = "Pulsado libro: " + l.getNombre() + " - "
				+ l.getFecha();
		int duration = Toast.LENGTH_LONG;

		Toast toast = Toast.makeText(context1, text, duration);
		toast.show();

		Intent intent = new Intent(BookListActivity.this, BookDetails.class);

		// Creamos la información a pasar entre actividades
		Bundle b = new Bundle();
		b.putString("NOMBRE", l.getNombre());
		b.putString("FECHA", l.getFecha().toString());
		b.putString("CACHEPATH", l.getCachePath());

		// Añadimos la información al intent
		intent.putExtras(b);

		// Iniciamos la nueva actividad
		startActivity(intent);

	}

}
