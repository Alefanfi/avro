package org.apache.avro.data;

import org.apache.avro.LogicalTypes;
import org.apache.avro.Schema;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.time.*;
import java.util.Arrays;
import java.util.Collection;

@RunWith(value = Parameterized.class)
public class TimeConversionsTest {

  private final Object expected;

  private final String typeTest;
  private final Object input;

  private static final String dateConversion_toInt = "DATE CONVERSION_toInt";
  private static final String dateConversion_fromInt = "DATE CONVERSION_fromInt";

  private static final String timeMillisConversion_toInt = "TimeMillisConversion_toInt";
  private static final String timeMillisConversion_fromInt = "TimeMillisConversion_fromInt";

  private static final String timeMicrosConversion_fromLong = "TimeMicrosConversion_fromLong";
  private static final String timeMicrosConversion_toLong = "TimeMicrosConversion_toLong";

  private static final String timestampMillisConversion_fromLong = "TimestampMillisConversion_fromLong";
  private static final String timestampMillisConversion_toLong = "TimestampMillisConversion_toLong";

  public TimeConversionsTest(String typeTest, Object input, Object expected){

    this.typeTest = typeTest;
    this.input = input;
    this.expected = expected;

  }

  @Parameterized.Parameters
  public static Collection getParameters() {

    return Arrays.asList(new Object[][]{

      {dateConversion_toInt, "2019-11-15", 18215},
      {dateConversion_toInt, "18-02-1920", "String form not valid"},
      {dateConversion_toInt, "", "String form not valid"},

      {dateConversion_fromInt, 18215, LocalDate.of(2019,11,15)},
      {dateConversion_fromInt, 0, LocalDate.of(1970,1,1)},
      {dateConversion_fromInt, -18215, LocalDate.of(1920,2,18)},

      {timeMillisConversion_toInt, "19:34:50.63", 70490630},
      {timeMillisConversion_toInt, "00:00", 0},
      {timeMillisConversion_toInt, "", "String form not valid"},

      {timeMillisConversion_fromInt, 70490630, LocalTime.parse("19:34:50.63")},
      {timeMillisConversion_fromInt, 0, LocalTime.parse("00:00")},
      {timeMillisConversion_fromInt, -70490630, "Can't convert negative value"},

      {timeMicrosConversion_fromLong, ((long) (15 * 60 + 14) * 60 + 15) * 1_000_000 + 926_551, LocalTime.of(15,14,15,926_551_000)},
      {timeMicrosConversion_fromLong, 0, LocalTime.of(0,0,0,0)},
      {timeMicrosConversion_fromLong, -((long) (15 * 60 + 14) * 60 + 15) * 1_000_000 + 926_551, "Input value not valid"},

      {timeMicrosConversion_toLong, "00:00", (long) 0},
      {timeMicrosConversion_toLong, "19:34:00.00", 70440000000L},
      {timeMicrosConversion_toLong, "", "Input not valid"},

      {timestampMillisConversion_fromLong, 1432849613221L, ZonedDateTime.of(1970, 1, 17, 14, 0, 49, 613_221_000, ZoneOffset.UTC).toInstant()},
      {timestampMillisConversion_fromLong, -1432849613221L, "Input not valid"},
      {timestampMillisConversion_fromLong, 0, ZonedDateTime.of(1970, 1, 1, 0, 0, 0,0, ZoneOffset.UTC).toInstant()},

      {timestampMillisConversion_toLong, "2018-05-12T23:30:00Z", 1526167800000000L},
      //{timestampMillisConversion_toLong, "0", (long)0},
      //{timestampMillisConversion_toLong, -((long) (15 * 60 + 14) * 60 + 15) * 1_000_000 + 926_551, "Input value not valid"},


    });
  }

  @Test
  public void test(){

    TimeConversions.DateConversion dataConversions = new TimeConversions.DateConversion();

    TimeConversions.TimeMillisConversion timeMillisConversion = new TimeConversions.TimeMillisConversion();

    TimeConversions.TimeMicrosConversion timeMicrosConversion = new TimeConversions.TimeMicrosConversion();

    TimeConversions.TimestampMicrosConversion timestampMicrosConversion = new TimeConversions.TimestampMicrosConversion();

    String s;
    Integer numberOfDay;
    long convertedLong;
    LocalTime localTime;

    Object result = null;

    switch (typeTest) {

      case dateConversion_toInt:

        s = (String) input;

        if (s.matches("^\\d{4}-\\d{2}-\\d{2}$")) {

          LocalDate localDate = LocalDate.parse(s);

          result = dataConversions.toInt(localDate, LogicalTypes.date().addToSchema(Schema.create(Schema.Type.INT)), LogicalTypes.date());

        }else{

          result = "String form not valid";
        }
        break;

      case dateConversion_fromInt:

        numberOfDay = (Integer) input;

        result = dataConversions.fromInt(numberOfDay, LogicalTypes.date().addToSchema(Schema.create(Schema.Type.INT)), LogicalTypes.date());

        break;

      case timeMillisConversion_toInt:

        s = (String) input;

        if(s.matches("^\\d{2}:\\d{2}:\\d{2}.\\d{2}$") || s.matches("^\\d{2}:\\d{2}$")) {

          localTime = LocalTime.parse(s);

          result = timeMillisConversion.toInt(localTime, LogicalTypes.timeMillis().addToSchema(Schema.create(Schema.Type.INT)), LogicalTypes.timeMillis());

        }else{

          result = "String form not valid";

        }

        break;

      case timeMillisConversion_fromInt:

        numberOfDay = (Integer) input;

        if (numberOfDay < 0) {

          result = "Can't convert negative value";

        } else{

          result = timeMillisConversion.fromInt(numberOfDay, LogicalTypes.timeMillis().addToSchema(Schema.create(Schema.Type.INT)), LogicalTypes.timeMillis());

        }

        break;

      case timeMicrosConversion_fromLong:

        s = String.valueOf(input);
        convertedLong = Long.parseLong(s);

        if(convertedLong < 0){

          result = "Input value not valid";

        }else {

          result = timeMicrosConversion.fromLong(convertedLong, LogicalTypes.timeMicros().addToSchema(Schema.create(Schema.Type.LONG)), LogicalTypes.timeMicros());

        }

        break;

      case timeMicrosConversion_toLong:

        s = (String) input;

        if(s.matches("^\\d{2}:\\d{2}:\\d{2}.\\d{2}$") || s.matches("^\\d{2}:\\d{2}$")) {

          localTime = LocalTime.parse(s);

          result = timeMicrosConversion.toLong(localTime, LogicalTypes.timeMicros().addToSchema(Schema.create(Schema.Type.LONG)), LogicalTypes.timeMicros());

        }else{

          result = "Input not valid";

        }

        break;

      case timestampMillisConversion_fromLong:

        s = String.valueOf(input);
        convertedLong = Long.parseLong(s);

        if(convertedLong >= 0) {

          result = timestampMicrosConversion.fromLong(convertedLong, LogicalTypes.timestampMillis().addToSchema(Schema.create(Schema.Type.LONG)), LogicalTypes.timestampMillis());

        }else {

          result = "Input not valid";
        }
        break;

      case timestampMillisConversion_toLong:

        s = (String)input;
        Instant instant = Instant.parse(s);

        result = timestampMicrosConversion.toLong(instant, LogicalTypes.timestampMillis().addToSchema(Schema.create(Schema.Type.LONG)), LogicalTypes.timestampMillis());

        break;

      default:
        throw new IllegalStateException("Unexpected value: " + typeTest);
    }

    Assert.assertEquals(result, expected);

  }

}
