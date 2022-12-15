package lsieun.trove;

public abstract class TIntHash extends TPrimitiveHash
    implements TIntHashingStrategy {

    /** the set of ints */
    protected transient int[] _set;

    /** strategy used to hash values in this collection */
    protected final TIntHashingStrategy _hashingStrategy;

    /**
     * Creates a new <code>TIntHash</code> instance with the default
     * capacity and load factor.
     */
    public TIntHash() {
        _hashingStrategy = this;
    }

    /**
     * Creates a new <code>TIntHash</code> instance whose capacity
     * is the next highest prime above <tt>initialCapacity + 1</tt>
     * unless that value is already prime.
     *
     * @param initialCapacity an <code>int</code> value
     */
    public TIntHash(int initialCapacity) {
        super(initialCapacity);
        _hashingStrategy = this;
    }

    /**
     * Creates a new <code>TIntHash</code> instance with a prime
     * value at or near the specified capacity and load factor.
     *
     * @param initialCapacity used to find a prime capacity for the table.
     * @param loadFactor used to calculate the threshold over which
     * rehashing takes place.
     */
    public TIntHash(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
        _hashingStrategy = this;
    }

    /**
     * Creates a new <code>TIntHash</code> instance with the default
     * capacity and load factor.
     * @param strategy used to compute hash codes and to compare keys.
     */
    public TIntHash(TIntHashingStrategy strategy) {
        _hashingStrategy = strategy;
    }

    /**
     * Creates a new <code>TIntHash</code> instance whose capacity
     * is the next highest prime above <tt>initialCapacity + 1</tt>
     * unless that value is already prime.
     *
     * @param initialCapacity an <code>int</code> value
     * @param strategy used to compute hash codes and to compare keys.
     */
    public TIntHash(int initialCapacity, TIntHashingStrategy strategy) {
        super(initialCapacity);
        _hashingStrategy = strategy;
    }

    /**
     * Creates a new <code>TIntHash</code> instance with a prime
     * value at or near the specified capacity and load factor.
     *
     * @param initialCapacity used to find a prime capacity for the table.
     * @param loadFactor used to calculate the threshold over which
     * rehashing takes place.
     * @param strategy used to compute hash codes and to compare keys.
     */
    public TIntHash(int initialCapacity, float loadFactor, TIntHashingStrategy strategy) {
        super(initialCapacity, loadFactor);
        _hashingStrategy = strategy;
    }

    /**
     * @return a deep clone of this collection
     */
    @Override
    public Object clone() {
        TIntHash h = (TIntHash)super.clone();
        h._set = _set == null ? null : _set.clone();
        return h;
    }

    /**
     * initializes the hash table to a prime capacity which is at least
     * <tt>initialCapacity + 1</tt>.
     *
     * @param initialCapacity an <code>int</code> value
     * @return the actual capacity chosen
     */
    @Override
    protected int setUp(int initialCapacity) {
        int capacity = super.setUp(initialCapacity);
        _set = initialCapacity == JUST_CREATED_CAPACITY ? null : new int[capacity];
        return capacity;
    }

    /**
     * Searches the set for <tt>val</tt>
     *
     * @param val an <code>int</code> value
     * @return a <code>boolean</code> value
     */
    public boolean contains(int val) {
        return index(val) >= 0;
    }

    /**
     * Executes <tt>procedure</tt> for each element in the set.
     *
     * @param procedure a <code>TObjectProcedure</code> value
     * @return false if the loop over the set terminated because
     * the procedure returned false for some value.
     */
    public boolean forEach(TIntProcedure procedure) {
        byte[] states = _states;
        int[] set = _set;
        if (states != null) {
            for (int i = states.length; i-- > 0;) {
                if (states[i] == FULL && ! procedure.execute(set[i])) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Releases the element currently stored at <tt>index</tt>.
     *
     * @param index an <code>int</code> value
     */
    @Override
    protected void removeAt(int index) {
        _set[index] = (int)0;
        super.removeAt(index);
    }

    /**
     * Locates the index of <tt>val</tt>.
     *
     * @param val an <code>int</code> value
     * @return the index of <tt>val</tt> or -1 if it isn't in the set.
     */
    protected int index(int val) {
        byte[] states = _states;
        if (states == null) return -1;
        int[] set = _set;
        int length = states.length;
        int hash = _hashingStrategy.computeHashCode(val) & 0x7fffffff;
        int index = hash % length;

        if (states[index] != FREE &&
            (states[index] == REMOVED || set[index] != val)) {
            // see Knuth, p. 529
            int probe = 1 + (hash % (length - 2));

            do {
                index -= probe;
                if (index < 0) {
                    index += length;
                }
            } while (states[index] != FREE &&
                     (states[index] == REMOVED || set[index] != val));
        }

        return states[index] == FREE ? -1 : index;
    }

    /**
     * Locates the index at which <tt>val</tt> can be inserted.  if
     * there is already a value equal()ing <tt>val</tt> in the set,
     * returns that value as a negative integer.
     *
     * @param val an <code>int</code> value
     * @return an <code>int</code> value
     */
    protected int insertionIndex(int val) {
        if (_set == null) {
            setUp((int) (DEFAULT_INITIAL_CAPACITY / DEFAULT_LOAD_FACTOR + 1));
        }
        byte[] states = _states;
        int[] set = _set;
        int length = states.length;
        int hash = _hashingStrategy.computeHashCode(val) & 0x7fffffff;
        int index = hash % length;

        if (states[index] == FREE) {
            return index;       // empty, all done
        } else if (states[index] == FULL && set[index] == val) {
            return -index -1;   // already stored
        } else {                // already FULL or REMOVED, must probe
            // compute the double hash
            int probe = 1 + (hash % (length - 2));
            // starting at the natural offset, probe until we find an
            // offset that isn't full.
            do {
                index -= probe;
                if (index < 0) {
                    index += length;
                }
            } while (states[index] == FULL && set[index] != val);

            // if the index we found was removed: continue probing until we
            // locate a free location or an element which equal()s the
            // one we have.
            if (states[index] == REMOVED) {
                int firstRemoved = index;
                while (states[index] != FREE &&
                       (states[index] == REMOVED || set[index] != val)) {
                    index -= probe;
                    if (index < 0) {
                        index += length;
                    }
                }
                return states[index] == FULL ? -index -1 : firstRemoved;
            }
            // if it's full, the key is already stored
            return states[index] == FULL ? -index -1 : index;
        }
    }

    /**
     * Default implementation of TIntHashingStrategy:
     * delegates hashing to HashFunctions.hash(int).
     *
     * @param val the value to hash
     * @return the hash code.
     */
    public final int computeHashCode(int val) {
        return HashFunctions.hash(val);
    }
} // TIntHash