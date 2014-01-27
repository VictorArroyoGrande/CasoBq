package caso.victor_arroyo.dropboxepubs;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class BookDetails extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_book_detail);
		
		//Obtenemos el elemento de texto al que queremos dar valor
		final TextView tv_nombre = (TextView) findViewById(R.id.tv_book_name);

		//Obtenemos los valores que hemos asignado al crear la actividad
		Bundle bundle = this.getIntent().getExtras();
		
		tv_nombre.setText(bundle.getString("NOMBRE"));
	}

}