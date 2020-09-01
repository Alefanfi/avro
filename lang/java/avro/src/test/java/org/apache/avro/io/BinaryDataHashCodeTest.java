package org.apache.avro.io;

import junit.framework.TestCase;
import org.apache.avro.AvroRuntimeException;
import org.apache.avro.Schema;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

@RunWith(value = Parameterized.class)
public class BinaryDataHashCodeTest extends TestCase {

  private Schema.Type typeBytes;
  private int start;
  private int length;
  private Schema.Type type;

  private String test = "hashCode";

  private Boolean b;

  private Object expected;
  private Object result;

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

      {Schema.Type.NULL, true, 0, 1, Schema.Type.RECORD, 0},
      {Schema.Type.UNION, true, 1, 0, Schema.Type.UNION, 0}
      //{1, 0, Schema.Type.UNION, Schema.Type.UNION, Schema.Type.UNION, false, true, 1}

    });
  }

  @Test
  public void test(){

    try {

      Schema schema = BinaryDataUtils.createSchema(type);

      byte[] bytes = BinaryDataUtils.createByteArray(typeBytes, b, test);

      if(bytes != null) {

        result = BinaryData.hashCode(bytes, start, length, schema);

      }else{

        result =  0;

      }

    } catch (Exception e) {
      e.printStackTrace();
      result = 0;
    }

    Assert.assertEquals(result, expected);

  }

}
