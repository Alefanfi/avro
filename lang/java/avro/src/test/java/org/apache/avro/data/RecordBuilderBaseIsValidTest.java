package org.apache.avro.data;

import org.apache.avro.Schema;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;


@RunWith(value = Parameterized.class)
public class RecordBuilderBaseIsValidTest {

  private final Schema.Type typeSchema;
  private final Object value;

  private final Object expected;

  public RecordBuilderBaseIsValidTest(Schema.Type typeSchema, Object value, Object expected){

    this.typeSchema = typeSchema;
    this.value = value;
    this.expected = expected;
  }

  @Parameterized.Parameters
  public static Collection getParameters() {

    return Arrays.asList(new Object[][]{

      {Schema.Type.NULL, null, true},
      {Schema.Type.INT, null, false},
      {Schema.Type.UNION, new Object(), true},
      {Schema.Type.RECORD, new Object(), true},
      {Schema.Type.ENUM, new Object(), true},
      {Schema.Type.LONG, new Object(), true},
      {Schema.Type.FLOAT, new Object(), true},
      {Schema.Type.DOUBLE, new Object(), true},
      {Schema.Type.BYTES, new Object(), true},
      {Schema.Type.BOOLEAN, new Object(), true},
      {Schema.Type.ARRAY, new Object(), true},
      {Schema.Type.MAP, null, NullPointerException.class},
      {Schema.Type.STRING, new Object(), true},
      {Schema.Type.FIXED, new Object(), true},
      {Schema.Type.INT, 12345, true},
      {Schema.Type.FLOAT, "dfrgg", true}

    });
  }

  @Test
  public void test(){

    Object result;

    try {

      Schema.Field field = createField(typeSchema);

      result = RecordBuilderBase.isValidValue(field, value);

    } catch (Exception e) {

      result = e.getClass();
    }

    Assert.assertEquals(result, expected);

  }

  @Test
  public void testUNION(){

    Schema schemaUnionNull = Schema.createUnion(Arrays.asList(
      Schema.create(Schema.Type.INT), Schema.create(Schema.Type.LONG),
      Schema.create(Schema.Type.STRING), Schema.create(Schema.Type.FLOAT),
      Schema.create(Schema.Type.DOUBLE), Schema.create(Schema.Type.BOOLEAN),
      Schema.create(Schema.Type.NULL)));

    Schema.Field field = createField2(schemaUnionNull);

    Assert.assertTrue(RecordBuilderBase.isValidValue(field, new Object()));
    Assert.assertTrue(RecordBuilderBase.isValidValue(field, null));

    Schema schemaUnion = Schema.createUnion(Arrays.asList(
      Schema.create(Schema.Type.INT), Schema.create(Schema.Type.LONG),
      Schema.create(Schema.Type.STRING), Schema.create(Schema.Type.FLOAT),
      Schema.create(Schema.Type.DOUBLE), Schema.create(Schema.Type.BOOLEAN)));

    Schema.Field field2 = createField2(schemaUnion);

    Assert.assertTrue(RecordBuilderBase.isValidValue(field2, new Object()));
    Assert.assertFalse(RecordBuilderBase.isValidValue(field2, null));
  }

  private Schema.Field createField2(Schema schema) {

    Schema.Field f = null;

    try{

      f = new Schema.Field("f", schema, null, null);

    } catch (Exception e) {
      return null;
    }

    return f;
  }


  public Schema.Field createField(Schema.Type type){

    Schema.Field f = null;

    try{

      f = new Schema.Field("f", Schema.create(type), null, null);

    } catch (Exception e) {
      return null;
    }

    return f;

  }

}
