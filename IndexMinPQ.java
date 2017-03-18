/******************************************************************************
 *  Compilation:  javac IndexMinPQ.java
 *  Execution:    java IndexMinPQ
 *  Dependencies: StdOut.java
 *
 *  Minimum-oriented indexed PQ implementation using a binary heap.
 *
 ******************************************************************************/

/**

    Changed increaseCar and decreaseCar methods to take an extra parameter called mode to indicate whether we're dealing with a PQ for cost or for mileage
        In CarTracker.java, we create two PQ's that hold identical cars, but are organized based on either price OR mileage, respectively.
        This allows for simpler minPrice or minMileage retrieval since we don't have to account for both in one PQ.

*/

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *  The {@code IndexMinPQ} class represents an indexed priority queue of generic keys.
 *  It supports the usual <em>insert</em> and <em>delete-the-minimum</em>
 *  operations, along with <em>delete</em> and <em>change-the-key</em> 
 *  methods. In order to let the client refer to keys on the priority queue,
 *  an integer between {@code 0} and {@code maxN - 1}
 *  is associated with each key—the client uses this integer to specify
 *  which key to delete or change.
 *  It also supports methods for peeking at the minimum key,
 *  testing if the priority queue is empty, and iterating through
 *  the keys.
 *  <p>
 *  This implementation uses a binary heap along with an array to associate
 *  keys with integers in the given range.
 *  The <em>insert</em>, <em>delete-the-minimum</em>, <em>delete</em>,
 *  <em>change-key</em>, <em>decrease-key</em>, and <em>increase-key</em>
 *  operations take logarithmic time.
 *  The <em>is-empty</em>, <em>size</em>, <em>min-index</em>, <em>min-key</em>,
 *  and <em>key-of</em> operations take constant time.
 *  Construction takes time proportional to the specified capacity.
 *  <p>
 *  For additional documentation, see <a href="http://algs4.cs.princeton.edu/24pq">Section 2.4</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 *
 *  @param <Car> object contained within this priority queue
 */
public class IndexMinPQ implements Iterable<Integer> {

    private int n;           // number of elements on PQ
    private int maxN;        // maximum number of elements on PQ
    public int[] pq;        // binary heap using 1-based indexing
    private int[] qp;        // inverse of pq - qp[pq[i]] = pq[qp[i]] = i
    private char mode;
    private Car[] cars;      // cars[i] = priority of i

    /**
     * Initializes an empty indexed priority queue with indices between {@code 0}
     * and {@code maxN - 1}.
     * @param  maxN the keys on this priority queue are index from {@code 0}
     * to  {@code maxN - 1}
     * @throws IllegalArgumentException if {@code maxN < 0}
     */
    public IndexMinPQ (int maxN, char mode) {
        
        this.mode = mode;   // set mode for priority queue (either dealing with cost or mileage)
        
        if (maxN < 0) throw new IllegalArgumentException();
        this.maxN = maxN;
        n = 0;
        cars = new Car[maxN + 1];           // make this of length maxN??
        pq   = new int[maxN + 1];
        qp   = new int[maxN + 1];           // make this of length maxN??
        for (int i = 0; i <= maxN; i++)
            qp[i] = -1;
    }

    /**
     * Returns true if this priority queue is empty.
     *
     * @return {@code true} if this priority queue is empty;
     *         {@code false} otherwise
     */
    public boolean isEmpty () {
        return n == 0;
    }

    /**
     * Is {@code i} an index on this priority queue?
     *
     * @param  i an index
     * @return {@code true} if {@code i} is an index on this priority queue;
     *         {@code false} otherwise
     * @throws IndexOutOfBoundsException unless {@code 0 <= i < maxN}
     */
    public boolean contains (int i) {
        if (i < 0 || i >= maxN) throw new IndexOutOfBoundsException();
        return qp[i] != -1;
    }

    /**
     * Returns the number of keys on this priority queue.
     *
     * @return the number of keys on this priority queue
     */
    public int size () {
        return n;
    }

