package org.apache.avro.io;

import org.apache.avro.AvroRuntimeException;
import org.apache.avro.Schema;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

@RunWith(value = Parameterized.class)
public class BinaryDataCompareTest {

  private int s1;
  private int s2;
  private Schema.Type type1;
  private Schema.Type type2;
  private Schema.Type typeSchema;
  private Boolean bB1;
  private Boolean bB2;
  private String test = "compare";

  private Object expected;
  private Object result;

  public BinaryDataCompareTest(int s1,  int s2, Schema.Type type1, Schema.Type type2, Schema.Type typeSchema, Boolean bB1, Boolean bB2, Object expected) {

    this.s1 = s1;
    this.s2 = s2;
    this.type1 = type1;
    this.type2 = type2;
    this.typeSchema = typeSchema;
    this.bB1 = bB1;
    this.bB2 = bB2;
    this.expected = expected;
  }

  @Parameterized.Parameters
  public static Collection getParameters() {

    return Arrays.asList(new Object[][]{

      {1, 0, Schema.Type.UNION, Schema.Type.UNION, Schema.Type.UNION, false, true, 1},
      {0, 0, Schema.Type.FIXED, Schema.Type.FIXED, Schema.Type.FIXED, true, true, 0},
      {0, 0, Schema.Type.STRING, Schema.Type.STRING, Schema.Type.STRING, true, false, 0},
      {0, 0, Schema.Type.INT, Schema.Type.INT, Schema.Type.INT, false, true, -1},
      {0, 0, Schema.Type.ENUM, Schema.Type.ENUM, Schema.Type.ENUM, false, false, 0},
      {0, 0, Schema.Type.LONG, Schema.Type.LONG, Schema.Type.LONG, true, false, 1},
      {0, 0, Schema.Type.MAP, Schema.Type.MAP, Schema.Type.MAP, true, false, 0},

      {-1,-2, Schema.Type.NULL, Schema.Type.NULL, Schema.Type.RECORD, true, true, 0},
      {0, 0, Schema.Type.NULL, Schema.Type.DOUBLE, Schema.Type.FLOAT, true, false, 0},
      {0, 0, Schema.Type.NULL, Schema.Type.DOUBLE, Schema.Type.BYTES, true, false, 0},
      {0, 0, Schema.Type.MAP, Schema.Type.DOUBLE, Schema.Type.BOOLEAN, true, false, 0},
      {1, 2, Schema.Type.ARRAY, Schema.Type.ARRAY, Schema.Type.INT, true, false, -1},
      {0, 0, Schema.Type.INT, Schema.Type.DOUBLE, Schema.Type.UNION, true, false, 1},
      {0, 0, Schema.Type.INT, Schema.Type.DOUBLE, Schema.Type.NULL, true, false, 0},
      {0, 0, Schema.Type.INT, Schema.Type.DOUBLE, Schema.Type.ARRAY, true, false, 1},
      {0, 0, Schema.Type.INT, Schema.Type.DOUBLE, Schema.Type.MAP, true, false, 0},
      {0, 0, Schema.Type.FLOAT, Schema.Type.DOUBLE, Schema.Type.FLOAT, true, false, 1},
      {0, 0, Schema.Type.ENUM, Schema.Type.ENUM, Schema.Type.FLOAT, true, false, 0},

      {1, 0, Schema.Type.ARRAY, Schema.Type.ARRAY, Schema.Type.ARRAY, true, false, -1},
      {0, 0, Schema.Type.RECORD, Schema.Type.RECORD, Schema.Type.RECORD, true, true, 0},
      {0, 0, Schema.Type.LONG, Schema.Type.LONG, Schema.Type.LONG, false, true, -1},

      {0, 0, Schema.Type.LONG, Schema.Type.LONG, Schema.Type.LONG, true, true, 0},
      {1, 0, Schema.Type.ARRAY, Schema.Type.ARRAY, Schema.Type.ARRAY, true, true, 1},
      {0, 0, Schema.Type.ARRAY, Schema.Type.ARRAY, Schema.Type.ARRAY, true, true, 0},
      {0, 1, Schema.Type.ARRAY, Schema.Type.ARRAY, Schema.Type.ARRAY, true, false, -1},
      {1, 0, Schema.Type.STRING, Schema.Type.STRING, Schema.Type.STRING, false, true, 0},
      {0, 0, Schema.Type.BOOLEAN, Schema.Type.BOOLEAN, Schema.Type.BOOLEAN, true, false, 1},
      {0, 0, Schema.Type.FIXED, Schema.Type.FIXED, Schema.Type.FIXED, true, false, -1},
      {1, 0, Schema.Type.UNION, Schema.Type.UNION, Schema.Type.UNION, true, false, 1}

    });
  }

  @Test
  public void test(){

    try {

      if(typeSchema != Schema.Type.MAP) {

        Schema schema = BinaryDataUtils.createSchema(typeSchema);

        byte[] b1 = BinaryDataUtils.createByteArray(type1, bB1, test);

        byte[] b2 = BinaryDataUtils.createByteArray(type2, bB2, test);

        if (b1 == null || b2 == null) {

          result = 0;

        } else {

          result = BinaryData.compare(b1, s1, b2, s2, schema);

        }

      }else{

        result = 0;

      }

    } catch (IOException e) {
      e.printStackTrace();
      result = 0;
    }

    Assert.assertEquals(result, expected);

  }

}
