
/**
 * 
 * @author Cooper
 * @version 1
 */
import java.*;

public class AVLTree<E>
{
    
    private int size;
    
    private int height;
    
    private TreeNode aboveRoot;
    
    String dictionary = "";
    String log = "";
    
    int modCount = 0;
    int comparisons = 0;
    
    /**
     * Constructor for objects of class AVLTree
     */
    public AVLTree()
    {
        // initialise instance variables
    }
    
    
    /*
     * a Linked List implementation for the nodes of the AVL Tree
     */
    private class TreeNode
    {
        private E item;      // item in the node
        private TreeNode left;   // left subtree
        private TreeNode right;  // right subtree
        
        TreeNode()
        {
            item = null;
            left = null;
            right = null;
        }
        
        // Constructor
        TreeNode(E item)
        {
            this.item = item;
            left = null;
            right = null;
        }
        
        // failed compareTo method
        /*
        private int compareTo(E item)
        {
            String word = (String) this.item;
            String otherWord = (String) item;
            int length = word.length();
            
            for( int i = 0; i < length; i++ )
            {
                if( word.charAt(i) > otherWord.charAt(i) )
                return 1;
                
                else if( word.charAt(i) < otherWord.charAt(i) )
                return -1;
            }
            
        }
        */
        
        //@return item stored in the node
        public E examine()
        {
            return item;
        }
        
        // replaces item in node
        // @param item  the item to replace current item in node
        public void replace(E item)
        {
            this.item = item;
        }
        
        //@return left subtree
        public TreeNode getLeft()
        {
            return left;
        }
        
        //@return right subtree
        public TreeNode getRight()
        {
            return right;
        }
        
        //Replaces the item of the left child
        public void setLeft(E item)
        {
            if( left == null )
            {
                left = new TreeNode(item);
            }
            else left.replace(item);
        }
        
        //Replaces the item of the right child
        public void setRight(E item)
        {
            if( right == null )
            {
                right = new TreeNode(item);
            }
            else right.replace(item);
        }
        
        //Replaces the left subtree
        public void setLeftTree(TreeNode n)
        {
            left = n;   
        }
        
        //Replaces the right subtree
        public void setRightTree(TreeNode n)
        {
            right = n;   
        }
        
        //@return the height of the tree having the node as the root
        public int height()
        {
            return 1 + Math.max( left.height(), right.height() );   // recursively gets height of subtrees
        }
        
        //@return the height difference between the left and right subtrees
        // a positive number means the left tree is larger, while a negative means the right tree is larger
        public int balance()
        {
            int leftHeight;
            int rightHeight;
            
            if ( left == null )
                leftHeight = 0;
            else leftHeight = left.height();
                
            if ( right == null )
                rightHeight = 0;
            else rightHeight = right.height();
            
            return leftHeight - rightHeight;
        }
        
    }
    
    /*
     * @param rotated the node to be rotated
     * @param parent the parent of the rotating node
     */
    private void rotate(TreeNode rotated, TreeNode parent)
    {
        modCount++; 
        
        int balance = rotated.balance();
        TreeNode child;
        TreeNode grandChild;
        
        // if statements determine if LL, RR, LR, or RL rotation is required
        // look at which child has unbalanced side, and then which grandchild has unbalanced side
        
        if( balance > 1 )
            child = rotated.getLeft();
            
        else if ( balance < -1 )
            child = rotated.getRight();
        
        if( child.balance() > 1 )
            grandChild = child.getLeft();
            
        else if( child.balance() < -1 )
            grandChild = child.getRight();
        
        int childBalance = child.balance();
            
        // if left imbalance in left subtree (Left-left rotation)
        if( balance > 1 && childBalance > 1 )
        {
            if( parent != aboveRoot && parent.getRight() == rotated )   
            {
                parent.setRightTree(child); // replace parent's right child if the rotated node is right child
            }
            else 
            {
                parent.setLeftTree(child);  // replace parent's left child as rotated node was the left child
            }
            
            child.setRightTree(rotated);    // the child is now the root of the subtree, and it's right child
                                        // is now the old root of the subtree
                                        
            rotated.setLeftTree( child.getRight() );    // old root now has right child of new root as it's left child
        }
        
        // if right imbalance in right subtree
        if( balance < -1 && childBalance < -1 )
        {
            if( parent != aboveRoot && parent.getRight() == rotated )
            {
                parent.setRightTree(child);
            }
            else
            {
                parent.setLeftTree(child);
            }
            
            child.setLeftTree(rotated);
            
            rotated.setRightTree( child.getLeft() );
        }
        
        // if right imbalance in left subtree
        if( balance > 1 && childBalance < -1 )
        {
            grandChild = child.getRight();      // grandChild is right heavy node
            
            rotated.setLeftTree( grandChild );      // replaces the child with the grandChild
            
            child.setRightTree( grandChild.getLeft() ); // old child now has grandChild's left child as its right child
            
            grandChild.setLeftTree( child );        // grandChild has old child as its left child
            
            rotate(rotated, parent);            // now the tree has left imbalance on left subtree, so recurse to make final fix
            
        }
        
        // if left imbalance in right subtree
        if( balance < -1 && childBalance > 1 )
        {
            grandChild = child.getLeft();       // grandChild is left heavy node
                
            rotated.setRightTree( grandChild );     // replaces child with the grandChild
            
            child.setLeftTree( grandChild.getRight() ); // old child now has grandChild's right child as its left child
            
            grandChild.setRightTree( child );       // grandChild has old child as its right child
            
            rotate( rotated, parent);           // now the tree has right imbalance on right subtree, so recurse to make final fix
            
        }
        
        
        log = log + " " + "Operation rotate(Rotated, Parent) completed using 0 comparisons.";
    }
    
    
   
