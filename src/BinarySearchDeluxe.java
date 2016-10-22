import java.util.Comparator;

public class BinarySearchDeluxe 
{    
	public static long numCompare = 0; 
    // Returns the index of the first key in a[] that equals the search key, or -1 if no such key.
    public static <Key> int firstIndexOf(Key[] a, Key key, Comparator<Key> comparator)
    {
        if (a == null || key == null || comparator == null)
            throw new java.lang.NullPointerException();
        
        int lo = 0;
        int hi = a.length - 1;
        while (lo <= hi)
        {	
            int mid = lo + ( hi - lo ) / 2;
//            System.out.println("fisrt bs mid: "+ mid + "\thi: " + hi + "\tlo: " + lo);
            if (comparator.compare(key, a[mid]) < 0) 
                hi = mid - 1;
            else if (comparator.compare(key, a[mid]) > 0)
                lo = mid + 1;
            else
            {
                for (int i = lo; i <= mid; i++)
                {
                	if (comparator.compare(a[i], a[mid]) == 0){
                		numCompare++;
//                		System.out.println("first\t" + a[i] + "\tkey\t" + key);
                		return i;
                	}
                }
            }
            numCompare++;
//            System.out.println("fisrt bs mid: "+ mid + "\thi: " + hi + "\tlo: " + lo);
        }
        return -1; 
    }
    
    // Returns the index of the last key in a[] that equals the search key, or -1 if no such key.
    public static <Key> int lastIndexOf(Key[] a, Key key, Comparator<Key> comparator)
    {
        if (a == null || key == null || comparator == null)
            throw new java.lang.NullPointerException();
        
        int lo = 0;
        int hi = a.length - 1;
        while (lo <= hi)
        {
            int mid = lo + (hi - lo) / 2;
//            System.out.println("last bs\tmid: "+ mid + "\thi: " + hi + "\tlo: " + lo);
            if (comparator.compare(key, a[mid]) < 0) 
                hi = mid - 1;
            else if (comparator.compare(key, a[mid]) > 0)
                lo = mid + 1;
            else
            {
                for (int i = hi; mid <= i ; i--)
                {
                    if (comparator.compare(a[i], a[mid]) == 0){
                    	numCompare++;
//                    	System.out.println("last:" + a[i] + "\tkey: " + key); 
                    	return i;
                    }
                }
            }
            numCompare++;
        }
        return -1; 
    }
}

