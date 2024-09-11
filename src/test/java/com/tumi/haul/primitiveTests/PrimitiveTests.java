package com.tumi.haul.primitiveTests;


import com.tumi.haul.model.primitives.Name;
import com.tumi.haul.model.primitives.Price;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class PrimitiveTests {
    /*
    @Test
   void testValidId(){
       UserId id = new IdChar("thisismyid");
       Assertions.assertEquals("thisismyid", id.getValue());
   }
   @Test
    void testInvalidId(){
        Assertions.assertThrows(IllegalArgumentException.class, () -> new IdChar("invalidid"));
   }*/
   @Test
   void testValidPrice(){
       Price price = new Price(new BigDecimal("19"));
       Assertions.assertEquals(new BigDecimal("19"), price.getValue());
   }
   @Test
  void testInvalidPrice(){
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Price(new BigDecimal("-10")));
  }
  @Test
  void testValidName(){
   Name name = new Name("Edward");
   Assertions.assertEquals("Edward", name.getValue());
  }
  @Test
  void testInvalidName(){
        //also tested Name length
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Name("E"));
  }
  /*
  @Test
  void testingValidUser(){
        User user = new User(
                UUID.randomUUID(),
                new Name("Edward"),
                new String("djdjdjd"),
                new String("OUud"),
                //Role.CLIENT,
                new CreationDate(LocalDateTime.now())
        );

  }*/
}