    /**
    *Adds a new element to the Dictionary 
    *If there is an equal element already in the table, or the item is null it returns false.
    *@param item the item to be added.
    *@return true if the item is not null, and not already in the dictionary.
    **/
    public boolean add(E item)
    {
        modCount++; 
        
        if(item == null)
            return false;
        
        if( aboveRoot.getLeft() == null )           // if there is no root, make the new item the root
        {
            aboveRoot.getLeft().replace(item);
            return true;
        }
            
        return add( item, aboveRoot.getLeft() );    // will return false if item is duplicate
        
        
    }
   
    /*
     * private method used by the public add method
     * @param item the item to be added
     * @param comp the node of the tree to compare the new item to
     * @return true if the item is not already in the dictionary
     */
    
    
    private boolean add(E item, TreeNode comp)
    {
        
        int comparison = item.compareTo( comp.examine() ); // returns -1 if below, 0 if equal, +1 if greater than
        comparisons++;
        
        boolean unique = true;
        
        if( comparison == 0)    // return false without addiing duplicate entry
            return false;
        
        if( comparison < 0 )    // if new entry is less than compared value
        {
            if( comp.getLeft() == null )    // if there is no node
            {
                comp.getLeft().replace(item);   // then place the new node
                return true;                    // this is where a recursive traversal could end
            }
            
            
            else if( add( item, comp.getLeft() ) )   // if left child is not null, recursively traverse and compare
            {
                unique = true;    // if all recursive calls return true, it was unique
            }
        }
        
        else
        {
            if( comp.getRight() == null )
            {
                comp.getRight().replace(item);
                return true;                    // this is where a recursive traversal could end
            }
            
            
            else if( add( item, comp.getRight() ) ) // if right child is not null, recursively traverse and compare
            {
                unique = true;    // if all recursive calls return true, it was unique  
            }
        }
               
        if( comp.getLeft() != null )
            rotate( comp.getLeft(), comp );
            
        if( comp.getRight() != null )
            rotate( comp.getRight(), comp );
            
        log = log + " " + "Operation add( item ) completed using " + comparisons + " comparisons.";    
        comparisons = 0;
        
        return unique;      // true if item is not a duplicate
        
    }
    
    /**
    *Checks to see whether the Dictionary is empty
    *@return true if and only if the Dictionary is Empty
    **/
    public boolean isEmpty()
    {
        if( aboveRoot.getLeft() == null )
        {
            log = log + " " + "Operation isEmpty() completed using 0 comparisons.";
            return true;
        }
            
        else 
        {
            log = log + " " + "Operation isEmpty() completed using 0 comparisons.";
            return false;
        }
    }

    /**
    *Checks to see if an element is contained in the Dictionary
    *@param item the item to be checked.
    *@return true if and only if the Dictionary contains something equal to item.
    **/
    public boolean contains(E item);
    {
        TreeNode root = aboveRoot.getLeft();
        
        contains( item, root );
        
        log = log + " " + "Operation contains( item ) completed using " + comparisons + " comparisons.";
        comparisons = 0;
        
    }
    
