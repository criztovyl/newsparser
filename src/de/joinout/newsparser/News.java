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

import java.util.ArrayList;
import java.util.HashMap;

public class News extends HashMap<String, ArrayList<Advice>>{
	
	private String lastHeading;

	/**
	 * 
	 */
	private static final long serialVersionUID = -3636241449202883755L;
	
	public News(){
		super();
	}
	
	public void setHeading(String heading){
		
		if(!containsKey(heading))
			put(heading, new ArrayList<Advice>());

		lastHeading = heading;
		
	}
	public void add(String heading, ArrayList<Advice> advices){
		
		if(!containsKey(heading))
			put(heading, advices);
		else
			get(heading).addAll(advices);
		
	}
	
	public void add(String heading, Advice advice){
		
		if(!containsKey(heading))
			put(heading, new ArrayList<Advice>());
		
		get(heading).add(advice);
	}
	public void addAdvice(Advice advice){
		if(!containsKey(lastHeading))
			put(lastHeading, new ArrayList<Advice>());
		get(lastHeading).add(advice);
	}
	
	public String getLastHeading(){
		return lastHeading;
	}
}
