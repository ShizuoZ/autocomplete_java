import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Queue;
import java.util.Scanner;

public class TrieST<Value> {
	public static long numCompare = 0;
	
	private static final int R = 256;        // extended ASCII


    private Node root;      // root of trie
    private int N;          // number of keys in trie

    // R-way trie node
    private static class Node {
        private Object val;
        private Node[] next = new Node[R];
    }
    
    public TrieST() {
    }

    public Value get(String key) {
        Node x = get(root, key, 0);
        if (x == null) return null;
        return (Value) x.val;
    }

    public boolean contains(String key) {
        return get(key) != null;
    }

    private Node get(Node x, String key, int d) {
        if (x == null) return null;
        if (d == key.length()) return x;
        char c = key.charAt(d);
        numCompare++;
        return get(x.next[c], key, d+1);
    }

    public void put(String key, Value val) {
        if (val == null) delete(key);
        else root = put(root, key, val, 0);
    }

    private Node put(Node x, String key, Value val, int d) {
        if (x == null) x = new Node();
        if (d == key.length()) {
            if (x.val == null) N++;
            x.val = val;
            return x;
        }
        char c = key.charAt(d);
        x.next[c] = put(x.next[c], key, val, d+1);
        numCompare++;
        return x;
    }

    public int size() {
        return N;
    }

    public boolean isEmpty() {
        return size() == 0;
    }
    
    public Iterable<String> keys() {
        return keysWithPrefix("");
    }

    public Iterable<String> keysWithPrefix(String prefix) {
        ArrayList<String> results = new ArrayList<String>();
        Node x = get(root, prefix, 0);
        collect(x, new StringBuilder(prefix), results);
        return results;
    }

    private void collect(Node x, StringBuilder prefix, ArrayList<String> results) {
        if (x == null) return;
        if (x.val != null) results.add(prefix.toString());
        for (char c = 0; c < R; c++) {
            prefix.append(c);
            collect(x.next[c], prefix, results);
            prefix.deleteCharAt(prefix.length() - 1);
        }
        numCompare++;
    }

    public Iterable<String> keysThatMatch(String pattern) {
        ArrayList<String> results = new ArrayList<String>();
        collect(root, new StringBuilder(), pattern, results);
        return results;
    }

    private void collect(Node x, StringBuilder prefix, String pattern, ArrayList<String> results) {
        if (x == null) return;
        int d = prefix.length();
        if (d == pattern.length() && x.val != null)
            results.add(prefix.toString());
        if (d == pattern.length())
            return;
        char c = pattern.charAt(d);
        if (c == '.') {
            for (char ch = 0; ch < R; ch++) {
                prefix.append(ch);
                collect(x.next[ch], prefix, pattern, results);
                prefix.deleteCharAt(prefix.length() - 1);
            }
        }
        else {
            prefix.append(c);
            collect(x.next[c], prefix, pattern, results);
            prefix.deleteCharAt(prefix.length() - 1);
        }
    }

    public String longestPrefixOf(String query) {
        int length = longestPrefixOf(root, query, 0, -1);
        if (length == -1) return null;
        else return query.substring(0, length);
    }
    
    private int longestPrefixOf(Node x, String query, int d, int length) {
        if (x == null) return length;
        if (x.val != null) length = d;
        if (d == query.length()) return length;
        char c = query.charAt(d);
        return longestPrefixOf(x.next[c], query, d+1, length);
    }

    public void delete(String key) {
        root = delete(root, key, 0);
    }

    private Node delete(Node x, String key, int d) {
        if (x == null) return null;
        if (d == key.length()) {
            if (x.val != null) N--;
            x.val = null;
        }
        else {
            char c = key.charAt(d);
            x.next[c] = delete(x.next[c], key, d+1);
        }

        // remove subtrie rooted at x if it is completely empty
        if (x.val != null) return x;
        for (int c = 0; c < R; c++)
            if (x.next[c] != null)
                return x;
        return null;
    }
    
    public int height(){
    	return height(root);
    }
    
    public int height(Node x){
    	int h = -1;
    	for(String s : keys()){
    		if(s.length() > h) h = s.length();
    	}
		return h;
    }

    public static void main(String[] args) throws FileNotFoundException {

        // build symbol table from standard input
        TrieST<Long> st = new TrieST<Long>();
        String filename = "src/alexa.txt";
    	InputStreamReader reader = new InputStreamReader(new FileInputStream(filename));
    	BufferedReader in = new BufferedReader(reader); 
    	Scanner sc = new Scanner(in);
        int N = sc.nextInt();
        N = 100000;
        int k = 6;
        int mb = 1024 * 1024;
        Runtime instance = Runtime.getRuntime();
        for (int i = 0; i < N; i++) 
        {
            long weight = sc.nextLong();           // read the next weight
                                                   // scan past the tab
            String query = sc.next();          // read the next query
            st.put(query, weight);    // construct the term
//          System.out.println("weight: " + weight + "\tstring: " + query);
        }
        System.out.println("Number of compares: " + numCompare);
        System.out.println("Number of terms: " + st.N);
        System.out.println("Tree size: " + st.size());
//        System.out.println("Height: " + st.height());
        System.out.println("Used memory: " + (instance.totalMemory() - instance.freeMemory()) / mb + "MB");
// read in queries from standard input and print out the top k matching terms
//      int k = Integer.parseInt(args[1]);
        sc.close();
        long runtime = numCompare;
        System.out.println("construction time: " + runtime);
        System.out.println("Please enter a prefix: ");
        sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
	        String prefix = sc.nextLine();
	        Term[] result = new Term[0];
	        for(String s : st.keysWithPrefix(prefix)){
	          	result = Arrays.copyOf(result, result.length + 1);
	        	result[result.length-1] = new Term(s, st.get(s));
	        }
	        MergeX.sort(result, Term.byReverseWeightOrder());
	        numCompare += MergeX.numCompare;
	        for (int i = 0; i < Math.min(k, result.length); i++) 	//Math.min(k, results.length)
	            System.out.println(result[i]);
	        System.out.println("Used memory: " + (instance.totalMemory() - instance.freeMemory()) / mb + "MB");
	        System.out.println("search time: " + (numCompare - runtime));
            runtime = numCompare;
        }
        sc.close();
    }
}
