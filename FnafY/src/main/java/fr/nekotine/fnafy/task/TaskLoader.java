package fr.nekotine.fnafy.task;

import org.bukkit.configuration.file.YamlConfiguration;

public class TaskLoader {
	
	public static final boolean load(YamlConfiguration config, TaskManager mnger) {
		final int nb = config.getInt("numberOfTasks", 0);
		for (BaseTask t : mnger.tasklist) {
			t.reset();
		}
		mnger.tasklist.clear();
		for (int i=0;i<nb;i++) {
			Object t = config.get("task_"+i);
			if (t!=null) {
				BaseTask bt = (BaseTask)t;
				mnger.tasklist.add(bt);
				bt.setTaskManager(mnger);
				mnger.main.pmanager.registerEvents(bt, mnger.main);
			}
		}
		return false;
	}

}
