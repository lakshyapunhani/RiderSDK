package in.roadcast.ridersdk.Utils;

import java.util.Comparator;
import java.util.Currency;
import java.util.Locale;
import java.util.SortedMap;
import java.util.TreeMap;

public class API
{
	public final static String BASE_URL = "https://liveapi.roadcast.co.in/api/v1/";

	public final static String TRACKING_BASE_URL = "https://liveapi.roadcast.co.in/";

    public final static String IMAGE_UPLOAD_RETROFIT=BASE_URL+ "api/v4/index.php/";

    public final static String RETROFIT_RELATIVE_URL="api/v4/index.php/";

	public final static String RELATIVE_BASE_URL = "webserviceos_followme_api/webserviceos_followme_api.php";

	public final static String MAP_GEOCODING_URL = "https://maps.googleapis.com/maps/api/geocode/";


	public final static String TIMED_OUT = "Timed Out";

	static class Utils {
		public static SortedMap<Currency, Locale> currencyLocaleMap;

		static {
			currencyLocaleMap = new TreeMap<Currency, Locale>(new Comparator<Currency>() {
				public int compare(Currency c1, Currency c2) {
					return c1.getCurrencyCode().compareTo(c2.getCurrencyCode());
				}
			});
			for (Locale locale : Locale.getAvailableLocales()) {
				try {
					Currency currency = Currency.getInstance(locale);
					currencyLocaleMap.put(currency, locale);
				} catch (Exception e) {
				}
			}
		}

		public static String getCurrencySymbol(String currencyCode) {
			Currency currency = Currency.getInstance(currencyCode);
			System.out.println(currencyCode + ":-" + currency.getSymbol(currencyLocaleMap.get(currency)));
			return currency.getSymbol(currencyLocaleMap.get(currency));
		}
	}
			
}