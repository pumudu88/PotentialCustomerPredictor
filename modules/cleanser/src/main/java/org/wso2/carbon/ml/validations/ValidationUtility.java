package org.wso2.carbon.ml.validations;

import com.maxmind.geoip.Location;
import com.maxmind.geoip.LookupService;
import org.apache.commons.validator.routines.InetAddressValidator;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by pumudu on 12/1/14.
 */
public class ValidationUtility {


    final Map<String, String> countryMap = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
    private static InetAddressValidator ipAddressValidator = new InetAddressValidator();

    public ValidationUtility() {

        countryMap.put("Andorra", "AD");
        countryMap.put("United Arab Emirates", "AE");
        countryMap.put("Afghanistan", "AF");
        countryMap.put("Antigua And Barbuda", "AG");
        countryMap.put("Anguilla", "AI");
        countryMap.put("Albania", "AL");
        countryMap.put("Armenia", "AM");
        countryMap.put("Netherlands Antilles", "AN");
        countryMap.put("Angola", "AO");
        countryMap.put("Antarctica", "AQ");
        countryMap.put("Argentina", "AR");
        countryMap.put("American Samoa", "AS");
        countryMap.put("Austria", "AT");
        countryMap.put("Australia", "AU");
        countryMap.put("Aruba", "AW");
        countryMap.put("Azerbaidjan", "AZ");
        countryMap.put("Bosnia And Herzegovina", "BA");
        countryMap.put("Barbados", "BB");
        countryMap.put("Bangladesh", "BD");
        countryMap.put("Belgium", "BE");
        countryMap.put("Burkina Faso", "BF");
        countryMap.put("Bulgaria", "BG");
        countryMap.put("Bahrain", "BH");
        countryMap.put("Burundi", "BI");
        countryMap.put("Benin", "BJ");
        countryMap.put("Bermuda", "BM");
        countryMap.put("Brunei", "BN");
        countryMap.put("Bolivia", "BO");
        countryMap.put("Brazil", "BR");
        countryMap.put("Bahamas", "BS");
        countryMap.put("Bhutan", "BT");
        countryMap.put("Bouvet Island", "BV");
        countryMap.put("Botswana", "BW");
        countryMap.put("Belarus", "BY");
        countryMap.put("Belize", "BZ");
        countryMap.put("Canada", "CA");
        countryMap.put("Cocos (Keeling) Islands", "CC");
        countryMap.put("Central African Republic", "CF");
        countryMap.put("Congo, The Democratic Republic Of The", "CD");
        countryMap.put("Congo", "CG");
        countryMap.put("Switzerland", "CH");
        countryMap.put("Cote D'Ivoire", "CI");
        countryMap.put("Cook Islands", "CK");
        countryMap.put("Chile", "CL");
        countryMap.put("Cameroon", "CM");
        countryMap.put("China", "CN");
        countryMap.put("Colombia", "CO");
        countryMap.put("Costa Rica", "CR");
        countryMap.put("Former Czechoslovakia", "CS");
        countryMap.put("Cuba", "CU");
        countryMap.put("Cape Verde", "CV");
        countryMap.put("Christmas Island", "CX");
        countryMap.put("Cyprus", "CY");
        countryMap.put("Czech Republic", "CZ");
        countryMap.put("Germany", "DE");
        countryMap.put("Djibouti", "DJ");
        countryMap.put("Denmark", "DK");
        countryMap.put("Dominica", "DM");
        countryMap.put("Dominican Republic", "DO");
        countryMap.put("Algeria", "DZ");
        countryMap.put("Ecuador", "EC");
        countryMap.put("Estonia", "EE");
        countryMap.put("Egypt", "EG");
        countryMap.put("Western Sahara", "EH");
        countryMap.put("Eritrea", "ER");
        countryMap.put("Spain", "ES");
        countryMap.put("Ethiopia", "ET");
        countryMap.put("Finland", "FI");
        countryMap.put("Fiji", "FJ");
        countryMap.put("Falkland Islands", "FK");
        countryMap.put("Micronesia", "FM");
        countryMap.put("Faroe Islands", "FO");
        countryMap.put("France", "FR");
        countryMap.put("France (European Territory)", "FX");
        countryMap.put("Gabon", "GA");
        countryMap.put("Great Britain", "UK");
        countryMap.put("Grenada", "GD");
        countryMap.put("Georgia", "GE");
        countryMap.put("French Guiana", "GF");
        countryMap.put("Ghana", "GH");
        countryMap.put("Gibraltar", "GI");
        countryMap.put("Greenland", "GL");
        countryMap.put("Gambia", "GM");
        countryMap.put("Guinea", "GN");
        countryMap.put("Guadeloupe", "GP");
        countryMap.put("Equatorial Guinea", "GQ");
        countryMap.put("Greece", "GR");
        countryMap.put("S. Georgia & S. Sandwich Isls.", "GS");
        countryMap.put("Guatemala", "GT");
        countryMap.put("Guam (USA)", "GU");
        countryMap.put("Guinea-Bissau", "GW");
        countryMap.put("Guyana", "GY");
        countryMap.put("Hong Kong", "HK");
        countryMap.put("Heard Island And McDonald Islands", "HM");
        countryMap.put("Honduras", "HN");
        countryMap.put("Croatia", "HR");
        countryMap.put("Haiti", "HT");
        countryMap.put("Hungary", "HU");
        countryMap.put("Indonesia", "ID");
        countryMap.put("Ireland", "IE");
        countryMap.put("Israel", "IL");
        countryMap.put("India", "IN");
        countryMap.put("British Indian Ocean Territory", "IO");
        countryMap.put("Iraq", "IQ");
        countryMap.put("Iran", "IR");
        countryMap.put("Iceland", "IS");
        countryMap.put("Italy", "IT");
        countryMap.put("Jamaica", "JM");
        countryMap.put("Jordan", "JO");
        countryMap.put("Japan", "JP");
        countryMap.put("Kenya", "KE");
        countryMap.put("Kyrgyz Republic (Kyrgyzstan)", "KG");
        countryMap.put("Cambodia, Kingdom Of", "KH");
        countryMap.put("Kiribati", "KI");
        countryMap.put("Comoros", "KM");
        countryMap.put("Saint Kitts & Nevis Anguilla", "KN");
        countryMap.put("North Korea", "KP");
        countryMap.put("South Korea", "KR");
        countryMap.put("Kuwait", "KW");
        countryMap.put("Cayman Islands", "KY");
        countryMap.put("Kazakhstan", "KZ");
        countryMap.put("Laos", "LA");
        countryMap.put("Lebanon", "LB");
        countryMap.put("Saint Lucia", "LC");
        countryMap.put("Liechtenstein", "LI");
        countryMap.put("Sri Lanka", "LK");
        countryMap.put("Liberia", "LR");
        countryMap.put("Lesotho", "LS");
        countryMap.put("Lithuania", "LT");
        countryMap.put("Luxembourg", "LU");
        countryMap.put("Latvia", "LV");
        countryMap.put("Libyan Arab Jamahiriya", "LY");
        countryMap.put("Morocco", "MA");
        countryMap.put("Monaco", "MC");
        countryMap.put("Moldavia", "MD");
        countryMap.put("Madagascar", "MG");
        countryMap.put("Marshall Islands", "MH");
        countryMap.put("Macedonia", "MK");
        countryMap.put("Mali", "ML");
        countryMap.put("Myanmar", "MM");
        countryMap.put("Mongolia", "MN");
        countryMap.put("Macau", "MO");
        countryMap.put("Northern Mariana Islands", "MP");
        countryMap.put("Martinique (French)", "MQ");
        countryMap.put("Mauritania", "MR");
        countryMap.put("Montserrat", "MS");
        countryMap.put("Malta", "MT");
        countryMap.put("Mauritius", "MU");
        countryMap.put("Maldives", "MV");
        countryMap.put("Malawi", "MW");
        countryMap.put("Mexico", "MX");
        countryMap.put("Malaysia", "MY");
        countryMap.put("Mozambique", "MZ");
        countryMap.put("Namibia", "NA");
        countryMap.put("New Caledonia (French)", "NC");
        countryMap.put("Niger", "NE");
        countryMap.put("Norfolk Island", "NF");
        countryMap.put("Nigeria", "NG");
        countryMap.put("Nicaragua", "NI");
        countryMap.put("Netherlands", "NL");
        countryMap.put("Norway", "NO");
        countryMap.put("Nepal", "NP");
        countryMap.put("Nauru", "NR");
        countryMap.put("Neutral Zone", "NT");
        countryMap.put("Niue", "NU");
        countryMap.put("New Zealand", "NZ");
        countryMap.put("Oman", "OM");
        countryMap.put("Panama", "PA");
        countryMap.put("Peru", "PE");
        countryMap.put("French Polynesia", "PF");
        countryMap.put("Papua New Guinea", "PG");
        countryMap.put("Philippines", "PH");
        countryMap.put("Pakistan", "PK");
        countryMap.put("Poland", "PL");
        countryMap.put("Saint Pierre And Miquelon", "PM");
        countryMap.put("Pitcairn Island", "PN");
        countryMap.put("Puerto Rico", "PR");
        countryMap.put("Portugal", "PT");
        countryMap.put("Palau", "PW");
        countryMap.put("Paraguay", "PY");
        countryMap.put("Qatar", "QA");
        countryMap.put("Reunion (French)", "RE");
        countryMap.put("Romania", "RO");
        countryMap.put("Russian Federation", "RU");
        countryMap.put("Rwanda", "RW");
        countryMap.put("Saudi Arabia", "SA");
        countryMap.put("Solomon Islands", "SB");
        countryMap.put("Seychelles", "SC");
        countryMap.put("Sudan", "SD");
        countryMap.put("Sweden", "SE");
        countryMap.put("Singapore", "SG");
        countryMap.put("Saint Helena", "SH");
        countryMap.put("Slovenia", "SI");
        countryMap.put("Svalbard And Jan Mayen Islands", "SJ");
        countryMap.put("Slovak Republic", "SK");
        countryMap.put("Sierra Leone", "SL");
        countryMap.put("San Marino", "SM");
        countryMap.put("Senegal", "SN");
        countryMap.put("Somalia", "SO");
        countryMap.put("Suriname", "SR");
        countryMap.put("Sao Tome and Principe", "ST");
        countryMap.put("Former USSR", "SU");
        countryMap.put("El Salvador", "SV");
        countryMap.put("Syria", "SY");
        countryMap.put("Swaziland", "SZ");
        countryMap.put("Turks And Caicos Islands", "TC");
        countryMap.put("Chad", "TD");
        countryMap.put("French Southern Territories", "TF");
        countryMap.put("Togo", "TG");
        countryMap.put("Thailand", "TH");
        countryMap.put("Tadjikistan", "TJ");
        countryMap.put("Tokelau", "TK");
        countryMap.put("Turkmenistan", "TM");
        countryMap.put("Tunisia", "TN");
        countryMap.put("Tonga", "TO");
        countryMap.put("East Timor", "TP");
        countryMap.put("Turkey", "TR");
        countryMap.put("Trinidad And Tobago", "TT");
        countryMap.put("Tuvalu", "TV");
        countryMap.put("Taiwan", "TW");
        countryMap.put("Tanzania", "TZ");
        countryMap.put("Ukraine", "UA");
        countryMap.put("Uganda", "UG");
        countryMap.put("United Kingdom", "GB");
        countryMap.put("United States Minor Outlying Islands", "US");
        countryMap.put("United States", "US");
        countryMap.put("Uruguay", "UY");
        countryMap.put("Uzbekistan", "UZ");
        countryMap.put("Holy See (Vatican City State)", "VA");
        countryMap.put("Saint Vincent & Grenadines", "VC");
        countryMap.put("Venezuela", "VE");
        countryMap.put("British Virgin Islands", "VG");
        countryMap.put("Virgin Islands", "VI");
        countryMap.put("Vietnam", "VN");
        countryMap.put("Vanuatu", "VU");
        countryMap.put("Wallis And Futuna", "WF");
        countryMap.put("Samoa", "WS");
        countryMap.put("Yemen", "YE");
        countryMap.put("Mayotte", "YT");
        countryMap.put("Yugoslavia", "YU");
        countryMap.put("South Africa", "ZA");
        countryMap.put("Zambia", "ZM");
        countryMap.put("Zaire", "ZR");
        countryMap.put("Zimbabwe", "ZW");

    }

