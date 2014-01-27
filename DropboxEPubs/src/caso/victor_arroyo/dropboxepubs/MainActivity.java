package caso.victor_arroyo.dropboxepubs;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);

		// Obtenemos los botones y los campos de texto

		final Button btnEntrar = (Button) findViewById(R.id.BtnLogin);
		final Button btnBorrar = (Button) findViewById(R.id.BtnBorrar);
		final EditText et_email = (EditText) findViewById(R.id.et_email);
		final EditText et_pass = (EditText) findViewById(R.id.et_pass);
		
		// Implementamos la accion ejecutada al pulsar el boton ENTRAR
		btnEntrar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//Obtenemos los valores introducidos en ambos campos de texto
				String sEmail = et_email.getText().toString().trim();
				String sPass = et_pass.getText().toString().trim();
				
				//Comprobamos que los campos no están vacios
				if((!sEmail.equals(""))&&(!sPass.equals(""))){
					Intent intent = new Intent(MainActivity.this,
							BookListActivity.class);

					// Creamos la información a pasar entre actividades
					Bundle b = new Bundle();			
					b.putString("NOMBRE", "Hola actividad nueva");

					// Añadimos la información al intent
					intent.putExtras(b);

					// Iniciamos la nueva actividad
					startActivity(intent);
				}
				else{
					Context context1 = getApplicationContext();
					CharSequence text = "Debes introducir tus datos en ambos campos de texto.";
					int duration = Toast.LENGTH_SHORT;

					Toast toast = Toast.makeText(context1, text, duration);
					toast.show();
				}
			}
		});

		// Implementamos la accion ejecutada al pulsar el boton BORRAR
		btnBorrar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				et_email.setText("");
				et_pass.setText("");
			}
		});
		return true;
	}

}
