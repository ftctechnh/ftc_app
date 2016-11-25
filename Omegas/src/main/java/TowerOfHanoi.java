import static java.lang.System.out;
import java.util.Stack;

public class TowerOfHanoi
{
    // Create the 3 towers
    Stack<Integer> t1 = new Stack<Integer>();
    Stack<Integer> t2 = new Stack<Integer>();
    Stack<Integer> t3 = new Stack<Integer>();

    /**
     * Print all 3 towers
     */
    void printTowers()
    {
        out.println("- T1:"); printTower(t1);
        out.println("- T2:"); printTower(t2);
        out.println("- T3:"); printTower(t3);
        out.println("--------------------");

    }

    /**
     * Print the given tower
     *
     * @param t
     */
    void printTower(Stack<Integer> t)
    {
        out.println(t);
    }

    /**
     * Populate given tower with the specified number of disks placing largest
     * disk in first and then the next smaller and so on
     *
     * @param t
     * @param n
     */
    void populate(Stack<Integer> t, int n)
    {
        for (int i=n; i>0; i--)
        {
            t.push(i);
        }
    }

    /**
     * Solve ToH by moving d disks from source to dest using buffer
     *
     * @param d
     * @param source
     * @param dest
     * @param buffer
     */
    void solve(int d, Stack<Integer> source, Stack<Integer> dest, Stack<Integer> buffer)
    {
        if (d==1)
        {
            moveDisk(source, dest);
        }
        else
        {
            solve(d-1, source, buffer, dest);
            moveDisk(source, dest);
            solve(d-1, buffer, dest, source);
        }
    }

    /**
     * Move the top disk from source to dest and prints the towers
     *
     * @param source
     * @param dest
     */
    private void moveDisk(Stack<Integer> source, Stack<Integer> dest)
    {
        Integer i = source.pop();
        dest.push(i);
        printTowers();
    }

    /**
     * Runs the ToH algorithm for given number of disks
     * @param disks
     */
    void run(int disks)
    {
        // Populate first tower with 10 disks
        populate(t1, disks);

        // Print Towers, solve, print solution
        printTowers();
        solve(disks, t1, t3, t2);

    }


    /**
     * Main - Instantiate class and kicks off the run() method
     *
     * @param args
     */
    public static void main(String[] args)
    {
        TowerOfHanoi toh = new TowerOfHanoi();
        toh.run(5);
    }

}
 