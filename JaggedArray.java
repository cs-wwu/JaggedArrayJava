import java.util.LinkedList;

/**
 * Represent a JaggedArray of characters in n bins
 */
public class JaggedArray<T> {
    // number of elements
    private int numberOfElements;

    // Unpacked representation
    private LinkedList<T>[] unpackedValues;

    // Packed representation
    private int[] offsets;
    private T[] packedValues;

    /**
     * Create a unpacked JaggedArray with the given number of bins
     *
     * @param bins The number of bins to create
     */
    public JaggedArray(int bins) {
        unpackedValues = createUnpackedValues(bins);

        offsets = null;
        packedValues = null;

        numberOfElements = 0;
    }

    private LinkedList<T>[] createUnpackedValues(int bins) {
        LinkedList<T>[] values = (LinkedList<T>[]) new LinkedList[bins];
        for (int i = 0; i < bins; i++) {
            values[i] = new LinkedList<T>();
        }
        return values;
    }

    /**
     * Return the number of elements in the jagged array
     */
    public int size() {
        return numberOfElements;
    }

    /**
     * Return the number of bins
     */
    public int numberOfBins() {
        if (unpackedValues != null) {
            return unpackedValues.length;
        } else {
            return offsets.length;
        }
    }

    /**
     * Return the number of slots in the given bin
     */
    public int numberOfSlots(int bin) {
        if (unpackedValues != null) {
            return unpackedValues[bin].size();
        } else {
            if (bin < numberOfBins() - 1) {
                return offsets[bin + 1] - offsets[bin];
            } else {
                // return offsets.length - offsets[bin] - 1;
                return numberOfElements - offsets[bin] - 1;
            }
        }
    }

    /**
     * Return the element at the given bin and slot number
     */
    public T getElement(int bin, int slot) throws IndexOutOfBoundsException {
        if (unpackedValues != null) {
            return unpackedValues[bin].get(slot);
        } else {
            return packedValues[offsets[bin] + slot];
        }
    }

    /**
     * Add an element into the bin at the end
     */
    public boolean addElement(int bin, T element) throws IndexOutOfBoundsException {
        if (unpackedValues == null) {
            return false;
        }

        if (bin < 0 || bin > numberOfBins()) {
            throw new IndexOutOfBoundsException();
        }

        unpackedValues[bin].addLast(element);
        numberOfElements += 1;
        return true;
    }

    /**
     * Remove an element at the given bin and slot number
     */
    public boolean removeElement(int bin, int slot) throws IndexOutOfBoundsException {
        if (unpackedValues == null) {
            return false;
        }

        if (bin < 0 || bin > numberOfBins() || slot < 0 || slot > numberOfSlots(bin)) {
            throw new IndexOutOfBoundsException();
        }

        unpackedValues[bin].remove(slot);
        numberOfElements -= 1;
        return true;
    }

    /**
     * Return the unpacked array of linked lists
     */
    public LinkedList<T>[] getUnpackedValues() {
        return unpackedValues;
    }

    /**
     * Return the packed values.
     */
    public T[] getPackedValues() {
        return packedValues;
    }

    /**
     * Return the offsets array
     */
    public int[] getOffsets() {
        return offsets;
    }

    /**
     * Unpack the representation from packed.
     *
     * @return True if successful, or false if already unpacked
     */
    public boolean unpack() {
        if (unpackedValues != null) {
            return false;
        }

        int bins = numberOfBins();
        LinkedList<T>[] values = createUnpackedValues(bins);
        for (int i = 0; i < bins; i++) {
            for (int j = 0; j < numberOfSlots(i); j++) {
                values[i].addLast(getElement(i, j));
            }
        }
        unpackedValues = values;
        packedValues = null;
        offsets = null;

        return true;
    }

    /**
     * Pack the values
     *
     * @return True if successful, or false if already packed
     */
    public boolean pack() {
        if (unpackedValues == null) {
            return false;
        }

        int bins = numberOfBins();
        int[] newOffsets = new int[bins];
        T[] newValues = (T[]) new Object[size()];
        int previousOffset = 0;
        int previousSize = 0;
        for (int i = 0; i < bins; i++) {
            newOffsets[i] = previousOffset + previousSize;
            for (int j = 0; j < numberOfSlots(i); j++) {
                newValues[newOffsets[i] + j] = getElement(i, j);
            }
            previousOffset = newOffsets[i];
            previousSize = unpackedValues[i].size();
        }

        packedValues = newValues;
        offsets = newOffsets;
        unpackedValues = null;
        return true;
    }

    /**
     * A helpful method to print out a jagged array. Useful for debugging.
     */
    public void print() {
        System.out.println("JaggedArray(Number of Bins=" + numberOfBins());
        for (int i = 0; i < numberOfBins(); i++) {
            System.out.print("\tBin " + i + "(Slots=" + numberOfSlots(i) + ": ");
            for (int j = 0; j < numberOfSlots(i); j++) {
                System.out.print(getElement(i, j));
                if (j < numberOfSlots(i) - 1) {
                    System.out.print(", ");
                }
            }
            System.out.println(")");
        }
        System.out.println(")");
    }
}
