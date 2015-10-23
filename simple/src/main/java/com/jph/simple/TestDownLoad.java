package com.jph.simple;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.jph.putils.http.DownloadHandler;

public class TestDownLoad extends FragmentActivity {

	private static final String TAG = TestDownLoad.class.getSimpleName();

	private ProgressBar mProgressBar;
	private Button start;
	private Button pause;
	private Button delete;
	private Button reset;
	private TextView total;

	private ImageView image;

	private int max;

	private DownloadHandler mDownloadUtil;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_download);
		mProgressBar = (ProgressBar) findViewById(R.id.progressBar1);
		start = (Button) findViewById(R.id.button_start);
		pause = (Button) findViewById(R.id.button_pause);
		delete = (Button) findViewById(R.id.button_delete);
		reset = (Button) findViewById(R.id.button_reset);
		total = (TextView) findViewById(R.id.textView_total);
		image = (ImageView) findViewById(R.id.image);

		String urlString = "http://pic.ksudi.com/Ksudi_parttime.apk";
		final String localPath = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/ADownLoadTest";
		mDownloadUtil = new DownloadHandler(2, localPath, "abc.apk", urlString,
				this);
		mDownloadUtil.setOnDownloadListener(new DownloadHandler.OnDownloadListener() {

			@Override
			public void downloadStart(int fileSize) {
				max = fileSize;
				mProgressBar.setMax(fileSize);
			}

			@Override
			public void downloadProgress(int downloadedSize) {
				mProgressBar.setProgress(downloadedSize);
				total.setText((int) downloadedSize * 100 / max + "%");
			}

			@Override
			public void downloadEnd() {
//				Bitmap bitmap = decodeSampledBitmapFromResource(localPath
//						+ File.separator + "abc.jpg", 200, 200);
//				image.setImageBitmap(bitmap);
			}
		});
		start.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mDownloadUtil.start();
			}
		});
		pause.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mDownloadUtil.pause();
			}
		});
		delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mDownloadUtil.delete();
				mProgressBar.setProgress(0);
				total.setText("0%");
				image.setImageBitmap(null);
			}
		});
		reset.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mDownloadUtil.reset();
				mProgressBar.setProgress(0);
				total.setText("0%");
				image.setImageBitmap(null);
			}
		});

	}

	public static Bitmap decodeSampledBitmapFromResource(String fileName,
			int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(fileName, options);
		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);
		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(fileName, options);
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and
			// keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}

}