    /**
    *Checks to see if an element is contained in the Dictionary
    *@param item the item to be checked.
    *@return the TreeNode of that item.
    **/
    private TreeNode find(E item);
    {
        TreeNode root = aboveRoot.getLeft();
        
        find( item, root );
        
        log = log + " " + "Operation contains( item ) completed using " + comparisons + " comparisons.";
        comparisons = 0;
        
    }
    
    /**
    *Checks to see if an element is contained in the Dictionary
    *@param item the item to be checked.
    *@return true if the item is in the tree.
    **/
    private boolean contains(E item, TreeNode comp)
    {
        comparisons++;
        
        int comparison = item.compareTo( comp.examine() ); // returns -1 if below, 0 if equal, +1 if greater than
        
        if( comp == null )
            return false;
        
        if( comparison == 0 )       // return true if items are equal
            return true;
            
        else if( comparison < 0 )   // if item is less than compared, traverse and compare recursively starting with left child
        {
            if( comp.getLeft() != null )
                return contains( item, comp.getLeft() );
        }
        
        else if( comparison >= 1 )  // if item is greater than compared, traverse and compare recursively starting with right child
        {
            if( comp.getRight() != null )
                return contains( item, comp.getRight() );
        }
        
        return false;
    }
    
    /*
     * method based on above method to return the TreeNode of a given item
     * @param item the item to be found
     * @param comp the node in the tree to be compared to, allowing item to be found recursively
     * @return TreeNode of the given item
    */
    private TreeNode find(E item, TreeNode comp)
    {
        comparisons++;
        
        int comparison = item.compareTo( comp.examine() ); // returns -1 if below, 0 if equal, +1 if greater than
        
        if( comp == null )
            return null;
        
        if( comparison == 0 )       // return node if items are equal
            return comp;
            
        else if( comparison < 0 )   // if item is less than compared, traverse and compare recursively starting with left child
        {
            if( comp.getLeft() != null )
                return find( item, comp.getLeft() );
        }
        
        else if( comparison >= 1 )  // if item is greater than compared, traverse and compare recursively starting with right child
        {
            if( comp.getRight() != null )
                return find( item, comp.getRight() );
        }
        
        return null;
    }

    /**
    *Checks to see if an element has a predecessor in the dictionary
    *@return true if and only if there is an element strictly less than item in the Dictionary
    *@param item the item to be checked
    **/ 
    public boolean hasPredecessor(E item)
    {
        TreeNode<E> node;
        
        if( contains( item ) == false )
            return false;
            
        node = find( item );
        
        if( node.getLeft() == null )        // if left child of item is not null, there is a predecessor
            return false;
        
        else return true;
            
        
            
    }

    /**
    *Checks to see if an element has a successor in the dictionary
    *@return true if and only if there is an element strictly greater than item in the Dictionary
    *@param item the item to be checked
    **/ 
    public boolean hasSuccessor(E item);

    /**
    *Find the greatest element less than the specified element
    *@return the element strictly less than item in the Dictionary
    *@param item the item to be checked
    *@throws NoSuchElementException if there is no lesser element.
    **/ 
    public E predecessor(E item)
    {
        TreeNode<E> node;
        
        if( contains( item ) == false )             // check the item exists
            throw new NoSuchElementException("");
            
        node = find( item );                        // find TreeNode for that item
        
        if( node.getLeft() == null )                // if left child of item is not null, there is a predecessor
            throw new NoSuchElementException("");
        
        while( node.getLeft() != null )             // traverse left children of node until null
        {
            node = node.getLeft();
        }
        
        return node.item;
    }
   

    /**
    *Find the least element greater than the specified element
    *@return the element strictly greater than item in the Dictionary
    *@param item the item to be checked
    *@throws NoSuchElementException if there is no greater element.
    **/ 
    public E successor(E item)
    {
        TreeNode<E> node;
        
        if( contains( item ) == false )             // check the item exists
            throw new NoSuchElementException("");
            
        node = find( item );                        // find TreeNode for that item
        
        if( node.getRight() == null )
            throw new NoSuchElementException("");
        
        while( node.getRight() != null )            // traverse right children of node until null
        {
            node = node.getRight();
        }
        
        return node.item;
        
    }

