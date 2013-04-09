/**
 * This 7digital Android app partner SDK contains functionality to simplify interaction with the features the app
 * provides, levering Android's Intent mechanism.
 * <p>
 * Several common actions, including performing a search, displaying release details, different areas of the app 
 * (e.g. a user's downloads) and playing or previewing individual tracks and full releases. For an overview  and 
 * full documentation on all actions, refer to {@link uk.co.sevendigital.android.partner.sdk.SDIIntent.Action}.
 * <p>
 * Although Intents for performing actions on the 7digital Android app can be constructed manually, it is recommended
 * to take advantage of the provided methods in {@link uk.co.sevendigital.android.partner.sdk.SDIPartnerUtil}, which
 * could be considered as 'shortcuts', or the build methods in {@link uk.co.sevendigital.android.partner.sdk.SDIIntent.Builder}.
 * The build methods return Intents with parameters tailored for the relevant action and can be further customized if
 * required. The same build methods are also used by the different view calls.
 * <p>
 * <p>
 * <p>
 * Another way to tap in the information the 7digital app offers, is by means of the in-app's sharing feature. Whenever
 * a user shares an album, track or artist, extra information is attached to the share Intent as key-value pairs. Each of
 * the parameters is optional and thus presence may vary depending on where the share action originated from. For example:
 * track or release information will not be passed in when an artist gets shared. However, the opposite is generally true:
 * a release or track share action normally includes information about the artist of that track/release. 
 * <p>
 * The extras use the same keys as the app accepts for the different actions:
 * <p>
 * <ul>
 * <li>{@link uk.co.sevendigital.android.partner.sdk.SDIIntent.Extra#ARTISTNAME} - a String describing the name of the artist.</li>
 * <li>{@link uk.co.sevendigital.android.partner.sdk.SDIIntent.Extra#ARTISTID} - a Long describing the 7digital ID for the artist.</li>
 * <li>{@link uk.co.sevendigital.android.partner.sdk.SDIIntent.Extra#RELEASETITLE} - a String describing the title of the release.</li>
 * <li>{@link uk.co.sevendigital.android.partner.sdk.SDIIntent.Extra#RELEASEID} - a Long describing the 7digital ID for the release.</li>
 * <li>{@link uk.co.sevendigital.android.partner.sdk.SDIIntent.Extra#TRACKTITLE} - a String describing the title of the track.</li>
 * <li>{@link uk.co.sevendigital.android.partner.sdk.SDIIntent.Extra#TRACKID} - a Long describing the 7digital ID for the track.</li>
 * <li>{@link uk.co.sevendigital.android.partner.sdk.SDIIntent.Extra#TRACKVERSION} - a String describing the version of the track, if available.</li>
 * <li>{@link uk.co.sevendigital.android.partner.sdk.SDIIntent.Extra#COVERURL} - a String describing the location of the 200 x 200 pixels album cover, if available.</li>
 * </ul>
 * <p>
 * <p>
 * This SDK can be built and added to an existing Android project as stand-alone Java archive (.jar) or as an Android library project. 
 */
package uk.co.sevendigital.android.partner.sdk;


