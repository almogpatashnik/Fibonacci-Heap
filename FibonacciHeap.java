/**
 * FibonacciHeap
 * <p>
 * An implementation of a Fibonacci Heap over integers.
 */
	public class FibonacciHeap {

    private HeapNode first;
    private HeapNode min;
    private int size;
    private int numTrees;
    private int numMarked;
    private static final double Factor = 1.4404;

    public static int numLinks;
    public static int numCuts;


    /**
     * public boolean isEmpty()
     * <p>
     * Returns true if and only if the heap is empty.
     *  complexity: O(1)
     */
    public boolean isEmpty() {
        return this.size == 0; 
    }

    /**
     * public HeapNode insert(int key)
     * <p>
     * Creates a node (of type HeapNode) which contains the given key, and inserts it into the heap.
     * The added key is assumed not to already belong to the heap.
     * <p>
     * Returns the newly created node.
     * complexity: O(1)
     */
    public HeapNode insert(int key) {
        HeapNode newNode = new HeapNode(key);
        return insertNode(newNode);
    }
    /**
     * public HeapNode insertNode(HeapNode newNode)
     * <p>
     * Inserts the node into the heap.
     * The added key is assumed not to already belong to the heap.
     * <p>
     * Returns the newly created node.
     * complexity: O(1)
     */
    private HeapNode insertNode(HeapNode newNode) {
        if (size == 0){
            first = newNode;
            min = newNode;
            newNode.setNext(newNode);
            newNode.setPrev(newNode);
        }
        else {
            insertAtFirst(newNode);
            min = first.getKey() < min.getKey() ? first : min;

        }
        size ++;
        numTrees ++;
        return newNode;
    }

    /**
     * public void deleteMin()
     * <p>
     * Deletes the node containing the minimum key.
     * complexity: O(n)
     */
    public void deleteMin() {
       if(min.getChild() == null && numTrees == 1){ //which means the deleted min was the only node in the heap
            first = null;
            min = null;
            size = 0;
            numTrees = 0;
            numMarked = 0;
            return;
       }
       //actual deleting
        disconnectMinNode();

       //succesive linking
       HeapNode[] arr = successiveLinking();

        //connecting the trees all together
             
       HeapNode lastConnectedTree = connectTrees(arr);
        
        //updating all lasts tree fields and first
        HeapNode last = lastConnectedTree;
        first.setPrev(last);
        last.setNext(first);


    }
    
    private HeapNode[] successiveLinking() {
        HeapNode[] arr = new HeapNode[(int) Math.ceil(Factor * Math.log(size)) + 1];

        for (int i = 0; i < numTrees; i++) {
            HeapNode currTree = first;
            first = first.getNext();
            currTree.setNext(null);
            currTree.setPrev(null);

            while (arr[currTree.getRank()] != null) {
                currTree = link(currTree, arr[currTree.getRank()]);
                arr[currTree.getRank() - 1] = null;
            }

            arr[currTree.getRank()] = currTree;
        }

        return arr;
    }

    private int findIndexOfFirstTree(HeapNode[] arr) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] != null) {
                return i;
            }
        }
        return 0; // Default to the first index if no tree found
    }
    
    
   /* returns the last tree that was connected */
    private HeapNode connectTrees(HeapNode[] arr, int indexFirstTree) {
        int indexFirstTree = findIndexOfFirstTree(arr); //searching for index for first tree
        first = arr[indexFirstTree];
        
        HeapNode curr = first;
        HeapNode treeToConnect = first;
        min = first;
        numTrees = 1;// legal for sure, as  we have already checked the case that no trees are left

        for (int i = indexFirstTree+1; i <arr.length ; i++) {
            if (arr[i]!=null){
                treeToConnect = arr[i];
                curr.setNext(treeToConnect);
                treeToConnect.setPrev(curr);
                min = treeToConnect.getKey()< min.getKey()? treeToConnect: min;
                numTrees++;
                curr = treeToConnect;
            }
        }
        
        return treeToConnect;
    }

    
    
    /**
     * public void disconnectMinNode()
     * <p>
     * actual deleting - disconnecting from the heap
     * complexity: O(logn)
     */
    private void disconnectMinNode(){
        HeapNode child = min.getChild();
        size--;
        if (numTrees == 1) {
            // disconnect min from his child
            min.setChild(null);
            child.setParent(null);

            // defining first
            first = child;
            unmarking(first, min.getRank()); // need to unmark all the new nodes that now became roots.
            numTrees = min.getRank();
        }

        else { // in case that there is more than just tree
            HeapNode prevTreeofMin = min.getPrev();
            HeapNode nextTreeofMin = min.getNext();

            if (child == null){ //case that min doent have a child
                //disconnecting min from other trees
                min.setPrev(null);
                min.setNext(null);

                //bypassing minimum
                prevTreeofMin.setNext(nextTreeofMin);
                nextTreeofMin.setPrev(prevTreeofMin);

                //updating fields
                first = first == min? nextTreeofMin : first;
                numTrees--;

            }
            else {// case that min has children - add them as trees to heap.
                HeapNode firstChild = min.getChild();
                HeapNode lastChild = firstChild.getPrev();

                //connecting children to trees
                firstChild.setPrev(prevTreeofMin);
                lastChild.setNext(nextTreeofMin);

                // connecting trees to children
                prevTreeofMin.setNext(firstChild);
                nextMin.setPrev(lastChild);

                //updating fileds
                numTrees = numTrees - 1 + min.getRank();
                first = first == min? firstChild : first;
                unmarking(firstChild, min.getRank());
            }
        }


    }



    /**
     * public void link()
     * <p>
     * link 2 trees with same rank
     * updating fields
     * complexity: O(1)
     */
    public HeapNode link(HeapNode x, HeapNode y) {
        HeapNode smaller = x.getKey()>y.getKey()? y: x;
        HeapNode bigger = x.getKey()<y.getKey()? y: x;

        //setting children pointers
        if (smaller.getChild() != null) {
            HeapNode childSmaller = smaller.getChild();
            HeapNode lastChildSmaller = childSmaller.getPrev();
            //connecting the bigger to the smaller childrens
            bigger.setNext(childSmaller);
            childSmaller.setPrev(bigger);
            lastChildSmaller.setNext(bigger);
            bigger.setPrev(lastChildSmaller);
        }
        else{ // this is the case of smaller and bigger are two individuals nodes.
            bigger.setNext(bigger);
            bigger.setPrev(bigger);
        }

        //setting the child of smaller - for both cases
        smaller.setChild(bigger);
        smaller.setRank(smaller.getRank() +1);
        bigger.setParent(smaller);

        numLinks++;
        numTrees--;
        return smaller;

    }


    /**
     * public HeapNode findMin()
     * <p>
     * Returns the node of the heap whose key is minimal, or null if the heap is empty.
     * complexity: O(1)
     */
    public HeapNode findMin() { return isEmpty() ? null : this.min;    }


    /**
     * public void meld (FibonacciHeap heap2)
     * <p>
     * Melds heap2 with the current heap.
     * complexity: O(1)
     */
    public void meld(FibonacciHeap heap2) {

        //if one of the heaps are empty
        if (heap2.isEmpty())
            return;

        if (this.isEmpty()){
            first = heap2.first;
            min = heap2.min;
            size = heap2.size;
            numTrees = heap2.numTrees;
            numMarked = heap2.numMarked;
            return;
        }

        //otherwise

        //update fields
        this.size += heap2.size;
        this.min = this.min.getKey() > heap2.min.getKey()? heap2.min : this.min;
        this.numTrees += heap2.numTrees;
        this.numMarked += heap2.numMarked;


        //save pointers to last nodes of each heap
        HeapNode lastHeap1 = this.first.prev;
        HeapNode lastHeap2 = heap2.first.prev;

        // connecting between the heaps
        lastHeap1.setNext(heap2.first);
        heap2.first.setPrev(lastHeap1);

        // connecting the first and the last
        lastHeap2.setNext(first);
        first.setPrev(lastHeap2);

    }

    /**
     * public int size()
     * <p>
     * Returns the number of elements in the heap.
     * complexity: O(1)
     */
    public int size() {
        return this.size; // should be replaced by student code
    }

    /**
     * public int[] countersRep()
     * <p>
     * Return an array of counters. The i-th entry contains the number of trees of order i in the heap.
     * (Note: The size of of the array depends on the maximum order of a tree.)
     * complexity: O(n)
     */
    public int[] countersRep() {
        if (first == null)
            return new int[0];
        int[] arr = new int[getMaxRank()+1];
        arr[first.getRank()]++;
        HeapNode pos = first.getNext();

        //iterating over the trees in the heap
        while (pos != first){
            arr[pos.getRank()]++;
            pos = pos.getNext();
        }
        return arr;
    }

    /**
     * public int getMaxRank()
     * <p>
     * returns the maximum rank in the heap
     * complexity: O(n)
     *
     */
    public int getMaxRank(){
        int max = Integer.MIN_VALUE;
        if (first == null)
            return max;
        max = first.getRank();
        HeapNode pos = first.getNext();
        while (pos != first){
            max = Math.max(pos.getRank(), max);
            pos = pos.getNext();
        }
        return max;
    }

    /**
     * public void delete(HeapNode x)
     * <p>
     * Deletes the node x from the heap.
     * It is assumed that x indeed belongs to the heap.
     * complexity: O(n)
     */
    public void delete(HeapNode x) {
       int minValue = min.getKey();
       decreaseKey(x, x.getKey() - minValue +1);
       deleteMin();
    }

    /**
     * public void decreaseKey(HeapNode x, int delta)
     * <p>
     * Decreases the key of the node x by a non-negative value delta. The structure of the heap should be updated
     * to reflect this change (for example, the cascading cuts procedure should be applied if needed).
     * complexity: O(n)
     */
    public void decreaseKey(HeapNode x, int delta) {
            //updating value
            x.setKey(x.getKey() - delta);
            // check if there is no violation or the node is a root of a tree
            if (x.getParent() == null || x.getParent().getKey() < x.getKey()) {
                min = x.getKey() < min.getKey() ? x : min;
                return;
            }

            // handling violation
            cascadingCut(x,x.getParent());
    }

    /**
     * public void cut(HeapNode x, HeapNode y)
     * <p>
     * cuts x from y
     * complexity: O(1)
     */
    public void cut(HeapNode x, HeapNode y){
        x.setParent(null);
        numMarked = x.getMarked()? numMarked-1 : numMarked;
        x.setMark(false);
        y.setRank(y.getRank() -1);

        //changing pointers in the current tree of x
        if (x.getNext() == x)
            y.setChild(null);
        else{
            if(y.getChild() == x) {
                y.setChild(x.getNext());
            }
            x.getPrev().setNext(x.getNext());
            x.getNext().setPrev(x.getPrev());
        }
        // moving x to be the first tree in the heap
        insertAtFirst(x);
        min = first.getKey() < min.getKey() ? first : min;

        //updating other fields
        numTrees++;
        numCuts++;
    }

    /**
     * public void cascadingCut(HeapNode x, HeapNode y)
     * <p>
     * implementing cascading cuts
     * complexity: O(n)
     */
    public void cascadingCut(HeapNode x, HeapNode y){
        cut(x,y);
        if (y.getParent() != null){
            if (y.getMarked() == false){
                 y.setMark(true);
                numMarked++;
            }
            else {
                cascadingCut(y, y.getParent());
            }
        }
    }


    /**
     * public int nonMarked()
     * <p>
     * This function returns the current number of non-marked items in the heap
     * complexity: O(1)
     */
    public int nonMarked() { return  this.size - this.numMarked;}


    /**
     * public int potential()
     * <p>
     * This function returns the current potential of the heap, which is:
     * Potential = #trees + 2*#marked
     * <p>
     * In words: The potential equals to the number of trees in the heap
     * plus twice the number of marked nodes in the heap.
     * complexity: O(1)
     */
    public int potential() { return this.numTrees + 2 * this.numMarked;}


    /**
     * public static int totalLinks()
     * <p>
     * This static function returns the total number of link operations made during the
     * run-time of the program. A link operation is the operation which gets as input two
     * trees of the same rank, and generates a tree of rank bigger by one, by hanging the
     * tree which has larger value in its root under the other tree.
     * complexity: O(1)
     */
    public static int totalLinks() {
        return numLinks;
    }

    /**
     * public static int totalCuts()
     * <p>
     * This static function returns the total number of cut operations made during the
     * run-time of the program. A cut operation is the operation which disconnects a subtree
     * from its parent (during decreaseKey/delete methods).
     * complexity: O(1)
     */
    public static int totalCuts() {
        return numCuts;
    }

    /**
     * public static int[] kMin(FibonacciHeap H, int k)
     * <p>
     * This static function returns the k smallest elements in a Fibonacci heap that contains a single tree.
     * The function should run in O(k*deg(H)). (deg(H) is the degree of the only tree in H.)
     * <p>
     * ###CRITICAL### : you are NOT allowed to change H.
     * complexity: O(k * DegH)
     */
    public static int[] kMin(FibonacciHeap H, int k) {
        FibonacciHeap helpHeap = new FibonacciHeap();
        int[] arrMin = new int[k];

        //check if heap is empty
        if(H.size == 0) return arrMin;


        HeapNodePointer min =  new HeapNodePointer(H.min.getKey(), H.min );
        helpHeap.insertNode(min);
        for (int i=0; i<k; i++){
            HeapNode nextMin = helpHeap.min;
            arrMin[i] = nextMin.getKey();
            helpHeap.deleteMin();

            // insert children of nextMin to helpHeap
            HeapNode currDeleted = ((HeapNodePointer)nextMin).pointer;
            HeapNode currChild = currDeleted.getChild();
            for (int j = 0; j < currDeleted.getRank(); j++) {
                HeapNodePointer currChildPointer = new HeapNodePointer(currChild.getKey(), currChild);
                helpHeap.insertNode(currChildPointer);
                currChild = currChild.getNext();
            }

        }

        return arrMin;
    }


    /**
     * public void insertAtFirst(HeapNode x)
     * <p>
     * inserts x to the beginning of the heap
     * complexity: O(1)
     */
    private void insertAtFirst(HeapNode x){
        x.setNext(first);
        x.setPrev(first.prev);
        first = x;
        first.getNext().setPrev(first);
        first.getPrev().setNext(first);
    }

    /**
     * public void unmarking()
     * <p>
     * nodes became roots -> need to be unmarked
     * complexity: O(logn)
     */
    private void unmarking(HeapNode x, int rank){
        for (int i = 0; i < rank; i++) {
            if (x.getMarked()){
                numMarked--;
            }
            x.setMark(false);
            x.setParent(null);
            x = x.getNext();

        }
    }


    /**
     * public class HeapNode
     * <p>
     * If you wish to implement classes other than FibonacciHeap
     * (for example HeapNode), do it in this file, not in another file.
     */
    public static class HeapNode {

        public int key;
        private int rank;
        private boolean mark;
        private HeapNode child;
        private HeapNode next;
        private HeapNode prev;
        private HeapNode parent;

        /***construcor***/

        public HeapNode(int key) {
            this.key = key;

        }

        /****************geters and setters********************/

        /**
         * public int getKey()
         * complexity: O(1)
         */
        public int getKey() {
            return this.key;
        }

        /**
         * public void setKey()
         * complexity: O(1)
         */
        public void setKey(int key) {
            this.key = key;
        }

        /**
         * public int getRank()
         * complexity: O(1)
         */
        public int getRank() {
            return rank;
        }

        /**
         * public void setRank(int rank)()
         * complexity: O(1)
         */
        public void setRank(int rank) {
            this.rank = rank;
        }

        /**
         * public boolean getMarked()
         * complexity: O(1)
         */
        public boolean getMarked() {
            return mark;
        }

        /**
         * public void setMark(boolean mark)
         * complexity: O(1)
         */
        public void setMark(boolean mark) {
            this.mark = mark;
        }

        /**
         * public HeapNode getChild()
         * complexity: O(1)
         */
        public HeapNode getChild() {
            return child;
        }


        /**
         * public void setChild(HeapNode child)
         * complexity: O(1)
         */
        public void setChild(HeapNode child) {
            this.child = child;
        }

        /**
         * public void HeapNode getNext()
         * complexity: O(1)
         */
        public HeapNode getNext() {
            return next;
        }

        /**
         * public void void setNext(HeapNode next)
         * complexity: O(1)
         */
        public void setNext(HeapNode next) {
            this.next = next;
        }

        /**
         * public HeapNode getPrev()
         * complexity: O(1)
         */
        public HeapNode getPrev() {
            return prev;
        }

        /**
         * public void setPrev(HeapNode prev)
         * complexity: O(1)
         */
        public void setPrev(HeapNode prev) {
            this.prev = prev;
        }

        /**
         * public HeapNode getParent()
         * complexity: O(1)
         */
        public HeapNode getParent() {
            return parent;
        }

        /**
         * public void setParent(HeapNode parent)
         * complexity: O(1)
         */
        public void setParent(HeapNode parent) {
            this.parent = parent;
        }

        /*****************************************************/


    }

    // class to extend HeapNode, adding pointer field
    public static class HeapNodePointer extends HeapNode{

        private HeapNode pointer;
        public HeapNodePointer(int key, HeapNode pointer) {
            super(key);
            this.pointer = pointer;
        }
    }
}


