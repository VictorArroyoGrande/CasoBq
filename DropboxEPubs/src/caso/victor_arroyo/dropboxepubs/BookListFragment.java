package caso.victor_arroyo.dropboxepubs;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class BookListFragment extends Fragment{

	private ListView lstListado;
	//private Libro[] datos = new Libro[1000];
	private Libro[] datos = {new Libro("nombre1","fecha1"),new Libro("nombre2","fecha2"),new Libro("nombre3","fecha3"),new Libro("nombre4","fecha4"),new Libro("nombre5","fecha5"),new Libro("nombre6","fecha6"),new Libro("nombre7","fecha7")};
	private LibrosListener listener;
	
	 @Override
	    public View onCreateView(LayoutInflater inflater,
	                             ViewGroup container,
	                             Bundle savedInstanceState) {
	 
	        return inflater.inflate(R.layout.booklistfragment, container, false);
	    }
	 
	    @Override
	    public void onActivityCreated(Bundle state) {
	        super.onActivityCreated(state);
	        
	        lstListado = (ListView)getView().findViewById(R.id.LstListadoLibros);
	 
	        lstListado.setAdapter(new AdaptadorLibros(this));
	        
	        lstListado.setOnItemClickListener(new OnItemClickListener() {
	            @Override
	            public void onItemClick(AdapterView<?> list, View view, int pos, long id) {
	                if (listener!=null) {
	                    listener.onLibroSeleccionado(
	                        (Libro)lstListado.getAdapter().getItem(pos));
	                }
	            }
	        });
	    }
	    
	    public void actualizaLista() {
	    	
	    	/*
        
	      	lstListado = (ListView)getView().findViewById(R.id.LstListadoLibros);
	 
	        lstListado.setAdapter(new AdaptadorLibros(this));
	        
	        lstListado.setOnItemClickListener(new OnItemClickListener() {
	            @Override
	            public void onItemClick(AdapterView<?> list, View view, int pos, long id) {
	                if (listener!=null) {
	                    listener.onLibroSeleccionado(
	                        (Libro)lstListado.getAdapter().getItem(pos));
	                }
	            }
	        });*/
	    }
	 
	    class AdaptadorLibros extends ArrayAdapter<Libro> {
	 
	            Activity context;
	 
	            AdaptadorLibros(Fragment context) {
	                super(context.getActivity(), R.layout.fragment_book_list, datos);
	                this.context = context.getActivity();
	            }
	 
	            public View getView(int position, View convertView, ViewGroup parent) {
	            LayoutInflater inflater = context.getLayoutInflater();
	            View item = inflater.inflate(R.layout.fragment_book_list, null);
	 
	            TextView lblNombre = (TextView)item.findViewById(R.id.LblNombre);
	            lblNombre.setText(datos[position].getNombre());
	            
	            TextView lblFecha = (TextView)item.findViewById(R.id.LblFecha);
	            lblFecha.setText(datos[position].getFecha());
	 
	            return(item);
	        }
	        }
	    
	    public interface LibrosListener {
	        void onLibroSeleccionado(Libro l);
	    }
	 
	    public void setLibrosListener(LibrosListener listener) {
	        this.listener=listener;
	    }
	}

