package fr.nekotine.fnafy.task;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import fr.nekotine.fnafy.FnafYMain;
import fr.nekotine.fnafy.events.GameStartEvent;
import fr.nekotine.fnafy.events.GameStopEvent;

public class TaskManager implements Listener {
	
	private final FnafYMain main;
	public final ArrayList<BaseTask> tasklist=new ArrayList<BaseTask>();
	private boolean commonComplete=false;
	private boolean epicComplete=false;
	private boolean legendaryComplete=false;
	private boolean rareComplete=false;
	private byte commonAsked=0;
	private byte commonNotAsked=0;
	private byte epicAsked=0;
	private byte epicNotAsked=0;
	private byte legendaryAsked=0;
	private byte legendaryNotAsked=0;
	private byte rareAsked=0;
	private byte rareNotAsked=0;
	private byte commonAskedDone=0;
	private byte commonNotAskedDone=0;
	private byte epicAskedDone=0;
	private byte epicNotAskedDone=0;
	private byte legendaryAskedDone=0;
	private byte legendaryNotAskedDone=0;
	private byte rareAskedDone=0;
	private byte rareNotAskedDone=0;
	private boolean tasksDone=false;
	
	public TaskManager(FnafYMain mainref) {
		main=mainref;
	}
	
	private boolean loadTasks() {
		return TaskLoader.load(main.getYamlReader().getConfig(main.getMapName(), "tasks"),tasklist);
	}
	
	private void shuffleAskedTasks() {
		byte nbCommon=0;
		byte nbRare=0;
		byte nbEpic=0;
		byte nbLegendary=0;
		for (BaseTask t : tasklist) {
			switch (t.getDifficulty()) {
			case COMMON:
				nbCommon++;
				break;
			case EPIC:
				nbEpic++;
				break;
			case LEGENDARY:
				nbLegendary++;
				break;
			case RARE:
				nbRare++;
				break;
			default:
				break;
			}
		}
		byte askedNbCommon=(byte)(nbCommon/2);
		byte askedNbRare=(byte)(nbRare/2);
		byte askedNbEpic=(byte)(nbEpic/2);
		byte askedNbLegendary=(byte)(nbLegendary/2);
		Random r = new Random(System.currentTimeMillis());
		for (BaseTask t : tasklist) {
			switch (t.getDifficulty()) {
			case COMMON:
				t.setAsked(r.nextInt(1000)<=1000*(askedNbCommon/nbCommon));
				nbCommon--;
				break;
			case EPIC:
				t.setAsked(r.nextInt(1000)<=1000*(askedNbEpic/nbEpic));
				nbEpic--;
				break;
			case LEGENDARY:
				t.setAsked(r.nextInt(1000)<=1000*(askedNbLegendary/nbLegendary));
				nbLegendary--;
				break;
			case RARE:
				t.setAsked(r.nextInt(1000)<=1000*(askedNbRare/nbRare));
				nbRare--;
				break;
			default:
				break;
			}
		}
	}
	
	private void tasksCheck() {
		commonComplete = commonAsked<=commonAskedDone || commonNotAsked<=commonNotAskedDone;
		epicComplete = epicAsked<=epicAskedDone || epicNotAsked<=epicNotAskedDone;
		legendaryComplete = legendaryAsked<=legendaryAskedDone || legendaryNotAsked<=legendaryNotAskedDone;
		rareComplete = rareAsked<=rareAskedDone || rareNotAsked<=rareNotAskedDone;
		setTasksDone(commonComplete && epicComplete && legendaryComplete && rareComplete);
	}
	
	private void showScoreboard() {
		
	}
	
	public void completeTask(BaseTask task) {
		switch (task.getDifficulty()) {
		case COMMON:
			if (task.isAsked()) {
				commonAskedDone++;
			}else {
				commonNotAskedDone++;
			}
			break;
		case EPIC:
			if (task.isAsked()) {
				epicAskedDone++;
			}else {
				epicNotAskedDone++;
			}
			break;
		case LEGENDARY:
			if (task.isAsked()) {
				legendaryAskedDone++;
			}else {
				legendaryNotAskedDone++;
			}
			break;
		case RARE:
			if (task.isAsked()) {
				rareAskedDone++;
			}else {
				rareNotAskedDone++;
			}
			break;
		default:
			break;
		}
		tasksCheck();
	}
	
	@EventHandler
	public void onGameStart(GameStartEvent evt) {
		if (loadTasks()) {
			shuffleAskedTasks();
			commonComplete=false;
			epicComplete=false;
			legendaryComplete=false;
			rareComplete=false;
			commonAsked=0;
			commonNotAsked=0;
			epicAsked=0;
			epicNotAsked=0;
			legendaryAsked=0;
			legendaryNotAsked=0;
			rareAsked=0;
			rareNotAsked=0;
			commonAskedDone=0;
			commonNotAskedDone=0;
			epicAskedDone=0;
			epicNotAskedDone=0;
			legendaryAskedDone=0;
			legendaryNotAskedDone=0;
			rareAskedDone=0;
			rareNotAskedDone=0;
			tasksDone=false;
		}else {
			evt.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onGameStop(GameStopEvent evt) {
		for (BaseTask t : tasklist) {
			t.reset();
		}
		tasklist.clear();
	}

	public boolean isTasksDone() {
		return tasksDone;
	}

	public void setTasksDone(boolean tasksDone) {
		this.tasksDone = tasksDone;
	}
	
}
