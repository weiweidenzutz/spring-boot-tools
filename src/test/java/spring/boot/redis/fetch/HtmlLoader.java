package spring.boot.redis.fetch;

import java.io.IOException;

import org.jsoup.Jsoup;

public class HtmlLoader {

    public String loadUrl(String newUrl) {
        try {
            return Jsoup.connect(newUrl).post().toString();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

}