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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMultipart;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public abstract class AbstractNewsletter extends News implements Newsletter{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8542675765449417769L;
	protected Document doc;
	private Logger logger;
	
	public AbstractNewsletter(){
		super();
		
		logger = LogManager.getLogger();
	}
	
	public AbstractNewsletter(String html){
		this();
		doc = Jsoup.parse(html);
	}
	
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

	public HashMap<String, ArrayList<Advice>> getNews() {
		return this;
	}
	public Document getDocument(){
		return doc;
	}

}
