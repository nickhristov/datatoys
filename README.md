# datatoys

This is a small collection of datastructures in Java.

## RadixTrie

The radix trie implementation here keeps its children in a sorted list, in order to be able to 
have efficient traversals between prefixes at the same level. Implements the java.util.Map<String, V> interface.

At present, the radix trie is not optimal in the degenerate case where a radix trie becomes a regular trie when 
each prefix is one character long.

Performance of put operations is more or less equivalent to the java red-black tree implementation.

At present, the implementation of the iterator is naive and copies the data into an ArrayList.
