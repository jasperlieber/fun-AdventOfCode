package com.goatwalker.aoc16;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/*
value 5 goes to bot 2
bot 2 gives low to bot 1 and high to bot 0
value 3 goes to bot 1
bot 1 gives low to output 1 and high to bot 0
bot 0 gives low to output 2 and high to output 0
value 2 goes to bot 2

 */
public class Day10Part1 
{	
	
	MyBots bots;
	Pattern pValue, pBot;
	MyOuts outs;	
	
	public static void main(String[] args) throws Exception 
	{
		Day10Part1 game = new Day10Part1();
		game.doit();
	}
	
	private void doit() throws Exception {
		init();
		String filename = "C:\\Users\\jasper\\Google Drive\\Fun\\2016AdventOfCode\\EclipseWorkspace\\"
				+ "day10.txt"; 
//				+ "day10 - Copy.txt"; 
//				+ "day10 - Copy - Copy.txt"; 

		
		try(BufferedReader br = new BufferedReader(new FileReader(filename))) {
		    String line = br.readLine();
		    
		    while (line != null) {
		    	
		    	processLine(line);
//		        System.out.println("read: " + line);
		        
		        line = br.readLine();
		    }
		    
		    percolate();
		}
	}

	private void percolate() throws Exception {
    	// example:  bot 2 gives low to bot 1 and high to output 10
		int answer = -1;
		while (true)
		{
			Map.Entry<Integer, Bot> entry = findReadyBot();
			if (entry == null) 
				break;
			
			Bot bot = entry.getValue();
			
			int low = bot.sortedChips.low();
			int high = bot.sortedChips.high();
			
			if (low == 17 && high == 61) answer = bot.id;
			bot.sortedChips.clear();
			
			Bot lowTarget = (bot.giveRule.giveLowTarget.equals("bot") 
					? bots : outs).getBot(bot.giveRule.giveLowId);
			lowTarget.sortedChips.add(low);
			
			Bot highTarget = (bot.giveRule.giveHighTarget.equals("bot") 
					? bots : outs).getBot(bot.giveRule.giveHighId);
			highTarget.sortedChips.add(high);
			
			Log.out("Percolated bot #" + entry.getKey() + " with instruction "
					+ bot.giveRule);
			Log.out(" low target: " + lowTarget);
			Log.out("high target: " + highTarget);
 
			
		}
		System.out.println("special bot = " + answer);
		
		System.out.println("Product of outs: " + outs.productOfIds());
		//bots.toString();
		
//		Log.out("" + outs.entrySet().size());
	}

	private Map.Entry<Integer, Bot> findReadyBot() {
		for (Map.Entry<Integer, Bot> entry : bots.entrySet()) {
		    Bot bot = entry.getValue();
		    if (bot.sortedChips.ready())
		    	return entry;
		}
		return null;
	}

	public class InstructionDay10 {
		@Override
		public String toString() {
			return "Give rule: botId=" + botId + ", giveLowTarget="
					+ giveLowTarget + ", giveLowId=" + giveLowId
					+ ", giveHighTarget=" + giveHighTarget + ", giveHighId="
					+ giveHighId ;
		}
		public int giveLowId, giveHighId;
		public String giveLowTarget, giveHighTarget;
		public int botId;
	}

	public class ChipSet {
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			String out = "[chips =";
			for (Integer chip : chips)
				out += " " + chip;
			out += "]";
			return out;
		}

		private TreeSet<Integer> chips = new TreeSet<Integer>();
	
		public int low() throws Exception {
			if (chips.size() != 2) throw new Exception("bad low");
			return chips.first();
		}
	
		public int single() throws Exception {
			if (chips.size() != 1) throw new Exception("bad single");
			return chips.first();
		}
	
		public int high() throws Exception {
			if (chips.size() != 2) throw new Exception("bad high");
			return chips.last();
		}
	
		public void add(int low) throws Exception {
			if (chips.size() > 1) throw new Exception("bad add");
			chips.add(low);
		}
	