    /**
     * Associates key with index {@code i}.
     *
     * @param  i an index
     * @param  key the key to associate with index {@code i}
     * @throws IndexOutOfBoundsException unless {@code 0 <= i < maxN}
     * @throws IllegalArgumentException if there already is an item associated
     *         with index {@code i}
     */
    public void insert (int i, Car newCar) {
        if (i < 0 || i >= maxN) throw new IndexOutOfBoundsException();
        if (contains(i)) throw new IllegalArgumentException("index is already in the priority queue");
        n++;
        qp[i] = n;
        pq[n] = i;
        cars[i] = newCar;
        swim(n);
    }

    /**
     * Returns an index associated with a minimum key.
     *
     * @return an index associated with a minimum key
     * @throws NoSuchElementException if this priority queue is empty
     */
    public int minIndex () {
        if (n == 0) throw new NoSuchElementException("Priority queue underflow");
        return pq[1];
    }

    /**
     * Returns a minimum key.
     *
     * @return a minimum key
     * @throws NoSuchElementException if this priority queue is empty
     */
    public Car minCar () {
        if (n == 0) throw new NoSuchElementException("Priority queue underflow");
        return cars[pq[1]];
    }

    /**
     * Removes a minimum key and returns its associated index.
     * @return an index associated with a minimum key
     * @throws NoSuchElementException if this priority queue is empty
     */
    public int delMin () {
        if (n == 0) throw new NoSuchElementException("Priority queue underflow");
        int min = pq[1];
        exch(1, n--);
        sink(1);
        assert min == pq[n+1];
        qp[min] = -1;        // delete
        cars[min] = null;    // to help with garbage collection
        pq[n+1] = -1;        // not needed
        return min;
    }

    /**
     * Returns the key associated with index {@code i}.
     *
     * @param  i the index of the key to return
     * @return the key associated with index {@code i}
     * @throws IndexOutOfBoundsException unless {@code 0 <= i < maxN}
     * @throws NoSuchElementException no key is associated with index {@code i}
     */
    public Car carAt (int i) {
        if (i < 0 || i >= maxN) throw new IndexOutOfBoundsException();
        if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
        else return cars[i];
    }

    /**
     * Change the key associated with index {@code i} to the specified value.
     *
     * @param  i the index of the key to change
     * @param  key change the key associated with index {@code i} to this key
     * @throws IndexOutOfBoundsException unless {@code 0 <= i < maxN}
     * @throws NoSuchElementException no key is associated with index {@code i}
     */
    public void changeCar (int i, Car car) {
        if (i < 0 || i >= maxN) throw new IndexOutOfBoundsException();
        if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
        cars[i] = car;
        swim(qp[i]);
        sink(qp[i]);
    }

    /**
     * Change the key associated with index {@code i} to the specified value.
     *
     * @param  i the index of the key to change
     * @param  key change the key associated with index {@code i} to this key
     * @throws IndexOutOfBoundsException unless {@code 0 <= i < maxN}
     * @deprecated Replaced by {@code changeKey(int, Key)}.
     */
    @Deprecated
    public void change (int i, Car car) {
        changeCar(i, car);
    }

    /**
     * Decrease the key associated with index {@code i} to the specified value.
     *
     * @param  i the index of the key to decrease
     * @param  j the price/mileage to decrease
     * @throws IndexOutOfBoundsException unless {@code 0 <= i < maxN}
     * @throws IllegalArgumentException if {@code key >= keyOf(i)}
     * @throws NoSuchElementException no key is associated with index {@code i}
     */
    public void decrease (int i, int j) {
        if (i < 0 || i >= maxN) throw new IndexOutOfBoundsException();
        if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");

        if (mode == 'p') {                                  // If dealing with price...
            if (cars[i].getPrice() < j)         // Make sure new price is not smaller than old price.
                throw new IllegalArgumentException("Calling decrease() with given argument would not strictly decrease the cost");
            cars[i].setPrice(j);
        } else if (mode == 'm') {                           // Else if dealing with mileage...
            if (cars[i].getMileage() < j)   // Make sure new mileage is not smaller than old mileage.
                throw new IllegalArgumentException("Calling decrease() with given argument would not strictly decrease the mileage");
            cars[i].setMileage(j);
        }  
        swim(qp[i]);
    }

