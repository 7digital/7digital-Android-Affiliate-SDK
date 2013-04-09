package uk.co.sevendigital.android.partner.sdk;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * A utility class that provides 'shortcuts' to perform common actions on the 7digital Android app. These
 * methods wrap the different builders defined in {@link SDIIntent.Builder} and simply use the passed in
 * Context to execute the action.
 * 
 * @author mhelder
 */
public class SDIPartnerUtil {

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
	 * constants
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
	 * util methods (suitable for external/stand-alone usage)
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	/**
	 * Starts the 7digital Android app and performs a search on the given query.
	 * @param context Context to start the app on (normally an Activity).
	 * @param query Query text.
	 * @param affiliateId Identifier for the affiliate/partner using this method.
	 */
	public static void search7digital(Context context, String query, String affiliateId) {
		Intent intent = SDIIntent.Builder.buildSearchIntent(query);
		start7digitalIntent(context, intent, affiliateId);
	}

	/**
	 * Starts the 7digital Android app in the 'shop' section.
	 * @param context Context to start the app on (normally an Activity).
	 * @see #start7digitalOrMarket(Context, Intent)
	 */
	public static void launch7digitalShop(Context context) {
		Intent intent = SDIIntent.Builder.buildViewShopIntent();
		start7digitalOrMarket(context, intent);
	}

	/**
	 * Starts the 7digital Android app in the 'your music' section. Requires the user to be logged
	 * in. If not, a login screen will be displayed prompting the user to sign in.
	 * @param context Context to start the app on (normally an Activity).
	 * @see #start7digitalOrMarket(Context, Intent)
	 */
	public static void launch7digitalMusic(Context context) {
		Intent intent = SDIIntent.Builder.buildViewYourMusicIntent();
		start7digitalOrMarket(context, intent);
	}

	/**
	 * Starts the 7digital Android app in the 'downloads' section. Requires the user to be logged
	 * in. If not, a login screen will be displayed prompting the user to sign in.
	 * @param context Context to start the app on (normally an Activity).
	 * @see #start7digitalOrMarket(Context, Intent)
	 */
	public static void launch7digitalDownloads(Context context) {
		Intent intent = SDIIntent.Builder.buildViewDownloadsIntent();
		start7digitalOrMarket(context, intent);
	}

	/**
	 * Starts the 7digital Android app and displays the release details of the album identified by the given
	 * releaseId. 
	 * @param context Context to start the app on (normally an Activity).
	 * @param releaseId The 7digital identifier for the release to display.
	 * @param affiliateId Identifier for the affiliate/partner using this method.
	 * @see {@link #view7digitalItem(Context, long, String, long, String, String, String, long, String, String)}
	 */
	public static void view7digitalRelease(Context context, long releaseId, String affiliateId) {
		Intent intent = SDIIntent.Builder.buildView7digitalRelease(releaseId);
		start7digitalExternalIntent(context, intent, affiliateId);
	}

	/**
	 * Starts the 7digital Android app and displays the release details of the album identified by the given
	 * releaseId and highlights the given track (by id).
	 * @param context Context to start the app on (normally an Activity).
	 * @param releaseId The 7digital identifier for the release to display.
	 * @param trackId The 7digital identifier for the track to highlight for this release.
	 * @param affiliateId Identifier for the affiliate/partner using this method.
	 * @see {@link #view7digitalItem(Context, long, String, long, String, String, String, long, String, String)}
	 */
	public static void view7digitalTrack(Context context, long releaseId, long trackId, String affiliateId) {
		Intent intent = SDIIntent.Builder.buildView7digitalTrack(releaseId, trackId);
		start7digitalExternalIntent(context, intent, affiliateId);
	}
	
