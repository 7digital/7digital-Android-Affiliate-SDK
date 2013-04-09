package uk.co.sevendigital.android.partner.sdk;

import java.io.File;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.text.TextUtils;

/**
 * A helper class that contains functionality to build an {@link Intent} suitable for launching various
 * sections of the 7digital Android app, and performing common actions. Although all Intents can be
 * set up manually, it is recommended to use the methods provided by {@link Builder} to ensure all
 * requirements for performing a specific action are met. The Builder methods return an Intent that
 * can be further customized if necessary. If you do not need to do any further modifications to the 
 * Intent returned, consider using the convenience methods in {@link SDIPartnerUtil}. 
 *
 * All valid actions can be found in {@link Action}.
 * All valid Intent extras can be found in {@link Extra}.
 * 
 * @author mhelder
 */
public class SDIIntent {

	private SDIIntent() { /* prevent instantiating */ }
	
	/**
	 * Constant for the 7digital website URL. Used to redirect the user to if the 7digital Android app is not installed and
	 * no prompt to download the app from Google Play should be displayed.
	 * @see SDIPartnerUtil#start7digitalOrWebsite(Context, Intent) 
	 */
	public static final String SDI_WEBSITE_URL = "http://www.7digital.com";
	
	/**
	 * Constant that indicates the package name of the 7digital Android app.
	 */
	public static final String SDI_ANDROID_PACKAGE_NAME = "uk.co.sevendigital.android";
	
	/**
	 * Constant for the normal entry point in the 7digital Android app that 3rd party apps should send their Intent to. The
	 * only exceptions ton this are the {@link Intent#ACTION_SEARCH} and {@link Action#SEARCH} actions and {@link Action#VIEW_SHOP}, 
	 * which may be send to the app's launch activity. Use {@link PackageManager#getLaunchIntentForPackage(SDI_ANDROID_PACKAGE_NAME)} 
	 * to get an Intent appropriate for the search action, or refer to {@link Builder#buildSearchIntent(String)} and 
	 * {@link SDIPartnerUtil#search7digital(Context, String, String)}. 
	 */
	public static final String SDI_ANDROID_EXTERNAL_ENTRY_POINT = "uk.co.sevendigital.android.library.shop.SDIExternalActionActivity";
	
	/**
	 * A static helper class for building Intents suitable for performing common action on the 7digital Android app.
	 * @see {@link Action} definition for a list of available actions.
	 * @author mhelder
	 */
	public static class Builder {
		
		private Builder() { /* prevent instantiating */ }
		
		/**
		 * Constructs an Intent suitable for performing a shop search in the 7digital Android app. 
		 * 
		 * Note that if you use this method you are responsible for adding an appropriate {@link Extra#PARTNER} yourself.
		 * @param query The query string to perform the search with
		 * @see SDIPartnerUtil#search7digital(Context, String, String)
		 */
		public static Intent buildSearchIntent(String query) {
			if (TextUtils.isEmpty(query)) throw new IllegalArgumentException("Parameter query should not be null or empty.");
			Intent intent = new Intent(Action.SEARCH);
			intent.putExtra(SearchManager.QUERY, query);
			return intent;
		}
		
		/**
		 * Constructs an Intent suitable for launching the shop in the 7digital Android app. 
		 * 
		 * Note that if you use this method you are responsible for adding an appropriate {@link Extra#PARTNER} yourself.
		 * @see SDIPartnerUtil#launch7digitalShop(Context)
		 */
		public static Intent buildViewShopIntent() {
			return new Intent().setClassName(SDIIntent.SDI_ANDROID_PACKAGE_NAME, SDIIntent.SDI_ANDROID_EXTERNAL_ENTRY_POINT).setAction(Action.VIEW_SHOP);
		}
		
		/**
		 * Constructs an Intent suitable for launching the Your Music section in the 7digital Android app. In order for this action to
		 * succeed, the user requires to be logged in. If (s)he is not, a prompt will ask for credentials, optionally allowing new users
		 * to create an account.
		 * 
		 * Note that if you use this method you are responsible for adding an appropriate {@link Extra#PARTNER} yourself.
		 * @see SDIPartnerUtil#launch7digitalMusic(Context)
		 */
		public static Intent buildViewYourMusicIntent() {
			return new Intent().setClassName(SDIIntent.SDI_ANDROID_PACKAGE_NAME, SDIIntent.SDI_ANDROID_EXTERNAL_ENTRY_POINT).setAction(Action.VIEW_YOUR_MUSIC);
		}
		
