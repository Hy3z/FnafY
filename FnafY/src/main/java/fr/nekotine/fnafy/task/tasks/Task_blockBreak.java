package fr.nekotine.fnafy.task.tasks;

import java.util.ArrayList;
import java.util.Map;

import org.bukkit.Bukkit;

import fr.nekotine.fnafy.task.BaseTask;
import fr.nekotine.fnafy.utils.SerializedBlock;

public class Task_blockBreak extends BaseTask {
	
	private final ArrayList<SerializedBlock> blocks = new ArrayList<SerializedBlock>();
	
	public Task_blockBreak(String n) {
		super(n);
	}
	
	public boolean addBlock(SerializedBlock b) {
		if (!blocks.contains(b)) {
			blocks.add(b);
			return true;
		}
		return false;
	}

	@Override
	public Difficulty getDifficulty() {
		return Difficulty.LEGENDARY;
	}

	@Override
	public void setup() {
		for (SerializedBlock b : blocks) {
			b.place();
		}
	}

	@Override
	public void reset() {
		for (SerializedBlock b : blocks) {
			b.place();
		}
	}

	@Override
	public Map<String, Object> serialize() {
		return null;
	}
	
	public static Task_blockBreak deserialize(Map<String, Object> args) {
		Task_blockBreak b = new Task_blockBreak((String)args.get("name"));
		int nb_block = (int)args.get("nb_bl");
		for (int i=0;i<nb_block;i++) {
			if (!b.addBlock((SerializedBlock)args.get("block_"+i))) {
				Bukkit.getLogger().warning("adding multiple times block_"+i+" to "+b.getName());
			}
		}
		return b;
	}
	
}
