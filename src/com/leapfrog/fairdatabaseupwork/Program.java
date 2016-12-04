package com.leapfrog.fairdatabaseupwork;

import com.leapfrog.fairdatabaseupwork.util.RegexMatcher;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Program {

    private static final String base_url = "http://www.expodatabase.com/aussteller/messen/index.php?OK=1&sortierid=0&maxPerPage=20&i_cockpitkeyfindwo=2&i_cockpitkeyfindart=1&currPage=";

    public static void main(String[] args) {

        try {
            int counter = 1;
            RegexMatcher regexMatcher = new RegexMatcher();
            for (int i = 1; i <= 5; i++) {
                String main_url = base_url + i;

                Document docs = Jsoup.connect(main_url).timeout(0).get();
                Elements allelements = docs.select("div.search_result_list_box div.firma a");

                for (Element e : allelements) {

                    String innerlink = e.attr("href");
                    String formatlink = "http://www.expodatabase.com/aussteller/messen/" + innerlink;
                    Document innerdoc = Jsoup.connect(formatlink).timeout(0).get();
                    // System.out.println(innerdoc.toString());
                    Elements realdata = innerdoc.select("div.messe_treffer_middle ");
                    for (Element e1 : realdata) {
                        StringBuilder builder = new StringBuilder();
                        String content = e1.html();
                        Scanner scanner = new Scanner(content);
                        while (scanner.hasNextLine()) {
                            String line = scanner.nextLine();
                            builder.append(line);
                        }
                        //System.out.println(builder.toString());
                        String regex = "<div class=\"messe_headline\"> <h1>(.*?)</h1>(.*?)Venue:</th>\\s+<td style=\"width: 135px;\">(.*?)</td>";
                        Matcher matcher = RegexMatcher.match(regex, builder.toString());

                        while (matcher.find()) {

                            String companyName = matcher.group(1);
                            System.out.println("Company Name :" + companyName);
                            String Venue = matcher.group(3);
                            String realVenue = Jsoup.parse(Venue).text();
                            System.out.println("Venue :" + realVenue);
                        }

                        String reGex = "Show type:</th>\\s+<td>(.*?)</td>";
                        Matcher matcher1 = RegexMatcher.match(reGex, builder.toString());

                        while (matcher1.find()) {
                            System.out.println("Show Type :" + matcher1.group(1));

                        }

                        String reGex1 = "Frequency:</th>\\s+<td>(.*?)</td>";
                        Matcher matcher2 = RegexMatcher.match(reGex1, builder.toString());

                        while (matcher2.find()) {
                            System.out.println("Frequency :" + matcher2.group(1));
                        }

                        String reGex2 = "Open to:</th>\\s+<td>(.*?)</td>";
                        Matcher matcher3 = RegexMatcher.match(reGex2, builder.toString());

                        while (matcher3.find()) {
                            System.out.println("Open To :" + matcher3.group(1));

                        }
                        String reGex3 = "Exhibits/main sectors:</th>\\s+<td>(.*?)</td>";
                        Matcher matcher4 = RegexMatcher.match(reGex3, builder.toString());

                        while (matcher4.find()) {
                            System.out.println("Exhibits/main sectors: " + matcher4.group(1));

                        }
                        String reGex4 = "Business sectors:</th>\\s+<td>(.*?)</td>";
                        Matcher matcher5 = RegexMatcher.match(reGex4, builder.toString());

                        while (matcher5.find()) {
                            System.out.println("Business sectors: " + matcher5.group(1));

                        }
                        String reGex5 = "Exhibit space cost \\(per sqm\\):</th>.*?_right border_bottom\">(.*?)</td>";
                        Matcher matcher6 = RegexMatcher.match(reGex5, builder.toString());

                        while (matcher6.find()) {
                            System.out.println("Exhibit space cost (per sqm): " + matcher6.group(1));

                        }
                        String reGex6 = "Organiser/s:</th>.*?ml\">(.*?)</a><br>(.*?)<br>(.*?)<br> Phone:(.*?)<br> Fax:(.*?)<br>";
                        Matcher matcher7 = RegexMatcher.match(reGex6, builder.toString());

                        while (matcher7.find()) {
                            System.out.println("Organiser/s : " + matcher7.group(1));
                            System.out.println("Address : " + matcher7.group(2));
                            System.out.println("Code :" + matcher7.group(3));
                            System.out.println("Phone : " + matcher7.group(4));
                            System.out.println("Fax : "+matcher7.group(5));

                        }

                        String reGex7 = "Statistics:</th>.*?left border_bottom\\\">(.*?)</td>.*?bottom\\\">(.*?)</td>.*?tom\">(.*?)</td>.*?Exhibitors.*?bottom\\\">(.*?)</td>.*?ttom\\\">(.*?)</td>.*?tom\">(.*?)</td>.*?Visitors.*?tom\\\">(.*?)</td>.*?tom\\\">(.*?)</td>.*?tom\\\">(.*?)</td>";
                        Matcher matcher8 = RegexMatcher.match(reGex7, builder.toString());

                        while (matcher8.find()) {
                            System.out.println("Statistics:");
                            System.out.println("Net sqm National : " + matcher8.group(1));
                            System.out.println("Net sqm Foreign : " + matcher8.group(2));
                            System.out.println("Net sqm Total : " + matcher8.group(3));

                            System.out.println("Exhibitors National :" + matcher8.group(4));
                            System.out.println("Exhibitors Foreign : " + matcher8.group(5));
                            System.out.println("Exhibitors Total : " + matcher8.group(6));

                            
                            System.out.println("Visitors National : " + matcher8.group(7));
                            System.out.println("Visitors Foreign : " + matcher8.group(8));
                             System.out.println("Visitors Total : " + matcher8.group(9));

                        }

                        /*
                        Elements trElement = e1.select("div.messe_content_header tbody tr");
                        // for(int j=1;j<=trElement.size();j++){
                        //if (trElement.size() > 4) {
                            // Element Venue = trElement.select("tr");
                            //for (Element venue : trElement) {
                               //venue.select("tr:eq(4)");

                       int num=trElement.size();
                        for(Element Venvue: trElement){
                            String venue=Venvue.select("th").text();
                            if(venue.contains("Venue:")){
                        ==============================================================
                       
                        if(num>5 ){
                            Elements Venue = trElement.select("tr:eq(3)");
                            System.out.println(Venue.text());
                            
                        }else{
                             System.out.println(num);
                            Elements Venue1= trElement.select("tr:eq(2)");
                            System.out.println(Venue1.text());
                        }
                         */
                        System.out.println("==================" + counter + "=============");

                    }
                    counter++;
                }

            }

        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());

        }
    }
}
