import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;


public class TestJaggedArray {
    private JaggedArray<Character> jagged;
    private int DEFAULTSIZE = 5;

    @Before
    public void setup() {
        jagged = new JaggedArray<Character>(DEFAULTSIZE);
    }

    @Test
    public void testCreate() {
        assertTrue(jagged != null);
        assertEquals(jagged.numberOfBins(), DEFAULTSIZE);
        for (int i = 0; i < DEFAULTSIZE; i++) {
            assertEquals(0, jagged.numberOfSlots(i));
        }
    }

    @Test
    public void testAdd() {
        jagged.addElement(0, 'a');
        jagged.addElement(2, 'b');
        jagged.addElement(2, 'c');
        jagged.addElement(2, 'd');

        assertTrue(jagged != null);
        assertEquals(4, jagged.size());
        assertEquals(jagged.numberOfBins(), DEFAULTSIZE);
        assertEquals(1, jagged.numberOfSlots(0));
        assertEquals(3, jagged.numberOfSlots(2));

        assertEquals('a', jagged.getElement(0, 0));
        assertEquals('b', jagged.getElement(2, 0));
        assertEquals('c', jagged.getElement(2, 1));
        assertEquals('d', jagged.getElement(2, 2));
    }

    @Test
    public void testRemove() {
        jagged.addElement(0, 'a');
        jagged.addElement(2, 'b');
        jagged.addElement(2, 'c');
        jagged.addElement(2, 'd');

        jagged.removeElement(0, 0);
        assertEquals(3, jagged.size());
        assertEquals(jagged.numberOfBins(), DEFAULTSIZE);
        assertEquals(0, jagged.numberOfSlots(0));
        assertEquals(3, jagged.numberOfSlots(2));

        jagged.removeElement(2, 1);
        assertEquals(2, jagged.size());
        assertEquals('d', jagged.getElement(2, 1));
    }

    @Test
    public void testPack() {
        jagged.addElement(0, 'a');
        jagged.addElement(2, 'b');
        jagged.addElement(2, 'c');
        jagged.addElement(2, 'd');

        jagged.pack();

        assertEquals(null, jagged.getUnpackedValues());
        assertFalse(jagged.addElement(3, 'e'));
        assertFalse(jagged.removeElement(2, 0));

        assertEquals('a', jagged.getElement(0, 0));
        assertEquals('b', jagged.getElement(2, 0));
        assertEquals('c', jagged.getElement(2, 1));
        assertEquals('d', jagged.getElement(2, 2));

        Object[] values = jagged.getPackedValues();
        assertEquals(4, values.length);
        assertEquals('a', values[0]);
        assertEquals('b', values[1]);
        assertEquals('c', values[2]);
        assertEquals('d', values[3]);

        int[] offsets = jagged.getOffsets();
        assertEquals(0, offsets[0]);
        assertEquals(1, offsets[1]);
        assertEquals(1, offsets[2]);
        assertEquals(4, offsets[3]);
        assertEquals(4, offsets[4]);
    }

    @Test
    public void testUnpack() {
        jagged.addElement(0, 'a');
        jagged.addElement(2, 'b');
        jagged.addElement(2, 'c');
        jagged.addElement(2, 'd');

        jagged.pack();
        jagged.unpack();

        assertEquals(null, jagged.getPackedValues());
        assertEquals(null, jagged.getOffsets());
        assertEquals('a', jagged.getElement(0, 0));
        assertEquals('b', jagged.getElement(2, 0));
        assertEquals('c', jagged.getElement(2, 1));
        assertEquals('d', jagged.getElement(2, 2));
    }

    @Test
    public void testInvalid() {
        try {
            jagged.addElement(10, 'z');
            fail();
        } catch (Exception e) {
            // expected
            assertEquals(IndexOutOfBoundsException.class, e.getClass());
        }

        jagged.addElement(0, 'a');
        jagged.addElement(2, 'b');
        jagged.addElement(2, 'c');
        jagged.addElement(2, 'd');
        try {
            jagged.removeElement(2, 4);
            fail();
        } catch (Exception e) {
            // expected
            assertEquals(IndexOutOfBoundsException.class, e.getClass());
        }

        assertTrue(jagged.pack());
        assertFalse(jagged.pack());
        assertTrue(jagged.unpack());
        assertFalse(jagged.unpack());
    }

}
