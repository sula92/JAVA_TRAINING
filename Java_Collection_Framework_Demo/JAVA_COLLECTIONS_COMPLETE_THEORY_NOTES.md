# Java Collections - Complete Theory Notes

## 1. What Is the Java Collection Framework?

The Java Collection Framework is a group of interfaces, classes, and utility methods used to store, organize, search, sort, and process groups of objects.

Common real-world examples:

- list of students
- set of unique emails
- queue of print jobs
- map of product code to product details
- sorted ranking of exam results

---

## 2. Collection Framework Hierarchy

```text
Iterable
   |
Collection
   |
   |-- List
   |-- Set
   |-- Queue
        |
        |-- Deque

Map is part of the framework, but it does not extend Collection.
```

Important root interfaces:

- `Iterable`
- `Collection`
- `List`
- `Set`
- `Queue`
- `Deque`
- `Map`

---

## 3. Iterable and Collection

`Iterable` allows an object to be used in an enhanced for loop.

```java
for (String item : items) {
    System.out.println(item);
}
```

`Collection` is the root interface for most collection types.

Common methods:

- `add()`
- `remove()`
- `contains()`
- `size()`
- `isEmpty()`
- `clear()`

Demo class:

```text
CollectionIntroDemo
```

---

## 4. List

A `List` stores elements in insertion order and allows duplicates.

Important features:

- index-based access
- insertion order is preserved
- duplicates are allowed
- elements can be replaced using `set()`

Common implementations:

| Implementation | Main Use |
|----------------|----------|
| `ArrayList` | fast index access and general use |
| `LinkedList` | frequent insertions/removals in the middle |
| `Vector` | old synchronized list class |
| `Stack` | old stack class, usually prefer `ArrayDeque` |

Demo class:

```text
ListConceptDemo
```

---

## 5. ArrayList vs LinkedList

| Feature | ArrayList | LinkedList |
|--------|-----------|------------|
| Internal structure | resizable array | linked nodes |
| Index access | fast | slower |
| Add at end | usually fast | fast |
| Add/remove in middle | may require shifting | easier after position is found |
| Memory usage | lower | higher because of node links |

In most normal cases, `ArrayList` is the best default choice for a list.

---

## 6. Set

A `Set` stores unique elements only.

Important features:

- duplicates are not allowed
- no index-based access
- useful for uniqueness

Common implementations:

| Implementation | Main Behavior |
|----------------|---------------|
| `HashSet` | unique values, no guaranteed order |
| `LinkedHashSet` | unique values, insertion order |
| `TreeSet` | unique values, sorted order |

Demo class:

```text
SetConceptDemo
```

---

## 7. equals() and hashCode()

`HashSet`, `HashMap`, and similar hash-based collections use `equals()` and `hashCode()` to identify duplicate objects.

If two objects are logically equal, they should usually have the same hash code.

In the demo, two `Student` objects with the same `id` are treated as duplicates.

```java
public boolean equals(Object object)
public int hashCode()
```

This is very important in interviews and real projects.

---

## 8. Queue

A `Queue` is used when elements must be processed in order.

Common methods:

| Method | Meaning |
|--------|---------|
| `offer()` | add an element |
| `peek()` | view next element without removing |
| `poll()` | remove and return next element |

Common implementations:

- `ArrayDeque`
- `LinkedList`
- `PriorityQueue`

Demo class:

```text
QueueDequeDemo
```

---

## 9. Deque

`Deque` means double-ended queue.

It allows adding and removing from both ends.

It can also be used as a stack:

```java
push()
pop()
peek()
```

Modern Java usually prefers `ArrayDeque` instead of the old `Stack` class.

---

## 10. PriorityQueue

A `PriorityQueue` processes elements according to priority, not normal insertion order.

For numbers, the smallest value is processed first by default.

For custom objects, you can provide a `Comparator`.

---

## 11. Map

A `Map` stores key-value pairs.

Important features:

- each key must be unique
- values can be duplicated
- fast lookup by key

Common methods:

- `put()`
- `get()`
- `remove()`
- `containsKey()`
- `containsValue()`
- `putIfAbsent()`
- `keySet()`
- `values()`
- `entrySet()`

