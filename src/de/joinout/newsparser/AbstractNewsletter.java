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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Set;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * An abstract class useful for parsing newsletters.
 * @author Christoph "criztovyl" Schulz
 *
 */
public abstract class AbstractNewsletter extends News implements Newsletter{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8542675765449417769L;
	
	/**
	 * @deprecated Please use {@link #getDocument()}. This will be made private soon.
	 */
	@Deprecated
	protected Document doc;
	private Logger logger;
	private boolean written;
	
	/**
	 * Constructs the class.
	 */
	public AbstractNewsletter(){
		super();
		
		logger = LogManager.getLogger();
		written =  false;
	}
	
	/**
	 * Constructs the class and initiates the document with the given HTML String.
	 * @param html the HTML String.
	 */
	public AbstractNewsletter(String html){
		this();
		doc = Jsoup.parse(html);
	}
	
	/**
	 * Constructs the class and initiates the document from the given {@link Message}.
	 * @param message the message.
	 */
	public AbstractNewsletter(Message message){
		this();

		//Try to extract HTML message, its the last part.
		try {
			if(message.getContent() instanceof MimeMultipart){
				MimeMultipart mmp = (MimeMultipart) message.getContent();
				doc = Jsoup.parse((String) mmp.getBodyPart(mmp.getCount()-1).getContent());
			}
			else
				logger.error("Unsupported mail format {}.", message.getContentType());
		} catch (IOException | MessagingException e) {
			logger.catching(e);
		}
	}

	/**
	 * Returns the {@link News} of this newsletter.
	 * @deprecated This class extends {@link News}, please cast it to {@link News} if you need the news only.
	 */
	@Deprecated
	public HashMap<String, Set<Advice>> getNews() {
		return this;
	}
	
	/**
	 * The HTML {@link Document} of the newsletter
	 * @return
	 */
	public Document getDocument(){
		return doc;
	}
	
	/**
	 * Util to log the newsletter document to a file.
	 * @param body the body string.
	 * @deprecated Please use {@link #writeMessageDocumentToFile()}.
	 */
	@Deprecated
	protected void logMessageBody(String body){
			writeMessageDocumentToFile();			
	}
	/**
	 * Util to log the newsletter document to a file in the configuration directory.
	 * TODO: Find object-orientated way.
	 */
	protected void writeMessageDocumentToFile(){
		
		if(!written){

			File file = Main.DIR.append("documents").append(this.getClass().getSimpleName()).append(Long.toString(Calendar.getInstance().getTimeInMillis())).getFile();
			logger.info("Writing message body to {} ...", file.getPath());

			try {
				FileUtils.writeStringToFile(file, getDocument().toString());
				written = true;
			} catch (IOException e1) {
				logger.error("Failed to write message body to file {}. Catching exception.", file.getPath());
				logger.catching(e1);
			}
		}
	}

}
