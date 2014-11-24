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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.FlagTerm;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.joinout.criztovyl.tools.log4j.Log4JEnvironment;

public class Mail {

	public static void main(String args[]) {
		Properties props = System.getProperties();
		props.setProperty("mail.store.protocol", "imaps");
		
		new Log4JEnvironment();
		
		Logger logger = LogManager.getLogger();
		
		News news = new News();
		

		
		try {
			Session session = Session.getDefaultInstance(props, null);
			Store store = session.getStore("imaps");
			store.connect("mail.joinout.de", "ch.schulz@joinout.de", FileUtils.readFileToString(new File("/home/christoph/emailpass_ch.schulz@joinout.de")));
			logger.info("Store: {}", store);
			
			logger.info("Opening FAZ");

			Folder fazFolder = store.getFolder("INBOX.Newsletter.FAZ");
			fazFolder.open(Folder.READ_WRITE);
			
			Message[] faz_messages = fazFolder.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
			
			logger.info("Searched messages");
			
			ArrayList<FAZNewsletter> faz = new ArrayList<>();
			
			logger.info("Iterating over messages");
			for(Message message : faz_messages){
				
				FAZNewsletter faz_ = new FAZNewsletter(message);
				
				for(String key : faz_.keySet())
					news.add(key, faz_.get(key));
				
				logger.info("Parsed Message");
			}
			logger.info("{} FAZ Mails", faz.size());
			
			logger.info("Opening ZO");
			
			Folder zoFolder = store.getFolder("INBOX.Newsletter.ZEIT ONLINE Newsletter");
			zoFolder.open(Folder.READ_WRITE);
			
			Message[] zo_messages = zoFolder.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
			
			logger.info("Searched messages");
			
			ArrayList<ZONewsletter> zo = new ArrayList<>();
			
			logger.info("Iterating over messages");
			
			for(Message message : zo_messages){

				ZONewsletter zo_ = new ZONewsletter(message);

				for(String key : zo_.keySet())
					news.add(key, zo_.get(key));
				
				logger.info("Parsed message");
			}
			logger.info("{} ZO messages", zo.size());
			
			for(String key : news.keySet()){
				System.out.println(key);
				for(Advice advice : news.get(key))
					System.out.println(String.format("\t(%s) %s", advice.getSource(), advice.getHeading()));
			}
			
			
//			String messageS = "";
//			
//			if(message.getContent() instanceof MimeMultipart){
//				MimeMultipart mmp = (MimeMultipart) message.getContent();
//				messageS = (String) mmp.getBodyPart(mmp.getCount()-1).getContent();
//			}
//			else
//				messageS = message.getContent().toString();
//			
//			//Parse HTML mail
//			Document doc = Jsoup.parse(messageS);
//			
//			doc.select("img[src=http://www.faz.net/img/newsletter/d.gif]").remove();
//			for(Element element : doc.select("td")){
//				if(element.children().size() == 0 && element.text().equals(""))
//					if(element.parents().get(0).children().size() == 1)
//						element.parents().get(0).remove();
//					else
//						element.remove();
//			}
//			String heading = "";
//			for(Element element : doc.select("html > body > table > tbody > tr > td > table > tbody > tr"))
//				if(element.hasAttr("anchor"))
//					heading = element.text();
//				else if(element.attr("colspan").equals("7")){
//					
//					
//				}
//				else
//					System.out.println(element.html());
//			
//			System.out.println(doc.toString());
//			
//			File file = new File("/home/christoph/public_html/clipboard.htm");
//			FileUtils.writeStringToFile(file, doc.toString());
			
//			Message messages[] = inbox.getMessage(inbox.getMessageCount());
//			
//			for(Message message:messages) {
//				
//				if(message.getContent() instanceof MimeMultipart){
//					MimeMultipart mmp = (MimeMultipart) message.getContent();
//					System.out.println(mmp.getBodyPart(mmp.getCount()-1).getContent());
//				}
//				else
//					System.out.println(message.getContent().toString());
//				
//			}
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (MessagingException e) {
			e.printStackTrace();
			System.exit(2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