		/**
		 * Constructs an Intent suitable for launching the Downloads section in the 7digital Android app. In order for this action to
		 * succeed, the user requires to be logged in. If (s)he is not, a prompt will ask for credentials, optionally allowing new users
		 * to create an account.
		 * 
		 * Note that if you use this method you are responsible for adding an appropriate {@link Extra#PARTNER} yourself.
		 * @see SDIPartnerUtil#launch7digitalDownloads(Context)
		 */
		public static Intent buildViewDownloadsIntent() {
			return new Intent().setClassName(SDIIntent.SDI_ANDROID_PACKAGE_NAME, SDIIntent.SDI_ANDROID_EXTERNAL_ENTRY_POINT).setAction(Action.VIEW_DOWNLOADS);
		}
		
		/**
		 * Constructs an Intent suitable for displaying the details of a release identified by the given parameter. This
		 * is a convenience method for calling:
		 * 
		 * {@link #buildView7digitalItem(releaseId, null, -1, null, null, null, -1, null)}
		 * 
		 * Note that if you use this method you are responsible for adding an appropriate {@link Extra#PARTNER} yourself.
		 * @param releaseId The 7digital identifier for the release to display.
		 * @see SDIPartnerUtil#view7digitalRelease(Context, long, String)
		 */
		public static Intent buildView7digitalRelease(long releaseId) {
			if (releaseId == -1) throw new IllegalArgumentException("Parameter releaseId should not be -1.");
			return buildView7digitalItem(releaseId, null, -1, null, null, null, -1, null);
		}
		
		/**
		 * Constructs an Intent suitable for displaying the details of a given release and highlighting the given track. This
		 * is a convenience method for calling:
		 * 
		 * {@link #buildView7digitalItem(releaseId, null, trackId, null, null, null, -1, null)}
		 * 
		 * Note that if you use this method you are responsible for adding an appropriate {@link Extra#PARTNER} yourself.
		 * @param releaseId The 7digital identifier for the release to display.
		 * @param trackId The 7digital identifier for the track to highlight for this release.
		 * @see SDIPartnerUtil#view7digitalTrack(Context, long, long, String)
		 */
		public static Intent buildView7digitalTrack(long releaseId, long trackId) {
			if (releaseId == -1) throw new IllegalArgumentException("Parameter releaseId should not be -1.");
			return buildView7digitalItem(releaseId, null, trackId, null, null, null, -1, null);
		}
		
		/**
		 * Constructs an Intent suitable for displaying the details of a given release. This method allows for full control
		 * over the extras that are passed into the intent. Information that is added as extra will be immediately visible
		 * when the details screen gets loaded, improving the user experience. The bare minimum is a valid releaseId, which
		 * will yield the same result as {@link #buildView7digitalRelease(long)}.
		 * 
		 * Note that if you use this method you are responsible for adding an appropriate {@link Extra#PARTNER} yourself.
		 * @param releaseId The 7digital identifier for the release to display. <b>Required</b>.
		 * @param releaseTitle The title to display for the release.
		 * @param trackId The 7digital identifier for the track to highlight for this release, or <b>-1</b> for no highlight.
		 * @param trackTitle The title to display for the selected track, or <b>null</b> if it should be loaded automatically.
		 * @param trackVersion The version to display for the selected track, or <b>null</b> if it should be loaded automatically.
		 * @param coverUrl The location of the cover url for this release/track, or <b>null</b> if it should be loaded automatically.
		 * @param artistId The 7digital identifier for the artist of this release/track, or <b>-1</b> if it should be loaded automatically.
		 * @param artistName The name of the artist of this release/track, or <b>null</b> if it should be loaded automatically.
		 * @see SDIPartnerUtil#view7digitalItem(Context, long, String, long, String, String, String, long, String, String)
		 */
		public static Intent buildView7digitalItem(long releaseId, String releaseTitle, long trackId, String trackTitle, String trackVersion, String coverUrl, long artistId, String artistName) {
			if (releaseId == -1) throw new IllegalArgumentException("Parameter releaseId should not be -1.");
			Intent intent = new Intent().setClassName(SDIIntent.SDI_ANDROID_PACKAGE_NAME, SDIIntent.SDI_ANDROID_EXTERNAL_ENTRY_POINT).setAction(SDIIntent.Action.VIEW_RELEASE);
			intent.putExtra(SDIIntent.Extra.RELEASEID, releaseId);
			if (releaseTitle != null) intent.putExtra(SDIIntent.Extra.RELEASETITLE, releaseTitle);
			if (trackId != -1) intent.putExtra(SDIIntent.Extra.TRACKID, trackId);
			if (trackTitle != null) intent.putExtra(SDIIntent.Extra.TRACKTITLE, trackTitle);
			if (trackVersion != null) intent.putExtra(SDIIntent.Extra.TRACKVERSION, trackVersion);
			if (coverUrl != null) intent.putExtra(SDIIntent.Extra.COVERURL, coverUrl);
			if (artistId != -1) intent.putExtra(SDIIntent.Extra.ARTISTID, artistId);
			if (artistName != null) intent.putExtra(SDIIntent.Extra.ARTISTNAME, artistName);
			return intent;
		}
		
