package caso.victor_arroyo.dropboxepubs;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;

import android.app.Application;

public class GlobalStuff extends Application{
	
	String nombre;
	DropboxAPI<AndroidAuthSession> mApi;
	boolean logueado = false;

	public boolean isLogueado() {
		return logueado;
	}

	public void setLogueado(boolean logueado) {
		this.logueado = logueado;
	}

	public DropboxAPI<AndroidAuthSession> getmApi() {
		return mApi;
	}

	public void setmApi(DropboxAPI<AndroidAuthSession> mApi) {
		this.mApi = mApi;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	

}
