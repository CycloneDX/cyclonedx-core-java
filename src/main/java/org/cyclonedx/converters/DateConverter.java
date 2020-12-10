/*
 * This file is part of CycloneDX Core (Java).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (c) Steve Springett. All Rights Reserved.
 */
package org.cyclonedx.converters;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class DateConverter implements Converter
{
  @Override
  public void marshal(
      final Object o,
      final HierarchicalStreamWriter writer,
      final MarshallingContext marshallingContext)
  {
    if (o instanceof Date) {
      TimeZone tz = TimeZone.getTimeZone("UTC");
      DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
      df.setTimeZone(tz);
      writer.setValue(df.format(o));
    }
  }

  @Override
  public Object unmarshal(
      final HierarchicalStreamReader reader, final UnmarshallingContext unmarshallingContext)
  {
    TimeZone tz = TimeZone.getTimeZone("UTC");
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    df.setTimeZone(tz);
    try {
      return df.parse(reader.getValue());
    }
    catch (ParseException e) {
      return null;
    }
  }

  @Override
  public boolean canConvert(final Class clazz) {
    return Date.class.isAssignableFrom(clazz);
  }
}