		/**
		 * Constructs an Intent suitable for displaying the details of a given artist. The artist ID is required to successfully
		 * execute the action, the name is optional. Nevertheless, it's good practice to supply the artist name when known, as it
		 * visually improves loading responsiveness and older versions of the 7digital Android app required the artist name to be 
		 * passed in (no longer since version 5.09).
		 * @param artistId The 7digital identifier for the artist to display.
		 * @param artistName The name of the artist to display the details for (required only for displaying purposes)
		 * @see SDIPartnerUtil#view7digitalArtist(Context, long, String, String)
		 */
		public static Intent buildView7digitalArtist(long artistId, String artistName) {
			if (artistId == -1) throw new IllegalArgumentException("Parameter artistId should not be -1.");
			Intent intent = new Intent().setClassName(SDIIntent.SDI_ANDROID_PACKAGE_NAME, SDIIntent.SDI_ANDROID_EXTERNAL_ENTRY_POINT).setAction(SDIIntent.Action.VIEW_ARTIST);
			intent.putExtra(SDIIntent.Extra.ARTISTID, artistId);
			if (TextUtils.isEmpty(artistName)) intent.putExtra(SDIIntent.Extra.ARTISTNAME, artistName);
			return intent;
		}
		
		/**
		 * Constructs an Intent suitable for sequentially playing all the tracks of a given release. The exact result will depend
		 * on whether the user is logged in, whether the release is in the user's locker and whether it is downloaded or not.  
		 * 
 		 * If the following criteria are met:
		 * a) the user is signed in.
		 * b) AND at least one track of the given release is owned by the user.
		 * c) AND at least one track of the given release has been downloaded to the device.
		 * then the available tracks will be loaded up into and played by the full screen music player. If one or more of the 
		 * criteria don't hold, the release details screen will be displayed and previews of the tracks will be streamed (as 
		 * opposed to full length tracks that are locally available). The user can still navigate to the full screen player manually.
		 * @param releaseId The 7digital identifier for the release to play.
		 * @see SDIPartnerUtil#play7digitalRelease(Context, long, String)
		 */
		public static Intent buildPlay7digitalRelease(long releaseId) {
			if (releaseId == -1) throw new IllegalArgumentException("Parameter releaseId should not be -1.");
			Intent intent = new Intent().setClassName(SDIIntent.SDI_ANDROID_PACKAGE_NAME, SDIIntent.SDI_ANDROID_EXTERNAL_ENTRY_POINT).setAction(SDIIntent.Action.PLAY_RELEASE);
			intent.putExtra(SDIIntent.Extra.RELEASEID, releaseId);
			return intent;
		}
		
