package test;

import org.junit.jupiter.api.Test;

import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.*;

class ArgsTest {

    @Test
    void testInvalidBoolean() throws Exception {
        Args args = new Args("l", new String[]{"-l"});
        assertTrue(args.isValid());
        assertTrue(args.getBoolean('l'));
        assertEquals(1, args.cardinality());
    }

    @Test
    void testInvalidString() throws Exception {
        Args args = new Args("d*", new String[]{"-d", "qwqr"});
        assertTrue(args.isValid());
        assertEquals("qwqr", args.getString('d'));
        assertEquals(1, args.cardinality());
    }

    @Test
    void testInvalidBooleanAndString() throws Exception {
        Args args = new Args("l,d*",new String[]{"-l","-d","qwqr"});
        assertTrue(args.isValid());
        assertTrue(args.getBoolean('l'));
        assertEquals("qwqr", args.getString('d'));
        assertEquals(2, args.cardinality());
    }
}
