package org.apache.avro.data;

import org.apache.avro.Schema;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

@RunWith(value = Parameterized.class)
public class TimeConversionsTest {

  private Object expected;
  private Object result;

  private String typeTest;
  private String stringConvert;

  private Integer numberOfDay;

  private static final String dateConversion_toInt = "DATE CONVERSION_toInt";
  private static final String dateConversion_fromInt = "DATE CONVERSION_fromInt";

  private static final String timeMillisConversion_toInt = "TimeMillisConversion_toInt";
  private static final String timeMillisConversion_fromInt = "TimeMillisConversion_fromInt";

  public TimeConversionsTest(String typeTest, String stringConvert, Integer numberOfDay, Object expected){

    this.typeTest = typeTest;
    this.stringConvert = stringConvert;
    this.numberOfDay = numberOfDay;
    this.expected = expected;

  }

  @Parameterized.Parameters
  public static Collection getParameters() {

    return Arrays.asList(new Object[][]{

      {dateConversion_toInt, "2019-11-15", null, 18215},
      {dateConversion_toInt, "18-02-1920", null, "String form not valid"},
      {dateConversion_toInt, "", null, "String form not valid"},

      {dateConversion_fromInt, null, 18215, LocalDate.parse("2019-11-15")},
      {dateConversion_fromInt, null, 0, LocalDate.parse("1970-01-01")},
      {dateConversion_fromInt, null, -18215, LocalDate.parse("1920-02-18")},

      {timeMillisConversion_toInt, "19:34:50.63", null, 70490630},
      {timeMillisConversion_toInt, "00:00", null, 0},
      {timeMillisConversion_toInt, "", null, "String form not valid"},

      {timeMillisConversion_fromInt, null, 70490630, LocalTime.parse("19:34:50.63")},
      {timeMillisConversion_fromInt, null, 0, LocalTime.parse("00:00")},
      {timeMillisConversion_fromInt, null, -70490630, "Can't convert negative value"}

    });
  }

  @Test
  public void test(){

    switch (typeTest) {

      case dateConversion_toInt:

        if (stringConvert.matches("^\\d{4}-\\d{2}-\\d{2}$")){

          LocalDate localDate = LocalDate.parse(stringConvert);

          result = new TimeConversions.DateConversion().toInt(localDate, null, null);

        }else{

          result = "String form not valid";
        }
        break;

      case dateConversion_fromInt:

        result = new TimeConversions.DateConversion().fromInt(numberOfDay, null, null);

        break;

      case timeMillisConversion_toInt:

        if(stringConvert.matches("^\\d{2}:\\d{2}:\\d{2}.\\d{2}$") || stringConvert.matches("^\\d{2}:\\d{2}$")) {

          LocalTime localTime = LocalTime.parse(stringConvert);

          result = new TimeConversions.TimeMillisConversion().toInt(localTime, null, null);

        }else{

          result = "String form not valid";

        }

        break;

      case timeMillisConversion_fromInt:

        if (numberOfDay < 0) {

          result = "Can't convert negative value";

        } else{

          result = new TimeConversions.TimeMillisConversion().fromInt(numberOfDay, null, null);

        }

        break;

      default:
        throw new IllegalStateException("Unexpected value: " + typeTest);
    }

    Assert.assertEquals(result, expected);

  }

}
