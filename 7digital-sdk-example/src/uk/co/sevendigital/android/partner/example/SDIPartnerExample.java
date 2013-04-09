package uk.co.sevendigital.android.partner.example;

import uk.co.sevendigital.android.partner.sdk.SDIPartnerUtil;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * The purpose of this activity is to test launching and interfacing with the 7digital application.
 * @author juliusspencer
 */
public class SDIPartnerExample extends Activity implements View.OnClickListener {

	private static final int ERROR_DIALOG_ID = 777;
	private static final String EXTRA_ERROR_MESSAGE = "extra_error_message";
	
	// Change this to your partner code
	public static final String PARTNER_CODE = "2221";

	private EditText mSearchEditText;
	private Button mSearchButton;

	private Button mLaunchShopButton;
	private Button mLaunchMusicButton;
	private Button mLaunchDownloadsButton;

	private Button mPlayTrackButton;
	private Button mPlayReleaseButton;

	private Button mViewReleaseButton;
	private Button mViewArtistButton;
	private Button mViewTrackButton;

	private EditText mAffiliateIdEditText;
	private EditText mReleaseIdEditText;
	private EditText mTrackIdEditText;
	private EditText mArtistIdEditText;
	private EditText mArtistNameEditText;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// Bind views
		mSearchButton = (Button) findViewById(R.id.search_button);
		mSearchEditText = (EditText) findViewById(R.id.search_edittext);
		mLaunchShopButton = (Button) findViewById(R.id.launch_shop_button);
		mLaunchMusicButton = (Button) findViewById(R.id.launch_music_button);
		mLaunchDownloadsButton = (Button) findViewById(R.id.launch_downloads_button);
		mViewReleaseButton = (Button) findViewById(R.id.release_button);
		mViewArtistButton = (Button) findViewById(R.id.artist_button);
		mViewTrackButton = (Button) findViewById(R.id.track_button);
		mPlayTrackButton = (Button) findViewById(R.id.play_track_button);
		mPlayReleaseButton = (Button) findViewById(R.id.play_release_button);
		mAffiliateIdEditText = (EditText) findViewById(R.id.affiliate_id_edittext);
		mReleaseIdEditText = (EditText) findViewById(R.id.release_id_edittext);
		mTrackIdEditText = (EditText) findViewById(R.id.track_id_edittext);
		mArtistIdEditText = (EditText) findViewById(R.id.artist_id_edittext);
		mArtistNameEditText = (EditText) findViewById(R.id.artist_name_edittext);

		mAffiliateIdEditText.setText(PARTNER_CODE);
		mReleaseIdEditText.setText(Long.toString(1347415l));
		mTrackIdEditText.setText(Long.toString(14892292l));
		mArtistIdEditText.setText(Long.toString(2200l));
		mArtistNameEditText.setText("Elvis Presley");

		// attach listeners
		mSearchButton.setOnClickListener(this);
		mLaunchShopButton.setOnClickListener(this);
		mLaunchMusicButton.setOnClickListener(this);
		mLaunchDownloadsButton.setOnClickListener(this);
		mViewReleaseButton.setOnClickListener(this);
		mViewArtistButton.setOnClickListener(this);
		mViewTrackButton.setOnClickListener(this);
		mPlayTrackButton.setOnClickListener(this);
		mPlayReleaseButton.setOnClickListener(this);
	}

	@SuppressWarnings("deprecation") @Override protected Dialog onCreateDialog(int id, Bundle args) {
		if (id != ERROR_DIALOG_ID) return super.onCreateDialog(id, args);
		String message = args.getString(EXTRA_ERROR_MESSAGE);
		return new AlertDialog.Builder(this)
			.setIcon(android.R.drawable.ic_dialog_alert)
			.setPositiveButton(android.R.string.ok, null)
			.setTitle(R.string.error)
			.setMessage(message)
			.create();
	}
	
	@SuppressWarnings("deprecation") @Override protected void onPrepareDialog(int id, Dialog dialog, Bundle args) {
		super.onPrepareDialog(id, dialog, args);
		if (!(dialog instanceof AlertDialog)) return; 
		String message = args.getString(EXTRA_ERROR_MESSAGE);
		((AlertDialog) dialog).setMessage(message);
	}
	
	@SuppressWarnings("deprecation") @Override public void onClick(View v) {
		try {
			switch (v.getId()) {
				case R.id.search_button: 
					SDIPartnerUtil.search7digital(this, mSearchEditText.getText().toString().trim(), getAffiliateId());
					break;
				case R.id.launch_shop_button:
					SDIPartnerUtil.launch7digitalShop(this);
					break;
				case R.id.launch_music_button:
					SDIPartnerUtil.launch7digitalMusic(this);
					break;
				case R.id.launch_downloads_button:
					SDIPartnerUtil.launch7digitalDownloads(this);
					break;
				case R.id.release_button:
					SDIPartnerUtil.view7digitalRelease(this, getLongFromEditText(mReleaseIdEditText), getAffiliateId());
					break;
				case R.id.artist_button:
					SDIPartnerUtil.view7digitalArtist(this, getLongFromEditText(mArtistIdEditText), mArtistNameEditText.getText().toString(), getAffiliateId());
					break;
				case R.id.track_button:
					SDIPartnerUtil.view7digitalTrack(this, getLongFromEditText(mReleaseIdEditText), getLongFromEditText(mTrackIdEditText), getAffiliateId());
					break;
				case R.id.play_track_button:
					SDIPartnerUtil.play7digitalTrack(this, getLongFromEditText(mReleaseIdEditText), getLongFromEditText(mTrackIdEditText), getAffiliateId());
					break;
				case R.id.play_release_button:
					SDIPartnerUtil.play7digitalRelease(this, getLongFromEditText(mReleaseIdEditText), getAffiliateId());
					break;
			}
		} catch (IllegalArgumentException e) { showDialog(ERROR_DIALOG_ID, buildErrorMessageBundle(e.getMessage())); }
	}

	@SuppressWarnings("deprecation") private long getLongFromEditText(EditText edittext) {
		if (TextUtils.isEmpty(edittext.getText())) return -1;
		try { return Long.parseLong(edittext.getText().toString()); }
		catch (NumberFormatException e) { 
			showDialog(ERROR_DIALOG_ID, buildErrorMessageBundle(e.getMessage()));
			return -1;
		}
	}
	
	private static Bundle buildErrorMessageBundle(String message) {
		Bundle bundle = new Bundle();
		bundle.putString(EXTRA_ERROR_MESSAGE, message);
		return bundle;
	}

	/**
	 * Get the id from the edittext or return a default of 2221
	 */
	private String getAffiliateId() {
		String affiliateId = mAffiliateIdEditText.getText().toString().trim();
		if (TextUtils.isEmpty(affiliateId)) affiliateId = PARTNER_CODE;
		return affiliateId;
	}

}