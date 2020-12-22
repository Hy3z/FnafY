package fr.nekotine.fnafy.task;

import java.util.ArrayList;

import org.bukkit.configuration.file.YamlConfiguration;

public class TaskLoader {
	
	public static final boolean load(YamlConfiguration config, ArrayList<BaseTask> tasklist) {
		final int nb = config.getInt("numberOfTasks", 0);
		for (BaseTask t : tasklist) {
			t.reset();
		}
		tasklist.clear();
		for (int i=0;i<nb;i++) {
			Object t = config.get("task_"+i);
			if (t!=null) {tasklist.add((BaseTask)t);}
		}
		return false;
	}

}
