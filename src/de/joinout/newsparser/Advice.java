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

public class Advice {

	private String keyword, heading, zhort, readMore, image, src;
	
	public Advice(String keyword, String heading, String zhort, String readMore, String image){
		this.heading = heading;
		this.zhort = zhort;
		this.readMore = readMore;
		this.image = image;
		this.keyword = keyword;
	}
	public Advice(String keyword, String heading, String zhort, String readMore, String image, String src){
		this(keyword, heading, zhort, readMore, image);
		this.src = src;
	}

	public String getHeading() {
		return heading;
	}

	public String getZhort() {
		return zhort;
	}

	public String getReadMore() {
		return readMore;
	}

	/**
	 * @return the image
	 */
	public String getImage() {
		return image;
	}

	/**
	 * @return the keyword
	 */
	public String getKeyword() {
		return keyword;
	}
	
	public String getSource(){
		return src;
	}
}
