/*
 * Copyright (c) 2013 Codearte
 */

package com.devskiller.jfairy.data;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.snakeyaml.engine.v2.api.Load;
import org.snakeyaml.engine.v2.api.LoadSettings;

import com.devskiller.jfairy.producer.BaseProducer;
import com.devskiller.jfairy.producer.util.LanguageCode;
import com.devskiller.jfairy.producer.util.ValidateUtils;


public class MapBasedDataMaster implements DataMaster {

	public static final String LANGUAGE_TAG = "language";
	private final BaseProducer baseProducer;
	private Map<String, Object> dataSource = new CaseInsensitiveMap();


	public MapBasedDataMaster(BaseProducer baseProducer) {
		this.baseProducer = baseProducer;
	}

	/**
	 * Returns list (null safe) of elements for desired key from dataSource files
	 *
	 * @param key desired node key
	 * @return list of elements for desired key
	 * @throws IllegalArgumentException if no element for key has been found
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getStringList(String key) {
		return getData(key, List.class);
	}

	@SuppressWarnings("unchecked")
	public <T> T getValuesOfType(String dataKey, final String type, final Class<T> resultClass) {
		Map<String, List<T>> data = getData(dataKey, Map.class);

		List<T> entries = data.get(type);

		return baseProducer.randomElement(entries);
	}

	/**
	 * Returns element (null safe) for desired key from dataSource files
	 *
	 * @param key desired node key
	 * @return string element for desired key
	 * @throws IllegalArgumentException if no element for key has been found
	 */
	@Override
	public String getString(String key) {
		return getData(key, String.class);
	}

	@Override
	public String getRandomValue(String key) {
		return baseProducer.randomElement(getStringList(key));
	}

	@Override
	public LanguageCode getLanguage() {
		String tag = getString(LANGUAGE_TAG).toUpperCase();
		try {
			return LanguageCode.valueOf(tag);
		} catch (IllegalArgumentException ex) {
			throw new IllegalArgumentException("Unknown language tag: " + tag, ex);
		}
	}

	@SuppressWarnings({"unchecked", "ConstantConditions"})
	<T> T getData(String key, Class<T> type) {
		ValidateUtils.notNull(key, "key cannot be null");
		ValidateUtils.notNull(type, "type cannot be null");

		Object element = dataSource.get(key);

		ValidateUtils.isTrue(element != null, "No such key: %s", key);
		ValidateUtils.isTrue(type.isAssignableFrom(element.getClass()),
			"Element under desired key has incorrect type - should be %s", type.getSimpleName());

		return (T) element;
	}

	//fixme - should be package-private
	public void readResources(String path) throws IOException {
		Enumeration<URL> resources =
				getClass().getClassLoader().getResources(path);

		if (!resources.hasMoreElements()) {
			throw new IllegalArgumentException(String.format("File %s was not found on classpath", path));
		}

		final LoadSettings loadSettings = LoadSettings.builder().build();

		while (resources.hasMoreElements()) {
			final Load load = new Load(loadSettings);
			final URL url = resources.nextElement();
			try (InputStream is = url.openStream()) {
				final Map<String, Object> data = (Map<String, Object>) load.loadFromInputStream(is);
				appendData(data);
			}
		}
	}

	private void appendData(Map<String, Object> data) {
		dataSource.putAll(data);
	}

	private static class CaseInsensitiveMap extends HashMap<String, Object> {

		@Override
		@SuppressWarnings("unchecked")
		public Object put(String key, Object value) {
			String loweredKey = key.toLowerCase();
			Object valueToInsert = value;

			if (value instanceof Map) {
				valueToInsert = new CaseInsensitiveMap();
				((CaseInsensitiveMap) valueToInsert).putAll((Map<? extends String, ?>) value);
			}

			return super.put(loweredKey, valueToInsert);
		}

		@Override
		public void putAll(Map<? extends String, ?> map) {
			for (Map.Entry<? extends String, ?> entry : map.entrySet()) {
				put(entry.getKey(), entry.getValue());
			}
		}

		@Override
		@SuppressWarnings("unchecked")
		public Object get(Object key) {
			return super.get(((String) key).toLowerCase());
		}
	}
}
