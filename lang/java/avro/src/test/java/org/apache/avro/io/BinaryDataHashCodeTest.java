package org.apache.avro.io;

import org.apache.avro.AvroRuntimeException;
import org.apache.avro.Schema;
import org.apache.avro.UnresolvedUnionException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(value = Parameterized.class)
public class BinaryDataHashCodeTest {

  private final Schema.Type typeBytes;
  private final int start;
  private final int length;
  private final Schema.Type type;

  private final Boolean b;

  private final Object expected;

  public BinaryDataHashCodeTest(Schema.Type typeBytes, Boolean b, int start, int length, Schema.Type type, Object expected){

    this.typeBytes = typeBytes;
    this.b = b;
    this.start = start;
    this.length = length;
    this.type = type;
    this.expected = expected;

  }

  @Parameterized.Parameters
  public static Collection getParameters() {

    return Arrays.asList(new Object[][]{

      {Schema.Type.ENUM, true, 0, 1, Schema.Type.ENUM, 0},
      {Schema.Type.MAP, true, 0, 0, Schema.Type.MAP, 0},
      {Schema.Type.NULL, true, 0, 1, Schema.Type.RECORD, 0},
      {Schema.Type.NULL, false, 1, 0, Schema.Type.UNION, 0},
      {Schema.Type.NULL, true, 1, 0, Schema.Type.ARRAY, 0},
      {Schema.Type.NULL, true, 1, 0, Schema.Type.BYTES, 0},
      {Schema.Type.ARRAY, true, 0, 0, Schema.Type.NULL, 0},
      {Schema.Type.ARRAY, false, 1, 2, Schema.Type.INT, 5},

      {Schema.Type.ARRAY, false, 0, 0, Schema.Type.NULL, 0},
      {Schema.Type.ARRAY, true, -1, 2, Schema.Type.NULL, 0},

      {Schema.Type.ARRAY, true, 1, 2, Schema.Type.INT, -5},

      {Schema.Type.RECORD, true, 1, 2, Schema.Type.INT, -33},
      {Schema.Type.RECORD, true, 1, 2, Schema.Type.RECORD, ArrayIndexOutOfBoundsException.class},

      {Schema.Type.RECORD, true, 1, 2, Schema.Type.ENUM, -33},
      {Schema.Type.RECORD, true, 1, 2, Schema.Type.BOOLEAN, 1237},
      {Schema.Type.RECORD, false, 1, 2, Schema.Type.ARRAY, UnresolvedUnionException.class},

      {Schema.Type.RECORD, true, 1, 2, Schema.Type.STRING, 0},
      {Schema.Type.RECORD, true, 0, 0, Schema.Type.NULL, 0},

      {Schema.Type.ARRAY, false, 1, 2, Schema.Type.ARRAY, AvroRuntimeException.class},
      {Schema.Type.BOOLEAN, false, 1, 0, Schema.Type.BOOLEAN,AvroRuntimeException.class},
      {Schema.Type.FIXED, true, 0, 0, Schema.Type.FIXED, AvroRuntimeException.class},
      {Schema.Type.STRING, true, 0, 0, Schema.Type.STRING, AvroRuntimeException.class},
      {Schema.Type.INT, false, 0, 0, Schema.Type.INT, AvroRuntimeException.class},
      {Schema.Type.LONG, true, 0, 0, Schema.Type.LONG, AvroRuntimeException.class},
      {Schema.Type.INT, false, 0, 1, Schema.Type.RECORD, AvroRuntimeException.class}

    });
  }

  @Test
  public void test(){

    Object result;
    try {

      Schema schema = BinaryDataUtils.createSchema(type);

      String test = "hashCode";
      byte[] bytes = BinaryDataUtils.createByteArray(typeBytes, b, test);

      if(bytes != null) {

        result = BinaryData.hashCode(bytes, start, length, schema);

      }else{

        result =  0;

      }

    } catch (Exception e) {

      result = e.getClass();

    }

    Assert.assertEquals(result, expected);

  }






}
