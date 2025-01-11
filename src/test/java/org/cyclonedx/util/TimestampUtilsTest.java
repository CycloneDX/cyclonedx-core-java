package org.cyclonedx.util;

import java.util.Date;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TimestampUtilsTest
{
  @Test
  public void testParseTimestampValid() {
    String validTimestamp = "2023-10-01T12:34:56.789+00:00";
    Date date = TimestampUtils.parseTimestamp(validTimestamp);
    assertNotNull(date);
  }

  @Test
  public void testParseTimestampWithZ() {
    String validTimestampWithZ = "2021-01-01T00:00:00.000Z";
    Date date = TimestampUtils.parseTimestamp(validTimestampWithZ);
    assertNotNull(date);
  }

  @Test
  public void testParseTimestampValidWithoutMilliseconds() {
    String validTimestamp = "2023-10-01T12:34:56+00:00";
    Date date = TimestampUtils.parseTimestamp(validTimestamp);
    assertNotNull(date);
  }

  @Test
  public void testParseTimestampInvalid() {
    String invalidTimestamp = "invalid-timestamp";
    Date date = TimestampUtils.parseTimestamp(invalidTimestamp);
    assertNull(date);
  }

  @Test
  public void testParseTimestampNull() {
    Date date = TimestampUtils.parseTimestamp(null);
    assertNull(date);
  }

  @Test
  public void testParseTimestampEmpty() {
    String emptyTimestamp = "";
    Date date = TimestampUtils.parseTimestamp(emptyTimestamp);
    assertNull(date);
  }
}
