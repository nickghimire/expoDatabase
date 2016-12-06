package com.leapfrog.fairdatabaseupwork;

import com.leapfrog.fairdatabaseupwork.util.RegexMatcher;
import java.io.FileWriter;
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
            String maindata = "";
            String empty = " ";
            FileWriter writer = new FileWriter("/users/apple/desktop/expodatabase2.csv");
            for (int i = 1; i <= 753; i++) {
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

                        if (matcher.find()) {

                            String companyName = matcher.group(1).trim().replaceAll(",", "");
                            System.out.println("Company Name :" + companyName);
                            maindata = companyName;

                            String remaindata = matcher.group(2);
                            String regex02 = "City:.*?;\">(.*?)</td>";
                            Matcher matcher02 = RegexMatcher.match(regex02, remaindata.toString());
                            if (matcher02.find()) {
                                String city = matcher02.group(1).trim().replaceAll(",", "");
                                System.out.println("City :" + city);
                                maindata = maindata + "," + city;
                            }else{
                                maindata= maindata+","+empty;
                            }
                            String regex01 = "Country:.*?;\">(.*?)<br>";
                            Matcher matcher01 = RegexMatcher.match(regex01, remaindata.toString());
                            if (matcher01.find()) {
                                String Country = matcher01.group(1).trim().replaceAll(",", "");
                                System.out.println("Country :" + Country);
                                maindata = maindata + "," + Country;
                            }else{
                                maindata= maindata+","+empty;
                            }

                            String Venue = matcher.group(3);
                            String realVenue = Jsoup.parse(Venue).text().replaceAll(",", "");
                            System.out.println("Venue :" + realVenue);
                            maindata = maindata + "," + realVenue;
                        }else{
                            maindata=maindata+","+empty+","+ empty;
                        }
                        String reGex6 = "Organiser/s:</th>.*?ml\">(.*?)</a><br>(.*?)<br>(.*?)<br> Phone:(.*?)<br> Fax:(.*?)<br>";
                        Matcher matcher7 = RegexMatcher.match(reGex6, builder.toString());

                        if (matcher7.find()) {
                            String organisers = matcher7.group(1).trim().replaceAll(",", "");
                            String address = matcher7.group(2).trim().replaceAll(",", "");
                            String phone = matcher7.group(4).trim();
                            String fax = matcher7.group(5).trim();

                            //System.out.println("Code : " + matcher7.group(3));
                           /* String codes = matcher7.group(3);
                            String[] tokens = codes.split(" ");
                            for (int x = 0; x < tokens.length; x++) {
                                for (int j = 0; j <= 9; j++) {
                                    if (tokens[x].contains(Integer.toString(j))) {
                                        String code = tokens[x].replace(",", " ");
                                        System.out.println("Code : " + code);
                                        maindata = maindata + "," + code;
                                        break;
                                    } else {
                                        maindata = maindata + "," + empty;
                                    }
                                }
                            }
                            */
                            maindata = maindata + ","+address + "," + phone + "," + fax + "," + organisers;
                            System.out.println("Organiser/s : " + organisers);

                            System.out.println("Address : " + address);
                            System.out.println("Phone : " + phone);
                            System.out.println("Fax : " + fax);

                        } else {
                            maindata = maindata + "," + empty + "," + empty + "," + empty + "," + empty;
                        }

                        String reGex = "Show type:</th>\\s+<td>(.*?)</td>";
                        Matcher matcher1 = RegexMatcher.match(reGex, builder.toString());

                        if (matcher1.find()) {
                            String showType = matcher1.group(1).trim().replaceAll(",", "");
                            System.out.println("Show Type :" + showType);
                            maindata = maindata + "," + showType;
                        } else {
                            maindata = maindata + "," + empty;
                        }

                        String reGex1 = "Frequency:</th>\\s+<td>(.*?)</td>";
                        Matcher matcher2 = RegexMatcher.match(reGex1, builder.toString());

                        if (matcher2.find()) {
                            String frequency = matcher2.group(1).trim().replaceAll(",", "");
                            System.out.println("Frequency :" + frequency);
                            maindata = maindata + "," + frequency;
                        } else {
                            maindata = maindata + "," + empty;
                        }

                        String reGex2 = "Open to:</th>\\s+<td>(.*?)</td>";
                        Matcher matcher3 = RegexMatcher.match(reGex2, builder.toString());

                        if (matcher3.find()) {
                            String opento = matcher3.group(1).trim().replaceAll(",", "");
                            System.out.println("Open To :" + opento);
                            maindata = maindata + "," + opento;
                        } else {
                            maindata = maindata + "," + empty;

                        }

                        String reGex3 = "Exhibits/main sectors:</th>\\s+<td>(.*?)</td>";
                        Matcher matcher4 = RegexMatcher.match(reGex3, builder.toString());

                        if (matcher4.find()) {
                            String exhibits = matcher4.group(1).trim().replaceAll(",", "");
                            System.out.println("Exhibits/main sectors: " + exhibits);
                            maindata = maindata + "," + exhibits;
                        } else {
                            maindata = maindata + "," + empty;

                        }
                        String reGex4 = "Business sectors:</th>\\s+<td>(.*?)</td>";
                        Matcher matcher5 = RegexMatcher.match(reGex4, builder.toString());

                        if (matcher5.find()) {
                            String sectors = matcher5.group(1).trim().replaceAll(",", "");
                            System.out.println("Business sectors: " + sectors);
                            maindata = maindata + "," + sectors;
                        } else {
                            maindata = maindata + "," + empty;

                        }
                        String reGex5 = "Exhibit space cost \\(per sqm\\):</th>.*?_right border_bottom\">(.*?)</td>";
                        Matcher matcher6 = RegexMatcher.match(reGex5, builder.toString());

                        if (matcher6.find()) {
                            String spaceCost = matcher6.group(1).trim().replaceAll(",", "");
                            System.out.println("Exhibit space cost (per sqm): " + spaceCost);
                            maindata= maindata+","+spaceCost;
                        } else {
                            maindata = maindata + "," + empty;

                        }

                        String reGex7 = "Statistics:</th>.*?left border_bottom\\\">(.*?)</td>.*?bottom\\\">(.*?)</td>.*?tom\">(.*?)</td>.*?Exhibitors.*?bottom\\\">(.*?)</td>.*?ttom\\\">(.*?)</td>.*?tom\">(.*?)</td>.*?Visitors.*?tom\\\">(.*?)</td>.*?tom\\\">(.*?)</td>.*?tom\\\">(.*?)</td>";
                        Matcher matcher8 = RegexMatcher.match(reGex7, builder.toString());

                        if(matcher8.find()) {
                            System.out.println("Statistics:");
                            String Netnational = matcher8.group(1).trim().replaceAll("&nbsp;", "");
                            String NetForegin = matcher8.group(2).trim().replaceAll("&nbsp;", "");
                            String NetTotal = matcher8.group(3).trim().replaceAll("&nbsp;", "");
                            System.out.println("Net sqm National : " + Netnational);
                            System.out.println("Net sqm Foreign : " + NetForegin);
                            System.out.println("Net sqm Total : " + NetTotal);

                            String Exnational = matcher8.group(4).trim().replaceAll("&nbsp;", "");
                            String exForegin = matcher8.group(5).trim().replaceAll("&nbsp;", "");
                            String exTotal = matcher8.group(6).trim().replaceAll("&nbsp;", "");
                            System.out.println("Exhibitors National : " + Exnational);
                            System.out.println("Exhibitors Foreign : " + exForegin);
                            System.out.println("Exhibitors Total : " + exTotal);

                            String Vistnational = matcher8.group(7).trim().replaceAll("&nbsp;", "");
                            String visitForegin = matcher8.group(8).trim().replaceAll("&nbsp;", "");
                            String visitTotal = matcher8.group(9).trim().replaceAll("&nbsp;", "");
                            System.out.println("Visitors National : " + Vistnational);
                            System.out.println("Visitors Foreign : " + visitForegin);
                            System.out.println("Visitors Total : " + visitTotal);
                            
                            maindata= maindata +","+Netnational+","+NetForegin +","+NetTotal +"," +Exnational + ","+ exForegin +","+exTotal +","+Vistnational +","+visitForegin +"," +visitTotal+ "\n";
                        }
                        else{
                            maindata=maindata +","+ empty +","+ empty +","+ empty +"," + empty + ","+ empty +","+ empty +","+ empty +","+ empty +"," + empty + "\n";
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
                     writer.write(maindata);
                }
               

            }
            
            writer.close();

        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());

        }
    }
}
