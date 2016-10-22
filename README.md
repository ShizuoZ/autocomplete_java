# autocomplete_java

In this project, we tried three approach to autocomplete a string based on given pairs of data with words as key and frequency as value. 

First one is binary search, definitely owns poor performance due to inevitable sorting time.

Second idea is to use data structures like a trie. Search time is not bad, but each node have a long children list considering there might be 256 characters.

To save the space, we use ternary search tree as third approach. Which works like a binary search tree, but keep the following substrings in childern list when current character matches with value at current node.
