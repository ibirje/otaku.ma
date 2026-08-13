
export class ServerSettings {

    public static MINUTE = 60000;
    public static HEURE = 3600000;

    public static LOCAL_BASE_URL = 'http://localhost:8080/app/public/';
    public static WEB_BASE_URL   = 'https://otaku-221716.appspot.com/app/public/';

    public static BASE_URL = ServerSettings.WEB_BASE_URL;
    // public static BASE_URL = ServerSettings.LOCAL_BASE_URL;

    public static CACHE_TIMEOUT = 0 * ServerSettings.HEURE + 30 * ServerSettings.MINUTE;
}