Common implementations:

| Implementation | Main Behavior |
|----------------|---------------|
| `HashMap` | fast lookup, no guaranteed order |
| `LinkedHashMap` | keeps insertion order |
| `TreeMap` | sorted by key |
| `Hashtable` | old synchronized map class |

Demo class:

```text
MapConceptDemo
```

---

## 12. Iterator

An `Iterator` is used to traverse a collection safely.

Important methods:

- `hasNext()`
- `next()`
- `remove()`

Use `iterator.remove()` when removing items during iteration.

Demo class:

```text
IteratorConceptDemo
```

---

## 13. ListIterator

`ListIterator` works only with lists.

Extra features:

- move forward
- move backward
- add element during iteration
- replace element during iteration

Useful methods:

- `hasPrevious()`
- `previous()`
- `add()`
- `set()`

---

## 14. Sorting

Collections can be sorted using:

- `Comparable`
- `Comparator`
- `Collections.sort()`
- `List.sort()`

### Comparable

`Comparable` defines the natural order of a class.

```java
class Student implements Comparable<Student>
```

In the demo, `Student` naturally sorts by `id`.

### Comparator

`Comparator` defines custom sorting logic.

Examples:

- sort by name
- sort by GPA
- sort by price

Demo class:

```text
SortingConceptDemo
```

---

## 15. Collections Utility Class

`Collections` is a utility class with useful static methods.

Common methods:

- `sort()`
- `reverse()`
- `shuffle()`
- `max()`
- `min()`
- `frequency()`
- `unmodifiableList()`

Demo class:

```text
CollectionsUtilityDemo
```

---

## 16. Generics

Generics give type safety to collections.

Without generics, wrong types can accidentally be added.

With generics:

```java
List<String> names = new ArrayList<>();
```

The list accepts only strings, and you do not need manual casting when reading.

Demo class:

```text
GenericsConceptDemo
```

---

## 17. Wildcards

Wildcards make generic methods more flexible.

### Upper bounded wildcard

```java
List<? extends Number>
```

This means the list can contain `Number` or a subclass of `Number`.

Good for reading.

### Lower bounded wildcard

```java
List<? super Integer>
```

This means the list can accept `Integer` values into `Integer`, `Number`, or `Object` lists.

Good for adding.

---

## 18. Immutable and Unmodifiable Collections

Java has different ways to create collections that should not be changed.

Examples:

- `List.of()`
- `Set.of()`
- `Map.of()`
- `Collections.unmodifiableList()`

Important difference:

`List.of()` creates an immutable list.

`Collections.unmodifiableList()` creates an unmodifiable view over an existing list.

Demo class:

```text
ImmutableAndFailFastDemo
```

---

## 19. Arrays.asList()

`Arrays.asList()` creates a fixed-size list backed by an array.

It allows:

- `get()`
- `set()`

It does not allow:

- `add()`
- `remove()`

This is a common interview trap.

---

## 20. Fail-Fast Behavior

Many Java collection iterators are fail-fast.

If a collection is structurally modified directly while it is being iterated, Java may throw:

```text
ConcurrentModificationException
```

Wrong approach:

```java
for (String item : list) {
    list.remove(item);
}
```

Correct approach:

```java
Iterator<String> iterator = list.iterator();
while (iterator.hasNext()) {
    String item = iterator.next();
    iterator.remove();
}
```

---

## 21. Choosing the Right Collection

| Requirement | Good Choice |
|-------------|-------------|
| ordered data with duplicates | `ArrayList` |
| unique values, order not important | `HashSet` |
| unique values with insertion order | `LinkedHashSet` |
| unique values sorted | `TreeSet` |
| FIFO processing | `ArrayDeque` as `Queue` |
| stack behavior | `ArrayDeque` |
| key-value lookup | `HashMap` |
| key-value lookup with insertion order | `LinkedHashMap` |
| key-value lookup sorted by key | `TreeMap` |

---

## 22. Performance Summary

| Operation | Common Fast Choice |
|-----------|--------------------|
| index access | `ArrayList` |
| lookup by key | `HashMap` |
| uniqueness check | `HashSet` |
| sorted unique values | `TreeSet` |
| queue operations | `ArrayDeque` |