		/**
		 * Constructs an Intent suitable for playing a single track of a given release. The exact result will depend
		 * on whether the user is logged in, whether the track is in the user's locker and whether it is downloaded or not.  
		 * 
 		 * If the following criteria are met:
		 * a) the user is signed in.
		 * b) AND the track of the given release is owned by the user.
		 * c) AND the track of the given release has been downloaded to the device.
		 * then the specified track will be loaded up into and played by the full screen music player. If one or more of the 
		 * criteria don't hold, the release details screen will be displayed and a preview of the track will be streamed (as 
		 * opposed to the full length track that is locally available). The user can still navigate to the full screen player 
		 * manually.
		 * @param trackId The 7digital identifier for the track to play for this release.
		 * @param releaseId The 7digital identifier for the release to play.
		 * @see SDIPartnerUtil#play7digitalTrack(Context, long, long, String)
		 */
		public static Intent buildPlay7digitalTrack(long releaseId, long trackId) {
			if (releaseId == -1) throw new IllegalArgumentException("Parameter releaseId should not be -1.");
			if (trackId == -1) throw new IllegalArgumentException("Parameter trackId should not be -1.");
			Intent intent = new Intent().setClassName(SDIIntent.SDI_ANDROID_PACKAGE_NAME, SDIIntent.SDI_ANDROID_EXTERNAL_ENTRY_POINT).setAction(SDIIntent.Action.PLAY_TRACK);
			intent.putExtra(SDIIntent.Extra.RELEASEID, releaseId);
			intent.putExtra(SDIIntent.Extra.TRACKID, trackId);
			return intent;
		}
	}
	
	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
	 * actions
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */
	
	/**
	 * A wrapper class that contains the definitions of common actions that can be performed on the 7digital Android app. 
	 * @author mhelder
	 */
	public static class Action {

		private Action() { /* prevent instantiating */ }

		/** 
		 * Constant to indicate a search action in the 7digital Android app.
		 * @see Builder#buildSearchIntent(String)
		 * @see SDIPartnerUtil#search7digital(Context, String, String) 
		 */
		public static final String SEARCH = "uk.co.sevendigital.android.intent.action.SEARCH";

		/** 
		 * Constant to indicate a view artist action in the 7digital Android app.
		 * @see Builder#buildView7digitalArtist(long, String)
		 * @see SDIPartnerUtil#view7digitalArtist(Context, long, String, String) 
		 */
		public static final String VIEW_ARTIST = "uk.co.sevendigital.android.intent.action.VIEW_ARTIST";
		/** 
		 * Constant to indicate a view release action in the 7digital Android app. Note that this same action is used for
		 * viewing a specific track of a release, but with extra parameters.
		 * @see Builder#buildView7digitalRelease(long)
		 * @see Builder#buildView7digitalTrack(long, long)
		 * @see Builder#buildView7digitalItem(long, String, long, String, String, String, long, String)
		 * @see SDIPartnerUtil#view7digitalRelease(Context, long, String)
		 * @see SDIPartnerUtil#view7digitalTrack(Context, long, long, String)
		 * @see SDIPartnerUtil#view7digitalItem(Context, long, String, long, String, String, String, long, String, String) 
		 */
		public static final String VIEW_RELEASE = "uk.co.sevendigital.android.intent.action.VIEW_RELEASE";

		/** 
		 * Constant to indicate a view shop action in the 7digital Android app.
		 * @see Builder#buildViewShopIntent() 
		 * @see SDIPartnerUtil#launch7digitalShop(Context)
		 */
		public static final String VIEW_SHOP = "uk.co.sevendigital.android.intent.action.VIEW_SHOP";
		/** 
		 * Constant to indicate a view your music action in the 7digital Android app. Requires user to be logged in order
		 * to succeed.
		 * @see Builder#buildViewYourMusicIntent() 
		 * @see SDIPartnerUtil#launch7digitalMusic(Context) 
		 */
		public static final String VIEW_YOUR_MUSIC = "uk.co.sevendigital.android.intent.action.VIEW_YOUR_MUSIC";
		/** 
		 * Constant to indicate a view downloads action in the 7digital Android app. Requires user to be logged in order
		 * to succeed. 
		 * @see Builder#buildViewDownloadsIntent() 
		 * @see SDIPartnerUtil#launch7digitalDownloads(Context)
		 */
		public static final String VIEW_DOWNLOADS = "uk.co.sevendigital.android.intent.action.VIEW_DOWNLOADS";

		/** 
		 * Constant to indicate a play track action in the 7digital Android app. The result will vary depending on whether
		 * the user is logged in, a track is in the user's locker and downloaded or not.
		 * @see Builder#buildPlay7digitalTrack(long, long) 
		 * @see SDIPartnerUtil#play7digitalTrack(Context, long, long, String)
		 */
		public static final String PLAY_TRACK = "uk.co.sevendigital.android.intent.action.PLAY_TRACK";
		
