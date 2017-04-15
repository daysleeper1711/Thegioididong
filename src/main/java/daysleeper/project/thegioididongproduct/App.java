package daysleeper.project.thegioididongproduct;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class App {

    private final static String BASE = "https://www.thegioididong.com";
    private static String uri;
    private static final Service service = new Service();
    private static int count = 0;
    private static String MANUFACTURER;

    public static void main(String[] args) {
        long start = new Date().getTime();
        fetchingData();
        long end = new Date().getTime();
        System.out.println("Time: " + (end - start) / 1000 + "s");
        System.out.println(">>>> DONE <<<<<");
    }

    private static void fetchingData() {
        System.out.println("-------------------------------------------------");
        System.out.println("| Start getting data from www.thegioididong.com |");
        System.out.println("-------------------------------------------------");
        System.out.println();
        try {
            System.out.println(">>> Request to " + BASE);
            Document doc = Jsoup.connect(BASE)
                    .get();
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            System.out.println(">>> Getting the navigation links...");
            List<String> navLinks = service.navLink(doc);
            if (navLinks.isEmpty()) {
                System.out.println(">>> Nav links is empty");
                System.out.println(">>> Quit now...");
                return;
            }
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            System.out.println(">>> Start getting content...");
            for (String navLink : navLinks) {
                uri = BASE + navLink;
                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                System.out.println(">>> Start request to " + uri);
                doc = Jsoup.connect(uri)
                        .get();
                List<String> manuLinks = service.manuLink(doc);
                for (String manuLink : manuLinks) {
                    //--- extract the manufacture of product
                    String[] manuLinkComponents = manuLink.split("-");
                    if (manuLink.contains("apple")) {
                        MANUFACTURER = upperFirstLetter(manuLinkComponents[1]);
                    } else {
                        MANUFACTURER =  upperFirstLetter(manuLinkComponents[manuLinkComponents.length - 1]);
                    }
                    uri = BASE + manuLink;
                    System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                    System.out.println(">>> Start request to " + uri);
                    doc = Jsoup.connect(uri).get();
                    System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                    System.out.println(">>> Getting link to product");
                    List<String> productLinks = service.productLink(doc);
                    if (productLinks.isEmpty()) {
                        System.out.println("List is empty...<<<<<");
                    }
                    for (String productLink : productLinks) {
                        uri = BASE + productLink;
                        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                        System.out.println(">>> Start request to " + uri);
                        doc = Jsoup.connect(uri).get();
                        String proName = "";
                        String category = "";
                        switch (productLink.split("/")[1]) {
                            case "dtdd":
                                proName += doc.select("aside.picture > img").attr("alt").replace("Điện thoại", "").trim();
                                category += "mobile";
                                break;
                            case "laptop":
                                proName += doc.select("aside.picture > img").attr("alt").replace("Laptop", "").trim();
                                category += "laptop";
                                break;
                            case "may-tinh-bang":
                                proName += doc.select("aside.picture > img").attr("alt").replace("Máy tính bảng", "").trim();
                                category += "tablet";
                                break;
                            default:
                                break;
                        }
                        //--- File handling
                        String filePath = System.getProperty("user.dir")
                                + File.separator
                                + "data" + File.separator
                                + category + File.separator + productLink.split("/")[2];
                        System.out.println("File path: " + filePath);
                        File folder = new File(filePath);
                        folder.mkdirs();
                        File f = new File(folder, productLink.split("/")[2] + ".json");
                        System.out.println(">>> File created: " + f.getAbsolutePath());
                        FileOutputStream fo = new FileOutputStream(f);
                        System.out.println(">>> Start writing...");
                        //--- start json
                        fo.write("{".getBytes("UTF-8"));
                        //--- writing name of product
                        fo.write(("\"name\":" + "\"" + proName + "\",").getBytes("UTF-8"));
                        //--- writing category
                        fo.write(("\"category\":" + "\"" + category + "\",").getBytes("UTF-8"));
                        //--- writing manufacturer
                        fo.write(("\"manufacturer\":" + "\"" + MANUFACTURER + "\",").getBytes("UTF-8"));
                        String price = doc.select("aside.price_sale > div.area_price > strong")
                                .text();
                        if ("".equals(price)) {
                            price = doc.select("aside.price_sale > div.boxshock > div.boxshockheader > label > strong").text();
                        }
                        String priceInput = price.replace("₫", "").replace(".", "").trim();
                        //--- writing price
                        fo.write(("\"price\":" + priceInput + ",").getBytes("UTF-8"));
                        //--- download image
                        String imagePath
                                = service.downloadImage(filePath, doc.select("aside.picture > img").attr("src"));
                        //--- writing image path
                        fo.write(("\"imgPath\":" + "\"" + imagePath + "\",").getBytes("UTF-8"));
                        //--- writing unit
                        fo.write(("\"unit\":" + "\"VND\",").getBytes("UTF-8"));
                        //--- writing specification
                        fo.write(("\"specification\":{").getBytes("UTF-8"));
                        Elements specs = doc.select("div.tableparameter > ul.parameter > li > span");
                        for (Element spec : specs) {
                            String key = spec.text();
                            //--- writing key
                            fo.write(("\"" + key.replace(":", "") + "\":").getBytes("UTF-8"));
                            String value = spec.nextElementSibling().text();
                            if (spec != specs.last()) {
                                //--- not last item in the end of content will be add ","
                                fo.write(("\"" + value.replace("\"", "inch") + "\",")
                                        .getBytes("UTF-8"));
                            } else {
                                fo.write(("\"" + value.replace("\"", "inch") + "\"")
                                        .getBytes("UTF-8"));
                            }
                        }
                        //--- close file json
                        fo.write("}}".getBytes("UTF-8"));
                        fo.close();
                        System.out.println(">>> Stop writing...");
                        count++;
                    }
                }
            }
            System.out.println("Total items: " + count);
        } catch (IOException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(">>> Fail to request");
        }
    }
    
    private static String upperFirstLetter(String input) {
        return input.substring(0,1).toUpperCase().concat(input.substring(1));
    }
}
