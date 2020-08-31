package org.apache.avro.io;

import org.apache.avro.AvroRuntimeException;
import org.apache.avro.Schema;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import sun.jvmstat.perfdata.monitor.CountedTimerTaskUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.ScheduledExecutorService;

import static org.junit.Assert.*;

@RunWith(value = Parameterized.class)
public class BinaryDataCompareTest {

  private byte[] b1;
  private int s1;
  private byte[] b2;
  private int s2;
  private Schema.Type type;
  private Boolean bB1;
  private Boolean bB2;

  private Object expected;
  private Object result;

  public BinaryDataCompareTest( int s1,  int s2, Schema.Type type, Boolean bB1, Boolean bB2, Object expected) {

    this.s1 = s1;
    this.s2 = s2;
    this.type = type;
    this.bB1 = bB1;
    this.bB2 = bB2;
    this.expected = expected;
  }

  @Parameterized.Parameters
  public static Collection getParameters() {

    return Arrays.asList(new Object[][]{

      {-1,-2, Schema.Type.RECORD, NullPointerException.class}

    });
  }

  @Test
  public void test() throws IOException {

    Schema schema = createSchema(type);

    b1 = createByteArray(type, bB1, schema);

    b2 = createByteArray(type, bB2, schema);

    result = BinaryData.compare(b1, s1, b2, s2, schema);

    Assert.assertEquals(result, expected);

  }

  public static byte[] createByteArray(Schema.Type type, Boolean b, Schema schema) throws IOException {

    byte[] bytes = null;

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    out.reset();

    BinaryEncoder binaryEncoder = new EncoderFactory().binaryEncoder(out, null);

    switch (type) {

      case RECORD:

      case ENUM:

        //Not defined!
        break;

      case INT:

        binaryEncoder.writeInt(2147483647);
        binaryEncoder.flush();

        bytes = out.toByteArray();

        break;

      case LONG:

        binaryEncoder.writeLong(9223372036854775806L);
        binaryEncoder.flush();

        bytes = out.toByteArray();

        break;

      case FLOAT:

        binaryEncoder.writeFloat(10000);
        binaryEncoder.flush();

        bytes = out.toByteArray();

        break;

      case DOUBLE:

        binaryEncoder.writeDouble(1.0);
        binaryEncoder.flush();

        bytes = out.toByteArray();

        break;

      case BOOLEAN:

        binaryEncoder.writeBoolean(b);
        binaryEncoder.flush();

        bytes = out.toByteArray();

        break;

      case ARRAY:

        if(b){

        }

      case MAP:

        throw new AvroRuntimeException("Can't compare maps!");

      case FIXED:



      case STRING:

        //not defined!
        break;

      case BYTES:
      case NULL:

        return null;

      default:
        Assert.assertEquals(0, 0);

    }

    return bytes;

  }

  public static Schema createSchema(Schema.Type type){

    Schema schema = null;
    String string;

    switch (type){

      case BYTES:

        schema = Schema.create(Schema.Type.BYTES);

        break;

      case INT:

        schema = Schema.create(Schema.Type.INT);

        break;

      case BOOLEAN:

        schema = Schema.create(Schema.Type.BOOLEAN);

        break;

      case FLOAT:

        schema = Schema.create(Schema.Type.FLOAT);

        break;

      case STRING:

        schema = Schema.create(Schema.Type.STRING);

        break;

      case LONG:

        schema = Schema.create(Schema.Type.LONG);

        break;

      case NULL:

        schema = Schema.create(Schema.Type.NULL);

        break;

      case DOUBLE:

        schema = schema.create(Schema.Type.DOUBLE);

        break;

      case ARRAY:

        string = "{\"type\": \"array\", \"items\": \"int\"}";
        schema = new Schema.Parser().parse(string);

        break;

      case ENUM:

        string = "{\"type\": \"enum\",\n" + "  \"name\": \"Suit\",\n"
          + "  \"symbols\" : [\"ARRAY\", \"INT\", \"DIAMONDS\", \"CLUBS\"]\n" + "}";
        schema = new Schema.Parser().parse(string);

        break;

      case FIXED:

        string = "{\"type\" : \"fixed\" , \"name\" : \"bdata\", \"size\" : 1024}";
        schema = new Schema.Parser().parse(string);

        break;

      case UNION:

        string = "{ \n" + "   \"type\" : \"record\", \n" + "   \"namespace\" : \"tutorialspoint\", \n"
          + "   \"name\" : \"empdetails\", \n" + "   \"fields\" : \n" + "   [ \n"
          + "      {\"name\" : \"experience\", \"type\": [\"int\", \"null\"]}, {\"name\" : \"age\", \"type\": \"int\"} \n"
          + "   ] \n" + "}";

        schema = new Schema.Parser().parse(string);

        break;

      case MAP:

        string = "{\"type\" : \"map\", \"values\" : \"int\"}";
        schema = new Schema.Parser().parse(string);
        break;

      case RECORD:

        string = "{\"namespace\": \"example.avro\",\n" + " \"type\": \"record\",\n" + " \"name\": \"User\",\n"
          + " \"fields\": [\n" + "     {\"name\": \"name\", \"type\": \"string\"},\n"
          + "     {\"name\": \"favorite_number\",  \"type\": [\"int\", \"null\"]},\n"
          + "     {\"name\": \"favorite_color\", \"type\": [\"string\", \"null\"]}\n" + " ]\n" + "}";
        schema = new Schema.Parser().parse(string);
        break;

      default:
    }

    return schema;

  }
}