    /**
     * Increase the key associated with index {@code i} to the specified value.
     *
     * @param  i the index of the key to increase
     * @param  j the price/mileage to increase
     * @throws IndexOutOfBoundsException unless {@code 0 <= i < maxN}
     * @throws IllegalArgumentException if {@code key <= keyOf(i)}
     * @throws NoSuchElementException no key is associated with index {@code i}
     */
    public void increase (int i, int j) {
        if (i < 0 || i >= maxN) throw new IndexOutOfBoundsException();
        if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
        
        if (mode == 'p') {                       // If dealing with price...
            if (cars[i].getPrice() > j)          // Make sure new price is not smaller than price cost.
                throw new IllegalArgumentException("Calling increase() with given argument would not strictly increase the cost");
            cars[i].setPrice(j);
        } else if (mode == 'm') {            // Else if dealing with mileage...
            if (cars[i].getMileage() > j)   // Make sure new mileage is not smaller than old mileage.
                throw new IllegalArgumentException("Calling increase() with given argument would not strictly increase the mileage");
            cars[i].setMileage(j);
        }
        sink(qp[i]);
    }

    /**
     * Remove the key associated with index {@code i}.
     *
     * @param  i the index of the key to remove
     * @throws IndexOutOfBoundsException unless {@code 0 <= i < maxN}
     * @throws NoSuchElementException no key is associated with index {@code i}
     */
    public void delete(int i) {
        if (i < 0 || i >= maxN) throw new IndexOutOfBoundsException();
        if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
        int index = qp[i];
        exch(index, n--);
        swim(index);
        sink(index);
        cars[i] = null;
        qp[i] = -1;
    }


   /***************************************************************************
    * General helper functions.
    ***************************************************************************/

    private boolean greater(int i, int j) {
        if(mode == 'p') return cars[pq[i]].getPrice() > cars[pq[j]].getPrice();
        if(mode == 'm') return cars[pq[i]].getMileage() > cars[pq[j]].getMileage();
        else return false;
    }

    private void exch(int i, int j) {
        int swap = pq[i];
        pq[i] = pq[j];
        pq[j] = swap;
        qp[pq[i]] = i;
        qp[pq[j]] = j;
    }


   /***************************************************************************
    * Heap helper functions.
    ***************************************************************************/
    private void swim(int k) {
        while (k > 1 && greater(k/2, k)) {
            exch(k, k/2);
            k = k/2;
        }
    }

    private void sink(int k) {
        while (2*k <= n) {
            int j = 2*k;
            if (j < n && greater(j, j+1)) j++;
            if (!greater(k, j)) break;
            exch(k, j);
            k = j;
        }
    }


   /***************************************************************************
    * Iterators.
    ***************************************************************************/

    /**
     * Returns an iterator that iterates over the cars on the
     * priority queue in ascending order.
     * The iterator doesn't implement {@code remove()} since it's optional.
     *
     * @return an iterator that iterates over the cars in ascending order
     */
    public Iterator<Integer> iterator() { return new HeapIterator(); }

    private class HeapIterator implements Iterator<Integer> {
        // create a new pq
        private IndexMinPQ copy;

        // add all elements to copy of heap
        // takes linear time since already in heap order so no cars move
        public HeapIterator() {
            copy = new IndexMinPQ(pq.length - 1,' ');
            for (int i = 1; i <= n; i++)
                copy.insert(pq[i], cars[pq[i]]);
        }

        public boolean hasNext()  { return !copy.isEmpty();                    }
        public void remove()      { throw new UnsupportedOperationException(); }

        public Integer next() {
            if (!hasNext()) throw new NoSuchElementException();
            return copy.delMin();
        }
    }


    /*
     * Unit tests the {@code IndexMinPQ} data type.
     *
     * @param args the command-line arguments
     
    public static void main(String[] args) {
        // insert a bunch of strings
        String[] strings = { "it", "was", "the", "best", "of", "times", "it", "was", "the", "worst" };

        IndexMinPQ<String> pq = new IndexMinPQ<String>(strings.length);
        for (int i = 0; i < strings.length; i++) {
            pq.insert(i, strings[i]);
        }

        // delete and print each key
        while (!pq.isEmpty()) {
            int i = pq.delMin();
            StdOut.println(i + " " + strings[i]);
        }
        StdOut.println();

        // reinsert the same strings
        for (int i = 0; i < strings.length; i++) {
            pq.insert(i, strings[i]);
        }

        // print each key using the iterator
        for (int i : pq) {
            StdOut.println(i + " " + strings[i]);
        }
        while (!pq.isEmpty()) {
            pq.delMin();
        }

    }*/
}