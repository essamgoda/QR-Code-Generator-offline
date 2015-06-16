package com.essam.qrcodegenerator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

@SuppressWarnings("deprecation")
public class MainActivity extends ActionBarActivity {

	ImageView iv;
	EditText tv;
	String text = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	public void getQRCode(View v) {
		try {

			tv = (EditText) findViewById(R.id.editText);
			text = String.valueOf(tv.getText());
			if (text != null) {
				// Create a Writter
				QRCodeWriter writer = new QRCodeWriter();
				// Generate a QR Code in BitMatrix
				BitMatrix bitMatrix = writer.encode(text,
						BarcodeFormat.QR_CODE, 200, 200);

				// add it to the image view

				iv = (ImageView) findViewById(R.id.imageView);
				iv.setImageBitmap(toBitmap(bitMatrix));
				Button get = (Button) findViewById(R.id.button);
				get.setVisibility(View.INVISIBLE);
				Button save = (Button) findViewById(R.id.button2);
				save.setVisibility(View.VISIBLE);
			} else {
				Toast.makeText(getApplicationContext(), "Text can't be empty",
						Toast.LENGTH_LONG).show();
			}
		} catch (WriterException writerException) {
			Log.d("LOG", writerException.toString());
		}

	}

	// convert the BitMatrix to Bitmap
	public static Bitmap toBitmap(BitMatrix matrix) {
		int height = matrix.getHeight();
		int width = matrix.getWidth();
		Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				bmp.setPixel(x, y, matrix.get(x, y) ? Color.BLACK : Color.WHITE);
			}
		}
		return bmp;
	}

	public void saveQRCode(View v) {
		String te = String.valueOf(tv.getText());
		iv.setDrawingCacheEnabled(true);
		Bitmap b = iv.getDrawingCache();

		File fPath = Environment.getExternalStorageDirectory();
		File f = null;
		f = new File(fPath, "/" + Environment.DIRECTORY_PICTURES + "/"
				+ "QR Code " + te + " .png");
		if (f.exists()) {
			f = new File(fPath, "/" + Environment.DIRECTORY_PICTURES + "/"
					+ "QR Code " + te + " (1) " + " .png");
		}
		try {
			FileOutputStream strm = new FileOutputStream(f);
			b.compress(CompressFormat.PNG, 80, strm);
			strm.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Button get = (Button) findViewById(R.id.button);
		get.setVisibility(View.VISIBLE);
		Button save = (Button) findViewById(R.id.button2);
		save.setVisibility(View.INVISIBLE);
		tv.setText("");
		iv.setImageBitmap(null);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_about) {
			AlertDialog about = new AlertDialog.Builder(this).create();
			about.setTitle("About ...");
			about.setMessage(getResources().getString(R.string.about));

			about.setIcon(R.drawable.about);
			about.show();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
