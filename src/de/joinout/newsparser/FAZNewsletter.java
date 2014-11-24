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

import javax.mail.Message;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class FAZNewsletter extends AbstractNewsletter {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1259210826394590881L;

	public FAZNewsletter(Message message){
		super(message);
		
		if(doc != null){
			doc.select("img[src=http://www.faz.net/img/newsletter/d.gif]").remove();
			for(Element element : doc.select("td")){
				if(element.children().size() == 0 && element.text().equals(""))
					if(element.parents().get(0).children().size() == 1)
						element.parents().get(0).remove();
					else
						element.remove();
			}
			String generalHeading = "";
			for(Element element : doc.select("html > body > table > tbody > tr > td > table > tbody > tr")){
				try{
					if(element.attr("id").matches("anchor.*"))
						generalHeading = element.text();
					else if(element.select("td").get(0).attr("colspan").equals("7")){
						String keyword = element.select(".headline1").get(0).text();
						String heading = element.select(".headline2").get(0).text();
						String zhort = element.select("table > tbody > tr > td").get(0).ownText();
						String readMore = element.select(".more").get(0).attr("href");
						String image = "";
						Elements imageE = element.select(".media");
						if(imageE.size() > 0 )
							image = imageE.get(0).attr("src");

						if(generalHeading.equals("Video des Tages"))
							add("Video", new Advice(keyword, "Video des Tages: " + heading, zhort, readMore, image, "FAZ"));
						
						else if(generalHeading.equals("Politik-Analysen"))
							add("Politik", new Advice(keyword, "Analyse: " + heading, zhort, readMore, image, "FAZ"));
						
						else if(!generalHeading.equals("Empfehlungen des Verlags"))
							add(generalHeading, new Advice(keyword, heading, zhort, readMore, image, "FAZ"));
					}
				} catch(Exception e){
					e.printStackTrace();
					System.out.println(element);
					System.out.println(getLastHeading());
				}
			}
		}
		
	}

}
