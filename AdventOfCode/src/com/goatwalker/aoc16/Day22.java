package com.goatwalker.aoc16;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.goatwalker.utils.Pair;


public class Day22 
{

    static final String filename = "C:\\Users\\jasper\\Google Drive\\Fun\\2016AdventOfCode\\EclipseWorkspace\\"
//          + "day08part1_test.txt";
//          + "day08part1.txt";
            + "day22.txt"; 
    
	public static void main(String[] args) throws Exception 
	{
		Day22 game = new Day22();
		game.doit();
	}
	
	Set<Node> nodes = new HashSet<Node>();
	Set<Pair<Node,Node>> nodePairs = new HashSet<Pair<Node,Node>>();
	
	private void doit() throws Exception 
	{
		init();
		
		try(BufferedReader br = new BufferedReader(new FileReader(filename))) {
		    String line;

		    while ((line = br.readLine()) != null) {
		    	
//                System.out.println("line: " + line);
		    	Node node = processLine(line);
		    	
		    	if (node == null) continue;
		    	boolean notDupe = nodes.add(node);
		    	if (!notDupe) throw new Exception("dupe: " + line);
		    }
        }

        /* 
         * Rules:
         * 
         * Node A is not empty (its Used is not zero).
         * Nodes A and B are not the same node.
         * The data on node A (its Used) would fit on node B (its Avail).
         */

		for (Node aa : nodes)
		{
		    if (aa.used == 0) continue;
		    for (Node bb : nodes)
		    {
		        if (aa.equals(bb)) continue;
		        if (aa.used > bb.avail) continue;
		        nodePairs.add(new Pair<Node,Node>(aa,bb));
		        
		    }
		}
		
		System.out.println(nodePairs.size());
		
	}
	
	Pattern pat;

	private void init() 
	{
	    // Filesystem              Size  Used  Avail  Use%
	    // /dev/grid/node-x0-y0     94T   67T    27T   71%
        pat = Pattern.compile(
          "/dev/grid/node-x(\\d+)-y(\\d+)\\s+(\\d+)T\\s+(\\d+)T\\s+(\\d+)T");
	}

	private Node processLine(String line) throws Exception 
	{
        Matcher matcher = null;
        boolean found = false;
        
        found = (matcher = pat.matcher(line)).find();
            //System.out.println("found: " + line);
//        else
//            System.out.println("not found: " + line);
        
        if (!found) return null;
        
        int xx = Integer.parseInt(matcher.group(1));
        int yy = Integer.parseInt(matcher.group(2));
        int size = Integer.parseInt(matcher.group(3));
        int used = Integer.parseInt(matcher.group(4));
        int avail = Integer.parseInt(matcher.group(5));
        
        Node node = new Node(xx,yy,size,used,avail);
        
        return node;
        
//        System.out.println(node);
	}

    public class Node
    {

        int xx, yy, size, used, avail;

        public Node(int xx2, int yy2, int size2, int used2, int avail2)
        {
            xx = xx2; yy = yy2; size = size2; used = used2; avail = avail2;
        }

        @Override
        public String toString()
        {
            return "[xx=" + xx + ", yy=" + yy + ", size=" + size
                    + ", used=" + used + ", avail=" + avail + "]";
        }

        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + getOuterType().hashCode();
            result = prime * result + avail;
            result = prime * result + size;
            result = prime * result + used;
            result = prime * result + xx;
            result = prime * result + yy;
            return result;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (this == obj) return true;
            if (obj == null) return false;
            if (getClass() != obj.getClass()) return false;
            Node other = (Node) obj;
            if (!getOuterType().equals(other.getOuterType())) return false;
            if (avail != other.avail) return false;
            if (size != other.size) return false;
            if (used != other.used) return false;
            if (xx != other.xx) return false;
            if (yy != other.yy) return false;
            return true;
        }


        private Day22 getOuterType()
        {
            return Day22.this;
        }
    }
}
