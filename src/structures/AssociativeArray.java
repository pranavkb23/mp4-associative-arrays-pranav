package structures;

import static java.lang.reflect.Array.newInstance;

/**
 * A basic implementation of Associative Arrays with keys of type K
 * and values of type V. Associative Arrays store key/value pairs
 * and permit you to look up values by key.
 *
 * @author Pranav K Bhandari
 * @author Samuel A. Rebelsky
 */
public class AssociativeArray<K, V> {
  // +-----------+---------------------------------------------------
  // | Constants |
  // +-----------+

  /**
   * The default capacity of the initial array.
   */
  static final int DEFAULT_CAPACITY = 16;

  // +--------+------------------------------------------------------
  // | Fields |
  // +--------+

  /**
   * The size of the associative array (the number of key/value pairs).
   */
  int size;

  /**
   * The capacity of the associative array (the number of key/value pairs the entire 
   * AssociativeArray could hold without expanding).
   */
  int capacity = DEFAULT_CAPACITY;

  /**
   * The array of key/value pairs.
   */
  KVPair<K, V> pairs[];

  // +--------------+------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Create a new, empty associative array.
   */
  @SuppressWarnings({ "unchecked" })
  public AssociativeArray() {
    // Creating new arrays is sometimes a PITN.
    this.pairs = (KVPair<K, V>[]) newInstance((new KVPair<K, V>()).getClass(),
        DEFAULT_CAPACITY);
    this.size = 0;
  } // AssociativeArray()

  // +------------------+--------------------------------------------
  // | Standard Methods |
  // +------------------+

  /**
   * Create a copy of this AssociativeArray.
   */
  public AssociativeArray<K, V> clone() {
    AssociativeArray<K, V> newArr = new AssociativeArray<K, V>();

    for (int i = 0; i < this.size; i++) {
      if (newArr.size >= newArr.capacity) { 
        newArr.expand();
        newArr.capacity *= 2;
      }

      newArr.pairs[i] = new KVPair<K, V>(this.pairs[i].key, this.pairs[i].value);
      newArr.size++;
    }
    return newArr;
  } // clone()

  /**
   * Convert the array to a string.
   */
  public String toString() {
    String stringArray = this.pairs[0].key + ": " + this.pairs[0].value;

    if (this.size == 0) {
      return "{}";
    }

    for (int i = 1; i < this.size; i++) {
      stringArray = stringArray + ", " + this.pairs[i].key + ": " + this.pairs[i].value;
    }

    return "{ " + stringArray + " }"; 
  } // toString()

  // +----------------+----------------------------------------------
  // | Public Methods |
  // +----------------+

  /**
   * Set the value associated with key to value. Future calls to
   * get(key) will return value.
   */
  public void set(K key, V value) throws NullKeyException {
    if (key == null) // if the given key is null
      throw new NullKeyException();

    if (this.size >= this.capacity) { // if the array needs to be expanded
      this.expand();
      this.capacity *= 2;
    }

    try {
      int index = find(key);
      this.pairs[index].value = value;
    }
    catch (KeyNotFoundException e) { 
      this.pairs[size] = new KVPair<K, V>(key, value);
      size++;
    }
  } // set(K,V)

  /**
   * Get the value associated with key.
   *
   * @throws KeyNotFoundException
   *                              when the key is null or does not 
   *                              appear in the associative array.
   */
  public V get(K key) throws KeyNotFoundException {
    int index = find(key);
    return this.pairs[index].value; 
  } // get(K)

  /**
   * Determine if key appears in the associative array. Should
   * return false for the null key.
   */
  public boolean hasKey(K key) {
    try {
      find(key);
      return true;
    }
    catch (KeyNotFoundException e) {
      return false;
    }
  } // hasKey(K)

  /**
   * Remove the key/value pair associated with a key. Future calls
   * to get(key) will throw an exception. If the key does not appear
   * in the associative array, does nothing.
   */
  public void remove(K key) {
    try {
      int index = find(key);
      this.pairs[index] = this.pairs[size - 1];
      this.pairs[size-- - 1] = new KVPair<K, V>();
    }
    catch (KeyNotFoundException e) {}
  } // remove(K)

  /**
   * Determine how many key/value pairs are in the associative array.
   */
  public int size() {
    return this.size;
  } // size()

  // +-----------------+---------------------------------------------
  // | Private Methods |
  // +-----------------+

  /**
   * Expand the underlying array.
   */
  void expand() {
    this.pairs = java.util.Arrays.copyOf(this.pairs, this.pairs.length * 2);
  } // expand()

  /**
   * Find the index of the first entry in `pairs` that contains key.
   * If no such entry is found, throws an exception.
   */
  int find(K key) throws KeyNotFoundException {
    for (int i = 0; i < this.size; i++) {
      if (this.pairs[i].key.equals(key))
        return i;
    }
    throw new KeyNotFoundException();   
  } // find(K)

} // class AssociativeArray
