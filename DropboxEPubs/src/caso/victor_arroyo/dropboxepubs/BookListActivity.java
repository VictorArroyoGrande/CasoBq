package caso.victor_arroyo.dropboxepubs;

import caso.victor_arroyo.dropboxepubs.BookListFragment.LibrosListener;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class BookListActivity extends FragmentActivity implements
		LibrosListener {
	protected static final String EXTRA_TEXT = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_book_list);

		BookListFragment frgListadoLibros = (BookListFragment) getSupportFragmentManager()
				.findFragmentById(R.id.FrgListadoLibros);

		frgListadoLibros.setLibrosListener(this);

		// Inicializamos el spinner con los valores propuestos
		final String[] datos = new String[] { "Nombre", "Fecha" };

		ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, datos);

		Spinner cmbOpciones;

		cmbOpciones = (Spinner) findViewById(R.id.CmbOpcionesOrden);

		adaptador
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		cmbOpciones.setAdapter(adaptador);

		// Obtenemos el boton que hay que pulsar para ordenar
		final Button btnOrdenar = (Button) findViewById(R.id.BtnOrdenar);
		
		// Implementamos la accion ejecutada al pulsar el boton IR
		btnOrdenar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
			}
		});
	}

	@Override
	public void onLibroSeleccionado(Libro l) {
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

		// Añadimos la información al intent
		intent.putExtras(b);

		// Iniciamos la nueva actividad
		startActivity(intent);

	}

}