Big-O basics:

- `ArrayList` get by index: usually `O(1)`
- `HashMap` get by key: usually `O(1)`
- `HashSet` contains: usually `O(1)`
- `TreeSet` add/search: usually `O(log n)`
- `TreeMap` get/put: usually `O(log n)`

---

## 23. Legacy Collection Classes

Older collection classes still exist:

- `Vector`
- `Stack`
- `Hashtable`

They are synchronized legacy classes. In modern Java, usually prefer:

- `ArrayList` instead of `Vector`
- `ArrayDeque` instead of `Stack`
- `HashMap` instead of `Hashtable`

---

## 24. Demo Classes Included

| Demo Class | Covers |
|------------|--------|
| `CollectionMasterDemo` | runs all demos |
| `CollectionIntroDemo` | `Collection` basics |
| `ListConceptDemo` | `ArrayList`, `LinkedList`, list behavior |
| `SetConceptDemo` | `HashSet`, `LinkedHashSet`, `TreeSet`, duplicates |
| `QueueDequeDemo` | `Queue`, `Deque`, `PriorityQueue` |
| `MapConceptDemo` | `HashMap`, `LinkedHashMap`, `TreeMap` |
| `IteratorConceptDemo` | `Iterator`, `ListIterator`, safe removal |
| `SortingConceptDemo` | `Comparable`, `Comparator` |
| `CollectionsUtilityDemo` | `Collections` utility methods |
| `GenericsConceptDemo` | generics and wildcards |
| `ImmutableAndFailFastDemo` | immutable lists and fail-fast behavior |
| `Student` | custom object with `Comparable`, `equals`, `hashCode` |
| `Product` | custom object used in `Map` |

---

## 25. Interview Questions and Answers

### Question 1
What is the Java Collection Framework?

**Answer:** It is a set of interfaces and classes used to store and process groups of objects.

### Question 2
What is the difference between `List` and `Set`?

**Answer:** `List` allows duplicates and keeps order. `Set` stores unique elements.

### Question 3
What is the difference between `HashSet`, `LinkedHashSet`, and `TreeSet`?

**Answer:** `HashSet` gives no guaranteed order, `LinkedHashSet` keeps insertion order, and `TreeSet` keeps sorted order.

### Question 4
What is the difference between `HashMap`, `LinkedHashMap`, and `TreeMap`?

**Answer:** `HashMap` gives fast lookup with no guaranteed order, `LinkedHashMap` keeps insertion order, and `TreeMap` sorts by key.

### Question 5
Why are `equals()` and `hashCode()` important?

**Answer:** Hash-based collections use them to decide whether objects are equal and where they should be stored.

### Question 6
What is the difference between `Comparable` and `Comparator`?

**Answer:** `Comparable` defines natural ordering inside the class. `Comparator` defines external custom ordering.

### Question 7
Why use generics in collections?

**Answer:** Generics provide type safety and remove the need for manual casting.

### Question 8
What is fail-fast behavior?

**Answer:** It means an iterator may throw `ConcurrentModificationException` if the collection is modified directly while iterating.

### Question 9
What is the best replacement for `Stack`?

**Answer:** `ArrayDeque` is usually preferred.

### Question 10
Is `Map` a child of `Collection`?

**Answer:** No. `Map` is part of the collection framework, but it does not extend `Collection`.

---

## 26. Quick Revision

```text
List       -> ordered, duplicates allowed
Set        -> unique elements
Queue      -> process elements in order
Deque      -> add/remove from both ends, stack behavior
Map        -> key-value pairs
Iterator   -> safe traversal
Comparable -> natural sorting
Comparator -> custom sorting
Generics   -> type safety
```

---

## 27. Final Conclusion

Java collections are essential for practical programming.

The most important skill is choosing the correct collection:

- use `List` for ordered data
- use `Set` for uniqueness
- use `Queue` for processing
- use `Map` for key-value lookup
- use `Comparator` when custom sorting is needed
- use generics for type-safe code

Mastering collections makes Java code cleaner, faster, and easier to maintain.