	/**
	 * Starts the 7digital Android app and displays the release details of the album identified by the given
	 * releaseId. This method allows most of the required parameters to be manually passed in, for a better 
	 * user experience and seemingly faster loading. Note that the bare minimum is a valid releaseId, which
	 * will yield the same result as {@link #view7digitalRelease(Context, long, String)}.
	 * @param context Context to start the app on (normally an Activity).
	 * @param releaseId The 7digital identifier for the release to display.
	 * @param releaseTitle The title to display for the release.
	 * @param trackId The 7digital identifier for the track to highlight for this release.
	 * @param trackTitle The title to display for the selected track.
	 * @param trackVersion The version to display for the selected track.
	 * @param coverUrl The location of the cover url for this release/track.
	 * @param artistId The 7digital identifier for the artist of this release/track.
	 * @param artistName The name of the artist of this release/track.
	 * @param affiliateId Identifier for the affiliate/partner using this method.
	 * 
	 */
	public static void view7digitalItem(Context context, long releaseId, String releaseTitle, long trackId, String trackTitle, String trackVersion, String coverUrl, long artistId, String artistName, String affiliateId) {
		Intent intent = SDIIntent.Builder.buildView7digitalItem(releaseId, releaseTitle, trackId, trackTitle, trackVersion, coverUrl, artistId, artistName);
		start7digitalExternalIntent(context, intent, affiliateId);
	}

	/**
	 * Starts the 7digital Android app and displays the artist details of the artist identified by the given
	 * artistId. The name is optional but it's good practice to supply this when known. Limitations in the
	 * framework required the artist name to be passed in up until version 5.08 of the 7digital Android app.
	 * @param context Context to start the app on (normally an Activity).
	 * @param artistId The 7digital identifier for the artist to display.
	 * @param artistName The name of the artist to display the details for (required only for displaying purposes)
	 * @param affiliateId Identifier for the affiliate/partner using this method.
	 */
	public static void view7digitalArtist(Context context, long artistId, String artistName, String affiliateId) {
		Intent intent = SDIIntent.Builder.buildView7digitalArtist(artistId, artistName);
		start7digitalExternalIntent(context, intent, affiliateId);
	}

	/**
	 * Starts the 7digital Android app and attempts to play the given release in the full screen music 
	 * player. This will only succeed if:
	 * a) the user is signed in.
	 * b) AND at least one track of the given release is owned by the user.
	 * c) AND at least one track of the given release has been downloaded to the device.
	 * If one or more of the above criteria can't be met, the behaviour will be similar to performing a
	 * 'view' action on the release, as per {@link #view7digitalRelease(Context, long, String)}. This
	 * means the release details will be displayed while previews of the track get played in the 
	 * background. The user can still navigate to the full screen player manually.
	 * @param context Context to start the app on (normally an Activity).
	 * @param releaseId The 7digital identifier for the release to play.
	 * @param affiliateId Identifier for the affiliate/partner using this method.
	 */
	public static void play7digitalRelease(Context context, long releaseId, String affiliateId) {
		Intent intent = SDIIntent.Builder.buildPlay7digitalRelease(releaseId);
		start7digitalExternalIntent(context, intent, affiliateId);
	}

	/**
	 * Starts the 7digital Android app and attempts to play the specified track of the given release in 
	 * the full screen music player. This will only succeed if:
	 * a) the user is signed in.
	 * b) AND the track of the given release is owned by the user.
	 * c) AND the track of the given release has been downloaded to the device.
	 * If one or more of the above criteria can't be met, the behaviour will be similar to performing a
	 * 'view' action on the release, as per {@link #view7digitalTrack(Context, long, long, String)}. This
	 * means the release details will be displayed while a preview of the track gets played in the 
	 * background. The user can still navigate to the full screen player manually.
	 * @param context Context to start the app on (normally an Activity).
	 * @param releaseId The 7digital identifier for the release to play.
	 * @param trackId The 7digital identifier for the track to play for this release.
	 * @param affiliateId Identifier for the affiliate/partner using this method.
	 */
	public static void play7digitalTrack(Context context, long releaseId, long trackId, String affiliateId) {
		Intent intent = SDIIntent.Builder.buildPlay7digitalTrack(releaseId, trackId);
		start7digitalExternalIntent(context, intent, affiliateId);
	}

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
	 * internal
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */
	
	/* used for performing a search in the shop (intent passed in will point to the app's main entry point, rather than the external entry point) */ 
	private static void start7digitalIntent(Context context, Intent intent, String affiliateId) {
		if (affiliateId != null) intent.putExtra(SDIIntent.Extra.PARTNER, affiliateId);
		start7digitalOrMarket(context, intent);
	}

