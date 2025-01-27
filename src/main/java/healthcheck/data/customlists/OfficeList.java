package healthcheck.data.customlists;

import healthcheck.data.Office;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

public class OfficeList implements Iterable<Office>, Iterator<Office> {
    private ArrayList<Office> list;
    private int currentIndex; // Track the current position

    public OfficeList(ArrayList<Office> offices) {
        this.list = offices;
        sort();
        this.currentIndex = 0; // Initialize the index
    }

    public OfficeList(int initialCapacity) {
        this.list = new ArrayList<>(initialCapacity);
        this.currentIndex = 0; // Initialize the index
    }

    public OfficeList() {
        this.list = new ArrayList<>();
        this.currentIndex = 0; // Initialize the index
    }

    public int size() {
        return list.size();
    }

    public boolean contains (String office) {
        for (Office i : list) {
            if (i.getOfficeCode().equals(office)) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Office> getList() {
        return list;
    }

    public boolean contains (Office office) {
        return contains(office.getOfficeCode());
    }

    public boolean replace(Office office) {
        String officeCode = office.getOfficeCode();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getOfficeCode().equals(officeCode)) {
                list.set(i, office); // Replace the office
                return true;
            }
        }
        return false;
    }

    public void add (Office office) {
        list.add(office);
        sort();
    }

    public void addAll (ArrayList<Office> offices) {
        list.addAll(offices);
        sort();
    }

    public boolean remove (Office office) {
        String officeCode = office.getOfficeCode();
        boolean officeFound = false;
        int index = 0;
        for (Office i : list) {
            if (i.getOfficeCode().equals(officeCode)) {
                officeFound = true;
                break;
            }
            index++;
        }
        if (officeFound) {
            list.remove(index);
            return true;
        } else {
            return false;
        }
    }

    private void sort() {
        list.sort(Comparator.comparing(Office::getOfficeCode));
    }


    @Override
    public boolean hasNext() {
        return currentIndex < list.size(); // Check if there are more elements
    }

    @Override
    public Office next() {
        if (!hasNext()) {
            throw new IllegalStateException("No more elements to iterate.");
        }
        return list.get(currentIndex++); // Return the current element and advance the index
    }

    // Optional: Reset the iterator to allow re-iteration
    public void resetIterator() {
        this.currentIndex = 0;
    }

    @Override
    public Iterator<Office> iterator() {
        // Reset index when a new iterator is requested
        this.currentIndex = 0;
        return this;
    }


}