		public void clear() {
			chips.clear();
		}

		public boolean ready() {
			return chips.size() == 2;
		}
	}

	
	public class Bot {//{implements Comparable<Bot>{
		int id;
		ChipSet sortedChips = new ChipSet();
		public InstructionDay10 giveRule;
		
		public Bot(int botId) { 
			id = botId; 
		}

		@Override
		public String toString() {
			String out = "[bot" + id + " " + sortedChips + "]";
			return out;
			
		}
	}
	
	public static class Log
	{
		static boolean verbose = true;
		public static void out(String string) {
			if (verbose) System.out.println(string);
			
		}
		
	}
	
	public class MyOuts extends MyBots {
		protected static final String botOrOutput = "outputs";
	}

	public class MyBots 
	{
		protected static final String botOrOutput = "bots";
		@Override
		public String toString() {
			String out = "[myBotMap contains " + botOrOutput + ": ";
			for (Bot bot : myBotMap.values()) {
			    out += bot;
			}
			out += "]";
			Log.out(out);
			return out;
		}
		
		public long productOfIds() throws Exception
		{
			long product = getBot(0).sortedChips.single()
					*  getBot(1).sortedChips.single()
					*  getBot(2).sortedChips.single();
			
			return product;
		}

		private Map<Integer, Bot> myBotMap = new HashMap<Integer, Bot>(); 

		public void botGetsChip(int botId, int chip) throws Exception
		{
			Bot bot = getBot(botId);
			bot.sortedChips.add(chip);
			Log.out("Gave " + chip + " to " + bot);
		}
		
		public Set<Entry<Integer, Bot>> entrySet() {
			return myBotMap.entrySet();
		}

		public Bot getBot(int botId) {
			Bot bot = myBotMap.get(botId);
			if (bot == null)
			{
				bot = new Bot(botId);
				myBotMap.put(botId, bot);
			}
			return bot;
		}

		public void botGiveRule(InstructionDay10 instruction) {
        	// bot 2 gives low to bot 1 and high to bot 0
        	Bot bot = bots.getBot(instruction.botId);
        	bot.giveRule = instruction;
		}
	}


	private void init() {
		bots = new MyBots();
		outs = new MyOuts();
		pValue = Pattern.compile("value (\\d+) goes to bot (\\d+)");
		pBot   = Pattern.compile("bot (\\d+) gives low to (\\w+) (\\d+) " 
				+ "and high to (\\w+) (\\d+)");
	}
	
	private void processLine(String line) throws Exception 
	{
		
        Matcher matcher;
        
        InstructionDay10 instruction = new InstructionDay10();
        
        if ((matcher = pValue.matcher(line)).find())
        {
        	// value 5 goes to bot 2
        	if (matcher.groupCount() != 2) 
        		throw new Exception("cnt = " + matcher.groupCount());
        	int chipId = Integer.parseInt(matcher.group(1));
        	int botId  = Integer.parseInt(matcher.group(2));

			bots.botGetsChip(botId,chipId);
        }
        else if ((matcher = pBot.matcher(line)).find())
        {
        	// bot 2 gives low to bot 1 and high to bot 0
        	if (matcher.groupCount() != 5) 
        		throw new Exception("cnt = " + matcher.groupCount());
        	instruction.botId = Integer.parseInt(matcher.group(1));
        	instruction.giveLowTarget = matcher.group(2);
        	instruction.giveLowId = Integer.parseInt(matcher.group(3));
        	instruction.giveHighTarget = matcher.group(4);
        	instruction.giveHighId = Integer.parseInt(matcher.group(5));
        	
        	if (!instruction.giveLowTarget.equals("bot") && !instruction.giveLowTarget.equals("output") 
        	 || !instruction.giveHighTarget.equals("bot") && !instruction.giveHighTarget.equals("output"))
        		throw new Exception("bad target");
        	
        	bots.botGiveRule(instruction);      
        	System.out.println("read " + instruction);
        }
        else throw new Exception("bad instruction: " + line);
    	
        
	}
	

}