    /**
     * Return true if ip address matches with given country name, else return false.
     * @param ipAddress
     * @param countryName
     * @return
     */
    public synchronized Boolean countryByIpAddressValidation(String ipAddress, String countryName) {

        LookupService countryLocation = null;
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        Location ipLocation = null;

        Boolean returnValue;

        try {
            countryLocation = new LookupService(classloader.getResource("GeoLiteCity.dat").getPath(), LookupService.GEOIP_CHECK_CACHE);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (ipAddressValidator.isValid(ipAddress)) {

            ipLocation = countryLocation.getLocation(ipAddress);

//            System.out.println( "country name from csv   : " + countryName);
//            System.out.println( "country code using name : " +getCountryCode(countryName));
//            System.out.println( "country name using ip   : " +ipLocation.countryName);
//            System.out.println( "country code using ip   : "+ipLocation.countryCode);
            if (ipLocation.countryCode.equals(getCountryCode(countryName))) {
                returnValue = true;
            } else {
                returnValue = false;
            }

        } else {
            returnValue = false;
        }


        countryLocation.close();

        return returnValue;
    }

    /**
     * Return country code for given country name.
     * @param country
     * @return
     */
    public synchronized String getCountryCode(String country) {
        String countryCode = countryMap.get(country);
        if (countryCode == null) {
            countryCode = "Not Found";
        }
        return countryCode;
    }


}
