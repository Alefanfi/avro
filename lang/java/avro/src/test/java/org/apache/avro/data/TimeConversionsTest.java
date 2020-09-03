package org.apache.avro.data;

import org.apache.avro.LogicalTypes;
import org.apache.avro.Schema;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.time.*;
import java.time.format.DateTimeParseException;
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

  private static final String timestampMicrosConversion_fromLong = "TimestampMicrosConversion_fromLong";
  private static final String timestampMicrosConversion_toLong = "TimestampMicrosConversion_toLong";

  private static final String localTimestampMicrosConversion_fromLong = "LocalTimestampMicrosConversion_fromLong";
  private static final String localTimestampMicrosConversion_toLong = "LocalTimestampMicrosConversion_toLong";

  private static final String localTimestampMillisConversion_fromLong = "LocalTimestampMillisConversion_fromLong";
  private static final String localTimestampMillisConversion_toLong = "LocalTimestampMillisConversion_toLong";

  public TimeConversionsTest(String typeTest, Object input, Object expected){

    this.typeTest = typeTest;
    this.input = input;
    this.expected = expected;

  }

  @Parameterized.Parameters
  public static Collection getParameters() {

    return Arrays.asList(new Object[][]{

      {dateConversion_toInt, "2019-11-15", 18215},
      {dateConversion_toInt, "18-02-1920", DateTimeParseException.class},
      {dateConversion_toInt, "", DateTimeParseException.class},

      {dateConversion_fromInt, 18215, LocalDate.of(2019,11,15)},
      {dateConversion_fromInt, 0, LocalDate.of(1970,1,1)},
      {dateConversion_fromInt, -18215, LocalDate.of(1920,2,18)},
      {dateConversion_fromInt, "2dfv", ClassCastException.class},

      {timeMillisConversion_toInt, "19:34:50.63", 70490630},
      {timeMillisConversion_toInt, "00:00", 0},
      {timeMillisConversion_toInt, "cg:ds:fd.df", DateTimeParseException.class},

      {timeMillisConversion_fromInt, 70490630, LocalTime.parse("19:34:50.63")},
      {timeMillisConversion_fromInt, 0, LocalTime.parse("00:00")},
      {timeMillisConversion_fromInt, -70490630, DateTimeException.class},

      {timeMicrosConversion_fromLong, ((long) (15 * 60 + 14) * 60 + 15) * 1_000_000 + 926_551, LocalTime.of(15,14,15,926_551_000)},
      {timeMicrosConversion_fromLong, 0, LocalTime.of(0,0,0,0)},
      {timeMicrosConversion_fromLong, -((long) (15 * 60 + 14) * 60 + 15) * 1_000_000 + 926_551, DateTimeException.class},

      {timeMicrosConversion_toLong, "00:00", (long) 0},
      {timeMicrosConversion_toLong, "19:34:00.00", 70440000000L},
      {timeMicrosConversion_toLong, "", DateTimeParseException.class},

      {timestampMicrosConversion_fromLong, 1432849613221L, ZonedDateTime.of(1970, 1, 17, 14, 0, 49, 613_221_000, ZoneOffset.UTC).toInstant()},
      {timestampMicrosConversion_fromLong, -1432849613221L, ZonedDateTime.of(1969,12,15,9,59,10,386_779_000, ZoneOffset.UTC).toInstant()},
      {timestampMicrosConversion_fromLong, 0, ZonedDateTime.of(1970, 1, 1, 0, 0, 0,0, ZoneOffset.UTC).toInstant()},

      {timestampMicrosConversion_toLong, "2018-05-12T23:30:00Z", 1526167800000000L},
      {timestampMillisConversion_toLong, "00:00", DateTimeParseException.class},
      {timestampMicrosConversion_toLong, 223456, DateTimeParseException.class},

      {timestampMillisConversion_fromLong, 1432849613222L, ZonedDateTime.of(2015, 5, 28, 21, 46, 53, 222_000_000, ZoneOffset.UTC).toInstant()},
      {timestampMillisConversion_fromLong, -1432849613221L, ZonedDateTime.of(1924,8,6,2,13,6,779_000_000, ZoneOffset.UTC).toInstant()},
      {timestampMicrosConversion_fromLong, 0, ZonedDateTime.of(1970, 1, 1, 0, 0, 0,0, ZoneOffset.UTC).toInstant()},

      {timestampMillisConversion_toLong, "2018-05-12T23:30:00Z", 1526167800000L},
      {timestampMillisConversion_toLong, "", DateTimeParseException.class},

      {localTimestampMicrosConversion_fromLong, 1432849613222L, LocalDateTime.of(1970, 1, 17, 14, 0, 49, 613_222_000)},
      {localTimestampMicrosConversion_fromLong, -1432849613221L, LocalDateTime.of(1969,12,15,9,59, 10,386_779_000)},
      {localTimestampMicrosConversion_fromLong, 0, LocalDateTime.of(1970, 1, 1, 0, 0, 0,0)},
      {localTimestampMicrosConversion_fromLong, "dfs", NumberFormatException.class},

      {localTimestampMicrosConversion_toLong, "2018-10-23T17:19:33", 1540315173000000L},
      {localTimestampMicrosConversion_toLong, "asdf-00-ddffr0Tdfd", DateTimeParseException.class},

      {localTimestampMillisConversion_fromLong, 1432849613222L, LocalDateTime.of(2015,5,28,21,46,53,222_000_000)},
      {localTimestampMillisConversion_fromLong, -1432849613222L, LocalDateTime.of(1924,8,6,2,13,6,778_000_000)},
      {localTimestampMillisConversion_fromLong, 0, LocalDateTime.of(1970,1,1,0,0,0)},
      {localTimestampMillisConversion_fromLong, "dgfdd", NumberFormatException.class},

      {localTimestampMillisConversion_toLong, "2018-10-23T17:19:33", 1540315173000L},
      {localTimestampMillisConversion_toLong, "drfgdg", DateTimeParseException.class}

    });
  }

  @Test
  public void test(){

    TimeConversions.DateConversion dataConversions = new TimeConversions.DateConversion();

    TimeConversions.TimeMillisConversion timeMillisConversion = new TimeConversions.TimeMillisConversion();

    TimeConversions.TimeMicrosConversion timeMicrosConversion = new TimeConversions.TimeMicrosConversion();

    TimeConversions.TimestampMicrosConversion timestampMicrosConversion = new TimeConversions.TimestampMicrosConversion();

    TimeConversions.TimestampMillisConversion timestampMillisConversion = new TimeConversions.TimestampMillisConversion();

    TimeConversions.LocalTimestampMicrosConversion localTimestampMicrosConversion = new TimeConversions.LocalTimestampMicrosConversion();

    TimeConversions.LocalTimestampMillisConversion localTimestampMillisConversion = new TimeConversions.LocalTimestampMillisConversion();

    String s;
    Integer numberOfDay;
    long convertedLong;
    LocalTime localTime;

    Object result = null;

    switch (typeTest) {

      case dateConversion_toInt:

        try {

          s = String.valueOf(input);

          LocalDate localDate = LocalDate.parse(s);

          result = dataConversions.toInt(localDate, LogicalTypes.date().addToSchema(Schema.create(Schema.Type.INT)), LogicalTypes.date());
        } catch (Exception e) {

          result = e.getClass();

        }

        break;

      case dateConversion_fromInt:

        try {

          numberOfDay = (Integer) input;

          result = dataConversions.fromInt(numberOfDay, LogicalTypes.date().addToSchema(Schema.create(Schema.Type.INT)), LogicalTypes.date());

        } catch (Exception e) {

          result = e.getClass();
        }
        break;

      case timeMillisConversion_toInt:

        try {

          s = String.valueOf(input);

          localTime = LocalTime.parse(s);

          result = timeMillisConversion.toInt(localTime, LogicalTypes.timeMillis().addToSchema(Schema.create(Schema.Type.INT)), LogicalTypes.timeMillis());

        } catch (Exception e) {

          result = e.getClass();

        }

        break;

      case timeMillisConversion_fromInt:

        try{

          numberOfDay = (Integer) input;

          result = timeMillisConversion.fromInt(numberOfDay, LogicalTypes.timeMillis().addToSchema(Schema.create(Schema.Type.INT)), LogicalTypes.timeMillis());

        } catch (Exception e) {

          result = e.getClass();

        }

        break;

      case timeMicrosConversion_fromLong:

        try{

          s = String.valueOf(input);
          convertedLong = Long.parseLong(s);

          result = timeMicrosConversion.fromLong(convertedLong, LogicalTypes.timeMicros().addToSchema(Schema.create(Schema.Type.LONG)), LogicalTypes.timeMicros());

        } catch (Exception e) {

          result = e.getClass();
        }

        break;

      case timeMicrosConversion_toLong:

        try{

          s = String.valueOf(input);

          localTime = LocalTime.parse(s);

          result = timeMicrosConversion.toLong(localTime, LogicalTypes.timeMicros().addToSchema(Schema.create(Schema.Type.LONG)), LogicalTypes.timeMicros());

        } catch (Exception e) {

          result = e.getClass();
        }

        break;

      case timestampMicrosConversion_fromLong:

        try {

          s = String.valueOf(input);
          convertedLong = Long.parseLong(s);

          result = timestampMicrosConversion.fromLong(convertedLong, LogicalTypes.timestampMicros().addToSchema(Schema.create(Schema.Type.LONG)), LogicalTypes.timestampMicros());

        } catch (NumberFormatException e) {

          result = e.getClass();
        }
        break;

      case timestampMicrosConversion_toLong:

        try{

          s = String.valueOf(input);

          Instant instant = Instant.parse(s);

          result = timestampMicrosConversion.toLong(instant, LogicalTypes.timestampMicros().addToSchema(Schema.create(Schema.Type.LONG)), LogicalTypes.timestampMicros());

        } catch (Exception e) {

          result = e.getClass();

        }

        break;

      case timestampMillisConversion_fromLong:

        try {

          s = String.valueOf(input);
          convertedLong = Long.parseLong(s);

          result = timestampMillisConversion.fromLong(convertedLong, LogicalTypes.timestampMillis().addToSchema(Schema.create(Schema.Type.LONG)), LogicalTypes.timestampMillis());

        } catch (NumberFormatException e) {

          result = e.getClass();
        }
        break;

      case timestampMillisConversion_toLong:

        try{

          s = String.valueOf(input);

          Instant instant = Instant.parse(s);

          result = timestampMillisConversion.toLong(instant, LogicalTypes.timestampMillis().addToSchema(Schema.create(Schema.Type.LONG)), LogicalTypes.timestampMillis());

        } catch (Exception e) {

          result = e.getClass();
        }

        break;

      case localTimestampMicrosConversion_fromLong:

        try {

          s = String.valueOf(input);

          convertedLong = Long.parseLong(s);

          result = localTimestampMicrosConversion.fromLong(convertedLong, LogicalTypes.localTimestampMicros().addToSchema(Schema.create(Schema.Type.LONG)), LogicalTypes.localTimestampMicros());

        } catch (NumberFormatException e) {

          result = e.getClass();

        }
        break;

      case localTimestampMicrosConversion_toLong:

        try {

          s = String.valueOf(input);

          LocalDateTime localDateTime = LocalDateTime.parse(s);

          result = localTimestampMicrosConversion.toLong(localDateTime, LogicalTypes.localTimestampMicros().addToSchema(Schema.create(Schema.Type.LONG)), LogicalTypes.localTimestampMicros());

        } catch (Exception e) {

          result = e.getClass();
        }

        break;

      case localTimestampMillisConversion_fromLong:

        try{

          s = String.valueOf(input);

          convertedLong = Long.parseLong(s);

          result = localTimestampMillisConversion.fromLong(convertedLong, LogicalTypes.localTimestampMillis().addToSchema(Schema.create(Schema.Type.LONG)), LogicalTypes.localTimestampMillis());

        } catch (Exception e) {

          result = e.getClass();
        }
        break;

      case localTimestampMillisConversion_toLong:

        try {

          s = String.valueOf(input);

          LocalDateTime localDateTime = LocalDateTime.parse(s);

          result = localTimestampMillisConversion.toLong(localDateTime, LogicalTypes.localTimestampMillis().addToSchema(Schema.create(Schema.Type.LONG)), LogicalTypes.localTimestampMillis());

        } catch (Exception e) {

          result = e.getClass();
        }

        break;

      default:
        throw new IllegalStateException("Unexpected value: " + typeTest);
    }

    Assert.assertEquals(result, expected);

  }

}
