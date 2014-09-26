package sit.kmutt.com.ureportapp.vo;

/**
 * Created by Thanabut on 9/9/2557.
 */
public class UrlService {
    String headUrl = "http://192.168.183.202/ureportservice/";
    SessionManager ipMan;

    public UrlService(){

    }

    public UrlService(SessionManager ipMan) {
        this.ipMan = ipMan;
        setIp(ipMan.getIp());
    }

    public String getHeadUrl() {
        return headUrl;
    }
    public String getHeadUrl(String s) {
        return headUrl+s;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public void setIp(String ip){
        this.headUrl ="http://" + ip + "/service/";
    }
}
