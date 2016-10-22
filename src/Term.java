import java.util.Comparator;
import java.lang.String;

public class Term implements Comparable<Term> 
{
    
    private String query;
    private final long weight;
    
    // Initializes a term with the given query string and weight.
    public Term(String query, long weight) {
        if (query == null)
            throw new java.lang.NullPointerException();
        if (weight < 0)
            throw new java.lang.IllegalArgumentException();
        this.query = query;
        this.weight = weight;
    }
    
    // Compares the two terms in descending order by weight.
    public static Comparator<Term> byReverseWeightOrder() 
    {
        return new ReverseWeightOrder();
    }
    
    private static class ReverseWeightOrder implements Comparator<Term> 
    {
        
        public int compare(Term a, Term b) 
        {
            if (a.weight < b.weight) return 1;
            if (b.weight < a.weight) return -1;
            else return 0;
        }
    }
    
    // Compares the two terms in lexicographic order but using only the first r characters of each query.
    public static Comparator<Term> byPrefixOrder(int r) 
    {
        if (r < 0)
            throw new java.lang.IllegalArgumentException();
        return new PrefixOrder(r);
    }
    
    private static class PrefixOrder implements Comparator<Term> 
    {
        
        private int r;
        
        public PrefixOrder(int r) {
            this.r = r;
        }
        
        public int compare(Term a, Term b) 
        {
            String asub, bsub;
            if (a.query.length() < r) asub = a.query;
            else asub = a.query.substring(0, r);
            if (b.query.length() < r) bsub = b.query;
            else bsub = b.query.substring(0, r);
            Term a1 = new Term(asub, a.weight);
            Term b1 = new Term(bsub, b.weight);
            return a1.compareTo(b1);
        }
    }
    
    // Compares the two terms in lexicographic order by query.
    public int compareTo(Term that) 
    {
        return this.query.compareTo(that.query);
    }
    
    // Returns a string representation of this term in the following format:
    // the weight, followed by a tab, followed by the query.
    public String toString() {
        return weight + "\t" + query;
    }
}