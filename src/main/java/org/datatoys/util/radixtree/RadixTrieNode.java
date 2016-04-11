package org.datatoys.util.radixtree;

import java.util.List;

/**
 * A node in the radix tree.
 */
class RadixTrieNode<V> {

    String prefix;
    char[] chars;
    List<RadixTrieNode<V>> children;
    V value;

    public void setPrefix(String prefix) {
        this.prefix = prefix;
        chars = prefix.toCharArray();
    }

    @Override
    public String toString() {
        return  prefix;
    }
}