	/* used all non-search actions (intent passed in will point to the app's external entry point, rather than the main entry point) */
	private static void start7digitalExternalIntent(Context context, Intent intent, String affiliateId) {
		if (affiliateId != null) intent.putExtra(SDIIntent.Extra.PARTNER, affiliateId);
		start7digitalOrMarket(context, intent);
	}

	/**
	 * Start the 7digital application with the given intent. On failure, this will attempt to display the 7digital 
	 * Android app in Google Play to allow for easy installation.
	 * @param context The Context used to launch the 7digital app. Note that if this is not an Activity then the {@link Intent#FLAG_ACTIVITY_NEW_TASK} is automatically added to start the app in a new task.
	 * @param intent The Intent used to start the Activity
	 * @see Context#startActivity(Intent, android.os.Bundle)
	 */
	public static void start7digitalOrMarket(Context context, Intent intent) {
		if (context == null) throw new IllegalArgumentException("Parameter context should not be null.");
		if (intent == null) throw new IllegalArgumentException("Parameter intent should not be null.");
		if (TextUtils.isEmpty(intent.getAction())) throw new IllegalArgumentException("Parameter intent should specify an action to perform.");
		if (!intent.getAction().startsWith(SDIIntent.SDI_ANDROID_PACKAGE_NAME) && !intent.getAction().equals(Intent.ACTION_SEARCH)) throw new IllegalArgumentException("Parameter intent should specify an valid 7digital (or Android search) action to perform.");
		
		// if we weren't started on an activity, supply new task flag
		if (!(context instanceof Activity)) intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		try { context.startActivity(intent); } 
		catch (ActivityNotFoundException e) {
			Toast.makeText(context, "The 7digital application is not installed. Please install it from Google Play.", Toast.LENGTH_LONG).show();
			// Take user to market
			String marketQuery = "market://details?id=" + SDIIntent.SDI_ANDROID_PACKAGE_NAME;
			Intent marketIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(marketQuery));
			try { context.startActivity(marketIntent); } 
			catch (ActivityNotFoundException e1) {
				Toast.makeText(context, "Google Play not found. Please install the 7digital application manually.", Toast.LENGTH_LONG).show();
			}
		}		
	}
	

	/**
	 * Start the 7digital application with the given intent. On failure, this will load up the 7digital website in
	 * the browser.
	 * @param context The Context used to launch the 7digital app. Note that if this is not an Activity then the {@link Intent#FLAG_ACTIVITY_NEW_TASK} is automatically added to start the app in a new task.
	 * @param intent The Intent used to start the Activity
	 * @see Context#startActivity(Intent, android.os.Bundle)
	 */
	public static void start7digitalOrWebsite(Context context, Intent intent) {
		if (context == null) throw new IllegalArgumentException("Parameter context should not be null.");
		if (intent == null) throw new IllegalArgumentException("Parameter intent should not be null.");
		if (TextUtils.isEmpty(intent.getAction())) throw new IllegalArgumentException("Parameter intent should specify an action to perform.");
		if (!intent.getAction().startsWith(SDIIntent.SDI_ANDROID_PACKAGE_NAME) && !intent.getAction().equals(Intent.ACTION_SEARCH)) throw new IllegalArgumentException("Parameter intent should specify an valid 7digital (or Android search) action to perform.");
		
		// if we weren't started on an activity, supply new task flag
		if (!(context instanceof Activity)) intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		try { context.startActivity(intent); } 
		catch (ActivityNotFoundException e) {
			Toast.makeText(context, "The 7digital application is not installed. Redirecting to website.", Toast.LENGTH_LONG).show();
			// Take user to market
			Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(SDIIntent.SDI_WEBSITE_URL));
			try { context.startActivity(browserIntent); } 
			catch (ActivityNotFoundException e1) {
				Toast.makeText(context, "No browser found. Please install one in order to visit the 7digital website.", Toast.LENGTH_LONG).show();
			}
		}		
	}

}
