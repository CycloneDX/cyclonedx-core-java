/*
 * Copyright (c) 2018,2019, 2020, 2021 Lockheed Martin Corporation.
 *
 * This work is owned by Lockheed Martin Corporation. Lockheed Martin personnel are permitted to use and
 * modify this software.  Lockheed Martin personnel may also deliver this source code to any US Government
 * customer Agency under a "US Government Purpose Rights" license.
 *
 * See the LICENSE file distributed with this work for licensing and distribution terms
 */
package org.cyclonedx.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.cyclonedx.model.Property;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser;

/**
 * (U) This class is needed to deserialize older custom Boms from LM.
 * 
 * @author wrgoff
 * @since 28 May 2021
 */
public class PropertyDeserializer extends StdDeserializer<List<Property>> {
	private static final long serialVersionUID = 3080840606644733407L;
	
	/**
	 * Default Constructor.
	 */
	public PropertyDeserializer() {
		this(null);
	}
	
	/**
	 * Constructor.
	 * 
	 * @param vc Class for which the deserializer is for.
	 */
	public PropertyDeserializer(final Class<?> vc) {
		super(vc);
	}
	
	/**
	 * Method use to deserialize the JSon.
	 * 
	 * @param parser  JsonParser that is being used.
	 * @param context Context for the process of deserialization a single root-level value.
	 * @throws IOException in the event we are unable to deserialize the Properties.
	 */
	@Override
	public List<Property> deserialize(final JsonParser parser,
			final DeserializationContext context) throws IOException {
		List<Property> properties = new ArrayList<>();
		
		if (parser instanceof FromXmlParser) {
			JsonNode node = parser.getCodec().readTree(parser);
			if (node.has("properties")) {
				JsonNode propertiesNode = node.get("properties");
				if(propertiesNode.isArray()) {
					ArrayNode arrayNode = (ArrayNode) node;
					for (int i = 0; i < arrayNode.size(); i++) {
						node = node.get("property");
						properties.addAll(parsePropertyNode(node, new ArrayList<Property>()));
					}
				}
			}
			else if (node.has("property")) {
				node = node.get("property");
				properties.addAll(parsePropertyNode(node, new ArrayList<Property>()));
			}
		} else {
			Property[] props = parser.readValueAs(Property[].class);
			properties = Arrays.asList(props.clone());
		}
		return properties;
	}
	
	/**
	 * (U) This method is used to parse the JsonNode that contains properties.
	 * 
	 * @param node JsonNode to process the property form.
	 * @return Property processed from the JsonNode passed in.
	 */
	private Property parseProperty(JsonNode node) {
		String name = null;
		String value = null;
		
		JsonNode nameNode = node.get("name");
		if (nameNode != null) {
			name = nameNode.asText();
		}
		JsonNode valueNode = node.get("");
		if (valueNode != null) {
			value = valueNode.asText();
		} else if (isPreleaseDeserializationEnabled()) {
			valueNode = node.get("value");
			if (valueNode != null) {
				value = valueNode.asText();
			}
		}
		Property prop = new Property();
		prop.setName(name);
		prop.setValue(value);
		return prop;
	}
	
	/**
	 * (U) This method is use to recursively process the ArrayNodes into Properties.
	 * 
	 * @param arrayNode ArrayNode to process.
	 * @param props     List of properties to append to.
	 * @return List of properties processed.
	 */
	private List<Property> parseArrayNode(ArrayNode arrayNode, List<Property> props) {
		JsonNode node;
		for (int i = 0; i < arrayNode.size(); i++) {
			node = arrayNode.get(i);
			if (node instanceof ArrayNode) {
				parseArrayNode(((ArrayNode) node), props);
			} else {
				props.add(parseProperty(node));
			}
		}
		return props;
	}
	
	/**
	 * (U) This method is used to parse the actual Property node.
	 * 
	 * @param node       JsonNode that is the property node.
	 * @param properties List of properties to append to.
	 * @return List of properties.
	 */
	private List<Property> parsePropertyNode(JsonNode node, List<Property> properties) {
		if (node.isArray()) {
			ArrayNode arrayNode = (ArrayNode) node;
			properties.addAll(parseArrayNode(arrayNode, new ArrayList<Property>()));
		} else {
			properties.add(parseProperty(node));
		}
		return properties;
	}

	private boolean isPreleaseDeserializationEnabled() {
		final String s = System.getProperty("cyclonedx.prerelease.14.properties");
		return Boolean.parseBoolean(s);
	}
}
