package org.datatoys.util.radixtree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * An implementation of a radix trie.
 * <p>
 * This implementation is not thread safe, as it does not synchronize its operations.
 * <p>
 * Non-get/put operations utilize standard java.util collections to accumulate values:
 * thus concurrent modification exceptions are not possible to happen, since the values
 * in the collections are copies of the actual data.
 */
@SuppressWarnings("WeakerAccess")
public class RadixTrie<V> implements Map<String, V> {

    private RadixTrieNode<V> root = new RadixTrieNode<>();
    private int size;

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size < 1;
    }

    @Override
    public boolean containsKey(Object key) {
        return key instanceof String && get(key) != null;
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    @Override
    public boolean containsValue(Object value) {
        return values().contains(value);
    }

    @Override
    public V get(Object key) {
        String keyStr = (String) key;
        RadixTrieNode<V> node = getNode(root, keyStr);
        if (node != null) {
            return node.value;
        } else {
            return null;
        }
    }

    public V put(String key, V value) {
        if (key == null) {
            throw new NullPointerException("Supplied key is null: cannot map values based on null keys");
        }
        if (key.isEmpty()) {
            // we *could* allow this if the root is allowed to hold a value.
            throw new IllegalArgumentException("Supplied key is an empty string: cannot map values based on empty strings");
        }
        return putAtNode(this, root, key, value);
    }

    @Override
    public V remove(Object key) {

        return null;
    }

    @Override
    public void putAll(Map<? extends String, ? extends V> map) {
        for (Map.Entry<? extends String, ? extends V> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    // package private for unit testing
    static <V> V putAtNode(RadixTrie<V> trie, RadixTrieNode<V> node, String key, V value) {
        List<RadixTrieNode<V>> children = node.children;
        if (children == null) {
            children = new ArrayList<>();
            node.children = children;
        }
        if (children.isEmpty()) {
            children.add(createValueNode(key, value));
            trie.size++;
        } else {
            boolean inserted = false;
            for (int i = 0; i < children.size(); i++) {
                RadixTrieNode<V> child = children.get(i);
                String prefix = child.prefix;
                int commonCharacters = numCharsInCommonPrefix(child.chars, key);
                if (commonCharacters > 0) {
                    int prefixLength = prefix.length();
                    int keyLength = key.length();
                    if (commonCharacters == keyLength) {
                        // exact match, replace the node
                        V oldValue = child.value;
                        child.value = value;
                        return oldValue;
                    } else if (commonCharacters == prefixLength) {
                        // child node is a prefix for key, descend deeper in the tree
                        key = key.substring(commonCharacters, keyLength);
                        return putAtNode(trie, child, key, value);
                    } else {
                        // only partial overlap
                        // create a new parent node with common prefix, then re-parent this child
                        child = children.remove(i);

                        // create an add the new parent
                        RadixTrieNode<V> newParent = new RadixTrieNode<>();
                        newParent.setPrefix(key.substring(0, commonCharacters));
                        children.add(i, newParent);

                        String newChildPrefix = prefix.substring(commonCharacters, prefixLength);
                        putAtNode(trie, newParent, newChildPrefix, child.value); // reparent the previous node data

                        // now re-attempt insertion at the
                        key = key.substring( commonCharacters, keyLength);
                        return putAtNode(trie, newParent, key, value);

                    }
                } else {
                    // no common substring, order by first character
                    if (key.charAt(0) < prefix.charAt(0)) {
                        // insert here at this index
                        children.add(i, createValueNode(key, value));
                        inserted = true;
                        trie.size++;
                        break;
                    }
                }
            }
            if (!inserted) {
                // insertion is at the end of the list
                children.add(createValueNode(key, value));
                trie.size++;
                return null;
            }
        }
        return null;
    }

    static int numCharsInCommonPrefix(String prefix, String key) {
        int keyLength = key.length();
        int prefixLength = prefix.length();
        int commonChars = 0;
        for (int idx = 0; idx < prefixLength && idx < keyLength && prefix.charAt(idx) == key.charAt(idx); idx++) {
            commonChars++;
        }
        return commonChars;
    }

    static int numCharsInCommonPrefix(char[] prefix, String key) {
        int keyLength = key.length();
        int prefixLength = prefix.length;
        int commonChars = 0;
        for (int idx = 0; idx < prefixLength && idx < keyLength && prefix[idx] == key.charAt(idx); idx++) {
            commonChars++;
        }
        return commonChars;
    }

    // package private for unit testing
    RadixTrieNode<V> getNode(RadixTrieNode<V> node, String key) {
        if (node.chars != null) {
            int length = Math.min(key.length(), node.chars.length);
            String keyRemainder = key.substring(0, length);
            if (keyRemainder.isEmpty()) {
                return node;
            }
        }
        List<RadixTrieNode<V>> children = node.children;
        if (children == null || children.isEmpty()) {
            return null;
        }
        for (RadixTrieNode<V> childNode : node.children) {
            if (key.startsWith(childNode.prefix)) {
                if (key.equals(childNode.prefix)) {
                    return childNode;
                } else {
                    return getNode(childNode, key.substring(childNode.chars.length, key.length()));
                }
            }
        }
        return null;

    }

    @Override
    public void clear() {
        root = new RadixTrieNode<>();
    }

    @Override
    public Set<String> keySet() {
        TrieNodeKeyAccumulator accumulator = new TrieNodeKeyAccumulator();
        walker.walkTrie(accumulator);
        return accumulator.getKeys();
    }

    @Override
    public Collection<V> values() {
        TrieNodeValueAccumulator valueAccumulator = new TrieNodeValueAccumulator();
        walker.walkTrie(valueAccumulator);
        return valueAccumulator.getValues();
    }

    @Override
    public Set<Map.Entry<String, V>> entrySet() {
        TrieNodeEntryAccumulator accumulator = new TrieNodeEntryAccumulator();
        walker.walkTrie(accumulator);
        return accumulator.getAccumulator();
    }


    private static <V> RadixTrieNode<V> createValueNode(String key, V value) {
        RadixTrieNode<V> node = new RadixTrieNode<>();
        node.value = value;
        node.setPrefix(key);
        return node;
    }

    private interface TrieNodeVisitor<V> {
        void visitNode(String keyPath, RadixTrieNode<V> node);
    }

    private class TrieNodeWalker {
        void walkTrie(TrieNodeVisitor<V> visitor) {
            walkTrie("", root, visitor);
        }

        private void walkTrie(String priorprefix, RadixTrieNode<V> node, TrieNodeVisitor<V> visitor) {
            visitor.visitNode(priorprefix, node);
            List<RadixTrieNode<V>> children = node.children;
            if (children != null && !children.isEmpty()) {
                for (RadixTrieNode<V> child : children) {
                    walkTrie(priorprefix + child.prefix, child, visitor);
                }
            }
        }
    }

    private static class Entry<V> implements Map.Entry<String, V> {

        RadixTrieNode<V> node;
        String key;

        @Override
        public String getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return node.value;
        }

        @Override
        public V setValue(V value) {
            V oldValye = node.value;
            node.value = value;
            return oldValye;
        }
    }

    private class TrieNodeEntryAccumulator implements TrieNodeVisitor<V> {
        Set<Map.Entry<String, V>> accumulator = new HashSet<>(size);

        @Override
        public void visitNode(String keyPath, RadixTrieNode<V> node) {
            if (node.value != null) {
                Entry<V> entry = new Entry<>();
                entry.key = keyPath;
                entry.node = node;
                accumulator.add(entry);
            }
        }

        Set<Map.Entry<String, V>> getAccumulator() {
            return accumulator;
        }
    }

    private class TrieNodeValueAccumulator implements TrieNodeVisitor<V> {

        List<V> values = new ArrayList<>(size);

        @Override
        public void visitNode(String keyPath, RadixTrieNode<V> node) {
            if (node.value != null) {
                values.add(node.value);
            }
        }

        List<V> getValues() {
            return values;
        }
    }

    private class TrieNodeKeyAccumulator implements TrieNodeVisitor<V> {
        HashSet<String> keys = new HashSet<>(size);

        @Override
        public void visitNode(String keyPath, RadixTrieNode<V> node) {
            if (node.value != null) {
                keys.add(keyPath);
            }
        }

        HashSet<String> getKeys() {
            return keys;
        }
    }

    private final TrieNodeWalker walker = new TrieNodeWalker();
}
