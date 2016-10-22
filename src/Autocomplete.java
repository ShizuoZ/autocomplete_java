import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.SortedMap;

public class Autocomplete 
{
    private final Term[] term;
    private Term[] sorted;
    public static long numCompare = 0;
    
    // Initializes the data structure from the given array of terms.
    public Autocomplete(Term[] terms)
    {
        if (terms == null)
            throw new java.lang.NullPointerException();
        
        for (int i = 0; i < terms.length; i++)
        {
            if (terms[i] == null) 
                throw new java.lang.NullPointerException();
        }
        term = terms;
        sorted = terms;
        
        MergeX.sort(sorted); //
        numCompare += MergeX.numCompare;
    }
    
    // Returns all terms that start with the given prefix, in descending order of weight.
    public Term[] allMatches(String prefix)
    {
        if (prefix == null)
            throw new java.lang.NullPointerException();
        MergeX.sort(sorted, Term.byPrefixOrder(prefix.length()));
//        for(int i = 0; i<sorted.length;i++){System.out.println(sorted[i]);}
        Term pref = new Term(prefix, 1);
        int first = BinarySearchDeluxe.firstIndexOf(sorted, pref, Term.byPrefixOrder(prefix.length()));
        int last = BinarySearchDeluxe.lastIndexOf(sorted, pref, Term.byPrefixOrder(prefix.length()));
        numCompare += BinarySearchDeluxe.numCompare;
//        System.out.println("\nfirst: " + first + "\nlast: " + last);
        Term[] match = new Term[numberOfMatches(prefix)];
        for (int i = first; i <= last; i++)
        {
            match[i - first] = term[i];
        }
        
        MergeX.sort(match, Term.byReverseWeightOrder());
        numCompare += MergeX.numCompare;
        return match;
    }
    
    // Returns the number of terms that start with the given prefix.
    public int numberOfMatches(String prefix)
    {
        if (prefix == null)
            throw new java.lang.NullPointerException();
        MergeX.sort(sorted, Term.byPrefixOrder(prefix.length()));
        Term pref = new Term(prefix, 1);
        int first = BinarySearchDeluxe.firstIndexOf(sorted, pref, Term.byPrefixOrder(prefix.length()));
        int last = BinarySearchDeluxe.lastIndexOf(sorted, pref, Term.byPrefixOrder(prefix.length()));
        return (last - first + 1);
    }
    
    public static void main(String[] args) throws IOException 
    {
        // read in the terms from a file
//        String filename = args[0];
    	String filename = "src/alexa.txt";
    	InputStreamReader reader = new InputStreamReader(new FileInputStream(filename));
    	BufferedReader in = new BufferedReader(reader); 
    	Scanner sc = new Scanner(in);
        int N = sc.nextInt();
//        N = 512000;
        Term[] terms = new Term[N];
        for (int i = 0; i < N; i++) 
        {
            long weight = sc.nextLong();           // read the next weight
            										// scan past the tab
            String query = sc.next();          // read the next query
            terms[i] = new Term(query, weight);    // construct the term
//            System.out.println(terms[i]); 
        }
        
// read in queries from standard input and print out the top k matching terms
//      int k = Integer.parseInt(args[1]);
		int k = 6;
		int mb = 1024 * 1024;
		Runtime instance = Runtime.getRuntime();
		System.out.println("Number of terms: " + N);
        Autocomplete autocomplete = new Autocomplete(terms);
        sc.close();
        long runtime = numCompare;
        System.out.println("construction time: " + runtime);
        System.out.println("Used memory: " + (instance.totalMemory() - instance.freeMemory()) / mb + "MB");
        System.out.println("Please enter a prefix: ");
        sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            String prefix = sc.nextLine();
            Term[] results = autocomplete.allMatches(prefix);
            for (int i = 0; i < Math.min(k, results.length); i++) 	//Math.min(k, results.length)
                System.out.println(results[i]);
            System.out.println("Used memory: " + (instance.totalMemory() - instance.freeMemory()) / mb + "MB");
            System.out.println("search time: " + (numCompare - runtime));
            runtime = numCompare;
        }
        System.out.println("Used memory: " + (instance.totalMemory() - instance.freeMemory()) / mb + "MB");
        System.out.println("number of compares: " + numCompare);
        sc.close();
    }   
}
