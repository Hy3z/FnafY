package fr.nekotine.fnafy.room;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.FluidCollisionMode;
import org.bukkit.entity.Player;

import fr.nekotine.fnafy.FnafYMain;
import fr.nekotine.fnafy.animation.ASAnimation;
import fr.nekotine.fnafy.enums.Animatronic;
import fr.nekotine.fnafy.utils.BlockSelection;

public class RoomMinimap{
	private final String roomName;
	private final BlockSelection AftonSurface;
	private final BlockSelection AftonOutline;
	private final BlockSelection GuardOutline;
	private final BlockSelection GuardSurface;
	private final HashMap<Animatronic,List<ASAnimation>> MinimapPoses=new HashMap<Animatronic,List<ASAnimation>>();
	private List<Player> playerInAftonRoom = new ArrayList<>();
	private List<Player> playerInGuardRoom = new ArrayList<>();
	public RoomMinimap(String roomName, FnafYMain main, BlockSelection AftonSurface, BlockSelection AftonOutline, BlockSelection GuardSurface, BlockSelection GuardOutline,
			HashMap<Animatronic,List<ASAnimation>> MinimapPoses) {
		this.roomName=roomName;
		this.AftonSurface=AftonSurface;
		this.AftonOutline=AftonOutline;
		this.GuardOutline=GuardOutline;
		this.GuardSurface=GuardSurface;
		this.MinimapPoses.putAll(MinimapPoses);
	}
	public void playerMoveHeadEvent(Player p) {
		if(playerInAftonRoom.contains(p)) {
			if(!AftonSurface.isOneSelected(p.getTargetBlockExact(50, FluidCollisionMode.NEVER).getLocation())) {
				AftonOutline.disOutline(p);
				playerInAftonRoom.remove(p);
			}
			return;
		}else if(AftonSurface.isOneSelected(p.getTargetBlockExact(50, FluidCollisionMode.NEVER).getLocation())){
			AftonOutline.outline(p);
			playerInAftonRoom.add(p);
			return;
		}
		if(playerInGuardRoom.contains(p)) {
			if(!GuardSurface.isOneSelected(p.getTargetBlockExact(50, FluidCollisionMode.NEVER).getLocation())) {
				GuardOutline.disOutline(p);
				playerInGuardRoom.remove(p);
			}
			return;
		}else if(GuardSurface.isOneSelected(p.getTargetBlockExact(50, FluidCollisionMode.NEVER).getLocation())){
			GuardOutline.outline(p);
			playerInGuardRoom.add(p);
			return;
		}
	}
	public String getRoomName() {
		return roomName;
	}
	public List<ASAnimation> getAnimatronicAnimations(Animatronic anim){
		return MinimapPoses.get(anim);
	}
	public BlockSelection getAftonSurface() {
		return AftonSurface;
	}
	public BlockSelection getAftonOutline() {
		return AftonOutline;
	}
	public BlockSelection getGuardOutline() {
		return GuardOutline;
	}
	public BlockSelection getGuardSurface() {
		return GuardSurface;
	}
}
