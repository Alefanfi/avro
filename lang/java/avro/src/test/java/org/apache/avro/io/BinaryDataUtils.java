package org.apache.avro.io;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericFixed;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.generic.GenericData.Array;
import org.apache.avro.specific.SpecificDatumWriter;
import org.junit.Assert;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class BinaryDataUtils {

  public static byte[] createByteArray(Schema.Type type, Boolean b, String test) throws IOException {

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

        SpecificDatumWriter<Array> datumWriterArray = new SpecificDatumWriter<>(schema);
        GenericData.Array<Integer> c;

        if(b){

          c = new GenericData.Array<>(3, schema);

          c.add(-5);
          c.add(-2);
          c.add(0);
          c.add(5);

        }else{

          c = new GenericData.Array<>(5, schema);

          c.add(5);
          c.add(2);
          c.add(1);
          c.add(7);
          c.add(-5);

        }

        datumWriterArray.write(c, binaryEncoder);
        binaryEncoder.flush();

        bytes = out.toByteArray();

        break;

      case MAP:

        //Exeption
        break;

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

        break;

      case STRING:

        if(test.equals("hashCode")) {

          if(b) {

            binaryEncoder.writeString("test");

          }else{

            binaryEncoder.writeString("tteestt");

          }

          binaryEncoder.flush();

          bytes = out.toByteArray();

        }

        break;

      case BYTES:

        if(b){

          binaryEncoder.writeBytes("test".getBytes());

        }else{

          binaryEncoder.writeBytes("teest".getBytes());

        }

        binaryEncoder.flush();

        bytes = out.toByteArray();

        break;

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