    /**
    *Return the least item in the Dictionary
    *@return the least element in the Dictionary
    *@throws NoSuchElementException if the Dictionary is empty.
    **/ 
    public E min()
    {
        TreeNode<E> node = aboveRoot.getLeft();
    
        if( node == null )                          // starting at the root, the left children will be recursively traversed until null
            throw new NoSuchElementException();
            
        if( node.getLeft() == null )
            return node.item;
        
        while( node.getLeft() != null )             // while node's child isn't null, continue traversing
        {
            node = node.getLeft();
        }
        
        return node.item;
    }
    
    /**
    *Return the greatest element in the dictionary
    *@return the greatest element in the Dictionary
    *@throws NoSuchElementException if the Dictionary is empty.
    **/ 
    public E max()
    {
        TreeNode<E> node = aboveRoot.getLeft();
        
        if( node == null )                          // starting at the root, the right children will be recursively traversed until null
            throw new NoSuchElementException();
    
        if( node.getLeft() == null )
            return node.item;
        
        while( node.getRight() != null )            // while node's child isn't null, continue traversing
        {
            node = node.getRight();
        }
        
        return node.item;
    }
    
    /**
    *Deletes the specified element from the Dictionary if it is present.
    *@param item the element to be removed
    *@return true if the element was in the Dictionary and has now been removed. False otherwise.
    **/
    public boolean delete(E item);
    {
        modCount++; 
        
        if( contains(item == false ) )
            return false;
            
        
        
       
    }

    /**
    *Provides a fail fast iterator for the Dictionary, starting at the least element
    *The iterator should implement all methods of the iterator class including remove
    *@return an iterator whose next element is the least element in the dictionary, and which will iterate through all the elements in the Dictionary in ascending order. 
    */
    public Iterator<E> iterator();
    {
        return new AVLTreeIterator<E>();
    }

    /**
    *Provides a fail fast iterator for the Dictionary, starting at the least element greater than or equal to start
    *The iterator should implement all methods of the iterator class including remove
    *@param start the element at which to start iterating at.
    *@return an iterator whose next element is the least element greater than or equal to start in the dictionary, and which will iterate through all the elements in the Dictionary in ascending order. 
    */
    
    public Iterator<E> iterator(E start);
    {
        return new AVLTreeIterator<E>( start );
    }
    
    

    private class AVLTreeIterator<E>
    {
        private int expectedModCount = modCount;
        private QueueBlock q;
        private int current;
        
        AVLTreeIterator()
        {
            expectedModCount = modCount;
            
            inOrderTraversal( aboveRoot.getLeft() );
            
        }
        
        AVLTreeIterator( E start )
        {
            expectedModCount = modCount;
            
            inOrderTraversal( aboveRoot.getLeft() );
            
            while( q.examine().compareTo( start ) < 0 )     // while examined item is less than start item
            {
                q.dequeue();
            }
            
        }
        
        private void addToQueue(E item)
        {
            q.enqueue( item );            
        }
        
        private void inOrderTraversal(E node)
        {   
            if( node == null )
                return;
            
            inOrderTraversal( node.getLeft() );             // recursively traverses left subtrees, does it prior to adding root node to queue
        
            dictionary = dictionary + " " + (String) node.item;   // add item to dictionary string
            
            addToQueue( node.item );                        // add root node item to queue 
            
            inOrderTraversal( node.getRight() );            // recursively traverses right subtrees, does it after adding the root to queue

        
       }
        
        public boolean hasNext()
        {
            if( expectedModCount != modCount )
                throw new ConcurrentModificationException();
            
                
            if( q.isEmpty() == false )
                return true;
            else return false;
            
        }
        
        public E next()
        {
            if( expectedModCount != modCount )
                throw new ConcurrentModificationException();
        
            q.dequeue();
            return q.examine();
            
        }
        
        public void remove(E item)
        {
            if( expectedModCount != modCount )
                throw new ConcurrentModificationException();
                
            delete( item );            
        }
        
    }

    /**
    *Provides a string describing all operations performed on the table since its construction, or since the last time getLogString was called
    * As each operation returns (either called directly on the Dictionary, or on an iterator generated by the dictionary) append a new line to the String:"Operation <name of op>(<parameter values>) completed using [n] comparisons". 
    *@return A sting listing all operations called on the Dictionary, and how many comparisons were required to complete each operation.
    **/ 
    public String getLogString()
    {
        String oldLog = log;
        
        log = "";
        
        return oldLog;

    }

    /**
    *Provides a String representation of the Dictionary
    *@return a String representation of the Dictionary
    **/
    public String toString();
    {
        dictionary = "";
        inOrderTraversal( aboveRoot.getLeft() );
        
        return dictionary;
    }
}
