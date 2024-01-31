package com.goatwalker.aoc16;

import java.util.HashSet;
import java.util.LinkedList;

import com.goatwalker.aoc16.Day10Part1.Log;


public class Day13Part1 
{	
	private static final int designersFave = 1358;
	
	public class MappedRooms {
		private HashSet<Room> mappedRoomSet = new HashSet<Room>();

		public void add(Room room) {
			mappedRoomSet.add(room);
		}

		public boolean contains(Room room) {
			return mappedRoomSet.contains(room);
		}

		public int roomCount() {
			int cnt = 0;
			for (Room room : mappedRoomSet)
				if (room.isRoom()) cnt++;
			return cnt;
		}

	}

//	public boolean foundTarget;
//	private Room target = new Room(31,39);
	
	public class Room {
		int x, y;
		int dist = -1;
		
		public Room(int xx, int yy) {
			x = xx;
			y = yy;
		}

		boolean isRoom() {
			if (x < 0 || y < 0) return false;
			long z = x*x + 3*x + 2*x*y + y + y*y + designersFave;
			int numOneBits = getNumOneBits(z);
			return numOneBits % 2 == 0;
		}

		int getNumOneBits(long z) {
			int numOneBits = 0;
			while (z > 0)
			{
				if (z%2 == 1) numOneBits++;
				z /= 2;
			}
			return numOneBits;
		}

		public void expand() throws Exception {
			
			Log.out("Expanding " + this);
			
//			if (mappedRooms.contains(this))
//				throw new Exception("already explored");

			mappedRooms.add(this);
			
			if (x+1 >= maxx) maxx = x+2;
			if (y+1 >= maxy) maxy = y+2;
			
			Room[] neighbors = new Room[] {
					new Room(x-1,y),
					new Room(x+1,y),
					new Room(x,y-1),
					new Room(x,y+1)
			};
			
			for (Room neighbor : neighbors)
			{
				neighbor.explore(dist);
			}
			
//			if (this.equals(target))
//			{
//				foundTarget = true;
//			}

		}

		private void explore(int callersDist) throws Exception 
		{
			boolean isRoom = isRoom();
			
			if (!mappedRooms.contains(this))
			{
				if (expandableRooms.contains(this))
				{
					throw new Exception("2nd expandable");
				}
				mappedRooms.add(this);
				if (isRoom && callersDist < 49) 
					expandableRooms.add(this);
			}

			if (isRoom()) 
			{
				if (dist == -1 || dist > callersDist + 1) 
					dist = callersDist + 1;
			}
		}

		private Day13Part1 getOuterType() {
			return Day13Part1.this;
		}
		@Override
		public String toString() {
			return "[room " + x + " " + y + " (" + dist + ")]";
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + x;
			result = prime * result + y;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Room other = (Room) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (x != other.x)
				return false;
			if (y != other.y)
				return false;
			return true;
		}

	}

	public static void main(String[] args) throws Exception 
	{
		Day13Part1 game = new Day13Part1();
		game.doit();
	}

	private LinkedList<Room> expandableRooms = new LinkedList<Room>();
	private MappedRooms mappedRooms = new MappedRooms();
	private int maxx=0;
	private int maxy=0;
	
	private void doit() throws Exception {
		
		init();
		

//		Room r = new Room();
//		System.out.println(r.getNumOneBits(1));
//		System.out.println(r.getNumOneBits(2));
//		System.out.println(r.getNumOneBits(7));
//		System.out.println(r.getNumOneBits(8));
//		System.out.println(r.getNumOneBits(9));
		int cnt = 0;
		while(true)//!foundTarget)
		{
			cnt++;
			if (foundAllPaths()) break;
			System.out.println("Iteration #" + cnt);
			showMappedRooms();
			if (!growPaths()) {
				System.out.println("couldn't grow more");
				break;
			}

//			if (cnt > 6) break;
		}
		
//		System.out.println("Found target " + foundTarget);
		
		System.out.println(mappedRooms.roomCount());
	}

	private void showMappedRooms() {
		System.out.print("  ");
		for (int xx=0; xx <= maxx; xx++)
		{
			System.out.print("" + ((xx%10 == 0) ? ((int)(xx/10)%10) : " "));
		}
		System.out.println();
		System.out.print("  ");
		for (int xx=0; xx <= maxx; xx++)
		{
			System.out.print(xx%10);
		}
		System.out.println();
		
		for (int yy=0; yy <= maxy; yy++)
		{
			System.out.format("%02d ", yy % 100);
			for (int xx=0; xx < maxx; xx++)
			{
				Room r = new Room(xx,yy);
				System.out.print(mappedRooms.contains(r) 
						? (r.isRoom() ? '.' : '#')
					    : " ");
			}
			System.out.println();
		}		
		System.out.println();
	}

	private void init() {
		Room start = new Room(1,1);
		start.dist = 0;
		expandableRooms.add(start);
		
	}

	private boolean foundAllPaths() {
		return false;//mappedRooms.contains(new Room(7,4));
	}

	private boolean growPaths() throws Exception {
//		Room room;
		if (expandableRooms.size() == 0) return false;
		Room room = expandableRooms.removeFirst();
		room.expand();
		return true;
	}

}
