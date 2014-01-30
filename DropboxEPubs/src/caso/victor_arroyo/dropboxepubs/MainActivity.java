package caso.victor_arroyo.dropboxepubs;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.TokenPair;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	private DropboxAPI<AndroidAuthSession> mApi;
	private DropBoxController dbc;
	GlobalStuff myApp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Creamos una instacia de DropboxController que vamos a utilizar para
		// llevar a cabo el proceso de login con Dropbox
		dbc = new DropBoxController(getApplicationContext());
		
		myApp = ((GlobalStuff)getApplicationContext());

		// Creamos una AuthSession para poder usar la API de Dropbox
		AndroidAuthSession session = dbc.buildSession();
		mApi = new DropboxAPI<AndroidAuthSession>(session);

		dbc.checkAppKeySetup();

		setContentView(R.layout.activity_main);

		// Obtenemos los botones y los campos de texto

		final Button btnEntrar = (Button) findViewById(R.id.BtnLogin);
		final Button btnBorrar = (Button) findViewById(R.id.BtnBorrar);
		final EditText et_email = (EditText) findViewById(R.id.et_email);
		final EditText et_pass = (EditText) findViewById(R.id.et_pass);		

		// Implementamos la accion ejecutada al pulsar el boton ENTRAR
		btnEntrar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Obtenemos los valores introducidos en ambos campos de texto
				String sEmail = et_email.getText().toString().trim();
				String sPass = et_pass.getText().toString().trim();

				// Comprobamos que los campos no están vacios
				if ((!sEmail.equals("")) && (!sPass.equals(""))) {

					// Comenzamos la autenticacion a través del navegador
					mApi.getSession().startAuthentication(MainActivity.this);
				} else {
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
	}

	@Override
	protected void onResume() {
		super.onResume();

		// Esta parte se ejecuta cuando se ha terminado de autenticar mediante
		// el navegador

		AndroidAuthSession session = mApi.getSession();

		if (session.authenticationSuccessful()) {
			try {
				// Completamos la autenticacion
				session.finishAuthentication();

				// Almacenamos los token para que puedan ser utilizados mas
				// tarde
				TokenPair tokens = session.getAccessTokenPair();
				dbc.storeKeys(tokens.key, tokens.secret);
				// setLoggedIn(true);
				
						
				EpubsDownloader downDialog = new EpubsDownloader(MainActivity.this, mApi);
				downDialog.execute();
				
				myApp.setNombre("hola soy viti");
				myApp.setmApi(mApi);
				
				/*
				// Lanzamos la actividad que muestra los libros descargados 
				Intent intent = new Intent(MainActivity.this,
						BookListActivity.class);

				// Creamos la información a pasar entre actividades
				Bundle b = new Bundle();
				b.putString("NOMBRE", "Hola actividad nueva");

				// Añadimos la información al intent
				intent.putExtras(b);

				// Iniciamos la nueva actividad
				startActivity(intent);*/
								
			} catch (IllegalStateException e) {
				Context context1 = getApplicationContext();
				CharSequence text = "No se ha podido autenticar. Error: " + e.getLocalizedMessage();
				int duration = Toast.LENGTH_SHORT;

				Toast toast = Toast.makeText(context1, text, duration);
				toast.show();
				Log.i("MainActivity:onResume", "Error authenticating", e);
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);

		return true;
	}

}
