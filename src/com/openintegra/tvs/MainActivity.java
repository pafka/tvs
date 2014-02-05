package com.openintegra.tvs;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager.Request;
import android.content.DialogInterface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

//	private String sURL = "https://android.technovacsystem.com:20443/index.php?sleep=15";
	private String sURL = "https://android.technovacsystem.com:20443/index.php";
//	private String sURL = "http://192.168.1.159/android.technovacsystem.com.htm";
//	private String sURL = "http://10.10.10.224/android.technovacsystem.com.htm";
	private int iExpectedValuesCount = 23;
	private static int iUpdateIntervalInSeconds = 30;
	private static boolean bIsPaused = false;
	private static boolean bIsAlreadySchedulled = false;

	private static boolean bButtonPulloutState = false;
	private static boolean bButtonVacuumingState = false;
	private static boolean bButtonStartProcessState = false;
	private static boolean bButtonQuenchingState = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		new Handler().post(new Runnable() {
			@SuppressLint("NewApi")
			@Override
			public void run() {
				if ( android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN){
					((Button) findViewById(R.id.button_details_pullout)).setBackground(Sd(getResources().getColor(R.color.gray)));
					((Button) findViewById(R.id.button_vacuuming)).setBackground(Sd(getResources().getColor(R.color.gray)));
					((Button) findViewById(R.id.button_go_preferences)).setBackground(Sd(getResources().getColor(R.color.gray)));
					((Button) findViewById(R.id.button_start_stop_process)).setBackground(Sd(getResources().getColor(R.color.gray)));
				} else {
					((Button) findViewById(R.id.button_details_pullout)).setBackgroundDrawable(Sd(getResources().getColor(R.color.gray)));
					((Button) findViewById(R.id.button_vacuuming)).setBackgroundDrawable(Sd(getResources().getColor(R.color.gray)));
					((Button) findViewById(R.id.button_go_preferences)).setBackgroundDrawable(Sd(getResources().getColor(R.color.gray)));
					((Button) findViewById(R.id.button_start_stop_process)).setBackgroundDrawable(Sd(getResources().getColor(R.color.gray)));
				}
			}
		});


		((Button) findViewById(R.id.button_details_pullout))
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						new AlertDialog.Builder(v.getContext())
								.setMessage(R.string.button_confirm_details_out)
								.setCancelable(false)
								.setNegativeButton(R.string.cancel,
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												// TODO Auto-generated method
												// stub

											}
										})
								.setPositiveButton(R.string.ok,
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int id) {
												List<NameValuePair> params = new ArrayList<NameValuePair>();
												params.add(new BasicNameValuePair(
														"details_pull_out",
														bButtonPulloutState ? "0"
																: "1"));
												new DownloadFilesTask()
														.execute(params);
											}
										}).create().show();
					}
				});

		((Button) findViewById(R.id.button_vacuuming))
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						new AlertDialog.Builder(v.getContext())
								.setMessage(R.string.button_confirm_vacuuming)
								.setCancelable(false)
								.setNegativeButton(R.string.cancel,
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												// TODO Auto-generated method
												// stub

											}
										})
								.setPositiveButton(R.string.ok,
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int id) {
												List<NameValuePair> params = new ArrayList<NameValuePair>();
												params.add(new BasicNameValuePair(
														"vacuuming",
														bButtonVacuumingState ? "0"
																: "1"));
												new DownloadFilesTask()
														.execute(params);
											}
										}).create().show();
					}
				});
		
		((Button) findViewById(R.id.button_start_stop_process))
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						new AlertDialog.Builder(v.getContext())
								.setMessage(R.string.button_confirm_start_stop_process)
								.setCancelable(false)
								.setNegativeButton(R.string.cancel,
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												// TODO Auto-generated method
												// stub

											}
										})
								.setPositiveButton(R.string.ok,
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int id) {
												List<NameValuePair> params = new ArrayList<NameValuePair>();
												params.add(new BasicNameValuePair(
														"start_stop_process",
														bButtonStartProcessState ? "0"
																: "1"));
												new DownloadFilesTask()
														.execute(params);
											}
										}).create().show();
					}
				});

		((LinearLayout) findViewById(R.id.linearlayout_scrollcontainer)).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (0 == iUpdateIntervalInSeconds) {
					new DownloadFilesTask().execute();
				}
			}
		});
	}

	private void fSchedulleReload() {
		if ( bIsAlreadySchedulled || bIsPaused )
			return;
		
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				bIsAlreadySchedulled = true;
				new DownloadFilesTask().execute();
			}
		}, 1000 * iUpdateIntervalInSeconds);
	}
	
	@Override
	protected void onPause() {
		bIsPaused = true;
		super.onPause();
	}

	@Override
	protected void onResume() {
		bIsPaused = false;
		new DownloadFilesTask().execute();
		super.onResume();
	}

	private SSLSocketFactory newSslSocketFactory() {
		try {
			KeyStore trusted = KeyStore.getInstance("BKS");
			InputStream in = getResources().openRawResource(R.raw.my);
			try {
				trusted.load(in, "password".toCharArray());
			} finally {
				in.close();
			}
			TrustManagerFactory tmf = TrustManagerFactory
					.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			tmf.init(trusted);
			SSLContext ctx = SSLContext.getInstance("TLS");
			ctx.init(null, tmf.getTrustManagers(), null);
			return ctx.getSocketFactory();
		} catch (Exception e) {
			throw new AssertionError(e);
		}// end of catch
	}// end of ssl socket
	
	private class DownloadFilesTask extends AsyncTask<List<NameValuePair>, Integer, String> {

		@Override
		protected void onPreExecute() {
			bIsAlreadySchedulled = false;
			setTitle(getTitle()+"...");
			System.gc();
//			fPullToRefreshServiceOnUiThread(false);
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(List<NameValuePair>... params) {
			String strFileContents = "";
			try {
				URL url = new URL(sURL);
				byte[] contents = new byte[1024];
				if ( "http".equals(url.getProtocol() ) ) {
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					InputStream in = new BufferedInputStream(conn.getInputStream());
					int bytesRead = 0;
					try {
						while ((bytesRead = in.read(contents)) != -1) {
							strFileContents = new String(contents, 0, bytesRead);
						}
					} finally {
						in.close();
						conn.disconnect();
						in = null;
						contents = null;
						conn = null;
					}
				} else {
					HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
					conn.setSSLSocketFactory(newSslSocketFactory());
					conn.setRequestMethod("POST");
					if (0 < params.length && 0 < params[0].size()) {
						conn.setDoOutput(true);
						conn.setChunkedStreamingMode(0);
						OutputStream out = conn.getOutputStream();
						BufferedWriter writer = null;
						try {
							writer = new BufferedWriter(
									new OutputStreamWriter(out, "UTF-8"));
							writer.write(getQuery(params[0]));
						} catch (IOException e) {
							e.printStackTrace();
						} finally {
							writer.close();
							writer = null;
							out.close();
							out = null;
						}
					}
					InputStream in = new BufferedInputStream(conn.getInputStream());
					int bytesRead = 0;
					try {
						while ((bytesRead = in.read(contents)) != -1) {
							strFileContents = new String(contents, 0, bytesRead);
						}
					} finally {
						Log.e("CWE", conn.getRequestMethod());
						in.close();
						in = null;
						contents = null;
						conn.disconnect();
						conn = null;
					}
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return strFileContents;
		}

		/*
		 * protected void onProgressUpdate(Integer... progress) {
		 * setProgressPercent(progress[0]); }
		 */
		@SuppressLint("NewApi")
		@Override
		protected void onPostExecute(String sData) {
			if ( !bIsAlreadySchedulled && 0 < iUpdateIntervalInSeconds ) {
				// schedulle new update
				fSchedulleReload();
			}
			setTitle(android.text.format.DateFormat.format("kk:mm:ss", Calendar.getInstance().getTimeInMillis()));
//			fPullToRefreshServiceOnUiThread(true);
			super.onPostExecute(sData);
			int resId = getApplicationContext().getResources().getIdentifier(sData, "string", getApplicationContext().getPackageName());
			if (null == sData) {
				Log.d("CEW", "-"+sData+"-");
				Toast.makeText(getApplicationContext(),
						R.string.connection_error_102, Toast.LENGTH_LONG)
						.show();
				return;
			} else if (sData.startsWith("message_from_server_") && 0 < resId) {
				fShowAlert(resId);
				return;
			} else if (0==sData.length()) {
				Log.d("CEW", "|"+sData+"|");
				Toast.makeText(getApplicationContext(),
						R.string.connection_error_103, Toast.LENGTH_LONG)
						.show();
				return;
			}
			String[] aData = sData.split(";");
			if (iExpectedValuesCount != aData.length) {
				Log.d("CEW", "."+sData+".");
				Toast.makeText(getApplicationContext(),
						R.string.connection_error_112, Toast.LENGTH_LONG)
						.show();
				return;
			}
			Log.d("CEW", sData);
			for (int i = 0; i < aData.length; i++) {
				aData[i] = aData[i].trim();
			}

			((TextView) findViewById(R.id.temp_1)).setText(aData[14]);
			((TextView) findViewById(R.id.temp_2)).setText(aData[15]);
			((TextView) findViewById(R.id.time_total)).setText(aData[18]);
			((TextView) findViewById(R.id.time_step)).setText(aData[19]);
			((TextView) findViewById(R.id.time_step_remaining)).setText(aData[20]);
			((TextView) findViewById(R.id.preasure_1)).setText(aData[16]);
			((TextView) findViewById(R.id.preasure_2)).setText(aData[17]);
			((TextView) findViewById(R.id.pwm)).setText(aData[21]);
			((TextView) findViewById(R.id.pwm)).setBackgroundColor(getResources().getColor(R.color.yellow));

			if ( 0f == Float.parseFloat(aData[4])) {
				((TextView) findViewById(R.id.TU_up)).setBackgroundColor(getResources().getColor(R.color.green));
			} else {
				((TextView) findViewById(R.id.TU_up)).setBackgroundColor(getResources().getColor(R.color.red));
			}
			
			if ( 0f == Float.parseFloat(aData[5])) {
				((TextView) findViewById(R.id.TU_down)).setBackgroundColor(getResources().getColor(R.color.green));
			} else {
				((TextView) findViewById(R.id.TU_down)).setBackgroundColor(getResources().getColor(R.color.red));
			}
			
			if ( 0f == Float.parseFloat(aData[6])) {
				((TextView) findViewById(R.id.ekrani_otvoreni)).setBackgroundColor(getResources().getColor(R.color.green));
			} else {
				((TextView) findViewById(R.id.ekrani_otvoreni)).setBackgroundColor(getResources().getColor(R.color.red));
			}
			
			if ( 0f == Float.parseFloat(aData[7])) {
				((TextView) findViewById(R.id.ekrani_zatvoreni)).setBackgroundColor(getResources().getColor(R.color.green));
			} else {
				((TextView) findViewById(R.id.ekrani_zatvoreni)).setBackgroundColor(getResources().getColor(R.color.red));
			}
			
			if ( 0f == Float.parseFloat(aData[8])) {
				((TextView) findViewById(R.id.kapak_otvoren)).setBackgroundColor(getResources().getColor(R.color.green));
			} else {
				((TextView) findViewById(R.id.kapak_otvoren)).setBackgroundColor(getResources().getColor(R.color.red));
			}
			
			if ( 0f == Float.parseFloat(aData[9])) {
				((TextView) findViewById(R.id.kapak_zatvoren)).setBackgroundColor(getResources().getColor(R.color.green));
			} else {
				((TextView) findViewById(R.id.kapak_zatvoren)).setBackgroundColor(getResources().getColor(R.color.red));
			}
			
			if ( 0f == Float.parseFloat(aData[13])) {
				((TextView) findViewById(R.id.no_water)).setText(R.string.yes_water);
				((TextView) findViewById(R.id.no_water)).setBackgroundColor(getResources().getColor(R.color.green));
			} else {
				((TextView) findViewById(R.id.no_water)).setText(R.string.no_water);
				((TextView) findViewById(R.id.no_water)).setBackgroundColor(getResources().getColor(R.color.red));
			}
			
			if ( 0f == Float.parseFloat(aData[22])) {
				((TextView) findViewById(R.id.alarm)).setBackgroundColor(getResources().getColor(R.color.green));
			} else {
				((TextView) findViewById(R.id.alarm)).setBackgroundColor(getResources().getColor(R.color.red));
			}
			
			if ( 0f == Float.parseFloat(aData[2])) {
				bButtonPulloutState = false;
				if ( android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN){
					((Button) findViewById(R.id.button_details_pullout)).setBackground(Sd(getResources().getColor(R.color.gray)));
				} else {
					((Button) findViewById(R.id.button_details_pullout)).setBackgroundDrawable(Sd(getResources().getColor(R.color.gray)));
				}
			} else {
				bButtonPulloutState = true;
				if ( android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN){
					((Button) findViewById(R.id.button_details_pullout)).setBackground(Sd(getResources().getColor(R.color.red)));
				} else {
					((Button) findViewById(R.id.button_details_pullout)).setBackgroundDrawable(Sd(getResources().getColor(R.color.red)));
				}
			}
			
			if ( 0f == Float.parseFloat(aData[0])) {
				bButtonVacuumingState = false;
				if ( android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN){
					((Button) findViewById(R.id.button_vacuuming)).setBackground(Sd(getResources().getColor(R.color.gray)));
				} else {
					((Button) findViewById(R.id.button_vacuuming)).setBackgroundDrawable(Sd(getResources().getColor(R.color.gray)));
				}
			} else {
				bButtonVacuumingState = true;
				if ( android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN){
					((Button) findViewById(R.id.button_vacuuming)).setBackground(Sd(getResources().getColor(R.color.red)));
				} else {
					((Button) findViewById(R.id.button_vacuuming)).setBackgroundDrawable(Sd(getResources().getColor(R.color.red)));
				}
			}
			
			if ( 0f == Float.parseFloat(aData[1])) {
				bButtonStartProcessState = false;
				if ( android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN){
					((Button) findViewById(R.id.button_start_stop_process)).setBackground(Sd(getResources().getColor(R.color.gray)));
				} else {
					((Button) findViewById(R.id.button_start_stop_process)).setBackgroundDrawable(Sd(getResources().getColor(R.color.gray)));
				}
			} else {
				bButtonStartProcessState = true;
				if ( android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN){
					((Button) findViewById(R.id.button_start_stop_process)).setBackground(Sd(getResources().getColor(R.color.red)));
				} else {
					((Button) findViewById(R.id.button_start_stop_process)).setBackgroundDrawable(Sd(getResources().getColor(R.color.red)));
				}
			}

			((LinearLayout) ((Button) findViewById(R.id.button_start_stop_process)).getParent()).setVisibility(LinearLayout.VISIBLE);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_activity_actions, menu);
		if ( 0 < iUpdateIntervalInSeconds ) {
			menu.findItem(R.id.action_update_interval).setTitle(getResources().getString(R.string.action_update_interval) + " " + iUpdateIntervalInSeconds + "s");
		} else {
			menu.findItem(R.id.action_update_interval).setTitle(getResources().getString(R.string.action_update_interval) + " " + getResources().getString(R.string.no));
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items	
		switch (item.getItemId()) {
		case R.id.action_update_interval:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			// Get the layout inflater
			LayoutInflater inflater = getLayoutInflater();

			View v = inflater.inflate(R.layout.update_interval, null);
			final EditText et = (EditText) v.findViewById(R.id.edittext_update_interval);
			if (0<iUpdateIntervalInSeconds) {
				et.append(String.valueOf(iUpdateIntervalInSeconds));
			} else {
				et.setHint(R.string.refresh_interval_hint);
			}
			
			// Inflate and set the layout for the dialog
			// Pass null as the parent view because its going in the dialog
			// layout
			builder.setView(v)
					.setOnCancelListener(new DialogInterface.OnCancelListener() {
						
						@Override
						public void onCancel(DialogInterface dialog) {
							dialog.dismiss();
						}
					})
					// Add action buttons
					.setPositiveButton(R.string.save,
							new DialogInterface.OnClickListener() {
								@SuppressLint("NewApi")
								@Override
								public void onClick(DialogInterface dialog,
										int id) {
									String etValue = et.getText().toString();
									
									iUpdateIntervalInSeconds = Integer.parseInt(etValue);
									fSchedulleReload();
									
									dialog.dismiss();

									if ( android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB){
										invalidateOptionsMenu();
									} else{
										ActivityCompat.invalidateOptionsMenu(getParent());
									}
								}
							})
					.setNeutralButton(R.string.disable,
							new DialogInterface.OnClickListener() {
								@SuppressLint("NewApi")
								public void onClick(DialogInterface dialog,
										int id) {
									iUpdateIntervalInSeconds = 0;
									fSchedulleReload();
									
									dialog.dismiss();

									if ( android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB){
										invalidateOptionsMenu();
									} else{
										ActivityCompat.invalidateOptionsMenu(getParent());
									}
								}
							})
					.setNegativeButton(R.string.cancel,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.dismiss();
								}
							})
					.setTitle(R.string.refresh_time_in_secods);

			final AlertDialog dialog = builder.create();
			dialog.show();
			if (0 == iUpdateIntervalInSeconds) {
				dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
			}

			et.addTextChangedListener(new TextWatcher() {
				
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					if ( 0 == s.length() || 0 == Integer.valueOf(s.toString()) ) {
						dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
					} else {
						dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
					}
				}
				
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
					
				}
			});
			
			return true;
		case R.id.action_search:
			return true;
		case R.id.action_compose:
			Toast.makeText(getApplicationContext(),
					R.string.connection_error_103, Toast.LENGTH_LONG).show();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	private String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException
	{
	    StringBuilder result = new StringBuilder();
	    boolean first = true;

	    for (NameValuePair pair : params)
	    {
	        if (first)
	            first = false;
	        else
	            result.append("&");

	        result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
	        result.append("=");
	        result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
	    }

	    return result.toString();
	}

	public void fShowAlert(final int resId) {
		new AlertDialog.Builder(this)
				.setMessage(resId)
				.setCancelable(false)
				.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int id) {
								dialog.dismiss();
							}
						}).create().show();
	}

	public ShapeDrawable Sd(int s) {

		float[] outerR = new float[] { 12, 12, 12, 12, 12, 12, 12, 12 };
		ShapeDrawable mDrawable = new ShapeDrawable(new RoundRectShape(outerR,
				null, null));

		mDrawable.getPaint().setColor(s);
		return mDrawable;
	}
}
