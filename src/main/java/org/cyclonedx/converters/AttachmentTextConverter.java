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

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.cyclonedx.model.AttachmentText;

public class AttachmentTextConverter implements Converter {

    @Override
    public void marshal(Object object, HierarchicalStreamWriter writer, MarshallingContext context) {
        final AttachmentText attachmentText = (AttachmentText)object;
        writer.addAttribute("content-type", attachmentText.getContentType());
        writer.addAttribute("encoding", attachmentText.getEncoding());
        writer.setValue(attachmentText.getText());
    }

    @Override
    public boolean canConvert(Class clazz) {
        return clazz.equals(AttachmentText.class);
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        final String contentType = reader.getAttribute("content-type");
        final String encoding = reader.getAttribute("encoding");
        final String text = (reader.getValue());
        final AttachmentText attachmentText = new AttachmentText();
        attachmentText.setContentType(contentType);
        attachmentText.setEncoding(encoding);
        attachmentText.setText(text);
        return attachmentText;
    }
}
