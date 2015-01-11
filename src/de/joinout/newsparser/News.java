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

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONObject;

import de.joinout.criztovyl.tools.json.JSONMap;
import de.joinout.newsparser.json.JSONCreators;
/**
 * A class that holds advices. They are ordered by an superior heading.
 * @author Christoph "criztovyl" Schulz
 *
 */
public class News extends HashMap<String, Set<Advice>>{
	
	private String lastHeading;

	/**
	 * 
	 */
	private static final long serialVersionUID = -3636241449202883755L;
	
	/**
	 * Sets up the news.
	 */
	public News(){
		super();
	}
	/**
	 * Sets up the news from JSON.
	 * @param json the JSON data.
	 */
	public News(JSONObject json){
		super();
		putAll(new JSONMap<>(json, JSONCreators.STRING, JSONCreators.ADVICE_SET).getMap());
	}
	
	/**
	 * Set the heading for advices added by {@link #addAdvice(Advice)}.
	 * @param heading
	 */
	public void setHeading(String heading){
		
		if(!containsKey(heading))
			put(heading, new HashSet<Advice>());

		lastHeading = heading;
		
	}
	/**
	 * Adds a {@link Collection} of {@link Advice}s under the given heading.
	 * @param heading the heading
	 * @param advices the {@link Advice}s collection
	 */
	public void add(String heading, Collection<Advice> advices){
		addWI(heading, advices);
	}
	
	/**
	 * Adds a single {@link Advice} under the given heading.
	 * @param heading the heading
	 * @param advice the advice
	 */
	public void add(String heading, Advice advice){
		addWI(heading, advice);
	}
	
	/**
	 * Adds a single advice under the heading set by {@link #setHeading(String)}.<br>
	 * Pass-through to {@link #add(String, Advice)} with {@link #getLastHeading()} as heading.
	 * @param advice the advice
	 */
	public void addAdvice(Advice advice){
		add(getLastHeading(), advice);
	}
	/**
	 * Adds a value to the set from a key. Initiates the set if needed.
	 * @param key the key
	 * @param advice the value
	 * @param init the initiation set
	 */
	private void addWI(String key, Advice advice, HashSet<Advice> init){
		if(!containsKey(key))
			put(key, init);
		get(key).add(advice);
	}
	/**
	 * Adds values to the set from a key. Initiates the set if needed.
	 * @param key the key
	 * @param advices the values
	 * @param init the initiation set
	 */
	private void addWI(String key, Collection<Advice> advices, HashSet<Advice> init){
		if(!containsKey(key))
			put(key, init);
		get(key).addAll(advices);
	}
	/**
	 * Adds value to the set from a key. Initiates with an empty set if needed.
	 * @param key the key
	 * @param advices the value
	 */
	private void addWI(String key, Collection<Advice> advices){
		addWI(key, advices, new HashSet<Advice>());
	}
	/**
	 * Adds a value to the set from a key. Initiates with an empty set if needed. 
	 * @param key the key
	 * @param advice the value
	 */
	private void addWI(String key, Advice advice){
		addWI(key, advice, new HashSet<Advice>());
	}
	
	/**
	 * The heading set by {@link #setHeading(String)} and used as heading of {@link #addAdvice(Advice)}.
	 * @return a String
	 */
	public String getLastHeading(){
		return lastHeading;
	}
	
	/**
	 * The JSON data of this.
	 * @return a {@link JSONObject}.
	 */
	public JSONObject getJSON(){		
		return new JSONMap<>(this, JSONCreators.STRING, JSONCreators.ADVICE_SET).getJSON();
	}
}
