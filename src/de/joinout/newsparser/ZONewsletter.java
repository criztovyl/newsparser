/**
	This is a program to keep an overview over your news.
    Copyright (C) 2014 Christoph "criztovyl" Schulz

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

import javax.mail.Message;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ZONewsletter extends AbstractNewsletter{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4951315869497006644L;
	private Logger logger;

	public ZONewsletter(Message message){

		super(message);

		logger = LogManager.getLogger();

		if(doc != null){

			try{

				Elements title1 = doc.select("html body title");

				Elements title2 = doc.select("html body table tbody tr td table#dynamic1.dynamic tbody tr td table "
						+ "tbody tr td table tbody tr td table tbody tr td table#textAndImage2.textAndImage "
						+ "tbody tr td table tbody tr td font span");
				//Check if is "ZEIT für euch"
				if(title1.size() > 0 && title1.get(0).text().equals("Zeit Online GmbH - Schüler Newsletter")){
					logger.warn("\"ZEIT für euch\" is currently not supported.");
				}

				//Check if is "Fünf vor 8:00"
				else if(title2.size() > 0 && title2.get(0).text().equals(" FÜNF VOR 8:00")){
					logger.debug("\"Fünf for 8:00\" newsletter.");

					//Get date
					String date = doc.select("html body table tbody tr td table#dynamic1.dynamic tbody "
							+ "tr td table tbody tr td table tbody tr td table tbody tr td "
							+ "table#textAndImage3.textAndImage tbody tr td table tbody tr td font div "
							+ "font span span").text();

					Document doc2 = Jsoup.parse(new URL("http://www.zeit.de/serie/fuenf-vor-acht"), 5000);
					
					Elements teasers = doc2.select(
							"div.outerspace div#wrapper.wrapper.innerspace #content.section "
							+ "#main .teaserlist .teaser");
					
					logger.debug("Teaser size: {}", teasers.size());

					boolean found = false;
					
					for(Element teaser : teasers){
						
						if(teaser.select(".meta").get(0).text().startsWith(date)){
							logger.debug("Meta!");
							add("Politik", new Advice(
									teaser.select(".supertitle").get(0).text(),
									"Fünf vor 8.00: " + teaser.select(".title").get(0).text(),
									teaser.select(".innerteaser p").get(1).text(),
									teaser.select(".innerteaser a").get(0).attr("href"),
									teaser.select(".teaser-image-wrap img").get(0).attr("src"), 
									"ZON"));
						}
					}
					if(!found){
						logger.warn("\"Fünf vor 8.00\" {} not found.", date);
					}

					//add("Politik", new Advice(keyword, adv_heading, zhort, readMore, image, "ZON"));
				}
				else{
					
					logger.debug("Normal Newsletter");

					//Remove tracking pixels and unneeded images
					doc.select("img[width=1]").select("img[height=1]").remove();
					doc.select("img[src=http://images.zeit.de/bilder/elemente/homepage/transparent_pixel.gif]").remove();
					
					//Remove footer
					doc.select("html > body > table > tbody > tr").get(1).remove();

					String generalHeading = "";
					//Select elements which contains "news"
					for(Element element : doc.select("html > body > table > tbody > tr > td > table > tbody > tr")){

						//Check if is heading
						if(element.select("a[name]").size() != 0){
							generalHeading = element.text();
						}
						//Check if is "news" and create advice if so
						else if(element.children().size() == 2){

							Elements parts = element.select("td");
							String keyword = parts.get(1).select("font").get(0).text().replaceAll(":$", "");
							String adv_heading = parts.get(1).select("font").get(1).text();
							String zhort = parts.get(1).select("font").text();
							String readMore = parts.get(1).select("a").get(0).attr("href");
							String image = parts.get(0).select("img").get(0).attr("src");

							if(generalHeading.equals("Kultur"))
								add("Feuilleton", new Advice(keyword, adv_heading, zhort, readMore, image, "ZON"));

							else
								add(generalHeading, new Advice(keyword, adv_heading, zhort, readMore, image, "ZON"));

						}
					}
				}

			} catch(Exception e){
				logger.catching(e);
				//logger.info(doc.toString());
			}
		}
		else
			logger.warn("Parsed document is null.");
	}
}
