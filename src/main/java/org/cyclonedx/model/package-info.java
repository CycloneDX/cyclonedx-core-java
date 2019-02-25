@XmlJavaTypeAdapters({
        @XmlJavaTypeAdapter(type= LocalDateTime.class,
                value= DateAdapter.class),
})
package org.cyclonedx.model;

import org.cyclonedx.util.DateAdapter;

import java.time.LocalDateTime;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters;