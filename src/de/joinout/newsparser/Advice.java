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

import java.util.Calendar;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.json.JSONObject;

import de.joinout.criztovyl.tools.json.JSONCalendar;
import de.joinout.criztovyl.tools.json.creator.JSONCreator;
import de.joinout.newsparser.json.JSONCreators;

/**
 * A class that hold's an advice' keyword, heading, short version, read-more text, image and source.<br>
 * A {@link JSONCreator} is available in {@link JSONCreators}.
 * @author Christoph "criztovyl" Schulz
 *
 */
public class Advice{

	private String keyword, heading, teaserText, readMore, imageURL, sourceName;
	private Calendar date;
	
	private static String KEYWORD = "keyword";
	private static String HEADING = "heading";
	private static String TEASERTEXT = "teaser";
	private static String READMORE = "readmore";
	private static String IMAGEURL = "imageurl";
	private static String SOURCENAME = "source";
	private static String DATE = "date";
	
	/**
	 * Creates a new advice.
	 * @param keyword the keyword
	 * @param heading the heading
	 * @param teaserText the short/intro/teaser text
	 * @param readMore the read-more text
	 * @param imageURL the image source URL
	 */
	public Advice(String keyword, String heading, String teaserText, String readMore, String imageURL){
		this.heading = heading;
		this.teaserText = teaserText;
		this.readMore = readMore;
		this.imageURL = imageURL;
		this.keyword = keyword;
		this.date = daysOnly(Calendar.getInstance());
	}
	/**
	 * Creates a new advice.
	 * @param keyword the keyword
	 * @param heading the heading
	 * @param teaserText the short/intro/teaser text
	 * @param readMore the read-more text
	 * @param imageURL the image source URL
	 * @param srcName the advice' source name
	 */
	public Advice(String keyword, String heading, String teaserText, String readMore, String imageURL, String srcName){
		this(keyword, heading, teaserText, readMore, imageURL);
		this.sourceName = srcName;
	}
	/**
	 * Creates a new advice.
	 * @param keyword the keyword
	 * @param heading the heading
	 * @param teaserText the short/intro/teaser text
	 * @param readMore the read-more text
	 * @param imageURL the image source URL
	 * @param srcName the advice' source name
	 * @param date the advice' date (everything below days not used.)
	 */
	public Advice(String keyword, String heading, String teaserText, String readMore, String imageURL, String srcName, Calendar date){
		this(keyword, heading, teaserText, readMore, imageURL, srcName);
		
		this.date = daysOnly(date);
	}
	/**
	 * Creates a new advice from JSON data
	 * @param json JSON data
	 */
	public Advice(JSONObject json){
			this(
					json.getString(KEYWORD), json.getString(HEADING), json.getString(TEASERTEXT), 
					json.getString(READMORE), json.getString(IMAGEURL), json.getString(SOURCENAME), 
					json.has(DATE) ? new JSONCalendar(json.getJSONObject(DATE)).getCalendar() : null);
	}

	/**
	 * The advice' heading.
	 * @return a String.
	 */
	public String getHeading() {
		return heading;
	}

	/**
	 * The advice' short text (intro/teaser text).
	 * @return a String.
	 * @deprecated Please use {@link #getTeaserText()}. This method will be removed soon because the name isn't good.
	 */
	@Deprecated
	public String getZhort() {
		return getTeaserText();
	}
	
	/**
	 * The advice' teaser text.
	 * @return a String.
	 */
	public String getTeaserText(){
		return teaserText;
	}

	/**
	 * The advice' read-more text.
	 * @return a String
	 */
	public String getReadMore() {
		return readMore;
	}

	/**
	 * The advice' image source URL.
	 * @return a String
	 * @deprecated Please use {@link #getImageURL()}. This method will be removed soon and replaced by a method returning an Object, that describes the image.
	 */
	@Deprecated
	public String getImage() {
		return getImageURL();
	}
	
	/**
	 * The advice' image source URL.
	 * @return a String
	 */	
	public String getImageURL(){
		return imageURL;
	}

	/**
	 * The advice' keyword.
	 * @return a String
	 */
	public String getKeyword() {
		return keyword;
	}
	
	/**
	 * The advice' source' name
	 * @return a String
	 * @deprecated Please use {@link #getSourceName()}. This method will be removed soon and replaced by an method returning an Object, that describes the source.
	 */
	@Deprecated
	public String getSource(){
		return getSourceName();
	}
	/**
	 * The advice' source' name
	 * @return a String
	 */
	public String getSourceName(){
		return sourceName;
	}
	/**
	 * The advice' date (year, month and day only)
	 * @return a {@link Calendar}
	 */
	public Calendar getDate(){
		return date;
	}
	
	/**
	 * The JSON data of this.
	 * @return a {@link JSONObject}
	 */
	public JSONObject getJSON(){
		JSONObject json = new JSONObject();
		
		json.put(KEYWORD, getKeyword());
		json.put(HEADING, getHeading());
		json.put(TEASERTEXT, getTeaserText());
		json.put(READMORE, getReadMore());
		json.put(IMAGEURL, getImageURL());
		json.put(SOURCENAME, getSourceName() == null ? "" : getSourceName());
		if(getDate() != null)
			json.put(DATE, new JSONCalendar(getDate()).getJSON());
		
		return json;
	}
	
	/**
	 * Unsets everything that's not day, month or year. 
	 * @param cal
	 * @return
	 */
	private Calendar daysOnly(Calendar cal){
		
		Calendar date = Calendar.getInstance();
		date.clear();
		date.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
		
		return date;
	}
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object aObject){
		if(aObject instanceof Advice){
			Advice aA = (Advice) aObject;
			return new EqualsBuilder()
			.append(date, aA.date)
			.append(heading, aA.heading)
			.append(imageURL, aA.imageURL)
			.append(keyword, aA.keyword)
			.append(readMore, aA.readMore)
			.append(sourceName, aA.sourceName)
			.append(teaserText, aA.teaserText)
			.isEquals();
		}
		else
			return super.equals(aObject);
	}
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
		.append(date)
		.append(heading)
		.append(imageURL)
		.append(keyword)
		.append(readMore)
		.append(sourceName)
		.append(teaserText)
		.toHashCode();
	}
	
}
