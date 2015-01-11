/**
	This is a program to keep an overview over your news.
    Copyright (C) 2014, 2015 Christoph "criztovyl" Schulz

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.joinout.newsparser;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

import javax.mail.Message;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
/**
 * A class to parse (some) newsletters from "ZEIT ONLINE" (by "DIE ZEIT"; <a href="http://zeit.de">ZEIT ONLINE</a>).
 * @author Christoph "criztovyl" Schulz
 * TODO: Be Fair; Include advertisements, Live Blogs/Eilmeldungen
 */
public class ZONewsletter extends AbstractNewsletter{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4951315869497006644L;
	private static String name = "ZON";
	private Logger logger;

	/**
	 * Creates a parser that parses a message.
	 * @param message the message
	 */
	public ZONewsletter(Message message){

		super(message);

		//Init logger
		logger = LogManager.getLogger();
		//Set up ignored headings
		ArrayList<String> ignored_headings = new ArrayList<>(Arrays.asList(new String[]{"ENTDECKEN SIE UNSERE APPS", "VERWALTUNG", "WERBEKUNDEN", "WERBEKUNDEN", "WERBEKUNDEN"}));

		//Check if document isn't null
		if(getDocument() != null){

			try{

				// Select titles for detecting newsletter type
				//
				Elements title1 = getDocument().select("html body title");

				Elements title2 = getDocument().select("html body table tbody tr td table#dynamic1.dynamic tbody tr td table "
						+ "tbody tr td table tbody tr td table tbody tr td table#textAndImage2.textAndImage "
						+ "tbody tr td table tbody tr td font span");
				
				//Check if is "ZEIT für euch"; not implemented yet.
				if(title1.size() > 0 && title1.get(0).text().equals("Zeit Online GmbH - Schüler Newsletter")){
					logger.warn("\"ZEIT für euch\" is currently not supported.");
				}
				//Check if is "Fünf vor 8:00"
				else if(title2.size() > 0 && title2.get(0).text().equals(" FÜNF VOR 8:00")){
					logger.debug("\"Fünf for 8:00\" newsletter.");

					//Get date
					String date = getDocument().select("html body table tbody tr td table#dynamic1.dynamic tbody "
							+ "tr td table tbody tr td table tbody tr td table tbody tr td "
							+ "table#textAndImage3.textAndImage tbody tr td table tbody tr td font div "
							+ "font span span").text();

					//Details are only on category page
					Document doc2 = Jsoup.parse(new URL("http://www.zeit.de/serie/fuenf-vor-acht"), 5000);
					
					//Select teasers
					Elements teasers = doc2.select(
							"div.outerspace div#wrapper.wrapper.innerspace #content.section "
							+ "#main .teaserlist .teaser");
					
					logger.debug("Teaser size: {}", teasers.size());

					boolean found = false;
					
					//Search for this newsletter
					for(Element teaser : teasers){
						
						Elements meta = teaser.select(".meta");
						
						//If found .meta, check if starts with date
						if(meta.size() > 0 && meta.get(0).text().startsWith(date)){
							
							logger.debug("Found teaser meta :)");
							
							//If so, add advice.
							add("Politik", new Advice(
									teaser.select(".supertitle").get(0).text(),
									"Fünf vor 8.00: " + teaser.select(".title").get(0).text(),
									teaser.select(".innerteaser p").get(1).text(),
									teaser.select(".innerteaser a").get(0).attr("href"),
									teaser.select(".teaser-image-wrap img").get(0).attr("src"), 
									"ZON"));
							found = true;
						}
					}
					if(!found){
						logger.warn("\"Fünf vor 8.00\" {} not found.", date);
					}
				}
				//Take as normal newsletter
				else{
					
					logger.debug("Normal Newsletter");

					String generalHeading = "";
					
					//Select advice elements
					Elements elements = getDocument().select("html > body > div.desktopversion > center > table > tbody > tr > td > table > tbody > tr");
					
					//Select date and split by " " (written as "DAY. MONTNAME YEAR")
					String[] date = elements.get(2).text().split(",")[0].replaceAll("TÄGLICHER NEWSLETTER", "").trim().split(" ");
					
					//Get date
					Calendar cal = Calendar.getInstance();
					cal.clear();
					cal.set(
							Integer.parseInt(date[2]),
							Calendar.getInstance().getDisplayNames(Calendar.MONTH, Calendar.LONG_FORMAT, Locale.GERMANY).get(date[1]), //Need to get from Germany name
							Integer.parseInt(date[0].replaceAll("\\.", ""))
							);
					
					logger.debug("Date: {}", String.format("%tD", cal));
					
					//Select elements which contains "news"
					for(Element element : elements){
						
						//Select the advice
						Elements advice = element.select("td > table > tbody > tr");

						//Check if is heading
						if(element.select("td > a > img").size() == 0 && element.select("td > b > font").size() == 1){

							//If so, set heading
							generalHeading = element.text();
							logger.trace("Heading Element: {}", generalHeading);
						}
						//Check if heading is not interesting (address, advertisement details,...)
						else if(ignored_headings.contains(generalHeading)){
							continue;
						}
						//Check if is "news" and create advice if so
						else if(advice.size() != 0 && advice.select(" tr > td").size() != 2){
							
							logger.trace("Advice Element");
							
							try {
								
								Elements parts = advice.select("td font");
								String keyword = parts.get(0).text();
								String adv_heading = parts.get(2).text();
								String zhort = parts.get(3).text();
								String readMore = "";
								String image = element.select("td > a > img").attr("src");

								//"Kultur" is renamed to "Feuilleton".
								if(generalHeading.equals("Kultur"))
									add("Feuilleton", new Advice(keyword, adv_heading, zhort, readMore, image, name, cal));
								else
									add(generalHeading, new Advice(keyword, adv_heading, zhort, readMore, image, name, cal));
								
							} catch (Exception e) {
								
								logger.error("Failed to create advice. Catching exception and will write message body to a file.");							
								logger.catching(e);
								if(!advice.toString().contains("Commercial break text"))
									logger.debug("Advice element:\n{}", advice.toString());
								
								writeMessageDocumentToFile();	
							}

						}
						else
							logger.trace("Unknown Element.");
					}
				}

			} catch(Exception e){
				
				logger.catching(e);	
				writeMessageDocumentToFile();
			}
		}
		else
			logger.warn("Parsed document is null.");
	}
}
