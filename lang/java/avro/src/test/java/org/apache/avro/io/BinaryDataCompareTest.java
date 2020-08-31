package org.apache.avro.io;

import org.apache.avro.AvroRuntimeException;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericFixed;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.specific.SpecificDatumWriter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.ByteArrayOutputStream;
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

  private Object expected;
  private Object result;

  public BinaryDataCompareTest( int s1,  int s2, Schema.Type type1, Schema.Type type2, Schema.Type typeSchema, Boolean bB1, Boolean bB2, Object expected) {

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

      //{-1,-2, Schema.Type.RECORD, Schema.Type.RECORD, Schema.Type.RECORD, true, true, 0},
      {1, 0, Schema.Type.UNION, Schema.Type.UNION, Schema.Type.UNION, false, true, 1}

    });
  }

  @Test
  public void test(){

    try {

      Schema schema = createSchema(typeSchema);

      byte[] b1 = createByteArray(type1, bB1);

      byte[] b2 = createByteArray(type2, bB2);

      if (b1 == null || b2 == null) {

        result = 0;

      } else {

        result = BinaryData.compare(b1, s1, b2, s2, schema);

      }
    } catch (IOException e) {
      e.printStackTrace();
      result = 0;
    }

    Assert.assertEquals(result, expected);

  }

  public static byte[] createByteArray(Schema.Type type, Boolean b) throws IOException {

    byte[] bytes = null;

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    out.reset();

    BinaryEncoder binaryEncoder = new EncoderFactory().binaryEncoder(out, null);

    Schema schema;
    String stringa;

    GenericRecord a;
    SpecificDatumWriter<GenericRecord> datumWriter;

    switch (type) {

      case RECORD:

        stringa = "{\"namespace\": \"example.avro\",\n" + " \"type\": \"record\",\n" + " \"name\": \"User\",\n"
          + " \"fields\": [\n" + "     {\"name\": \"name\", \"type\": \"string\"},\n"
          + "     {\"name\": \"favorite_number\",  \"type\": [\"int\", \"null\"]},\n"
          + "     {\"name\": \"favorite_color\", \"type\": [\"string\", \"null\"]}\n" + " ]\n" + "}";
        schema = new Schema.Parser().parse(stringa);

        a = new GenericData.Record(schema);
        datumWriter = new SpecificDatumWriter<>(schema);

        if(b){

          a.put("name", "Ale");
          a.put("favorite_color", "green");

        }else{

          a.put("name", "Ale");
          a.put("favorite_color", "green");
          a.put("favorite_number", "5");
        }

        datumWriter.write(a, binaryEncoder);
        binaryEncoder.flush();
        bytes = out.toByteArray();

      case ENUM:

        //Not defined!
        break;

      case INT:


        if(b) {

          binaryEncoder.writeInt(2147483647);

        }else{

          binaryEncoder.writeInt(2147483646);
        }

        binaryEncoder.flush();

        bytes = out.toByteArray();

        break;

      case LONG:

        if(b) {

          binaryEncoder.writeLong(9223372036854775806L);

        }else{

          binaryEncoder.writeLong(9223372036854775805L);

        }

        binaryEncoder.flush();

        bytes = out.toByteArray();

        break;

      case FLOAT:

        if(b) {
          binaryEncoder.writeFloat(10000);

        }else{

          binaryEncoder.writeFloat(20000);

        }

        binaryEncoder.flush();

        bytes = out.toByteArray();

        break;

      case DOUBLE:

        if(b){

          binaryEncoder.writeDouble(1.0);

        }else {

          binaryEncoder.writeDouble(1.5);

        }

        binaryEncoder.flush();

        bytes = out.toByteArray();

        break;

      case BOOLEAN:

        binaryEncoder.writeBoolean(b);
        binaryEncoder.flush();

        bytes = out.toByteArray();

        break;

      case ARRAY:

        stringa = "{\"type\": \"array\", \"items\": \"int\"}";
        schema = new Schema.Parser().parse(stringa);

        a = new GenericData.Record(schema);
        datumWriter = new SpecificDatumWriter<>(schema);

        if(b){

          a.put("items", "5");

        }else{

          a.put("items", "50");
        }

        datumWriter.write(a, binaryEncoder);
        binaryEncoder.flush();

        bytes = out.toByteArray();

      case MAP:

        throw new AvroRuntimeException("Can't compare maps!");

      case FIXED:

        stringa = "{\"type\" : \"fixed\" , \"name\" : \"data\", \"size\" : 1024}";
        schema = new Schema.Parser().parse(stringa);

        SpecificDatumWriter<GenericFixed> datumWriterFixed = new SpecificDatumWriter<>(schema);

        byte[] genericFixedBytes = new byte[1024];

        if(b){

          genericFixedBytes[0] = 5;

        }else{

          genericFixedBytes[0] = 6;
        }

        GenericFixed genericFixed = new GenericData.Fixed(schema, genericFixedBytes);

        datumWriterFixed.write(genericFixed, binaryEncoder);

        binaryEncoder.flush();

        bytes = out.toByteArray();

      case STRING:

        //not defined!
        break;

      case BYTES:

       if(b){

         binaryEncoder.writeBytes("test".getBytes());

       }else{

         binaryEncoder.writeBytes("teest".getBytes());

       }

       binaryEncoder.flush();

       bytes = out.toByteArray();

      case NULL:

        break;

      case UNION:

        stringa = "{ \n" + "   \"type\" : \"record\", \n" + "   \"namespace\" : \"tutorialspoint\", \n"
          + "   \"name\" : \"empdetails\", \n" + "   \"fields\" : \n" + "   [ \n"
          + "      {\"name\" : \"experience\", \"type\": [\"int\", \"null\"]}, {\"name\" : \"age\", \"type\": \"int\"} \n"
          + "   ] \n" + "}";

        schema = new Schema.Parser().parse(stringa);

        a = new GenericData.Record(schema);
        datumWriter = new SpecificDatumWriter<>(schema);

        if(b){

          a.put("experience", 25);
          a.put("age", 18);

        }else{

          a.put("experience", 18);
          a.put("age", 25);

        }
        datumWriter.write(a, binaryEncoder);
        binaryEncoder.flush();
        bytes = out.toByteArray();

        break;

      default:
        Assert.assertEquals(0, 0);

    }

    return bytes;

  }

  public static Schema createSchema(Schema.Type type){

    Schema schema = null;
    String stringa;

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

        schema = Schema.create(Schema.Type.DOUBLE);

        break;

      case ARRAY:

        stringa = "{\"type\": \"array\", \"items\": \"int\"}";
        schema = new Schema.Parser().parse(stringa);

        break;

      case ENUM:

        stringa = "{\"type\": \"enum\",\n" + "  \"name\": \"Suit\",\n"
          + "  \"symbols\" : [\"ARRAY\", \"INT\", \"DIAMONDS\", \"CLUBS\"]\n" + "}";
        schema = new Schema.Parser().parse(stringa);

        break;

      case FIXED:

        stringa = "{\"type\" : \"fixed\" , \"name\" : \"data\", \"size\" : 1024}";
        schema = new Schema.Parser().parse(stringa);

        break;

      case UNION:

        stringa = "{ \n" + "   \"type\" : \"record\", \n" + "   \"namespace\" : \"tutorialspoint\", \n"
          + "   \"name\" : \"empdetails\", \n" + "   \"fields\" : \n" + "   [ \n"
          + "      {\"name\" : \"experience\", \"type\": [\"int\", \"null\"]}, {\"name\" : \"age\", \"type\": \"int\"} \n"
          + "   ] \n" + "}";

        schema = new Schema.Parser().parse(stringa);

        break;

      case MAP:

        stringa = "{\"type\" : \"map\", \"values\" : \"int\"}";
        schema = new Schema.Parser().parse(stringa);
        break;

      case RECORD:

        stringa = "{\"namespace\": \"example.avro\",\n" + " \"type\": \"record\",\n" + " \"name\": \"User\",\n"
          + " \"fields\": [\n" + "     {\"name\": \"name\", \"type\": \"string\"},\n"
          + "     {\"name\": \"favorite_number\",  \"type\": [\"int\", \"null\"]},\n"
          + "     {\"name\": \"favorite_color\", \"type\": [\"string\", \"null\"]}\n" + " ]\n" + "}";
        schema = new Schema.Parser().parse(stringa);
        break;

      default:
    }

    return schema;

  }
}
