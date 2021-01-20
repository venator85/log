package eu.alessiobianchi.log.demo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import eu.alessiobianchi.log.Log;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.e("Ciao", "msg");
		Log.e("Ciao", new Exception(), "msg");

		Log.e("Ciao");
		Log.e("Ciao", new Exception());

	}
}
