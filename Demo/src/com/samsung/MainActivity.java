package com.samsung;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

public class MainActivity extends Activity {

	private static final int SELECT_PICTURE = 1;

	private String selectedImagePath;
	private Context context;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.intro);
		context = this;

		((Button) findViewById(R.id.button1))
		.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(Intent.createChooser(intent,
						context.getResources().getString(R.string.select)), 
						SELECT_PICTURE);
			}
		});
		
		((Button) findViewById(R.id.button2))
		.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				if (selectedImagePath == null)
					return;
				Intent intent = new Intent(context, ImgActivity.class);
				intent.putExtra("path", selectedImagePath); 
				intent.putExtra("decay", ((TextView) findViewById(R.id.editText1)).getText());
				intent.putExtra("radius", ((TextView) findViewById(R.id.editText2)).getText());
				intent.putExtra("height", ((TextView) findViewById(R.id.editText3)).getText());
				context.startActivity(intent);
			}
		});
	}
	
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("path", selectedImagePath);
        // etc. -- the same for input fields
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
    	selectedImagePath = savedInstanceState.getString("path");
    	((TextView) findViewById(R.id.textView1)).setText(selectedImagePath);
        super.onRestoreInstanceState(savedInstanceState);
    }

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == SELECT_PICTURE) {
				Uri selectedImageUri = data.getData();
				selectedImagePath = getPath(selectedImageUri);
				((TextView) findViewById(R.id.textView1)).setText(
						selectedImagePath);

			}
		}
	}

	public String getPath(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}
}