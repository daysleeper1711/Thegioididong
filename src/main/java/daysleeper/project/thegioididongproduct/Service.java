package daysleeper.project.thegioididongproduct;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Service {

    public List<String> navLink(Document doc) {
        List<String> links = new ArrayList<>();
        String cssQuery = "div.wrap-main > nav > a";
        for (Element element : doc.select(cssQuery)) {
            if (element.hasClass("mobile") || element.hasClass("tablet") || element.hasClass("laptop")) {
                links.add(element.attr("href"));
            }
        }
        return links;
    }

    public List<String> manuLink(Document doc) {
        List<String> links = new ArrayList<>();
        Elements anchors = doc.select("ul.filter > li.fmanu > div.manufacture > aside > label > a");
        for (Element element : anchors) {
            links.add(element.attr("href"));
        }
        return links;
    }

    public List<String> productLink(Document doc) {
        List<String> links = new ArrayList<>();
        Elements proSelfLink = doc.select("ul.cate.filter-cate > li > a");
        for (Element self : proSelfLink) {
            links.add(self.attr("href"));
        }
        return links;
    }

    public String downloadImage(String folder, String url) {
        //get links to image
        try {
            System.out.println(">>> Get image at: " + url);
            String fName = url.substring(url.lastIndexOf("/") + 1);
            File f = new File(folder, fName);
            FileOutputStream fo = new FileOutputStream(f);
            InputStream is = new URL(url).openStream();
            int i;
            while ((i = is.read()) != -1) {
                fo.write(i);
            }
            fo.close();
            System.out.println(">>> Saved image at: " + f.getAbsolutePath());
            return f.getAbsolutePath().replace("\\", "\\\\");
        } catch (Exception e) {
        }
        return null;
    }
    
    
}
