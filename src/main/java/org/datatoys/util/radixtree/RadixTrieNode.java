package org.datatoys.util.radixtree;

import java.util.List;

/**
 * A node in the radix tree.
 */
class RadixTrieNode<V> {

    char[] chars;
    List<RadixTrieNode<V>> children;
    V value;

    public String toString() {
        return new String (chars);
    }
}
