package caso.victor_arroyo.dropboxepubs;

import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class BookListFragment extends Fragment {

	private ListView lstListado;
	private Epub[] datos = new Epub[1000];
	private LibrosListener listener;
	private DBManager dbm;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.booklistfragment, container, false);
	}

	@Override
	public void onActivityCreated(Bundle state) {
		super.onActivityCreated(state);

		// Obtenemos la lista de libros ordenados por nombre
		List<Epub> libros;
		dbm = new DBManager(getActivity());
		libros = dbm.getEpubsList();
		datos = new Epub[libros.size()];
		// La copiamos al array que se mostrará en el fragment
		for (int i = 0; i < libros.size(); i++) {
			datos[i] = libros.get(i);
			Log.d("BookListFragment:onActivityCreated", "Elemento copiado: "
					+ i);
		}
		Log.d("BookListFragment:onActivityCreated",
				"Tamaño lista: " + libros.size());

		lstListado = (ListView) getView().findViewById(R.id.LstListadoLibros);

		lstListado.setAdapter(new AdaptadorLibros(this));

		lstListado.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> list, View view, int pos,
					long id) {
				if (listener != null) {
					listener.onLibroSeleccionado((Epub) lstListado.getAdapter()
							.getItem(pos));
				}
			}
		});
	}

	public void actualizaListaPorNombre() {

		List<Epub> libros;
		libros = dbm.getEpubsList();
		datos = new Epub[libros.size()];
		for (int i = 0; i < libros.size(); i++) {
			datos[i] = libros.get(i);
			Log.d("BookListFragment:actualizaListaPorNombre", "Elemento copiado: "
					+ i);
		}
		Log.d("BookListFragment:actualizaListaPorNombre",
				"Tamaño lista: " + libros.size());

		lstListado = (ListView) getView().findViewById(R.id.LstListadoLibros);

		lstListado.setAdapter(new AdaptadorLibros(this));

		lstListado.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> list, View view, int pos,
					long id) {
				if (listener != null) {
					listener.onLibroSeleccionado((Epub) lstListado.getAdapter()
							.getItem(pos));
				}
			}
		});
	}

	public void actualizaListaPorFechaAsc() {
		List<Epub> libros;
		libros = dbm.getEpubsListFechaAsc();
		datos = new Epub[libros.size()];
		for (int i = 0; i < libros.size(); i++) {
			datos[i] = libros.get(i);
			Log.d("BookListFragment:actualizaListaPorFechaAsc", "Elemento copiado: "
					+ i);
		}
		Log.d("BookListFragment:actualizaListaPorFechaAsc",
				"Tamaño lista: " + libros.size());

		lstListado = (ListView) getView().findViewById(R.id.LstListadoLibros);

		lstListado.setAdapter(new AdaptadorLibros(this));

		lstListado.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> list, View view, int pos,
					long id) {
				if (listener != null) {
					listener.onLibroSeleccionado((Epub) lstListado.getAdapter()
							.getItem(pos));
				}
			}
		});
	}

	public void actualizaListaPorFechaDesc() {

		List<Epub> libros;
		libros = dbm.getEpubsListFechaDesc();
		datos = new Epub[libros.size()];
		for (int i = 0; i < libros.size(); i++) {
			datos[i] = libros.get(i);
			Log.d("BookListFragment:actualizaListaPorFechaDesc", "Elemento copiado: "
					+ i);
		}
		Log.d("BookListFragment:actualizaListaPorFechaDesc",
				"Tamaño lista: " + libros.size());

		lstListado = (ListView) getView().findViewById(R.id.LstListadoLibros);

		lstListado.setAdapter(new AdaptadorLibros(this));

		lstListado.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> list, View view, int pos,
					long id) {
				if (listener != null) {
					listener.onLibroSeleccionado((Epub) lstListado.getAdapter()
							.getItem(pos));
				}
			}
		});
	}

	class AdaptadorLibros extends ArrayAdapter<Epub> {

		Activity context;

		AdaptadorLibros(Fragment context) {
			super(context.getActivity(), R.layout.fragment_book_list, datos);
			this.context = context.getActivity();
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = context.getLayoutInflater();
			View item = inflater.inflate(R.layout.fragment_book_list, null);

			TextView lblNombre = (TextView) item.findViewById(R.id.LblNombre);
			lblNombre.setText(datos[position].getNombre());

			TextView lblFecha = (TextView) item.findViewById(R.id.LblFecha);
			lblFecha.setText(datos[position].getFecha().toString());

			return (item);
		}
	}

	public interface LibrosListener {
		void onLibroSeleccionado(Epub l);
	}

	public void setLibrosListener(LibrosListener listener) {
		this.listener = listener;
	}
}
