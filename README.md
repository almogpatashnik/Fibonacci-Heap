# Fibonacci Heap   
Fibonacci Heap class implementation in Java.
The Fibonacci Heap is a collection of trees, organized in a circular doubly linked list. Each tree within the heap follows a specific structure—each node has an arbitrary number of children, forming a tree that can be irregular in shape. Nodes in the heap are connected through pointers, allowing for efficient traversal and operations. The heap maintains a pointer to the minimum node, ensuring quick access to the element with the smallest key.

The Fibonacci Heaps offer fast amortized time for insertions, deletions, and key decreases, making them ideal for algorithms with frequent updates. They excel at merging heaps and perform well in average-case scenarios.


## Usage
In order to use the Fibonacci Heap you should download the attached file.

### FibonacciHeap Class
| Function                | Description                                                |
|-------------------------|------------------------------------------------------------|
| **boolean** isEmpty()       | Returns True iff the heap is empty.                        |
| **HeapNode** insert(int i)  | Constructs a node of type HeapNode that contains the key i and puts it on the heap. Returns the created node. |
| **void** deleteMin()        | Deletes the node whose key is minimal among the keys in the heap. |
| **HeapNode** findMin()      | Returns the node whose key is minimal among the keys in the heap. Returns null if the Heap is empty. |
| **void** meld(FibonacciHeap heap2) | Melds the current heap with heap2.                  |
| **int** size()              | Returns the number of items in the heap.                   |
| **int[]** countersRep()     | Returns array s.t. the index i shows the number of trees in the heap whose order (rank) is i. For an empty heap an empty array will be returned. |
| **void** delete(HeapNode x) | Deletes the node from the heap.                           |
| **void** decreaseKey(HeapNode x, int d) | The value of the key of the node x will be reduced by the value d≥0. |
| **int** nonMarked()         | Returns the amount of items in the heap which are not marked. |
| **int** potential()         | Returns the current potential value of the heap. The potential is defined as: Potential = #trees + 2*#marked. |
| **static int** totalLinks() | Returns the number of all links operations performed since the beginning of the program run. A link operation is the operation that receives two trees of the same order and connects them. |
| **static int** totalCuts()  | Returns the number of all cuts operations performed since the beginning of the program run. A cut operation occurs due to decreaseKey, when a subtree is cut off from its parent (including cascading cuts). |
| **static int** kMin(FibonacciHeap H, int k) | Receives a heap H which is a tree with rank deg⁡(H), and a positive number k<size(H). Returns a sorted array of the k smallest nodes in H. |







### HeapNode SubClass
| Function         | Description                                                        |
|------------------|--------------------------------------------------------------------|
| **int** getKey()       | Returns the key of the node.                                  |
| **void** setKey(int key)       | Sets the key of the node.                                  |
| **int** getRank()       | Returns the rank of the node.                                  |
| **void** setRank(int rank)       | Sets the rank of the node.                                  |
| **boolean** getMarked()       | Returns whether the node is marked.                                  |
| **void** setMarked(boolean mark)       | Sets whether the node is marked.                                 |
| **HeapNode** getChild()       | Returns the child of the node.                                  |
| **void** setChild(HeapNode child)       | Sets the child of the node.                                  |
| **HeapNode** getNext()       | Returns the next node of the node.                                  |
| **void** setNext(HeapNode next)       | Sets the next node of the node.                                  |
| **HeapNode** getPrev()       | Returns the previous node of the node.                                  |
| **void** setPrev(HeapNode prev)       | Sets the previous node of the node.                                  |
| **HeapNode** getParent()       | Returns the parent node of the node.                                  |
| **void** setParent(HeapNode parent)       | Sets the parent node of the node.                                  |



***Made by [@almogpatashnik](https://github.com/almogpatashnik) && [@saradorf](https://github.com/saradorf)***
