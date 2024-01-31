package com.goatwalker.aoc16;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;


public class Day16
{
    public static final String root = 
//            "hijkl";
//            "ihgpwlah";
    "udskfozm";
    
    static final String dirs = "UDLR";
    static final int maxRow = 3;
    static final int maxCol = 3;
    
    static MessageDigest md5;
    
    public static void main(String[] args) throws Exception
    {
        md5 = MessageDigest.getInstance("MD5");
        Day16 game = new Day16();
        game.doit();
    }


/*

frontier = PriorityQueue()
frontier.put(start, 0)
came_from = {}
cost_so_far = {}
came_from[start] = None
cost_so_far[start] = 0

while not frontier.empty():
   current = frontier.get()

   if current == goal:
      break
   
   for next in graph.neighbors(current):
      new_cost = cost_so_far[current] + graph.cost(current, next)
      if next not in cost_so_far or new_cost < cost_so_far[next]:
         cost_so_far[next] = new_cost
         priority = new_cost + heuristic(goal, next)
         frontier.put(next, priority)
         came_from[next] = current

*/    
    private void doit() throws Exception
    {
        PriorityQueue<Room> frontier = new PriorityQueue<Room>();
        
        Room start = new Room(0, 0, "");
        frontier.add(start);
        
        String longest = "";
        
        Room room;
        
        while ((room = frontier.poll()) != null)
        {
            if (room.isGoal())
            {
                if (room.path.length() > longest.length())
                    longest = room.path;
                System.out.println("Found " + room.path + ", longest = " + longest);
                continue;
            }
//                break;
            
            List<Room> neighbors = room.getNeighbors();
            for (Room next : neighbors)
            {
                next.priority = next.costSoFar + next.heuristic();
                frontier.add(next);
            }
        }
        
        System.out.println("longest len = " + longest.length());
//        if (room != null)
//            System.out.println("Shortest path is " + room.path);
//        else
//            System.out.println("No path found");
    }

    public class Room implements Comparable<Room>
    {
        int row,col;
        int priority = 0;
        Room cameFrom = null;
        int costSoFar = 0;
        String path = "";
        
        public Room(int cc, int rr, String pp)
        {
            row = rr;
            col = cc;
            path = pp;
            costSoFar = path.length();
        }

        public int heuristic()
        {
            return maxCol - col + maxRow - row;
        }

        /*
         * The doors in your current room are either open or closed (and locked)
         * based on the hexadecimal MD5 hash of a passcode (your puzzle input)
         * followed by a sequence of uppercase characters representing the path
         * you have taken so far (U for up, D for down, L for left, and R for
         * right).
         * 
         * Only the first four characters of the hash are used; they represent,
         * respectively, the doors up, down, left, and right from your current
         * position. Any b, c, d, e, or f means that the corresponding door is
         * open; any other character (any number or a) means that the
         * corresponding door is closed and locked.
         */
        public List<Room> getNeighbors()
        {
            List<Room> rooms = new ArrayList<Room>();

            String md5 = getMd5(root + path);
//            System.out.format("[%d %d] has path %s with hash %s\n",
//                    col,row,path,md5.substring(0,4));

            for (int jj = 0; jj < 4; jj++)
            {
                char dir = dirs.charAt(jj);
                int row2 = row;
                int col2 = col;
                if (dir == 'U' && --row2 < 0) continue;
                if (dir == 'D' && ++row2 > maxRow) continue;
                if (dir == 'L' && --col2 < 0) continue;
                if (dir == 'R' && ++col2 > maxCol) continue;
                
                char hexChar = md5.charAt(jj);
                
                boolean open =  hexChar == 'b' || 
                                hexChar == 'c' || 
                                hexChar == 'd' || 
                                hexChar == 'e' || 
                                hexChar == 'f';
//                System.out.format(" and so [%d %d] is %s\n",
//                        col2, row2, open ? "open" : "closed");
                
                if (open)
                {
                    Room newRoom = new Room(col2, row2, path + dir);
                    rooms.add(newRoom);
                }
            }
            return rooms;
        }

        public boolean isGoal()
        {
            return row == 3 && col == 3;
        }

        @Override
        public int compareTo(Room o)
        {
            return (int) Math.signum(priority-o.priority);
        }
    }

    String getMd5(String tst)// throws NoSuchAlgorithmException {
    {
        md5.update(StandardCharsets.UTF_8.encode(tst));
        String res = String.format("%032x", new BigInteger(1, md5.digest()));
        
        return res;
    }

}
