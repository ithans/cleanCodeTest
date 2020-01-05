package test;

import draught2.Args;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ArgsTest {
    @Test
    void testSimpleDoublePresent() throws Exception {
        Args args = new Args("x##", new String[]{"-x", "42.3"});
        Assertions.assertTrue(args.isValid());
        Assertions.assertEquals(1,args.cardinality());
        Assertions.assertTrue(args.has('x'));
        Assertions.assertEquals(42.3,args.getDouble('x'),0.001);
    }
}
