package caso.victor_arroyo.dropboxepubs;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
		final ImageView imageView = (ImageView)findViewById(R.id.image_view);
		
		// Le damos valor al campo de texto 
		Bundle bundle = this.getIntent().getExtras();
		tv_nombre.setText(bundle.getString("NOMBRE"));
		
		// Le asignamos la ruta de la imagen que cache que se desea mostrar
		mDrawable = Drawable.createFromPath(bundle.getString("CACHEPATH"));		
		imageView.setImageDrawable(mDrawable);
				
	}

}