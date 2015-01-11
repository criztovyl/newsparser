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
package de.joinout.newsparser.json;

import java.util.List;
import java.util.Set;

import org.json.JSONObject;

import de.joinout.criztovyl.tools.json.JSONList;
import de.joinout.criztovyl.tools.json.JSONSet;
import de.joinout.criztovyl.tools.json.creator.AbstractJSONCreator;
import de.joinout.criztovyl.tools.json.creator.JSONCreator;
import de.joinout.newsparser.Advice;
/**
 * A class that contains {@link JSONCreator}s for some classes/objects.
 * @author Christoph "criztovyl" Schulz
 *
 */
public class JSONCreators extends de.joinout.criztovyl.tools.json.creator.JSONCreators{

	/**
	 * The {@link JSONCreator} for an {@link Advice}.
	 */
	public static JSONCreator<Advice> ADVICE = new AbstractJSONCreator<Advice>() {

		@Override
		public JSONObject getJSON(Advice t) {
			return t.getJSON();
		}

		@Override
		public Advice fromJSON(JSONObject json) {
			return new Advice(json);
		}

		@Override
		public Class<?> getCreatorClass() {
			return Advice.class;
		}
	};

	/**
	 * The {@link JSONCreator} for an {@link Advice} {@link List}.
	 */
	public static JSONCreator<List<Advice>> ADVICE_LIST = new AbstractJSONCreator<List<Advice>>() {

		@Override
		public JSONObject getJSON(List<Advice> t) {
			return new JSONList<>(t, ADVICE).getJSON();
		}

		@Override
		public List<Advice> fromJSON(JSONObject json) {
			return new JSONList<>(json, ADVICE).getList();
		}

		@Override
		public Class<?> getCreatorClass() {
			return List.class;
		}
	};
	
	/**
	 * The {@link JSONCreator} for an {@link Advice} {@link Set}.
	 */
	public static JSONCreator<Set<Advice>> ADVICE_SET = new AbstractJSONCreator<Set<Advice>>() {

		@Override
		public JSONObject getJSON(Set<Advice> t) {
			return new JSONSet<>(t, JSONCreators.ADVICE).getJSON();
		}

		@Override
		public Set<Advice> fromJSON(JSONObject json) {
			return new JSONSet<>(json, JSONCreators.ADVICE).getSet();
		}

		@Override
		public Class<?> getCreatorClass() {
			return Set.class;
		}
	};
}