		/** 
		 * Constant to indicate a play release action in the 7digital Android app. The result will vary depending on whether
		 * the user is logged in, a track is in the user's locker and downloaded or not.
		 * @see Builder#buildPlay7digitalRelease(long) 
		 * @see SDIPartnerUtil#play7digitalRelease(Context, long, String)
		 */
		public static final String PLAY_RELEASE = "uk.co.sevendigital.android.intent.action.PLAY_RELEASE";

	}
	
	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
	 * extras
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */
	
	/**
	 * A wrapper class that contains the definitions of extras that can be attached to an Intent sent to the 7digital Android app.
	 * @author mhelder
	 */
	public static class Extra {
		
		private Extra() { /* prevent instantiating */ }
		
		// extras: ids
		/** 
		 * Key constant for a 7digital artist ID.
		 * @see Builder#buildView7digitalArtist(long, String)
		 * @see SDIPartnerUtil#view7digitalArtist(Context, long, String, String) 
		 */
		public static final String ARTISTID = "ARTISTID";
		/** 
		 * Key constant for a 7digital release ID.
		 * @see Builder#buildView7digitalRelease(long)
		 * @see Builder#buildView7digitalItem(long, String, long, String, String, String, long, String)
		 * @see SDIPartnerUtil#view7digitalRelease(Context, long, String)
		 * @see SDIPartnerUtil#view7digitalItem(Context, long, String, long, String, String, String, long, String, String) 
		 */
		public static final String RELEASEID = "RELEASEID";
		/** 
		 * Key constant for a 7digital track ID.
		 * @see Builder#buildView7digitalTrack(long, long)
		 * @see Builder#buildView7digitalItem(long, String, long, String, String, String, long, String)
		 * @see SDIPartnerUtil#view7digitalTrack(Context, long, long, String)
		 * @see SDIPartnerUtil#view7digitalItem(Context, long, String, long, String, String, String, long, String, String) 
		 */
		public static final String TRACKID = "TRACKID";
		
		// extras: titles/names
		/** 
		 * Key constant for a 7digital artist name.
		 * @see Builder#buildView7digitalArtist(long, String)
		 * @see SDIPartnerUtil#view7digitalArtist(Context, long, String, String) 
		 */
		public static final String ARTISTNAME = "ARTISTNAME";
		/** 
		 * Key constant for a 7digital release title. Note: optional.
		 * @see Builder#buildView7digitalRelease(long)
		 * @see Builder#buildView7digitalItem(long, String, long, String, String, String, long, String)
		 * @see SDIPartnerUtil#view7digitalRelease(Context, long, String)
		 * @see SDIPartnerUtil#view7digitalItem(Context, long, String, long, String, String, String, long, String, String) 
		 */
		public static final String RELEASETITLE = "RELEASETITLE";
		/** 
		 * Key constant for a 7digital track title. Note: optional. 
		 * @see Builder#buildView7digitalTrack(long, long)
		 * @see Builder#buildView7digitalItem(long, String, long, String, String, String, long, String)
		 * @see SDIPartnerUtil#view7digitalTrack(Context, long, long, String)
		 * @see SDIPartnerUtil#view7digitalItem(Context, long, String, long, String, String, String, long, String, String) 
		 */
		public static final String TRACKTITLE = "TRACKTITLE";

		// extras: misc
		/**
		 * Key constant for a 7digital track version. Note: optional.
		 * @see Builder#buildView7digitalItem(long, String, long, String, String, String, long, String)
		 * @see SDIPartnerUtil#view7digitalItem(Context, long, String, long, String, String, String, long, String, String) 
		 */
		public static final String TRACKVERSION = "TRACKVERSION";
		/**
		 * Key constant for the location of a 7digital cover. Should be either an absolute {@link File} path or {@link Uri}
		 * with "http" scheme. Note: optional.
		 * @see Builder#buildView7digitalItem(long, String, long, String, String, String, long, String)
		 * @see SDIPartnerUtil#view7digitalItem(Context, long, String, long, String, String, String, long, String, String) 
		 */
		public static final String COVERURL = "COVERURL";
		
		// extras: partner
		/**
		 * Key constant for a 7digital partner identifier to be used to perform the action. Note: not required for the
		 * actions to work, but should be provided to guarantee accurate tracking of requests by 7digital.
		 */
		public static final String PARTNER = "uk.co.sevendigital.android.intent.extra.PARTNER";
	}
	
}
